package tests;

import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

public class TShortByteMapDecoratorTest {

    @Test
    public void testConstructor() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Assert.assertEquals(0, decorator.size());
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testConstructorWithMap() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);

        Assert.assertEquals(map.size(), decorator.size());
        Assert.assertTrue(decorator.containsAll(map.entrySet()));
    }

    @Test
    public void testGetMap() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = decorator.getMap();
        Assert.assertSame(map, decorator._map);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) null, (Byte) 1);
    }

    @Test(expected = NullPointerException.class)
    public void testPutNullValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) null);
    }

    @Test
    public void testPut() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        Assert.assertEquals(1, decorator.size());
        Assert.assertEquals((Byte) 1, decorator.get((Short) 1));
    }

    @Test
    public void testPutExistingKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 1, (Byte) 2);
        Assert.assertEquals((Byte) 2, decorator.get((Short) 1));
    }

    @Test
    public void testGetNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Assert.assertNull(decorator.get((Object) null));
    }

    @Test
    public void testGetKeyNotShort() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        Assert.assertNull(decorator.get((Object) new Object()));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        Assert.assertEquals(1, decorator.size());
        decorator.clear();
        Assert.assertEquals(0, decorator.size());
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemoveNullKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        Assert.assertEquals((Byte) 1, decorator.remove((Object) null));
        Assert.assertEquals(0, decorator.size());
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemoveExistingKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        Assert.assertEquals((Byte) 1, decorator.remove((Short) 1));
        Assert.assertEquals(0, decorator.size());
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemoveNonExistingKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Assert.assertNull(decorator.remove((Object) null));
        Assert.assertEquals(0, decorator.size());
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testEntrySetSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);
        Assert.assertEquals(decorator.size(), decorator.entrySet().size());
    }

    @Test
    public void testEntrySetIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Assert.assertEquals(decorator.isEmpty(), decorator.entrySet().isEmpty());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 1);
        Assert.assertTrue(decorator.containsValue((Object) Byte.valueOf((byte) 1)));
        Assert.assertFalse(decorator.containsValue((Object) Byte.valueOf((byte) 2)));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        Assert.assertTrue(decorator.containsKey((Short) 1));
        Assert.assertFalse(decorator.containsKey((Short) 2));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);
        Assert.assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        Assert.assertTrue(decorator.isEmpty());
        decorator.put((Short) 1, (Byte) 1);
        Assert.assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        decorator.putAll(map);

        Assert.assertEquals(map.size(), decorator.size());
        Assert.assertTrue(decorator.containsAll(map.entrySet()));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (Byte) 2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(decorator);

        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator) ois.readObject();

        Assert.assertEquals(decorator.size(), deserializedDecorator.size());
        Assert.assertTrue(decorator.containsAll(deserializedDecorator.entrySet()));
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNoEntry() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        TShortByteIterator it = decorator.entrySet().iterator();
        it.next();
        it.remove();
        it.next();
        it.next(); // This should throw NoSuchElementException
    }

    @Test(expected = ClassCastException.class)
    public void testIteratorInvalidEntryType() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((Short) 1, (Byte) 1);
        decorator.put((Short) 2, (String) "hello");

        TShortByteIterator it = decorator.entrySet().iterator();
        it.next();
        Assert.assertNotNull(it.next());

        // The next call should throw ClassCastException
        Assert.assertNull(it.next());
    }
}