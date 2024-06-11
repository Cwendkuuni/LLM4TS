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
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 list2 = new ArrayList<>(Arrays.asList(1, 3, 4, 2, 5));
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testConstructorWithBothListsAndDefaultEquator() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 assertNotNull(comparator);
 }

 @Test
 public void testConstructorWithBothListsAndCustomEquator() {
 Equator<Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object obj1, Object obj2) {
 return obj1.equals(obj2) || (obj1 instanceof Integer && obj2 instanceof Integer && obj1.intValue() + 1 == obj2.intValue());
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, customEquator);
 assertNotNull(comparator);
 }

 @Test
 public void testGetScriptWithEqualSequences() {
 EditScript<Integer> script = comparator.getScript();
 assertNotNull(script);
 assertEquals(0, script.size());
 }

 @Test
 public void testGetScriptWithDifferentSequences() {
 EditScript<Integer> script = comparator.getScript();
 assertNotNull(script);
 assertFalse(script.isEmpty());
 }

 @Test(expected = RuntimeException.class)
 public void testGetScriptWithNullSequence1() {
 List<Integer> nullList1 = null;
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(nullList1, list2);
 comparator.getScript();
 fail("Expected RuntimeException but it did not occur.");
 }

 @Test(expected = RuntimeException.class)
 public void testGetScriptWithNullSequence2() {
 List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> nullList2 = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, nullList2);
 comparator.getScript();
 fail("Expected RuntimeException but it did not occur.");
 }

 @Test(expected = RuntimeException.class)
 public void testGetScriptWithNullEquator() {
 List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> list2 = Arrays.asList(1, 3, 4, 2, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2, null);
 comparator.getScript();
 fail("Expected RuntimeException but it did not occur.");
 }

 @Test
 public void testBuildScriptWithEqualSequences() {
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 }

 @Test
 public void testBuildScriptWithDifferentSequences() {
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 EditScript<Integer> script = comparator.getScript();
 comparator.buildScript(start1, end1, start2, end2, script);
 assertFalse(script.isEmpty());
 }

 @Test(expected = RuntimeException.class)
 public void testBuildScriptWithNullScript() {
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 fail("Expected RuntimeException but it did not occur.");
 }

 @Test
 public void testBuildScriptWithNullSequence1() {
 List<Integer> nullList1 = null;
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 fail("Expected RuntimeException but it did not occur.");
 }

 @Test
 public void testBuildScriptWithNullSequence2() {
 List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> nullList2 = null;
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 fail("Expected RuntimeException but it did not occur.");
 }

 @Test(expected = RuntimeException.class)
 public void testBuildScriptWithNullEquator() {
 List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> list2 = Arrays.asList(1, 3, 4, 2, 5);
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 fail("Expected RuntimeException but it did not occur.");
 }

 @Test
 public void testBuildScriptWithEdgeCases() {
 List<Integer> list1 = Arrays.asList(1, 1, 1, 1, 1);
 List<Integer> list2 = Arrays.asList(1, 1, 1, 1, 1);
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 assertFalse(script.isEmpty());
 }

 @Test
 public void testBuildScriptWithEmptySequences() {
 List<Integer> list1 = new ArrayList<>();
 List<Integer> list2 = new ArrayList<>();
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 assertFalse(script.isEmpty());
 }

 @Test
 public void testBuildScriptWithSequencesInReverseOrder() {
 List<Integer> list1 = Arrays.asList(5, 4, 3, 2, 1);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 assertFalse(script.isEmpty());
 }

 @Test
 public void testBuildScriptWithSequencesContainingDuplicates() {
 List<Integer> list1 = Arrays.asList(1, 2, 2, 3, 4, 4, 5);
 List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5, 5, 5);
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 assertFalse(script.isEmpty());
 }

 @Test
 public void testBuildScriptWithSequencesContainingNulls() {
 List<Integer> list1 = Arrays.asList(1, null, 3, 4, 5);
 List<Integer> list2 = Arrays.asList(1, 2, 3, null, 5);
 int start1 = 0;
 int end1 = list1.size();
 int start2 = 0;
 int end2 = list2.size();
 comparator.buildScript(start1, end1, start2, end2, null);
 assertFalse(script.isEmpty());
 }

}