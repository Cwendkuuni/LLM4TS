package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testNextID() {
        assertEquals(0, AbstractInstance.nextID);

        AbstractInstance instance1 = new DenseInstance(new double[1]);
        AbstractInstance instance2 = new DenseInstance(new double[2]);
        AbstractInstance instance3 = new DenseInstance(new double[3]);

        assertEquals(1, AbstractInstance.nextID);
        assertEquals(1, instance1.getID());
        assertEquals(2, instance2.getID());
        assertEquals(3, instance3.getID());
    }

    @Test
    public void testConstructor() {
        AbstractInstance instance = new DenseInstance(new double[0]);
        assertNull(instance.classValue());

        Object classValue = new Object();
        AbstractInstance instance2 = new DenseInstance(classValue);
        assertEquals(classValue, instance2.classValue());
        assertEquals(AbstractInstance.nextID, instance2.getID());

        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 1.0);
        attributes.put("feature2", 2.0);

        AbstractInstance instance3 = new DenseInstance(attributes);
        assertEquals(attributes, ((Map<String, Double>)instance3).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        assertEquals(AbstractInstance.nextID, instance3.getID());
    }

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[0]);
        assertEquals(0, instance.getID());

        Object classValue = new Object();
        AbstractInstance instance2 = new DenseInstance(classValue);
        assertEquals(1, instance2.getID());

        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 1.0);
        attributes.put("feature2", 2.0);
        AbstractInstance instance3 = new DenseInstance(attributes);
        assertEquals(3, instance3.getID());
    }

    @Test
    public void testIterator() {
        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 1.0);
        attributes.put("feature2", 2.0);
        AbstractInstance instance = new DenseInstance(attributes);

        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2.0, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMinus() {
        Map<String, Double> attributes1 = new HashMap<>();
        attributes1.put("feature1", 1.0);
        attributes1.put("feature2", 2.0);
        AbstractInstance instance1 = new DenseInstance(attributes1);

        Map<String, Double> attributes2 = new HashMap<>();
        attributes2.put("feature1", 0.5);
        attributes2.put("feature2", 1.5);
        AbstractInstance instance2 = new DenseInstance(attributes2);

        AbstractInstance result = instance1.minus(instance2);

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", 0.5);
        expectedAttributes.put("feature2", 0.5);
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test
    public void testMinusDouble() {
        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 3.0);
        attributes.put("feature2", 2.0);
        AbstractInstance instance = new DenseInstance(attributes);

        AbstractInstance result = instance.minus(1.5);

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", 1.5);
        expectedAttributes.put("feature2", 0.5);
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test
    public void testDivide() {
        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 6.0);
        attributes.put("feature2", 3.0);
        AbstractInstance instance = new DenseInstance(attributes);

        AbstractInstance result = instance.divide(2.0);

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", 3.0);
        expectedAttributes.put("feature2", 1.5);
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDivideByZero() {
        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 3.0);
        attributes.put("feature2", 3.0);
        AbstractInstance instance = new DenseInstance(attributes);

        instance.divide(0.0);
    }

    @Test
    public void testMultiply() {
        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 2.0);
        attributes.put("feature2", 3.0);
        AbstractInstance instance = new DenseInstance(attributes);

        AbstractInstance result = instance.multiply(2.0);

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", 4.0);
        expectedAttributes.put("feature2", 6.0);
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test
    public void testEquals() {
        Map<String, Double> attributes1 = new HashMap<>();
        attributes1.put("feature1", 1.0);
        attributes1.put("feature2", 2.0);
        AbstractInstance instance1 = new DenseInstance(attributes1);

        Map<String, Double> attributes2 = new HashMap<>();
        attributes2.put("feature1", 1.0);
        attributes2.put("feature2", 2.0);
        AbstractInstance instance2 = new DenseInstance(attributes2);

        Map<String, Double> attributes3 = new HashMap<>();
        attributes3.put("feature1", 1.0);
        attributes3.put("feature2", 3.0);
        AbstractInstance instance3 = new DenseInstance(attributes3);

        Map<String, Double> attributes4 = new HashMap<>();
        attributes4.put("feature1", 2.0);
        attributes4.put("feature2", 2.0);
        AbstractInstance instance4 = new DenseInstance(attributes4);

        assertEquals(instance1, instance1);
        assertEquals(instance1, instance2);
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(instance4));
    }

    @Test
    public void testHashCode() {
        Map<String, Double> attributes1 = new HashMap<>();
        attributes1.put("feature1", 1.0);
        attributes1.put("feature2", 2.0);
        AbstractInstance instance1 = new DenseInstance(attributes1);

        Map<String, Double> attributes2 = new HashMap<>();
        attributes2.put("feature1", 1.0);
        attributes2.put("feature2", 2.0);
        AbstractInstance instance2 = new DenseInstance(attributes2);

        Map<String, Double> attributes3 = new HashMap<>();
        attributes3.put("feature1", 1.0);
        attributes3.put("feature2", 3.0);
        AbstractInstance instance3 = new DenseInstance(attributes3);

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1.getID(), instance2.hashCode());
        assertFalse(instance1.hashCode() == instance3.hashCode());
    }

    @Test
    public void testMultiplyInstance() {
        Map<String, Double> attributes1 = new HashMap<>();
        attributes1.put("feature1", 2.0);
        attributes1.put("feature2", 3.0);
        AbstractInstance instance1 = new DenseInstance(attributes1);

        Map<String, Double> attributes2 = new HashMap<>();
        attributes2.put("feature1", 1.0);
        attributes2.put("feature2", 2.0);
        AbstractInstance instance2 = new DenseInstance(attributes2);

        AbstractInstance result = instance1.multiply(instance2);

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", 2.0 * 1.0);
        expectedAttributes.put("feature2", 3.0 * 2.0);
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test
    public void testDivideInstance() {
        Map<String, Double> attributes1 = new HashMap<>();
        attributes1.put("feature1", 6.0);
        attributes1.put("feature2", 3.0);
        AbstractInstance instance1 = new DenseInstance(attributes1);

        Map<String, Double> attributes2 = new HashMap<>();
        attributes2.put("feature1", 2.0);
        attributes2.put("feature2", 1.5);
        AbstractInstance instance2 = new DenseInstance(attributes2);

        AbstractInstance result = instance1.divide(instance2);

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", 6.0 / 2.0);
        expectedAttributes.put("feature2", 3.0 / 1.5);
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test
    public void testAddDouble() {
        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 1.0);
        attributes.put("feature2", 2.0);
        AbstractInstance instance = new DenseInstance(attributes);

        AbstractInstance result = instance.add(1.5);

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", 1.0 + 1.5);
        expectedAttributes.put("feature2", 2.0 + 1.5);
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test
    public void testAddInstance() {
        Map<String, Double> attributes1 = new HashMap<>();
        attributes1.put("feature1", 1.0);
        attributes1.put("feature2", 2.0);
        AbstractInstance instance1 = new DenseInstance(attributes1);

        Map<String, Double> attributes2 = new HashMap<>();
        attributes2.put("feature1", 1.5);
        attributes2.put("feature2", 1.5);
        AbstractInstance instance2 = new DenseInstance(attributes2);

        AbstractInstance result = instance1.add(instance2);

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", 1.0 + 1.5);
        expectedAttributes.put("feature2", 2.0 + 1.5);
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Test
    public void testSqrt() {
        Map<String, Double> attributes = new HashMap<>();
        attributes.put("feature1", 9.0);
        AbstractInstance instance = new DenseInstance(attributes);

        AbstractInstance result = instance.sqrt();

        Map<String, Double> expectedAttributes = new HashMap<>();
        expectedAttributes.put("feature1", Math.sqrt(9.0));
        assertEquals(expectedAttributes, ((Map<String, Double>)result).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
}