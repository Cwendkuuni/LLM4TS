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
        assertThat(obj, same(obj));
    }

    @Test
    public void testSameString() {
        String str = "test";
        assertThat(str, same(str));
    }

    @Test
    public void testSameCharacter() {
        Character c = 'a';
        Matcher<Character> charMatcher = same(c);
        assertThat('a', charMatcher);
        assertThat('a', charMatcher);
        assertThat('b', is(not(charMatcher)));
    }

    @Test
    public void testSameNull() {
        assertThat(null, same(null));
    }

    @Test
    public void testSameArray() {
        Object[] array = new Object[0];
        Object[] array2 = new Object[0];
        assertThat(array, same(array2));
    }

    @Test
    public void testDescribeTo() {
        Object obj = new Object();
        Description description = new Description();
        Same matcher = new Same(obj);
        matcher.describeTo(description);
        String expected = "same(" + obj.toString() + ")";
        String actual = description.toString();
        assertEquals(expected, actual);
    }
}