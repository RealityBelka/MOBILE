package ru.gozerov.presentation.screens.face_rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.gozerov.core.navigation.Screen
import ru.gozerov.core.navigation.launch
import ru.gozerov.presentation.databinding.FragmentFaceRulesBinding
import ru.gozerov.presentation.databinding.FragmentFaceRulesListBinding

class FaceRulesListFragment : Fragment() {

    private var _binding: FragmentFaceRulesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceRulesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.facrRulesReturn.setOnClickListener {
            findNavController().launch(screen = Screen.FaceRulesPage)
        }
    }
}