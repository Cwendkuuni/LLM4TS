package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setUp() {
 sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 sequence2 = Arrays.asList(5, 4, 3, 2, 1);
 }

 @Test
 public void testSequencesComparatorDefaultEquator() {
 comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();

 assertEquals(5, commands.size());
 assertEquals(KeepCommand.class, commands.get(0).getClass());
 assertEquals(DeleteCommand.class, commands.get(1).getClass());
 assertEquals(InsertCommand.class, commands.get(2).getClass());
 assertEquals(KeepCommand.class, commands.get(3).getClass());
 assertEquals(KeepCommand.class, commands.get(4).getClass());

 assertEquals(1, ((KeepCommand<Integer>) commands.get(0)).getElement());
 assertEquals(5, ((DeleteCommand<Integer>) commands.get(1)).getElement());
 assertEquals(5, ((InsertCommand<Integer>) commands.get(2)).getElement());
 assertEquals(2, ((KeepCommand<Integer>) commands.get(3)).getElement());
 assertEquals(4, ((KeepCommand<Integer>) commands.get(4)).getElement());
 }

 @Test
 public void testSequencesComparatorCustomEquator() {
 Equator<Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 return ((Integer) o1).intValue() >= ((Integer) o2).intValue();
 }
 };
 comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.getCommands();

 assertEquals(5, commands.size());
 assertEquals(KeepCommand.class, commands.get(0).getClass());
 assertEquals(DeleteCommand.class, commands.get(1).getClass());
 assertEquals(InsertCommand.class, commands.get(2).getClass());
 assertEquals(KeepCommand.class, commands.get(3).getClass());
 assertEquals(KeepCommand.class, commands.get(4).getClass());

 assertEquals(5, ((KeepCommand<Integer>) commands.get(0)).getElement());
 assertEquals(1, ((DeleteCommand<Integer>) commands.get(1)).getElement());
 assertEquals(1, ((InsertCommand<Integer>) commands.get(2)).getElement());
 assertEquals(4, ((KeepCommand<Integer>) commands.get(3)).getElement());
 assertEquals(3, ((KeepCommand<Integer>) commands.get(4)).getElement());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidInput() {
 comparator = new SequencesComparator<>(Arrays.asList(1, 2), null);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidInput2() {
 comparator = new SequencesComparator<>(null, Arrays.asList(1, 2));
 }
}