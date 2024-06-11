package org.apache.commons.cli; // Assuming you want to write tests in the same package as the class under test for simplicity

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get(Short.MIN_VALUE));
        assertNull(decorator.put(Short.MIN_VALUE, (byte) 1));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPutGetRemove() {
        TShortByteMap baseMap = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(baseMap);

        byte value1 = 1;
        Short key1 = Short.valueOf((short) 1);
        byte value2 = 2;
        Short key2 = Short.valueOf((short) 2);
        byte value3 = 3;
        Short key3 = Short.valueOf((short) 3);

        decorator.put(key1, value1);
        assertEquals(1, decorator.size());
        assertEquals(value1, decorator.get(key1));
        assertNull(decorator.get(key2));

        decorator.put(key2, value2);
        assertEquals(2, decorator.size());
        assertEquals(value2, decorator.get(key2));
        assertEquals(value1, decorator.get(key1));

        decorator.remove(key1);
        assertEquals(1, decorator.size());
        assertNull(decorator.get(key1));
        assertEquals(value2, decorator.get(key2));

        decorator.remove(key2);
        assertEquals(0, decorator.size());
        assertNull(decorator.get(key1));
        assertNull(decorator.get(key2));
    }

    @Test
    public void testPutGetRemoveAll() {
        TShortByteMap baseMap = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(baseMap);

        byte value1 = 1;
        Short key1 = Short.valueOf((short) 1);
        byte value2 = 2;
        Short key2 = Short.valueOf((short) 2);
        byte value3 = 3;
        Short key3 = Short.valueOf((short) 3);

        decorator.put(key1, value1);
        decorator.put(key2, value2);
        decorator.put(key3, value3);

        assertEquals(3, decorator.size());
        assertEquals(value1, decorator.get(key1));
        assertEquals(value2, decorator.get(key2));
        assertEquals(value3, decorator.get(key3));

        decorator.clear();
        assertEquals(0, decorator.size());
        assertNull(decorator.get(key1));
        assertNull(decorator.get(key2));
        assertNull(decorator.get(key3));

        decorator.putAll(Arrays.asList(new Entry[] { new ShortShortEntry(key1, value1), new ShortShortEntry(key2, value2), new ShortShortEntry(key3, value3) }));
        assertEquals(3, decorator.size());
        assertEquals(value1, decorator.get(key1));
        assertEquals(value2, decorator.get(key2));
        assertEquals(value3, decorator.get(key3));
    }

    @Test
    public void testIterator() {
        TShortByteMap baseMap = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(baseMap);

        byte value1 = 1;
        Short key1 = Short.valueOf((short) 1);
        byte value2 = 2;
        Short key2 = Short.valueOf((short) 2);
        byte value3 = 3;
        Short key3 = Short.valueOf((short) 3);

        decorator.put(key1, value1);
        decorator.put(key2, value2);
        decorator.put(key3, value3);

        Iterator<Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
        assertEquals(key1, iterator.next().getKey());
        assertEquals(value1, iterator.next().getValue());
        assertEquals(key2, iterator.next().getKey());
        assertEquals(value2, iterator.next().getValue());
        assertEquals(key3, iterator.next().getKey());
        assertEquals(value3, iterator.next().getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testExternalization() throws IOException, ClassNotFoundException {
        TShortByteMap baseMap = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(baseMap);

        byte value1 = 1;
        Short key1 = Short.valueOf((short) 1);
        decorator.put(key1, value1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutput objOut = new ObjectOutputStream(out);
        decorator.writeExternal(objOut);
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInput objIn = new ObjectInputStream(in);
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator) objIn.readObject();
        objIn.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        assertEquals(decorator.get(key1), deserializedDecorator.get(key1));
    }
}