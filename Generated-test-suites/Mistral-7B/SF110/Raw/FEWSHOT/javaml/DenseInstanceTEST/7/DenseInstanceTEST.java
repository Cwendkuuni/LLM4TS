package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceConstruction() {
        double[] att = new double[5];
        DenseInstance inst = new DenseInstance(att);
        assertEquals(5, inst.noAttributes());
        assertSame(att, inst.attributes);
        assertNull(inst.classValue());

        DenseInstance inst2 = new DenseInstance(att, "label");
        assertEquals("label", inst2.classValue());
        assertSame(att, inst2.attributes);
        assertEquals(5, inst2.noAttributes());
    }

    @Test
    public void testDenseInstanceCopy() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att, "label");

        DenseInstance copy = inst.copy();
        assertSame(inst, copy);

        copy.attributes[2] = 2.0;
        assertNotSame(inst, copy);
    }

    @Test
    public void testDenseInstanceSize() {
        double[] att = new double[5];
        DenseInstance inst = new DenseInstance(att);
        assertEquals(5, inst.noAttributes());

        DenseInstance inst2 = new DenseInstance(new double[0]);
        assertEquals(0, inst2.noAttributes());

        DenseInstance inst3 = new DenseInstance();
        assertEquals(0, inst3.noAttributes());

        assertThrows(UnsupportedOperationException.class, () -> inst.size());
    }

    @Test
    public void testDenseInstanceAttributes() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        for (int i = 0; i < inst.noAttributes(); i++) {
            assertEquals(inst.value(i), att[i], 0.0);
        }

        inst.attributes[2] = 2.0;
        assertEquals(2.0, inst.value(2), 0.0);
    }

    @Test
    public void testDenseInstanceKeySet() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        Set<Integer> keys = inst.keySet();
        assertEquals(5, keys.size());
        assertTrue(keys.containsAll(Arrays.asList(0, 1, 2, 3, 4)));
    }

    @Test
    public void testDenseInstanceEntrySet() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        Set<Map.Entry<Integer, Double>> entries = inst.entrySet();
        assertEquals(5, entries.size());
        Iterator<Map.Entry<Integer, Double>> it = entries.iterator();
        for (int i = 0; i < 5; i++) {
            Map.Entry<Integer, Double> entry = it.next();
            assertEquals(i, entry.getKey().intValue());
            assertEquals(1.0, entry.getValue().doubleValue(), 0.0);
        }
    }

    @Test
    public void testDenseInstanceValues() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        Collection<Double> values = inst.values();
        assertEquals(5, values.size());
        assertTrue(values.containsAll(Arrays.asList(1.0, 1.0, 1.0, 1.0, 1.0)));
    }

    @Test
    public void testDenseInstanceGet() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        assertEquals(1.0, inst.get(0));
        assertEquals(1.0, inst.get(1), 0.0);
        assertEquals(1.0, inst.get(2), 0.0);
        assertEquals(1.0, inst.get(3), 0.0);
        assertEquals(1.0, inst.get(4), 0.0);

        assertNull(inst.get(5));
    }

    @Test
    public void testDenseInstancePut() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        inst.put(2, 2.0);
        assertEquals(2.0, inst.value(2), 0.0);
    }

    @Test
    public void testDenseInstancePutAll() {
        double[] att1 = new double[5];
        Arrays.fill(att1, 1.0);
        DenseInstance inst1 = new DenseInstance(att1);

        double[] att2 = new double[3];
        Arrays.fill(att2, 2.0);
        DenseInstance inst2 = new DenseInstance(att2);

        Map<Integer, Double> map = new HashMap<>();
        map.put(2, 2.0);
        map.put(3, 3.0);
        map.put(4, 4.0);

        inst1.putAll(map);
        assertEquals(8, inst1.noAttributes());

        for (int i = 0; i < inst1.noAttributes(); i++) {
            if (i < 5) {
                assertEquals(inst1.value(i), inst1.attributes[i], 0.0);
            } else {
                assertEquals(map.get(i), inst1.value(i), 0.0);
            }
        }
    }

    @Test
    public void testDenseInstanceRemoveAttribute() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        inst.removeAttribute(2);
        assertEquals(4, inst.noAttributes());
        assertFalse(inst.containsKey(2));

        for (int i = 0; i < inst.noAttributes(); i++) {
            if (i != 2) {
                assertEquals(inst.value(i), att[i], 0.0);
            }
        }
    }

    @Test
    public void testDenseInstanceRemoveAttributes() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2, 3));
        inst.removeAttributes(indices);
        assertEquals(2, inst.noAttributes());

        for (int i = 0; i < inst.noAttributes(); i++) {
            assertEquals(inst.value(i), att[i + 3], 0.0);
        }
    }

    @Test
    public void testDenseInstanceClear() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        inst.clear();
        assertEquals(0, inst.noAttributes());
        assertFalse(inst.containsKey(0));
    }

    @Test
    public void testDenseInstanceContainsKey() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        for (int i = 0; i < inst.noAttributes(); i++) {
            assertTrue(inst.containsKey(i));
        }

        assertFalse(inst.containsKey(5));
    }

    @Test
    public void testDenseInstanceContainsValue() {
        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        DenseInstance inst = new DenseInstance(att);

        for (int i = 0; i < inst.noAttributes(); i++) {
            assertTrue(inst.containsValue(1.0));
        }

        assertFalse(inst.containsValue(2.0));
    }

    @Test
    public void testDenseInstanceIsEmpty() {
        DenseInstance inst = new DenseInstance();
        assertTrue(inst.isEmpty());

        double[] att = new double[5];
        Arrays.fill(att, 1.0);
        inst = new DenseInstance(att);
        assertFalse(inst.isEmpty());
    }
}