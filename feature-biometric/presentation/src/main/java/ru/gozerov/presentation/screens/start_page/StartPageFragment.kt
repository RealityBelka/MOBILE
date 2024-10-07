package ru.gozerov.presentation.screens.start_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.gozerov.core.navigation.Screen
import ru.gozerov.core.navigation.launch
import ru.gozerov.presentation.databinding.FragmentStartPageBinding

class StartPageFragment : Fragment() {
    private var _binding: FragmentStartPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonLaunch.setOnClickListener {
            findNavController().launch(screen = Screen.FaceRulesPage)
        }

    }

}