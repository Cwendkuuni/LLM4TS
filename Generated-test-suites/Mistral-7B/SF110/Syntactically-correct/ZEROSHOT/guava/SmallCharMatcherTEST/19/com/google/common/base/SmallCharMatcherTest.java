package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.BitSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SmallCharMatcherTest {

 private SmallCharMatcher charMatcher;
 private char character;

 @Parameterized.Parameters(name = "{index}: charMatcher.matches({0}) = {1}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 // Test empty charMatcher
 { null, '\u0000' },
 // Test charMatcher with no characters
 { createCharMatcher(new BitSet(), "emptyCharMatcher"), '\u0001' },
 // Test charMatcher with single character
 { createCharMatcher(new BitSet().set(0x01), "charMatcherWithSingleCharacter"), '\u0001' },
 // Test charMatcher with multiple characters
 { createCharMatcher(new BitSet().set(0x01, 0x03, 0x05), "charMatcherWithMultipleCharacters"), '\u0001' },
 { createCharMatcher(new BitSet().set(0x01, 0x03, 0x05), "charMatcherWithMultipleCharacters"), '\u0003' },
 { createCharMatcher(new BitSet().set(0x01, 0x03, 0x05), "charMatcherWithMultipleCharacters"), '\u0005' },
 // Test charMatcher with character not in the set
 { createCharMatcher(new BitSet().set(0x01, 0x03, 0x05), "charMatcherWithMultipleCharacters"), '\u0002' },
 // Test charMatcher with character equal to zero
 { createCharMatcher(new BitSet().set(0x01, 0x03, 0x05), "charMatcherWithMultipleCharacters"), '\u0000' },
 // Test charMatcher with character greater than ASCII range
 { createCharMatcher(new BitSet().set(0x01, 0x03, 0x05), "charMatcherWithMultipleCharacters"), '\u0080' },
 // Test charMatcher with character less than zero
 { createCharMatcher(new BitSet().set(0x01, 0x03, 0x05), "charMatcherWithMultipleCharacters"), '\uFFFD' },
 // Test charMatcher with characters outside of the char array
 { createCharMatcher(new BitSet().set(0x01, 0x03, 0x05), "charMatcherWithMultipleCharacters"), '\u1F600' },
 // Test charMatcher with an empty character array
 { createCharMatcher(new char[0], "emptyTableCharMatcher"), '\u0001' },
 // Test charMatcher with a character array of size 1
 { createCharMatcher(new char[] { '\u0001' }, "singleTableCharMatcher"), '\u0001' },
 // Test charMatcher with a character array of size greater than MAX_SIZE
 { createCharMatcher(new char[SmallCharMatcher.MAX_SIZE + 1], "largeTableCharMatcher"), '\u0001' },
 // Test charMatcher with a character array containing duplicate characters
 { createCharMatcher(new char[] { '\u0001', '\u0001' }, "duplicateTableCharMatcher"), '\u0001' },
 // Test charMatcher with a character array containing zero
 { createCharMatcher(new char[] { '\u0001', '\u0000' }, "zeroTableCharMatcher"), '\u0001' },
 // Test charMatcher with a character array containing non-ASCII characters
 { createCharMatcher(new char[] { '\u0080', '\u1F600' }, "nonASCITableCharMatcher"), '\u0080' },
 // Test charMatcher with a character array containing characters outside of the table
 { createCharMatcher(new char[] { '\u1F601' }, "outsideTableCharMatcher"), '\u1F601' }
 });
 }

 private static SmallCharMatcher createCharMatcher(BitSet chars, String description) {
 return new SmallCharMatcher(chars.toImmutableBitSet().asBitArray(), description);
 }

 @Test
 public void matches() {
 charMatcher = getParameters()[0];
 character = (char)getParameters()[1][0];
 System.out.println("Character: " + character);
 System.out.println("matcher: " + charMatcher);
 assertTrue(charMatcher.matches(character));
 }

 @Test(expected = AssertionError.class)
 public void mismatches() {
 charMatcher = getParameters()[0];
 character = (char)getParameters()[1][0];
 System.out.println("Character: " + character);
 System.out.println("matcher: " + charMatcher);
 assertFalse(charMatcher.matches(character));
 }

}