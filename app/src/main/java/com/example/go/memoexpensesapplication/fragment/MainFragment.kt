package com.example.go.memoexpensesapplication.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.go.memoexpensesapplication.R
import com.example.go.memoexpensesapplication.constant.RecyclerType
import com.example.go.memoexpensesapplication.model.Expense
import com.example.go.memoexpensesapplication.view.adapter.RecyclerAdapter
import com.example.go.memoexpensesapplication.view.listener.OnRecyclerListener
import kotlinx.android.synthetic.main.dialog_view_fragment_main_add.view.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), OnRecyclerListener {
    private var listener: OnFragmentInteractionListener? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerAdapter: RecyclerAdapter? = null
    private val data = arrayListOf(
        Expense(RecyclerType.HEADER, "Tag", "Value", "Note"),
        Expense(RecyclerType.SECTION, "testA", null, null),
        Expense(RecyclerType.BODY, "testA", "100", "memoA1"),
        Expense(RecyclerType.BODY, "testA", "200", "memoA2"),
        Expense(RecyclerType.SECTION, "testB", null, null),
        Expense(RecyclerType.BODY, "testB", "300", "memoB1"),
        Expense(RecyclerType.BODY, "testB", "400", "memoB2"),
        Expense(RecyclerType.SECTION, "testC", null, null),
        Expense(RecyclerType.BODY, "testC", "500", "memoC1")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = fragment_main_recycler_view
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = RecyclerAdapter(context, data, this)
        recyclerView?.adapter = recyclerAdapter
        fragment_main_floating_action_button.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_view_fragment_main_add, null, false)
            val builder = context?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.fragment_main_add_title)
                    .setView(dialogView)
                    .setPositiveButton(R.string.fragment_main_add_positive) { _, _ ->
                        data.add(Expense(
                            RecyclerType.BODY,
                            dialogView.dialog_view_fragment_main_add_tag.text.toString(),
                            dialogView.dialog_view_fragment_main_add_value.text.toString(),
                            dialogView.dialog_view_fragment_main_add_note.text.toString()))
                        Toast.makeText(context, "add", Toast.LENGTH_SHORT).show()
                        recyclerAdapter?.notifyDataSetChanged()
                    }
                    .setNegativeButton(R.string.cancel, null)
            } ?: return@setOnClickListener
            MyDialogFragment().setBuilder(builder)
                .show((activity as AppCompatActivity).supportFragmentManager, null)
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
        Toast.makeText(context, "${item.tag}: ${item.value}", Toast.LENGTH_SHORT)
            .show()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
