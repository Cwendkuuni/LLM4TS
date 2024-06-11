import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5);
 }

 @Test
 public void testContains() {
 // Test empty hash map
 assertFalse(hashMap.contains(0.1f));

 // Test adding and checking elements
 hashMap.insertKey(0.1f);
 assertTrue(hashMap.contains(0.1f));
 }

 @Test
 public void testForEach() {
 // Test empty hash map
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 fail("Should not be called for an empty hash map");
 return false;
 }
 };
 assertFalse(hashMap.forEach(procedure));

 // Test adding and checking elements
 hashMap.insertKey(0.1f);
 hashMap.insertKey(0.2f);
 TFloatProcedure procedure2 = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 0.1f, 0);
 return true;
 }
 };
 assertTrue(hashMap.forEach(procedure2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNegativeCapacity() {
 new TFloatDoubleHash(-1);
 }

 @Test
 public void testInsertKeyRehash() {
 // Test initial index
 hashMap.insertKey(0.1f);
 hashMap.insertKey(0.2f);
 hashMap.insertKey(0.3f);
 int initialIndex = hashMap.index(0.1f);
 assertEquals(0, initialIndex);

 // Test rehashing
 hashMap.insertKey(0.4f);
 int rehashedIndex = hashMap.index(0.1f);
 assertNotEquals(0, rehashedIndex);
 }

 @Test
 public void testRemoveAt() {
 // Test empty hash map
 assertFalse(hashMap.contains(0.1f));
 hashMap.removeAt(-1);

 // Test adding and removing elements
 hashMap.insertKey(0.1f);
 hashMap.removeAt(0);
 assertFalse(hashMap.contains(0.1f));
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 // Write and read back an empty hash map
 TFloatDoubleHash hashMap1 = new TFloatDoubleHash(5);
 hashMap1.writeExternal(new ByteArrayOutputStream());
 ByteArrayInputStream inputStream = new ByteArrayInputStream(hashMap1.getInternalOut().toByteArray());
 TFloatDoubleHash hashMap2 = new TFloatDoubleHash();
 hashMap2.readExternal(inputStream);
 assertEquals(0, hashMap1.size());
 assertEquals(0, hashMap2.size());

 // Write and read back a hash map with elements
 hashMap1.insertKey(0.1f);
 hashMap1.writeExternal(new ByteArrayOutputStream());
 inputStream = new ByteArrayInputStream(hashMap1.getInternalOut().toByteArray());
 hashMap2 = new TFloatDoubleHash();
 hashMap2.readExternal(inputStream);
 assertEquals(1, hashMap1.size());
 assertEquals(1, hashMap2.size());
 assertEquals(0.1f, hashMap2.getNoEntryKey(), 0);
 assertTrue(hashMap2.contains(0.1f));
 }
}