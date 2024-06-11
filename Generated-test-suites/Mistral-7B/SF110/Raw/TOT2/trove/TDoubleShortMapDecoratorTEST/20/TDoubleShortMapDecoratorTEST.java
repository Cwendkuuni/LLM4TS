
package gnu.trove.decorator.test;

import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testDefaultConstructor() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertNotNull(decorator);
        TDoubleShortMap map = decorator.getMap();
        Assert.assertNotNull(map);
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testConstructingWithTDoubleShortMap() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Assert.assertNotNull(decorator);
        Assert.assertSame(map, decorator.getMap());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Short value = decorator.get(1.0);
        Assert.assertEquals((short) 1, value);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Short value = decorator.get(1.0);
        Assert.assertEquals((short) 1, value);

        value = decorator.get(2.0);
        Assert.assertNull(value);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.clear();
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Short removedValue = decorator.remove(1.0);
        Assert.assertEquals((short) 1, removedValue);
        Assert.assertFalse(decorator.containsKey(1.0));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Assert.assertTrue(decorator.containsValue((short) 1));
        Assert.assertTrue(decorator.containsValue((short) 2));
        Assert.assertFalse(decorator.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Assert.assertTrue(decorator.containsKey(1.0));
        Assert.assertTrue(decorator.containsKey(2.0));
        Assert.assertFalse(decorator.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Assert.assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short) 1);
        Assert.assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        decorator.putAll(map);

        Assert.assertEquals(2, decorator.size());
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();

        Assert.assertEquals(2, entrySet.size());

        for (Entry<Double, Short> entry : entrySet) {
            Assert.assertEquals(entry.getKey(), decorator.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), decorator.get(entry.getKey()));
        }
    }

    @Test
    public void testIteration() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

        Entry<Double, Short> entry = null;
        while (iterator.hasNext()) {
            entry = iterator.next();
            Assert.assertEquals(entry.getKey(), decorator.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), decorator.get(entry.getKey()));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        try {
            decorator.addAll(Collections.singletonMap(1.0, (short) 3));
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testExternalizable() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        decorator.writeExternal(output);
        output.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream input = new ObjectInputStream(inputStream);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) input.readObject();
        input.close();

        Assert.assertEquals(decorator.size(), deserializedDecorator.size());
        for (Double key : decorator.keySet()) {
            Short value = decorator.get(key);
            Short deserializedValue = deserializedDecorator.get(key);
            Assert.assertEquals(value, deserializedValue);
        }
    }
}
