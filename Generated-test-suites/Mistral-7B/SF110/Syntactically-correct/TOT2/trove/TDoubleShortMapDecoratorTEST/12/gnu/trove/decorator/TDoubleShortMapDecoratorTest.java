import static org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {
    
    @Test
    public void testEmptyConstructor() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.getMap());
        assertEquals(0, map.size());
    }
    
    @Test
    public void testConstructorWithExistingMap() {
        TDoubleShortMap originalMap = new TDoubleShortMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(originalMap);
        assertEquals(originalMap, decorator.getMap());
        assertEquals(originalMap.size(), decorator.size());
    }
    
    @Test
    public void testPutWithValidKeyAndValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Short putValue = Short.valueOf((short)5);
        Double putKey = Double.valueOf(1.0);
        Short returnedValue = map.put(putKey, putValue);
        assertEquals(putValue, returnedValue);
        assertEquals(1, map.size());
    }
    
    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(null, (short)5);
    }
    
    @Test(expected = NullPointerException.class)
    public void testPutWithNullValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(Double.valueOf(1.0), null);
    }
    
    @Test
    public void testPutWithExistingKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double existingKey = Double.valueOf(1.0);
        Short existingValue = Short.valueOf((short)1);
        map.put(existingKey, existingValue);
        Short newValue = Short.valueOf((short)2);
        Short returnedValue = map.put(existingKey, newValue);
        assertEquals(newValue, returnedValue);
        assertEquals(1, map.size());
    }
    
    @Test
    public void testGetWithExistingKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double existingKey = Double.valueOf(1.0);
        Short existingValue = Short.valueOf((short)1);
        map.put(existingKey, existingValue);
        Short returnedValue = map.get(existingKey);
        assertEquals(existingValue, returnedValue);
    }
    
    @Test
    public void testGetWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.get(null));
    }
    
    @Test
    public void testGetWithNonExistingKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.get(Double.valueOf(1.0)));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(Double.valueOf(1.0), Short.valueOf((short)1));
        map.clear();
        assertEquals(0, map.size());
        assertNull(map.getMap());
    }
    
    @Test
    public void testRemoveWithExistingKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        Double existingKey = Double.valueOf(1.0);
        Short existingValue = Short.valueOf((short)1);
        map.put(existingKey, existingValue);
        Short removedValue = map.remove(existingKey);
        assertEquals(existingValue, removedValue);
        assertEquals(0, map.size());
    }
    
    @Test
    public void testRemoveWithNullKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.remove(null));
    }
    
    @Test
    public void testRemoveWithNonExistingKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.remove(Double.valueOf(1.0)));
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(Double.valueOf(1.0), Short.valueOf((short)1));
        map.put(Double.valueOf(2.0), Short.valueOf((short)2));

        Set<Entry<Double, Short>> entrySet = map.entrySet();

        assertEquals(2, entrySet.size());

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();

        Entry<Double, Short> entry1 = iterator.next();
        assertEquals(Double.valueOf(1.0), entry1.getKey());
        assertEquals(Short.valueOf((short)1), entry1.getValue());

        Entry<Double, Short> entry2 = iterator.next();
        assertEquals(Double.valueOf(2.0), entry2.getKey());
        assertEquals(Short.valueOf((short)2), entry2.getValue());
    }
}