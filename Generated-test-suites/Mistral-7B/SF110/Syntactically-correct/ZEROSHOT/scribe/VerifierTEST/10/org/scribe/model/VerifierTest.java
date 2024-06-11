package org.scribe.model;  // make sure the test package is the same as the Verifier class

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VerifierTest {

    @Test
    public void testVerifierConstructionWithNullValueThrowsNPE() {
        String nullValue = null;

        try {
            new Verifier(nullValue);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testVerifierConstructionWithValidValueSetsValue() {
        String validValue = "testValue";

        Verifier verifier = new Verifier(validValue);

        assertEquals(validValue, verifier.getValue());
    }
}