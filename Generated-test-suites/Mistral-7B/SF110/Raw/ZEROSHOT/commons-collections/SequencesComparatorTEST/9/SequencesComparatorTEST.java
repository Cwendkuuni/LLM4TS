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
 sequence2 = new ArrayList<>(Arrays.asList(1, 2, 6, 4, 5));
 equator = new DefaultEquator<>();
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 script = comparator.getScript();
 }

 @Test
 public void testEmptySequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(new ArrayList<>(), new ArrayList<>(), equator);
 assertEquals(new EditScript<>(), comparator.getScript());
 }

 @Test
 public void testSameSequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence1, equator);
 assertEquals(new EditScript<>(), comparator.getScript());
 }

 @Test
 public void testIdentityComparison() {
 equator = new IdentityEquator<>();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 assertEquals(new EditScript<>(), comparator.getScript());
 }

 @Test
 public void testDifferentLengthSequencesWithEqualsElements() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3), equator);
 assertEquals(new EditScript<>(), comparator.getScript());
 }

 @Test
 public void testSequencesWithSameElementsButDifferentOrder() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(3, 2, 1), equator);
 assertEditScriptEquals(new EditScript<>(), script);
 }

 @Test
 public void testSequencesWithDifferentElements() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 5), equator);
 assertEditScriptEquals(new EditScript().append(new InsertCommand<>(4)).append(new InsertCommand<>(5)), script);
 }

 @Test
 public void testSequencesWithEqualElementsButDifferentAmountOfDeletions() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3, 5), Arrays.asList(1, 2, 3), equator);
 assertEditScriptEquals(new EditScript().append(new DeleteCommand<>(5)), script);
 }

 @Test
 public void testSequencesWithEqualElementsButDifferentAmountOfInsertions() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3, 5), equator);
 assertEditScriptEquals(new EditScript().append(new InsertCommand<>(5)), script);
 }

 @Test
 public void testSequencesWithDifferentElementsButSameAmountOfDeletions() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 4), equator);
 assertEditScriptEquals(new EditScript().append(new DeleteCommand<>(2)).append(new DeleteCommand<>(4)).append(new InsertCommand<>(4)), script);
 }

 @Test
 public void testSequencesWithDifferentElementsButSameAmountOfInsertions() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3), Arrays.asList(1, 4, 4), equator);
 assertEditScriptEquals(new EditScript().append(new InsertCommand<>(4)).append(new InsertCommand<>(4)).append(new DeleteCommand<>(2)), script);
 }

 @Test
 public void testSequencesWithComplexEditScript() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1, 2, 3, 5, 6), Arrays.asList(3, 4, 7, 2, 5), equator);
 assertEditScriptEquals(new EditScript().append(new DeleteCommand<>(1)).append(new InsertCommand<>(3)).append(new DeleteCommand<>(1)).append(new InsertCommand<>(4)).append(new DeleteCommand<>(3)).append(new InsertCommand<>(7)).append(new InsertCommand(5)).append(new DeleteCommand<>(2)).append(new DeleteCommand<>(1)), script);
 }

 private void assertEditScriptEquals(EditScript<Integer> expectedScript, EditScript<Integer> actualScript) {
 EditScript<Integer> reversedExpectedScript = new EditScript<>();
 for (Command<Integer> command : expectedScript) {
 reversedExpectedScript.append(command.getReverseCommand());
 }
 assertEquals(reversedExpectedScript, actualScript);
 }

}