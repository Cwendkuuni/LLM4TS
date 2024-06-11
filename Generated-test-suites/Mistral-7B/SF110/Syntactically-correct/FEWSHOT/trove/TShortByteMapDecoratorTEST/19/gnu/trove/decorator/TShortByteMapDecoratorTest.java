package org.apache.commons.cli; // Assuming the test class is in the same package as the TShortByteMapDecorator class for simplicity

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    @Test
    public void testCreateInstance() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNotNull(decorator);
        
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decoratorWithMap = new TShortByteMapDecorator(map);
        assertEquals(map, decoratorWithMap.getMap());
    }

    @Test
    public void testPutGetClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
        
        decorator.clear();
        assertNull(decorator.get(key));
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        
        Byte removedValue = decorator.remove(key);
        assertEquals(value, removedValue);
        
        assertNull(decorator.get(key));
    }

    @Test
    public void testSizeAndIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());

        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);
        assertEquals(1, decorator.size());
        assertFalse(decorator.isEmpty());

        decorator.clear();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testContainsKeyAndContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);

        assertFalse(decorator.containsKey(key));
        assertFalse(decorator.containsValue(Byte.valueOf((byte) 1)));

        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);

        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        decorator.put(key1, value1);

        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
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

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        decorator.put(key, value);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteStream);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInput in = new ObjectInputStream(byteInput);
        TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
        decorator2.readExternal(in);
        in.close();

        assertEquals(map.size(), decorator2.getMap().size());
        assertTrue(decorator2.containsKey(key));
        assertEquals(value, decorator2.get(key));
    }
}