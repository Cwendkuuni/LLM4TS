package org.apache.commons.cli; // Assuming you want to test TDoubleShortMapDecorator in the same package as Util test case

import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import org.junit.Assert;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertEquals(0, decorator.size());
        Assert.assertNull(decorator.get(null));
        Assert.assertNull(decorator.get(Double.valueOf(0.0)));
    }

    @Test
    public void testPutMethod() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        double key1 = 1.0;
        short value1 = 1;
        decorator.put(Double.valueOf(key1), value1);

        double key2 = 2.0;
        short value2 = 2;
        decorator.put(Double.valueOf(key2), value2);

        Assert.assertEquals(2, decorator.size());

        Assert.assertEquals(value1, decorator.get(Double.valueOf(key1)));
        Assert.assertEquals(value2, decorator.get(Double.valueOf(key2)));
        Assert.assertNull(decorator.get(null));
        Assert.assertNull(decorator.get(Double.valueOf(3.0)));
    }

    @Test
    public void testClearMethod() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        double key1 = 1.0;
        short value1 = 1;
        decorator.put(Double.valueOf(key1), value1);

        double key2 = 2.0;
        short value2 = 2;
        decorator.put(Double.valueOf(key2), value2);

        decorator.clear();

        Assert.assertEquals(0, decorator.size());
        Assert.assertNull(decorator.get(null));
        Assert.assertNull(decorator.get(Double.valueOf(key1)));
        Assert.assertNull(decorator.get(Double.valueOf(key2)));
    }

    @Test
    public void testRemoveMethod() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        double key1 = 1.0;
        short value1 = 1;
        decorator.put(Double.valueOf(key1), value1);

        double key2 = 2.0;
        short value2 = 2;
        decorator.put(Double.valueOf(key2), value2);

        Assert.assertEquals(2, decorator.size());

        short removedValue = decorator.remove(Double.valueOf(key1));
        Assert.assertEquals(value1, removedValue);
        Assert.assertEquals(1, decorator.size());

        Assert.assertEquals(value2, decorator.get(Double.valueOf(key2)));
        Assert.assertNull(decorator.get(Double.valueOf(key1)));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        double key1 = 1.0;
        short value1 = 1;
        decorator.put(Double.valueOf(key1), value1);

        double key2 = 2.0;
        short value2 = 2;
        decorator.put(Double.valueOf(key2), value2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();

        int i = 0;
        for (Entry<Double, Short> entry : entrySet) {
            if (entry.getKey().equals(Double.valueOf(key1))) {
                Assert.assertEquals(value1, entry.getValue());
                i++;
            }

            if (entry.getKey().equals(Double.valueOf(key2))) {
                Assert.assertEquals(value2, entry.getValue());
                i++;
            }
        }

        Assert.assertEquals(2, i);
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        double key1 = 1.0;
        decorator.put(Double.valueOf(key1), (short) 1);

        Assert.assertTrue(decorator.containsKey(Double.valueOf(key1)));
        Assert.assertFalse(decorator.containsKey(Double.valueOf(2.0)));
        Assert.assertFalse(decorator.containsKey(null));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        double key1 = 1.0;
        short value1 = 1;
        decorator.put(Double.valueOf(key1), value1);

        Assert.assertTrue(decorator.containsValue((short) 1));
        Assert.assertFalse(decorator.containsValue((short) 2));
    }
}