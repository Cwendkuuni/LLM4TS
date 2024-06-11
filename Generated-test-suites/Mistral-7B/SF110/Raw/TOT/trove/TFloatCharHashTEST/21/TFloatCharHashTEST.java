import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatCharHash(3);
 }

 @Test
 public void testContainsEmptyMap() {
 assertFalse(hashMap.contains(0.5f));
 }

 @Test
 public void testContainsKeyWithValue() {
 hashMap.put(0.5f, 'A');
 assertTrue(hashMap.contains(0.5f));
 }

 @Test
 public void testContainsKeyNotPresent() {
 hashMap.put(0.5f, 'A');
 assertFalse(hashMap.contains(1.5f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float element) {
 assertEquals(element, 0.5f);
 return true;
 }
 };
 hashMap.put(0.5f, 'A');
 hashMap.put(1.5f, 'B');
 hashMap.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testContainsKeyAfterRemove() {
 hashMap.put(0.5f, 'A');
 hashMap.remove(0.5f);
 assertFalse(hashMap.contains(0.5f));
 }

 @Test
 public void testRemoveKeyNotPresent() {
 hashMap.put(0.5f, 'A');
 hashMap.remove(1.5f);
 assertArrayEquals(new float[]{0.5f}, hashMap._set);
 }

 @Test
 public void testInsertKeyWithAvailableSlot() {
 hashMap.put(0.5f, 'A');
 hashMap.put(1.5f, 'B');
 hashMap.put(2.5f, 'C');
 assertEquals(3, hashMap.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyNoFreeSlot() {
 hashMap.put(0.5f, 'A');
 hashMap.put(1.5f, 'B');
 hashMap.put(2.5f, 'C');
 hashMap.put(3.5f, 'D'); // Should throw an exception
 }

 @Test
 public void testSetUpWithInitialCapacity() {
 final int initialCapacity = 5;
 hashMap = new TFloatCharHash(initialCapacity);
 assertEquals(initialCapacity, hashMap.size());
 }

 @Test
 public void testSetUpWithInitialCapacityAndLoadFactor() {
 final int initialCapacity = 5;
 final float loadFactor = 0.75f;
 hashMap = new TFloatCharHash(initialCapacity, loadFactor);
 assertEquals(initialCapacity, hashMap.size());
 }

 @Test
 public void testSetUpWithInitialCapacityLoadFactorAndInitialValues() {
 final int initialCapacity = 5;
 final float loadFactor = 0.75f;
 final float no_entry_key = 3.14f;
 final char no_entry_value = 'X';
 hashMap = new TFloatCharHash(initialCapacity, loadFactor, no_entry_key, no_entry_value);
 assertEquals(initialCapacity, hashMap.size());
 assertEquals(no_entry_key, hashMap.getNoEntryKey());
 assertEquals(no_entry_value, hashMap.getNoEntryValue());
 }
}