import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatCharHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 TFloatCharHash hashMap = new TFloatCharHash();
 assertEquals(0, hashMap.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatCharHash hashMap = new TFloatCharHash(5, 0.75f);
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
 TFloatCharHash hashMap = new TFloatCharHash(5, 0.75f, 1.0f, 'X');
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash hashMap = new TFloatCharHash();
 float noEntryKey = hashMap.getNoEntryKey();
 assertEquals(0.0f, noEntryKey, 0);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash hashMap = new TFloatCharHash();
 char noEntryValue = hashMap.getNoEntryValue();
 assertEquals('\0', noEntryValue);
 }

 @Test
 public void testSetUpWithInitialCapacity() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 int capacity = hashMap.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);

 assertTrue(hashMap.contains(1.0f));
 assertTrue(hashMap.contains(2.0f));
 assertTrue(hashMap.contains(3.0f));
 assertTrue(hashMap.contains(4.0f));
 assertTrue(hashMap.contains(5.0f));

 assertFalse(hashMap.contains(6.0f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 assertEquals(Float.valueOf(floatValue), Float.valueOf(1.0f));
 return true;
 }
 };

 assertTrue(hashMap.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);

 hashMap.removeAt(1);

 assertEquals(4, hashMap.size());
 assertFalse(hashMap.contains(2.0f));
 assertTrue(hashMap.contains(1.0f));
 assertTrue(hashMap.contains(3.0f));
 assertTrue(hashMap.contains(4.0f));
 assertTrue(hashMap.contains(5.0f));
 }

 @Test
 public void testIndex() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);

 assertEquals(1, hashMap.index(1.0f));
 assertEquals(-1, hashMap.index(6.0f));
 }

 @Test
 public void testIndexRehashed() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);
 hashMap.insertKey(6.0f);
 hashMap.insertKey(7.0f);

 assertEquals(-1, hashMap.index(1.0f));
 assertEquals(5, hashMap.index(6.0f));
 }

 @Test
 public void testInsertKey() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 int index = hashMap.insertKey(1.0f);
 assertEquals(1, hashMap.size());
 assertEquals(0, index);

 index = hashMap.insertKey(2.0f);
 assertEquals(2, hashMap.size());
 assertEquals(1, index);
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);
 hashMap.insertKey(6.0f);
 hashMap.insertKey(7.0f);

 int index = hashMap.insertKey(8.0f);
 assertEquals(8, hashMap.size());
 assertEquals(5, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TFloatCharHash hashMap = new TFloatCharHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);
 hashMap.insertKey(6.0f);
 hashMap.insertKey(7.0f);
 hashMap.insertKey(8.0f);
 hashMap.insertKey(9.0f);
 }
}