import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(3);
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(3, hash.capacity());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hashWithCapacity = new TShortHash(5);
 assertEquals(5, hashWithCapacity.capacity());
 assertNull(hashWithCapacity.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hashWithCapacityAndLoadFactor = new TShortHash(5, 0.75f);
 assertEquals(5, hashWithCapacityAndLoadFactor.capacity());
 assertNull(hashWithCapacityAndLoadFactor.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hashWithCapacityAndLoadFactorAndNoEntryValue = new TShortHash(5, 0.75f, (short) 42);
 assertEquals(5, hashWithCapacityAndLoadFactorAndNoEntryValue.capacity());
 assertEquals((short) 42, hashWithCapacityAndLoadFactorAndNoEntryValue.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hashWithEntryValue = new TShortHash(3, (short) 1);
 assertEquals((short) 1, hashWithEntryValue.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(3, hash.capacity());
 assertNotNull(hash._set);
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertFalse(hash.contains((short) 4));
 }

 @Test
 public void testForEach() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals(value, (short) 1);
 return true;
 }
 };
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtWithNoFreeSlot() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.removeAt(1);
 hash.removeAt(1);
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(1, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 5));
 }

 @Test
 public void testIndexRehashed() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 assertEquals(2, hash.indexRehashed((short) 2, -1, 1, (byte) 0));
 assertEquals(-1, hash.indexRehashed((short) 6, -1, 1, (byte) 0));
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(1, hash.insertKey((short) 1));
 assertEquals(3, hash.insertKey((short) 4));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithNoFreeSlot() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 }
}