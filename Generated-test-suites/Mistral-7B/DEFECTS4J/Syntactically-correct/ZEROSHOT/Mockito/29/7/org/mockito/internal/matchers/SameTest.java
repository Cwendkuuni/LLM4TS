package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoMock;

import java.io.Serializable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @MockitoMock
    private Serializable mockSerializable;

    @Test
    public void testMatchesSameObjects() {
        Same same = new Same(mockSerializable);
        Serializable actual = mockSerializable;

        assertThat(same.matches(actual), is(true));
    }

    @Test
    public void testMatchesNullAndNull() {
        Same same = new Same(null);

        assertThat(same.matches(null), is(true));
    }

    @Test
    public void testMatchesNullAndNonNull() {
        Same same = new Same(mockSerializable);
        Object nonNullObject = new Object();

        assertThat(same.matches(nonNullObject), is(false));
    }

    @Test
    public void testMatchesDifferentObjectsOfSameClass() {
        Same same = new Same(mockSerializable);
        Serializable anotherSerializable = mock(Serializable.class);

        assertThat(same.matches(anotherSerializable), is(false));
    }

    @Test
    public void testMatchesDifferentObjectsOfDifferentClasses() {
        Same same = new Same(mockSerializable);
        String anotherString = "different string";

        assertThat(same.matches(anotherString), is(false));
    }

    @Test
    public void testDescribeToMatchesSameObjects() {
        Same same = new Same(mockSerializable);

        Description description = new Description();
        same.describeTo(description);

        String expectedDescription = "same(org.mockito.internal.matchers.Same@";
        String actualDescription = description.toString();

        assertThat(actualDescription, is(expectedDescription));
    }

    @Test
    public void testDescribeToMatchesNull() {
        Same same = new Same(null);

        Description description = new Description();
        same.describeTo(description);

        String expectedDescription = "same(null)";
        String actualDescription = description.toString();

        assertThat(actualDescription, is(expectedDescription));
    }

    @Test
    public void testDescribeToMatchesDifferentObjects() {
        Same same = new Same(mockSerializable);
        String anotherString = "different string";

        Description description = new Description();
        same.describeTo(description);

        String expectedDescription = "same(org.mockito.internal.matchers.Same@";
        String actualDescription = description.toString();

        assertThat(actualDescription, is(not(expectedDescription.substring(2, expectedDescription.length() - 1))));
    }

    @Test
    public void testMatchesWithInvocation() {
        Serializable wanted = mockSerializable;
        Serializable actual = mockSerializable;
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getArgument(0)).thenReturn(wanted);

        Same same = new Same(wanted);

        assertThat(same.matches(actual), is(true));
    }

    @Test
    public void testMatchesWithNonMatchingObjects() {
        Serializable wanted = mockSerializable;
        String anotherString = "different string";
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getArgument(0)).thenReturn(wanted);

        Same same = new Same(wanted);

        assertThat(same.matches(anotherString), is(false));
    }

    @Test
    public void testSerializable() {
        Serializable wanted = mockSerializable;
        Same same = new Same(wanted);

        // Serialize and deserialize
        byte[] serialized = new java.io.ByteArrayOutputStream().toByteArray(new java.io.ObjectOutputStream(new java.io.ByteArrayOutputStream()).writeObject(same));
        Same deserialized = (Same) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(serialized)).readObject();

        assertThat(deserialized.matches(wanted), is(true));
    }
}