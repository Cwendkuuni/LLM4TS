package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testAbstractInstanceConstructor() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
    }

    @Test
    public void testAbstractInstanceHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance3 = new DenseInstance(new double[] { 2.0, 1.0 });

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotSame(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testAbstractInstanceEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance3 = new DenseInstance(new double[] { 2.0, 1.0 });

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testAbstractInstanceGetID() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        assertEquals(0, instance.getID());
    }

    @Test
    public void testAbstractInstanceIterator() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        List<Double> expectedList = Arrays.asList(1.0, 2.0);

        Iterator<Double> actualIterator = instance.iterator();

        assertEquals(expectedList.size(), instance.noAttributes());

        List<Double> actualList = new ArrayList<>();
        while (actualIterator.hasNext()) {
            actualList.add(actualIterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceMinus() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
        AbstractInstance result = instance1.minus(instance2);

        List<Double> expectedList = Arrays.asList(-2.0, -2.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance result = instance.minus(5.0);

        List<Double> expectedList = Arrays.asList(-4.0, -2.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractInstanceRemoveFromIterator() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        Iterator<Double> iterator = instance.iterator();
        iterator.remove();
    }

    @Test
    public void testAbstractInstanceDivide() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance result = instance.divide(2.0);

        List<Double> expectedList = Arrays.asList(0.5, 1.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test(expected = ArithmeticException.class)
    public void testAbstractInstanceDivideByZero() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        instance.divide(0.0);
    }

    @Test
    public void testAbstractInstanceMultiply() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
        AbstractInstance result = instance1.multiply(instance2);

        List<Double> expectedList = Arrays.asList(3.0, 8.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceAdd() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
        AbstractInstance result = instance1.add(instance2);

        List<Double> expectedList = Arrays.asList(4.0, 6.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance result = instance.add(5.0);

        List<Double> expectedList = Arrays.asList(6.0, 7.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceSqrt() {
        AbstractInstance instance = new DenseInstance(new double[] { 4.0, 9.0 });
        AbstractInstance result = instance.sqrt();

        List<Double> expectedList = Arrays.asList(2.0, 3.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceAddInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
        AbstractInstance result = instance1.add(instance2);

        List<Double> expectedList = Arrays.asList(4.0, 6.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceSubtractInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
        AbstractInstance result = instance1.subtract(instance2);

        List<Double> expectedList = Arrays.asList(-2.0, -2.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceMultiplyInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
        AbstractInstance result = instance1.multiply(instance2);

        List<Double> expectedList = Arrays.asList(3.0, 8.0);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceDivideInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] { 1.0, 2.0 });
        AbstractInstance instance2 = new DenseInstance(new double[] { 3.0, 4.0 });
        AbstractInstance result = instance1.divide(instance2);

        List<Double> expectedList = Arrays.asList(0.3333333333333333, 0.5);

        Iterator<Double> iterator = result.iterator();

        List<Double> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAbstractInstanceClassValue() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        instance.setClassValue("foo");

        assertEquals("foo", instance.classValue());
    }

    @Test
    public void testAbstractInstanceClassValueNull() {
        AbstractInstance instance = new DenseInstance(new double[] { 1.0, 2.0 });
        instance.setClassValue(null);

        assertNull(instance.classValue());
    }

    @Test
    public void testAbstractInstancePutGet() {
        AbstractInstance instance = new DenseInstance(new double[2]);
        instance.put(0, 1.0);
        instance.put(1, 2.0);

        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAbstractInstancePutIndexOutOfBounds() {
        AbstractInstance instance = new DenseInstance(new double[2]);
        instance.put(2, 3.0);
    }

    @Test
    public void testAbstractInstanceNoAttributes() {
        AbstractInstance instance = new DenseInstance(new double[0]);

        assertEquals(0, instance.noAttributes());
    }
}