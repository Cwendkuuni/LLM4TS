package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testNextID() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        assertEquals(0, AbstractInstance.nextID);
        assertEquals(1, instance1.getID());
        assertEquals(2, instance2.getID());
    }

    @Test
    public void testInstanceConstructorNullClassValue() {
        assertThrows(NullPointerException.class, () -> new AbstractInstance(null));
    }

    @Test
    public void testInstanceConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertEquals(0, instance.getID());
        assertNull(instance.classValue());

        AbstractInstance instanceWithClassValue = new AbstractInstance("Test Class");
        assertEquals("Test Class", instanceWithClassValue.classValue());
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(0, instance.getID());

        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});
        assertEquals(1, instance2.getID());

        AbstractInstance instance3 = new DenseInstance(new double[]{5.0, 6.0});
        assertEquals(2, instance3.getID());
    }

    @Test
    public void testIterate() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();

        double[] expectedValues = {1.0, 2.0};
        assertArrayEquals(expectedValues, iterator.toArray());
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.5, 2.5});

        AbstractInstance result = instance1.minus(instance2);
        double[] expectedValues = {-0.5, -0.5};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.minus(3.0);

        double[] expectedValues = {-2.0, -1.0};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 4.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 2.0});

        AbstractInstance result = instance.divide(instance2);
        double[] expectedValues = {2.0, 2.0};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 4.0});

        AbstractInstance result = instance.divide(2.0);
        double[] expectedValues = {2.0, 2.0};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});

        AbstractInstance result = instance1.multiply(instance2);
        double[] expectedValues = {3.0, 8.0};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testMultiplyDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{3.0, 4.0});

        AbstractInstance result = instance.multiply(2.0);
        double[] expectedValues = {6.0, 8.0};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});

        AbstractInstance result = instance1.add(instance2);
        double[] expectedValues = {4.0, 6.0};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.add(3.0);
        double[] expectedValues = {4.0, 5.0};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{9.0, 16.0});

        AbstractInstance result = instance.sqrt();
        double[] expectedValues = {3.0, 4.0};
        assertArrayEquals(expectedValues, result.toArray());
    }

    @Test
    public void testHashCodeEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{3.0, 4.0});

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance1);

        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertEquals(instance1, instance2);

        assertNotSame(instance1, instance3);
        assertNotSame(instance1, null);
    }

    @Test
    public void testMinusNullInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertThrows(NullPointerException.class, () -> instance.minus(null));
    }

    @Test
    public void testMinusNullDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertThrows(NullPointerException.class, () -> instance.minus(null));
    }

    @Test
    public void testDivideByZeroInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertThrows(ArithmeticException.class, () -> instance.divide(new DenseInstance(new double[]{0.0, 0.0})));
    }

    @Test
    public void testDivideByZeroDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertThrows(ArithmeticException.class, () -> instance.divide(0.0));
    }

    @Test
    public void testMultiplyByZeroInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance zeroInstance = new DenseInstance(new double[]{0.0, 0.0});
        AbstractInstance result = instance.multiply(zeroInstance);
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }

    @Test
    public void testMultiplyByZeroDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance result = instance.multiply(0.0);
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.0, result.get(1), 0.001);
    }
}