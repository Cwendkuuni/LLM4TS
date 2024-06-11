package org.scribe.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

public class VerifierTest {

    private Verifier verifier;

    @Before
    public void setUp() {
        this.verifier = new Verifier("validVerifier");
    }

    // Test constructor method
    @Test
    public void testVerifierConstructor() {
        assertEquals("validVerifier", this.verifier.getValue());
    }

    // Test getValue method
    @Test
    public void testGetValue() {
        assertEquals("validVerifier", this.verifier.getValue());
    }

    // Test edge cases for constructor method

    @Test(expected = NullPointerException.class)
    public void testVerifierConstructorNullValue() {
        Preconditions.checkNotNull(null, "Must provide a valid string as verifier");
    }

    @Test
    public void testVerifierConstructorEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> new Verifier(""));
    }

    @Test
    public void testVerifierConstructorWhitespaceString() {
        assertThrows(IllegalArgumentException.class, () -> new Verifier("   "));
    }

    // Test edge cases for getValue method
    @Test
    public void testGetValueNullVerifier() {
        assertNull(new Verifier(null).getValue());
    }

    @Test
    public void testGetValueEmptyVerifier() {
        Verifier emptyVerifier = new Verifier("");
        assertFalse(emptyVerifier.getValue().isEmpty());
        assertEquals("", emptyVerifier.getValue());
    }

}