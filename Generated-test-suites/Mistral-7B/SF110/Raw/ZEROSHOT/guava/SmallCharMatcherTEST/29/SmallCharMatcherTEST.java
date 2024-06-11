import static org.junit.Assert.*;
import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.base.FastMatcher;
import org.junit.Test;

public class SmallCharMatcherTest {

 private static final char[] CHAR_ARRAY_1 = {'a', 'e', 'i', 'o', 'u'};
 private static final char[] CHAR_ARRAY_2 = {'a', 'b', 'c'};
 private static final char[] CHAR_ARRAY_3 = {};
 private static final char[] CHAR_ARRAY_4 = {'a', '\0', 'c'};
 private static final char[] CHAR_ARRAY_5 = {'a', 'b', 'c', 'a', 'b'};

 @Test
 public void testChooseTableSize() {
 final int setSize1 = 1;
 final int setSize2 = 6;
 final int setSize3 = 11;
 final int setSize4 = 13;
 final int setSize5 = 21;
 final int setSize6 = 31;
 final int expectedTableSize1 = 2;
 final int expectedTableSize2 = 8;
 final int expectedTableSize3 = 32;
 final int expectedTableSize4 = 16;
 final int expectedTableSize5 = 64;
 final int expectedTableSize6 = 128;

 assertEquals("Incorrect table size for setSize1", expectedTableSize1, SmallCharMatcher.chooseTableSize(setSize1));
 assertEquals("Incorrect table size for setSize2", expectedTableSize2, SmallCharMatcher.chooseTableSize(setSize2));
 assertEquals("Incorrect table size for setSize3", expectedTableSize3, SmallCharMatcher.chooseTableSize(setSize3));
 assertEquals("Incorrect table size for setSize4", expectedTableSize4, SmallCharMatcher.chooseTableSize(setSize4));
 assertEquals("Incorrect table size for setSize5", expectedTableSize5, SmallCharMatcher.chooseTableSize(setSize5));
 assertEquals("Incorrect table size for setSize6", expectedTableSize6, SmallCharMatcher.chooseTableSize(setSize6));
 }

 @Test
 public void testSmear() {
 final int hashCode = 123;
 final int expectedHashCode = 1997087743;

 assertEquals("Incorrect smeared hashcode", expectedHashCode, SmallCharMatcher.smear(hashCode));
 }

 @Test
 public void testCheckFilter() {
 final SmallCharMatcher charMatcher1 = new SmallCharMatcher(new char[0], 0L, false, "CharMatcher1");
 final SmallCharMatcher charMatcher2 = new SmallCharMatcher(new char[]{'a'}, 1L, false, "CharMatcher2");
 final SmallCharMatcher charMatcher3 = new SmallCharMatcher(new char[]{'a'}, 1L << 'a' | 1L << 'b', false, "CharMatcher3");
 final SmallCharMatcher charMatcher4 = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L << 'a' | 1L << 'b' | 1L << 'c', false, "CharMatcher4");
 final SmallCharMatcher charMatcher5 = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L << 'a' | 1L << 'b' | 1L << 'd', false, "CharMatcher5");
 final SmallCharMatcher charMatcher6 = new SmallCharMatcher(new char[]{'a', '\0'}, 1L << 'a', false, "CharMatcher6");
 final SmallCharMatcher charMatcher7 = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L << 'a' | 1L << 'b' | 1L << 'c' | 1L << '\0', false, "CharMatcher7");

 assertTrue("CharMatcher1 should match character '\0'", charMatcher1.matches('\0'));
 assertTrue("CharMatcher2 should match character 'a'", charMatcher2.matches('a'));
 assertTrue("CharMatcher3 should match characters 'a' and 'b'", charMatcher3.matches('a') && charMatcher3.matches('b'));
 assertTrue("CharMatcher4 should match characters 'a', 'b', and 'c'", charMatcher4.matches('a') && charMatcher4.matches('b') && charMatcher4.matches('c'));
 assertFalse("CharMatcher5 should not match character 'd'", charMatcher5.matches('d'));
 assertFalse("CharMatcher6 should not match character '\0'", charMatcher6.matches('\0'));
 assertFalse("CharMatcher7 should not match character '\0'", charMatcher7.matches('\0'));
 }

 @Test
 public void testFrom() {
 final BitSet bitSet1 = BitSet.create();
 final BitSet bitSet2 = BitSet.create().add('a').add('e').add('i').add('o').add('u');
 final BitSet bitSet3 = BitSet.create().add('a').add('b').add('c');
 final BitSet bitSet4 = BitSet.create().add('a').add('\0').add('c');
 final BitSet bitSet5 = BitSet.create().add('a').add('b').add('c').add('a').add('b');

 final CharMatcher charMatcher1 = SmallCharMatcher.from(bitSet1, "CharMatcher1");
 final CharMatcher charMatcher2 = SmallCharMatcher.from(bitSet2, "CharMatcher2");
 final CharMatcher charMatcher3 = SmallCharMatcher.from(bitSet3, "CharMatcher3");
 final CharMatcher charMatcher4 = SmallCharMatcher.from(bitSet4, "CharMatcher4");
 final CharMatcher charMatcher5 = SmallCharMatcher.from(bitSet5, "CharMatcher5");

 assertEquals("Incorrect description for CharMatcher1", "CharMatcher1", charMatcher1.description());
 assertEquals("Incorrect description for CharMatcher2", "CharMatcher2", charMatcher2.description());
 assertEquals("Incorrect description for CharMatcher3", "CharMatcher3", charMatcher3.description());
 assertEquals("Incorrect description for CharMatcher4", "CharMatcher4", charMatcher4.description());
 assertEquals("Incorrect description for CharMatcher5", "CharMatcher5", charMatcher5.description());

 assertTrue("CharMatcher1 should not match any character", !charMatcher1.matches('\0'));
 assertTrue("CharMatcher1 should not match character 'a'", !charMatcher1.matches('a'));
 assertTrue("CharMatcher2 should match character 'a'", charMatcher2.matches('a'));
 assertTrue("CharMatcher2 should match character 'e'", charMatcher2.matches('e'));
 assertTrue("CharMatcher2 should match character 'i'", charMatcher2.matches('i'));
 assertTrue("CharMatcher2 should match character 'o'", charMatcher2.matches('o'));
 assertTrue("CharMatcher2 should match character 'u'", charMatcher2.matches('u'));
 assertTrue("CharMatcher3 should match characters 'a' and 'c'", charMatcher3.matches('a') && charMatcher3.matches('c'));
 assertTrue("CharMatcher3 should not match character 'b'", !charMatcher3.matches('b'));
 assertTrue("CharMatcher4 should match character 'a'", charMatcher4.matches('a'));
 assertTrue("CharMatcher4 should not match character 'c'", !charMatcher4.matches('c'));
 assertTrue("CharMatcher4 should match character '\0'", charMatcher4.matches('\0'));
 assertTrue("CharMatcher5 should match characters 'a', 'b', and 'c'", charMatcher5.matches('a') && charMatcher5.matches('b') && charMatcher5.matches('c'));
 assertTrue("CharMatcher5 should match character 'a'", charMatcher5.matches('a'));
 }

 @Test
 public void testMatches() {
 final CharMatcher charMatcher1 = new SmallCharMatcher(new char[0], 0L, false, "CharMatcher1");
 final CharMatcher charMatcher2 = new SmallCharMatcher(new char[]{'a'}, 1L, false, "CharMatcher2");
 final CharMatcher charMatcher3 = new SmallCharMatcher(new char[]{'a'}, 1L << 'a' | 1L << 'b', false, "CharMatcher3");
 final CharMatcher charMatcher4 = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L << 'a' | 1L << 'b' | 1L << 'c', false, "CharMatcher4");
 final CharMatcher charMatcher5 = new SmallCharMatcher(new char[]{'a', '\0'}, 1L << 'a', false, "CharMatcher5");
 final CharMatcher charMatcher6 = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L << 'a' | 1L << 'b' | 1L << 'c' | 1L << '\0', false, "CharMatcher6");

 final char[] charArray1 = {};
 final char[] charArray2 = {'a'};
 final char[] charArray3 = {'a', 'e', 'i', 'o', 'u'};
 final char[] charArray4 = {'a', '\0', 'c'};
 final char[] charArray5 = {'a', 'b', 'c', 'a', 'b'};

 assertTrue("CharMatcher1 should not match any character", !charMatcher1.matches('\0'));
 assertTrue("CharMatcher1 should not match character 'a'", !charMatcher1.matches('a'));
 assertTrue("CharMatcher2 should match character 'a'", charMatcher2.matches('a'));
 assertFalse("CharMatcher2 should not match character 'e'", !charMatcher2.matches('e'));
 assertTrue("CharMatcher3 should match characters 'a' and 'b'", charMatcher3.matches('a') && charMatcher3.matches('b'));
 assertTrue("CharMatcher3 should not match character 'c'", !charMatcher3.matches('c'));
 assertTrue("CharMatcher4 should match character 'a'", charMatcher4.matches('a'));
 assertFalse("CharMatcher4 should not match character 'c'", !charMatcher4.matches('c'));
 assertTrue("CharMatcher4 should match character '\0'", charMatcher4.matches('\0'));
 assertFalse("CharMatcher5 should not match character '\0'", !charMatcher5.matches('\0'));
 assertFalse("CharMatcher5 should not match character 'a'", !charMatcher5.matches('a'));
 assertTrue("CharMatcher5 should match character 'c'", charMatcher5.matches('c'));
 assertTrue("CharMatcher6 should match characters 'a', 'b', 'c', and '\0'", charMatcher6.matches('a') && charMatcher6.matches('b') && charMatcher6.matches('c') && charMatcher6.matches('\0'));

 for (int i = 0; i < charArray1.length; i++) {
 assertFalse("CharMatcher1 should not match any character in charArray1", charMatcher1.matches(charArray1[i]));
 }
 for (int i = 0; i < charArray2.length; i++) {
 assertTrue("CharMatcher2 should match character in charArray2", charMatcher2.matches(charArray2[i]));
 }
 for (int i = 0; i < charArray3.length; i++) {
 assertTrue("CharMatcher2 should match all characters in charArray3", charMatcher2.matches(charArray3[i]));
 }
 for (int i = 0; i < charArray4.length; i++) {
 if (charArray4[i] != '\0') {
 assertTrue("CharMatcher4 should match character in charArray4, except for '\0'", charMatcher4.matches(charArray4[i]));
 } else {
 assertTrue("CharMatcher4 should match character '\0'", charMatcher4.matches(charArray4[i]));
 }
 }
 for (int i = 0; i < charArray5.length; i++) {
 assertTrue("CharMatcher5 should match character in charArray5", charMatcher5.matches(charArray5[i]));
 }
 }

 @Test
 public void testSetBits() {
 final BitSet bitSet1 = BitSet.create();
 final BitSet expectedBitSet1 = BitSet.create().add('a').add('e').add('i').add('o').add('u');
 final BitSet expectedBitSet2 = BitSet.create().add('a').add('b').add('c');
 final BitSet expectedBitSet3 = BitSet.create().add('a').add('\0').add('c');
 final BitSet expectedBitSet4 = BitSet.create().add('a').add('b').add('c').add('a').add('b');

 final CharMatcher charMatcher1 = new SmallCharMatcher(new char[0], 0L, false, "CharMatcher1");
 final CharMatcher charMatcher2 = new SmallCharMatcher(new char[]{'a'}, 1L, false, "CharMatcher2");
 final CharMatcher charMatcher3 = new SmallCharMatcher(new char[]{'a'}, 1L << 'a' | 1L << 'b', false, "CharMatcher3");
 final CharMatcher charMatcher4 = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L << 'a' | 1L << 'b' | 1L << 'c', false, "CharMatcher4");
 final CharMatcher charMatcher5 = new SmallCharMatcher(new char[]{'a', '\0'}, 1L << 'a', false, "CharMatcher5");
 final CharMatcher charMatcher6 = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 1L << 'a' | 1L << 'b' | 1L << 'c' | 1L << '\0', false, "CharMatcher6");

 charMatcher1.setBits(bitSet1);
 charMatcher2.setBits(expectedBitSet1);
 charMatcher3.setBits(expectedBitSet2);
 charMatcher4.setBits(expectedBitSet3);
 charMatcher5.setBits(expectedBitSet4);
 charMatcher6.setBits(expectedBitSet4);

 assertEquals("Incorrect bitset for CharMatcher1", expectedBitSet1, bitSet1);
 assertEquals("Incorrect bitset for CharMatcher2", expectedBitSet2, bitSet1);
 assertEquals("Incorrect bitset for CharMatcher3", expectedBitSet2, bitSet1);
 assertEquals("Incorrect bitset for CharMatcher4", expectedBitSet3, bitSet1);
 assertEquals("Incorrect bitset for CharMatcher5", expectedBitSet4, bitSet1);
 assertEquals("Incorrect bitset for CharMatcher6", expectedBitSet4, bitSet1);
 }
}