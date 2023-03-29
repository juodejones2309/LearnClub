package com.rcappstudios.qualityeducation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.adapters.SubjectAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentSelectSubjectBinding
import com.rcappstudios.qualityeducation.chatgpt.ui.ChatGptActivity

class SelectSubjectFragment : Fragment() {

    private lateinit var binding: FragmentSelectSubjectBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var subjectAdapter: SubjectAdapter

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            FragmentSelectSubjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        binding.subjectRv.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        initSubjectRv()
        clickListener()
    }

    private fun initSubjectRv() {
       /* FirebaseDatabase.getInstance().getReference("Groups")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })*/
    }

    private fun clickListener(){
        binding.physics.setOnClickListener {
            switchToSubjectDetail(
                SelectSubjectFragmentDirections
                    .actionSelectSubjectFragmentToSubjectDetailFragment
                        ("Physics"),
                R.id.subjectDetailFragment)
        }

        binding.chemistry.setOnClickListener {
            switchToSubjectDetail(
                SelectSubjectFragmentDirections
                    .actionSelectSubjectFragmentToSubjectDetailFragment
                        ("Chemistry"),
                R.id.subjectDetailFragment)
        }

        binding.maths.setOnClickListener {
            switchToSubjectDetail(
                SelectSubjectFragmentDirections
                    .actionSelectSubjectFragmentToSubjectDetailFragment
                        ("Maths"),
                R.id.subjectDetailFragment)
        }

        binding.computer.setOnClickListener {
            switchToSubjectDetail(
                SelectSubjectFragmentDirections
                    .actionSelectSubjectFragmentToSubjectDetailFragment
                        ("Computer"),
                R.id.subjectDetailFragment)
        }

        binding.social.setOnClickListener {
            switchToSubjectDetail(
                SelectSubjectFragmentDirections
                    .actionSelectSubjectFragmentToSubjectDetailFragment
                        ("Social"),
                R.id.subjectDetailFragment)
        }

        binding.addClsFab.setOnClickListener{
            initBottomSheet()
        }

        binding.bottomSheet.btnCreateRoom.setOnClickListener {
            extractDetails()
        }

    }

    private fun initBottomSheet(){
        binding.bottomSheet.textRoomTitle.setText("Enter Group title")
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        val state = if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            BottomSheetBehavior.STATE_COLLAPSED
        } else {
            //TODO : Setup api call and recycler view adapter
            BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetBehavior.state = state
    }


    private fun extractDetails(){
        val title = binding.bottomSheet.roomTitle.text.toString().trim()
        if(title != ""){
            createClass(title)
        } else {
            Toast.makeText(requireContext(), "Please fill the details", Toast.LENGTH_LONG).show()
        }
    }

    private fun createClass(subject: String) {
        FirebaseDatabase.getInstance().getReference("Groups/$subject/creatorId")
            .setValue(FirebaseAuth.getInstance().currentUser!!.uid)
            .addOnSuccessListener {
                switchToSubjectDetail(
                    SelectSubjectFragmentDirections
                        .actionSelectSubjectFragmentToSubjectDetailFragment
                            (subject),
                    R.id.subjectDetailFragment)
            }
    }

    private fun switchToSubjectDetail(actions: NavDirections, destinationId: Int){
        if (isFragmentInBackStack(destinationId)) {
            getNavController().popBackStack(destinationId, false)
        } else {
            getNavController().navigate(actions)
        }
    }

    private fun getNavController(): NavController {
        return (requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
    }


    private fun isFragmentInBackStack(destinationId: Int) =
        try {
            getNavController().getBackStackEntry(destinationId)
            true
        } catch (e: Exception) {
            false
        }


}