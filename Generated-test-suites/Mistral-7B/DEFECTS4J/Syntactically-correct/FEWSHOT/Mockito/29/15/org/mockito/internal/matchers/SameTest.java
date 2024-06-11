package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.same;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object obj = new Object();
        Matcher<Object> matcher = same(obj);
        Object actual = obj;

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testSameString() {
        String str = "test";
        Matcher<String> matcher = same(str);
        String actual = str;

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testSameCharacter() {
        char c = 'a';
        Matcher<Character> matcher = same(c);
        Character actual = c;

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testSameNull() {
        Object nullObject = null;
        Matcher<Object> matcher = same(nullObject);
        Object actual = null;

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testNotSameInstance() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Matcher<Object> matcher = same(obj1);
        Object actual = obj2;

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testNotSameString() {
        String str1 = "test1";
        String str2 = "test2";
        Matcher<String> matcher = same(str1);
        String actual = str2;

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testNotSameCharacter() {
        char c1 = 'a';
        char c2 = 'b';
        Matcher<Character> matcher = same(c1);
        Character actual = c2;

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testDescribeTo() throws Exception {
        Object obj = new Object();

        Description description = new Description();
        Same sameMatcher = new Same(obj);
        sameMatcher.describeTo(description);

        String expectedDescription = "same(" + obj.toString() + ")";
        String actualDescription = description.toString();

        assertThat(actualDescription, is(expectedDescription));
    }
}