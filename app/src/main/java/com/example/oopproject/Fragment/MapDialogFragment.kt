package com.example.oopproject.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.oopproject.R
import com.example.oopproject.databinding.FragmentMapDialogBinding
import com.example.oopproject.databinding.FragmentSignInBinding
import com.example.oopproject.viewModel.PostWriteViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapDialogFragment : DialogFragment() {

    private lateinit var binding : FragmentMapDialogBinding
    private lateinit var googleMap : GoogleMap
    private var selectedLatLng: LatLng? = null
    private lateinit var postWriteViewModel : PostWriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapDialogBinding.inflate(inflater, container, false)
        postWriteViewModel = ViewModelProvider(requireActivity()).get(PostWriteViewModel::class.java)

        setupMap()

        /*
        binding.confirmBtnMap.setOnClickListener {
            selectedLatLng?.let {
                postWriteViewModel.setSelectedLatLng(it)
                dismiss()
            }
        }

         */
        return binding.root
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map

            val defaultLocation = LatLng(37.5665, 126.9780)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

            googleMap.setOnMapClickListener { latLng ->
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
                selectedLatLng = latLng
            }
        }
    }

}