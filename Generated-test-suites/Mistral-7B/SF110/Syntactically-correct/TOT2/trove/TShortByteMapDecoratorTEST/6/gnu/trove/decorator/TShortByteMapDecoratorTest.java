import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;
import java.io.*;

public class TShortByteMapDecoratorTest {

    TShortByteMapDecorator map;
    TShortByteMap mapUnderlying;
    Short key;
    Byte value;

    @Before
    public void setup() {
        mapUnderlying = new TShortByteMap();
        map = new TShortByteMapDecorator(mapUnderlying);
    }

    @Test
    public void testEmptyConstructor() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testConstructorWithMap() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        assertEquals(map, decorator.getMap());
    }

    @Test
    public void testGetMap() {
        TShortByteMapDecorator map = new TShortByteMapDecorator();
        assertNotNull(map.getMap());
    }

    @Test
    public void testPut() {
        map.put(key, value);
        assertEquals(1, map.size());
        Byte retrievedValue = map.get(key);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGet() {
        map.put(key, value);
        Byte retrievedValue = map.get(key);
        assertEquals(value, retrievedValue);
        assertEquals(null, map.get(null));
    }

    @Test
    public void testClear() {
        map.put(key, value);
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testRemove() {
        map.put(key, value);
        Byte removedValue = map.remove(key);
        assertEquals(value, removedValue);
        assertEquals(0, map.size());
    }

    @Test
    public void testNullKeysAndValues() {
        map.put(null, null); // should throw NullPointerException
        map.put(key, null); // should store null value
        map.put(null, value); // should store null value
        assertEquals(null, map.get(null)); // should return null
        assertEquals(value, map.get(key)); // should return stored value
    }

    @Test
    public void testNegativeKeys() {
        map.put((short) -1, value); // should store value
        Byte retrievedValue = map.get((short) -1); // should return value
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetWhenMapIsEmpty() {
        assertNull(map.get(null));
    }

    @Test
    public void testRemoveWhenKeyDoesntExist() {
        assertNull(map.remove(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEntriesToSet() {
        map.entrySet().add(new Entry<>(null, null)); // should throw UnsupportedOperationException
    }

    @Test
    public void testContainsValue() {
        map.put(key, value);
        assertTrue(map.containsValue(value));
        map.put(key, (byte) 2); // change the value for the key
        assertFalse(map.containsValue(value));
    }

    @Test
    public void testContainsKey() {
        map.put(key, value);
        assertTrue(map.containsKey(key));
        map.remove(key);
        assertFalse(map.containsKey(key));
    }

    @Test
    public void testSize() {
        map.put(key, value);
        assertEquals(1, map.size());
        map.put(key, value);
        assertEquals(1, map.size());
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testIsEmpty() {
        map.put(key, value);
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Short, Byte> mapToAdd = new HashMap<>();
        mapToAdd.put(key, value);
        map.putAll(mapToAdd);
        assertEquals(mapToAdd.size(), map.size());
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        map.writeExternal(oos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TShortByteMapDecorator mapFromSerialized = (TShortByteMapDecorator) ois.readObject();
        assertEquals(map.size(), mapFromSerialized.size());
    }
}