import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, charHash.size());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testPutGetChar() {
 charHash.put('a', 'a');
 assertEquals('a', charHash.get('a'));
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testPutSameCharTwice() {
 charHash.put('a', 'a');
 charHash.put('a', 'a');
 assertEquals('a', charHash.get('a'));
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testPutLoadFactor() {
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
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 //hash collisions test
 charHash.put('aa', 'aa');
 charHash.put('ab', 'ab');
 charHash.put('ac', 'ac');
 charHash.put('ad', 'ad');
 charHash.put('ae', 'ae');
 charHash.put('af', 'af');
 charHash.put('ag', 'ag');
 charHash.put('ah', 'ah');
 charHash.put('ai', 'ai');
 charHash.put('aj', 'aj');
 charHash.put('ak', 'ak');
 charHash.put('al', 'al');
 charHash.put('am', 'am');
 charHash.put('an', 'an');
 charHash.put('ao', 'ao');
 charHash.put('ap', 'ap');
 charHash.put('aq', 'aq');
 charHash.put('ar', 'ar');
 charHash.put('as', 'as');
 charHash.put('at', 'at');
 charHash.put('au', 'au');
 charHash.put('av', 'av');
 charHash.put('aw', 'aw');
 charHash.put('ax', 'ax');
 charHash.put('ay', 'ay');
 charHash.put('az', 'az');
 //Put with load factor test
 charHash = new TCharHash(11, 0.75f);
 for (char key = 'a'; key <= 'z'; key++) {
 charHash.put(key, key);
 }
 assertEquals(17, charHash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNegativeKey() {
 charHash.put((char) -1, 'a');
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testPutNullKey() {
 charHash = new TCharHash(11);
 try {
 charHash.put((char) 0, null);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 //expected
 }
 }

 @Test
 public void testRemoveChar() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.remove('b');
 assertFalse(charHash.contains('b'));
 assertTrue(charHash.contains('a'));
 }

 @Test
 public void testRemoveCharDoesntExist() {
 charHash.put('a', 'a');
 assertFalse(charHash.contains('b'));
 charHash.remove('b');
 }

 @Test
 public void testForEachProcedure() {
 final TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals(charValue, charValue);
 return true;
 }
 };
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 charHash.put('c', 'c');
 charHash.forEach(procedure);
 assertEquals(3, charHash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testConsumeFreeSlot() {
 charHash = new TCharHash(11);
 assertEquals(0, charHash.size());
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
 charHash.put('AA', 'AA'); //should consume free slot since load factor is below 0.75
 charHash.put('AB', 'AB'); //should consume free slot since load factor is below 0.75
 charHash.put('AC', 'AC'); //should consume free slot since load factor is below 0.75
 charHash.put('AD', 'AD'); //should cause rehash since load factor is above 0.75
 charHash.put('AE', 'AE'); //should cause rehash since load factor is above 0.75
 charHash.put('AF', 'AF'); //should cause rehash since load factor is above 0.75
 charHash.put('AG', 'AG'); //should cause rehash since load factor is above 0.75
 charHash.put('AH', 'AH'); //should cause rehash since load factor is above 0.75
 charHash.put('AI', 'AI'); //should cause rehash since load factor is above 0.75
 charHash.put('AJ', 'AJ'); //should cause rehash since load factor is above 0.75
 charHash.put('AK', 'AK'); //should cause rehash since load factor is above 0.75
 charHash.put('AL', 'AL'); //should cause rehash since load factor is above 0.75
 charHash.put('AM', 'AM'); //should cause rehash since load factor is above 0.75
 charHash.put('AN', 'AN'); //should cause rehash since load factor is above 0.75
 charHash.put('AO', 'AO'); //should cause rehash since load factor is above 0.75
 charHash.put('AP', 'AP'); //should cause rehash since load factor is above 0.75
 charHash.put('AQ', 'AQ'); //should cause rehash since load factor is above 0.75
 charHash.put('AR', 'AR'); //should cause rehash since load factor is above 0.75
 charHash.put('AS', 'AS'); //should cause rehash since load factor is above 0.75
 charHash.put('AT', 'AT'); //should cause rehash since load factor is above 0.75
 charHash.put('AU', 'AU'); //should cause rehash since load factor is above 0.75
 charHash.put('AV', 'AV'); //should cause rehash since load factor is above 0.75
 charHash.put('AW', 'AW'); //should cause rehash since load factor is above 0.75
 charHash.put('AX', 'AX'); //should cause rehash since load factor is above 0.75
 charHash.put('AY', 'AY'); //should cause rehash since load factor is above 0.75
 charHash.put('AZ', 'AZ'); //should cause rehash since load factor is above 0.75
 }

 @Test
 public void testGetCharWithIndex() {
 charHash = new TCharHash(11);
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
 charHash.put('AA', 'AA');
 charHash.put('AB', 'AB');
 charHash.put('AC', 'AC');
 charHash.put('AD', 'AD');
 charHash.put('AE', 'AE');
 charHash.put('AF', 'AF');
 charHash.put('AG', 'AG');
 charHash.put('AH', 'AH');
 charHash.put('AI', 'AI');
 charHash.put('AJ', 'AJ');
 charHash.put('AK', 'AK');
 charHash.put('AL', 'AL');
 charHash.put('AM', 'AM');
 charHash.put('AN', 'AN');
 charHash.put('AO', 'AO');
 charHash.put('AP', 'AP');
 charHash.put('AQ', 'AQ');
 charHash.put('AR', 'AR');
 charHash.put('AS', 'AS');
 charHash.put('AT', 'AT');
 charHash.put('AU', 'AU');
 charHash.put('AV', 'AV');
 charHash.put('AW', 'AW');
 charHash.put('AX', 'AX');
 charHash.put('AY', 'AY');
 charHash.put('AZ', 'AZ');
 assertEquals('a', charHash.get((char) 0));
 assertEquals('b', charHash.get((char) 1));
 assertEquals('c', charHash.get((char) 2));
 // ...
 assertEquals('z', charHash.get((char) 25));
 }
}