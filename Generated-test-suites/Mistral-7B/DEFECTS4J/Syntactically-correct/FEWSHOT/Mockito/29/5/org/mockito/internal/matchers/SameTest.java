package org.mockito.internal.matchers;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class SameTest {

    @Test
    public void testSameString() {
        Object wanted = "mockito";
        Matcher<String> sameMockito = new Same(wanted);

        String str1 = "mockito";
        String str2 = "mockito";

        assertThat(sameMockito, matches(str1));
        assertThat(sameMockito, matches(str2));
    }

    @Test
    public void testSameCharacter() {
        Object wanted = 'a';
        Matcher<Character> sameA = new Same(wanted);

        Character c1 = 'a';
        Character c2 = 'a';

        assertThat(sameA, matches(c1));
        assertThat(sameA, matches(c2));
    }

    @Test
    public void testSameObject() {
        Object wanted = new Object();
        Matcher<Object> sameObject = new Same(wanted);

        Object obj1 = new Object();
        Object obj2 = new Object();

        assertThat(sameObject, matches(obj1));
        assertThat(sameObject, matches(obj2));
    }

    @Test
    public void testNotSameDifferentObjects() {
        Object wanted = new Object();
        Matcher<Object> sameObject = new Same(wanted);

        Object obj1 = new Object();
        Object obj2 = new String("different object");

        assertThat(sameObject, !matches(obj2));
    }

    @Test
    public void testDescription() {
        Object wanted = "mockito";
        Matcher<String> sameMockito = new Same(wanted);

        Description description = new Description();
        sameMockito.describeTo(description);

        String expectedDescription = "same(mockito)";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }
}