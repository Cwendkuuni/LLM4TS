import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TShortHash(5);
 }

 @Test
 public void testEmptyConstructor() {
 assertEquals(0, hashMap.size());
 assertNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hashMap = new TShortHash(10);
 assertEquals(10, hashMap.size());
 assertNotNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hashMap = new TShortHash(15, 0.75f);
 assertEquals(15, hashMap.size());
 assertNotNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 hashMap = new TShortHash(10, 0.75f, (short) 123);
 assertEquals(10, hashMap.size());
 assertSame(123, hashMap.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hashMap.put((short) 1, (short) 1);
 assertEquals(123, hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testContains() {
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 assertTrue(hashMap.contains((short) 1));
 assertTrue(hashMap.contains((short) 2));
 }

 @Test
 public void testForEach() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 1, value);
 return true;
 }
 };
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 hashMap.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyAndFullHashTable() {
 for (short i = 0; i <= hashMap.size(); i++) {
 hashMap.put((short) i, (short) i);
 }
 }

 @Test
 public void testInsertKey() {
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 int index = hashMap.insertKey((short) 3);
 assertEquals(3, hashMap.size());
 assertEquals((short) 3, hashMap.get((short) 3));
 assertEquals(1, hashMap.get((short) 1));
 assertEquals(2, hashMap.get((short) 2));
 }

 @Test
 public void testRemoveAt() {
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 hashMap.removeAt(0);
 assertEquals(1, hashMap.size());
 assertNull(hashMap.get((short) 1));
 }

 @Test
 public void testIndex() {
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 assertEquals(1, hashMap.index((short) 1));
 assertEquals(-1, hashMap.index((short) 3));
 }

 @Test
 public void testIndexRehashed() {
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 assertEquals(1, hashMap.indexRehashed((short) 1, 0, HashFunctions.hash((short) 1), (byte) 1));
 }
}