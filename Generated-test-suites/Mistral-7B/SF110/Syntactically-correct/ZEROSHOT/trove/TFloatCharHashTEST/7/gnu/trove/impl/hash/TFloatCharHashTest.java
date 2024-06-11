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
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(0.0f));
 assertFalse(hash.contains('\0'));
 }

 @Test
 public void testBasicOperation() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 assertEquals(3, hash.size());
 assertTrue(hash.containsKey(1.0f));
 assertTrue(hash.containsValue('a'));

 assertEquals('a', hash.get(1.0f));
 assertEquals('b', hash.get(2.0f));
 assertEquals('c', hash.get(3.0f));

 assertFalse(hash.containsKey(4.0f));
 assertFalse(hash.containsValue('d'));

 hash.remove(1.0f);
 assertEquals(2, hash.size());
 assertFalse(hash.containsKey(1.0f));
 assertTrue(hash.containsKey(2.0f));
 assertTrue(hash.containsKey(3.0f));
 }

 @Test
 public void testLoadFactor() {
 hash = new TFloatCharHash(5, 0.75f);
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');

 assertEquals(5, hash.size());
 assertTrue(hash.containsKey(1.0f));
 assertTrue(hash.containsKey(2.0f));
 assertTrue(hash.containsKey(3.0f));
 assertTrue(hash.containsKey(4.0f));
 assertTrue(hash.containsKey(5.0f));

 hash.put(6.0f, 'f');
 assertEquals(6, hash.size());
 }

 @Test
 public void testForEachProcedure() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 System.out.println("Key: " + key);
 return true;
 }
 };

 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 hash.forEachValue(procedure);
 hash.forEachKey(procedure);
 }
}