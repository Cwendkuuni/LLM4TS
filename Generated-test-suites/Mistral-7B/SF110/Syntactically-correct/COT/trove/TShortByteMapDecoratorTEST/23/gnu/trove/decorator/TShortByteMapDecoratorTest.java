package tests;

import static org.junit.Assert.*;

import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TShortByteMapDecoratorTest {

    private TShortByteMap _map;
    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        _map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(_map);
    }

    @After
    public void tearDown() {
        decorator = null;
        _map = null;
    }

    // Test constructor
    @Test
    public void testConstructor() {
        assertNotNull(decorator);
    }

    // Test getMap()
    @Test
    public void testGetMap() {
        TShortByteMap map = decorator.getMap();
        assertSame(map, _map);
    }

    // Test put() with null key
    @Test
    public void testPutNullKey() {
        short key = _map.getNoEntryKey();
        byte value = 0;
        byte result = decorator.put(key, value);
        assertNull(result);
        assertEquals(key, decorator.get(key));
        assertEquals(value, decorator.get(key));
    }

    // Test put() with valid key and value
    @Test
    public void testPutValidKeyAndValue() {
        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        byte result = decorator.put(key, value);
        assertNull(result);
        assertEquals(key, decorator.get(key));
        assertEquals(value, decorator.get(key));
    }

    // Test put() with existing key
    @Test
    public void testPutExistingKey() {
        Short key = Short.valueOf((short)1);
        Byte value1 = Byte.valueOf((byte)1);
        Byte value2 = Byte.valueOf((byte)2);
        decorator.put(key, value1);
        byte result = decorator.put(key, value2);
        assertNull(result);
        assertEquals(value2, decorator.get(key));
    }

    // Test get() with null key
    @Test
    public void testGetNullKey() {
        assertNull(decorator.get(null));
    }

    // Test get() with valid key
    @Test
    public void testGetValidKey() {
        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        assertEquals(value, decorator.get(key));
    }

    // Test get() with non-existent key
    @Test
    public void testGetNonExistentKey() {
        assertNull(decorator.get(Short.valueOf((short)2)));
    }

    // Test clear()
    @Test
    public void testClear() {
        Short key1 = Short.valueOf((short)1);
        Byte value1 = Byte.valueOf((byte)1);
        Short key2 = Short.valueOf((short)2);
        Byte value2 = Byte.valueOf((byte)2);
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    // Test remove() with null key
    @Test
    public void testRemoveNullKey() {
        assertNull(decorator.remove(null));
    }

    // Test remove() with valid key
    @Test
    public void testRemoveValidKey() {
        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        byte removedValue = decorator.remove(key);
        assertNull(removedValue);
        assertTrue(decorator.isEmpty());
    }

    // Test remove() with non-existent key
    @Test
    public void testRemoveNonExistentKey() {
        assertNull(decorator.remove(Short.valueOf((short)2)));
    }

    // Test entrySet()
    @Test
    public void testEntrySet() {
        Short key1 = Short.valueOf((short)1);
        Byte value1 = Byte.valueOf((byte)1);
        Short key2 = Short.valueOf((short)2);
        Byte value2 = Byte.valueOf((byte)2);
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            public Short getKey() {
                return key1;
            }

            public Byte getValue() {
                return value1;
            }
        }));
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            public Short getKey() {
                return key2;
            }

            public Byte getValue() {
                return value2;
            }
        }));
    }

    // Test containsValue() with null value
    @Test
    public void testContainsValueNullValue() {
        assertFalse(decorator.containsValue(null));
    }

    // Test containsValue() with valid value
    @Test
    public void testContainsValueValidValue() {
        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        assertTrue(decorator.containsValue(value));
    }

    // Test containsKey() with null key
    @Test
    public void testContainsKeyNullKey() {
        assertFalse(decorator.containsKey(null));
    }

    // Test containsKey() with valid key
    @Test
    public void testContainsKeyValidKey() {
        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        assertTrue(decorator.containsKey(key));
    }

    // Test size()
    @Test
    public void testSize() {
        Short key1 = Short.valueOf((short)1);
        Byte value1 = Byte.valueOf((byte)1);
        Short key2 = Short.valueOf((short)2);
        Byte value2 = Byte.valueOf((byte)2);
        decorator.put(key1, value1);
        decorator.put(key2, value2);
        assertEquals(2, decorator.size());
    }

    // Test isEmpty()
    @Test
    public void testIsEmpty() {
        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        assertFalse(decorator.isEmpty());
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    // Test putAll() with empty map
    @Test
    public void testPutAllEmptyMap() {
        Map<Short, Byte> map = Collections.emptyMap();
        decorator.putAll(map);
        assertTrue(decorator.isEmpty());
    }

    // Test putAll() with non-empty map
    @Test
    public void testPutAllNonEmptyMap() {
        Map<Short, Byte> map = new TShortByteMap();
        map.put(Short.valueOf((short)1), Byte.valueOf((byte)1));
        map.put(Short.valueOf((short)2), Byte.valueOf((byte)2));
        decorator.putAll(map);
        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(Short.valueOf((short)1)));
        assertTrue(decorator.containsKey(Short.valueOf((short)2)));
    }

    // Test Externalizable
    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        Short key = Short.valueOf((short)1);
        Byte value = Byte.valueOf((byte)1);
        decorator.put(key, value);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        decorator.writeExternal(out);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TShortByteMapDecorator dec = (TShortByteMapDecorator)in.readObject();
        assertEquals(key, dec.get(key));
        assertEquals(value, dec.get(key));
    }
}