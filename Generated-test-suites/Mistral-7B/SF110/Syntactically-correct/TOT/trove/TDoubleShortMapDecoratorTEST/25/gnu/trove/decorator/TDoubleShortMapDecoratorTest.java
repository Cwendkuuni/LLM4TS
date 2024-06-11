import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMapView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyMap() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Assert.assertEquals(Collections.emptyMap(), decorator);
    }

    @Test
    public void testMap() {
        TDoubleShortMap map = new TDoubleShortMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);

        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Assert.assertEquals(map, decorator.getMap());

        // Test put
        decorator.put(1.0, (short) 3);
        TDoubleShortMapView view = decorator.entrySet().iterator().next().getValue();
        Assert.assertEquals((short) 3, view.getValue());

        // Test get
        Assert.assertEquals((short) 1, decorator.get(1.0));
        Assert.assertEquals((short) null, decorator.get(3.0));

        // Test clear
        decorator.clear();
        Assert.assertEquals(Collections.emptyMap(), decorator);

        // Test remove
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Assert.assertEquals((short) 2, decorator.remove(2.0));
        Assert.assertEquals(Collections.singletonMap(1.0, (short) 1), decorator);

        // Test entrySet
        List<Entry<Double, Short>> entries = new ArrayList<>(decorator.entrySet());
        Assert.assertEquals(1, entries.size());

        // Test containsValue
        Assert.assertTrue(decorator.containsValue((short) 1));
        Assert.assertFalse(decorator.containsValue((short) 2));

        // Test containsKey
        Assert.assertTrue(decorator.containsKey(1.0));
        Assert.assertFalse(decorator.containsKey(3.0));

        // Test size
        Assert.assertEquals(1, decorator.size());

        // Test isEmpty
        Assert.assertFalse(decorator.isEmpty());

        // Test putAll
        TDoubleShortMap mapToAdd = new TDoubleShortMap();
        mapToAdd.put(3.0, (short) 3);
        mapToAdd.put(4.0, (short) 4);

        decorator.putAll(mapToAdd);
        Assert.assertEquals(3, decorator.size());

        // Test externalization
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(outputStream);
        decorator.writeExternal(out);
        out.close();

        ObjectInput in = new ObjectInput(new ByteArrayInputStream(outputStream.toByteArray()));
        TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator) in.readObject();
        in.close();

        Assert.assertEquals(decorator, deserializedDecorator);
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(null, (short) 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullValuePut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, null);
    }

    @Test(expected = ClassCastException.class)
    public void testNonDoubleKeyPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put("1", (short) 1);
    }

    @Test(expected = ClassCastException.class)
    public void testNonShortValuePut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, new Integer(1));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Assert.assertNull(decorator.get(null));
    }

    @Test(expected = ClassCastException.class)
    public void testNonDoubleKeyGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Assert.assertNull(decorator.get("1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Assert.assertNull(decorator.remove(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonDoubleKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        Assert.assertNull(decorator.remove("1"));
    }
}