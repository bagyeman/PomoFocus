package com.belinda.pomofocus.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.belinda.pomofocus.R
import com.belinda.pomofocus.databinding.FragmentTimerBinding


class TimerFragment : Fragment(R.layout.fragment_timer) {

    private val viewModel by viewModels<TimerViewModel>()
    private val binding by dataBinding<FragmentTimerBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timerViewModel = viewModel

        setObservers()

    }


    fun setObservers() {
        viewModel.progress.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it)
        }
    }




}