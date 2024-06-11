package org.apache.commons.cli; // Assuming you want to test TShortByteMapDecorator in the same package as Util for the sake of convenience

import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutGetAndRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);
        assertEquals(2, decorator.size());

        Byte getValue1 = decorator.get((short) 1);
        assertEquals((byte) 1, getValue1);

        Byte getValue2 = decorator.get((short) 2);
        assertEquals((byte) 2, getValue2);

        decorator.remove((short) 1);
        assertEquals(1, decorator.size());
        assertNull(decorator.get((short) 1));
    }

    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.put((Short) null, (Byte) null));
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (Byte) null);
        assertEquals(1, decorator.size());
        Byte value = decorator.get((short) 1);
        assertNull(value);
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();
        Entry<Short, Byte> entry1 = iterator.next();
        assertEquals((short) 1, entry1.getKey());
        assertEquals((byte) 1, entry1.getValue());

        Entry<Short, Byte> entry2 = iterator.next();
        assertEquals((short) 2, entry2.getKey());
        assertEquals((byte) 2, entry2.getValue());
    }

    @Test
    public void testContainsKeyAndValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 2));
        assertFalse(decorator.containsKey((short) 3));

        assertTrue(decorator.containsValue((byte) 1));
        assertTrue(decorator.containsValue((byte) 2));
        assertFalse(decorator.containsValue((byte) 3));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);
        decorator.clear();

        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator1 = new TShortByteMapDecorator();
        decorator1.put((short) 1, (byte) 1);
        decorator1.put((short) 2, (byte) 2);

        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
        decorator2.put((short) 3, (byte) 3);
        decorator2.put((short) 4, (byte) 4);

        decorator1.putAll(decorator2.entrySet());

        assertEquals(4, decorator1.size());
        assertTrue(decorator1.containsKey((short) 1));
        assertTrue(decorator1.containsKey((short) 2));
        assertTrue(decorator1.containsKey((short) 3));
        assertTrue(decorator1.containsKey((short) 4));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteOutputStream);
        decorator.writeExternal(objectOutput);
        objectOutput.close();
        byte[] bytes = byteOutputStream.toByteArray();

        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = new ObjectInputStream(byteInputStream);
        TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
        deserializedDecorator.readExternal(objectInput);
        objectInput.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 2));
        assertTrue(deserializedDecorator.containsKey((short) 1));
        assertTrue(deserializedDecorator.containsKey((short) 2));
    }

}