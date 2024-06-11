package org.junit.runner;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.sequences.EditScript;
import org.apache.commons.collections4.sequences.SequencesComparator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setup() {
 list1 = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
 list2 = new ArrayList<>(Arrays.asList(2, 4, 6, 8, 10));
 }

 @Test
 public void testSequencesComparatorDefaultEquator() {
 comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<Integer> resultList = Sequences.editOperationsToList(script);
 List<Integer> expectedList = Arrays.asList(1, 4, 3, 5, 7, 6, 9, 8, 10);
 assertEquals(expectedList, resultList);
 }

 @Test
 public void testSequencesComparatorCustomEquator() {
 Equator<Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object a, Object b) {
 return (Integer)a == (Integer)b - 1;
 }
 };
 comparator = new SequencesComparator<>(list1, list2, customEquator);
 EditScript<Integer> script = comparator.getScript();
 List<Integer> resultList = Sequences.editOperationsToList(script);
 List<Integer> expectedList = Arrays.asList(1, 2, 4, 5, 6, 3, 7, 8, 9, 10);
 assertEquals(expectedList, resultList);
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorInternalError() {
 List<String> stringList1 = new ArrayList<>(Arrays.asList("a", "b", "c"));
 List<String> stringList2 = new ArrayList<>(Arrays.asList("x", "y", "z"));
 comparator = new SequencesComparator<>(stringList1, stringList2);
 comparator.getScript();
 }

 @Test
 public void testSequencesComparatorEmptyLists() {
 List<String> emptyList1 = Collections.emptyList();
 List<String> emptyList2 = Collections.emptyList();
 comparator = new SequencesComparator<>(emptyList1, emptyList2);
 EditScript<String> script = comparator.getScript();
 List<String> expectedList = Collections.emptyList();
 List<String> resultList = Sequences.editOperationsToList(script);
 assertEquals(expectedList, resultList);
 }

 @Test
 public void testSequencesComparatorNullLists() {
 List<String> nullList1 = null;
 List<String> nullList2 = null;
 comparator = new SequencesComparator<>(nullList1, nullList2);
 EditScript<String> script = comparator.getScript();
 List<String> expectedList = Collections.emptyList();
 List<String> resultList = Sequences.editOperationsToList(script);
 assertEquals(expectedList, resultList);
 }

 @Test
 public void testSequencesComparatorIdentityEquator() {
 Equator<Integer> identityEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object a, Object b) {
 return a.equals(b);
 }
 };
 comparator = new SequencesComparator<>(list1, list2, identityEquator);
 EditScript<Integer> script = comparator.getScript();
 List<Integer> resultList = Sequences.editOperationsToList(script);
 List<Integer> expectedList = ListUtils.difference(list1, list2);
 assertEquals(expectedList, resultList);
 }
}