package com.example.go.memoexpensesapplication.actioncreator

import com.example.go.memoexpensesapplication.Preferences
import com.example.go.memoexpensesapplication.action.TagListAction
import com.example.go.memoexpensesapplication.dispatcher.TagListDispatcher
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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
    }

    @Test
    fun addTag_confirmDispatchAddTagAction() {
        val tag = "tag1"

        actionCreator.addTag(tag)
        argumentCaptor<TagListAction.AddTag>().apply {
            verify(dispatcher, times(1)).dispatch(capture())
            assertThat(firstValue)
                .extracting("data")
                .isEqualTo(tag)
        }
    }

    @Test
    fun deleteTag_confirmDispatchDeleteTagAction() {
        val tag = "tag1"

        actionCreator.deleteTag(tag)
        argumentCaptor<TagListAction.DeleteTag>().apply {
            verify(dispatcher, times(1)).dispatch(capture())
            assertThat(firstValue)
                .extracting("data")
                .isEqualTo(tag)
        }
    }
}