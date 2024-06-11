// Your combined test cases should be in this file

package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;

public class ComplexTest {

    // Test cases for Complex class methods go here

    @Test
    public void testAbs() {
        Complex complex = new Complex(3.0, 4.0);
        double expected = Math.sqrt(3.0 * 3.0 + 4.0 * 4.0);
        assertEquals(expected, complex.abs());
    }

    // ... other test cases

    @Test
    public void testToString() {
        Complex complex = new Complex(1.0, 2.0);
        String expected = "1.0 + 2.0i";
        String result = complex.toString();
        assertEquals(expected, result);
    }
}

// Test cases for Complex static methods go here