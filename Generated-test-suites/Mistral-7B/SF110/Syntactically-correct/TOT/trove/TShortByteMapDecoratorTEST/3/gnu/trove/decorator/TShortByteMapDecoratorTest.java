import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.views.TShortByteMapView;
import gnu.trove.map.hash.TShortByteHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TShortByteMapDecoratorTest {

    @Test
    public void testDefaultConstructor() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.getMap());
    }

    @Test
    public void testConstructWithMap() {
        TShortByteMap map = new TShortByteHashMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testPutNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.put((Short) null, (Byte) 0));
        TShortByteMap map = decorator.getMap();
        assertFalse(map.containsKey(Short.MIN_VALUE));
    }

    @Test
    public void testPutNullValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.put((Short) 1, (Byte) null));
        TShortByteMap map = decorator.getMap();
        assertEquals(Byte.MIN_VALUE, map.get(1));
    }

    @Test
    public void testPutExistingKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Short key = Short.valueOf("1");
        Byte oldValue = Byte.valueOf("5");
        Byte newValue = Byte.valueOf("10");

        decorator.put(key, oldValue);
        assertEquals(oldValue, decorator.get(key));

        decorator.put(key, newValue);
        assertEquals(newValue, decorator.get(key));
        assertFalse(oldValue.equals(newValue));
    }

    @Test
    public void testGetNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertNull(decorator.get(null));
    }

    @Test
    public void testGetWrongTypeKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);

        assertNull(decorator.get("String"));
    }

    @Test
    public void testGetWrongTypeValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);

        assertNull(decorator.get(1.0));
    }

    @Test
    public void testGetKeyExists() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);

        Short key = Short.valueOf("1");
        Byte value = decorator.get(key);

        assertEquals(key, decorator.get(key));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);
        decorator.clear();

        TShortByteMap map = decorator.getMap();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);

        assertNull(decorator.remove(null));
        TShortByteMap map = decorator.getMap();
        assertTrue(map.containsKey(1));
    }

    @Test
    public void testRemoveWrongTypeKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);

        assertNull(decorator.remove("String"));
        TShortByteMap map = decorator.getMap();
        assertTrue(map.containsKey(1));
    }

    @Test
    public void testRemoveWrongTypeValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);

        assertNull(decorator.remove(1.0));
        TShortByteMap map = decorator.getMap();
        assertTrue(map.containsKey(1));
    }

    @Test
    public void testRemoveKeyExists() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        Byte removedValue = decorator.remove((Short) 1);
        assertEquals(Byte.valueOf("1"), removedValue);
        assertFalse(decorator.getMap().containsKey(1));
    }

    @Test
    public void testEntrySet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
        Iterator<Entry<Short, Byte>> iterator = entrySet.iterator();

        Entry<Short, Byte> entry = iterator.next();
        assertEquals(1, entry.getKey().shortValue());
        assertEquals(Byte.valueOf("1"), entry.getValue());

        entry = iterator.next();
        assertEquals(2, entry.getKey().shortValue());
        assertEquals(Byte.valueOf("2"), entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);

        assertTrue(decorator.containsValue(Byte.valueOf("1")));
        assertFalse(decorator.containsValue(Byte.valueOf("2")));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);

        assertTrue(decorator.containsKey((Short) 1));
        assertFalse(decorator.containsKey((Short) 2));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();

        assertTrue(decorator.isEmpty());
        decorator.put((Short) 1, (Byte) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteHashMap();
        map.put((Short) 1, (Byte) 1);
        map.put((Short) 2, (Byte) 2);

        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.putAll(map);

        TShortByteMap mapDecorator = decorator.getMap();
        assertEquals(map, mapDecorator);
    }
}