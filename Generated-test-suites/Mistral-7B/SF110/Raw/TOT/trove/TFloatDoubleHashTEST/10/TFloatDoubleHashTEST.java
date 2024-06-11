import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(0.0f));
 }

 @Test
 public void testBasicFunctionality() {
 hash.insert(0.1f, 1.0);
 hash.insert(0.2f, 2.0);
 hash.insert(0.3f, 3.0);

 assertTrue(hash.contains(0.1f));
 assertEquals(1.0, hash.get(0.1f), 0.0);

 assertTrue(hash.contains(0.2f));
 assertEquals(2.0, hash.get(0.2f), 0.0);

 assertTrue(hash.contains(0.3f));
 assertEquals(3.0, hash.get(0.3f), 0.0);

 assertFalse(hash.contains(0.5f));

 assertEquals(3, hash.size());
 }

 @Test
 public void testLoadFactor() {
 hash = new TFloatDoubleHash(3, 0.75f);
 hash.insert(0.1f, 1.0);
 hash.insert(0.2f, 2.0);
 hash.insert(0.3f, 3.0);
 hash.insert(0.4f, 4.0);
 hash.insert(0.5f, 5.0);

 assertTrue(hash.contains(0.1f));
 assertEquals(1.0, hash.get(0.1f), 0.0);

 assertTrue(hash.contains(0.2f));
 assertEquals(2.0, hash.get(0.2f), 0.0);

 assertTrue(hash.contains(0.3f));
 assertEquals(3.0, hash.get(0.3f), 0.0);

 assertTrue(hash.contains(0.4f));
 assertEquals(4.0, hash.get(0.4f), 0.0);

 assertTrue(hash.contains(0.5f));
 assertEquals(5.0, hash.get(0.5f), 0.0);

 assertEquals(5, hash.size());
 }

 @Test
 public void testRemove() {
 hash = new TFloatDoubleHash(5);
 hash.insert(0.1f, 1.0);
 hash.insert(0.2f, 2.0);
 hash.insert(0.3f, 3.0);

 hash.remove(0.2f);

 assertFalse(hash.contains(0.2f));

 assertEquals(2, hash.size());
 }

 @Test
 public void testProcedure() {
 hash = new TFloatDoubleHash(5);
 hash.insert(0.1f, 1.0);
 hash.insert(0.2f, 2.0);
 hash.insert(0.3f, 3.0);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 if (key == 0.2f) {
 System.out.println("Found key: " + key);
 return false;
 }
 return true;
 }
 };

 assertFalse(hash.forEach(procedure));

 assertEquals(3, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 hash = new TFloatDoubleHash(3, 1.0f);
 hash.insert(0.1f, 1.0);
 hash.insert(0.2f, 2.0);
 hash.insert(0.3f, 3.0);
 hash.insert(0.4f, 4.0);
 hash.insert(0.5f, 5.0);
 hash.insert(0.6f, 6.0);
 }
}