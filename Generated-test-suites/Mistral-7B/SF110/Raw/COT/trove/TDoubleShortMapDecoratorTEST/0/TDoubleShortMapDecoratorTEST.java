import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import java.util.Arrays;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testConstructorWithMap() {
 _map.put(1.0, (short)1);
 _map.put(2.0, (short)2);
 decorator = new TDoubleShortMapDecorator(_map);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testGetMap() {
 TDoubleShortMap map = decorator.getMap();
 assertEquals(_map, map);
 }

 @Test
 public void testPut() {
 Short value = (short)1;
 decorator.put(1.0, value);
 assertEquals(1, decorator.size());
 assertEquals(value, decorator.get(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (short)1);
 fail("NullPointerException expected but not thrown");
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put(1.0, null);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testGetWithNullKey() {
 assertEquals(null, decorator.get(null));
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithNonDoubleKey() {
 decorator.put("key", (short)1);
 assertEquals(null, decorator.get("key"));
 }

 @Test
 public void testClear() {
 decorator.clear();
 assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 Short removedValue = decorator.remove(1.0);
 assertEquals(removedValue, (short)1);
 assertEquals(0, decorator.size());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test(expected = ClassCastException.class)
 public void testRemoveWithNonDoubleKey() {
 decorator.put("key", (short)1);
 decorator.remove("key");
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 assertEquals(0, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertTrue(decorator.containsValue((short)1));
 assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertTrue(decorator.containsKey(1.0));
 assertTrue(decorator.containsKey(2.0));
 assertFalse(decorator.containsKey(3.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short)1);
 decorator.put(2.0, (short)2);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 assertEquals(true, decorator.isEmpty());
 decorator.put(1.0, (short)1);
 assertEquals(false, decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap anotherMap = new TDoubleShortMap();
 anotherMap.put(1.0, (short)1);
 anotherMap.put(2.0, (short)2);
 decorator.putAll(anotherMap);
 assertEquals(2, decorator.size());
 }

 @Test
 public void testWrapKey() {
 assertEquals(1.0, decorator.wrapKey(1.0));
 }

 @Test
 public void testUnwrapKey() {
 assertEquals(1.0, decorator.unwrapKey(1.0));
 }

 @Test
 public void testWrapValue() {
 assertEquals((short)1, decorator.wrapValue((short)1));
 }

 @Test
 public void testUnwrapValue() {
 assertEquals((short)1, decorator.unwrapValue((short)1));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 deserializedDecorator.readExternal(in);
 assertEquals(_map, deserializedDecorator.getMap());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 decorator.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
 deserializedDecorator.readExternal(new ByteArrayInputStream(bytes));
 assertEquals(_map, deserializedDecorator.getMap());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 TDoubleShortMapDecorator clonedDecorator = (TDoubleShortMapDecorator)decorator.clone();
 assertEquals(_map, clonedDecorator.getMap());
 }
}