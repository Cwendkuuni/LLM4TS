import org.junit.Before;
import org.junit.Test;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import static org.junit.Assert.*;

public class TShortHashTest {

 private TShortHash hash;
 private short[] testData = {-128, 0, 127, 255, 300};

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(0, hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 assertEquals(5, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 assertEquals(5, hash.size());
 assertEquals(0.5f, hash.loadFactor(), 0.01f);
 }

 @Test
 public void testInitialCapacityLoadFactorAndNoEntryValueConstructor() {
 assertEquals(5, hash.size());
 assertEquals(0.5f, hash.loadFactor(), 0.01f);
 assertEquals(127, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(127, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 for (short s : testData) {
 assertTrue(hash.contains(s));
 }
 for (short s : new short[]{-1, 301, 129}) {
 assertFalse(hash.contains(s));
 }
 }

 @Test
 public void testForEach() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 if (value == 0) {
 fail("Value 0 should not exist in the hash.");
 }
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 short valueToRemove = 127;
 int index = hash.indexOf(valueToRemove);
 hash.removeAt(index);
 assertFalse(hash.contains(valueToRemove));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHashTable() {
 for (short s : testData) {
 hash.put(s);
 }
 hash.put((short) 301);
 }
}