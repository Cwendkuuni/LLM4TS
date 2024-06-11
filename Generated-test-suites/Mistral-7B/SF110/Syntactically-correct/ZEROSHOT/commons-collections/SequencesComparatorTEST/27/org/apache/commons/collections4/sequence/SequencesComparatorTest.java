package org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.ChainedPredicate;
import org.apache.commons.collections4.functors.PredicateWrapper;
import org.apache.commons.collections4.seq.EditScript;
import org.apache.commons.collections4.seq.SequencesComparator;

public class SequencesComparatorTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_sameSequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>(sequence1);
 sequencesComparatorTest(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_emptySequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>();
 sequencesComparatorTest(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_identicalSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 sequencesComparatorTest(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_differentSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(3, 2, 1);
 sequencesComparatorTest(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_equalLengthSequencesWithCommonElements() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 2, 3);
 List<Integer> sequence2 = Arrays.asList(3, 2, 1, 2, 3);
 sequencesComparatorTest(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_equalLengthSequencesWithoutCommonElements() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
 List<Integer> sequence2 = Arrays.asList(1, 2, 5, 4);
 sequencesComparatorTest(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_sequencesWithDifferentLengths() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 sequencesComparatorTest(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_sequencesWithNullElements() {
 List<Integer> sequence1 = Arrays.asList(1, null, 3);
 List<Integer> sequence2 = Arrays.asList(1, null, 3, null);
 sequencesComparatorTest(sequence1, sequence2, new NullEquator());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_sequencesWithCustomEquator() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "orange");
 List<String> sequence2 = Arrays.asList("orange", "banana", "apple");
 sequencesComparatorTest(sequence1, sequence2, new CustomEquator());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_getScript_emptySequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>();
 EditScript<Integer> script = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR).getScript();
 assertNull(script);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_getScript_sameSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 EditScript<Integer> script = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR).getScript();
 assertArrayEquals(new KeepCommand(1).getCommands(), script.getCommands());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparator_getScript_differentSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(3, 2, 1);
 EditScript<Integer> script = new SequencesComparator<>(sequence1, sequence2, DefaultEquator.DEFAULT_EQUATOR).getScript();
 List<Command<Integer>> expectedCommands = Arrays.asList(new DeleteCommand(1), new InsertCommand(3), new KeepCommand(2), new KeepCommand(1));
 assertArrayEquals(expectedCommands.toArray(), script.getCommands().toArray());
 }

 private void sequencesComparatorTest(List<Object> sequence1, List<Object> sequence2, Equator<? super Object> equator) {
 List<Object> clonedSequence1 = new ArrayList<>(collectionSize(sequence1));
 Collections.copy(clonedSequence1, sequence1);
 List<Object> clonedSequence2 = new ArrayList<>(collectionSize(sequence2));
 Collections.copy(clonedSequence2, sequence2);
 try {
 new SequencesComparator<>(sequence1, sequence2, equator);
 fail("Exception expected due to different sizes.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 new SequencesComparator<>(clonedSequence1, clonedSequence2, equator);
 }

 private static final Equator<Object> DEFAULT_EQUATOR = new DefaultEquator<>();
 private static final Equator<Object> NULL_EQUATOR = new NullEquator();
 private static final Equator<String> CUSTOM_EQUATOR = new CustomEquator();
 private static final int collectionSize(Collection<?> collection) {
 return (collection == null) ? 0 : collection.size();
 }

 private static class DefaultEquator<T> implements Equator<T> {
 public static final Equator<Object> DEFAULT_EQUATOR = new DefaultEquator<>();

 @Override
 public boolean equate(T o1, T o2) {
 return o1 == o2 || (o1 != null && o1.equals(o2));
 }
 }

 private static class NullEquator implements Equator<Object> {
 @Override
 public boolean equate(Object o1, Object o2) {
 return o1 == null && o2 == null;
 }
 }

 private static class CustomEquator implements Equator<String> {
 @Override
 public boolean equate(String o1, String o2) {
 return o1.equalsIgnoreCase(o2);
 }
 }
}