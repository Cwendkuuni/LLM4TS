package org.apache.commons.cli; // adjust the package name if necessary

import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get(null));
        assertNull(decorator.get(1.0));
        assertFalse(decorator.containsKey(null));
        assertFalse(decorator.containsKey(1.0));
    }

    @Test
    public void testPutGetAndRemove() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short)1);
        assertEquals(1, decorator.size());
        assertEquals((short)1, decorator.get(1.0));
        assertFalse(decorator.containsKey(2.0));
        decorator.put(2.0, (short)2);
        assertEquals(2, decorator.size());
        assertEquals((short)1, decorator.get(1.0));
        assertEquals((short)2, decorator.get(2.0));
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertEquals(null, decorator.get(3.0));
        decorator.remove(1.0);
        assertEquals(1, decorator.size());
        assertNull(decorator.get(1.0));
        assertFalse(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(null, (short)1);
        assertEquals(0, decorator.size());
        assertNull(decorator.get(null));
        assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testPutNullValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, null);
        assertEquals(0, decorator.size());
        assertNull(decorator.get(1.0));
        assertFalse(decorator.containsValue(null));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> firstEntry = iterator.next();
        assertEquals(1.0, firstEntry.getKey().doubleValue());
        assertEquals((short)1, firstEntry.getValue().shortValue());

        Entry<Double, Short> secondEntry = iterator.next();
        assertEquals(2.0, secondEntry.getKey().doubleValue());
        assertEquals((short)2, secondEntry.getValue().shortValue());

        iterator.remove();
        assertEquals(1, entrySet.size());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);

        assertTrue(decorator.containsValue((short)1));
        assertTrue(decorator.containsValue((short)2));
        assertFalse(decorator.containsValue((short)3));
    }

    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);
        decorator.clear();

        assertEquals(0, decorator.size());
        assertNull(decorator.get(1.0));
        assertNull(decorator.get(2.0));
        assertFalse(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(2.0));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short)1);
        decorator.put(2.0, (short)2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutput);
        objectOutput.close();

        ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)objectInput.readObject();
        objectInput.close();

        assertEquals(decorator.size(), deserializedDecorator.size());
        assertEquals(decorator.get(1.0), deserializedDecorator.get(1.0));
        assertEquals(decorator.get(2.0), deserializedDecorator.get(2.0));
    }
}