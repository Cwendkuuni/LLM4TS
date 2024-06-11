package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = wanted;

        assertTrue(sameMatcher.matches(actual));
        assertThat(sameMatcher, is(sameInstance(sameMatcher)));
        assertThat(sameMatcher, is(sameInstance(wanted)));
        assertThat(sameMatcher, is(sameInstance(new Object())));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = new Object();

        assertFalse(sameMatcher.matches(actual));
        assertThat(sameMatcher, is(not(sameInstance(actual))));
        assertThat(sameMatcher, is(not(sameInstance(wanted))));
        assertThat(sameMatcher, is(not(sameInstance(new Object()))));
    }

    @Test
    public void testStringMatching() {
        String wanted = "Hello World";
        Same sameMatcher = new Same(wanted);

        String actual = "Hello World";

        assertTrue(sameMatcher.matches(actual));
        assertThat(sameMatcher, is(sameInstance(sameMatcher)));
        assertThat(sameMatcher, is(sameInstance(wanted)));
        assertThat(sameMatcher, is(sameInstance(actual)));
    }

    @Test
    public void testCharacterMatching() {
        char wanted = 'A';
        Same sameMatcher = new Same(wanted);

        char actual = 'A';

        assertTrue(sameMatcher.matches(actual));
        assertThat(sameMatcher, is(sameInstance(sameMatcher)));
        assertThat(sameMatcher, is(sameInstance(wanted)));
        assertThat(sameMatcher, is(sameInstance(actual)));
    }

    @Test
    public void testDescription() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(Object)";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescriptionWithString() {
        String wanted = "Hello World";
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(\"Hello World\")";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescriptionWithCharacter() {
        char wanted = 'A';
        Same sameMatcher = new Same(wanted);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same('A')";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testMatchingWithAnswer() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object mock = mock(Object.class);
        Answer answer = invocation -> sameMatcher.matches(invocation.getArgumentAt(0, Object.class));

        when(mock.equals(wanted)).thenAnswer(answer);

        assertTrue(mock.equals(wanted));
        verify(mock).equals(wanted);
    }

    private boolean sameInstance(Object expected) {
        return expected == this.wanted || (expected != null && expected.getClass() == this.wanted.getClass() && expected.equals(this.wanted));
    }

    private Matcher sameInstance(Matcher matcher) {
        return is(matcher);
    }

    private InvocationOnMock invocation() {
        return mock(InvocationOnMock.class);
    }

}