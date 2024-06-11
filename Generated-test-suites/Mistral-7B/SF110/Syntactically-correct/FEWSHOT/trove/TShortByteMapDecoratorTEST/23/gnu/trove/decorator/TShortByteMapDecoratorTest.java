package org.example.test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Assert;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);
        Assert.assertEquals(value1, decorator.get(key1));

        Byte value2 = Byte.valueOf((byte) 2);
        Short key2 = Short.valueOf((short) 2);
        decorator.put(key2, value2);
        Assert.assertEquals(value2, decorator.get(key2));

        Assert.assertEquals(null, decorator.get(null));
        Assert.assertEquals(null, decorator.get(Short.valueOf((short) 3)));
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte) 2);
        Short key2 = Short.valueOf((short) 2);
        decorator.put(key2, value2);

        decorator.clear();

        Assert.assertEquals(0, decorator.size());
        Assert.assertNull(decorator.get(null));
        Assert.assertNull(decorator.get(Short.valueOf((short) 1)));
        Assert.assertNull(decorator.get(Short.valueOf((short) 2)));
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte) 2);
        Short key2 = Short.valueOf((short) 2);
        decorator.put(key2, value2);

        decorator.remove(key1);

        Assert.assertEquals(1, decorator.size());
        Assert.assertEquals(value2, decorator.get(Short.valueOf((short) 2)));
        Assert.assertNull(decorator.get(Short.valueOf((short) 1)));

        decorator.remove(key2);

        Assert.assertEquals(0, decorator.size());
        Assert.assertNull(decorator.get(Short.valueOf((short) 1)));
        Assert.assertNull(decorator.get(Short.valueOf((short) 2)));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte) 2);
        Short key2 = Short.valueOf((short) 2);
        decorator.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        Assert.assertEquals(key1, entry.getKey());
        Assert.assertEquals(value1, entry.getValue());

        entry = iterator.next();
        Assert.assertEquals(key2, entry.getKey());
        Assert.assertEquals(value2, entry.getValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte) 2);
        Short key2 = Short.valueOf((short) 2);
        decorator.put(key2, value2);

        Assert.assertTrue(decorator.containsValue(value1));
        Assert.assertTrue(decorator.containsValue(value2));
        Assert.assertFalse(decorator.containsValue(Byte.valueOf((byte) 3)));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte) 2);
        Short key2 = Short.valueOf((short) 2);
        decorator.put(key2, value2);

        Assert.assertTrue(decorator.containsKey(key1));
        Assert.assertTrue(decorator.containsKey(key2));
        Assert.assertFalse(decorator.containsKey(Short.valueOf((short) 3)));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte) 2);
        Short key2 = Short.valueOf((short) 2);
        decorator.put(key2, value2);

        Assert.assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        Assert.assertEquals(false, decorator.isEmpty());

        decorator.clear();

        Assert.assertEquals(true, decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map1 = new TShortByteMap();
        TShortByteMapDecorator decorator1 = new TShortByteMapDecorator(map1);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator1.put(key1, value1);

        TShortByteMap map2 = new TShortByteMap();
        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator(map2);

        Byte value2 = Byte.valueOf((byte) 2);
        Short key2 = Short.valueOf((short) 2);
        decorator2.put(key2, value2);

        Map<? extends Short, ? extends Byte> map = new TShortByteMap();
        map.putAll(decorator1);
        map.putAll(decorator2);

        TShortByteMapDecorator result = new TShortByteMapDecorator(map);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(value1, result.get(key1));
        Assert.assertEquals(value2, result.get(key2));
        Assert.assertNull(result.get(Short.valueOf((short) 3)));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInputStream(byteInput);
        TShortByteMapDecorator decoratorFromStream = (TShortByteMapDecorator)in.readObject();
        in.close();

        Assert.assertEquals(value1, decoratorFromStream.get(key1));
    }
}