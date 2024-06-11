
package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        // Test with default constructor
        AbstractInstance instance1 = new AbstractInstance();
        int id1 = instance1.getID();
        assertEquals(AbstractInstance.nextID, id1);

        // Test with custom constructor
        Object classValue = new Object();
        AbstractInstance instance2 = new AbstractInstance(classValue);
        int id2 = instance2.getID();
        assertEquals(AbstractInstance.nextID, id2);
        AbstractInstance.nextID++;
    }

    @Test
    public void testIterator() {
        // Test with default constructor
        AbstractInstance instance = new AbstractInstance();
        List<Double> values = new ArrayList<>();
        for (Double value : instance) {
            values.add(value);
        }
        assertEquals(instance.noAttributes(), values.size());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(i, values.get(i).doubleValue());
        }

        // Test with custom constructor
        Object classValue = new Object();
        AbstractInstance instanceWithClassValue = new AbstractInstance(classValue);
        List<Double> valuesWithClassValue = new ArrayList<>();
        for (Double value : instanceWithClassValue) {
            valuesWithClassValue.add(value);
        }
        assertEquals(instanceWithClassValue.noAttributes(), valuesWithClassValue.size());
        for (int i = 0; i < instanceWithClassValue.noAttributes(); ++i) {
            assertEquals(i, valuesWithClassValue.get(i).doubleValue());
        }
    }

    @Test
    public void testSubtraction() {
        // Test subtraction with Instance
        AbstractInstance instance1 = new AbstractInstance();
        instance1.put(0, 5.0);
        instance1.put(1, 3.0);

        AbstractInstance instance2 = new AbstractInstance();
        instance2.put(0, 2.0);
        instance2.put(1, 1.0);

        AbstractInstance result = instance1.minus(instance2);
        List<Double> expectedValues = new ArrayList<>();
        expectedValues.add(3.0);
        expectedValues.add(2.0);

        for (int i = 0; i < result.noAttributes(); ++i) {
            assertEquals(expectedValues.get(i), result.get(i));
        }

        // Test subtraction with double
        AbstractInstance instance3 = new AbstractInstance();
        instance3.put(0, 7.0);
        instance3.put(1, 2.0);

        AbstractInstance resultFromDouble = instance3.minus(2.0);
        List<Double> expectedValuesFromDouble = new ArrayList<>();
        expectedValuesFromDouble.add(5.0);
        expectedValuesFromDouble.add(0.0);

        for (int i = 0; i < resultFromDouble.noAttributes(); ++i) {
            assertEquals(expectedValuesFromDouble.get(i), resultFromDouble.get(i));
        }
    }
}