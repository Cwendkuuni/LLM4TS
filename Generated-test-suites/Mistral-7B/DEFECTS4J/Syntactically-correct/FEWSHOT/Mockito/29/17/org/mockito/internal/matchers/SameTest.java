package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstanceMatches() {
        Object obj = new Object();
        Same sameObj = new Same(obj);

        Matcher<Object> matcher = sameObj;
        Object actual = obj;

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testSameInstanceDoesNotMatchDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same sameObj = new Same(obj1);

        Matcher<Object> matcher = sameObj;
        Object actual = obj2;

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameStringMatches() {
        String str = "test";
        Same sameStr = new Same(str);

        Matcher<String> matcher = sameStr;
        String actual = str;

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testSameStringDoesNotMatchDifferentStrings() {
        String str1 = "test1";
        String str2 = "test2";
        Same sameStr = new Same(str1);

        Matcher<String> matcher = sameStr;
        String actual = str2;

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testSameCharacterMatches() {
        char c = 'a';
        Same sameC = new Same(c);

        Matcher<Character> matcher = sameC;
        Character actual = c;

        assertThat(matcher.matches(actual), is(true));
    }

    @Test
    public void testSameCharacterDoesNotMatchDifferentCharacters() {
        char c1 = 'a';
        char c2 = 'b';
        Same sameC = new Same(c1);

        Matcher<Character> matcher = sameC;
        Character actual = c2;

        assertThat(matcher.matches(actual), is(false));
    }

    @Test
    public void testDescriptionMatchesExpectedObject() {
        Object obj = new Object();
        Same sameObj = new Same(obj);

        Description description = new Description();
        sameObj.describeTo(description);

        String expectedDescription = String.format("same(%s)", obj.toString());
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }
}