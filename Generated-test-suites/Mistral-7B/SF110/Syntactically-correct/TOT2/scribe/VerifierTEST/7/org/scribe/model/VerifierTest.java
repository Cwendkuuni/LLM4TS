package org.scribe.model;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.scribe.utils.Preconditions;

@RunWith(JUnit4.class)
public class VerifierTest {

    @Test
    public void testVerifierConstruction() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testVerifierNullConstructor() {
        new Verifier(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierEmptyConstructor() {
        new Verifier("");
    }

    @Test
    public void testVerifierLongStringConstructor() {
        String longString = "This is a long string to test the constructor with a string longer than the average.";
        new Verifier(longString);
    }

    @Test
    public void testGetValue() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }

    @Test
    public void testGetValueEmptyString() {
        Verifier verifier = new Verifier("");
        assertEquals("", verifier.getValue());
    }

    @Test
    public void testGetValueNullString() {
        Verifier verifier = new Verifier(null);
        assertNull(verifier.getValue());
    }

    @Test
    public void testGetValueLongString() {
        Verifier verifier = new Verifier("This is a long string to test the getValue() method with a long string.");
        assertEquals("This is a long string to test the getValue() method with a long string.", verifier.getValue());
    }

    @Test
    public void testGetValueWhitespaceString() {
        Verifier verifier = new Verifier("   ");
        assertEquals("", verifier.getValue());
    }
}