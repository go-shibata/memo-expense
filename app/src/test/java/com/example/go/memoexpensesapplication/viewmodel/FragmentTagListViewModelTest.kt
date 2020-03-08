package com.example.go.memoexpensesapplication.viewmodel

import com.example.go.memoexpensesapplication.RxImmediateSchedulerRule
import com.example.go.memoexpensesapplication.action.TagListAction
import com.example.go.memoexpensesapplication.dispatcher.TagListDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FragmentTagListViewModelTest {

    @get:Rule
    val rule = RxImmediateSchedulerRule()

    @Spy
    val dispatcher = TagListDispatcher()

    lateinit var viewModel: FragmentTagListViewModel

    @Before
    fun setUp() {
        viewModel = FragmentTagListViewModel(dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun occurredGetAllTagsAction_confirmTagsFlowPost() {
        val tags = listOf("tag1", "tag2", "tag3")
        val subscriber = viewModel.tags.test()
        dispatcher.dispatch(TagListAction.GetAllTags(tags))
        subscriber
            .assertNoErrors()
            .assertValue(tags)
    }

    @Test
    fun occurredAddTagAction_confirmAddTagFlowPost() {
        val tag = "tag1"
        val subscriber = viewModel.addTag.test()
        dispatcher.dispatch(TagListAction.AddTag(tag))
        subscriber
            .assertNoErrors()
            .assertValue(tag)
    }

    @Test
    fun occurredDeleteTagAction_confirmDeleteTagFlowPost() {
        val tag = "tag1"
        val subscriber = viewModel.deleteTag.test()
        dispatcher.dispatch(TagListAction.DeleteTag(tag))
        subscriber
            .assertNoErrors()
            .assertValue(tag)
    }
}