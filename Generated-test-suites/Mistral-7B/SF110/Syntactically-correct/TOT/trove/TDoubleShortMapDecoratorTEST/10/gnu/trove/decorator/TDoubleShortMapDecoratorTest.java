import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        assertFalse(decorator.containsKey(null));
        assertFalse(decorator.containsKey(1.0));
        assertFalse(decorator.containsValue((short) 1));
        assertNull(decorator.get(null));
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testPutGetAndRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        assertEquals(1, decorator.size());
        assertEquals((short) 1, decorator.get(1.0));
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());
        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        assertNull(decorator.get(3.0));
        decorator.remove(2.0);
        assertEquals(1, decorator.size());
        assertNull(decorator.get(2.0));
    }

    @Test
    public void testPutNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNull(decorator.put(null, (short) 1));
        assertEquals(0, decorator.size());
    }

    @Test
    public void testPutNullValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, null);
        assertEquals(1, decorator.size());
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testPutDuplicateKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(1.0, (short) 2);
        assertEquals(1, decorator.size());
        assertEquals((short) 2, decorator.get(1.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.clear();
        assertEquals(0, decorator.size());
        assertNull(decorator.get(1.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

        Entry<Double, Short> entry1 = iterator.next();
        assertEquals(1.0, entry1.getKey());
        assertEquals((short) 1, entry1.getValue());

        Entry<Double, Short> entry2 = iterator.next();
        assertEquals(2.0, entry2.getKey());
        assertEquals((short) 2, entry2.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertFalse(decorator.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        decorator.put(1.0, (short) 1);
        assertEquals(1, decorator.size());
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TDoubleShortMap map = new THashDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        decorator.writeExternal(out);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();

        assertEquals(map, deserializedDecorator.getMap());
    }
}