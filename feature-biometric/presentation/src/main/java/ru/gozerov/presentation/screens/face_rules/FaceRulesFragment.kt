package ru.gozerov.presentation.screens.face_rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.FragmentFaceRulesBinding

class FaceRulesFragment : Fragment() {

    private var _binding: FragmentFaceRulesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rulesStart.setOnClickListener {
            findNavController().navigate(R.id.action_faceRulesFragment_to_face_capturing)
        }

        binding.rulesDesc.setOnClickListener {
            findNavController().navigate(R.id.action_faceRulesFragment_to_faceRulesListFragment)
        }

        binding.exitButton.setOnClickListener {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.startPageFragment, true).build()
            findNavController().navigate(
                resId = R.id.action_faceRulesFragment_to_startPageFragment,
                args = null,
                navOptions = navOptions
            )
        }

    }
}