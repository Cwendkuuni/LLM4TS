import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;
import gnu.trove.map.view.TDoubleShortView;
import gnu.trove.map.view.TShortMapView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testConstructor() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertNotNull(decorator);
        
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decoratorWithMap = new TDoubleShortMapDecorator(map);
        Assert.assertNotNull(decoratorWithMap);
        Assert.assertEquals(map, decoratorWithMap.getMap());
    }

    @Test
    public void testGetMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertNotNull(decorator.getMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(null, (short) 0);
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        TDoubleShortMap map = decorator.getMap();
        TDoubleShortMapEntry entry = map.get(1.0);
        Assert.assertNotNull(entry);
        Assert.assertEquals(1.0, entry.getKey().doubleValue(), 0.0);
        Assert.assertEquals((short) 1, entry.getValue());
    }

    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Short value = decorator.get(null);
        Assert.assertNull(value);
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Double key = 1.0;
        Short value = decorator.get(key);
        Assert.assertEquals(key, decorator.wrapKey(key));
        Assert.assertEquals((short) 1, value);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.clear();
        Assert.assertEquals(0, decorator.size());
    }

    @Test
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Short removedValue = decorator.remove(null);
        Assert.assertNull(removedValue);
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Double key = 1.0;
        Short removedValue = decorator.remove(key);
        Assert.assertEquals(key, decorator.wrapKey(key));
        Assert.assertEquals((short) 1, removedValue);
        Assert.assertEquals(0, decorator.size());
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        TDoubleMap<Double, Short> doubleMapView = decorator.entrySet().asMap();
        Assert.assertEquals(2, doubleMapView.size());
        Assert.assertTrue(doubleMapView.containsKey(1.0));
        Assert.assertEquals((short) 1, doubleMapView.get(1.0));
        Assert.assertTrue(doubleMapView.containsKey(2.0));
        Assert.assertEquals((short) 2, doubleMapView.get(2.0));

        TShortMap<Double> shortMapView = decorator.entrySet().asView(Short.class);
        Assert.assertEquals(2, shortMapView.size());
        Assert.assertTrue(shortMapView.containsKey(1.0));
        Assert.assertEquals((short) 1, shortMapView.value(1.0));
        Assert.assertTrue(shortMapView.containsKey(2.0));
        Assert.assertEquals((short) 2, shortMapView.value(2.0));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Assert.assertTrue(decorator.containsValue((short) 1));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Assert.assertTrue(decorator.containsKey(1.0));
        Assert.assertFalse(decorator.containsKey("1.0"));
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
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.putAll(map);
        TDoubleMap<Double, Short> doubleMapView = decorator.entrySet().asMap();
        Assert.assertEquals(2, doubleMapView.size());
        Assert.assertTrue(doubleMapView.containsKey(1.0));
        Assert.assertEquals((short) 1, doubleMapView.get(1.0));
        Assert.assertTrue(doubleMapView.containsKey(2.0));
        Assert.assertEquals((short) 2, doubleMapView.get(2.0));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(out);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(byteArrayInputStream);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)in.readObject();
        Assert.assertEquals(decorator.getMap(), deserializedDecorator.getMap());
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();
        Assert.assertEquals(decorator.getMap(), clonedDecorator.getMap());
    }
}