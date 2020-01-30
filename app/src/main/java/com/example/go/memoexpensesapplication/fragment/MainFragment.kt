package com.example.go.memoexpensesapplication.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.go.memoexpensesapplication.Prefs
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.action.MainAction
import com.example.go.memoexpensesapplication.constant.ExpenseViewType
import com.example.go.memoexpensesapplication.databinding.DialogViewFragmentMainAddBinding
import com.example.go.memoexpensesapplication.databinding.FragmentMainBinding
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.view.adapter.RecyclerAdapter
import com.example.go.memoexpensesapplication.view.adapter.TagListSpinnerAdapter
import com.example.go.memoexpensesapplication.view.listener.OnRecyclerListener
import com.example.go.memoexpensesapplication.viewmodel.MainFragmentViewModel

class MainFragment : Fragment(), OnRecyclerListener {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var recyclerAdapter: RecyclerAdapter

    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainFragmentViewModel::class.java]
        recyclerAdapter = RecyclerAdapter(viewModel.data.value.orEmpty(), this@MainFragment).apply {
            setHeader()
            setFooter()
        }

        binding.expenseList.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        viewModel.data.observe(this, Observer {
            recyclerAdapter.update(it)
        })

        binding.buttonAddExpense.setOnClickListener {
            val binding =
                DialogViewFragmentMainAddBinding.inflate(layoutInflater, view as ViewGroup, false)
            val tags = Prefs.getTags().toList()
            binding.tag.adapter = TagListSpinnerAdapter(context!!, tags)

            val builder = context?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.fragment_main_add_title)
                    .setView(binding.root)
                    .setPositiveButton(R.string.add) { _, _ ->
                        val item = Expense(
                            null,
                            ExpenseViewType.BODY,
                            binding.tag.selectedItem as String,
                            binding.value.text.toString().toInt(10),
                            binding.memo.text.toString()
                        )
                        viewModel.send(MainAction.AddExpense(item))
                    }
                    .setNegativeButton(R.string.cancel, null)
            } ?: return@setOnClickListener
            MyDialogFragment().setBuilder(builder)
                .show((activity as AppCompatActivity).supportFragmentManager, null)
        }

        viewModel.send(MainAction.GetExpense())
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_fragment_main_edit_tag -> {
                listener?.onTransitionTagList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecyclerClicked(v: View, position: Int, item: Expense) {
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.fragment_main_remove_title)
                .setMessage(getString(R.string.fragment_main_remove_message, item.tag, item.value))
                .setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.send(MainAction.DeleteExpense(item))
                }
                .setNegativeButton(R.string.cancel, null)
        } ?: return
        MyDialogFragment().setBuilder(builder)
            .show((activity as AppCompatActivity).supportFragmentManager, null)
    }

    interface OnFragmentInteractionListener {
        fun onTransitionTagList()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
