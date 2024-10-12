package ru.gozerov.presentation.screens.voice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gozerov.core.di.biometricComponentHolder
import ru.gozerov.core.recycler.smoothScrollToPositionWithCustomSpeed
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.FragmentVoiceBinding
import ru.gozerov.presentation.screens.voice.models.VoiceAction
import ru.gozerov.presentation.screens.voice.models.VoiceEvent
import java.io.IOException
import javax.inject.Inject

class VoiceFragment : Fragment() {

    private var _binding: FragmentVoiceBinding? = null
    private val binding: FragmentVoiceBinding get() = _binding!!

    @Inject
    lateinit var factory: VoiceViewModel.Factory
    private val viewModel: VoiceViewModel by viewModels { factory }

    private val numberAdapter = NumberPagerAdapter()

    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String = ""

    private var numbersJob: Job? = null
    private var waveformJob: Job? = null

    override fun onAttach(context: Context) {
        val component = context.biometricComponentHolder
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoiceBinding.inflate(inflater, container, false)

        val step = arguments?.getInt(ARG_STEP) ?: throw IllegalStateException("No args provided")
        viewModel.obtainEvent(VoiceEvent.Initialize(step, arguments?.getString(ARG_FAIL)))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestPermissions()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.viewStates().collect { viewState ->
                        binding.txtCurrentStep.text =
                            getString(R.string.recording_is, viewState.step)
                        binding.txtHint.text =
                            viewState.fail ?: getString(R.string.default_voice_hint)

                        binding.imgHint.visibility =
                            if (viewState.fail != null) View.VISIBLE else View.GONE
                        numberAdapter.data = viewState.numbers

                        binding.captureVoice.setOnClickListener {
                            if (!viewState.isCapturing) {
                                viewModel.obtainEvent(VoiceEvent.StartRecording)
                            } else {
                                stopRecording()
                            }
                        }

                        if (viewState.isFinish) {
                            binding.captureVoice.visibility = View.GONE
                            binding.sendRecording.visibility = View.VISIBLE
                            binding.retry.visibility = View.VISIBLE
                        } else {
                            binding.captureVoice.visibility = View.VISIBLE
                            binding.sendRecording.visibility = View.GONE
                            binding.retry.visibility = View.GONE
                        }

                        binding.retry.setOnClickListener {
                            viewModel.obtainEvent(VoiceEvent.StartRecording)
                        }

                        binding.sendRecording.setOnClickListener {
                            findNavController().navigate(R.id.action_voiceFragment_to_recordingListFragment)
                        }
                    }
                }

                launch {
                    viewModel.viewActions().collect { viewAction ->
                        when (viewAction) {
                            is VoiceAction.OnStartRecording -> {
                                binding.waveformView.clear()
                                startRecording(viewAction.step)
                                startNumbers(viewAction.numbers)
                            }

                            is VoiceAction.OnStopRecording -> {

                            }

                            null -> {}
                        }
                    }
                }
            }
        }

        binding.numbersList.adapter = numberAdapter
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.numbersList)

        binding.exitButton.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.startPageFragment, true)
                .setLaunchSingleTop(true).build()
            findNavController().navigate(
                resId = R.id.action_voiceFragment_to_startPageFragment,
                args = null,
                navOptions = navOptions
            )
        }

    }

    private fun startNumbers(data: List<SelectableNumber>) {
        numbersJob = viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repeat(data.size) { pos ->
                    _binding?.let {
                        binding.root.post {
                            _binding?.let {
                                binding.numbersList.smoothScrollToPositionWithCustomSpeed(pos)
                                numberAdapter.data = data.mapIndexed { ind, number ->
                                    if (ind == pos) number.copy(isSelected = true) else number.copy(
                                        isSelected = false
                                    )
                                }
                            }
                        }
                        delay(1200)
                    }
                }
                finishRecording()
            }
        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        }
    }

    private fun initRecorder(step: Int) {
        mediaRecorder = MediaRecorder()
        audioFilePath = "${requireActivity().externalCacheDir?.absolutePath}/record_$step.aac"

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFilePath)
        }
    }

    private fun startRecording(step: Int) {
        try {
            initRecorder(step)
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            waveformJob = viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    while (true) {
                        _binding?.let {
                            binding.root.post {
                                _binding?.let {
                                    binding.waveformView.addAmplitude(
                                        mediaRecorder?.maxAmplitude?.plus(50f) ?: 0f
                                    )
                                }
                            }
                            delay(100)
                        }
                    }
                }
            }

            binding.captureVoice.text = getString(R.string.stop_recording)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            reset()
            release()
        }
        waveformJob?.cancel()
        mediaRecorder = null
        numbersJob?.cancel()

        binding.captureVoice.text = getString(R.string.start_recording)

        viewModel.obtainEvent(VoiceEvent.StopRecording)

        binding.waveformView.clear()
        binding.numbersList.smoothScrollToPosition(0)
        numberAdapter.data =
            numberAdapter.data.mapIndexed { ind, n -> SelectableNumber(n.number, ind == 0) }
    }

    private fun finishRecording() {
        mediaRecorder?.apply {
            stop()
            reset()
            release()
        }
        waveformJob?.cancel()
        mediaRecorder = null
        numbersJob?.cancel()

        viewModel.obtainEvent(VoiceEvent.FinishRecording)
    }


    override fun onDestroyView() {
        stopRecording()
        _binding = null

        super.onDestroyView()
    }

    companion object {

        const val ARG_STEP = "step"
        const val ARG_FAIL = "fail"

    }

}