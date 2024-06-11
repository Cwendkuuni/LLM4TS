import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(11, 0.7f);
 }

 @Test
 public void testEmptyTCharHash() {
 assertEquals(0, charHash.size());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testInitialization() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testPutAndGet() {
 charHash.put('a', 'a');
 assertEquals('a', charHash.get('a'));
 }

 @Test
 public void testPutAndContains() {
 charHash.put('a', 'a');
 assertTrue(charHash.contains('a'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithFullHashTable() {
 for (char i = 'a'; i <= 'z'; i++) {
 charHash.put(i, i);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutWithNullKey() {
 charHash.put((char) 0, 'a');
 }

 @Test
 public void testRemove() {
 charHash.put('a', 'a');
 charHash.remove('a');
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testForEachProcedure() {
 final char[] result = new char[10];
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 final TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 result[0] = character;
 return true;
 }
 };
 charHash.forEach(procedure);
 assertArrayEquals(new char[]{'a', 'b', 'c'}, result);
 }

 @Test
 public void testRehash() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.put('d', 'd');
 charHash.put('e', 'e');
 charHash.put('f', 'f');
 charHash.put('g', 'g');
 charHash.put('h', 'h');
 charHash.put('i', 'i');
 charHash.put('j', 'j');
 charHash.put('k', 'k');
 charHash.put('l', 'l');
 charHash.put('m', 'm');
 charHash.put('n', 'n');
 charHash.put('o', 'o');
 charHash.put('p', 'p');
 charHash.put('q', 'q');
 charHash.put('r', 'r');
 charHash.put('s', 's');
 charHash.put('t', 't');
 charHash.put('u', 'u');
 charHash.put('v', 'v');
 charHash.put('w', 'w');
 charHash.put('x', 'x');
 charHash.put('y', 'y');
 charHash.put('z', 'z');
 assertEquals(37, charHash.size());
 }

}