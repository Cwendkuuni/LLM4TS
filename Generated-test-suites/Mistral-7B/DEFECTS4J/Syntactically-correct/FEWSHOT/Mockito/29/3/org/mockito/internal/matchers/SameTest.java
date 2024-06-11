package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class SameTest {

    @Test
    public void testSameObjectMatches() {
        Object wanted = new Object();
        Object actual = wanted;
        boolean result = new Same(wanted).matches(actual);
        assertTrue(result);
    }

    @Test
    public void testSameStringMatches() {
        String wanted = "test string";
        String actual = wanted;
        boolean result = new Same(wanted).matches(actual);
        assertTrue(result);
    }

    @Test
    public void testDifferentObjectsDoNotMatch() {
        Object wanted = new Object();
        Object actual = new String("test string");
        boolean result = new Same(wanted).matches(actual);
        assertFalse(result);
    }

    @Test
    public void testNullValuesAreConsideredSame() {
        Object wanted = null;
        Object actual = null;
        boolean result = new Same(wanted).matches(actual);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgumentMatcherCannotHandleNonObjectType() {
        new Same(1);
    }

    @Test
    public void testDescribeTo() {
        Object wanted = "test string";
        Description description = new Description();
        new Same(wanted).describeTo(description);
        String expectedDescription = "same(test string)";
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testDescribeToWithString() {
        Object wanted = "test string";
        Description description = new Description();
        new Same(wanted).describeTo(description);
        String expectedDescription = "same(\"test string\")";
        assertEquals(expectedDescription, description.toString());
    }

    @Test
    public void testDescribeToWithCharacter() {
        char wanted = 't';
        Description description = new Description();
        new Same(wanted).describeTo(description);
        String expectedDescription = "same('t')";
        assertEquals(expectedDescription, description.toString());
    }
}