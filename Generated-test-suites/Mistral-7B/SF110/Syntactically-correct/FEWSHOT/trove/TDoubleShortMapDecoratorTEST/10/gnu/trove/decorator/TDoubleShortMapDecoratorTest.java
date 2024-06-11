package org.apache.commons.cli; // assume we have JUnit imported as in the provided example

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
        assertNull(decorator.get(-1.0));
        assertNull(decorator.get(null));
    }

    @Test
    public void testPutGetAndRemove() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        double key1 = 1.0;
        short value1 = 1;
        decorator.put(key1, value1);
        assertEquals(value1, decorator.get(key1));
        short removedValue = decorator.remove(key1);
        assertNull(decorator.get(key1));
        assertEquals(value1, removedValue);

        double key2 = 2.0;
        short value2 = 2;
        decorator.put(key2, value2);
        assertEquals(value2, decorator.get(key2));
        short valueAfterRemove1 = decorator.remove(key1);
        assertNull(decorator.get(key1));
        assertEquals(value1, valueAfterRemove1);
    }

    @Test
    public void testPutAndGetNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        Short nullValue = decorator.put((Double)null, (Short)null);
        assertNull(decorator.get(null));
        assertEquals(nullValue, nullValue);

        decorator.put((Double)null, (Short)1);
        assertNull(decorator.get(null));
    }

    @Test
    public void testPutAndGetNullValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (Short)null);
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, 1);
        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(2.0));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, 1);
        assertTrue(decorator.containsValue((short)1));
        assertFalse(decorator.containsValue((short)2));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, 1);
        decorator.put(2.0, 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry1 = iterator.next();
        Entry<Double, Short> entry2 = iterator.next();

        assertEquals(1.0, entry1.getKey().doubleValue());
        assertEquals(1, entry1.getValue().shortValue());
        assertEquals(2.0, entry2.getKey().doubleValue());
        assertEquals(2, entry2.getValue().shortValue());
    }

    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, 1);
        decorator.put(2.0, 2);
        decorator.clear();

        assertFalse(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(2.0));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testExternalizable() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, 1);
        decorator.put(2.0, 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInputStream(inputStream);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
        in.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        Iterator<Map.Entry<Double, Short>> deserializedIterator = deserializedDecorator.entrySet().iterator();
        Iterator<Map.Entry<Double, Short>> iterator = decorator.entrySet().iterator();

        while (deserializedIterator.hasNext() && iterator.hasNext()) {
            Map.Entry<Double, Short> deserializedEntry = deserializedIterator.next();
            Map.Entry<Double, Short> entry = iterator.next();
            assertEquals(deserializedEntry.getKey(), entry.getKey());
            assertEquals(deserializedEntry.getValue(), entry.getValue());
        }
    }
}