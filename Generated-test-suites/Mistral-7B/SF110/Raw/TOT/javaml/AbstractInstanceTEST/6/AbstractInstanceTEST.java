package net.sf.javaml.core;

import org.junit.*;
import static org.junit.Assert.*;;

public class AbstractInstanceTest {
    @Test
    public void testSubtractInstances() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0, 1.5});
        AbstractInstance expectedResult = new DenseInstance(new double[]{0.5, 1.0, 1.5});

        AbstractInstance result = instance1.minus(instance2);

        Assert.assertArrayEquals(expectedResult.toArray(), result.toArray(), 0.001);
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});

        Assert.assertTrue(instance1.equals(instance1));
        Assert.assertTrue(instance1.equals(instance2));
        Assert.assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testSubtractEdgeCases() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new SparseInstance();

        try {
            AbstractInstance result = instance1.minus(instance2);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        AbstractInstance instance3 = new DenseInstance(new double[]{-1.0, 2.0, 3.0});
        AbstractInstance result = instance1.minus(instance3);

        AbstractInstance expectedResult = new DenseInstance(new double[]{0.5, 0.0, 2.5});
        Assert.assertArrayEquals(expectedResult.toArray(), result.toArray(), 0.001);

        AbstractInstance instance4 = new DenseInstance(null);

        try {
            AbstractInstance result = instance1.minus(instance4);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}