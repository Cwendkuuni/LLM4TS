import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash<Character> charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash<>(5, 0.75f, '\u0000');
 }

 @Test
 public void testEmptyHash() {
 assertTrue(charHash.isEmpty());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testPutAndGet() {
 charHash.put('a', 1);
 assertEquals('a', charHash.get('a'));
 }

 @Test
 public void testPutAndContains() {
 charHash.put('a', 1);
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testPutSameKeyTwice() {
 charHash.put('a', 1);
 charHash.put('a', 2);
 assertEquals(2, charHash.get('a'));
 }

 @Test
 public void testPutNullKey() {
 try {
 charHash.put((char) 0, 1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testPutOutOfBounds() {
 try {
 charHash.put((char) 10, 1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testPutRehash() {
 for (int i = 0; i < 6; i++) {
 charHash.put((char) ('a' + i), i + 1);
 }
 assertEquals('f', charHash.get((char) 'f'));
 }

 @Test
 public void testRemove() {
 charHash.put('a', 1);
 charHash.put('b', 2);
 charHash.put('c', 3);
 charHash.remove('b');
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testRemoveNotPresent() {
 charHash.put('a', 1);
 charHash.remove('b');
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testRemoveOutOfBounds() {
 try {
 charHash.remove((int) -1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testForEach() {
 int count = 0;
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 count++;
 return true;
 }
 };
 charHash.forEach(procedure);
 assertEquals(6, count);
 }

}