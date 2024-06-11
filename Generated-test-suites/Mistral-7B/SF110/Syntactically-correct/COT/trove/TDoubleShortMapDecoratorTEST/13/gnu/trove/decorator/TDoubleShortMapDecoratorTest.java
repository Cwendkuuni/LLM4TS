import org.junit.Test;
import org.junit.Assert.*;
import gnu.trove.map.*;
import gnu.trove.iterator.*;
import java.util.*;

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
 TDoubleShortMap actualMap = decorator.getMap();
 Assert.assertSame(actualMap, _map);
 }

 @Test
 public void testPut() {
 final double key = 1.0;
 final short value = 1;
 short actualValue = decorator.put(key, value);
 Assert.assertEquals(value, actualValue);
 Assert.assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutWithNullValue() {
 decorator.put(1.0, null);
 }

 @Test
 public void testGetWithNullKey() {
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGetWithKeyNotPresent() {
 Assert.assertNull(decorator.get(2.0));
 }

 @Test
 public void testClear() {
 decorator.clear();
 Assert.assertEquals(0, decorator.size());
 }

 @Test
 public void testRemove() {
 decorator.put(1.0, (short)1);
 short actualValue = decorator.remove(1.0);
 Assert.assertEquals(1, actualValue);
 Assert.assertNull(decorator.get(1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithKeyNotPresent() {
 decorator.remove(2.0);
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> actualSet = decorator.entrySet();
 Assert.assertNotNull(actualSet);
 }

 @Test
 public void testContainsValue() {
 decorator.put(1.0, (short)1);
 Assert.assertTrue(decorator.containsValue((short)1));
 }

 @Test
 public void testContainsKey() {
 decorator.put(1.0, (short)1);
 Assert.assertTrue(decorator.containsKey(1.0));
 }

 @Test
 public void testSize() {
 decorator.put(1.0, (short)1);
 Assert.assertEquals(1, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short)1);
 map.put(2.0, (short)2);
 decorator.putAll(map);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testWrapKey() {
 double key = 1.0;
 Assert.assertEquals(key, decorator.wrapKey(key));
 }

 @Test
 public void testUnwrapKey() {
 double key = decorator.unwrapKey(1.0);
 Assert.assertEquals(1.0, key, 0.001);
 }

 @Test
 public void testWrapValue() {
 short value = 1;
 Assert.assertEquals(value, decorator.wrapValue(value));
 }

 @Test
 public void testUnwrapValue() {
 short value = decorator.unwrapValue((short)1);
 Assert.assertEquals(1, value);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objStream);
 byte[] data = byteStream.toByteArray();
 ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(data));
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(input);
 Assert.assertEquals(decorator._map, readDecorator.getMap());
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 decorator.writeExternal(objStream);
 byte[] data = byteStream.toByteArray();
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(new ObjectInputStream(new ByteArrayInputStream(data)));
 Assert.assertEquals(decorator.size(), readDecorator.size());
 }
}