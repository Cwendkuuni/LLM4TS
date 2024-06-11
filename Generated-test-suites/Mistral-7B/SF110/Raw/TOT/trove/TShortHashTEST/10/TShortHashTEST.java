import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hashMap;
 private TShortHash hashMapFull;
 private TShortHash hashMapWithNoEntryValue;
 private TShortProcedure procedure;

 @Before
 public void setUp() {
 hashMap = new TShortHash(5);
 hashMapFull = new TShortHash(5);
 hashMapWithNoEntryValue = new TShortHash(5, 0);
 procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return value > 0;
 }
 };
 }

 @Test
 public void testEmptyHashMap() {
 assertFalse(hashMap.contains((short) 0));
 assertFalse(hashMap.contains((short) 1));
 assertFalse(hashMap.forEach(procedure));
 }

 @Test
 public void testPutAndGet() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 assertEquals((short) 1, hashMap.get((short) 1));
 assertEquals((short) 2, hashMap.get((short) 2));
 }

 @Test
 public void testPutAndGetWithNoEntryValue() {
 hashMapWithNoEntryValue.put((short) 1);
 hashMapWithNoEntryValue.put((short) 2);
 assertEquals((short) 0, hashMapWithNoEntryValue.get((short) 1));
 assertEquals((short) 0, hashMapWithNoEntryValue.get((short) 2));
 }

 @Test
 public void testPutAndContains() {
 hashMap.put((short) 1);
 hashMap.put((short) 1);
 assertTrue(hashMap.contains((short) 1));
 }

 @Test
 public void testPutAndRemove() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 hashMap.remove((short) 1);
 assertFalse(hashMap.contains((short) 1));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutWhenHashMapIsFull() {
 for (short i = 0; i < 5; i++) {
 hashMapFull.put((short) i);
 }
 hashMapFull.put((short) 6);
 }

 @Test
 public void testForEachProcedure() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 hashMap.forEach(procedure);
 assertTrue(hashMap.contains((short) 1));
 assertFalse(hashMap.contains((short) -1));
 }

 @Test
 public void testIndexAndRemoveAt() {
 hashMap.put((short) 1);
 hashMap.put((short) 2);
 int index = hashMap.indexOf((short) 1);
 assertEquals((short) 1, hashMap.get(index));
 hashMap.removeAt(index);
 assertFalse(hashMap.contains((short) 1));
 }
}