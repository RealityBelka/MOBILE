package ru.gozerov.presentation.screens.voice

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gozerov.presentation.databinding.FragmentVoiceBinding
import java.io.IOException

class VoiceFragment : Fragment() {

    private var _binding: FragmentVoiceBinding? = null
    private val binding: FragmentVoiceBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestPermissions()
        startRecording()
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

    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String = ""

    var task: Job? = null

    private fun initRecorder() {
        mediaRecorder = MediaRecorder()
        audioFilePath = "${requireActivity().externalCacheDir?.absolutePath}/audio_record.3gp"

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFilePath)
        }
    }

    private fun startRecording() {
        try {
            initRecorder()
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            task = CoroutineScope(Dispatchers.IO).launch {
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
            Toast.makeText(requireContext(), "Recording started", Toast.LENGTH_SHORT).show()
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
        task?.cancel()
        mediaRecorder = null
        _binding?.let {
            binding.waveformView.clear()
            Toast.makeText(requireContext(), "Recording saved at $audioFilePath", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onDestroyView() {
        _binding = null
        stopRecording()
        super.onDestroyView()
    }

}