package ru.gozerov.presentation.screens.recording_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.gozerov.core.di.biometricComponentHolder
import ru.gozerov.domain.models.VoiceRecording
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.FragmentRecordingListBinding
import ru.gozerov.presentation.screens.recording_list.models.RecordingListAction
import ru.gozerov.presentation.screens.recording_list.models.RecordingListEvent
import ru.gozerov.presentation.screens.voice.VoiceFragment
import javax.inject.Inject

class RecordingListFragment : Fragment() {

    private var _binding: FragmentRecordingListBinding? = null
    private val binding: FragmentRecordingListBinding get() = _binding!!

    @Inject
    lateinit var factory: RecordingListViewModel.Factory
    private val viewModel: RecordingListViewModel by viewModels { factory }

    private val adapter = RecordingListAdapter { step, fail ->
        val args = bundleOf(VoiceFragment.ARG_STEP to step, VoiceFragment.ARG_FAIL to fail)
        val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
        findNavController()
            .navigate(
                resId = R.id.action_recordingListFragment_to_voiceFragment,
                args = args,
                navOptions = navOptions
            )
    }

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
        _binding = FragmentRecordingListBinding.inflate(inflater, container, false)

        viewModel.obtainEvent(RecordingListEvent.GetVoices)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recordingList.adapter = adapter

        binding.exitButton.setOnClickListener {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.startPageFragment, true).build()
            findNavController().navigate(
                resId = R.id.action_recordingListFragment_to_startPageFragment,
                args = null,
                navOptions = navOptions
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.viewStates().collect { viewState ->
                        adapter.data = viewState.voices
                        binding.actionButton.text =
                            if (viewState.areAllSuccess) getString(R.string.finish) else getString(R.string.continue_recording)
                    }
                }

                launch {
                    viewModel.viewActions().collect { viewAction ->
                        when (viewAction) {
                            is RecordingListAction.ShowError -> {}
                            null -> {}
                        }
                    }
                }
            }
        }

        binding.actionButton.setOnClickListener {
            if (viewModel.viewStates().value.areAllSuccess) {
                findNavController().navigate(R.id.action_recordingListFragment_to_finalPageFragment)
            } else {
                val voices = viewModel.viewStates().value.voices
                val firstVoice = voices.firstOrNull { v -> !v.isSuccess } ?: VoiceRecording(
                    voices.size + 1,
                    false
                )
                val args = bundleOf(
                    VoiceFragment.ARG_STEP to firstVoice.step,
                    VoiceFragment.ARG_FAIL to firstVoice.fail
                )
                val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
                findNavController().navigate(
                    resId = R.id.action_recordingListFragment_to_voiceFragment,
                    args = args,
                    navOptions = navOptions
                )
            }
        }
    }

}