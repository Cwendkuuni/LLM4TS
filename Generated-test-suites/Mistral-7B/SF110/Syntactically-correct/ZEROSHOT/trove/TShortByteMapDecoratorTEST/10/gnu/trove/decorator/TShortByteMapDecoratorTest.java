import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;
import gnu.trove.map.TShortByteMapImpl;
import gnu.trove.iterator.TShortByteIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TShortByteMapDecoratorTest {

 private TShortByteMap _map;
 private TShortByteMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TShortByteMapImpl();
 decorator = new TShortByteMapDecorator(_map);
 }

 @Test
 public void testEmptyMap() {
 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPut() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 Assert.assertEquals(1, decorator.size());
 Assert.assertFalse(decorator.isEmpty());
 Assert.assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testNullKeyPut() {
 decorator.put(null, (byte)1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValuePut() {
 decorator.put((short)1, null);
 }

 @Test
 public void testGet() {
 short key = 1;
 byte value = 1;
 decorator.put(key, value);
 Assert.assertEquals(value, decorator.get(key));
 Assert.assertNull(decorator.get(2));
 }

 @Test(expected = NullPointerException.class)
 public void testNullGet() {
 decorator.get(null);
 }

 @Test
 public void testClear() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 byte removedValue = decorator.remove((short)1);
 Assert.assertEquals(1, decorator.size());
 Assert.assertEquals((byte)1, removedValue);
 }

 @Test(expected = NullPointerException.class)
 public void testNullRemove() {
 decorator.remove(null);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Short, Byte>> entrySet = decorator.entrySet();
 Assert.assertEquals(0, entrySet.size());
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 entrySet = decorator.entrySet();
 Assert.assertEquals(2, entrySet.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertTrue(decorator.containsValue((byte)1));
 Assert.assertTrue(decorator.containsValue((byte)2));
 Assert.assertFalse(decorator.containsValue((byte)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertTrue(decorator.containsKey((short)1));
 Assert.assertTrue(decorator.containsKey((short)2));
 Assert.assertFalse(decorator.containsKey((short)3));
 }

 @Test
 public void testSize() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put((short)1, (byte)1);
 decorator.put((short)2, (byte)2);
 Assert.assertFalse(decorator.isEmpty());
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TShortByteMap map = new TShortByteMapImpl();
 map.put((short)1, (byte)1);
 map.put((short)2, (byte)2);
 decorator.putAll(map);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testWrapKey() {
 short key = 1;
 Assert.assertEquals(key, decorator.wrapKey(key));
 }

 @Test
 public void testUnwrapKey() {
 short key = 1;
 Assert.assertEquals(key, decorator.unwrapKey(decorator.wrapKey(key)));
 }

 @Test
 public void testWrapValue() {
 byte value = 1;
 Assert.assertEquals(value, decorator.wrapValue(value));
 }

 @Test
 public void testUnwrapValue() {
 byte value = 1;
 Assert.assertEquals(value, decorator.unwrapValue(decorator.wrapValue(value)));
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
 ObjectOutput objectOutput = out::writeObject;
 ObjectInput objectInput = in::readObject;
 TShortByteMapDecorator decorator2 = new TShortByteMapDecorator();
 decorator2.readExternal(objectInput);
 decorator2.writeExternal(objectOutput);
 Assert.assertEquals(decorator, decorator2);
 }

 @After
 public void tearDown() {
 _map = null;
 decorator = null;
 }

}