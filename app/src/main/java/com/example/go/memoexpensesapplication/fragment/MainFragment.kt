package com.example.go.memoexpensesapplication.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.go.memoexpensesapplication.Prefs

import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.constant.ExpenseViewType
import com.example.go.memoexpensesapplication.databinding.FragmentMainBinding
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.network.Database
import com.example.go.memoexpensesapplication.view.adapter.RecyclerAdapter
import com.example.go.memoexpensesapplication.view.listener.OnRecyclerListener
import com.example.go.memoexpensesapplication.viewmodel.MainFragmentViewModel
import kotlinx.android.synthetic.main.dialog_view_fragment_main_add.view.*

class MainFragment : Fragment(), OnRecyclerListener {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var recyclerAdapter: RecyclerAdapter

    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainFragmentViewModel::class.java]
        recyclerAdapter = RecyclerAdapter(viewModel.data.value!!, this@MainFragment).apply {
            setHeader()
            setFooter()
        }

        binding.fragmentMainRecyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.data.observe(this, Observer {
            recyclerAdapter.notifyDataSetChanged()
        })

        binding.fragmentMainFloatingActionButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_view_fragment_main_add, null, false)
            val tags = Prefs.getTags().toList()
            dialogView.dialog_view_fragment_main_add_tag.adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, tags)

            val builder = context?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.fragment_main_add_title)
                    .setView(dialogView)
                    .setPositiveButton(R.string.fragment_main_add_positive) { _, _ ->
                        val item = Expense(
                            null,
                            ExpenseViewType.BODY,
                            dialogView.dialog_view_fragment_main_add_tag.selectedItem as String,
                            dialogView.dialog_view_fragment_main_add_value.text.toString().toInt(10),
                            dialogView.dialog_view_fragment_main_add_note.text.toString())
                        Database.addExpense(item) { id ->
                            item.id = id
                            viewModel.data.apply {
                                value?.add(item)
                                postValue(value)
                            }
                        }
                    }
                    .setNegativeButton(R.string.cancel, null)
            } ?: return@setOnClickListener
            MyDialogFragment().setBuilder(builder)
                .show((activity as AppCompatActivity).supportFragmentManager, null)
        }

        Database.readExpenses {
            viewModel.data.apply {
                value?.addAll(it)
                postValue(value)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onRecyclerClicked(v: View, position: Int, item: Expense) {
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.fragment_main_remove_title)
                .setMessage(getString(R.string.fragment_main_remove_message, item.tag, item.value))
                .setPositiveButton(R.string.ok) { _, _ ->
                    Database.deleteExpenses(item) {
                        viewModel.data.apply {
                            value?.remove(item)
                            postValue(value)
                        }
                    }
                }
                .setNegativeButton(R.string.cancel, null)
        } ?: return
        MyDialogFragment().setBuilder(builder)
            .show((activity as AppCompatActivity).supportFragmentManager, null)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
