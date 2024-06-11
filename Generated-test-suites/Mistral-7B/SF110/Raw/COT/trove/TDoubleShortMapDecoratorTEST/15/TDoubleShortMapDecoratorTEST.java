import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.util.HashMap;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(new TDoubleShortMap());
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
 TDoubleShortMapMap = decorator.getMap();
 Assert.assertNotNull(map);
 Assert.assertEquals(decorator, map.decorator());
 }

 @Test
 public void testPut() {
 Double key = 1.0;
 Short value = 1;
 Short result = decorator.put(key, value);
 Assert.assertNotNull(result);
 Assert.assertEquals(value, result);
 Double wrappedKey = decorator.wrapKey(key);
 Assert.assertEquals(key, wrappedKey);
 Short wrappedValue = decorator.wrapValue(value);
 Assert.assertEquals(value, wrappedValue);
 }

 @Test
 public void testPutWithNullKey() {
 decorator.put(null, (short) 1);
 TDoubleShortMapMap = decorator.getMap();
 Assert.assertFalse(map.containsKey(Double.NaN));
 }

 @Test
 public void testPutWithNullValue() {
 decorator.put(1.0, null);
 TDoubleShortMapMap = decorator.getMap();
 Assert.assertFalse(map.containsValue((short) 0));
 }

 @Test
 public void testGet() {
 decorator.put(1.0, (short) 1);
 Short result = decorator.get(1.0);
 Assert.assertNotNull(result);
 Assert.assertEquals((short) 1, result);
 }

 @Test
 public void testGetWithNullKey() {
 Short result = decorator.get(null);
 Assert.assertNull(result);
 }

 @Test
 public void testGetWithWrongKeyType() {
 Integer wrongKey = 1;
 Short result = decorator.get(wrongKey);
 Assert.assertNull(result);
 }

 @Test
 public void testClear() {
 decorator.put(1.0, (short) 1);
 decorator.clear();
 TDoubleShortMapMap = decorator.getMap();
 Assert.assertFalse(map.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short) 1);
 Short result = decorator.remove(1.0);
 Assert.assertNotNull(result);
 Assert.assertEquals((short) 1, result);
 }

 @Test
 public void testRemoveWithNullKey() {
 Short result = decorator.remove(null);
 Assert.assertNull(result);
 }

 @Test
 public void testRemoveWithWrongKeyType() {
 Integer wrongKey = 1;
 Short result = decorator.remove(wrongKey);
 Assert.assertNull(result);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Assert.assertNotNull(entrySet);
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short) 1);
 Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 TDoubleShortMapMap = new TDoubleShortMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 Map<Double, Short> mapToAdd = new HashMap<>();
 mapToAdd.put(1.0, (short) 1);
 mapToAdd.put(2.0, (short) 2);
 decorator.putAll(mapToAdd);
 TDoubleShortMapMap = decorator.getMap();
 Assert.assertEquals(2, map.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 entrySet.add(new Entry<Double, Short>(null, null));
 }

 @Test
 public void testRemoveEntrySet() {
 decorator.put(1.0, (short) 1);
 decorator.put(2.0, (short) 2);
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Entry<Double, Short> entryToRemove = entrySet.iterator().next();
 entrySet.remove(entryToRemove);
 TDoubleShortMapMap = decorator.getMap();
 Assert.assertEquals(1, map.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 Assert.assertTrue(decorator.containsValue((short) 1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 Assert.assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testContainsKeyWithNullKey() {
 decorator.put(1.0, (short) 1);
 Assert.assertFalse(decorator.containsKey(null));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
 ObjectInput in = new ObjectInputStream(byteIn);
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(in);
 TDoubleShortMap readMap = readDecorator.getMap();
 Assert.assertEquals(decorator.size(), readMap.size());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 decorator.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 }
}