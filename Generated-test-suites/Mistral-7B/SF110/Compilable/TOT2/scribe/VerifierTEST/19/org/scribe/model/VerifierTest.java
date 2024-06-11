package org.scribe.model;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class VerifierTest {
    @Test
    public void testVerifierConstruction() {
        String validValue = "testValue";
        Verifier verifier = new Verifier(validValue);
        assertNotNull(verifier);
    }
}