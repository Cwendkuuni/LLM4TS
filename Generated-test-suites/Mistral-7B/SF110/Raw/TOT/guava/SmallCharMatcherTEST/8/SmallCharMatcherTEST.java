import org.junit.Test;
import org.junit.Assert.*;
import com.google.common.base.CharMatcher;
import com.google.common.base.BitSet;
import com.google.common.base.FastMatcher;

public class SmallCharMatcherTest {

 @Test
 public void testEmptyMatcher() {
 CharMatcher emptyMatcher = SmallCharMatcher.from(BitSet.create(), "Empty CharMatcher");
 BitSet expectedTable = BitSet.create();
 BitSet actualTable = new BitSet();
 emptyMatcher.setBits(actualTable);
 assertEquals(expectedTable, actualTable);
 assertTrue(emptyMatcher.matches('\0'));
 }

 @Test
 public void testSingleCharacterMatcher() {
 CharMatcher singleCharacterMatcher = SmallCharMatcher.from(BitSet.create((char) 'a'), "Single Character CharMatcher");
 BitSet expectedTable = BitSet.create((char) 'a');
 BitSet actualTable = new BitSet();
 singleCharacterMatcher.setBits(actualTable);
 assertEquals(expectedTable, actualTable);
 assertTrue(singleCharacterMatcher.matches('a'));
 }

 @Test
 public void testMultipleCharactersMatcher() {
 CharMatcher multipleCharactersMatcher = SmallCharMatcher.from(BitSet.create((char) 'a', (char) 'e', (char) 'i'), "Multiple Characters CharMatcher");
 BitSet expectedTable = BitSet.create((char) 'a', (char) 'e', (char) 'i');
 BitSet actualTable = new BitSet();
 multipleCharactersMatcher.setBits(actualTable);
 assertEquals(expectedTable, actualTable);
 assertTrue(multipleCharactersMatcher.matches('a'));
 assertTrue(multipleCharactersMatcher.matches('e'));
 assertTrue(multipleCharactersMatcher.matches('i'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCharacterMatcher() {
 CharMatcher negativeCharacterMatcher = SmallCharMatcher.from(BitSet.create((char) -1), "Negative Character CharMatcher");
 fail("Expected an IllegalArgumentException");
 }

 @Test
 public void testCharMatcherWithZero() {
 CharMatcher charMatcherWithZero = SmallCharMatcher.from(BitSet.create((char) 'a', '\0', (char) 'e'), "CharMatcherWithZero");
 BitSet expectedTable = BitSet.create((char) 'a', '\0', (char) 'e');
 BitSet actualTable = new BitSet();
 charMatcherWithZero.setBits(actualTable);
 assertEquals(expectedTable, actualTable);
 assertTrue(charMatcherWithZero.matches('a'));
 assertTrue(charMatcherWithZero.matches('\0'));
 assertTrue(charMatcherWithZero.matches('e'));
 }

 @Test
 public void testCharMatcherWithFilter() {
 CharMatcher charMatcherWithFilter = SmallCharMatcher.from(BitSet.create((char) 'a', (char) 'b', (char) 'c'), "CharMatcherWithFilter");
 assertFalse(charMatcherWithFilter.matches('\0'));
 assertFalse(charMatcherWithFilter.matches('d'));
 assertTrue(charMatcherWithFilter.matches('a'));
 assertTrue(charMatcherWithFilter.matches('b'));
 assertTrue(charMatcherWithFilter.matches('c'));
 }
}