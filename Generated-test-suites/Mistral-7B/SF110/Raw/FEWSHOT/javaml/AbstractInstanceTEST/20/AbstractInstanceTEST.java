package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testNextID() {
        AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 4.0});
        AbstractInstance instance3 = new DenseInstance(new double[] {5.0, 6.0});

        assertEquals(0, AbstractInstance.nextID);
        assertEquals(1, instance1.getID());
        assertEquals(2, instance2.getID());
        assertEquals(3, instance3.getID());
    }

    @Test
    public void testIterator() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});

        Iterator<Double> iterator = instance.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(1.0, iterator.next(), 0.0);
        assertTrue(iterator.hasNext());
        assertEquals(2.0, iterator.next(), 0.0);
        assertTrue(iterator.hasNext());
        assertEquals(3.0, iterator.next(), 0.0);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAdd() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});

        AbstractInstance result1 = instance.add(1.0);
        AbstractInstance result2 = instance.add(new DenseInstance(new double[] {1.0, 1.0}));

        assertEquals(instance.noAttributes(), result1.noAttributes());
        assertEquals(instance.noAttributes(), result2.noAttributes());

        assertArrayEquals(new double[] {2.0, 3.0}, result1.asArray(), 0.0);
        assertArrayEquals(new double[] {2.1, 3.1}, result2.asArray(), 0.0);
    }

    @Test
    public void testMultiply() {
        final AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 4.0});

        AbstractInstance result = instance1.multiply(instance2);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertEquals(instance1.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {3.0, 6.0}, result.asArray(), 0.0);
    }

    @Test
    public void testDivide() {
        final AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 4.0});

        AbstractInstance result = instance1.divide(instance2);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertEquals(instance1.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {0.3333333333333333, 0.5}, result.asArray(), 0.0);
    }

    @Test
    public void testMinus() {
        final AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 4.0});

        AbstractInstance result1 = instance1.minus(instance2);
        AbstractInstance result2 = instance1.minus(5.0);

        assertEquals(instance1.noAttributes(), result1.noAttributes());
        assertEquals(instance1.noAttributes(), result1.noAttributes());

        assertArrayEquals(new double[] {-2.0, -2.0}, result1.asArray(), 0.0);
        assertArrayEquals(new double[] {-4.0, -1.0}, result2.asArray(), 0.0);
    }

    @Test
    public void testHashCodeAndEquals() {
        final AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance3 = new DenseInstance(new double[] {3.0, 4.0});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1, instance1);
        assertEquals(instance1, instance1);
        assertNotEquals(instance1, null);
        assertNotEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
    }

    @Test
    public void testNoAttributes() {
        final AbstractInstance instance = new DenseInstance(new double[0]);

        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testClassValue() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
        instance.setClassValue("Label1");

        assertEquals("Label1", instance.classValue());
    }

    @Test
    public void testSetClassValueNull() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});

        assertNull(instance.classValue());
    }

    @Test
    public void testSqrt() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 4.0});

        AbstractInstance result = instance.sqrt();

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertEquals(instance.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {1.0, 2.0}, result.asArray(), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});

        Iterator<Double> iterator = instance.iterator();
        iterator.next();
        iterator.remove();
    }

    @Test
    public void testMapGet() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(map.get(i), instance.get(i), 0.0);
        }
    }

    @Test
    public void testMinusWithMap() {
        final AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 1.0});

        AbstractInstance result = instance1.minus(instance2);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertEquals(instance1.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {0.0, 1.0}, result.asArray(), 0.0);
    }

    @Test
    public void testDivideWithMap() {
        final AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 4.0});

        AbstractInstance result = instance1.divide(instance2);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertEquals(instance1.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {0.3333333333333333, 0.5}, result.asArray(), 0.001);
    }

    @Test
    public void testMultiplyWithMap() {
        final AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 4.0});

        AbstractInstance result = instance1.multiply(instance2);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertEquals(instance1.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {3.0, 6.0}, result.asArray(), 0.001);
    }

    @Test
    public void testAddWithMap() {
        final AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 2.0});
        final AbstractInstance instance2 = new DenseInstance(new double[] {1.0, 1.0});

        AbstractInstance result = instance1.add(instance2);

        assertEquals(instance1.noAttributes(), result.noAttributes());
        assertEquals(instance1.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {2.0, 3.0}, result.asArray(), 0.0);
    }

    @Test
    public void testAddWithArray() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});

        AbstractInstance result = instance.add(new double[] {1.0, 1.0});

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertEquals(instance.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {2.0, 3.0}, result.asArray(), 0.0);
    }

    @Test
    public void testSubtractWithArray() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});

        AbstractInstance result = instance.minus(new double[] {1.0, 1.0});

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertEquals(instance.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {0.0, 1.0}, result.asArray(), 0.0);
    }

    @Test
    public void testDivideWithArray() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});

        AbstractInstance result = instance.divide(new double[] {3.0, 4.0});

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertEquals(instance.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {0.3333333333333333, 0.5}, result.asArray(), 0.001);
    }

    @Test
    public void testMultiplyWithArray() {
        final AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});

        AbstractInstance result = instance.multiply(new double[] {3.0, 4.0});

        assertEquals(instance.noAttributes(), result.noAttributes());
        assertEquals(instance.noAttributes(), result.noAttributes());

        assertArrayEquals(new double[] {3.0, 6.0}, result.asArray(), 0.001);
    }

    @Test
    public void testCopyOnWriteArrayList() {
        List<AbstractInstance> list = new CopyOnWriteArrayList<>();
        list.add(new DenseInstance(new double[] {1.0, 2.0}));
        list.add(new DenseInstance(new double[] {3.0, 4.0}));

        list.forEach(instance -> instance.setClassValue("Label1"));

        for (AbstractInstance abstractInstance : list) {
            assertEquals("Label1", abstractInstance.classValue());
        }
    }

    @Test
    public void testEqualsWithSameInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 1.0});
        AbstractInstance instance2 = instance1;

        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testEqualsWithDifferentInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 1.0});
        AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 3.0});

        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testEqualsWithNull() {
        AbstractInstance instance = new DenseInstance(new double[] {1.0, 1.0});

        assertFalse(instance.equals(null));
    }

    @Test
    public void testHashCodeWithSameInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 1.0});
        AbstractInstance instance2 = instance1;

        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] {1.0, 1.0});
        AbstractInstance instance2 = new DenseInstance(new double[] {3.0, 3.0});

        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testHashCodeWithNull() {
        AbstractInstance instance = new DenseInstance(new double[] {1.0, 1.0});

        assertNotEquals(instance.hashCode(), null);
    }

    @Test
    public void testAddEmptyInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[] {});
        AbstractInstance instance2 = new DenseInstance(new double[] {});

        AbstractInstance result = instance1.add(instance2);

        assertEquals(0, result.noAttributes());
        assertArrayEquals(new double[] {}, result.asArray(), 0.0);
    }
}