package org.apache.commons.cli; // Assuming you want to test TDoubleShortMapDecorator in the context of the previous package

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutGetClear() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Double key1 = 1.0;
        Short value1 = 1;
        Short putValue = decorator.put(key1, value1);
        assertEquals(value1, putValue);
        Short gotValue = decorator.get(key1);
        assertEquals(value1, gotValue);

        decorator.clear();
        assertFalse(decorator.containsKey(key1));
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Short putValue = decorator.put((Double) null, (Short) null);
        assertNull(putValue);
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testPutNullValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Double key = 1.0;
        Short putValue = decorator.put(key, (Short) null);
        assertNull(putValue);
        assertEquals(decorator.getNoEntryValue(), decorator.get(key));
    }

    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Double key1 = 1.0;
        Short value1 = 1;
        decorator.put(key1, value1);
        Short removedValue = decorator.remove(key1);
        assertEquals(value1, removedValue);
        assertFalse(decorator.containsKey(key1));
    }

    @Test
    public void testRemoveNullKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertFalse(decorator.containsKey(null));
        assertNull(decorator.remove(null));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertEquals(0, decorator.size());

        Double key1 = 1.0;
        Short value1 = 1;
        decorator.put(key1, value1);

        // Check that the entry is present in the set
        Iterator<Entry<Double, Short>> setIterator = decorator.entrySet().iterator();
        Entry<Double, Short> entry = setIterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());

        // Check that the entry can be removed from the map and the set
        decorator.remove(key1);
        assertFalse(decorator.containsKey(key1));
        assertFalse(setIterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Double key1 = 1.0;
        Short value1 = 1;
        decorator.put(key1, value1);

        assertTrue(decorator.containsValue(value1));
        assertFalse(decorator.containsValue((Short) null));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Double key1 = 1.0;
        Double key2 = 2.0;
        decorator.put(key1, (Short) 1);
        decorator.put(key2, (Short) 2);

        assertTrue(decorator.containsKey(key1));
        assertTrue(decorator.containsKey(key2));
        assertFalse(decorator.containsKey((Object) null));
    }

    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertEquals(0, decorator.size());

        Double key1 = 1.0;
        Short value1 = 1;
        decorator.put(key1, value1);
        assertEquals(1, decorator.size());

        Double key2 = 2.0;
        Short value2 = 2;
        decorator.put(key2, value2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        assertTrue(decorator.isEmpty());

        Double key1 = 1.0;
        Short value1 = 1;
        decorator.put(key1, value1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap map1 = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator1 = new TDoubleShortMapDecorator(map1);

        TDoubleShortMap map2 = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator(map2);

        Double key1 = 1.0;
        Double key2 = 2.0;
        Short value1 = 1;
        Short value2 = 2;

        decorator1.put(key1, value1);
        decorator1.put(key2, value2);

        decorator2.putAll(decorator1);

        assertEquals(2, decorator2.size());
        assertTrue(decorator2.containsKey(key1));
        assertTrue(decorator2.containsKey(key2));
        assertEquals(value1, decorator2.get(key1));
        assertEquals(value2, decorator2.get(key2));
    }

    @Test
    public void testExternalizable() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInputStream(byteInputStream);
        TDoubleShortMapDecorator decorator2 = (TDoubleShortMapDecorator) in.readObject();
        in.close();

        assertEquals(decorator.size(), decorator2.size());
        for (TDoubleShortIterator it = decorator.iterator(); it.hasNext(); ) {
            Map.Entry<Double, Short> entry = it.next();
            assertEquals(entry.getKey(), decorator2.get(entry.getKey()));
            assertEquals(entry.getValue(), decorator2.get(entry.getKey()));
        }
    }
}