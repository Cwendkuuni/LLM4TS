package org.apache.commons.cli; // Assuming the test class is in the same package as the TShortByteMapDecorator class for simplicity

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        Assert.assertEquals((byte) 1, map.get((short) 1));
        Assert.assertEquals((byte) 2, map.get((short) 2));
        Assert.assertEquals((byte) 3, map.get((short) 3));
        Assert.assertNull(map.get(Short.valueOf((short) 4)));
    }

    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        Assert.assertNull(map.get(null));
        map.put(null, (byte) 0);
        Assert.assertEquals((byte) 0, map.get(null));
    }

    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        map.put((short) 1, null);
        Assert.assertNull(map.get((short) 1));
        map.put((short) 1, (byte) 1);
        Assert.assertEquals((byte) 1, map.get((short) 1));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        map.clear();
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        Assert.assertEquals((byte) 1, map.remove((short) 1));
        Assert.assertEquals(2, map.size());
        Assert.assertFalse(map.isEmpty());
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        Assert.assertNull(map.remove(null));
        Assert.assertEquals(3, map.size());
        Assert.assertFalse(map.isEmpty());
    }

    @Test
    public void testRemoveNonExistentKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        Assert.assertNull(map.remove((short) 4));
        Assert.assertEquals(3, map.size());
        Assert.assertFalse(map.isEmpty());
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        Set<Entry<Short, Byte>> entrySet = map.entrySet();
        Assert.assertEquals(3, entrySet.size());
        Assert.assertFalse(entrySet.isEmpty());

        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Assert.assertTrue(iterator.hasNext());
        Entry<Short, Byte> firstEntry = iterator.next();
        Assert.assertEquals((short) 1, firstEntry.getKey().shortValue());
        Assert.assertEquals((byte) 1, firstEntry.getValue().byteValue());

        Assert.assertTrue(iterator.hasNext());
        Entry<Short, Byte> secondEntry = iterator.next();
        Assert.assertEquals((short) 2, secondEntry.getKey().shortValue());
        Assert.assertEquals((byte) 2, secondEntry.getValue().byteValue());

        Assert.assertTrue(iterator.hasNext());
        Entry<Short, Byte> thirdEntry = iterator.next();
        Assert.assertEquals((short) 3, thirdEntry.getKey().shortValue());
        Assert.assertEquals((byte) 3, thirdEntry.getValue().byteValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        Assert.assertTrue(map.containsValue((byte) 1));
        Assert.assertTrue(map.containsValue((byte) 2));
        Assert.assertTrue(map.containsValue((byte) 3));

        map.put((short) 4, (byte) 4);
        Assert.assertTrue(map.containsValue((byte) 1));
        Assert.assertTrue(map.containsValue((byte) 2));
        Assert.assertTrue(map.containsValue((byte) 3));
        Assert.assertTrue(map.containsValue((byte) 4));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        Assert.assertTrue(map.containsKey((short) 1));
        Assert.assertTrue(map.containsKey((short) 2));
        Assert.assertTrue(map.containsKey((short) 3));

        Assert.assertFalse(map.containsKey((short) 4));

        map.put((short) 4, (byte) 4);
        Assert.assertTrue(map.containsKey((short) 1));
        Assert.assertTrue(map.containsKey((short) 2));
        Assert.assertTrue(map.containsKey((short) 3));
        Assert.assertTrue(map.containsKey((short) 4));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Assert.assertEquals(0, map.size());

        map.put((short) 1, (byte) 1);
        Assert.assertEquals(1, map.size());

        map.put((short) 2, (byte) 2);
        Assert.assertEquals(2, map.size());

        map.put((short) 3, (byte) 3);
        Assert.assertEquals(3, map.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Assert.assertTrue(map.isEmpty());

        map.put((short) 1, (byte) 1);
        Assert.assertFalse(map.isEmpty());

        map.clear();
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator map1 = new TShortByteMapDecorator();
        map1.put((short) 1, (byte) 1);
        map1.put((short) 2, (byte) 2);
        map1.put((short) 3, (byte) 3);

        TShortByteMapDecorator map2 = new TShortByteMapDecorator();
        map2.put((short) 4, (byte) 4);
        map2.put((short) 5, (byte) 5);

        Set<Entry<Short, Byte>> target = map1.entrySet();
        map1.putAll(map2);

        Iterator<Entry<Short, Byte>> iterator = target.iterator();
        Entry<Short, Byte> first = iterator.next();
        Assert.assertEquals((short) 1, first.getKey().shortValue());
        Assert.assertEquals((byte) 1, first.getValue().byteValue());

        Entry<Short, Byte> second = iterator.next();
        Assert.assertEquals((short) 2, second.getKey().shortValue());
        Assert.assertEquals((byte) 2, second.getValue().byteValue());

        Entry<Short, Byte> third = iterator.next();
        Assert.assertEquals((short) 3, third.getKey().shortValue());
        Assert.assertEquals((byte) 3, third.getValue().byteValue());

        Entry<Short, Byte> fourth = iterator.next();
        Assert.assertEquals((short) 4, fourth.getKey().shortValue());
        Assert.assertEquals((byte) 4, fourth.getValue().byteValue());

        Entry<Short, Byte> fifth = iterator.next();
        Assert.assertEquals((short) 5, fifth.getKey().shortValue());
        Assert.assertEquals((byte) 5, fifth.getValue().byteValue());
    }

    @Test
    public void testExternalizable() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        map.put((short) 3, (byte) 3);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        map.writeExternal(out);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);

        TShortByteMapDecorator mapFromSerialized = new TShortByteMapDecorator();
        mapFromSerialized.readExternal(in);

        Set<Entry<Short, Byte>> entrySet = mapFromSerialized.entrySet();
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();

        Entry<Short, Byte> first = iterator.next();
        Assert.assertEquals((short) 1, first.getKey().shortValue());
        Assert.assertEquals((byte) 1, first.getValue().byteValue());

        Entry<Short, Byte> second = iterator.next();
        Assert.assertEquals((short) 2, second.getKey().shortValue());
        Assert.assertEquals((byte) 2, second.getValue().byteValue());

        Entry<Short, Byte> third = iterator.next();
        Assert.assertEquals((short) 3, third.getKey().shortValue());
        Assert.assertEquals((byte) 3, third.getValue().byteValue());
    }
}