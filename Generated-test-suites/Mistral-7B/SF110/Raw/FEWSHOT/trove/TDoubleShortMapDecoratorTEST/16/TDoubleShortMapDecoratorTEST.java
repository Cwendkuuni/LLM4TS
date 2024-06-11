package org.apache.commons.cli;

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
        Assert.assertNull(map.get(null));
        Map<Double, Short> underylingMap = map.getMap();
        Assert.assertNotNull(underylingMap);
        Assert.assertEquals(0, underylingMap.size());
        Assert.assertTrue(underylingMap.isEmpty());
        Assert.assertNull(underylingMap.get(null));
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Short value = (short) 1;
        map.put(1.0, value);
        Assert.assertEquals(1, map.size());
        Assert.assertFalse(map.isEmpty());
        Assert.assertEquals(value, map.get(1.0));
        map.put(2.0, (short) 2);
        Assert.assertEquals(2, map.size());
        Assert.assertEquals((short) 1, map.get(1.0));
        Assert.assertEquals((short) 2, map.get(2.0));
        Assert.assertNull(map.get(null));

        Double key = 1.0;
        map.put(key, null);
        Assert.assertEquals((Short) null, map.get(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(null, (short) 1);
    }

    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        Assert.assertNull(map.get(null));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.clear();
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
        Assert.assertNull(map.get(null));
        Assert.assertNull(map.get(1.0));
        Assert.assertNull(map.get(2.0));
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        Short removedValue = map.remove(1.0);
        Assert.assertEquals((Short) 1, removedValue);
        Assert.assertEquals(1, map.size());
        Assert.assertEquals((short) 2, map.get(2.0));
        Assert.assertNull(map.get(1.0));
    }

    @Test
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        Assert.assertNull(map.remove(null));
    }

    @Test
    public void testRemoveNonExistentKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        Assert.assertNull(map.remove(2.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = map.entrySet();
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();
        Assert.assertEquals(1.0, entry.getKey());
        Assert.assertEquals((short) 1, entry.getValue());

        iterator.hasNext();
        entry = iterator.next();
        Assert.assertEquals(2.0, entry.getKey());
        Assert.assertEquals((short) 2, entry.getValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        Assert.assertTrue(map.containsValue((short) 1));
        Assert.assertTrue(map.containsValue((short) 2));
        Assert.assertFalse(map.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        Assert.assertTrue(map.containsKey(1.0));
        Assert.assertTrue(map.containsKey(2.0));
        Assert.assertFalse(map.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        Assert.assertEquals(2, map.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Assert.assertTrue(map.isEmpty());

        map.put(1.0, (short) 1);
        Assert.assertFalse(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Map<Double, Short> mapToPut = new HashMap<>();
        mapToPut.put(1.0, (short) 1);
        mapToPut.put(2.0, (short) 2);

        map.putAll(mapToPut);

        Assert.assertEquals(2, map.size());
        Assert.assertEquals((short) 1, map.get(1.0));
        Assert.assertEquals((short) 2, map.get(2.0));
    }

    @Test
    public void testUnsupportedOperations() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();

        try {
            map.add(Collections.singletonEntry(1.0, (short) 1));
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            map.remove(Collections.singletonEntry(1.0, (short) 1));
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            map.contains(Collections.singletonEntry(1.0, (short) 1));
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            map.addAll(Collections.singletonList(Collections.singletonEntry(1.0, (short) 1)));
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testExternalizable() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        map.writeExternal(out);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInputStream(inputStream);

        TDoubleShortMapDecorator mapFromSerialized = new TDoubleShortMapDecorator();
        mapFromSerialized.readExternal(in);

        Assert.assertEquals(map.size(), mapFromSerialized.size());
        Assert.assertEquals(map.get(1.0), mapFromSerialized.get(1.0));
        Assert.assertEquals(map.get(2.0), mapFromSerialized.get(2.0));
    }
}