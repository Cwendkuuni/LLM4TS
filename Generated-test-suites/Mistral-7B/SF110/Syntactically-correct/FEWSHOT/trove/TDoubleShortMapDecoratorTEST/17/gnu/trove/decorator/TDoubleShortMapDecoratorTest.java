package org.apache.commons.cli; // Change the package name if necessary

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMap _map;

    @Before
    public void setUp() {
        _map = new TDoubleShortMap();
    }

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);
        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey(null));
        assertFalse(decorator.containsKey(1.0));
        assertFalse(decorator.containsValue((short) 1));
    }

    @Test
    public void testPutGetAndRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertEquals(2, decorator.size());
        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));

        decorator.remove(1.0);

        assertEquals(1, decorator.size());
        assertNull(decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        assertNull(decorator.put(null, (short) 1));
        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testPutNullValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        decorator.put(1.0, null);

        assertEquals(1, decorator.size());
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();

        assertEquals(2, entrySet.size());

        for (Entry<Double, Short> entry : entrySet) {
            Double key = entry.getKey();
            Short value = entry.getValue();

            assertEquals(key, 1.0); // or another key that was put
            assertEquals(value, (short) 1); // or another value that was put
        }

        decorator.clear();

        entrySet = decorator.entrySet();

        assertEquals(0, entrySet.size());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertFalse(decorator.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(3.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        decorator.clear();

        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(2.0));
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap source = new TDoubleShortMap();
        TDoubleShortMapDecorator sourceDecorator = new TDoubleShortMapDecorator(source);

        sourceDecorator.put(1.0, (short) 1);
        sourceDecorator.put(2.0, (short) 2);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        decorator.putAll(sourceDecorator);

        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(_map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);

        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);

        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
        in.close();

        assertEquals(2, deserializedDecorator.size());
        assertTrue(deserializedDecorator.containsKey(1.0));
        assertTrue(deserializedDecorator.containsKey(2.0));
    }
}