package com.example.junit; // replace with your package name

import gnu.trove.decorator.TDoubleShortMap;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.iterator.TObjectShortIterator;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterators;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testConstructor() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Assert.assertNotNull(map);
    }

    @Test
    public void testConstructorWithMap() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Assert.assertSame(map, decorator.getMap());
    }

    @Test
    public void testGetMap() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Assert.assertSame(map, decorator.getMap());
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Assert.assertNull(map.put(null, (short) 0));
        Assert.assertEquals(0, map.size());
    }

    @Test
    public void testPutValidKeyAndValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        double key = 1.0;
        short value = 1;
        Assert.assertEquals(null, map.put(key, value));
        Assert.assertEquals(1, map.size());
        Assert.assertEquals(value, map.get(key));
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        double key = 1.0;
        map.put(key, null);
    }

    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Assert.assertNull(map.get(null));
    }

    @Test
    public void testGetValidKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        double key = 1.0;
        short value = 1;
        map.put(key, value);
        Assert.assertEquals(value, map.get(key));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.clear();
        Assert.assertEquals(0, map.size());
    }

    @Test
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        Assert.assertEquals((short) 1, map.remove(null));
        Assert.assertEquals(0, map.size());
    }

    @Test
    public void testRemoveValidKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        Assert.assertEquals((short) 1, map.remove(1.0));
        Assert.assertEquals(0, map.size());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveKeyNotPresent() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Assert.assertEquals(0, map.remove(1.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);

        Set<Entry<Double, Short>> entrySet = map.entrySet();
        Assert.assertEquals(1, entrySet.size());

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();
        Assert.assertEquals(1.0, entry.getKey());
        Assert.assertEquals((short) 1, entry.getValue());

        iterator.remove();
        Assert.assertEquals(0, map.size());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        Assert.assertTrue(map.containsValue((short) 1));
        map.put(2.0, (short) 2);
        Assert.assertTrue(map.containsValue((short) 1));
        Assert.assertTrue(map.containsValue((short) 2));
        map.clear();
        Assert.assertFalse(map.containsValue((short) 1));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        Assert.assertTrue(map.containsKey(1.0));
        map.clear();
        Assert.assertFalse(map.containsKey(1.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Assert.assertEquals(0, map.size());
        map.put(1.0, (short) 1);
        Assert.assertEquals(1, map.size());
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
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.putAll(map);

        Assert.assertEquals(2, decorator.size());
        Assert.assertEquals((short) 1, decorator.get(1.0));
        Assert.assertEquals((short) 2, decorator.get(2.0));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);

        ByteArrayOutputStream byteStreamOut = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStreamOut);
        map.writeExternal(out);
        out.close();

        ByteArrayInputStream byteStreamIn = new ByteArrayInputStream(byteStreamOut.toByteArray());
        ObjectInput in = new ObjectInputStream(byteStreamIn);
        TDoubleShortMapDecorator mapFromStream = (TDoubleShortMapDecorator) in.readObject();
        in.close();

        Assert.assertEquals(1.0, mapFromStream.get(1.0));
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);

        TDoubleShortMapDecorator clonedMap = (TDoubleShortMapDecorator) map.clone();

        Assert.assertEquals(map.size(), clonedMap.size());
        Assert.assertEquals(map.get(1.0), clonedMap.get(1.0));
    }
}