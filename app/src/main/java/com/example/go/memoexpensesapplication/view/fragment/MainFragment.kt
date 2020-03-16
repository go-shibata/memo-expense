package com.example.go.memoexpensesapplication.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.go.memoexpensesapplication.Preferences
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.actioncreator.MainActionCreator
import com.example.go.memoexpensesapplication.databinding.DialogViewFragmentMainAddBinding
import com.example.go.memoexpensesapplication.databinding.FragmentMainBinding
import com.example.go.memoexpensesapplication.di.ViewModelFactory
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.model.User
import com.example.go.memoexpensesapplication.view.activity.MainActivity
import com.example.go.memoexpensesapplication.view.adapter.ExpenseListAdapter
import com.example.go.memoexpensesapplication.view.adapter.TagListSpinnerAdapter
import com.example.go.memoexpensesapplication.viewmodel.FragmentMainViewModel
import javax.inject.Inject

class MainFragment : Fragment(), ExpenseListAdapter.OnClickExpenseListener,
    FragmentMainViewModel.FragmentMainNavigator {
    @Inject
    lateinit var actionCreator: MainActionCreator

    @Inject
    lateinit var pref: Preferences

    @Inject
    lateinit var factory: ViewModelFactory<FragmentMainViewModel>

    private val viewModel: FragmentMainViewModel by activityViewModels { factory }
    private lateinit var binding: FragmentMainBinding
    private lateinit var expenseListAdapter: ExpenseListAdapter
    private var actionMode: ActionMode? = null
    private val actionModeCallback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.delete -> {
                    val checkedExpenses = expenseListAdapter.getCheckedExpenses()
                    if (checkedExpenses.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            R.string.fragment_main_menu_delete_checked_list_empty,
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                    val builder = context?.let { context ->
                        AlertDialog.Builder(context)
                            .setTitle(R.string.fragment_main_remove_title)
                            .setMessage(R.string.fragment_main_menu_delete_dialog_message)
                            .setPositiveButton(R.string.ok) { _, _ ->
                                checkedExpenses.forEach { actionCreator.deleteExpense(it) }
                                mode.finish()
                            }
                            .setNegativeButton(R.string.cancel, null)
                    } ?: return false
                    MyDialogFragment().setBuilder(builder)
                        .show((activity as AppCompatActivity).supportFragmentManager, null)
                    true
                }
                else -> false
            }
        }

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflater = mode.menuInflater
            inflater.inflate(R.menu.fragment_main_action_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.title = getString(R.string.fragment_main_menu_delete)
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionCreator.toggleCheckable()
            actionMode = null
        }
    }

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            user = MainFragmentArgs.fromBundle(it).user
        } ?: throw RuntimeException("Invalid arguments")

        activity?.run {
            (this as AppCompatActivity).supportActionBar?.show()
            if (this is MainActivity) {
                mainComponent.inject(this@MainFragment)
            } else throw RuntimeException("$this must be MainActivity")
        } ?: throw RuntimeException("Invalid activity")

        viewModel.setMainNavigator(this)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
        }

        expenseListAdapter =
            ExpenseListAdapter(this, viewModel, this).apply {
                setHeader()
                setFooter()
            }

        binding.expenseList.apply {
            adapter = expenseListAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        actionCreator.getAllExpenses(user.uid)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> {
                actionMode = activity?.startActionMode(actionModeCallback)
                actionCreator.toggleCheckable()
            }
            R.id.edit_tag -> actionCreator.moveToTagList()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClickExpense(expense: Expense) {
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(
                    getString(
                        R.string.fragment_main_click_expense_title,
                        expense.tag,
                        expense.value
                    )
                )
                .setItems(R.array.fragment_main_click_expense_selector) { dialog, pos ->
                    when (pos) {
                        SELECT_EDIT -> onClickEditExpense(expense)
                        SELECT_DELETE -> onClickDeleteExpense(expense)
                    }
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel, null)
        } ?: return
        MyDialogFragment().setBuilder(builder)
            .show((activity as AppCompatActivity).supportFragmentManager, null)
    }

    private fun onClickEditExpense(expense: Expense) {
        val binding = DialogViewFragmentMainAddBinding
            .inflate(layoutInflater, view as ViewGroup, false)
        val tags = pref.getTags().toList()
        binding.tag.adapter = TagListSpinnerAdapter(requireContext(), tags)
        binding.tag.setSelection(tags.indexOf(expense.tag))
        binding.value.setText(expense.value.toString())
        binding.memo.setText(expense.note)

        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.fragment_main_edit_title)
                .setView(binding.root)
                .setPositiveButton(R.string.fragment_main_edit_ok) { _, _ ->
                    val item = Expense(
                        expense.id,
                        expense.uid,
                        binding.tag.selectedItem as String,
                        binding.value.text.toString().toInt(10),
                        binding.memo.text.toString()
                    )
                    actionCreator.editExpense(item)
                }
                .setNegativeButton(R.string.cancel, null)
        } ?: return
        MyDialogFragment().setBuilder(builder)
            .show((activity as AppCompatActivity).supportFragmentManager, null)
    }

    private fun onClickDeleteExpense(expense: Expense) {
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.fragment_main_remove_title)
                .setMessage(
                    getString(
                        R.string.fragment_main_remove_message,
                        expense.tag,
                        expense.value
                    )
                )
                .setPositiveButton(R.string.ok) { _, _ ->
                    actionCreator.deleteExpense(expense)
                }
                .setNegativeButton(R.string.cancel, null)
        } ?: return
        MyDialogFragment().setBuilder(builder)
            .show((activity as AppCompatActivity).supportFragmentManager, null)
    }

    fun onClickAddExpense() {
        val binding = DialogViewFragmentMainAddBinding
            .inflate(layoutInflater, view as ViewGroup, false)
        val tags = pref.getTags().toList()
        binding.tag.adapter = TagListSpinnerAdapter(requireContext(), tags)

        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.fragment_main_add_title)
                .setView(binding.root)
                .setPositiveButton(R.string.add) { _, _ ->
                    val item = Expense(
                        user.uid,
                        binding.tag.selectedItem as String,
                        binding.value.text.toString().toInt(10),
                        binding.memo.text.toString()
                    )
                    actionCreator.addExpense(item)
                }
                .setNegativeButton(R.string.cancel, null)
        } ?: return
        MyDialogFragment().setBuilder(builder)
            .show((activity as AppCompatActivity).supportFragmentManager, null)
    }

    override fun onTransitionTagList() {
        view?.findNavController()?.navigate(
            MainFragmentDirections.actionMainFragmentToTagListFragment()
        )
    }

    companion object {
        private const val SELECT_EDIT = 0
        private const val SELECT_DELETE = 1

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
