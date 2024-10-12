package ru.gozerov.presentation.screens.face_rules_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.gozerov.core.di.biometricComponentHolder
import ru.gozerov.domain.repositories.RuleRepository
import ru.gozerov.presentation.databinding.FragmentFaceRulesListBinding
import javax.inject.Inject

class FaceRulesListFragment : Fragment() {

    private var _binding: FragmentFaceRulesListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var ruleRepository: RuleRepository

    private lateinit var adapter: FaceRulesAdapter

    override fun onAttach(context: Context) {
        val component = context.biometricComponentHolder
        component.inject(this)

        super.onAttach(context)
    }

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