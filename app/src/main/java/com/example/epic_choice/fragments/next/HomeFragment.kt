package com.example.epic_choice.fragments.next

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.epic_choice.R
import com.example.epic_choice.adapters.HomePage.HomeviewpagerAdapter
import com.example.epic_choice.databinding.FragmentHomeBinding
import com.example.epic_choice.fragments.categories.EpicBuyFragment
import com.example.epic_choice.fragments.categories.EpicDineFragment
import com.example.epic_choice.fragments.categories.EpicExperienceFragment
import com.example.epic_choice.fragments.categories.EpicGuideFragment
import com.example.epic_choice.fragments.categories.EpicRideFragment
import com.example.epic_choice.fragments.categories.EpicStayFragment
import com.example.epic_choice.fragments.categories.MainCategoryFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.zxing.integration.android.IntentIntegrator
import android.speech.RecognizerIntent
import android.app.Activity
import android.widget.Toast
import java.util.Locale

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            EpicGuideFragment(),
            EpicRideFragment(),
            EpicStayFragment(),
            EpicDineFragment(),
            EpicExperienceFragment(),
            EpicBuyFragment()
        )

        binding.viewPagerHome.isUserInputEnabled = false

        val viewPager2Adapter = HomeviewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Epic Guide"
                2 -> tab.text = "Epic Ride"
                3 -> tab.text = "Epic Stay"
                4 -> tab.text = "Epic Dine"
                5 -> tab.text = "Epic Experience"
                6 -> tab.text = "Epic Buy"
            }
        }.attach()

        // Set up search bar click listener
        binding.searchBar.setOnClickListener {
            navigateToSearch()
        }

        // Set up scan icon click listener
        binding.scanIcon.setOnClickListener {
            initiateScan()
        }

        // Set up microphone icon click listener
        binding.microphoneIcon.setOnClickListener {
            initiateVoiceSearch()
        }

    }

    private fun navigateToSearch() {
        // Implement navigation to search screen or activity
        findNavController().navigate(R.id.exploreFragment)
    }

    private fun initiateScan() {
        // Use ZXing's IntentIntegrator to start a scan
        IntentIntegrator.forSupportFragment(this).initiateScan()
    }

    private fun initiateVoiceSearch() {
        // Create an Intent for voice recognition
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")
        }

        // Check if the device has speech recognition capabilities
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_VOICE_SEARCH)
        } else {
            Toast.makeText(requireContext(), "Speech recognition is not supported on this device", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VOICE_SEARCH && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = result?.get(0) ?: ""
            // Handle the spoken text (e.g., perform search with it)
            Toast.makeText(requireContext(), "Voice input: $spokenText", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val REQUEST_CODE_VOICE_SEARCH = 1001
    }
}
