import org.junit.Assert;
import org.junit.Test;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.TObjectShortMap;
import java.util.Arrays;
import java.util.Collections;
import static org.mockito.Mockito.*;

public class TDoubleShortMapDecoratorTest {

 private TDoubleShortMap _map;
 private TDoubleShortMapDecorator decorator;

 @Before
 public void setUp() {
 _map = new TDoubleShortMap();
 decorator = new TDoubleShortMapDecorator(_map);
 }

 @Test
 public void testPutNullKey() {
 decorator.put(null, (short) 0);
 // _map should not have any entry since put method in AbstractMap throws an exception for null keys
 Assert.assertEquals(0, _map.size());
 }

 @Test
 public void testPutNullValue() {
 decorator.put((double) 1.0, null);
 // _map should have one entry with key 1.0 and value 0
 Assert.assertEquals(1, _map.size());
 }

 @Test
 public void testPut() {
 decorator.put((double) 1.0, (short) 5);
 // _map should have one entry with key 1.0 and value 5
 Assert.assertEquals(1, _map.size());
 }

 @Test
 public void testGetNullKey() {
 Assert.assertNull(decorator.get(null));
 }

 @Test
 public void testGet() {
 decorator.put((double) 1.0, (short) 5);
 Assert.assertEquals((short) 5, decorator.get((double) 1.0));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullValue() {
 decorator.put((double) 1.0, (short) 5);
 decorator.get(null);
 }

 @Test
 public void testClear() {
 decorator.put((double) 1.0, (short) 5);
 decorator.put((double) 2.0, (short) 10);
 decorator.clear();
 // _map should be empty
 Assert.assertEquals(0, _map.size());
 }

 @Test
 public void testRemoveNullKey() {
 decorator.put((double) 1.0, (short) 5);
 decorator.clear();
 decorator.put((double) 2.0, (short) 10);
 decorator.remove(null);
 // _map should have only one entry with key 2.0 and value 10
 Assert.assertEquals(1, _map.size());
 }

 @Test
 public void testRemove() {
 decorator.put((double) 1.0, (short) 5);
 decorator.put((double) 2.0, (short) 10);
 decorator.remove((double) 1.0);
 // _map should have only one entry with key 2.0 and value 10
 Assert.assertEquals(1, _map.size());
 }

 @Test
 public void testEntrySet() {
 decorator.put((double) 1.0, (short) 5);
 decorator.put((double) 2.0, (short) 10);
 // Create a mock object to test if remove method in AbstractSet is called when an existing entry is removed
 TObjectShortMap mockEntrySet = mock(TObjectShortMap.class);
 doReturn(Collections.singleton((Entry<? extends Double, ? extends Short>) new EntryImpl((double) 1.0, (short) 5))).when(mockEntrySet).iterator().next();
 doAnswer(invocation -> decorator.remove(invocation.getArguments()[0]))
         .when(mockEntrySet).remove(eq((Object) new EntryImpl((double) 1.0, (short) 5)));
 Set<Entry<Double, Short>> entrySet = decorator.entrySet();
 entrySet.addAll(mockEntrySet);
 entrySet.remove(new EntryImpl((double) 1.0, (short) 5));
 // _map should have only one entry with key 2.0 and value 10
 Assert.assertEquals(1, _map.size());
 }

 @Test
 public void testContainsValue() {
 decorator.put((double) 1.0, (short) 5);
 decorator.put((double) 2.0, (short) 10);
 Assert.assertTrue(decorator.containsValue((short) 5));
 Assert.assertTrue(decorator.containsValue((short) 10));
 Assert.assertFalse(decorator.containsValue((short) 11));
 }

 @Test
 public void testContainsKey() {
 decorator.put((double) 1.0, (short) 5);
 decorator.put((double) 2.0, (short) 10);
 Assert.assertTrue(decorator.containsKey((double) 1.0));
 Assert.assertTrue(decorator.containsKey((double) 2.0));
 Assert.assertFalse(decorator.containsKey((double) 3.0));
 }

 @Test
 public void testSize() {
 decorator.put((double) 1.0, (short) 5);
 decorator.put((double) 2.0, (short) 10);
 Assert.assertEquals(2, decorator.size());
 }

 @Test
 public void testIsEmpty() {
 decorator.put((double) 1.0, (short) 5);
 decorator.put((double) 2.0, (short) 10);
 Assert.assertFalse(decorator.isEmpty());
 decorator.clear();
 Assert.assertTrue(decorator.isEmpty());
 }

 @Test
 public void testPutAll() {
 TObjectDoubleMap<Double, Short> map = new TObjectDoubleMap<>();
 map.put((double) 1.0, (short) 5);
 map.put((double) 2.0, (short) 10);
 decorator.putAll(map);
 // _map should have the same entries as map
 Assert.assertEquals(2, decorator.size());
 Assert.assertEquals(map.get((double) 1.0), decorator.get((double) 1.0));
 Assert.assertEquals(map.get((double) 2.0), decorator.get((double) 2.0));
 }
}

class EntryImpl implements Entry<Double, Short> {
 private Double key;
 private Short value;

 public EntryImpl(Double key, Short value) {
 this.key = key;
 this.value = value;
 }

 public boolean equals(Object o) {
 if (o instanceof Entry) {
 Entry<?> entry = (Entry<?>)o;
 return entry.getKey().equals(this.key) && entry.getValue().equals(this.value);
 }
 return false;
 }

 public Double getKey() {
 return this.key;
 }

 public Short getValue() {
 return this.value;
 }

 public int hashCode() {
 return this.key.hashCode() + this.value.hashCode();
 }

 public Short setValue(Short value) {
 Short oldValue = this.value;
 this.value = value;
 return oldValue;
 }
}