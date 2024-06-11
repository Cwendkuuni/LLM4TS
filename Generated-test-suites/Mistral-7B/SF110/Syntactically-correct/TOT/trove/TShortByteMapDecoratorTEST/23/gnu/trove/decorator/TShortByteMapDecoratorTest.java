import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.iterator.TShortByteMapIterator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Short key = Short.valueOf((short) 1);
        Byte value = Byte.valueOf((byte) 2);

        decorator.put(key, value);
        assertEquals(1, decorator.size());
        assertFalse(decorator.isEmpty());
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testPutNullKey() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Byte value = Byte.valueOf((byte) 2);

        assertNull(decorator.put((Short) null, value));
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testGetNullKey() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        decorator.put((Short) 1, (Byte) 2);

        assertNull(decorator.get((Object) null));
    }

    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        decorator.clear();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        Byte removedValue = decorator.remove((Short) 1);
        assertEquals(1, decorator.size());
        assertFalse(decorator.isEmpty());
        assertEquals(Byte.valueOf((byte) 2), removedValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveNullKey() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((Short) 1, (Byte) 1);

        decorator.remove((Object) null);
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        TShortByteMap.Entry entry1 = new TShortByteMap.MapEntry((Short) 1, (Byte) 1);
        TShortByteMap.Entry entry2 = new TShortByteMap.MapEntry((Short) 2, (Byte) 2);

        assertTrue(decorator.entrySet().add(entry1));
        assertTrue(decorator.entrySet().add(entry2));
        assertEquals(2, decorator.size());

        TShortByteMap.Entry entryToRemove = new TShortByteMap.MapEntry((Short) 1, (Byte) 1);
        decorator.entrySet().remove(entryToRemove);
        assertEquals(1, decorator.size());
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 1, (Byte) 2);

        assertTrue(decorator.containsValue(Byte.valueOf((byte) 1)));
        assertTrue(decorator.containsValue(Byte.valueOf((byte) 2)));
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((Short) 1, (Byte) 1);

        assertTrue(decorator.containsKey((Short) 1));
        assertFalse(decorator.containsKey((Short) 2));
    }

    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map1 = new TShortByteMapImpl();
        TShortByteMap map2 = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map1);

        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        TShortByteMap map3 = new TShortByteMapImpl();
        map3.put((Short) 3, (Byte) 3);
        map3.put((Short) 4, (Byte) 4);

        decorator.putAll(map3);
        assertEquals(4, decorator.size());
        assertEquals(Byte.valueOf((byte) 3), decorator.get((Short) 3));
        assertEquals(Byte.valueOf((byte) 4), decorator.get((Short) 4));
    }

    @Test
    public void testSerializability() throws IOException, ClassNotFoundException {
        TShortByteMap map = new TShortByteMapImpl();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        decorator.writeExternal(out);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator) in.readObject();
        in.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        assertArrayEquals(decorator.getMap().toArray(), deserializedDecorator.getMap().toArray());
    }
}