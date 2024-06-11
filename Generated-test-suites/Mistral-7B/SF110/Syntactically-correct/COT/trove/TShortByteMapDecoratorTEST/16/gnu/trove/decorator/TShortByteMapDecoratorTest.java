import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.view.TShortByteMapView;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setup() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 decorator = new TShortByteMapDecorator();
 Assert.assertEquals(0, decorator.size());
 Assert.assertNull(decorator.getMap());

 // Test constructor with TShortByteMap
 decorator = new TShortByteMapDecorator(_map);
 Assert.assertEquals(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
 short key = 1;
 byte value = 2;

 // Test null key
 Assert.assertNull(decorator.put((Short) null, value));
 short wrappedKey = decorator.wrapKey(key);
 byte wrappedValue = decorator.wrapValue(value);

 // Test put with valid key and value
 decorator.put(wrappedKey, wrappedValue);
 Assert.assertEquals(1, decorator.size());
 Assert.assertEquals(wrappedValue, decorator.get(wrappedKey));

 // Test put with existing key
 decorator.put(wrappedKey, (byte) 3);
 Assert.assertEquals(wrappedValue, decorator.get(wrappedKey));
 Assert.assertEquals(3, decorator.get((Short) wrappedKey));

 // Test put with null value
 Assert.assertNull(decorator.put(wrappedKey, null));
 Assert.assertEquals(wrappedValue, decorator.get(wrappedKey));
 }

 @Test
 public void testGet() {
 short key = 1;
 byte value = 2;
 decorator.put(key, value);
 short wrappedKey = decorator.wrapKey(key);
 byte wrappedValue = decorator.wrapValue(value);

 // Test get with valid key
 Assert.assertEquals(wrappedValue, decorator.get(wrappedKey));
 Assert.assertEquals(wrappedValue, decorator.get(key));

 // Test get with null key
 Assert.assertNull(decorator.get(null));

 // Test get with non-existent key
 Assert.assertNull(decorator.get((Short) 0));
 }

 @Test
 public void testClear() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);
 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 short key = 1;
 byte value = 2;
 decorator.put(key, value);
 short wrappedKey = decorator.wrapKey(key);
 byte wrappedValue = decorator.wrapValue(value);

 // Test remove with valid key
 decorator.remove(wrappedKey);
 Assert.assertEquals(0, decorator.size());
 Assert.assertNull(decorator.get(wrappedKey));

 // Test remove with null key
 Assert.assertNull(decorator.remove(null));
 Assert.assertEquals(1, decorator.size());

 // Test remove with non-existent key
 Assert.assertNull(decorator.remove((Short) 0));
 Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testEntrySet() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 // Test entrySet size
 Assert.assertEquals(2, decorator.entrySet().size());

 // Test entrySet contains
 TShortByteMapView view = decorator.asView();
 Iterator<Entry<Short, Byte>> iterator = decorator.entrySet().iterator();
 Entry<Short, Byte> entry = iterator.next();
 Assert.assertEquals(entry.getKey(), view.get(entry.getKey()));
 Assert.assertEquals(entry.getValue(), decorator.get(entry.getKey()));

 // Test entrySet iterator has next
 Assert.assertTrue(iterator.hasNext());

 // Test entrySet iterator next
 entry = iterator.next();
 Assert.assertFalse(iterator.hasNext());

 // Test entrySet remove
 decorator.remove(entry.getKey());
 Assert.assertEquals(1, decorator.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testEntrySetAdd() {
 decorator.entrySet().add(new Entry<Short, Byte>(null, null));
 }

 @Test
 public void testContainsValue() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 // Test containsValue with null value
 Assert.assertFalse(decorator.containsValue(null));

 // Test containsValue with valid value
 Assert.assertTrue(decorator.containsValue((byte) 1));

 // Test containsValue with different value
 Assert.assertFalse(decorator.containsValue((byte) 3));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 // Test containsKey with null key
 Assert.assertFalse(decorator.containsKey(null));

 // Test containsKey with valid key
 Assert.assertTrue(decorator.containsKey((short) 1));

 // Test containsKey with different key
 Assert.assertFalse(decorator.containsKey((short) 3));
 }

 @Test
 public void testSize() {
 decorator.put((short) 1, (byte) 1);
 decorator.put((short) 2, (byte) 2);

 // Test size
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.clear();

 // Test isEmpty
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMap();
 map.put((short) 1, (byte) 1);
 map.put((short) 2, (byte) 2);

 decorator.putAll(map);
 Assert.assertEquals(2, decorator.size());

 short wrappedKey1 = decorator.wrapKey(1);
 short wrappedKey2 = decorator.wrapKey(2);
 byte wrappedValue1 = decorator.wrapValue(1);
 byte wrappedValue2 = decorator.wrapValue(2);

 // Test putAll with valid keys and values
 Assert.assertEquals(wrappedValue1, decorator.get(wrappedKey1));
 Assert.assertEquals(wrappedValue2, decorator.get(wrappedKey2));
 }

 @Test(expected = ClassCastException.class)
 public void testPutAllWithInvalidMapType() {
 TIntObjectMap map = new TIntObjectHashMap();
 map.put((int) 1, "abc");
 decorator.putAll(map);
 }

 @Test(expected = NullPointerException.class)
 public void testPutAllWithNullMap() {
 decorator.putAll(null);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 decorator.writeExternal(new ObjectOutput() {
 @Override public void writeByte(int arg0) {
 throw new UnsupportedOperationException();
 }

 @Override public void writeObject(Object arg0) throws IOException {
 baos.writeObject(arg0);
 }
 });
 decorator.clear();
 decorator.readExternal(new ObjectInput() {
 @Override public int read() throws IOException {
 return bais.read();
 }

 @Override public Object readObject() throws IOException, ClassNotFoundException {
 return bais.readObject();
 }
 });
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 decorator.writeExternal(new ObjectOutput() {
 @Override public void writeByte(int arg0) {
 baos.writeByte(arg0);
 }

 @Override public void writeObject(Object arg0) throws IOException {
 throw new UnsupportedOperationException();
 }
 });
 byte[] bytes = baos.toByteArray();
 ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
 TShortByteMap map = new TShortByteMap();
 map.readExternal(new ObjectInput() {
 @Override public int read() throws IOException {
 return bais.read();
 }

 @Override public Object readObject() throws IOException, ClassNotFoundException {
 return bais.readObject();
 }
 });
 Assert.assertEquals(decorator.size(), map.size());
 }
}