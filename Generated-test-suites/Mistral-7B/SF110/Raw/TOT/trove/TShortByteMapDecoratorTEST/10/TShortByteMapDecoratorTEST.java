package gnu.trove.decorator.test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TShortByteMap decoratorMap = new TShortByteMapDecorator();
        assertNotNull(decoratorMap);
        assertTrue(decoratorMap.isEmpty());
        assertEquals(0, decoratorMap.size());
        assertNull(decoratorMap.get((Short) null));
        assertNull(decoratorMap.get(1));
        assertNull(decoratorMap.remove((Short) null));
        assertNull(decoratorMap.remove(1));
    }

    @Test
    public void testPutGetAndRemove() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");
        decoratorMap.put((Short) 2, (Byte) "two");

        assertEquals("one", decoratorMap.get((Short) 1));
        assertEquals("two", decoratorMap.get((Short) 2));
        assertNull(decoratorMap.get((Short) 3));

        decoratorMap.remove((Short) 1);
        assertNull(decoratorMap.get((Short) 1));
        assertEquals("two", decoratorMap.get((Short) 2));

        decoratorMap.remove((Short) 2);
        assertNull(decoratorMap.get((Short) 1));
        assertNull(decoratorMap.get((Short) 2));
    }

    @Test
    public void testIterator() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");
        decoratorMap.put((Short) 2, (Byte) "two");
        decoratorMap.put((Short) 3, (Byte) "three");

        TShortByteIterator it = decoratorMap.entrySet().iterator();

        assertSame(Map.Entry.class, it.next().getClass());
        assertEquals((Short) 1, it.next().getKey());
        assertEquals((Byte) "one", it.next().getValue());
        assertEquals((Short) 2, it.next().getKey());
        assertEquals((Byte) "two", it.next().getValue());
        assertEquals((Short) 3, it.next().getKey());
        assertEquals((Byte) "three", it.next().getValue());

        assertFalse(it.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        TShortByteMapDecorator decoratorMap = new TShortByteMapDecorator();
        decoratorMap.put(1, 1);
        decoratorMap.put(2, 2);
        decoratorMap.put(3, 3);

        try {
            decoratorMap.add(Map.entry(1, 1));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            decoratorMap.remove(2);
            decoratorMap.add(Map.entry(2, 1));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");
        decoratorMap.put((Short) 2, (Byte) "two");

        assertTrue(decoratorMap.entrySet().isEmpty());

        decoratorMap.entrySet().add(Map.entry((Short) 3, (Byte) "three"));
        assertFalse(decoratorMap.entrySet().isEmpty());

        assertEquals(1, decoratorMap.size());

        decoratorMap.entrySet().remove(Map.entry((Short) 1, (Byte) "one"));
        assertEquals(0, decoratorMap.size());
    }

    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");
        decoratorMap.put((Short) 2, (Byte) "two");

        assertTrue(decoratorMap.containsKey((Short) 1));
        assertTrue(decoratorMap.containsKey((Short) 2));
        assertFalse(decoratorMap.containsKey((Short) 3));
    }

    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");
        decoratorMap.put((Short) 1, (Byte) "two");

        assertTrue(decoratorMap.containsValue((Byte) "one"));
        assertTrue(decoratorMap.containsValue((Byte) "two"));
        assertFalse(decoratorMap.containsValue((Byte) "three"));
    }

    @Test
    public void testPutNullKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");

        assertNull(decoratorMap.put((Short) null, (Byte) "two"));
        assertNull(decoratorMap.get((Short) null));
    }

    @Test
    public void testPutNullValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");

        assertEquals((Byte) "one", decoratorMap.get((Short) 1));

        decoratorMap.put((Short) 1, null);
        assertNull(decoratorMap.get((Short) 1));
    }

    @Test
    public void testExternalizable() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");
        decoratorMap.put((Short) 2, (Byte) "two");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);

        decoratorMap.writeExternal(out);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInput in = new ObjectInputStream(inputStream);

        TShortByteMapDecorator readDecoratorMap = (TShortByteMapDecorator) in.readObject();

        assertEquals(decoratorMap.size(), readDecoratorMap.size());
        assertEquals(decoratorMap.containsKey((Short) 1), readDecoratorMap.containsKey((Short) 1));
        assertEquals(decoratorMap.containsKey((Short) 2), readDecoratorMap.containsKey((Short) 2));
        assertEquals(decoratorMap.get((Short) 1), readDecoratorMap.get((Short) 1));
        assertEquals(decoratorMap.get((Short) 2), readDecoratorMap.get((Short) 2));
    }

    @Test
    public void testCloneable() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMap decoratorMap = new TShortByteMapDecorator(map);

        decoratorMap.put((Short) 1, (Byte) "one");
        decoratorMap.put((Short) 2, (Byte) "two");

        TShortByteMap cloneDecoratorMap = (TShortByteMapDecorator) decoratorMap.clone();

        assertEquals(decoratorMap.size(), cloneDecoratorMap.size());
        assertEquals(decoratorMap.containsKey((Short) 1), cloneDecoratorMap.containsKey((Short) 1));
        assertEquals(decoratorMap.containsKey((Short) 2), cloneDecoratorMap.containsKey((Short) 2));
        assertEquals(decoratorMap.get((Short) 1), cloneDecoratorMap.get((Short) 1));
        assertEquals(decoratorMap.get((Short) 2), cloneDecoratorMap.get((Short) 2));
    }
}