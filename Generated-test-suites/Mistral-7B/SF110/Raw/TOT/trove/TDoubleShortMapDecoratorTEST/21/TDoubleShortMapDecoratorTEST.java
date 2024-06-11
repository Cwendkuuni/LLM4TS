import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testEmptyDecorator() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals(0, mapDecorator.size());
        assertFalse(mapDecorator.isEmpty());
        assertNull(mapDecorator.get(null));
        assertNull(mapDecorator.get(1.0));
        assertNull(mapDecorator.put(1.0, (short) 1));
        assertFalse(mapDecorator.containsKey(1.0));
        assertNull(mapDecorator.remove(1.0));
        assertFalse(mapDecorator.containsValue((short) 1));
        assertSame(mapDecorator, mapDecorator.clone());
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testDecoratorWithMap() {
        TDoubleShortMap map = new TDoubleDoubleHashMap();
        map.put(1.0, 1.0);
        map.put(2.0, 2.0);
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator(map);

        assertEquals(map.size(), mapDecorator.size());
        assertFalse(mapDecorator.isEmpty());
        assertEquals((short) 1, mapDecorator.get(1.0));
        assertNull(mapDecorator.get(2.1));
        assertSame((short) 1, mapDecorator.put(1.0, (short) 2));
        assertEquals((short) 2, mapDecorator.get(1.0));
        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(2.1));
        assertEquals((short) 1, mapDecorator.remove(1.0));
        assertNull(mapDecorator.get(1.0));
        assertFalse(mapDecorator.containsValue((short) 1));
        assertSame(mapDecorator, mapDecorator.clone());
        mapDecorator.clear();
        assertEquals(0, mapDecorator.size());
        assertTrue(mapDecorator.isEmpty());

        TDoubleMap<Double, Short> doubleView = mapDecorator.entrySet().iterator().next().getValue();
        TShortMap<Double, Short> shortView = mapDecorator.entrySet().iterator().next().getKey();

        for (TDoubleShortIterator iterator = map.iterator(); iterator.hasNext(); ) {
            double key = iterator.key();
            short value = iterator.value();
            assertEquals(key, shortView.get(key));
            assertEquals(value, doubleView.get(key));
        }
    }

    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMap map = new TDoubleDoubleHashMap();
        map.put(1.0, 1.0);
        map.put(2.0, 2.0);
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator(map);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        mapDecorator.writeExternal(objectOutput);
        objectOutput.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
        TDoubleShortMapDecorator mapDecoratorFromStream = (TDoubleShortMapDecorator)objectInput.readObject();
        objectInput.close();

        assertEquals(mapDecorator.size(), mapDecoratorFromStream.size());
        assertFalse(mapDecoratorFromStream.isEmpty());
        assertEquals(mapDecorator.get(1.0), mapDecoratorFromStream.get(1.0));
        assertEquals(mapDecorator.size(), mapDecoratorFromStream.size());
    }
}