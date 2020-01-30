package com.example.go.memoexpensesapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.go.memoexpensesapplication.action.TagListAction
import com.example.go.memoexpensesapplication.databinding.FragmentTagListBinding
import com.example.go.memoexpensesapplication.view.adapter.TagListAdapter
import com.example.go.memoexpensesapplication.viewmodel.TagListFragmentViewModel

class TagListFragment : Fragment(), TagListAdapter.OnClickListener {
    private lateinit var tagListAdapter: TagListAdapter

    private lateinit var viewModel: TagListFragmentViewModel
    private lateinit var binding: FragmentTagListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTagListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[TagListFragmentViewModel::class.java]
        tagListAdapter = TagListAdapter(viewModel.data.value.orEmpty(), this)

        binding.layoutTagList.apply {
            adapter = tagListAdapter
            layoutManager = LinearLayoutManager(activity)

            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(itemDecoration)
        }

        viewModel.data.observe(this, Observer {
            tagListAdapter.update(it)
        })

        viewModel.send(TagListAction.GetTag())
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        fun newInstance() = TagListFragment()
    }
}
