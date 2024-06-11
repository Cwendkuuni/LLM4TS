package org.junit.Test;

import static org.junit.Assert.*;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.seq.EditScript;
import org.apache.commons.collections4.seq.SequencesComparator;
import org.apache.commons.collections4.seq.KeepCommand;
import org.apache.commons.collections4.seq.DeleteCommand;
import org.apache.commons.collections4.seq.InsertCommand;
import java.util.*;

@SuppressWarnings("unchecked")
public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private List<Integer> expectedResult;

 @Before
 public void setUp() {
 list1 = Arrays.asList(1, 2, 3, 4, 5);
 list2 = Arrays.asList(4, 5, 2, 1, 3);
 expectedResult = Arrays.asList(1, 2, 4, 5, 3);
 }

 @Test
 public void testSimpleComparator() {
 SequencesComparator<Integer> comparator = new SequencesComparator<Integer>(list1, list2);
 Equator<Integer> defaultEquator = DefaultEquator.defaultEquator();
 EditScript<Integer> script = comparator.getScript();

 List<Integer> result = new ArrayList<>();
 for (Object cmd : script) {
 if (cmd instanceof KeepCommand) {
 result.add(((KeepCommand<Integer>) cmd).getObject());
 } else if (cmd instanceof InsertCommand) {
 result.add(((InsertCommand<Integer>) cmd).getObject());
 } else if (cmd instanceof DeleteCommand) {
 // do nothing, just remove the element from list1
 list1.remove(((DeleteCommand<Integer>) cmd).getIndex());
 }
 }

 Collections.sort(result, Collections.singletonList(Collections.reverseOrder()));
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSequence1() {
 new SequencesComparator<Integer>(null, list2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSequence2() {
 new SequencesComparator<Integer>(list1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSameSequence() {
 new SequencesComparator<Integer>(list1, list1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testZeroSizeSequence1() {
 new SequencesComparator<Integer>(Collections.emptyList(), list2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testZeroSizeSequence2() {
 new SequencesComparator<Integer>(list1, Collections.emptyList());
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildScriptWithNullEquator() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
 List<String> sequence2 = Arrays.asList("banana", "cherry", "apple");
 SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2);
 EditScript<String> script = comparator.getScript();
 comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);
 }

 @Test
 public void testCustomComparator() {
 Equator<String> customEquator = new Equator<String>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return o1.toString().compareToIgnoreCase(o2.toString()) == 0;
 }
 };
 List<String> sequence1 = Arrays.asList("apple", "banana", "cherry");
 List<String> sequence2 = Arrays.asList("cherry", "banana", "apple");
 SequencesComparator<String> comparator = new SequencesComparator<String>(sequence1, sequence2, customEquator);
 EditScript<String> script = comparator.getScript();

 List<String> result = new ArrayList<>();
 for (Object cmd : script) {
 if (cmd instanceof KeepCommand) {
 result.add(((KeepCommand<String>) cmd).getObject());
 } else if (cmd instanceof InsertCommand) {
 result.add(((InsertCommand<String>) cmd).getObject());
 } else if (cmd instanceof DeleteCommand) {
 // do nothing, just remove the element from sequence1
 sequence1.remove((Integer) ((DeleteCommand<String>) cmd).getIndex());
 }
 }

 Collections.sort(result);
 assertEquals(Arrays.asList("apple", "banana", "cherry"), result);
 }
}