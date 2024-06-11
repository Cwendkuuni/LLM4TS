package org.apache.commons.cli; // Make sure to adjust the package name if necessary

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get(null));
        assertNull(decorator.get(1.0));
        assertNull(decorator.remove(null));
        assertNull(decorator.remove(1.0));
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, null);
        decorator.put(null, (short) 0);

        assertEquals(4, decorator.size());
        assertFalse(decorator.isEmpty());

        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertEquals(null, decorator.get(3.0));
        assertEquals((short) 0, decorator.get(null));

        assertNull(decorator.remove(4.0));
        assertEquals(3, decorator.size());

        assertEquals((short) 1, decorator.put(1.0, (short) 3));
        assertEquals((short) 3, decorator.get(1.0));

        assertEquals(null, decorator.remove(null));
        assertEquals(2, decorator.size());
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();

        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return 1.0;
            }

            @Override
            public Short getValue() {
                return (short) 1;
            }
        }));
        assertTrue(entrySet.contains(new Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return 2.0;
            }

            @Override
            public Short getValue() {
                return (short) 2;
            }
        }));

        decorator.clear();
        entrySet = decorator.entrySet();
        assertTrue(entrySet.isEmpty());
    }

    @Test
    public void testContainsKeyAndValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));

        assertFalse(decorator.containsKey(3.0));
        assertFalse(decorator.containsValue((short) 3));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(decorator);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) ois.readObject();

        assertEquals(decorator.size(), deserializedDecorator.size());
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertTrue(deserializedDecorator.containsKey(1.0));
        assertTrue(deserializedDecorator.containsKey(2.0));
    }
}