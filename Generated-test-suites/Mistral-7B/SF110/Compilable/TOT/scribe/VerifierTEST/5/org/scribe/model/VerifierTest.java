package org.scribe.model;
import org.junit.Test;
import static org.junit.Assert.*;;

public class VerifierTest {
    

    @Test
    public void testVerifierValue() {
        Verifier verifier = new Verifier("testValue");
        String actual = verifier.getValue();
        String expected = "testValue";
        assertEquals(expected, actual);
    }

    
}