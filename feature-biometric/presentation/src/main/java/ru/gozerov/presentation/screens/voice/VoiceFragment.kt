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
import ru.gozerov.presentation.databinding.FragmentVoiceBinding
import java.io.IOException

class VoiceFragment: Fragment() {

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
        binding.startRecordingButton.setOnClickListener {
            startRecording()
        }

        binding.stopRecordingButton.setOnClickListener {
            stopRecording()
        }
    }


    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String = ""

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
            mediaRecorder?.prepare()
            mediaRecorder?.start()
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
        mediaRecorder = null
        Toast.makeText(requireContext(), "Recording saved at $audioFilePath", Toast.LENGTH_SHORT).show()
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}