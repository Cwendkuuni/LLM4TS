import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;
 private char[] testData;
 private TCharProcedure procedure;

 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 testData = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};
 procedure = new TCharProcedure() {
 public boolean execute(char charValue) {
 return charValue != 'e';
 }
 };
 }

 @Test
 public void testEmptyHash() {
 assertTrue(charHash.isEmpty());
 assertFalse(charHash.containsAny(testData));
 }

 @Test
 public void testBasicOperations() {
 charHash.put('a', 0);
 charHash.put('b', 1);
 charHash.put('c', 2);

 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));

 charHash.put('d', 3);
 assertEquals(4, charHash.size());

 assertTrue(charHash.containsAll(Arrays.asList(new Character[]{'a', 'b', 'c', 'd'})));

 charHash.remove('c');
 assertEquals(3, charHash.size());
 assertFalse(charHash.contains('c'));
 }

 @Test
 public void testPutNullKey() {
 try {
 charHash.put((char) 0, 0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveNonExistingKey() {
 assertFalse(charHash.remove('z'));
 }

 @Test
 public void testContainsAll() {
 HashSet<Character> hashSet = new HashSet<>();
 hashSet.addAll(Arrays.asList(new Character[]{'a', 'b', 'c', 'd', 'e'}));

 assertTrue(charHash.containsAll(hashSet.toArray(new Character[0])));
 }

 @Test
 public void testForEachProcedure() {
 charHash.putAll(testData);
 charHash.forEachProcedure(procedure);

 assertTrue(Arrays.stream(testData).noneMatch(procedure::execute));
 }

 @Test
 public void testHashCollision() {
 charHash.put('a', 0);
 charHash.put('b', 1);
 charHash.put('a', 2);

 assertEquals(3, charHash.size());

 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));

 int indexA = charHash.index('a');
 int indexB = charHash.index('b');

 assertNotEquals(indexA, indexB);
 }

 @Test
 public void testRehashing() {
 Random random = new Random();
 charHash = new TCharHash(random.nextInt(10) + 1);

 for (char c : testData) {
 charHash.put(c, random.nextInt());
 }

 int sizeBeforeRehash = charHash.size();
 charHash.resize(sizeBeforeRehash * 2);

 int sizeAfterRehash = charHash.size();

 assertTrue(sizeBeforeRehash < sizeAfterRehash);
 }
}