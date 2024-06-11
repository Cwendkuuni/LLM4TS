package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testHashCodeAndEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance3 = new DenseInstance(new double[]{2.0, 2.0});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1, instance1);
        assertEquals(instance1.getClass(), instance1.getClass());
        assertEquals(instance1.getID(), instance1.ID);

        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertEquals(instance1, instance2);

        assertNotEquals(instance1, instance3);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testAbstractInstanceConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertNull(instance.classValue);
        assertEquals(0, instance.getID());

        AbstractInstance instanceWithClassValue = new AbstractInstance("TestClass");
        assertEquals("TestClass", instanceWithClassValue.classValue());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();

        assertEquals(2, iterator.nextCount());
        assertEquals(1.0, iterator.next());
        assertEquals(2.0, iterator.next());
    }

    @Test
    public void testMinus() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});

        AbstractInstance difference = instance1.minus(instance2);
        Assert.assertArrayEquals(new double[]{-1.0, -1.0}, difference.toArray());
    }

    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance difference = instance.minus(3.0);
        Assert.assertArrayEquals(new double[]{-2.0, -1.0}, difference.toArray());
    }

    @Test
    public void testDivide() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 8.0});

        AbstractInstance quotient = instance.divide(2.0);
        Assert.assertArrayEquals(new double[]{2.0, 4.0}, quotient.toArray());
    }

    @Test
    public void testMultiply() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance product = instance.multiply(3.0);
        Assert.assertArrayEquals(new double[]{3.0, 6.0}, product.toArray());
    }

    @Test
    public void testAdd() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 3.0});

        AbstractInstance sum = instance1.add(instance2);
        Assert.assertArrayEquals(new double[]{3.0, 5.0}, sum.toArray());
    }

    @Test
    public void testAddDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance sum = instance.add(3.0);
        Assert.assertArrayEquals(new double[]{4.0, 5.0}, sum.toArray());
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{9.0});

        AbstractInstance squareRoot = instance.sqrt();
        Assert.assertEquals(3.0, squareRoot.get(0), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> iterator = instance.iterator();
        iterator.next();
        iterator.remove();
    }
}