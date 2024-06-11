package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 private char[] charArray1 = {'a', 'b', 'a', 'b', 'a'};
 private char[] charArray2 = {'a', 'b', 'c'};
 private char[] charArray3 = {};
 private char[] charArray4 = {'\u0100'};
 private char[] target1 = {'a', 'b'};
 private char[] target2 = {'a', 'b', 'a'};
 private char[] target3 = {'b', 'a'};
 private char[] target4 = {'c'};

 @Test
 public void constructor_validString_expectedSuccess() {
     IndexedString string = new IndexedString("ababa");
     assertNotNull(string);
     assertEquals(5, string.values.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void constructor_nullString_expectedException() {
     assertThrows(IllegalArgumentException.class, () -> new IndexedString(null));
 }

 @Test
 public void constructor_emptyString_expectedSuccess() {
     IndexedString string = new IndexedString("");
     assertEquals(0, string.values.length);
 }

 @Test
 public void count_validCharacter_expectedCorrectCount() {
     IndexedString string = new IndexedString("ababa");
     assertEquals(3, string.count('a'));
 }

 @Test
 public void count_invalidCharacter_expectedZero() {
     IndexedString string = new IndexedString("ababa");
     assertEquals(0, string.count('z'));
 }

 @Test
 public void indexOf_validTarget_expectedCorrectIndex() {
     IndexedString string = new IndexedString("ababaab");
     assertEquals(2, string.indexOf(target1));
 }

 @Test
 public void indexOf_invalidTarget_expectedMinusOne() {
     IndexedString string = new IndexedString("ababaab");
     assertEquals(-1, string.indexOf(target3));
 }

 @Test
 public void indexOf_emptyTarget_expectedMinusOne() {
     IndexedString string = new IndexedString("ababaab");
     assertEquals(-1, string.indexOf(new char[0]));
 }

 @Test
 public void indexOf_targetBeyondArrayBoundaries_expectedMinusOne() {
     IndexedString string = new IndexedString("ababaab");
     assertEquals(-1, string.indexOf(target4));
 }

 @Test
 public void indexOf_targetPartiallyMatches_expectedMinusOne() {
     IndexedString string = new IndexedString("ababaab");
     assertEquals(-1, string.indexOf(target2));
 }

 @Test
 public void indexOf_targetIsSubsequence_expectedCorrectIndex() {
     IndexedString string = new IndexedString("ababaabab");
     assertEquals(1, string.indexOf(target2));
 }

 @Test
 public void indexOf_targetMatchesFirstElement_expectedCorrectIndex() {
     IndexedString string = new IndexedString("ababaab");
     assertEquals(0, string.indexOf(target1));
 }
}