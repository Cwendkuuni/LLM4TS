import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.view.TDoubleMap;
import gnu.trove.map.view.TShortMap;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @After
 public void tearDown() {
 _map.clear();
 decorator = null;
 }

 @Test
 public void testConstructor() {
 assertNotNull(decorator);
 assertEquals(decorator.getClass(), TDoubleShortMapDecorator.class);
 }

 @Test
 public void testGetMap() {
 TDoubleMap map = decorator.getMap();
 assertNotNull(map);
 assertEquals(map.getClass(), TDoubleShortMap.class);
 }

 @Test
 public void testPut() {
 Double key = 1.0;
 Short value = (short)1;
 Short originalValue = decorator.put(key, value);
 assertEquals(originalValue, null);
 Short retrievedValue = decorator.get(key);
 assertEquals(retrievedValue, value);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 Double key = null;
 Short value = (short)1;
 decorator.put(key, value);
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 Double key = 1.0;
 Short value = null;
 decorator.put(key, value);
 fail("Expected NullPointerException");
 }

 @Test
 public void testGetNullKey() {
 assertNull(decorator.get(null));
 }

 @Test
 public void testClear() {
 assertFalse(decorator.isEmpty());
 decorator.clear();
 assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 Double key = 1.0;
 Short value = (short)1;
 decorator.put(key, value);
 Short removedValue = decorator.remove(key);
 assertEquals(removedValue, value);
 assertNull(decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 Double key = null;
 decorator.remove(key);
 fail("Expected NullPointerException");
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(entrySet.getClass(), AbstractSet.class);
 Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
 assertEquals(iterator.getClass(), Iterator.class);
 }

 @Test
 public void testContainsValue() {
 Short value = (short)1;
 assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 Double key = 1.0;
 assertTrue(decorator.containsKey(key));
 }

 @Test
 public void testSize() {
 int size = 3;
 for (int i = 0; i < size; i++) {
 decorator.put((double)i, (short)i);
 }
 assertEquals(decorator.size(), size);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(decorator.isEmpty());
 decorator.put(1.0, (short)1);
 assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap mapToAdd = new TDoubleShortMap();
 mapToAdd.put(1.0, (short)1);
 mapToAdd.put(2.0, (short)2);
 decorator.putAll(mapToAdd);
 assertEquals(decorator.size(), 2);
 }

 @Test
 public void testExternalizable() {
 ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteArrayOS);
 decorator.writeExternal(out);
 out.close();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteArrayOS.toByteArray()));
 TDoubleShortMapDecorator decoratorFromSerialized = (TDoubleShortMapDecorator)in.readObject();
 in.close();
 assertEquals(decoratorFromSerialized.getMap().size(), decorator.getMap().size());
 }

 @Test
 public void testCloneable() {
 try {
 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();
 assertEquals(clonedDecorator.getMap().size(), decorator.getMap().size());
 } catch (CloneNotSupportedException e) {
 fail("Expected Cloneable interface implementation");
 }
 }
}