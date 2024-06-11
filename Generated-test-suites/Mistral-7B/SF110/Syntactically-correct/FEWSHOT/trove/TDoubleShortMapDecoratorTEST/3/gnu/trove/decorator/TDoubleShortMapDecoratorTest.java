package org.apache.commons.cli; // Replace with the actual package name of your tests

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TDoubleShortMapDecoratorTest {

    private static TDoubleShortMap createTestMap() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        return map;
    }

    @Test
    public void testTDoubleShortMapDecorator() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        assertEquals(decorator.size(), 3);
        assertTrue(decorator.isEmpty(), false);
    }

    @Test
    public void testGetMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        TDoubleShortMap map = decorator.getMap();
        assertEquals(map.size(), 3);
        assertEquals(map.get(1.0), (short) 1);
        assertEquals(map.get(2.0), (short) 2);
        assertEquals(map.get(3.0), (short) 3);
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Double key = 4.0;
        Short value = (short) 4;
        Short result = decorator.put(key, value);
        assertNull(result);
        assertEquals(decorator.size(), 1);
        assertEquals(decorator.get(key), value);

        key = 1.0;
        value = (short) 5;
        result = decorator.put(key, value);
        assertEquals(result, (short) 1);
        assertEquals(decorator.get(key), value);

        key = 2.0;
        result = decorator.put(key, (short) 6);
        assertEquals(result, (short) 2);
        assertEquals(decorator.get(key), result);

        key = 3.0;
        result = decorator.put(key, (short) 7);
        assertEquals(result, (short) 3);
        assertEquals(decorator.get(key), result);

        assertEquals(decorator.get(4.0), value);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        assertEquals(decorator.get(1.0), (short) 1);
        assertEquals(decorator.get(2.0), (short) 2);
        assertEquals(decorator.get(3.0), (short) 3);
        assertNull(decorator.get(null));
        assertNull(decorator.get(4.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        decorator.clear();
        assertEquals(decorator.size(), 0);
        assertNull(decorator.get(1.0));
        assertNull(decorator.get(2.0));
        assertNull(decorator.get(3.0));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        Double key = 2.0;
        Short result = decorator.remove(key);
        assertEquals(result, (short) 2);
        assertEquals(decorator.size(), 2);
        assertNull(decorator.get(key));

        key = 1.0;
        result = decorator.remove(key);
        assertEquals(result, (short) 1);
        assertEquals(decorator.size(), 1);
        assertEquals(decorator.get(key), null); // null, since the original map contained null values

        key = 3.0;
        result = decorator.remove(key);
        assertNull(result);
        assertEquals(decorator.size(), 1);
        assertEquals(decorator.get(key), (short) 3);

        key = null;
        result = decorator.remove(key);
        assertNull(result);
        assertEquals(decorator.size(), 1);
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(entrySet.size(), 3);

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            double key = entry.getKey();
            short value = entry.getValue();
            assertEquals(decorator.get(key), value);
        }

        DecoratorTestUtil.testModifyEntrySet(decorator, entrySet);
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        Short value = (short) 1;
        assertTrue(decorator.containsValue(value));

        value = (short) 4;
        assertFalse(decorator.containsValue(value));

        value = null;
        assertTrue(decorator.containsValue(null));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        Double key = 1.0;
        assertTrue(decorator.containsKey(key));

        key = 4.0;
        assertFalse(decorator.containsKey(key));

        key = null;
        assertFalse(decorator.containsKey(key));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        assertEquals(decorator.size(), 3);

        decorator.clear();
        assertEquals(decorator.size(), 0);

        decorator.put(1.0, (short) 1);
        assertEquals(decorator.size(), 1);

        decorator.putAll(new HashMap<Double, Short>());
        assertEquals(decorator.size(), 0);
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        assertFalse(decorator.isEmpty());

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        decorator.putAll(map);
        assertEquals(decorator.size(), 3);

        DecoratorTestUtil.testKeysAndValues(decorator, map.keySet().toArray(new Double[0]), map.values().toArray(new Short[0]));
    }

    @Test
    public void testExternalizable() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInputStream(byteInput);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) in.readObject();
        in.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        DecoratorTestUtil.testKeysAndValues(decorator, deserializedDecorator.keySet().toArray(new Double[0]), deserializedDecorator.values().toArray(new Short[0]));
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(createTestMap());
        TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator) decorator.clone();

        assertEquals(decorator.size(), clonedDecorator.size());
        DecoratorTestUtil.testKeysAndValues(decorator, clonedDecorator.keySet().toArray(new Double[0]), clonedDecorator.values().toArray(new Short[0]));
    }

    private static void DecoratorTestUtil_testModifyEntrySet(TDoubleShortMapDecorator decorator, Set<Entry<Double, Short>> entrySet) {
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

        Entry<Double, Short> entry = iterator.next();
        double key = entry.getKey();
        short value = entry.getValue();
        short newValue = (short) (value + 1);

        assertEquals(decorator.get(key), value);
        entry.setValue(newValue);
        assertEquals(decorator.get(key), value); // The decorator doesn't modify the values directly

        assertEquals(decorator.size(), 3);
        iterator.remove();
        assertEquals(decorator.size(), 2);
        assertEquals(decorator.get(key), null); // The decorator doesn't modify the keys directly
    }

    private static void DecoratorTestUtil_testKeysAndValues(TDoubleShortMapDecorator decorator, Object[] keys, Object[] values) {
        for (int i = 0; i < keys.length; i++) {
            Double key = (Double) keys[i];
            Short value = (Short) values[i];

            assertEquals(decorator.get(key), value);
        }
    }
}