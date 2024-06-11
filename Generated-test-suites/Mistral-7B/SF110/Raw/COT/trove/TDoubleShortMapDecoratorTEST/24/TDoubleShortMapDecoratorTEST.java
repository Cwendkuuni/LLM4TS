package com.example.junit4.test; // replace with your test package name

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testConstructor() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNotNull(decorator);

        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratorWithMap = new TDoubleShortMapDecorator(map);
        assertEquals(map, decoratorWithMap.getMap());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
         double key = 1.0;
         short value = 1;
         Short result = decorator.put(key, value);
         assertTrue(decorator.containsKey(key));
         assertEquals(value, decorator.get(key));
         assertEquals(value, result);
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNull(decorator.put(null, (short)1));
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testPutNullValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        double key = 1.0;
        Short result = decorator.put(key, null);
        assertTrue(decorator.containsKey(key));
        assertNull(decorator.get(key));
        assertNull(result);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        double key = 1.0;
        short value = 1;
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
        assertEquals(value, decorator.get(key)); // check get method consistency
        assertNull(decorator.get(null));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        double key1 = 1.0;
        double key2 = 2.0;
        short value1 = 1;
        short value2 = 2;
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        decorator.clear();
        assertTrue(decorator.isEmpty());
        assertFalse(decorator.containsKey(key1));
        assertFalse(decorator.containsKey(key2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        double key = 1.0;
        short value = 1;
        decorator.put(key, value);
        decorator.remove(null);
    }

    @Test
    public void testRemoveExistingKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        double key1 = 1.0;
        double key2 = 2.0;
        short value1 = 1;
        short value2 = 2;
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        Short result1 = decorator.remove(key1);
        Short result2 = decorator.remove(key2);
        assertTrue(decorator.containsKey(key1) == false);
        assertTrue(decorator.containsKey(key2) == false);
        assertNull(result1);
        assertNull(result2);
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        double key1 = 1.0;
        double key2 = 2.0;
        short value1 = 1;
        short value2 = 2;
        decorator.put(key1, value1);
        decorator.put(key2, value2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        int size = entrySet.size();
        assertTrue(size == 2);

        Entry<Double, Short> entry1 = entrySet.iterator().next();
        Entry<Double, Short> entry2 = entrySet.iterator().next();
        assertNotNull(entry1);
        assertNotNull(entry2);
        assertNotNull(entry1.getKey());
        assertNotNull(entry1.getValue());
        assertNotNull(entry2.getKey());
        assertNotNull(entry2.getValue());

        assertTrue(entry1.getKey().equals(key1));
        assertTrue(entry1.getValue().equals(value1));
        assertTrue(entry2.getKey().equals(key2));
        assertTrue(entry2.getValue().equals(value2));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        assertTrue(decorator.containsValue((short)1));
        assertTrue(decorator.containsValue((short)2));
        assertFalse(decorator.containsValue((short)3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        int size = decorator.size();
        assertTrue(size == 0);

        decorator.put(1.0, (short)1);
        size = decorator.size();
        assertTrue(size == 1);

        decorator.put(2.0, (short)2);
        size = decorator.size();
        assertTrue(size == 2);
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short)1);
        assertFalse(decorator.isEmpty());

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        TDoubleShortMap sourceMap = new TDoubleShortMap();
        sourceMap.put(1.0, (short)1);
        sourceMap.put(2.0, (short)2);

        decorator.putAll(sourceMap);

        int size = decorator.size();
        assertTrue(size == 2);
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
    }

    @Test
    public void testExternalization() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap map = new TDoubleShortMap();
        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        decorator.put(3.0, (short)3);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInputStream(byteInput);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) in.readObject();
        in.close();

        int size = deserializedDecorator.size();
        assertTrue(size == 3);
        assertTrue(deserializedDecorator.containsKey(1.0));
        assertTrue(deserializedDecorator.containsKey(2.0));
        assertTrue(deserializedDecorator.containsKey(3.0));
    }
}