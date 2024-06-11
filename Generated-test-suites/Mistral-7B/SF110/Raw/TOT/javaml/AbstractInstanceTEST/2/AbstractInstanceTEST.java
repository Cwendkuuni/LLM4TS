
package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(AbstractInstance.nextID, instance.getID());
        AbstractInstance anotherInstance = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        assertEquals(AbstractInstance.nextID + 1, anotherInstance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next(), 0.001);
        assertTrue(iterator.hasNext());
        assertEquals(2.0, iterator.next(), 0.001);
        assertTrue(iterator.hasNext());
        assertEquals(3.0, iterator.next(), 0.001);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance result = instance.minus(2.0);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i) - 2.0, result.get(i), 0.001);
        }
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance min = new DenseInstance(new double[]{2.0, 1.0, 1.0});
        AbstractInstance result = instance.minus(min);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i) - min.get(i), result.get(i), 0.001);
        }
    }

    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance result = instance.divide(2.0);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i) / 2.0, result.get(i), 0.001);
        }
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance min = new DenseInstance(new double[]{2.0, 1.0, 1.0});
        AbstractInstance result = instance.divide(min);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i) / min.get(i), result.get(i), 0.001);
        }
    }

    @Test
    public void testMultiplyDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance result = instance.multiply(2.0);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i) * 2.0, result.get(i), 0.001);
        }
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance value = new DenseInstance(new double[]{2.0, 2.0, 2.0});
        AbstractInstance result = instance.multiply(value);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i) * value.get(i), result.get(i), 0.001);
        }
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance result = instance.add(2.0);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i) + 2.0, result.get(i), 0.001);
        }
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance min = new DenseInstance(new double[]{2.0, 1.0, 1.0});
        AbstractInstance result = instance.add(min);
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i) + min.get(i), result.get(i), 0.001);
        }
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0});
        AbstractInstance result = instance.sqrt();
        assertEquals(instance.noAttributes(), result.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(Math.sqrt(instance.get(i)), result.get(i), 0.001);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Iterator<Double> iterator = instance.iterator();
        iterator.next();
        iterator.remove();
    }
}