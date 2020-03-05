package com.example.go.memoexpensesapplication.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.go.memoexpensesapplication.Preferences
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.actioncreator.MainActionCreator
import com.example.go.memoexpensesapplication.component.DaggerMainComponent
import com.example.go.memoexpensesapplication.databinding.DialogViewFragmentMainAddBinding
import com.example.go.memoexpensesapplication.databinding.FragmentMainBinding
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.model.User
import com.example.go.memoexpensesapplication.navigator.FragmentMainNavigator
import com.example.go.memoexpensesapplication.view.adapter.ExpenseListAdapter
import com.example.go.memoexpensesapplication.view.adapter.TagListSpinnerAdapter
import com.example.go.memoexpensesapplication.viewmodel.FragmentMainViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class MainFragment : Fragment(), ExpenseListAdapter.OnClickExpenseListener {
    private lateinit var expenseListAdapter: ExpenseListAdapter

    private lateinit var viewModel: FragmentMainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var navigator: FragmentMainNavigator
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var actionCreator: MainActionCreator

    @Inject
    lateinit var pref: Preferences

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainComponent = DaggerMainComponent
            .create()
        mainComponent.inject(this)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FragmentMainViewModel::class.java]
        viewModel.inject(mainComponent)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this

        viewModel.expenses
            .subscribe { expenses -> expenseListAdapter.update(expenses) }
            .addTo(compositeDisposable)
        viewModel.addExpense
            .subscribe { expense -> expenseListAdapter.add(expense) }
            .addTo(compositeDisposable)
        viewModel.deleteExpense
            .subscribe { expense -> expenseListAdapter.delete(expense) }
            .addTo(compositeDisposable)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
        }

        expenseListAdapter =
            ExpenseListAdapter(emptyList(), this@MainFragment).apply {
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
        inflater.inflate(R.menu.menu_fragment_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_fragment_main_edit_tag -> {
                navigator.onTransitionTagList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClickExpense(v: View, position: Int, item: Expense) {
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.fragment_main_remove_title)
                .setMessage(getString(R.string.fragment_main_remove_message, item.tag, item.value))
                .setPositiveButton(R.string.ok) { _, _ ->
                    actionCreator.deleteExpense(item)
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
        binding.tag.adapter = TagListSpinnerAdapter(context!!, tags)

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

    fun setUser(user: User) {
        this.user = user
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User, navigator: FragmentMainNavigator) = MainFragment().apply {
            setUser(user)
            this.navigator = navigator
        }
    }
}
