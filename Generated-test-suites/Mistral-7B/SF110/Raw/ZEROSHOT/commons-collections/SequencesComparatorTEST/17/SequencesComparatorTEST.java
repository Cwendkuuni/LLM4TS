package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;

public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;
 private Equator<Integer> equator;
 private SequencesComparator<Integer> comparator;
 private EditScript<Integer> script;

 @Before
 public void setUp() {
 sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 sequence2 = new ArrayList<>(Arrays.asList(1, 2, 6, 4, 5, 7));
 equator = DefaultEquator.defaultEquator();
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 script = comparator.getScript();
 }

 @Test
 public void testEmptySequences() {
 List<Integer> sequence1 = Collections.emptyList();
 List<Integer> sequence2 = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 assertEquals(new EditScript<Integer>(), comparator.getScript());
 }

 @Test
 public void testEqualSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 assertEquals(new EditScript<Integer>(), comparator.getScript());
 }

 @Test
 public void testDifferentSizeSequencesEqualElements() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 assertEquals(new EditScript<Integer>(), comparator.getScript());
 }

 @Test
 public void testDifferentSizeSequencesDifferentElements() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 5, 6);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new DeleteCommand<>(4));
 expectedScript.append(new InsertCommand<>(5));
 expectedScript.append(new InsertCommand<>(6));
 assertEquals(expectedScript, comparator.getScript());
 }

 @Test
 public void testDiagonalSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(6, 5, 4, 3, 2, 1);
 Equator<Integer> diagEquator = new DiagonalEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, diagEquator);
 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new DeleteCommand<>(1));
 expectedScript.append(new DeleteCommand<>(2));
 expectedScript.append(new DeleteCommand<>(3));
 expectedScript.append(new DeleteCommand<>(4));
 expectedScript.append(new DeleteCommand<>(5));
 expectedScript.append(new InsertCommand<>(1));
 expectedScript.append(new InsertCommand<>(2));
 expectedScript.append(new InsertCommand<>(3));
 expectedScript.append(new InsertCommand<>(4));
 expectedScript.append(new InsertCommand<>(5));
 expectedScript.append(new InsertCommand<>(6));
 expectedScript.append(new InsertCommand<>(5));
 expectedScript.append(new InsertCommand<>(4));
 expectedScript.append(new InsertCommand<>(3));
 expectedScript.append(new InsertCommand<>(2));
 expectedScript.append(new InsertCommand<>(1));
 assertEquals(expectedScript, comparator.getScript());
 }

 @Test(expected = RuntimeException.class)
 public void testInternalError() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 comparator.getScript();
 }

}