package com.example.junit4;

import static org.junit.Assert.*;

import com.google.common.base.Splitter;
import org.junit.Test;

public class SplitterTest {
    Expert1 expert1 = new Expert1();
    Expert2 expert2 = new Expert2();
    Expert3 expert3 = new Expert3();

    @Test
    public void testSplit() {
        expert1.testSplit_EmptyString();
        expert1.testSplit_SingleString();
        expert1.testSplit_MultipleStrings();
        expert1.testSplit_NullInput();
        expert1.testSplit_EmptyCharSequence();
        expert1.testSplit_Limit();
    }

    @Test
    public void testSplitToList() {
        expert2.testSplitToList_EmptyString();
        expert2.testSplitToList_SingleString();
        expert2.testSplitToList_MultipleStrings();
        expert2.testSplitToList_NullInput();
        expert2.testSplitToList_EmptyCharSequence();
        expert2.testSplitToList_Limit();
    }

    @Test
    public void testOn_Char() {
        expert2.testOn_Char();
    }

    @Test
    public void testOn_String() {
        expert2.testOn_String();
    }

    @Test
    public void testOn_Pattern() {
        expert2.testOn_Pattern();
    }

    @Test
    public void testOmitEmptyStrings() {
        expert3.testOmitEmptyStrings();
    }

    @Test
    public void testLimit() {
        expert3.testLimit();
    }

    @Test
    public void testTrimResults() {
        expert3.testTrimResults();
    }

    @Test
    public void testTrimResults_CharMatcher() {
        expert3.testTrimResults_CharMatcher();
    }
}