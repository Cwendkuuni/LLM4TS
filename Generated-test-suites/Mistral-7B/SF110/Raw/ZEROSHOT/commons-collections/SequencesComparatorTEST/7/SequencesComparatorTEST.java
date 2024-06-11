package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.EditScript;
import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class SequencesComparatorTest {

 private static final Equator<Object> EQUATOR = new Equator<Object>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return o1.equals(o2);
 }
 };

 @Test
 public void testSequencesComparator_equalSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, EQUATOR);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3));
 List<Integer> actual = SequencesComparator.toList(script);

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSequencesComparator_emptySequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, EQUATOR);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = Collections.emptyList();
 List<Integer> actual = SequencesComparator.toList(script);

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSequencesComparator_reverseSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(3, 2, 1));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, EQUATOR);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = new ArrayList<>(Arrays.asList(new DeleteCommand<>(1), new InsertCommand<>(3), new InsertCommand<>(2), new InsertCommand<>(1)));
 List<Integer> actual = SequencesComparator.toList(script);

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSequencesComparator_singleElementDifferenceSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 4));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, EQUATOR);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = new ArrayList<>(Arrays.asList(new DeleteCommand<>(3), new InsertCommand<>(4)));
 List<Integer> actual = SequencesComparator.toList(script);

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSequencesComparator_multipleElementDifferenceSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 4, 5, 2, 3));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, EQUATOR);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> expected = new ArrayList<>(Arrays.asList(new DeleteCommand<>(2), new DeleteCommand<>(3), new InsertCommand<>(4), new InsertCommand<>(5), new InsertCommand<>(2), new InsertCommand<>(3)));
 List<Integer> actual = SequencesComparator.toList(script);

 Assert.assertEquals(expected, actual);
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparator_differentSizeSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, EQUATOR);
 }
}