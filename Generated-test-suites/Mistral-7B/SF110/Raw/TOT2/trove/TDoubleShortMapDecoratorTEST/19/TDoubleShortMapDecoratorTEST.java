import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.io.*;
import gnu.trove.decorator.TDoubleShortMapDecorator;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testTDoubleShortMapDecoratorConstructor() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertNotNull(decorator);
    }

    @Test
    public void testTDoubleShortMapDecoratorWithMapArgument() {
        TDoubleShortMap map = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        Assert.assertSame(map, decorator.getMap());
    }

    @Test
    public void testPutWithNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertNull(decorator.put((Double) null, (Short) 1));
    }

    @Test
    public void testPutWithNullValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertNull(decorator.put((Double) 1.0, (Short) null));
    }

    @Test
    public void testPutWithDuplicateKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put((Double) 1.0, (Short) 1);
        Assert.assertEquals((Short) 1, decorator.put((Double) 1.0, (Short) 2));
    }

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put((Double) 1.0, (Short) 1);
        decorator.put((Double) 2.0, (Short) 2);
        Assert.assertEquals(2, decorator.size());
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put((Double) 1.0, (Short) 1);
        decorator.put((Double) 2.0, (Short) 2);
        decorator.clear();
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put((Double) 1.0, (Short) 1);
        decorator.put((Double) 2.0, (Short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        Assert.assertEquals(2, entrySet.size());

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        Entry<Double, Short> entry = iterator.next();
        Assert.assertEquals((Double) 1.0, entry.getKey());
        Assert.assertEquals((Short) 1, entry.getValue());

        entry = iterator.next();
        Assert.assertEquals((Double) 2.0, entry.getKey());
        Assert.assertEquals((Short) 2, entry.getValue());
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put((Double) 1.0, (Short) 1);
        decorator.put((Double) 2.0, (Short) 2);

        Assert.assertTrue(decorator.containsValue((Short) 1));
        Assert.assertTrue(decorator.containsValue((Short) 2));
        Assert.assertFalse(decorator.containsValue((Short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put((Double) 1.0, (Short) 1);
        decorator.put((Double) 2.0, (Short) 2);

        Assert.assertTrue(decorator.containsKey((Double) 1.0));
        Assert.assertTrue(decorator.containsKey((Double) 2.0));
        Assert.assertFalse(decorator.containsKey((Double) 3.0));
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put((Double) 1.0, (Short) 1);

        Assert.assertEquals((Short) 1, decorator.get((Double) 1.0));
        Assert.assertNull(decorator.get((Double) 2.0));
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put((Double) 1.0, (Short) 1);
        map.put((Double) 2.0, (Short) 2);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.putAll(map);

        Assert.assertEquals(2, decorator.size());
        Assert.assertEquals((Double) 1.0, decorator.keySet().iterator().next());
        Assert.assertEquals((Double) 2.0, decorator.keySet().iterator().next());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put((Double) 1.0, (Short) 1);
        decorator.put((Double) 2.0, (Short) 2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        out.writeObject(decorator);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInput in = new ObjectInputStream(bais);
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) in.readObject();
        in.close();

        Assert.assertEquals(2, deserializedDecorator.size());
        Assert.assertEquals((Double) 1.0, deserializedDecorator.keySet().iterator().next());
        Assert.assertEquals((Double) 2.0, deserializedDecorator.keySet().iterator().next());
    }
}