package com.example.go.memoexpensesapplication.actioncreator

import com.example.go.memoexpensesapplication.Preferences
import com.example.go.memoexpensesapplication.action.TagListAction
import com.example.go.memoexpensesapplication.dispatcher.TagListDispatcher
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TagListActionCreatorTest {

    @Mock
    lateinit var pref: Preferences

    @Mock
    lateinit var dispatcher: TagListDispatcher
    lateinit var actionCreator: TagListActionCreator

    @Before
    fun setUp() {
        actionCreator = TagListActionCreator(pref, dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getAllTags_confirmDispatchGetAllTagsAction() {
        val tags = setOf("tag1", "tag2", "tag3")
        whenever(pref.getTags()).thenReturn(tags)

        actionCreator.getAllTags()
        argumentCaptor<TagListAction.GetAllTags>().apply {
            verify(dispatcher, times(1)).dispatch(capture())
            assertThat(firstValue)
                .extracting("data")
                .isEqualTo(tags.toList())
        }
        verify(dispatcher, never()).dispatch(any<TagListAction.AddTag>())
        verify(dispatcher, never()).dispatch(any<TagListAction.DeleteTag>())
    }

    @Test
    fun addTag_confirmDispatchAddTagAction() {
        val tag = "tag1"

        actionCreator.addTag(tag)
        verify(dispatcher, never()).dispatch(any<TagListAction.GetAllTags>())
        argumentCaptor<TagListAction.AddTag>().apply {
            verify(dispatcher, times(1)).dispatch(capture())
            assertThat(firstValue)
                .extracting("data")
                .isEqualTo(tag)
        }
        verify(dispatcher, never()).dispatch(any<TagListAction.DeleteTag>())
    }

    @Test
    fun deleteTag_confirmDispatchDeleteTagAction() {
        val tag = "tag1"

        actionCreator.deleteTag(tag)
        verify(dispatcher, never()).dispatch(any<TagListAction.GetAllTags>())
        verify(dispatcher, never()).dispatch(any<TagListAction.AddTag>())
        argumentCaptor<TagListAction.DeleteTag>().apply {
            verify(dispatcher, times(1)).dispatch(capture())
            assertThat(firstValue)
                .extracting("data")
                .isEqualTo(tag)
        }
    }
}