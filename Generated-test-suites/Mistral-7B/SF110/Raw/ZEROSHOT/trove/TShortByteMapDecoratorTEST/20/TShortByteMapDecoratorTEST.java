package test;

import static org.junit.Assert.*;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TShortByteMapDecoratorTest {

    private TShortByteMap baseMap;
    private TShortByteMap decorator;

    @Before
    public void setUp() {
        baseMap = new TShortByteMapImpl();
        decorator = new TShortByteMapDecorator(baseMap);
    }

    @Test
    public void testEmptyDecorator() {
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testDecoratorWithPut() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        assertEquals(2, decorator.size());
        assertFalse(decorator.isEmpty());

        Byte value1 = decorator.get((short) 1);
        Byte value2 = decorator.get((short) 2);

        assertEquals((byte) 1, value1);
        assertEquals((byte) 2, value2);
    }

    @Test(expected = NullPointerException.class)
    public void testDecoratorWithNullKey() {
        decorator.put((Short) null, (byte) 1);
    }

    @Test(expected = NullPointerException.class)
    public void testDecoratorWithNullValue() {
        decorator.put((short) 1, (Short) null);
    }

    @Test
    public void testDecoratorWithClear() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        decorator.clear();

        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testDecoratorWithRemove() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        decorator.remove((short) 1);

        assertEquals(1, decorator.size());
        assertFalse(decorator.containsKey((short) 1));
        assertNotNull(decorator.get((short) 2));
    }

    @Test
    public void testDecoratorWithEntrySet() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        Iterator<Entry<Short, Byte>> iterator = decorator.entrySet().iterator();

        Entry<Short, Byte> entry1 = iterator.next();
        assertEquals((short) 1, entry1.getKey());
        assertEquals((byte) 1, entry1.getValue());

        Entry<Short, Byte> entry2 = iterator.next();
        assertEquals((short) 2, entry2.getKey());
        assertEquals((byte) 2, entry2.getValue());

        iterator.remove();

        assertEquals(1, decorator.size());
        assertFalse(decorator.containsKey((short) 2));
        assertNotNull(decorator.get((short) 1));
    }

    @Test
    public void testDecoratorWithContainsValue() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        assertTrue(decorator.containsValue((byte) 1));
        assertTrue(decorator.containsValue((byte) 2));
    }

    @Test
    public void testDecoratorWithContainsKey() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 2));

        assertFalse(decorator.containsKey((short) 0));
    }

    @Test
    public void testDecoratorWithSize() {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testDecoratorWithPutAll() {
        TShortByteMap baseMap2 = new TShortByteMapImpl();
        baseMap2.put((short) 3, (byte) 3);
        baseMap2.put((short) 4, (byte) 4);

        decorator.putAll(baseMap2);

        assertEquals(4, decorator.size());
        assertTrue(decorator.containsKey((short) 3));
        assertTrue(decorator.containsKey((short) 4));
    }

    @Test
    public void testDecoratorWithExternalizable() throws IOException, ClassNotFoundException {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        decorator.writeExternal(objOut);
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(in);
        TShortByteMapDecorator decoratorFromStream = (TShortByteMapDecorator) objIn.readObject();
        objIn.close();

        assertEquals(decorator.size(), decoratorFromStream.size());
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 2));
        assertEquals(decorator.get((short) 1), decoratorFromStream.get((short) 1));
        assertEquals(decorator.get((short) 2), decoratorFromStream.get((short) 2));
    }

    @Test
    public void testDecoratorWithCloneable() throws CloneNotSupportException {
        decorator.put((short) 1, (byte) 1);
        decorator.put((short) 2, (byte) 2);

        TShortByteMapDecorator cloned = (TShortByteMapDecorator) decorator.clone();

        assertEquals(decorator.size(), cloned.size());
        assertTrue(decorator.containsKey((short) 1));
        assertTrue(decorator.containsKey((short) 2));
        assertEquals(decorator.get((short) 1), cloned.get((short) 1));
        assertEquals(decorator.get((short) 2), cloned.get((short) 2));
    }
}