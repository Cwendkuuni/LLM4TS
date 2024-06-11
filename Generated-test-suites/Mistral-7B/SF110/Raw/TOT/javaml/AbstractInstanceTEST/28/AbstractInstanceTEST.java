
package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        int id = instance.getID();
        assertEquals(id, instance.getID());
        AbstractInstance anotherInstance = new DenseInstance(new double[5]);
        assertNotSame(id, anotherInstance.getID());
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{-1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{-2.0, -1.0, 0.0, 1.0, 2.0});
        AbstractInstance expectedResult = new DenseInstance(new double[]{-3.0, 1.0, 3.0, 3.0, 3.0});
        AbstractInstance result = instance1.minus(instance2);
        for (int i = 0; i < instance1.noAttributes(); i++) {
            assertEquals(expectedResult.get(i), result.get(i), 0.0);
        }
    }

    @Test
    public void testDivideByZero() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        try {
            instance.divide(0.0);
            fail("An exception should be thrown when dividing by zero");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        AbstractInstance expectedResult = new DenseInstance(new double[]{1.0, 0.5, 1.5, 0.25, 0.25});
        AbstractInstance result = instance.divide(2.0);
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(expectedResult.get(i), result.get(i), 0.001);
        }
    }
}