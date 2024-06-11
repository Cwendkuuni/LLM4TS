package org.apache.commons.cli;

import static org.junit.Assert.*;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import java.util.Collections;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TShortByteMapDecoratorTest {

    @Test
    public void testTShortByteMapDecorator() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);

        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(key1));
        assertTrue(decorator.containsKey(key2));
        assertEquals(value1, decorator.get(key1));
        assertEquals(value2, decorator.get(key2));
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);

        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        decorator.clear();

        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey(key1));
        assertFalse(decorator.containsKey(key2));
        assertNull(decorator.get(key1));
        assertNull(decorator.get(key2));
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);

        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        decorator.remove(key1);

        assertEquals(1, decorator.size());
        assertFalse(decorator.containsKey(key1));
        assertTrue(decorator.containsKey(key2));
        assertEquals(value2, decorator.get(key2));
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);

        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();

        assertEquals(2, entrySet.size());
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> firstEntry = iterator.next();
        Entry<Short, Byte> secondEntry = iterator.next();
        assertTrue(firstEntry.getKey().equals(key1) && firstEntry.getValue().equals(value1));
        assertTrue(secondEntry.getKey().equals(key2) && secondEntry.getValue().equals(value2));
    }

    @Test
    public void testExternalizable() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);

        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();

        byte[] bytes = byteStream.toByteArray();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(byteInputStream);
        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
        decorator2.readExternal(in);
        in.close();

        assertEquals(decorator.size(), decorator2.size());
        assertTrue(decorator.containsKey(key1) && decorator2.containsKey(key1) && decorator.get(key1).equals(decorator2.get(key1)) && decorator.containsKey(key2) && decorator2.containsKey(key2) && decorator.get(key2).equals(decorator2.get(key2)));
    }

    @Test
    public void testCloneable() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);

        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        TShortByteMapDecorator decoratorClone = (TShortByteMapDecorator) decorator.clone();

        assertEquals(decorator.size(), decoratorClone.size());
        assertTrue(decorator.containsKey(key1) && decoratorClone.containsKey(key1) && decorator.get(key1).equals(decoratorClone.get(key1)) && decorator.containsKey(key2) && decoratorClone.containsKey(key2) && decorator.get(key2).equals(decoratorClone.get(key2)));
    }
}