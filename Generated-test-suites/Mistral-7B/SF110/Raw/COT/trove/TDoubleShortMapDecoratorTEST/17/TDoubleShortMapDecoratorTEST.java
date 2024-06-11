import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.decorator.TDoubleShortMapDecorator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
 Assert.assertNotNull(decorator);
 Assert.assertEquals(decorator.size(), 0);
 }

 @Test
 public void testConstructorWithMap() {
 _map.put(1.0, (short)1);
 _map.put(2.0, (short)2);
 decorator = new TDoubleShortMapDecorator(_map);
 TestEntry entry;
 Set<Entry<Double, Short>> entries = decorator.entrySet();
 int i = 0;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 Assert.assertEquals(entry.getKey(), _map.get(entry.getKey()), 0.001);
 Assert.assertEquals(entry.getValue(), _map.get(entry.getKey()), (short)0);
 i++;
 }
 Assert.assertEquals(i, _map.size());
 }

 @Test
 public void testGetMap() {
 TDoubleShortMap map = decorator.getMap();
 Assert.assertSame(map, _map);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullKey() {
 decorator.put(null, (short)1);
 }

 @Test(expected = NullPointerException.class)
 public void testPutNullValue() {
 decorator.put((double)1.0, null);
 }

 @Test
 public void testPut() {
 decorator.put((double)1.0, (short)1);
 decorator.put((double)2.0, (short)2);
 TestEntry entry;
 Set<Entry<Double, Short>> entries = decorator.entrySet();
 int i = 0;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 Assert.assertEquals(entry.getKey(), 1.0, 0.001);
 Assert.assertEquals(entry.getValue(), (short)1);
 i++;
 }
 Assert.assertEquals(i, 2);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullKey() {
 decorator.put((double)1.0, (short)1);
 decorator.get(null);
 }

 @Test
 public void testGet() {
 decorator.put((double)1.0, (short)1);
 decorator.put((double)2.0, (short)2);
 TestEntry entry = (TestEntry)decorator.get((double)1.0);
 Assert.assertNotNull(entry);
 Assert.assertEquals(entry.getKey(), 1.0, 0.001);
 Assert.assertEquals(entry.getValue(), (short)1);
 }

 @Test
 public void testClear() {
 decorator.clear();
 Assert.assertEquals(decorator.size(), 0);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 decorator.put((double)1.0, (short)1);
 decorator.remove(null);
 }

 @Test
 public void testRemove() {
 decorator.put((double)1.0, (short)1);
 decorator.put((double)2.0, (short)2);
 decorator.remove((double)1.0);
 TestEntry entry;
 Set<Entry<Double, Short>> entries = decorator.entrySet();
 int i = 0;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 if (entry.getKey().equals(1.0)) {
 continue;
 }
 Assert.assertEquals(entry.getKey(), 2.0, 0.001);
 Assert.assertEquals(entry.getValue(), (short)2);
 i++;
 }
 Assert.assertEquals(i, 1);
 }

 @Test
 public void testSize() {
 decorator.put((double)1.0, (short)1);
 decorator.put((double)2.0, (short)2);
 Assert.assertEquals(decorator.size(), 2);
 }

 @Test
 public void testIsEmpty() {
 decorator.put((double)1.0, (short)1);
 decorator.put((double)2.0, (short)2);
 Assert.assertFalse(decorator.isEmpty());
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short)1);
 map.put(2.0, (short)2);
 decorator.putAll(map.entrySet());
 TestEntry entry;
 Set<Entry<Double, Short>> entries = decorator.entrySet();
 int i = 0;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 Assert.assertEquals(entry.getKey(), 1.0, 0.001);
 Assert.assertEquals(entry.getValue(), (short)1);
 i++;
 }
 Assert.assertEquals(i, 2);
 }

 @Test
 public void testContainsValue() {
 decorator.put((double)1.0, (short)1);
 decorator.put((double)2.0, (short)1);
 Assert.assertTrue(decorator.containsValue((short)1));
 Assert.assertFalse(decorator.containsValue((short)3));
 }

 @Test
 public void testContainsKey() {
 decorator.put((double)1.0, (short)1);
 decorator.put((double)2.0, (short)1);
 Assert.assertTrue(decorator.containsKey(1.0));
 Assert.assertFalse(decorator.containsKey(2.0));
 }

 @Test
 public void testEntrySet() {
 Set<Entry<Double, Short>> entries = decorator.entrySet();
 TestEntry entry;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 Assert.assertNotNull(entry);
 }
 }

 /* Edge cases and exception handling tests */

 @Test(expected = ClassCastException.class)
 public void testGetWrongTypeKey() {
 decorator.put((double)1.0, (short)1);
 decorator.get("String");
 }

 @Test(expected = ClassCastException.class)
 public void testPutWrongTypeKey() {
 decorator.put((double)1.0, (short)1);
 decorator.put("String", (short)2);
 }

 @Test(expected = ClassCastException.class)
 public void testContainsKeyWrongTypeKey() {
 decorator.put((double)1.0, (short)1);
 Assert.assertFalse(decorator.containsKey("String"));
 }

 @Test
 public void testPutEmptyMap() {
 TDoubleShortMap map = new TDoubleShortMap();
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 decorator.put((double)1.0, (short)1);
 TestEntry entry;
 Set<Entry<Double, Short>> entries = decorator.entrySet();
 int i = 0;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 Assert.assertEquals(entry.getKey(), 1.0, 0.001);
 Assert.assertEquals(entry.getValue(), (short)1);
 i++;
 }
 Assert.assertEquals(i, 1);
 }

 @Test
 public void testPutSameKeyTwice() {
 decorator.put((double)1.0, (short)1);
 decorator.put((double)1.0, (short)2);
 TestEntry entry;
 Set<Entry<Double, Short>> entries = decorator.entrySet();
 int i = 0;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 if (entry.getKey().equals(1.0)) {
 Assert.assertEquals(entry.getValue(), (short)2);
 }
 i++;
 }
 Assert.assertEquals(i, 1);
 }

 @Test
 public void testExternalizable() throws IOException, ClassNotFoundException {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short)1);
 map.put(2.0, (short)2);
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 ByteArrayOutputStream byteStreamOut = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStreamOut);
 decorator.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStreamOut.toByteArray()));
 TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
 decorator2.readExternal(in);
 TestEntry entry;
 Set<Entry<Double, Short>> entries = decorator2.entrySet();
 int i = 0;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 Assert.assertEquals(entry.getKey(), 1.0, 0.001);
 Assert.assertEquals(entry.getValue(), (short)1);
 i++;
 }
 Assert.assertEquals(i, 2);
 }

 @Test
 public void testCloneable() throws CloneNotSupportedException {
 TDoubleShortMap map = new TDoubleShortMap();
 map.put(1.0, (short)1);
 map.put(2.0, (short)2);
 TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
 TDoubleShortMapDecorator cloned = (TDoubleShortMapDecorator)decorator.clone();
 TestEntry entry;
 Set<Entry<Double, Short>> entries = cloned.entrySet();
 int i = 0;
 for (Object obj : entries) {
 entry = (TestEntry)obj;
 Assert.assertEquals(entry.getKey(), 1.0, 0.001);
 Assert.assertEquals(entry.getValue(), (short)1);
 i++;
 }
 Assert.assertEquals(i, 2);
 }

}

class TestEntry implements Map.Entry<Double, Short> {
 private Double key;
 private Short value;

 public TestEntry(Double key, Short value) {
 this.key = key;
 this.value = value;
 }

 @Override
 public Double getKey() {
 return key;
 }

 @Override
 public Short getValue() {
 return value;
 }

 @Override
 public Short setValue(Short value) {
 Short oldValue = this.value;
 this.value = value;
 return oldValue;
 }

 @Override
 public boolean equals(Object o) {
 if (this == o) {
 return true;
 }
 if (o == null || getClass() != o.getClass()) {
 return false;
 }
 TestEntry that = (TestEntry)o;
 return key.equals(that.key) && value.equals(that.value);
 }

 @Override
 public int hashCode() {
 return key.hashCode() * 31 + value.hashCode();
 }
}