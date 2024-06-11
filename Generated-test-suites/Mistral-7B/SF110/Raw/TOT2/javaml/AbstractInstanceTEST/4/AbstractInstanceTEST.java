package net.sf.javaml.core;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance(null);
        int expectedID = 0;
        int actualID = instance.getID();
        assertEquals(expectedID, actualID);

        AbstractInstance instanceWithValue = new AbstractInstance(new Object());
        expectedID = 1;
        actualID = instanceWithValue.getID();
        assertEquals(expectedID, actualID);
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance(null);
        Iterator<Double> iterator = instance.iterator();
        assertFalse(iterator.hasNext());

        AbstractInstance instanceWithValues = new AbstractInstance(new Object());
        instanceWithValues.put(0, 1.0);
        instanceWithValues.put(1, 2.0);
        iterator = instanceWithValues.iterator();
        Double firstValue = iterator.next();
        Double secondValue = iterator.next();
        assertEquals(1.0, firstValue.doubleValue());
        assertEquals(2.0, secondValue.doubleValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructor() {
        AbstractInstance instance = new AbstractInstance(null);
        assertNotNull(instance);

        AbstractInstance instanceWithValue = new AbstractInstance(new Object());
        assertNotNull(instanceWithValue);
    }

    @Test
    public void testMethodsWithInstanceArgument_NullInstance() {
        AbstractInstance instance = new AbstractInstance(null);
        AbstractInstance nullInstance = null;

        AbstractInstance result1 = instance.minus(nullInstance);
        assertNull(result1);

        try {
            instance.divide(nullInstance);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            instance.multiply(nullInstance);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            instance.divide(nullInstance);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            instance.add(nullInstance);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            instance.add(nullInstance);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMethodsWithInstanceArgument_EmptyInstance() {
        AbstractInstance instance = new AbstractInstance(null);
        AbstractInstance emptyInstance = new AbstractInstance(new Object());

        AbstractInstance result1 = instance.minus(emptyInstance);
        AbstractInstance expectedResult1 = new AbstractInstance(new double[instance.noAttributes()]);
        assertInstanceEquals(expectedResult1, result1);

        AbstractInstance result2 = emptyInstance.minus(instance);
        AbstractInstance expectedResult2 = new AbstractInstance(new double[instance.noAttributes()]);
        assertInstanceEquals(expectedResult2, result2);

        try {
            instance.divide(emptyInstance);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            instance.multiply(emptyInstance);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            instance.divide(emptyInstance);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            instance.add(emptyInstance);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }

        try {
            instance.add(emptyInstance);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testMethodsWithInstanceArgument_NonEmptyInstances() {
        AbstractInstance instance = new AbstractInstance(null);
        AbstractInstance instanceWithValues = new AbstractInstance(new Object());
        instanceWithValues.put(0, 1.0);
        instanceWithValues.put(1, 2.0);
        instanceWithValues.put(2, 3.0);

        AbstractInstance anotherInstance = new AbstractInstance(new Object());
        anotherInstance.put(0, 4.0);
        anotherInstance.put(1, 5.0);
        anotherInstance.put(2, 6.0);

        AbstractInstance result1 = instance.minus(anotherInstance);
        AbstractInstance expectedResult1 = new AbstractInstance(new double[]{ -3.0, -1.0, 0.0, 0.0, 0.0 });
        assertInstanceEquals(expectedResult1, result1);

        AbstractInstance result2 = anotherInstance.minus(instance);
        AbstractInstance expectedResult2 = new AbstractInstance(new double[]{ 3.0, 1.0, 0.0, 0.0, 0.0 });
        assertInstanceEquals(expectedResult2, result2);

        AbstractInstance result3 = instance.divide(anotherInstance);
        AbstractInstance expectedResult3 = new AbstractInstance(new double[]{ 0.232152538825215, 0.2, 0.0, 0.0, 0.0 });
        assertInstanceEquals(expectedResult3, result3);

        try {
            instance.multiply(anotherInstance);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        AbstractInstance result4 = anotherInstance.multiply(instance);
        AbstractInstance expectedResult4 = new AbstractInstance(new double[]{ 16.0, 4.0, 3.0, 0.0, 0.0 });
        assertInstanceEquals(expectedResult4, result4);

        AbstractInstance result5 = instance.divide(instance);
        AbstractInstance expectedResult5 = new AbstractInstance(new double[]{ 1.0, 1.0, 1.0, 0.0, 0.0 });
        assertInstanceEquals(expectedResult5, result5);

        AbstractInstance result6 = instance.add(anotherInstance);
        AbstractInstance expectedResult6 = new AbstractInstance(new double[]{ 5.0, 3.0, 3.0, 0.0, 0.0 });
        assertInstanceEquals(expectedResult6, result6);

        AbstractInstance result7 = anotherInstance.add(instance);
        AbstractInstance expectedResult7 = new AbstractInstance(new double[]{ 7.0, 3.0, 3.0, 0.0, 0.0 });
        assertInstanceEquals(expectedResult7, result7);
    }

    @Test
    public void testMethodsWithMapArgument() {
        AbstractInstance instance = new AbstractInstance(new Object());
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);

        AbstractInstance result1 = instance.get(map);
        AbstractInstance expectedResult1 = new AbstractInstance(new double[]{ 1.0, 2.0 });
        assertInstanceEquals(expectedResult1, result1);

        try {
            instance.put(10, 10.0);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            instance.put(null, 10.0);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            instance.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            instance.get(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            instance.put(0, null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance(new Object());
        instance.put(0, -1.0);

        AbstractInstance result = instance.sqrt();
        AbstractInstance expectedResult = new AbstractInstance(new double[]{ -1.0 });
        assertInstanceEquals(expectedResult, result);

        try {
            instance.sqrt();
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testStaticInitializer() {
        AbstractInstanceTest test = new AbstractInstanceTest();
        AbstractInstanceTest testWithInitialValue = new AbstractInstanceTest();
        AbstractInstanceTest testWithNegativeInitialValue = new AbstractInstanceTest();

        assertEquals(0, AbstractInstance.nextID);
        test.new AbstractInstanceTest();
        testWithInitialValue.new AbstractInstanceTest();
        testWithNegativeInitialValue.new AbstractInstanceTest(-1);

        assertEquals(1, AbstractInstance.nextID);
        test.new AbstractInstanceTest();
        testWithInitialValue.new AbstractInstanceTest();
        testWithNegativeInitialValue.new AbstractInstanceTest(-1);

        assertEquals(2, AbstractInstance.nextID);
        test.new AbstractInstanceTest();
        testWithInitialValue.new AbstractInstanceTest();
        testWithNegativeInitialValue.new AbstractInstanceTest(-1);
    }

    private void assertInstanceEquals(AbstractInstance expected, AbstractInstance actual) {
        assertEquals(expected.noAttributes(), actual.noAttributes());
        for (int i = 0; i < expected.noAttributes(); ++i) {
            assertEquals(expected.get(i), actual.get(i), 0.001);
        }
    }
}