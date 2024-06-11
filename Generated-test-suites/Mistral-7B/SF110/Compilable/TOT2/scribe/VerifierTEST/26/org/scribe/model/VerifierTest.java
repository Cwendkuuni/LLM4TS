package org.scribe.model;
import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {
    @Test
    public void testVerifierConstructor() {
        String value = "testValue";
        Verifier verifier = new Verifier(value);
        assertEquals(value, verifier.getValue());
    }

    @Test
    public void testVerifierGetter() {
        Verifier verifier = new Verifier("testValue");
        assertEquals("testValue", verifier.getValue());
    }
}