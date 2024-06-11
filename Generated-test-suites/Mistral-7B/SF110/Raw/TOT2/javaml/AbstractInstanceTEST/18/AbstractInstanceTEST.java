package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {
            {
                setClassValue("TestClass");
            }
        };
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

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
    public void testConstructor() {
        AbstractInstance instance = new AbstractInstance();
        assertNotNull(instance);

        AbstractInstance instanceWithValue = new AbstractInstance("TestClass");
        assertNotNull(instanceWithValue);
    }

    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
                setClassValue("TestClass");
            }
        };
        assertEquals("TestClass", instance.classValue());
    }

    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        instance.setClassValue("TestClass");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveWithIterator() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        Iterator<Double> iterator = instance.iterator();
        iterator.next();
        iterator.remove();
    }

    @Test
    public void testNoAttributesEdgeCases() {
        AbstractInstance instance = new AbstractInstance();
        assertEquals(0, instance.noAttributes());

        AbstractInstance instanceWithOneAttribute = new AbstractInstance() {
            {
                put(0, 1.0);
            }
        };
        assertEquals(1, instanceWithOneAttribute.noAttributes());

        AbstractInstance instanceWithMaxIntAttributes = new AbstractInstance() {
            {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    put(i, 0.0);
                }
            }
        };
        assertEquals(Integer.MAX_VALUE, instanceWithMaxIntAttributes.noAttributes());
    }

    @Test
    public void testMinusNullInstance() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance nullInstance = null;
        try {
            instance.minus(nullInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMinusEmptyInstance() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance emptyInstance = new AbstractInstance();
        AbstractInstance result = instance.minus(emptyInstance);

        assertEquals(3, result.noAttributes());
        assertEquals(1.0, result.value(0), 0.0);
        assertEquals(1.0, result.value(1), 0.0);
        assertEquals(1.0, result.value(2), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideByZero() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        instance.divide(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideByInstanceWithNoAttributes() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance instanceWithNoAttributes = new AbstractInstance() {
        };

        instance.divide(instanceWithNoAttributes);
    }

    @Test
    public void testMultiplyWithNullInstance() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance nullInstance = null;
        try {
            instance.multiply(nullInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyWithInstanceOfDifferentClass() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        Object otherInstance = new Object() {
        };

        instance.multiply(otherInstance);
    }

    @Test
    public void testAddWithNullInstance() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance nullInstance = null;
        try {
            instance.add(nullInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithInstanceOfDifferentClass() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        Object otherInstance = new Object() {
        };

        instance.add(otherInstance);
    }

    @Test
    public void testEqualsWithSameInstance() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance equalInstance = instance;
        assertTrue(instance.equals(equalInstance));
    }

    @Test
    public void testEqualsWithDifferentID() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance differentInstance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
                setID(1);
            }
        };

        assertFalse(instance.equals(differentInstance));
    }

    @Test
    public void testEqualsWithDifferentValues() {
        AbstractInstance instance = new AbstractInstance() {
            {
                put(0, 1.0);
                put(1, 2.0);
                put(2, 3.0);
            }
        };

        AbstractInstance differentInstance = new AbstractInstance() {
            {
                put(0, 1.1);
                put(1, 2.1);
                put(2, 3.1);
            }
        };

        assertFalse(instance.equals(differentInstance));
    }
}