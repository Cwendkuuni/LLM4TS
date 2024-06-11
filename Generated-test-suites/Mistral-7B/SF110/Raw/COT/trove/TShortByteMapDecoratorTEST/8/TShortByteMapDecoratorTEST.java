import org.junit.Assert;
import org.junit.Test;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.view.TShortByteMapViews;
import gnu.trove.map.view.TShortByteOpenMapView;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMap();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TShortByteMapDecorator decorator = new TShortByteMapDecorator(_map);
 Assert.assertSame(_map, decorator.getMap());
 }

 @Test
 public void testPut() {
 final Byte value = Byte.valueOf((byte)1);
 decorator.put((short)1, value);
 byte actualValue = decorator.get((short)1);
 Assert.assertEquals(value, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, Byte.valueOf((byte)1));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put((short)1, null);
 }

 @Test
 public void testGet() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 byte actualValue = decorator.get((short)1);
 Assert.assertEquals(Byte.valueOf((byte)1), actualValue);
 }

 @Test
 public void testGetWithNullKey() {
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGetWithWrongKeyType() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 Assert.assertNull(decorator.get("wrongKeyType"));
 }

 @Test
 public void testClear() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 decorator.clear();
 boolean isEmpty = decorator.isEmpty();
 Assert.assertTrue(isEmpty);
 }

 @Test
 public void testRemove() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 byte actualValue = decorator.remove((short)1);
 Assert.assertEquals(Byte.valueOf((byte)1), actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithWrongKeyType() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.remove("wrongKeyType");
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 boolean containsEntry = entrySet.contains(new Entry<Short, Byte>(Short.valueOf((short)1), Byte.valueOf((byte)1)));
 boolean isEmpty = decorator.isEmpty();
 Assert.assertFalse(isEmpty);
 Assert.assertTrue(containsEntry);
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 boolean containsValue = decorator.containsValue(Byte.valueOf((byte)1));
 boolean doesNotContainValue = decorator.containsValue(Byte.valueOf((byte)3));
 Assert.assertTrue(containsValue);
 Assert.assertFalse(doesNotContainValue);
 }

 @Test
 public void testContainsKey() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 boolean containsKey = decorator.containsKey((short)1);
 boolean doesNotContainKey = decorator.containsKey((short)3);
 Assert.assertTrue(containsKey);
 Assert.assertFalse(doesNotContainKey);
 }

 @Test
 public void testSize() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 int size = decorator.size();
 Assert.assertEquals(decorator.entrySet().size(), size);
 }

 @Test
 public void testIsEmpty() {
 TShortByteMapDecorator emptyDecorator = new TShortByteMapDecorator();
 boolean isEmpty = emptyDecorator.isEmpty();
 Assert.assertTrue(isEmpty);
 }

 @Test
 public void testPutAll() {
 TShortByteMap sourceMap = new TShortByteMap();
 sourceMap.put((short)1, Byte.valueOf((byte)1));
 sourceMap.put((short)2, Byte.valueOf((byte)2));
 decorator.putAll(sourceMap);
 int size = decorator.size();
 boolean containsKey1 = decorator.containsKey((short)1);
 boolean containsKey2 = decorator.containsKey((short)2);
 boolean containsValue1 = decorator.containsValue(Byte.valueOf((byte)1));
 boolean containsValue2 = decorator.containsValue(Byte.valueOf((byte)2));
 Assert.assertEquals(
 sourceMap.size(),
 size
 );
 Assert.assertTrue(containsKey1);
 Assert.assertTrue(containsKey2);
 Assert.assertTrue(containsValue1);
 Assert.assertTrue(containsValue2);
 }

 @Test
 public void testWrapperMethods() {
 short key = 1;
 byte value = 1;
 Short wrappedKey = decorator.wrapKey(key);
 byte unwrappedValue = decorator.unwrapValue(value);
 Assert.assertEquals(key, wrappedKey);
 Assert.assertEquals(value, unwrappedValue);
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)in.readObject();
 Assert.assertEquals(decorator, deserializedDecorator);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TShortByteMapDecorator clone = (TShortByteMapDecorator)decorator.clone();
 Assert.assertNotSame(decorator, clone);
 Assert.assertEquals(decorator, clone.getMap());
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TShortByteMapDecorator deserializedDecorator = (TShortByteMapDecorator)in.readObject();
 TShortByteMap deserializedMap = deserializedDecorator.getMap();
 TShortByteMap originalMap = _map;
 TShortByteMap copyMap = (TShortByteMap)originalMap.clone();
 deserializedMap.putAll(copyMap);
 boolean areEqual = TShortByteMapDecorator.equals(deserializedDecorator, decorator);
 Assert.assertTrue(areEqual);
 }

 @Test
 public void testEquals() {
 TShortByteMapDecorator decorator1 = new TShortByteMapDecorator(_map);
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator(_map);
 TShortByteMapDecorator decorator3 = new TShortByteMapDecorator(new TShortByteMap());
 Assert.assertTrue(decorator1.equals(decorator2));
 Assert.assertFalse(decorator1.equals(decorator3));
 }

 @Test
 public void testHashCode() {
 TShortByteMapDecorator decorator1 = new TShortByteMapDecorator(_map);
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator(_map);
 TShortByteMapDecorator decorator3 = new TShortByteMapDecorator(new TShortByteMap());
 int hashCode1 = decorator1.hashCode();
 int hashCode2 = decorator2.hashCode();
 int hashCode3 = decorator3.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 Assert.neq(hashCode1, hashCode3);
 }

 @Test
 public void testEntryIterator() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 TShortByteIterator iterator = decorator.entrySet().iterator();
 Map.Entry<Short, Byte> entry = iterator.next();
 Assert.assertEquals(Short.valueOf((short)1), entry.getKey());
 Assert.assertEquals(Byte.valueOf((byte)1), entry.getValue());
 }

 @Test
 public void testKeySetIterator() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 Iterator<Short> keyIterator = decorator.keySet().iterator();
 Short key = keyIterator.next();
 Assert.assertEquals(Short.valueOf((short)1), key);
 }

 @Test
 public void testValuesIterator() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 Iterator<Byte> valueIterator = decorator.values().iterator();
 Byte value = valueIterator.next();
 Assert.assertEquals(Byte.valueOf((byte)1), value);
 }

 @Test
 public void testValuesView() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 TShortByteMapViews view = decorator.values();
 boolean containsValue1 = view.contains(Byte.valueOf((byte)1));
 boolean containsValue2 = view.contains(Byte.valueOf((byte)2));
 boolean doesNotContainValue3 = view.contains(Byte.valueOf((byte)3));
 Assert.assertTrue(containsValue1);
 Assert.assertTrue(containsValue2);
 Assert.assertFalse(doesNotContainValue3);
 }

 @Test
 public void testKeyView() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 TShortByteMapViews view = decorator.keySet();
 boolean containsKey1 = view.contains(Short.valueOf((short)1));
 boolean containsKey2 = view.contains(Short.valueOf((short)2));
 boolean doesNotContainKey3 = view.contains(Short.valueOf((short)3));
 Assert.assertTrue(containsKey1);
 Assert.assertTrue(containsKey2);
 Assert.assertFalse(doesNotContainKey3);
 }

 @Test
 public void testKeySetView() {
 decorator.put((short)1, Byte.valueOf((byte)1));
 decorator.put((short)2, Byte.valueOf((byte)2));
 TShortByteOpenMapView openView = decorator.keySet().view();
 boolean containsKey1 = openView.containsKey(Short.valueOf((short)1));
 boolean containsKey2 = openView.containsKey(Short.valueOf((short)2));
 boolean doesNotContainKey3 = openView.containsKey(Short.valueOf((short)3));
 Assert.assertTrue(containsKey1);
 Assert.assertTrue(containsKey2);
 Assert.assertFalse(doesNotContainKey3);
 }

}