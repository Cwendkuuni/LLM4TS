package org.scribe.model.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.scribe.model.Verifier;
import org.scribe.utils.Preconditions;

@RunWith(org.junit.runner.JUnit4.class)
public class VerifierTest {

    @Test
    public void testVerifierInstance() {
        String testValue = "testVerifierValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals("Expected verifier value to be set correctly", testValue, verifier.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifierNullValue() {
        Preconditions mockPreconditions = Preconditions.createMock(Preconditions.class); // mock Preconditions class
        Verifier verifier = new Verifier(null);
        mockPreconditions.expects(1).method("checkNotNull").withParameter("value", isNull());
        mockPreconditions.rethrows(IllegalArgumentException.class);
        verifier = mockPreconditions.createMockStatic("org.scribe.utils.Preconditions.checkNotNull");
        verifier.checkNotNull(test, "Must provide a valid string as verifier");
    }

    @Test
    public void testVerifierGetValue() {
        String testValue = "testVerifierValue";
        Verifier verifier = new Verifier(testValue);
        assertEquals("Expected getValue method to return the same value as constructor", testValue, verifier.getValue());
    }
}