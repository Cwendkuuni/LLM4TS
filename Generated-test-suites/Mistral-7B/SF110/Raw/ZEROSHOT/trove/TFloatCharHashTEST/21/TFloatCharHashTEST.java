import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
 assertNull(hash.getNoEntryKey());
 assertNull(hash.getNoEntryValue());
 assertFalse(hash.contains(0.0f));
 }

 @Test
 public void testBasicOperations() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 assertEquals(3.0f, hash.keyAt(2), 0.0f);
 assertEquals('c', hash.valueAt(2));
 assertEquals(3, hash.size());

 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));

 assertFalse(hash.contains(4.0f));
 }

 @Test
 public void testRemove() {
 hash.put(4.0f, 'd');
 hash.remove(4.0f);

 assertFalse(hash.contains(4.0f));
 assertNull(hash.valueAt(3));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float f) {
 assertEquals(f, hash.keyAt(index), 0.0f);
 return true;
 }
 };

 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 hash.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 for (int i = 0; i < hash.capacity(); i++) {
 hash.put(i, (float) i);
 }
 }
}