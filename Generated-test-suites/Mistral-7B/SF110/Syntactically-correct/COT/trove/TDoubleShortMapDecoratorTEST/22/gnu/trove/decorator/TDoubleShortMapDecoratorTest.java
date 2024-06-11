import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;

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
 // Test empty constructor
 TDoubleShortMapDecorator emptyDecorator = new TDoubleShortMapDecorator();
 }

 @Test
 public void testConstructorWithMap() {
 // Test constructor with existing map
 TDoubleShortMap existingMap = new TDoubleShortMap();
 TDoubleShortMapDecorator decoratorWithMap = new TDoubleShortMapDecorator(existingMap);
 }

 @Test
 public void testGetMap() {
 // Test getting the underlying map
 TDoubleShortMap map = decorator.getMap();
 Assert.assertSame(map, _map);
 }

 @Test
 public void testPut() {
 // Test putting a key-value pair
 decorator.put(1.0, (short) 1);
 double key = 1.0;
 Short expectedValue = (short) 1;
 Short actualValue = decorator.get(key);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short) 1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGetNullKey() {
 Short expectedValue = decorator.getNoEntryValue();
 Short actualValue = decorator.get(null);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testClear() {
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 // Test removing a key-value pair
 decorator.put(1.0, (short) 1);
 decorator.remove(1.0);
 Short expectedValue = decorator.getNoEntryValue();
 Short actualValue = decorator.get(1.0);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short) 1);
 boolean containsValue = decorator.containsValue((short) 1);
 Assert.assertTrue(containsValue);
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short) 1);
 boolean containsKey = decorator.containsKey(1.0);
 Assert.assertTrue(containsKey);
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short) 1);
 int size = decorator.size();
 Assert.assertEquals(1, size);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap mapToAdd = new TDoubleShortMap();
 mapToAdd.put(1.0, (short) 1);
 mapToAdd.put(2.0, (short) 2);
 decorator.putAll(mapToAdd);
 Short value1 = decorator.get(1.0);
 Short value2 = decorator.get(2.0);
 Assert.assertEquals((short) 1, value1);
 Assert.assertEquals((short) 2, value2);
 }

 @Test
 public void testWrapKey() {
 // Test that wrapKey returns the input as is since it is a double
 double key = 1.0;
 Double wrappedKey = decorator.wrapKey(key);
 Assert.assertEquals(key, wrappedKey);
 }

 @Test
 public void testUnwrapKey() {
 double key = decorator.unwrapKey(1.0);
 Assert.assertEquals(1.0, key, 0.0);
 }

 @Test
 public void testWrapValue() {
 // Test that wrapValue returns the input as is since it is a short
 short value = (short) 1;
 Short wrappedValue = decorator.wrapValue(value);
 Assert.assertEquals(value, wrappedValue);
 }

 @Test
 public void testUnwrapValue() {
 short value = decorator.unwrapValue((short) 1);
 Assert.assertEquals(1, value);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objectStream);
 byte[] bytes = byteStream.toByteArray();
 ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TDoubleShortMapDecorator deserializedDecorator = (TDoubleShortMapDecorator)input.readObject();
 TDoubleShortMap deserializedMap = deserializedDecorator.getMap();
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objectStream);
 byte[] bytes = byteStream.toByteArray();
 }
}