import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 @Test
 public void testEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash(5);
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(0.0f));
 }

 @Test
 public void testBasicOperations() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 assertEquals(3, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));

 assertEquals('a', hash.get('a'));
 assertEquals('b', hash.get('b'));
 assertEquals('c', hash.get('c'));

 assertEquals(1.0f, hash.get(1.0f));
 assertEquals(2.0f, hash.get(2.0f));
 assertEquals(3.0f, hash.get(3.0f));

 hash.remove(1.0f);
 assertEquals(2, hash.size());
 assertFalse(hash.containsKey(1.0f));
 assertFalse(hash.containsValue('a'));
 }

 @Test
 public void testLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(5, 0.75f);
 for (int i = 0; i < 7; i++) {
 hash.put(i + 0.5f, (char) i);
 }
 assertEquals(10, hash.size());
 }

 @Test
 public void testNullKey() {
 TFloatCharHash hash = new TFloatCharHash(5);
 try {
 hash.put(null, 'a');
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testNullValue() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, null);
 try {
 assertEquals(null, hash.get(1.0f));
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveEntry() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 hash.remove(2.0f);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(2.0f));
 assertFalse(hash.containsValue('b'));
 }

 @Test
 public void testRemoveNonExistingEntry() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.remove(5.0f);
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testIteration() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 if (key == 2.0f) {
 fail();
 return false;
 }
 return true;
 }
 };

 assertTrue(hash.forEach(procedure));
 }

}