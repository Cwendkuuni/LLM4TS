import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.impl.hash.Constants;

public class TShortHashTest {

 private TShortHash hash;
 private short[] testData = new short[] {1, 2, 3, 4, 5, 6, 7, 8, 9};

 @Before
 public void setup() {
 hash = new TShortHash(10);
 for (short num : testData) {
 hash.put(num);
 }
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 for (short num : testData) {
 assertTrue(hash.contains(num));
 }
 assertFalse(hash.contains((short) 10));
 }

 @Test
 public void testForEach() {
 int count = 0;
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(testData.length, count);
 }

 @Test
 public void testRemoveAt() {
 short numToRemove = 3;
 int index = hash.indexOf(numToRemove);
 hash.removeAt(index);
 assertFalse(hash.contains(numToRemove));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TShortHash hash = new TShortHash(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 TShortHash hash = new TShortHash(5, -0.5f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0.5f, (short) -1);
 }

 @Test
 public void testInsertKey() {
 short numToInsert = 10;
 int index = hash.insertKey(numToInsert);
 assertTrue(hash.contains(numToInsert));
 assertEquals(index, hash.indexOf(numToInsert));
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 hash.clear();
 for (short num : testData) {
 hash.put(num);
 }
 short numToInsert = 10;
 int index = hash.insertKey(numToInsert);
 fail("Should not be able to insert key into a full hash");
 }
}