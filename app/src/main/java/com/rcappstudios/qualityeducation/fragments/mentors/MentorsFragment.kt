package com.rcappstudios.qualityeducation.fragments.mentors

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.adapters.MentorAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentMentorsBinding
import com.rcappstudios.qualityeducation.model.mentors.MentorData
import com.rcappstudios.qualityeducation.utils.Constants


class MentorsFragment : Fragment() {

    private lateinit var binding: FragmentMentorsBinding
    private lateinit var dialog: Dialog
    private lateinit var mentorRecyclerView: RecyclerView
    private var subject: String? = "Physics"
    private var isMentor = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMentorsBinding.inflate(layoutInflater)
        val sharedPref = requireActivity().getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE)
        isMentor = sharedPref.getBoolean(Constants.IS_MENTOR, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()
        initDialog()
        if (isMentor) {
            binding.layoutSelectSubject.visibility = View.GONE
        }
    }

    private fun clickListener() {
        binding.physicsSubject.setOnClickListener {
            subject = "Physics"
            fetchMentorDetails()
        }
    }

    private fun fetchMentorDetails() {
        FirebaseDatabase
            .getInstance()
            .getReference("Mentors/$subject")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val list = mutableListOf<MentorData>()
                    for (c in it.children) {
//                        Log.d("TAGData", "fetchMentorDetails: ")
                        list.add(c.getValue(MentorData::class.java)!!)
                    }
                    showDialog(list)
                }
            }
    }

    private fun initDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_list_mentor)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mentorRecyclerView = dialog.findViewById(R.id.rvMentors)
    }

    private fun showDialog(mentorList: MutableList<MentorData>) {
        mentorRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mentorRecyclerView.adapter = MentorAdapter(
            requireContext(),
            mentorList
        ) { mentorUserId ->
            val intent = Intent(requireActivity(), MentorSupportActivity::class.java)
            intent.putExtra("mentorUserId", mentorUserId)
            intent.putExtra("subject", subject)
            requireActivity().startActivity(intent)
        }
        dialog.show()
    }


}