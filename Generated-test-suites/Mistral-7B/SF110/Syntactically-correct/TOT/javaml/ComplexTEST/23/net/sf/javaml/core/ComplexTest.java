package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {
    // Previous test cases

    @Test(expected = IllegalArgumentException.class)
    public void testTimesWithNullComplex() {
        Complex a = new Complex(1.0, 2.0);
        Complex b = null;
        a.times(b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimesWithNullDouble() {
        Complex a = new Complex(1.0, 2.0);
        double alpha = null;
        a.times(alpha);
    }
}

// Test the edge cases and error scenarios