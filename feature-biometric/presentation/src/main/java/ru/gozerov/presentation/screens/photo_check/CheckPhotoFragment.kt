package ru.gozerov.presentation.screens.photo_check

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.FragmentCheckPhotoBinding
import java.io.File

class CheckPhotoFragment : Fragment() {

    private var _binding: FragmentCheckPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cacheDir = requireActivity().externalCacheDir
        val imageFile = File(cacheDir, "face.jpeg")

        if (imageFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            binding.imgCaptured.setImageBitmap(bitmap)
        }

        binding.sendButton.setOnClickListener {
            findNavController().navigate(R.id.action_photoCheckFragment_to_takeVoiceStartFragment)
        }

        binding.exitButton.setOnClickListener {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.startPageFragment, true).build()
            findNavController().navigate(
                resId = R.id.action_photoCheckFragment_to_startPageFragment,
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