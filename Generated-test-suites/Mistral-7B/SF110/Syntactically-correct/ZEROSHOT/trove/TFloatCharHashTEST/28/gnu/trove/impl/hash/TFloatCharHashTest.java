import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, hash.size());
 assertFalse(hash.contains(0.0f));
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testConstructor() {
 TFloatCharHash hash = new TFloatCharHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testConstructorWithLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
 assertEquals(13, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryKeyAndNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'X');;
 assertEquals(13, hash.size());
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('X', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 hash.put(0.0f, 'A');
 assertTrue(hash.contains(0.0f));
 }

 @Test
 public void testForEach() {
 char result = 0;
 TFloatCharHash hash = new TFloatCharHash(3);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 result = (char) key + 1;
 return true;
 }
 };

 hash.forEach(procedure);
 assertEquals('B', result);
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtIndexOutOfBounds() {
 hash.removeAt(-1);
 }

 @Test
 public void testRemoveAt() {
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(2.0f, 'C');
 hash.removeAt(1);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testIndex() {
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(2.0f, 'C');

 int index = hash.index(1.0f);
 assertEquals(1, index);
 }

 @Test
 public void testIndexRehashed() {
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(2.0f, 'C');
 hash.put(3.0f, 'D');
 hash.put(4.0f, 'E');
 hash.put(5.0f, 'F');
 hash.put(6.0f, 'G');
 hash.put(7.0f, 'H');
 hash.put(8.0f, 'I');
 hash.put(9.0f, 'J');
 hash.put(10.0f, 'K');

 int index = hash.index(6.0f);
 assertEquals(6, index);
 }

 @Test
 public void testInsertKey() {
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(2.0f, 'C');
 int index = hash.insertKey(3.0f);
 assertEquals(4, hash.size());
 assertEquals(3.0f, hash.get(index));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
 TFloatCharHash hash = new TFloatCharHash(2);
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.insertKey(2.0f);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyAtIndexOutOfBounds() {
 hash.insertKeyAt(-1, 3.0f);
 }

 @Test
 public void testInsertKeyAt() {
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(2.0f, 'C');
 hash.insertKeyAt(3, 3.0f);
 assertEquals(4, hash.size());
 assertEquals(3.0f, hash.get(3));
 }
}