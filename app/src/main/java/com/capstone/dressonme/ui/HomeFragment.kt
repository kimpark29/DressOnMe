package com.capstone.dressonme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.dressonme.model.Picture
import com.capstone.dressonme.R
import com.capstone.dressonme.ui.adapter.RecommendationAdapter
import com.capstone.dressonme.databinding.FragmentHomeBinding
import com.capstone.dressonme.ui.adapter.GalleryAdapter
import com.capstone.dressonme.viewmodel.ProcessViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val listItems = ArrayList<Picture>()
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var galleryAdapter: GalleryAdapter
    private val processViewModel : ProcessViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getData()
    }

    private fun setAdapter() {
        recommendationAdapter = RecommendationAdapter()
        galleryAdapter = GalleryAdapter()

        binding.apply {
            rvRecommendation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvGallery.layoutManager = GridLayoutManager(context, 2)
            rvRecommendation.adapter = recommendationAdapter
            rvGallery.adapter = galleryAdapter
        }
    }

    private fun getData() {
        processViewModel.userPhotos.observe(viewLifecycleOwner) {
            galleryAdapter.setListGallery(it)
        }

        listItems.addAll(listPics)
        recommendationAdapter.setListRecommendation(listItems)
    }

    private val listPics: ArrayList<Picture>
        get() {
            val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
            val listItem = ArrayList<Picture>()
            for (i in 0..8) {
                val items = Picture(dataPhoto.getResourceId(i, -1))
                listItem.add(items)
            }
            dataPhoto.recycle()
            return listItem
        }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}