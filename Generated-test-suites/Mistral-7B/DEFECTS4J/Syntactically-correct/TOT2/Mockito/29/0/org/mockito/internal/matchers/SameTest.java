package org.junit.tests;

import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testConstructor() {
        Object expected = "testString";
        Same sameMatcher = new Same(expected);
    }

    @Test
    public void testMatches() {
        Object expected = new String("testString");
        Object actual = new String("testString");
        Same sameMatcher = new Same(expected);
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testMatches_NullExpected() {
        Same sameMatcher = new Same(null);
        assertFalse(sameMatcher.matches(new Object()));
    }

    @Test
    public void testMatches_NullActual() {
        Object expected = new String("testString");
        Same sameMatcher = new Same(expected);
        assertFalse(sameMatcher.matches(null));
    }

    @Test
    public void testMatches_DifferentClasses() {
        Object expected = new String("testString");
        Object actual = new Integer(42);
        Same sameMatcher = new Same(expected);
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testMatches_SameObjects() {
        Object expected = new String("testString");
        Object actual = expected;
        Same sameMatcher = new Same(expected);
        assertTrue(sameMatcher.matches(actual));
    }

    @Test(expected = NullPointerException.class)
    public void testMatches_NullExpectedNullActual() {
        Same sameMatcher = new Same(null);
        sameMatcher.matches(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatches_NonSerializableObject() {
        Object expected = new Thread();
        Same sameMatcher = new Same(expected);
    }

    @Test
    public void testDescribeTo() {
        Object expected = new String("testString");
        Description description = new Description();
        Same sameMatcher = new Same(expected);
        sameMatcher.describeTo(description);
        assertEquals("same(testString)", description.toString());
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        Object expected = new String("testString");
        Same sameMatcher = new Same(expected);
        byte[] serialized = new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(sameMatcher);
        Same deserialized = (Same) new ObjectInputStream(new ByteArrayInputStream(serialized)).readObject();
        Object actual = new String("testString");
        assertTrue(deserialized.matches(actual));
    }
}