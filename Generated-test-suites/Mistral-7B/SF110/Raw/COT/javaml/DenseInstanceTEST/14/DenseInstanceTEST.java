import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.AbstractInstance;

public class DenseInstanceTest {

 private DenseInstance denseInstance;

 @Before
 public void setUp() {
 double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
 denseInstance = new DenseInstance(attributes);
 }

 // Basic test case for constructor with double array argument
 @Test
 public void testDenseInstanceWithDoubleArray() {
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 assertEquals(4, denseInstance.noAttributes());
 for (int i = 0; i < 4; i++) {
 assertEquals(i + 1.0, denseInstance.value(i), 0.0);
 }
 }

 // Basic test case for constructor with double array argument and class value
 @Test
 public void testDenseInstanceWithDoubleArrayAndClassValue() {
 Object classValue = "ClassValue";
 DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, classValue);
 assertEquals(classValue, denseInstance.classValue());
 }

 // Basic test case for constructor with size
 @Test
 public void testDenseInstanceWithSize() {
 DenseInstance denseInstance = new DenseInstance(5);
 assertEquals(5, denseInstance.noAttributes());
 }

 // Testing method value()
 @Test
 public void testValue() {
 assertEquals(1.0, denseInstance.value(0), 0.0);
 }

 // Testing method clear()
 @Test
 public void testClear() {
 denseInstance.clear();
 assertEquals(0, denseInstance.noAttributes());
 }

 // Testing method containsKey()
 @Test
 public void testContainsKey() {
 assertEquals(true, denseInstance.containsKey(0));
 assertEquals(false, denseInstance.containsKey(5));
 }

 // Testing method containsValue()
 @Test
 public void testContainsValue() {
 assertEquals(true, denseInstance.containsValue(2.0));
 assertEquals(false, denseInstance.containsValue(5.0));
 }

 // Testing method entrySet()
 @Test
 public void testEntrySet() {
 Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
 Iterator<Map.Entry<Integer, Double>> iterator = entrySet.iterator();
 int pos = 0;
 while (iterator.hasNext()) {
 Map.Entry<Integer, Double> entry = iterator.next();
 assertEquals(pos++, entry.getKey());
 assertEquals(denseInstance.value(pos - 1), entry.getValue(), 0.0);
 }
 }

 // Testing method get()
 @Test
 public void testGet() {
 assertEquals(1.0, denseInstance.get(0), 0.0);
 }

 // Testing method isEmpty()
 @Test
 public void testIsEmpty() {
 denseInstance.clear();
 assertTrue(denseInstance.isEmpty());
 }

 // Testing method keySet()
 @Test
 public void testKeySet() {
 SortedSet<Integer> keySet = denseInstance.keySet();
 Iterator<Integer> keySetIterator = keySet.iterator();
 int pos = 0;
 while (keySetIterator.hasNext()) {
 Integer key = keySetIterator.next();
 assertEquals(pos++, key.intValue());
 }
 }

 // Testing method put()
 @Test(expected = UnsupportedOperationException.class)
 public void testPut() {
 denseInstance.put(5, 5.0);
 fail("Should throw UnsupportedOperationException");
 }

 // Testing method putAll()
 @Test
 public void testPutAll() {
 DenseInstance newInstance = new DenseInstance();
 newInstance.put(0, 5.0);
 newInstance.put(1, 6.0);
 denseInstance.putAll(newInstance.entrySet());
 assertEquals(5.0, denseInstance.value(0), 0.0);
 assertEquals(6.0, denseInstance.value(1), 0.0);
 }

 // Testing method remove() (Deprecated)
 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 denseInstance.remove(0);
 fail("Should throw UnsupportedOperationException");
 }

 // Testing method size() (Deprecated)
 @Test
 public void testSize() {
 assertEquals(4, denseInstance.noAttributes());
 }

 // Testing method values()
 @Test
 public void testValues() {
 List<Double> values = new ArrayList<>();
 values.addAll(denseInstance.values());
 assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0), values);
 }

 // Testing method noAttributes()
 @Test
 public void testNoAttributes() {
 assertEquals(4, denseInstance.noAttributes());
 }

 // Testing method toString()
 @Test
 public void testToString() {
 String toString = denseInstance.toString();
 assertTrue(toString.contains("{"));
 assertTrue(toString.contains(";"));
 }

 // Testing method removeAttribute()
 @Test
 public void testRemoveAttribute() {
 denseInstance.removeAttribute(1);
 assertEquals(3, denseInstance.noAttributes());
 }

 // Testing method hashCode()
 @Test
 public void testHashCode() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 assertEquals(instance1.hashCode(), instance2.hashCode());
 }

 // Testing method equals()
 @Test
 public void testEquals() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 DenseInstance instance2 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 assertTrue(instance1.equals(instance2));
 DenseInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0, 4.0});
 assertFalse(instance1.equals(instance3));
 }

 // Testing method copy()
 @Test
 public void testCopy() {
 DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
 DenseInstance instance2 = instance1.copy();
 assertEquals(instance1, instance2);
 }

 // Testing method removeAttributes()
 @Test
 public void testRemoveAttributes() {
 Set<Integer> indices = new HashSet<>();
 indices.add(1);
 indices.add(3);
 denseInstance.removeAttributes(indices);
 assertEquals(2, denseInstance.noAttributes());
 }

 // Testing method value() with negative index
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testValueNegativeIndex() {
 denseInstance.value(-1);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 // Testing method value() with index greater than the size of the instance
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testValueIndexOutOfBounds() {
 denseInstance.value(5);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 // Testing method containsKey() with negative index
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testContainsKeyNegativeIndex() {
 denseInstance.containsKey(-1);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 // Testing method containsKey() with index greater than the size of the instance
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testContainsKeyIndexOutOfBounds() {
 denseInstance.containsKey(5);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 // Testing method containsValue() with null value
 @Test(expected = NullPointerException.class)
 public void testContainsValueNullValue() {
 denseInstance.containsValue(null);
 fail("Should throw NullPointerException");
 }

 // Testing method containsValue() with a value that is not present in the instance
 @Test(expected = IllegalArgumentException.class)
 public void testContainsValueNotPresentValue() {
 denseInstance.containsValue(5.0);
 fail("Should throw IllegalArgumentException");
 }

 // Testing method put() with a key that already exists in the instance
 @Test(expected = IllegalArgumentException.class)
 public void testPutExistingKey() {
 denseInstance.put(0, 5.0);
 try {
 denseInstance.put(0, 6.0);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Testing method putAll() with a map that contains keys that already exist in the instance
 @Test(expected = IllegalArgumentException.class)
 public void testPutAllExistingKeys() {
 DenseInstance instance = new DenseInstance();
 instance.put(0, 1.0);
 DenseInstance newInstance = new DenseInstance();
 newInstance.put(0, 2.0);
 newInstance.put(1, 3.0);
 try {
 denseInstance.putAll(newInstance.entrySet());
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Testing method remove() with a key that does not exist in the instance
 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveNonExistingKey() {
 try {
 denseInstance.remove(5);
 fail("Should throw UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 // Testing method remove() with a null key
 @Test(expected = NullPointerException.class)
 public void testRemoveNullKey() {
 try {
 denseInstance.remove(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}