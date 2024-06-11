import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.util.Collections;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setup() {
 map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(map);
 }

 @Test
 public void testConstructor() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
 Assert.assertNotNull(decorator);
 }

 @Test
 public void testConstructorWithMap() {
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 Assert.assertNotNull(decorator);
 Assert.assertEquals(map, decorator.getMap());
 }

 @Test
 public void testPut() {
 final Double key = 1.0;
 final Short value = (short) 1;
 short originalValue = decorator.put(key, value);
 if (originalValue == TDoubleShortMapDecorator.this._map.getNoEntryValue()) {
 Assert.assertNull(originalValue);
 } else {
 Assert.assertEquals(value, originalValue);
 }
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
 public void testGet() {
 final Double key = 1.0;
 final Short value = (short) 1;
 decorator.put(key, value);
 Assert.assertEquals(value, decorator.get(key));
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithNullKey() {
 decorator.get(null);
 }

 @Test(expected = ClassCastException.class)
 public void testGetWithNonDoubleKey() {
 decorator.put("key", (short)1);
 decorator.get("key");
 }

 @Test
 public void testClear() {
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testRemove() {
 final Double key = 1.0;
 final Short value = (short) 1;
 decorator.put(key, value);
 short originalValue = decorator.remove(key);
 if (originalValue == TDoubleShortMapDecorator.this._map.getNoEntryValue()) {
 Assert.assertNull(originalValue);
 } else {
 Assert.assertEquals(value, originalValue);
 }
 Assert.assertFalse(decorator.containsKey(key));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveWithNullKey() {
 decorator.remove(null);
 }

 @Test(expected = ClassCastException.class)
 public void testRemoveWithNonDoubleKey() {
 decorator.put("key", (short)1);
 decorator.remove("key");
 }

 @Test
 public void testEntrySet() {
 final Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 Assert.assertNotNull(entrySet);
 Assert.assertTrue(entrySet.isEmpty());
 }

 @Test
 public void testContainsValue() {
 final Short value = (short) 1;
 decorator.put(1.0, value);
 Assert.assertTrue(decorator.containsValue(value));
 }

 @Test
 public void testContainsKey() {
 final Double key = 1.0;
 decorator.put(key, (short)1);
 Assert.assertTrue(decorator.containsKey(key));
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
 final Map<Double, Short> mapToPut = new TDoubleShortOpenHashMap<>();
 mapToPut.put(1.0, (short)1);
 mapToPut.put(2.0, (short)2);
 decorator.putAll(mapToPut);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 final ByteArrayOutputStream out = new ByteArrayOutputStream();
 final ObjectOutput objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 objOut.close();
 final byte[] bytes = out.toByteArray();
 final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
 final ObjectInput objIn = new ObjectInputStream(in);
 TDoubleShortMapDecorator readDecorator = new TDoubleShortMapDecorator();
 readDecorator.readExternal(objIn);
 objIn.close();
 Assert.assertEquals(decorator.size(), readDecorator.size());
 }

 @Test
 public void testWriteExternal() throws IOException {
 final ByteArrayOutputStream out = new ByteArrayOutputStream();
 final ObjectOutput objOut = new ObjectOutputStream(out);
 decorator.writeExternal(objOut);
 objOut.close();
 final byte[] bytes = out.toByteArray();
 }
}