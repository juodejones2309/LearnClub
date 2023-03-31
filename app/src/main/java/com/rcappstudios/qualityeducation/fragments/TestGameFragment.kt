package com.rcappstudios.qualityeducation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase

import com.rcappstudios.qualityeducation.adapters.ProblemAdapter
import com.rcappstudios.qualityeducation.adapters.SubjectAdapter
import com.rcappstudios.qualityeducation.databinding.FragmentTestGameBinding
import com.rcappstudios.qualityeducation.model.ProblemStatement
import com.rcappstudios.qualityeducation.model.Test


class TestGameFragment : Fragment(){

    private lateinit var binding: FragmentTestGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTestGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        storeData()
        fetchProblemStatement()
    }

    private fun fetchProblemStatement(){
        FirebaseDatabase.getInstance()
            .getReference("ProblemStatements")
            .get()
            .addOnSuccessListener {
                val problemList = mutableListOf<ProblemStatement>()
                for(c in it.children){
                    problemList.add(c.getValue(ProblemStatement::class.java)!!)
                }
                initProblemStatement(problemList)
            }
    }

    private fun initProblemStatement(problemList: MutableList<ProblemStatement>){
        binding.problemRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.problemRecyclerView.adapter = ProblemAdapter(
            requireContext(),
            problemList
        ){

        }
    }

    private fun storeData(){
        FirebaseDatabase.getInstance()
            .getReference("ProblemStatements")
            .push()
            .setValue(
                ProblemStatement(
                    title = "Two days hackathon",
                    imageUrl = "https://imgs.search.brave.com/ZmbRarCF2Vda6JWJlAuz_oLKx8lEBw-8jwy8Y0SiLtw/rs:fit:1021:225:1/g:ce/aHR0cHM6Ly90c2U0/Lm1tLmJpbmcubmV0/L3RoP2lkPU9JUC5w/T0ZuaTdUNHJCWXph/RnZia3JPbnVnSGFE/YyZwaWQ9QXBp",
                    link = "https://gdsc.community.dev/",
                    description= "Opportunities come and go like sunrises. If you wait too long, you miss them.\n" +
                            "DSC MEC proudly presents our new venture for the betterment of the students, the Opportunity track. This platform will inform all students of the opportunities available to them and ensure that no one misses out on any as a result of not being aware of the same.\n" +
                            "Don’t forget to hit the link to join and keep track of the opportunities just for you.\n"
                )
            )

        FirebaseDatabase.getInstance()
            .getReference("ProblemStatements")
            .push()
            .setValue(
                ProblemStatement(
                    title = "Smart India Hackathon",
                    imageUrl = "https://imgs.search.brave.com/sIoTmyD9MMrzNQG2yoXju-ZbGC2fzsn2pyRYX3fkL1w/rs:fit:491:225:1/g:ce/aHR0cHM6Ly90c2U0/Lm1tLmJpbmcubmV0/L3RoP2lkPU9JUC5h/bVhoNXFjbXhnYXpN/Vi1Ldm82MTRBSGFI/SiZwaWQ9QXBp",
                    link = "https://www.sih.gov.in/",
                    description= " Smart India Hackathon Smart India Hackathon is a nationwide initiative to provide students a platform to solve some of pressing problems we face in our daily lives, and thus inculcate a culture of product innovation and a mindset of problem solving."
                )
            )

        FirebaseDatabase.getInstance()
            .getReference("ProblemStatements")
            .push()
            .setValue(
                ProblemStatement(
                    title = "Two days hackathon",
                    imageUrl = "https://imgs.search.brave.com/ZmbRarCF2Vda6JWJlAuz_oLKx8lEBw-8jwy8Y0SiLtw/rs:fit:1021:225:1/g:ce/aHR0cHM6Ly90c2U0/Lm1tLmJpbmcubmV0/L3RoP2lkPU9JUC5w/T0ZuaTdUNHJCWXph/RnZia3JPbnVnSGFE/YyZwaWQ9QXBp",
                    link = "https://kavach.mic.gov.in/img/kavach-logo-white.png",
                    description= "Working toward empowering these imperative notions of our society, MoE's Innovation Cell,AICTE along with Bureau of Police Research and Development (BPR&D)(MHA) and Indian Cybercrime Coordination Centre (I4C)(MHA) have launched ‘KAVACH-2023’ a unique national Hackathon to identify innovative concepts and technology solutions for addressing the security challenges of the 21st century faced by our intelligence agencies.KAVACH-2023 is conceived to challenge India’s innovative minds to conceptualize ideas and framework in the domain of cyber security using artificial intelligence, deep learning, machine learning, automation, big data and cloud computing.\n"
                )
            )
    }
}