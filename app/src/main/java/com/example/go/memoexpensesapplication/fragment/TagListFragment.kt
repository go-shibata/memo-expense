package com.example.go.memoexpensesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.actioncreator.TagListActionCreator
import com.example.go.memoexpensesapplication.component.DaggerTagListComponent
import com.example.go.memoexpensesapplication.databinding.DialogAddTagBinding
import com.example.go.memoexpensesapplication.databinding.FragmentTagListBinding
import com.example.go.memoexpensesapplication.view.adapter.TagListAdapter
import com.example.go.memoexpensesapplication.viewmodel.FragmentTagListViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class TagListFragment : Fragment(), TagListAdapter.OnClickListener {
    private lateinit var tagListAdapter: TagListAdapter

    private lateinit var viewModel: FragmentTagListViewModel
    private lateinit var binding: FragmentTagListBinding
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var actionCreator: TagListActionCreator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tagListComponent = DaggerTagListComponent.create()
        tagListComponent.inject(this)

        activity?.run {
            viewModel = ViewModelProviders.of(this)[FragmentTagListViewModel::class.java]
        } ?: throw RuntimeException("Invalid activity")
        viewModel.inject(tagListComponent)
        viewModel.tags
            .subscribe { tags -> tagListAdapter.update(tags) }
            .addTo(compositeDisposable)
        viewModel.addTag
            .subscribe { tag -> tagListAdapter.add(tag) }
            .addTo(compositeDisposable)
        viewModel.deleteTag
            .subscribe { tag -> tagListAdapter.delete(tag) }
            .addTo(compositeDisposable)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTagListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            setTitle(R.string.fragment_tag_list_title)
            setDisplayHomeAsUpEnabled(true)
        }

        tagListAdapter = TagListAdapter(emptyList(), this)

        binding.layoutTagList.apply {
            adapter = tagListAdapter
            layoutManager = LinearLayoutManager(activity)

            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
        }

        actionCreator.getAllTags()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecyclerClicked(v: View, position: Int, item: String) {
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.fragment_tag_list_remove_tag_title)
                .setMessage(resources.getString(R.string.fragment_tag_list_remove_tag, item))
                .setPositiveButton(R.string.ok) { _, _ ->
                    actionCreator.deleteTag(item)
                }
                .setNegativeButton(R.string.cancel, null)
        } ?: return
        MyDialogFragment().setBuilder(builder)
            .show((activity as AppCompatActivity).supportFragmentManager, null)
    }

    fun onClickAddTag() {
        val binding = DialogAddTagBinding.inflate(layoutInflater, view as ViewGroup, false)

        val builder = context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.fragment_tag_list_add_tag_title)
                .setView(binding.root)
                .setPositiveButton(R.string.add) { _, _ ->
                    val tag = binding.inputTag.text.toString()
                    actionCreator.addTag(tag)
                }
                .setNegativeButton(R.string.cancel, null)
        } ?: return
        MyDialogFragment().setBuilder(builder)
            .show((activity as AppCompatActivity).supportFragmentManager, null)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TagListFragment()
    }
}
