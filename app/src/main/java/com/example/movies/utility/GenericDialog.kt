package com.example.movies.utility

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.movies.R
import com.example.movies.databinding.LoadingViewBinding
import com.github.ybq.android.spinkit.style.Circle

class GenericDialog : DialogFragment() {

    override fun onResume() {
        super.onResume()
        val window: Window? = dialog?.window
        val width = context!!.resources.displayMetrics.widthPixels * 0.9
        val height = context!!.resources.displayMetrics.heightPixels * 0.5
        window?.setLayout(width.toInt(), height.toInt())
        window?.setGravity(Gravity.CENTER)

        // Animation
        window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    var binding: LoadingViewBinding? = null
    private var message: Message? = null
    private var viewModel: DialogViewModel? = null

    init {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this).get(DialogViewModel::class.java)
        binding = LoadingViewBinding.inflate(inflater)
        binding?.lifecycleOwner = this
        binding?.viewmodel = viewModel
        binding?.model = message
        isCancelable = false

        val circle = Circle()
        binding?.messageDialogLoadingPage?.spinKit?.setIndeterminateDrawable(circle)

        binding?.messageDialogWarningPage?.okBtn?.setOnClickListener {
            dismiss()
        }

        if (message != null) {

            viewModel?.postDialogModel(message!!)
            message = null
        }

        return binding?.root
    }

    fun setInfoText(message: Message) {

        viewModel?.postDialogModel(message)
        this.message = message
    }
}