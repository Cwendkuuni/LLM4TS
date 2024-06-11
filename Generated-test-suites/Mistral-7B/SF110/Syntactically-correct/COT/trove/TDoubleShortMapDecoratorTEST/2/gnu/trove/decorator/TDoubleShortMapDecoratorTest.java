import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyConstructor() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Assert.assertEquals(0, mapDecorator.size());
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testConstructorWithMap() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator(map);
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        Assert.assertEquals(map.size(), mapDecorator.size());
        Assert.assertFalse(mapDecorator.isEmpty());

        TDoubleMap<Double, Short> doubleMapView = mapDecorator.entrySet().asMap();
        Assert.assertEquals(map, doubleMapView);

        TShortMap<Double> shortMapView = mapDecorator.asShortMap();
        Assert.assertEquals(map, shortMapView);
    }

    @Test
    public void testGetMap() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        TDoubleShortMap map = mapDecorator.getMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        Assert.assertEquals(map, mapDecorator.getMap());
    }

    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        Assert.assertEquals((short) 1, mapDecorator.get(1.0));
        Assert.assertEquals((short) 2, mapDecorator.get(2.0));
        Assert.assertNull(mapDecorator.get(3.0));
    }

    @Test
    public void testGetNullKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Assert.assertNull(mapDecorator.get(null));
    }

    @Test
    public void testGetWrongTypeKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Assert.assertNull(mapDecorator.get("wrong_type_key"));
    }

    @Test(expected = ClassCastException.class)
    public void testGetWrongTypeValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, "wrong_type_value");
        mapDecorator.get(1.0);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        mapDecorator.clear();

        Assert.assertEquals(0, mapDecorator.size());
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        Assert.assertEquals((short) 1, mapDecorator.remove(1.0));
        Assert.assertEquals((short) 2, mapDecorator.get(2.0));
        Assert.assertNull(mapDecorator.get(1.0));
        Assert.assertEquals(1, mapDecorator.size());

        mapDecorator.remove(2.0);
        Assert.assertNull(mapDecorator.get(2.0));
        Assert.assertEquals(0, mapDecorator.size());
        Assert.assertTrue(mapDecorator.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.remove(null);
    }

    @Test(expected = ClassCastException.class)
    public void testRemoveWrongTypeKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put("wrong_type_key", (short) 1);
        mapDecorator.remove("wrong_type_key");
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = mapDecorator.entrySet();

        Assert.assertEquals(2, entrySet.size());
        Assert.assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 1)));
        Assert.assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2.0, (short) 2)));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        Assert.assertTrue(mapDecorator.containsValue((short) 1));
        Assert.assertTrue(mapDecorator.containsValue((short) 2));
        Assert.assertFalse(mapDecorator.containsValue("wrong_type_value"));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        Assert.assertTrue(mapDecorator.containsKey(1.0));
        Assert.assertTrue(mapDecorator.containsKey(2.0));
        Assert.assertFalse(mapDecorator.containsKey("wrong_type_key"));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        Assert.assertEquals(2, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        Assert.assertTrue(mapDecorator.isEmpty());

        mapDecorator.put(1.0, (short) 1);

        Assert.assertFalse(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();

        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        mapDecorator.putAll(map);

        Assert.assertEquals(map.size(), mapDecorator.size());
        Assert.assertFalse(mapDecorator.isEmpty());

        TDoubleMap<Double, Short> doubleMapView = mapDecorator.entrySet().asMap();
        Assert.assertEquals(map, doubleMapView);

        TShortMap<Double> shortMapView = mapDecorator.asShortMap();
        Assert.assertEquals(map, shortMapView);
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Assert.assertEquals(1.0, mapDecorator.wrapKey(1.0));
    }

    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Assert.assertEquals(1.0, mapDecorator.unwrapKey(1.0));
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Assert.assertEquals((short) 1, mapDecorator.wrapValue((short) 1));
    }

    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Assert.assertEquals((short) 1, mapDecorator.unwrapValue((short) 1));
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        mapDecorator.writeExternal(objectOutputStream);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        TDoubleShortMapDecorator mapDecoratorFromStream = (TDoubleShortMapDecorator) objectInputStream.readObject();
        objectInputStream.close();

        Assert.assertEquals(mapDecorator.size(), mapDecoratorFromStream.size());
        Assert.assertFalse(mapDecorator.isEmpty());

        TDoubleMap<Double, Short> doubleMapView = mapDecorator.entrySet().asMap();
        Assert.assertEquals(mapDecorator, doubleMapView);

        TShortMap<Double> shortMapView = mapDecorator.asShortMap();
        Assert.assertEquals(mapDecorator, shortMapView);
    }

    @Test
    public void testCloneable() throws CloneNotSupportedException {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);

        TDoubleShortMapDecorator clone = (TDoubleShortMapDecorator) mapDecorator.clone();

        Assert.assertEquals(mapDecorator.size(), clone.size());
        Assert.assertFalse(mapDecorator.isEmpty());

        TDoubleMap<Double, Short> doubleMapView = mapDecorator.entrySet().asMap();
        Assert.assertEquals(mapDecorator, doubleMapView);

        TShortMap<Double> shortMapView = mapDecorator.asShortMap();
        Assert.assertEquals(mapDecorator, shortMapView);

        Assert.assertNotSame(mapDecorator, clone);
        Assert.assertEquals(mapDecorator, clone);
    }
}