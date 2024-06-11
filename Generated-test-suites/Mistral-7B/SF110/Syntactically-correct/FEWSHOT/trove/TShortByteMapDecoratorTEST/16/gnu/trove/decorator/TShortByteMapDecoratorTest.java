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
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get(Short.MIN_VALUE));
        assertNull(decorator.get(Short.MAX_VALUE));
        assertFalse(decorator.containsKey(Short.MIN_VALUE));
        assertFalse(decorator.containsKey(Short.MAX_VALUE));
        assertFalse(decorator.containsValue((byte)0));
    }

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);
        assertEquals(1, decorator.size());
        Byte retrievedValue = decorator.get(key1);
        assertEquals(value1, retrievedValue);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);
        assertEquals(2, decorator.size());
        Byte retrievedValue2 = decorator.get(key2);
        assertEquals(value2, retrievedValue2);

        Byte retrievedValue1 = decorator.get(key1);
        assertEquals(value1, retrievedValue1);

        Short nullKey = Short.valueOf((short)0);
        assertNull(decorator.get(nullKey));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);

        assertEquals(2, decorator.size());
        decorator.clear();
        assertEquals(0, decorator.size());
        assertNull(decorator.get(key1));
        assertNull(decorator.get(key2));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);

        Byte removedValue = decorator.remove(key1);
        assertEquals(value1, removedValue);
        assertEquals(1, decorator.size());
        assertNull(decorator.get(key1));
        Byte retrievedValue = decorator.get(key2);
        assertEquals(value2, retrievedValue);

        decorator.remove(key2);
        assertEquals(0, decorator.size());
        assertNull(decorator.get(key1));
        assertNull(decorator.get(key2));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry = iterator.next();
        assertEquals(key1, entry.getKey());
        assertEquals(value1, entry.getValue());

        entry = iterator.next();
        assertEquals(key2, entry.getKey());
        assertEquals(value2, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);

        assertTrue(decorator.containsValue(value1));
        assertTrue(decorator.containsValue(value2));
        assertFalse(decorator.containsValue((byte)3));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);

        assertTrue(decorator.containsKey(key1));
        assertTrue(decorator.containsKey(key2));
        assertFalse(decorator.containsKey((short)0));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);

        assertFalse(decorator.isEmpty());

        decorator.clear();

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator1 = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator1.put(key1, value1);

        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator2.put(key2, value2);

        decorator1.putAll(decorator2);

        assertEquals(2, decorator1.size());

        Byte retrievedValue1 = decorator1.get(key1);
        assertEquals(value1, retrievedValue1);

        Byte retrievedValue2 = decorator1.get(key2);
        assertEquals(value2, retrievedValue2);
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Byte value1 = Byte.valueOf((byte)1);
        Short key1 = Short.valueOf((short)1);
        decorator.put(key1, value1);

        Byte value2 = Byte.valueOf((byte)2);
        Short key2 = Short.valueOf((short)2);
        decorator.put(key2, value2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput output = new ObjectOutputStream(outputStream);
        decorator.writeExternal(output);
        output.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput input = new ObjectInputStream(inputStream);
        TShortByteMapDecorator decoratedCopy = (TShortByteMapDecorator)input.readObject();
        input.close();

        assertEquals(2, decoratedCopy.size());

        Byte retrievedValue1 = decoratedCopy.get(key1);
        assertEquals(value1, retrievedValue1);

        Byte retrievedValue2 = decoratedCopy.get(key2);
        assertEquals(value2, retrievedValue2);
    }
}