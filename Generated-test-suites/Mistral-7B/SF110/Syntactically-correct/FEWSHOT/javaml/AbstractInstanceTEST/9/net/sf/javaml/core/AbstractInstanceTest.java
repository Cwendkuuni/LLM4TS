package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testEmptyConstructor() {
        AbstractInstance instance = new DenseInstance(new double[0]);
        assertEquals(0, instance.getID());
        assertNull(instance.classValue());
        assertTrue(instance.iterator().hasNext());
        assertEquals(0.0, instance.iterator().next(), 0.0);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testConstructorWithClassValue() {
        Object classValue = "foo";
        AbstractInstance instance = new DenseInstance(new double[2], classValue);
        assertEquals(AbstractInstance.nextID, instance.getID());
        assertEquals(classValue, instance.classValue());
        assertFalse(instance.iterator().hasNext());
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[2], "foo");
        assertEquals(AbstractInstance.nextID, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        Iterator<Double> iterator = instance.iterator();
        Double value = iterator.next();
        assertEquals(1.0, value.doubleValue(), 0.0);
        value = iterator.next();
        assertEquals(2.0, value.doubleValue(), 0.0);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMinus() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5}, "bar");
        AbstractInstance difference = instance1.minus(instance2);
        Iterator<Double> differenceIterator = difference.iterator();
        Double firstDifference = differenceIterator.next();
        Double secondDifference = differenceIterator.next();

        Map<Integer, Double> instance1Map = new HashMap<>();
        instance1Map.put(0, 1.0);
        instance1Map.put(1, 2.0);

        Map<Integer, Double> instance2Map = new HashMap<>();
        instance2Map.put(0, 0.5);
        instance2Map.put(1, 1.5);

        for (int i = 0; i < difference.noAttributes(); ++i) {
            Double expectedDifference = instance1Map.get(i) - instance2Map.get(i);
            assertEquals(expectedDifference, firstDifference, 0.0);
            firstDifference = secondDifference;
            secondDifference = differenceIterator.next();
        }

        assertFalse(differenceIterator.hasNext());
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        AbstractInstance difference = instance.minus(3.0);
        Iterator<Double> differenceIterator = difference.iterator();
        Double firstDifference = differenceIterator.next();
        Double secondDifference = differenceIterator.next();

        Map<Integer, Double> instanceMap = new HashMap<>();
        instanceMap.put(0, 1.0);
        instanceMap.put(1, 2.0);

        for (int i = 0; i < difference.noAttributes(); ++i) {
            Double expectedDifference = instanceMap.get(i) - 3.0;
            assertEquals(expectedDifference, firstDifference, 0.0);
            firstDifference = secondDifference;
            secondDifference = differenceIterator.next();
        }

        assertFalse(differenceIterator.hasNext());
    }

    @Test
    public void testDivide() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        double divisor = 2.0;
        AbstractInstance quotient = instance1.divide(divisor);
        Iterator<Double> quotientIterator = quotient.iterator();
        Double firstQuotient = quotientIterator.next();
        Double secondQuotient = quotientIterator.next();

        Map<Integer, Double> instance1Map = new HashMap<>();
        instance1Map.put(0, 1.0);
        instance1Map.put(1, 2.0);

        for (int i = 0; i < quotient.noAttributes(); ++i) {
            Double expectedQuotient = instance1Map.get(i) / divisor;
            assertEquals(expectedQuotient, firstQuotient, 0.0);
            firstQuotient = secondQuotient;
            secondQuotient = quotientIterator.next();
        }

        assertFalse(quotientIterator.hasNext());
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        double multiplier = 2.0;
        AbstractInstance product = instance1.multiply(multiplier);
        Iterator<Double> productIterator = product.iterator();
        Double firstProduct = productIterator.next();
        Double secondProduct = productIterator.next();

        Map<Integer, Double> instance1Map = new HashMap<>();
        instance1Map.put(0, 1.0);
        instance1Map.put(1, 2.0);

        for (int i = 0; i < product.noAttributes(); ++i) {
            Double expectedProduct = instance1Map.get(i) * multiplier;
            assertEquals(expectedProduct, firstProduct, 0.0);
            firstProduct = secondProduct;
            secondProduct = productIterator.next();
        }

        assertFalse(productIterator.hasNext());
    }

    @Test
    public void testHashCodeEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 1.0}, "bar");

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance1);
        assertNotEquals(instance1, null);
        assertNotEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);

        Map<Integer, Double> instance1Map = new HashMap<>();
        instance1Map.put(0, 1.0);
        instance1Map.put(1, 2.0);

        Map<Integer, Double> instance2Map = new HashMap<>();
        instance2Map.put(0, 1.0);
        instance2Map.put(1, 2.0);

        Map<Integer, Double> instance3Map = new HashMap<>();
        instance3Map.put(0, 2.0);
        instance3Map.put(1, 1.0);

        assertEquals(instance1Map.hashCode(), instance1.hashCode());
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance1);
        assertNotEquals(instance1, null);
        assertNotEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0}, "bar");
        AbstractInstance product = instance1.multiply(instance2);
        Iterator<Double> productIterator = product.iterator();
        Double firstProduct = productIterator.next();
        Double secondProduct = productIterator.next();

        Map<Integer, Double> instance1Map = new HashMap<>();
        instance1Map.put(0, 1.0);
        instance1Map.put(1, 2.0);

        Map<Integer, Double> instance2Map = new HashMap<>();
        instance2Map.put(0, 3.0);
        instance2Map.put(1, 4.0);

        Map<Integer, Double> productMap = new HashMap<>();
        for (int i = 0; i < product.noAttributes(); ++i) {
            Double expectedProduct = instance1Map.get(i) * instance2Map.get(i);
            assertEquals(expectedProduct, firstProduct, 0.0);
            firstProduct = secondProduct;
            secondProduct = productIterator.next();
        }

        assertFalse(productIterator.hasNext());
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.0}, "bar");
        AbstractInstance quotient = instance1.divide(instance2);
        Iterator<Double> quotientIterator = quotient.iterator();
        Double firstQuotient = quotientIterator.next();
        Double secondQuotient = quotientIterator.next();

        Map<Integer, Double> instance1Map = new HashMap<>();
        instance1Map.put(0, 1.0);
        instance1Map.put(1, 2.0);

        Map<Integer, Double> instance2Map = new HashMap<>();
        instance2Map.put(0, 0.5);
        instance2Map.put(1, 1.0);

        Map<Integer, Double> quotientMap = new HashMap<>();
        for (int i = 0; i < quotient.noAttributes(); ++i) {
            Double expectedQuotient = instance1Map.get(i) / instance2Map.get(i);
            assertEquals(expectedQuotient, firstQuotient, 0.0);
            firstQuotient = secondQuotient;
            secondQuotient = quotientIterator.next();
        }

        assertFalse(quotientIterator.hasNext());
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        double addend = 3.0;
        AbstractInstance sum = instance.add(addend);
        Iterator<Double> sumIterator = sum.iterator();
        Double firstSum = sumIterator.next();
        Double secondSum = sumIterator.next();

        Map<Integer, Double> instanceMap = new HashMap<>();
        instanceMap.put(0, 1.0);
        instanceMap.put(1, 2.0);

        for (int i = 0; i < sum.noAttributes(); ++i) {
            Double expectedSum = instanceMap.get(i) + addend;
            assertEquals(expectedSum, firstSum, 0.0);
            firstSum = secondSum;
            secondSum = sumIterator.next();
        }

        assertFalse(sumIterator.hasNext());
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "foo");
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0}, "bar");
        AbstractInstance sum = instance1.add(instance2);
        Iterator<Double> sumIterator = sum.iterator();
        Double firstSum = sumIterator.next();
        Double secondSum = sumIterator.next();

        Map<Integer, Double> instance1Map = new HashMap<>();
        instance1Map.put(0, 1.0);
        instance1Map.put(1, 2.0);

        Map<Integer, Double> instance2Map = new HashMap<>();
        instance2Map.put(0, 3.0);
        instance2Map.put(1, 4.0);

        Map<Integer, Double> sumMap = new HashMap<>();
        for (int i = 0; i < sum.noAttributes(); ++i) {
            Double expectedSum = instance1Map.get(i) + instance2Map.get(i);
            assertEquals(expectedSum, firstSum, 0.0);
            firstSum = secondSum;
            secondSum = sumIterator.next();
        }

        assertFalse(sumIterator.hasNext());
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0}, "foo");
        AbstractInstance squareRoot = instance.sqrt();
        Iterator<Double> squareRootIterator = squareRoot.iterator();
        Double firstSquareRoot = squareRootIterator.next();
        Double secondSquareRoot = squareRootIterator.next();

        Map<Integer, Double> instanceMap = new HashMap<>();
        instanceMap.put(0, 1.0);
        instanceMap.put(1, 4.0);

        for (int i = 0; i < squareRoot.noAttributes(); ++i) {
            Double expectedSquareRoot = Math.sqrt(instanceMap.get(i));
            assertEquals(expectedSquareRoot, firstSquareRoot, 0.0);
            firstSquareRoot = secondSquareRoot;
            secondSquareRoot = squareRootIterator.next();
        }

        assertFalse(squareRootIterator.hasNext());
    }
}