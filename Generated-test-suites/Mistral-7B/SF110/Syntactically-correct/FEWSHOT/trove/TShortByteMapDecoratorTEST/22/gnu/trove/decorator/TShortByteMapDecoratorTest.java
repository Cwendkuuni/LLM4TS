package org.apache.commons.cli; // This is just for consistency with the given example, adjust the package name if necessary

import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testPutGetKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        map.put(key, value);

        assertTrue(map.containsKey(key));
        assertEquals(value, map.get(key));
    }

    @Test
    public void testPutGetNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte value = Byte.valueOf((byte) 1);
        map.put(null, value);

        assertNull(map.get(null));
    }

    @Test
    public void testPutGetNullValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        map.put(key, null);

        assertEquals(null, map.get(key));
    }

    @Test
    public void testPutTwiceSameKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key, value1);
        map.put(key, value2);

        assertEquals(value2, map.get(key));
    }

    @Test
    public void testPutGetDifferentKeys() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key1, value1);
        map.put(key2, value2);

        assertEquals(value1, map.get(key1));
        assertEquals(value2, map.get(key2));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key1, value1);
        map.put(key2, value2);

        map.clear();

        assertFalse(map.containsKey(key1));
        assertFalse(map.containsKey(key2));
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        map.put(key, value);

        Byte removedValue = map.remove(key);

        assertNull(map.get(key));
        assertEquals(removedValue, value);
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Byte removedValue = map.remove(null);

        assertNull(removedValue);
    }

    @Test
    public void testRemoveNonExistentKey() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);

        assertNull(map.remove(key));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key1, value1);
        map.put(key2, value2);

        Set<Entry<Short, Byte>> entrySet = map.entrySet();

        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return key1;
            }

            @Override
            public Byte getValue() {
                return value1;
            }

            @Override
            public boolean equals(Object o) {
                if (o instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                    return entry.getKey().equals(key1) && entry.getValue().equals(value1);
                }
                return false;
            }

            @Override
            public int hashCode() {
                return key1.hashCode() + value1.hashCode();
            }
        }));
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return key2;
            }

            @Override
            public Byte getValue() {
                return value2;
            }

            @Override
            public boolean equals(Object o) {
                if (o instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                    return entry.getKey().equals(key2) && entry.getValue().equals(value2);
                }
                return false;
            }

            @Override
            public int hashCode() {
                return key2.hashCode() + value2.hashCode();
            }
        }));
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        Byte value2 = Byte.valueOf((byte) 2);
        map.put(key, value1);

        assertTrue(map.containsValue(value1));
        assertFalse(map.containsValue(value2));
    }

    @Test
    public void testPutAll() {
        TShortByteMap map1 = new TShortByteMap();
        Short key1 = Short.valueOf((short) 1);
        Byte value1 = Byte.valueOf((byte) 1);
        map1.put(key1, value1);

        TShortByteMapDecorator map2 = new TShortByteMapDecorator();
        Short key2 = Short.valueOf((short) 2);
        Byte value2 = Byte.valueOf((byte) 2);
        map2.put(key2, value2);

        map2.putAll(map1);

        assertTrue(map2.containsKey(key1));
        assertEquals(value1, map2.get(key1));
        assertTrue(map2.containsKey(key2));
        assertEquals(value2, map2.get(key2));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 1);
        map.put(key, value);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteStream);
        map.writeExternal(out);
        out.close();
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        TShortByteMapDecorator mapDeserialized = (TShortByteMapDecorator)in.readObject();
        in.close();

        assertEquals(map.get(key), mapDeserialized.get(key));
    }
}