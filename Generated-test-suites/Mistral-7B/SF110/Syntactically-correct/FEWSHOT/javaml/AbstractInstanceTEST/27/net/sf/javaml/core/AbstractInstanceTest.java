package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testAbstractInstanceEmptyConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertEquals(0, instance.noAttributes());
        assertNull(instance.classValue());
    }

    @Test
    public void testAbstractInstanceConstructorWithClassValue() {
        AbstractInstance instance = new AbstractInstance(new Object());
        assertEquals(0, instance.noAttributes());
        assertEquals(new Object(), instance.classValue());
    }

    @Test
    public void testAbstractInstanceID() {
        AbstractInstance instance = new AbstractInstance();
        assertEquals(0, instance.getID());

        AbstractInstance instance2 = new AbstractInstance();
        assertEquals(1, instance2.getID());
        assertEquals(instance.getClass(), instance2.getClass());
    }

    @Test
    public void testAbstractInstanceIterator() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        Iterator<Double> iterator = instance.iterator();
        assertEquals(3, iterator.nextCount());
        assertArrayEquals(new Double[]{1.0, 2.0, 3.0}, iterator.nextArray());
    }

    @Test
    public void testAbstractInstanceMinusWithInstance() {
        AbstractInstance instance1 = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance instance2 = new AbstractInstance() {
            {
                put(0, 0.5);
                put(1, -1.5);
                put(2, 0.5);
            }
        };

        AbstractInstance result = instance1.minus(instance2);
        AbstractInstance expected = new AbstractInstance() {
            {
                put(0, 0.5);
                put(1, 0.5);
                put(2, 2.5);
            }
        };

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testAbstractInstanceMinusWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance result = instance.minus(1.0);
        AbstractInstance expected = new AbstractInstance() {
            {
                put(0, 0.0);
                put(1, 1.0);
                put(2, 2.0);
            }
        };

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testAbstractInstanceDivideWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 6.0);
                put(1, 12.0);
                put(2, 18.0);
            }
        };

        AbstractInstance result = instance.divide(3.0);
        AbstractInstance expected = new AbstractInstance() {
            {
                put(0, 2.0);
                put(1, 4.0);
                put(2, 6.0);
            }
        };

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testAbstractInstanceMultiplyWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance result = instance.multiply(2.0);
        AbstractInstance expected = new AbstractInstance() {
            {
                put(0, 2.0);
                put(1, 4.0);
                put(2, 6.0);
            }
        };

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testAbstractInstanceEquals() {
        AbstractInstance instance1 = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance instance2 = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance instance3 = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.1);
                put(2, 3.0);
            }
        };

        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testAbstractInstanceHashCode() {
        AbstractInstance instance1 = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance instance2 = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance1.getID(), instance2.hashCode());
    }

    @Test
    public void testAbstractInstanceAddWithDouble() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance result = instance.add(1.0);
        AbstractInstance expected = new AbstractInstance() {
            {
                put(0, 2.0);
                put(1, 3.0);
                put(2, 4.0);
            }
        };

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testAbstractInstanceAddWithInstance() {
        AbstractInstance instance1 = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance instance2 = new AbstractInstance() {
            {
                put(0, 1.5);
                put(1, 2.5);
                put(2, 3.5);
            }
        };

        AbstractInstance result = instance1.add(instance2);
        AbstractInstance expected = new AbstractInstance() {
            {
                put(0, 2.5);
                put(1, 4.5);
                put(2, 6.5);
            }
        };

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testAbstractInstanceSqrt() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 4.0);
                put(1, 9.0);
                put(2, 16.0);
            }
        };

        AbstractInstance result = instance.sqrt();
        AbstractInstance expected = new AbstractInstance() {
            {
                put(0, 2.0);
                put(1, 3.0);
                put(2, 4.0);
            }
        };

        assertArrayEquals(expected.toArray(), result.toArray());
    }

    @Test
    public void testAbstractInstanceNextID() {
        assertEquals(0, AbstractInstance.nextID);

        AbstractInstance instance1 = new AbstractInstance();
        AbstractInstance instance2 = new AbstractInstance();
        AbstractInstance instance3 = new AbstractInstance();

        assertEquals(1, AbstractInstance.nextID);
        assertEquals(2, AbstractInstance.nextID);

        AbstractInstance instance4 = new AbstractInstance();

        assertEquals(3, AbstractInstance.nextID);
    }
}