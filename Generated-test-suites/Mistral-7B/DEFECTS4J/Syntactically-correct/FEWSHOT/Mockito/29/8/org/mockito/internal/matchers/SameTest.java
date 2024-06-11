package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.same;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameObject() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        Matcher<Object> sameMatcher = same(obj1);
        assertThat(sameMatcher, is(same(obj1)));
        // Add more test cases as needed
    }

    @Test
    public void testSameString() {
        String str1 = "test string";
        String str2 = "test string";

        Matcher<String> sameMatcher = same(str1);
        assertThat(sameMatcher, is(same(str1)));
        // Add more test cases as needed
    }

    @Test
    public void testSameCharacter() {
        Character char1 = 'a';
        Character char2 = 'a';

        Matcher<Character> sameMatcher = same(char1);
        assertThat(sameMatcher, is(same(char1)));
        // Add more test cases as needed
    }

    @Test
    public void testSameNull() {
        Object nullObject = null;

        Matcher<Object> sameMatcher = same(nullObject);
        // Add more test cases as needed
    }

    @Test
    public void testDescribeToString() {
        Object obj = new Object();
        String description = "";

        Same sameMatcher = new Same(obj);
        sameMatcher.describeTo(new Description() {
            @Override
            public void appendText(String text) {
                description += text;
            }
        });

        assertEquals("same(<" + obj.toString() + ">)", description);
    }
}