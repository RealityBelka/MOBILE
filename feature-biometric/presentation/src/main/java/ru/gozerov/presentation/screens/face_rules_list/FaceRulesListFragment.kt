package ru.gozerov.presentation.screens.face_rules_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.gozerov.data.repository.RuleRepositoryImpl
import ru.gozerov.presentation.databinding.FragmentFaceRulesListBinding

class FaceRulesListFragment : Fragment() {

    private var _binding: FragmentFaceRulesListBinding? = null
    private val binding get() = _binding!!

    private val ruleRepository = RuleRepositoryImpl();
    private lateinit var adapter: FaceRulesAdapter

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

        adapter = FaceRulesAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.faceRulesList.layoutManager = layoutManager
        binding.faceRulesList.adapter = adapter
        adapter.items = ruleRepository.getList()


        binding.faceRulesReturn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}