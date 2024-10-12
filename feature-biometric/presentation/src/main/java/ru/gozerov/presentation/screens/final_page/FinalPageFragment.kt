package ru.gozerov.presentation.screens.final_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.FragmentFinalPageBinding

class FinalPageFragment : Fragment() {

    private var _binding: FragmentFinalPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinalPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.finalPageExit.setOnClickListener {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.startPageFragment, true).build()
            findNavController().navigate(
                resId = R.id.action_finalPageFragment_to_startPageFragment,
                args = null,
                navOptions = navOptions
            )
        }
    }
}