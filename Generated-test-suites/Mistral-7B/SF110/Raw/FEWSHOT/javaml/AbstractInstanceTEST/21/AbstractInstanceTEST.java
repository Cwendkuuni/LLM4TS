package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testNextID() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});

        assertEquals(0, AbstractInstance.nextID);
        assertEquals(1, instance1.getID());
        assertEquals(2, instance2.getID());
        assertEquals(3, AbstractInstance.nextID);
    }

    @Test
    public void testInstanceWithNullClassValue() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };

        assertNull(instance.classValue());
    }

    @Test
    public void testInstanceWithClassValue() {
        Object classValue = "foo";
        AbstractInstance instance = new AbstractInstance(classValue) {
            @Override
            public int noAttributes() {
                return 2;
            }
        };

        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testMinusWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 0.5});

        AbstractInstance result = instance1.minus(instance2);

        AbstractInstance expected = new DenseInstance(new double[]{0.5, 1.5});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testMinusWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.minus(1.0);

        AbstractInstance expected = new DenseInstance(new double[]{0.0, 1.0});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testDivideWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.divide(2.0);

        AbstractInstance expected = new DenseInstance(new double[]{0.5, 1.0});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testMultiplyWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.multiply(2.0);

        AbstractInstance expected = new DenseInstance(new double[]{2.0, 4.0});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testEquals() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});

        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));

        AbstractInstance instance3 = new DenseInstance(new double[]{1.0, 3.0});

        assertFalse(instance1.equals(instance3));
        assertFalse(instance3.equals(instance1));
    }

    @Test
    public void testHashCode() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0, 2.0});

        assertEquals(instance1.getID(), instance1.hashCode());
        assertEquals(instance2.getID(), instance2.hashCode());

        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testMultiplyWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{3.0, 4.0});

        AbstractInstance result = instance1.multiply(instance2);

        AbstractInstance expected = new DenseInstance(new double[]{3.0, 8.0});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testDivideWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{4.0, 8.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{2.0, 4.0});

        AbstractInstance result = instance1.divide(instance2);

        AbstractInstance expected = new DenseInstance(new double[]{2.0, 2.0});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testAddWithDouble() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        AbstractInstance result = instance.add(1.0);

        AbstractInstance expected = new DenseInstance(new double[]{2.0, 3.0});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testAddWithInstance() {
        AbstractInstance instance1 = new DenseInstance(new double[]{1.0, 2.0});
        AbstractInstance instance2 = new DenseInstance(new double[]{0.5, 0.5});

        AbstractInstance result = instance1.add(instance2);

        AbstractInstance expected = new DenseInstance(new double[]{1.5, 2.5});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testSqrt() {
        AbstractInstance instance = new DenseInstance(new double[]{4.0, 9.0});

        AbstractInstance result = instance.sqrt();

        AbstractInstance expected = new DenseInstance(new double[]{2.0, 3.0});
        assertInstanceEquals(expected, result);
    }

    @Test
    public void testIteratorReturnsCorrectValuesInOrder() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});

        Iterator<Double> iterator = instance.iterator();

        List<Double> expectedValues = Arrays.asList(1.0, 2.0, 3.0);
        List<Double> actualValues = new ArrayList<>();

        while (iterator.hasNext()) {
            actualValues.add(iterator.next());
        }

        assertArrayEquals(expectedValues.stream().mapToDouble(d -> d).toArray(), actualValues.stream().mapToDouble(d -> d).toArray());
    }

    @Test
    public void testEqualsAndHashcodeWithSameInstance() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});

        assertEquals(instance, instance);
        assertEquals(instance.hashCode(), instance.hashCode());
    }

    @Test
    public void testInstanceWithEmptyMap() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 0;
            }
        };

        assertNull(instance.classValue());
        assertFalse(instance.iterator().hasNext());
    }

    @Test
    public void testInstanceWithValueMap() {
        Map<String, Double> valueMap = new HashMap<>();
        valueMap.put("A", 1.0);
        valueMap.put("B", 2.0);

        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Map<String, Double> asMap() {
                return valueMap;
            }
        };

        assertEquals(valueMap.get("A"), instance.value("A"));
        assertEquals(valueMap.get("B"), instance.value("B"));
    }

    @Test
    public void testInstanceWithValueMapEmptyStringKey() {
        Map<String, Double> valueMap = new HashMap<>();

        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 1;
            }

            @Override
            public Map<String, Double> asMap() {
                return valueMap;
            }
        };

        assertThrows(IllegalArgumentException.class, () -> instance.value(""));
    }

    @Test
    public void testInstanceWithNegativeID() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int getID() {
                return -1;
            }
        };

        assertFalse(instance.equals(instance));
        assertFalse(instance.equals(new Object()));
        assertFalse(instance.hashCode() == instance.hashCode());
    }

    @Test
    public void testInstanceWithNullValueMap() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Map<String, Double> asMap() {
                return null;
            }
        };

        assertThrows(NullPointerException.class, () -> instance.value("A"));
    }

    @Test
    public void testInstanceWithEmptyValueMap() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }

            @Override
            public Map<String, Double> asMap() {
                return Collections.emptyMap();
            }
        };

        assertThrows(UnsupportedOperationException.class, () -> instance.iterator().next());
    }

    @Test
    public void testInstanceWithNullIterator() {
        AbstractInstance instance = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 2;
            }
        };

        assertThrows(NullPointerException.class, () -> instance.iterator());
    }

    private void assertInstanceEquals(AbstractInstance expected, AbstractInstance actual) {
        assertEquals(expected.getID(), actual.getID());
        assertEquals(expected.getClassValue(), actual.getClassValue());

        Iterator<Double> expectedIterator = expected.iterator();
        Iterator<Double> actualIterator = actual.iterator();

        while (expectedIterator.hasNext()) {
            Double expectedValue = expectedIterator.next();
            Double actualValue = actualIterator.next();

            assertEquals(expectedValue, actualValue);
        }
    }
}