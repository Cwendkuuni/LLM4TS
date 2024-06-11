package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.beiri22.stringincrementor.helper.IndexedString;

@RunWith(JUnit4.class)
public class IndexedStringTest {

 private static final char[] EMPTY_CHAR_ARRAY = new char[0];
 private static final char[] CHAR_ARRAY_1 = new char[]{'a'};
 private static final char[] CHAR_ARRAY_2 = new char[]{'a', 'b'};
 private static final char[] CHAR_ARRAY_3 = new char[]{'a', 'a'};
 private static final char[] CHAR_ARRAY_4 = new char[]{'a', 'b', 'a'};

 @Test
 public void testConstructor() throws InstantiationException, IllegalAccessException, InvocationTargetException {
     String input = "abba";
     IndexedString indexedString = new IndexedString(input);
     assertEquals(input.length(), indexedString.values.length);
     for (int i = 0; i < input.length(); ++i) {
         assertEquals(input.charAt(i), indexedString.values[i]);
     }
 }

 @Test
 public void testIndexOf_EmptyString() {
     IndexedString indexedString = new IndexedString("abba");
     assertNull(indexedString.indexOf(EMPTY_CHAR_ARRAY));
 }

 @Test
 public void testIndexOf_SingleChar() {
     IndexedString indexedString = new IndexedString("abba");
     assertEquals(0, indexedString.indexOf(CHAR_ARRAY_1));
 }

 @Test
 public void testIndexOf_CharNotPresent() {
     IndexedString indexedString = new IndexedString("abba");
     assertNull(indexedString.indexOf(CHAR_ARRAY_2));
 }

 @Test
 public void testIndexOf_SubStringPresent() {
     IndexedString indexedString = new IndexedString("abba");
     assertEquals(2, indexedString.indexOf(CHAR_ARRAY_3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOf_TargetLongerThanString() {
     IndexedString indexedString = new IndexedString("abba");
     char[] targetLongerThanString = new char[6];
     Arrays.fill(targetLongerThanString, 'a');
     indexedString.indexOf(targetLongerThanString);
 }

 @Test
 public void testIndexOf_SubStringPresentAtEnd() {
     IndexedString indexedString = new IndexedString("abba");
     assertEquals(4, indexedString.indexOf(CHAR_ARRAY_4));
 }

 @Test
 public void testIndexOf_SubStringPresentAtBeginning() {
     IndexedString indexedString = new IndexedString("abba");
     assertEquals(0, indexedString.indexOf(CHAR_ARRAY_1));
     assertEquals(0, indexedString.indexOf(CHAR_ARRAY_2));
     assertEquals(0, indexedString.indexOf(CHAR_ARRAY_3));
     assertEquals(0, indexedString.indexOf(CHAR_ARRAY_4));
 }

 @Test
 public void testIndexOf_SubStringPresentInMiddle() {
     IndexedString indexedString = new IndexedString("abba");
     assertEquals(2, indexedString.indexOf(CHAR_ARRAY_3));
     assertEquals(3, indexedString.indexOf(CHAR_ARRAY_4));
 }
}