package ru.gozerov.presentation.screens.recording_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.gozerov.core.navigation.Screen
import ru.gozerov.core.navigation.launch
import ru.gozerov.presentation.databinding.FragmentRecordingListBinding
import kotlin.random.Random

class RecordingListFragment : Fragment() {

    private val adapter = RecordingListAdapter { step, fail ->
        findNavController().launch(screen = Screen.VoiceRecord, step, fail)
    }

    private var _binding: FragmentRecordingListBinding? = null
    private val binding: FragmentRecordingListBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.data = (0..2).map { Recording(it, Random.nextBoolean(), "Something went wrong") }
        binding.recordingList.adapter = adapter
    }

}