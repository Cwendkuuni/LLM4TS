import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;

public class SmallCharMatcherTest {

 @Test
 public void testEmptyMatcher() {
 CharMatcher emptyMatcher = SmallCharMatcher.from(new BitSet(), "Empty CharMatcher");
 assertFalse(emptyMatcher.matches('\0'));
 assertFalse(emptyMatcher.matches('a'));
 BitSet expectedTable = new BitSet();
 emptyMatcher.setBits(expectedTable);
 assertTrue(expectedTable.isEmpty());
 }

 @Test
 public void testSingleCharacterMatcher() {
 CharMatcher singleCharacterMatcher = SmallCharMatcher.from(BitSet.create(1), "Single CharacterMatcher for 'a'");
 assertTrue(singleCharacterMatcher.matches('a'));
 assertFalse(singleCharacterMatcher.matches('b'));
 BitSet expectedTable = BitSet.create(1);
 singleCharacterMatcher.setBits(expectedTable);
 assertEquals(expectedTable.get(0), 'a');
 }

 @Test
 public void testMultipleCharacterMatcher() {
 CharMatcher multipleCharacterMatcher = SmallCharMatcher.from(BitSet.create(1, 3, 5), "Multiple CharacterMatcher for 'a', 'c', 'e'");
 assertTrue(multipleCharacterMatcher.matches('a'));
 assertTrue(multipleCharacterMatcher.matches('c'));
 assertTrue(multipleCharacterMatcher.matches('e'));
 assertFalse(multipleCharacterMatcher.matches('b'));
 BitSet expectedTable = BitSet.create(1, 3, 5);
 multipleCharacterMatcher.setBits(expectedTable);
 assertEquals(expectedTable.size(), 3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeSizeCharMatcher() {
 CharMatcher negativeSizeCharMatcher = SmallCharMatcher.from(BitSet.create(), -1);
 // the test should fail with an IllegalArgumentException here
 }

 @Test
 public void testCharMatcherDescription() {
 CharMatcher charMatcher = SmallCharMatcher.from(BitSet.create(1, 3, 5), "Multi-CharacterMatcher for 'a', 'c', 'e'");
 assertEquals("Multi-CharacterMatcher for 'a', 'c', 'e'", charMatcher.description());
 }

}