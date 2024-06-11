import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import gnu.trove.map.hash.TShortByteHashMap;
import gnu.trove.map.hash.TShortByteMapEntry;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteHashMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(new TShortByteHashMap());
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testGetMap() {
 TShortByteMap expectedMap = _map;
 TShortByteMap actualMap = decorator.getMap();
 Assert.assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testPut() {
 short key = 1;
 byte value = 1;
 byte expectedValue = value;
 byte actualValue = decorator.put(key, value);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put((short)1, null);
 }

 @Test
 public void testGet() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 byte expectedValue = value;
 byte actualValue = decorator.get(key);
 Assert.assertEquals(expectedValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetWithNonExistentKey() {
 decorator.get((short)10);
 }

 @Test
 public void testClear() {
 int initialSize = decorator.size();
 decorator.clear();
 int finalSize = decorator.size();
 Assert.assertEquals(0, finalSize);
 Assert.assertEquals(0, initialSize - finalSize);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveWithNonExistentKey() {
 decorator.remove((short)10);
 }

 @Test
 public void testRemove() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 byte removedValue = decorator.remove(key);
 Assert.assertEquals(value, removedValue);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> expectedSet = _map.entrySet();
 Set<Entry<Short, Byte>> actualSet = decorator.entrySet();
 Assert.assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testContainsValue() {
 byte value = 1;
 decorator.put((short)1, value);
 boolean expectedResult = true;
 boolean actualResult = decorator.containsValue(value);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testContainsKey() {
 short key = 1;
 decorator.put(key, (byte)1);
 boolean expectedResult = true;
 boolean actualResult = decorator.containsKey(key);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSize() {
 int initialSize = decorator.size();
 decorator.put((short)1, (byte)1);
 int finalSize = decorator.size();
 Assert.assertEquals(1, finalSize);
 Assert.assertEquals(1, initialSize - finalSize);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(decorator.isEmpty());
 decorator.put((short)1, (byte)1);
 Assert.assertFalse(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap newMap = new TShortByteHashMap();
 newMap.put((short)1, (byte)1);
 newMap.put((short)2, (byte)2);
 decorator.putAll(newMap);
 TShortByteMapView view = decorator.asView();
 TShortByteMapEntry[] entries = view.toArray(new TShortByteMapEntry[0]);
 Assert.assertArrayEquals(new TShortByteMapEntry[] { new TShortByteMapEntry((short)1, (byte)1), new TShortByteMapEntry((short)2, (byte)2) }, entries);
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 decorator.writeExternal(new ObjectOutput() {
 @Override
 public void writeByte(int byte0) {}
 @Override
 public void writeObject(Object obj) throws IOException, ClassNotFoundException {
 byteStream.writeTo(ObjectOutput.writeObject(obj));
 }
 });
 ObjectInput objectInput = new ObjectInput() {
 @Override
 public int readByte() throws IOException {
 return byteInput.readByte();
 }
 @Override
 public Object readObject() throws IOException, ClassNotFoundException {
 return ObjectInput.readObject(byteInput);
 }
 };
 TShortByteMapDecorator newDecorator = new TShortByteMapDecorator();
 newDecorator.readExternal(objectInput);
 Assert.assertEquals(decorator, newDecorator);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator original = decorator;
 TShortByteMapDecorator clone = (TShortByteMapDecorator)original.clone();
 Assert.assertNotSame(original, clone);
 Assert.assertEquals(decorator, clone);
 }
}