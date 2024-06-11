import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash tShortHash;
 private TShortProcedure shortProcedure;

 @Before
 public void setUp() {
 tShortHash = new TShortHash(5);
 shortProcedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 return true;
 }
 };
 }

 @Test
 public void testDefaultConstructor() {
 TShortHash tShortHashDefault = new TShortHash();
 assertEquals(0, tShortHashDefault.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash tShortHashInitialCapacity = new TShortHash(10);
 assertEquals(10, tShortHashInitialCapacity.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash tShortHashInitialCapacityAndLoadFactor = new TShortHash(10, 0.75f);
 assertEquals(10, tShortHashInitialCapacityAndLoadFactor.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash tShortHashInitialCapacityAndLoadFactorAndNoEntryValue = new TShortHash(10, 0.75f, (short) 1);
 assertEquals(10, tShortHashInitialCapacityAndLoadFactorAndNoEntryValue.size());
 assertEquals((short) 1, tShortHashInitialCapacityAndLoadFactorAndNoEntryValue.getNoEntryValue());
 }

 @Test
 public void testContains() {
 tShortHash.put((short) 1);
 assertTrue(tShortHash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 short count = 0;
 tShortHash.put((short) 1);
 tShortHash.put((short) 2);
 tShortHash.put((short) 3);
 tShortHash.forEach(shortProcedure);
 assertEquals(3, count);
 }

 @Test
 public void testRemoveAt() {
 short initialSize = tShortHash.size();
 tShortHash.put((short) 1);
 int index = 0;
 tShortHash.removeAt(index);
 assertEquals(initialSize - 1, tShortHash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullyLoadedHash() {
 for (short i = 0; i < TShortHash.SIZE_LIMIT; i++) {
 tShortHash.put((short) i);
 }
 tShortHash.put((short) 1);
 }

 @Test
 public void testIndex() {
 tShortHash.put((short) 1);
 tShortHash.put((short) 2);
 assertEquals(0, tShortHash.index((short) 1));
 assertEquals(-1, tShortHash.index((short) 3));
 }

 @Test
 public void testIndexRehashed() {
 TShortHash tShortHashRehashed = new TShortHash(TShortHash.SIZE_LIMIT / 2);
 for (short i = 0; i < TShortHash.SIZE_LIMIT; i++) {
 tShortHashRehashed.put((short) i);
 }
 assertEquals(-1, tShortHashRehashed.index((short) 100));
 }

 @Test
 public void testInsertKey() {
 tShortHash.put((short) 1);
 int index = tShortHash.insertKey((short) 2);
 assertEquals(1, tShortHash.size());
 assertEquals(1, tShortHash.index((short) 1));
 assertEquals(0, tShortHash.index((short) 2));
 }
}