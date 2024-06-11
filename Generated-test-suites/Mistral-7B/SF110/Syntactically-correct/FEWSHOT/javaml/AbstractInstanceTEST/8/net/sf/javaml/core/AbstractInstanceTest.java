package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(AbstractInstance.nextID, instance.getID());

        Instance anotherInstance = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        assertEquals(AbstractInstance.nextID + 1, anotherInstance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Iterator<Double> iterator = instance.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            Double next = iterator.next();
            assertEquals(index++, next.doubleValue(), 0.001);
        }
    }

    @Test
    public void testMinusInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5});

        AbstractInstance result = instance1.minus(instance2);

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(-0.5, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(-1.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(-1.5, nextValue.doubleValue(), 0.001);
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.minus(2.0);

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(-1.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(-2.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(-3.0, nextValue.doubleValue(), 0.001);
    }

    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.divide(2.0);

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(0.5, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(1.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(1.5, nextValue.doubleValue(), 0.001);
    }

    @Test
    public void testMultiplyDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.multiply(2.0);

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(2.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(4.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(6.0, nextValue.doubleValue(), 0.001);
    }

    @Test
    public void testEqualsObject() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));

        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});

        assertFalse(instance1.equals(instance3));
        assertFalse(instance3.equals(instance1));

        Object obj = new Object();

        assertFalse(instance1.equals(obj));
        assertFalse(obj.equals(instance1));
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});

        assertEquals(AbstractInstance.nextID, instance1.getID());
        assertEquals(AbstractInstance.nextID + 1, instance2.getID());
        assertEquals(AbstractInstance.nextID + 2, instance3.getID());

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance1.hashCode());

        assertNotEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0, 4.0});

        AbstractInstance result = instance1.multiply(instance2);

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(2.0 * 1.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(3.0 * 2.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(4.0 * 3.0, nextValue.doubleValue(), 0.001);
    }

    @Test
    public void testDivideInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 2.0, 2.0});

        AbstractInstance result = instance1.divide(instance2);

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(1.0 / 2.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(1.0 / 2.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(1.0 / 2.0, nextValue.doubleValue(), 0.001);
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        AbstractInstance result = instance.add(2.0);

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(3.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(4.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(5.0, nextValue.doubleValue(), 0.001);
    }

    @Test
    public void testAddInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 1.5, 2.5});

        AbstractInstance result = instance1.add(instance2);

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(1.5, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(3.5, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(5.5, nextValue.doubleValue(), 0.001);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 4.0, 9.0});

        AbstractInstance result = instance.sqrt();

        Iterator<Double> resultIterator = result.iterator();
        Double nextValue;

        nextValue = resultIterator.next();
        assertEquals(Math.sqrt(1.0), nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(2.0, nextValue.doubleValue(), 0.001);

        nextValue = resultIterator.next();
        assertEquals(3.0, nextValue.doubleValue(), 0.001);
    }
}