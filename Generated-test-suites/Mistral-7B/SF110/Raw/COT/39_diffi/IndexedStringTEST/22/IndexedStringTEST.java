package de.beiri22.stringincrementor.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {

 private char[] array1 = {'a', 'b', 'a', 'b', 'a'};
 private char[] array2 = {'a', 'b', 'c'};
 private char[] array3 = {};
 private char[] array4 = {'a'};
 private char[] array5 = {'\u00e9', '\u00e9', '\u00e9'};
 private char[] target1 = {'a', 'a', 'a'};
 private char[] target2 = {'a', 'b'};
 private char[] target3 = {'c'};
 private char[] target4 = {'a', 'b', 'a'};

 @Test
 public void testCountChar() {
         char c = 'a';
         IndexedString string1 = new IndexedString("aaab");
         IndexedString string2 = new IndexedString("abc");
         IndexedString string3 = new IndexedString("");
         IndexedString string4 = new IndexedString("a");
         IndexedString string5 = new IndexedString("ééé");

         assertEquals(4, string1.count(c));
         assertEquals(1, string2.count(c));
         assertEquals(0, string3.count(c));
         assertEquals(1, string4.count(c));
         assertEquals(3, string5.count(c));
     }

 @Test
 public void testIndexedStringConstructor() {
         String s1 = "aaab";
         String s2 = "abc";
         String s3 = "";
         String s4 = "a";
         String s5 = "ééé";

         IndexedString indexedString1 = new IndexedString(s1);
         IndexedString indexedString2 = new IndexedString(s2);
         IndexedString indexedString3 = new IndexedString(s3);
         IndexedString indexedString4 = new IndexedString(s4);
         IndexedString indexedString5 = new IndexedString(s5);

         assertEquals(5, indexedString1.values.length);
         assertEquals(3, indexedString2.values.length);
         assertEquals(0, indexedString3.values.length);
         assertEquals(1, indexedString4.values.length);
         assertEquals(3, indexedString5.values.length);
     }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexedStringConstructorNull() {
         IndexedString indexedString = new IndexedString(null);
     }

 @Test
 public void testIndexOf() {
         IndexedString indexedString1 = new IndexedString("aaab");
         IndexedString indexedString2 = new IndexedString("abc");
         IndexedString indexedString3 = new IndexedString("");
         IndexedString indexedString4 = new IndexedString("a");
         IndexedString indexedString5 = new IndexedString("ééé");

         // Case 1: Target found at the beginning of the string
         assertEquals(0, indexedString1.indexOf(target1));

         // Case 2: Target found in the middle of the string
         assertEquals(2, indexedString1.indexOf(target2));

         // Case 3: Target not found in the string
         assertNull(indexedString1.indexOf(target3));

         // Case 4: Target overlaps with the end of the string
         assertEquals(4, indexedString1.indexOf(target4));

         // Edge case: Target with the same characters as the string
         assertEquals(0, indexedString4.indexOf(array4));

         // Edge case: Target with a single character not present in the string
         assertNull(indexedString2.indexOf(new char[]{'d'}));

         // Test exception handling
         String s1Empty = "";
         IndexedString emptyIndexedString = new IndexedString(s1Empty);
         assertNull(emptyIndexedString.indexOf(array1));
         assertEquals(-1, emptyIndexedString.indexOf(target1));
     }
}