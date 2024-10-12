package ru.gozerov.presentation.screens.take_voice_start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.FragmentTakeVoiceStartBinding
import ru.gozerov.presentation.screens.voice.VoiceFragment

class TakeVoiceStartFragment : Fragment() {

    private var _binding: FragmentTakeVoiceStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTakeVoiceStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.voiceStartRecord.setOnClickListener {
            val args = bundleOf(VoiceFragment.ARG_STEP to 1, VoiceFragment.ARG_FAIL to null)
            val navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
            findNavController()
                .navigate(
                    resId = R.id.action_takeVoiceStartFragment_to_voiceFragment,
                    args = args,
                    navOptions = navOptions
                )
        }

        binding.voiceSkipRecord.setOnClickListener {
            findNavController().navigate(R.id.action_takeVoiceStartFragment_to_finalPageFragment)
        }

        binding.exitButton.setOnClickListener {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.startPageFragment, true).build()
            findNavController().navigate(
                resId = R.id.action_takeVoiceStartFragment_to_startPageFragment,
                args = null,
                navOptions = navOptions
            )
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}