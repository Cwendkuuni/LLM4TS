import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceValueIterator;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test public void testGetID() {
        AbstractInstance instance = new AbstractInstance(new Object());
        int id = instance.getID();
        assertEquals(AbstractInstance.nextID, id);
    }

    @Test public void testIterator() {
        AbstractInstance instance = new AbstractInstance(new Object());
        Iterator<Double> iterator = instance.iterator();
        List<Double> values = new ArrayList<>();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }
        assertEquals(instance.noAttributes(), values.size());
    }

    @Test public void testConstructor() {
        AbstractInstance instance1 = new AbstractInstance();
        AbstractInstance instance2 = new AbstractInstance(new Object());
        AbstractInstance instance3 = new AbstractInstance(instance2);
        assertNotEquals(instance1, instance2);
        assertEquals(instance2, instance3);
    }

    @Test public void testClassValue() {
        AbstractInstance instance = new AbstractInstance(new Object());
        Object classValue = new Object();
        instance.setClassValue(classValue);
        Object retrievedClassValue = instance.classValue();
        assertEquals(classValue, retrievedClassValue);
    }

    @Test public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance(new Object());
        Object oldClassValue = instance.classValue();
        Object newClassValue = new Object();
        instance.setClassValue(newClassValue);
        Object retrievedClassValue = instance.classValue();
        assertNotEquals(oldClassValue, retrievedClassValue);
        assertEquals(newClassValue, retrievedClassValue);
    }

    @Test public void testMinusInstance() {
        AbstractInstance instance1 = new AbstractInstance(new Object());
        AbstractInstance instance2 = new AbstractInstance(new Object());
        Instance result = instance1.minus(instance2);
        Instance expectedResult = new DenseInstance(new double[instance1.noAttributes()]);
        for (int i = 0; i < instance1.noAttributes(); ++i) {
            expectedResult.put(i, instance1.get(i) - instance2.get(i));
        }
        assertEquals(expectedResult, result);
    }

    @Test public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstance(new Object());
        Instance result = instance.minus(1.0);
        Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
        for (int i = 0; i < instance.noAttributes(); ++i) {
            expectedResult.put(i, instance.get(i) - 1.0);
        }
        assertEquals(expectedResult, result);
    }

    @Test public void testDivideDouble() {
        AbstractInstance instance = new AbstractInstance(new Object());
        Instance result = instance.divide(2.0);
        Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
        for (int i = 0; i < instance.noAttributes(); ++i) {
            expectedResult.put(i, instance.get(i) / 2.0);
        }
        assertEquals(expectedResult, result);
    }

    @Test public void testMultiplyDouble() {
        AbstractInstance instance = new AbstractInstance(new Object());
        Instance result = instance.multiply(2.0);
        Instance expectedResult = new DenseInstance(new double[instance.noAttributes()]);
        for (int i = 0; i < instance.noAttributes(); ++i) {
            expectedResult.put(i, instance.get(i) * 2.0);
        }
        assertEquals(expectedResult, result);
    }

    @Test public void testMinusDoubleEdgeCase() {
        AbstractInstance instance = new AbstractInstance(new Object());
        try {
            Instance result = instance.minus(-1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test public void testDivideDoubleEdgeCase() {
        AbstractInstance instance = new AbstractInstance(new Object());
        try {
            Instance result = instance.divide(-1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test public void testDivideByZero() {
        AbstractInstance instance = new AbstractInstance(new Object());
        try {
            Instance result = instance.divide(0.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test public void testMinusSameInstance() {
        AbstractInstance instance = new AbstractInstance(new Object());
        Instance result = instance.minus(instance);
        // Test for zero or null result
        // Add test for specific edge cases based on the implementation of DenseInstance
    }
}