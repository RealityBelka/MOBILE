package ru.gozerov.presentation.screens.photo_check

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gozerov.presentation.databinding.FragmentPhotoCheckBinding
import java.io.File

class PhotoCheckFragment : Fragment() {


    private var _binding: FragmentPhotoCheckBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cacheDir = requireActivity().externalCacheDir
        val imageFile = File(cacheDir, "face.jpeg")

        if (imageFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            binding.imgCaptured.setImageBitmap(bitmap) // Set the bitmap to the ImageView
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}