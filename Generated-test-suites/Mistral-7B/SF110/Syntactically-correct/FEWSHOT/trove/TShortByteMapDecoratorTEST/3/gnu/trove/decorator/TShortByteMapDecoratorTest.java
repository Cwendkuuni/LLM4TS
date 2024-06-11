package org.apache.commons.cli; // Assuming you want to test the TShortByteMapDecorator class in the given package

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.isA;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMap.MultiMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;

@RunWith(Parameterized.class)
public class TShortByteMapDecoratorTest {

    private TShortByteMap _map;
    private TShortByteMapDecorator _decorator;

    @Parameters(name = "{0} - {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new TShortByteMapImpl(), null},
                {new TShortByteMapImpl(), new TShortByteMapDecorator(new TShortByteMapImpl())},
                {new MultiMap(), new TShortByteMapDecorator(new MultiMap())}
        });
    }

    @Before
    public void setup() {
        _map = (TShortByteMap) data[0][0];
        _decorator = (TShortByteMapDecorator) data[0][1];
    }

    @Test
    public void testConstructor() {
        assertEquals("TShortByteMapDecorator", _decorator.getClass().getSimpleName());
        assertEquals("TShortByteMapDecorator[Map=null]", _decorator.toString());
    }

    @Test
    public void testGetMap() {
        assertSame(_map, _decorator.getMap());
    }

    @Test
    public void testPut() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        Byte putValue1 = _decorator.put(key1, value1);
        assertNull(putValue1);
        assertEquals(value1, _decorator.get(key1));

        Byte putValue2 = _decorator.put(key1, value2);
        assertSame(value1, _decorator.get(key1));
        assertNull(putValue2);

        Byte putValue3 = _decorator.put((short) 2, value2);
        assertNull(putValue3);
        assertEquals(value2, _decorator.get((short) 2));
    }

    @Test
    public void testGet() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        _decorator.put(key1, value1);
        _decorator.put(key1, value2);

        assertNull(_decorator.get(null));
        assertNull(_decorator.get((short) -1));
        assertEquals(value2, _decorator.get(key1));
        assertEquals(null, _decorator.get((short) 2));
    }

    @Test
    public void testClear() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        _decorator.put(key1, value1);
        _decorator.put(key1, value2);

        _decorator.clear();
        assertTrue(_decorator.isEmpty());
        assertNull(_decorator.get(key1));
    }

    @Test
    public void testRemove() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        _decorator.put(key1, value1);
        _decorator.put(key1, value2);

        Byte removedValue = _decorator.remove(key1);
        assertSame(value2, removedValue);
        assertNull(_decorator.get(key1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        _decorator.put(null, null);
    }

    @Test
    public void testEntrySet() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        _decorator.put(key1, value1);
        _decorator.put(key1, value2);

        Set<Entry<Short, Byte>> entrySet = _decorator.entrySet();
        assertEquals(1, entrySet.size());
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return key1;
            }

            @Override
            public Byte getValue() {
                return value2;
            }
        }));

        Byte newValue = (byte) 3;
        Byte oldValue = _decorator.get(key1);
        Byte putValue = _decorator.put(key1, newValue);
        assertEquals(oldValue, putValue);
        assertEquals(newValue, _decorator.get(key1));
        assertTrue(entrySet.contains(new Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return key1;
            }

            @Override
            public Byte getValue() {
                return newValue;
            }
        }));
    }

    @Test
    public void testContainsValue() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        _decorator.put(key1, value1);
        _decorator.put(key1, value2);

        assertTrue(_decorator.containsValue(value1));
        assertTrue(_decorator.containsValue(value2));
        assertFalse(_decorator.containsValue((byte) 3));
    }

    @Test
    public void testContainsKey() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        _decorator.put(key1, value1);
        _decorator.put(key1, value2);

        assertTrue(_decorator.containsKey(key1));
        assertFalse(_decorator.containsKey((short) 2));
    }

    @Test
    public void testSize() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        _decorator.put(key1, value1);
        _decorator.put(key1, value2);

        assertEquals(1, _decorator.size());
    }

    @Test
    public void testIsEmpty() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Byte value2 = (byte) 2;

        _decorator.put(key1, value1);
        _decorator.put(key1, value2);
        _decorator.clear();

        assertTrue(_decorator.isEmpty());
        assertEquals(0, _decorator.size());
    }

    @Test
    public void testPutAll() {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;
        Short key2 = (short) 2;
        Byte value2 = (byte) 2;

        Map<Short, Byte> map = new TShortByteMapImpl();
        map.put(key1, value1);
        map.put(key2, value2);

        _decorator.putAll(map);

        assertEquals(2, _decorator.size());
        assertTrue(_decorator.containsKey(key1));
        assertTrue(_decorator.containsKey(key2));
        assertTrue(_decorator.containsValue(value1));
        assertTrue(_decorator.containsValue(value2));
    }

    @Test
    public void testWriteReadExternal() throws IOException, ClassNotFoundException {
        Short key1 = (short) 1;
        Byte value1 = (byte) 1;

        _decorator.put(key1, value1);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(output);
        _decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ObjectInput in = new ObjectInputStream(input);
        TShortByteMapDecorator decorator = (TShortByteMapDecorator) in.readObject();
        in.close();

        assertEquals(1, decorator.size());
        assertTrue(decorator.containsKey(key1));
        assertTrue(decorator.containsValue(value1));
    }
}