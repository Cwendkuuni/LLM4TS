package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setup() {
 list1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<Integer>(Arrays.asList(5, 4, 3, 2, 1));
 comparator = new SequencesComparator<Integer>(list1, list2);
 }

 @Test
 public void testConstructorWithLists() {
 SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
 assertNotNull(comparator.sequence1);
 assertNotNull(comparator.sequence2);
 assertNotNull(comparator.equator);
 assertNotNull(comparator.vDown);
 assertNotNull(comparator.vUp);
 }

 @Test
 public void testConstructorWithListsAndEquator() {
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals(o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2, equator);
 assertNotNull(comparator.sequence1);
 assertNotNull(comparator.sequence2);
 assertNotNull(comparator.equator);
 assertNotNull(comparator.vDown);
 assertNotNull(comparator.vUp);
 }

 @Test
 public void testGetScript() {
 EditScript<Integer> script = comparator.getScript();
 assertNotNull(script);
 }

 @Test(expected = RuntimeException.class)
 public void testGetScriptWithException() {
 SequencesComparator<String> comparator = new SequencesComparator<String>(new ArrayList<String>(), new ArrayList<String>());
 EditScript<String> script = comparator.getScript();
 }

 @Test
 public void testBuildScriptEdgeCases() {
 List<Integer> list1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> list2 = new ArrayList<Integer>(Arrays.asList(5, 4, 3, 2, 1));
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer)o1).equals(o2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2, equator);
 EditScript<Integer> script = comparator.getScript();
 // Test when sequence1 is empty
 list1 = new ArrayList<Integer>();
 comparator = new SequencesComparator<Integer>(list1, list2, equator);
 script = comparator.getScript();
 // Test when sequence2 is empty
 list2 = new ArrayList<Integer>();
 comparator = new SequencesComparator<Integer>(list1, list2, equator);
 script = comparator.getScript();
 // Test when both sequences are empty
 list1 = new ArrayList<Integer>();
 list2 = new ArrayList<Integer>();
 comparator = new SequencesComparator<Integer>(list1, list2, equator);
 script = comparator.getScript();
 // Test when both sequences are equal
 list1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
 comparator = new SequencesComparator<Integer>(list1, list2, equator);
 script = comparator.getScript();
 // Test when sequences are reversed
 list2 = new ArrayList<Integer>(Arrays.asList(5, 4, 3, 2, 1));
 list1 = new ArrayList<Integer>(Collections.reverseOrder(list2));
 comparator = new SequencesComparator<Integer>(list1, list2, equator);
 script = comparator.getScript();
 }

 @Test
 public void testBuildScriptException() {
 List<String> list1 = new ArrayList<String>();
 List<String> list2 = new ArrayList<String>();
 SequencesComparator<String> comparator = new SequencesComparator<String>(list1, list2);
 try {
 comparator.getScript();
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testBuildScriptNullPointer() {
 List<Integer> list1 = null;
 List<Integer> list2 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
 try {
 comparator.getScript();
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testBuildScriptIndexOutOfBounds() {
 List<Integer> list1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> list2 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
 SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
 try {
 comparator.getScript();
 fail("Expected RuntimeException");
 } catch (RuntimeException e) {
 // Expected
 }
 // Modify sequence1 to have a smaller size
 list1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
 comparator = new SequencesComparator<Integer>(list1, list2);
 try {
 comparator.getScript();
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 // Modify sequence2 to have a smaller size
 list2 = new ArrayList<Integer>(Arrays.asList(1, 2));
 comparator = new SequencesComparator<Integer>(list1, list2);
 try {
 comparator.getScript();
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 // Modify both sequences to have smaller size
 list1 = new ArrayList<Integer>(Arrays.asList(1));
 list2 = new ArrayList<Integer>();
 comparator = new SequencesComparator<Integer>(list1, list2);
 try {
 comparator.getScript();
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

}