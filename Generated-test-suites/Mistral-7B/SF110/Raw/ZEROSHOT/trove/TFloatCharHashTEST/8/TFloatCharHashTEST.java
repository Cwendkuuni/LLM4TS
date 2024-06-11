import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 assertNotNull(hash);
 assertEquals(0, hash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TFloatCharHash(10);
 assertNotNull(hash);
 assertEquals(10, hash.capacity());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hash = new TFloatCharHash(15, 0.75f);
 assertNotNull(hash);
 assertEquals(15, hash.capacity());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValuesConstructor() {
 hash = new TFloatCharHash(20, 0.8f, 1.0f, 'A');
 assertNotNull(hash);
 assertEquals(20, hash.capacity());
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('A', hash.getNoEntryValue());
 }

 @Test
 public void testSetUpMethod() {
 int capacity = hash.setUp(10);
 assertNotNull(hash._set);
 assertEquals(10, capacity);
 }

 @Test
 public void testContains() {
 hash.put(0.1f, 'A');
 assertTrue(hash.contains(0.1f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 assertEquals(0.1f, key);
 return true;
 }
 };
 hash.put(0.1f, 'A');
 hash.put(0.2f, 'B');
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 hash.put(0.1f, 'A');
 hash.put(0.2f, 'B');
 int index = hash.indexOfKey(0.1f);
 hash.removeAt(index);
 assertFalse(hash.containsKey(0.1f));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertionWhenSetIsFull() {
 for (int i = 0; i < hash.capacity(); i++) {
 hash.put((float) i, (char) i);
 }
 }
}