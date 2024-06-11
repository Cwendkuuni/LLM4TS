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
 assertFalse(hash.contains(0.0f));
 assertFalse(hash.contains(1.0f));
 assertFalse(hash.remove(0.0f));
 assertFalse(hash.remove(1.0f));
 }

 @Test
 public void testPutGetAndRemove() {
 hash.put(0.0f, '\u0001');
 hash.put(1.0f, '\u0002');
 assertTrue(hash.contains(0.0f));
 assertTrue(hash.contains(1.0f));
 assertEquals('\u0001', hash.get(0.0f));
 assertEquals('\u0002', hash.get(1.0f));
 hash.remove(0.0f);
 assertFalse(hash.contains(0.0f));
 hash.remove(1.0f);
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testPutAndGetNoEntryKey() {
 TFloatCharHash hashWithNoEntry = new TFloatCharHash(5, 0.1f, 'A', (char) 0);
 hashWithNoEntry.put(0.0f, '\u0001');
 hashWithNoEntry.put(1.0f, '\u0002');
 assertEquals(0.1f, hashWithNoEntry.getNoEntryKey(), 0.0);
 assertEquals((char) 0, hashWithNoEntry.getNoEntryValue());
 }

 @Test
 public void testPutAndGetDifferentLoadFactors() {
 TFloatCharHash hashWithLowLoadFactor = new TFloatCharHash(5, 0.75f);
 TFloatCharHash hashWithHighLoadFactor = new TFloatCharHash(5, 0.95f);
 hashWithLowLoadFactor.put(0.0f, '\u0001');
 hashWithLowLoadFactor.put(1.0f, '\u0002');
 hashWithHighLoadFactor.put(0.0f, '\u0003');
 hashWithHighLoadFactor.put(1.0f, '\u0004');
 hashWithLowLoadFactor.put(2.0f, '\u0005');
 hashWithHighLoadFactor.put(2.0f, '\u0006');

 assertEquals(2, hashWithLowLoadFactor.size());
 assertEquals(4, hashWithHighLoadFactor.size());

 assertTrue(hashWithLowLoadFactor.contains(0.0f));
 assertTrue(hashWithLowLoadFactor.contains(1.0f));
 assertTrue(hashWithLowLoadFactor.contains(2.0f));

 assertTrue(hashWithHighLoadFactor.contains(0.0f));
 assertTrue(hashWithHighLoadFactor.contains(1.0f));
 assertTrue(hashWithHighLoadFactor.contains(2.0f));

 assertEquals('\u0001', hashWithLowLoadFactor.get(0.0f));
 assertEquals('\u0002', hashWithLowLoadFactor.get(1.0f));
 assertEquals('\u0005', hashWithLowLoadFactor.get(2.0f));

 assertEquals('\u0003', hashWithHighLoadFactor.get(0.0f));
 assertEquals('\u0004', hashWithHighLoadFactor.get(1.0f));
 assertEquals('\u0006', hashWithHighLoadFactor.get(2.0f));
 }

 @Test
 public void testForEachProcedure() {
 TFloatCharHash hash = new TFloatCharHash(3, 0.5f);
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(2.0f, 'C');
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key > 1.0f;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullProcedure() {
 TFloatCharHash hash = new TFloatCharHash(3, 0.5f);
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(2.0f, 'C');
 TFloatProcedure procedure = null;
 try {
 hash.forEach(procedure);
 } catch (IllegalArgumentException e) {
 assertEquals("Procedure cannot be null", e.getMessage());
 throw e;
 }
 }
}