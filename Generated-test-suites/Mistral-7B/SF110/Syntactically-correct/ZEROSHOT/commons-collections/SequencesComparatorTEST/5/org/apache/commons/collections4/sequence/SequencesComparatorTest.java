package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.Functor;
import org.apache.commons.collections4.iterators.EditScript;
import org.apache.commons.collections4.iterators.KeepCommand;
import org.apache.commons.collections4.iterators.InsertCommand;
import org.apache.commons.collections4.iterators.DeleteCommand;
import org.apache.commons.collections4.iterators.Snake;
import org.apache.commons.collections4.eq.Equator;
import org.apache.commons.collections4.list.ArrayListMultimap;
import org.apache.commons.collections4.test.eq.DefaultEquator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;
 private SequencesComparator<Integer> comparator;
 private EditScript<Integer> script;
 private Equator<Integer> equator;

 @Before
 public void setUp() {
 sequence1 = new ArrayList<>();
 sequence1.add(1);
 sequence1.add(2);
 sequence1.add(3);
 sequence1.add(4);
 sequence1.add(5);

 sequence2 = new ArrayList<>();
 sequence2.add(1);
 sequence2.add(2);
 sequence2.add(3);
 sequence2.add(6);
 sequence2.add(7);
 sequence2.add(4);

 equator = new DefaultEquator<>();
 }

 @Test
 public void testSequencesComparatorEmpty() {
 List<Integer> seq1 = Collections.emptyList();
 List<Integer> seq2 = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 Equator<Integer> equator = new DefaultEquator<>();
 EditScript<Integer> script = comparator.getScript();

 assertTrue(script.isEmpty());
 }

 @Test
 public void testSequencesComparatorIdentity() {
 List<Integer> seq1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> seq2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 Equator<Integer> equator = new DefaultEquator<>();
 EditScript<Integer> script = comparator.getScript();

 assertTrue(script.isEmpty());
 }

 @Test
 public void testSequencesComparatorSameOrder() {
 List<Integer> seq1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> seq2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 Equator<Integer> equator = new DefaultEquator<>();
 EditScript<Integer> script = comparator.getScript();

 assertEquals(script.size(), 0);
 }

 @Test
 public void testSequencesComparatorDifferentOrder() {
 List<Integer> seq1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> seq2 = Arrays.asList(5, 4, 3, 2, 1);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 Equator<Integer> equator = new DefaultEquator<>();
 EditScript<Integer> script = comparator.getScript();

 ArrayListMultimap<Class<?>, Command<Integer>> commands = ArrayListMultimap.decorate(new HashMap<>());
 while (!script.isEmpty()) {
 Commands:
 switch (script.nextCommand()) {
 case KEEP:
 Commands keep = (KeepCommand<Integer>) script.getCommand();
 commands.put(KeepCommand.class, keep);
 break;
 case DELETE:
 Commands delete = (DeleteCommand<Integer>) script.getCommand();
 commands.put(DeleteCommand.class, delete);
 break;
 case INSERT:
 Commands insert = (InsertCommand<Integer>) script.getCommand();
 commands.put(InsertCommand.class, insert);
 break;
 default:
 throw new AssertionError("Unexpected command encountered: " + script.nextCommand());
 }
 }

 assertEquals(commands.size(), 5);
 List<Command<Integer>> keepCommands = commands.get(KeepCommand.class);
 List<Command<Integer>> deleteCommands = commands.get(DeleteCommand.class);
 List<Command<Integer>> insertCommands = commands.get(InsertCommand.class);

 assertEquals(keepCommands.size(), 5);
 assertEquals(deleteCommands.size(), 1);
 assertEquals(insertCommands.size(), 1);

 assertEquals(keepCommands.get(0).getObject(), 1);
 assertEquals(keepCommands.get(1).getObject(), 2);
 assertEquals(keepCommands.get(2).getObject(), 3);
 assertEquals(keepCommands.get(3).getObject(), 4);
 assertEquals(keepCommands.get(4).getObject(), 5);

 assertEquals(deleteCommands.get(0).getIndex(), 1);
 assertEquals(deleteCommands.get(0).getObject(), 2);

 assertEquals(insertCommands.get(0).getIndex(), 1);
 assertEquals(insertCommands.get(0).getObject(), 6);
 }

 @Test
 public void testSequencesComparatorSameElementsDifferentOrderInverse() {
 List<Integer> seq1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> seq2 = Arrays.asList(5, 4, 3, 2, 1);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 Equator<Integer> equator = new DefaultEquator<>();
 EditScript<Integer> script = comparator.getScript();

 ArrayListMultimap<Class<?>, Command<Integer>> commands = ArrayListMultimap.decorate(new HashMap<>());
 while (!script.isEmpty()) {
 Commands:
 switch (script.nextCommand()) {
 case KEEP:
 Commands keep = (KeepCommand<Integer>) script.getCommand();
 commands.put(KeepCommand.class, keep);
 break;
 case DELETE:
 Commands delete = (DeleteCommand<Integer>) script.getCommand();
 commands.put(DeleteCommand.class, delete);
 break;
 case INSERT:
 Commands insert = (InsertCommand<Integer>) script.getCommand();
 commands.put(InsertCommand.class, insert);
 break;
 default:
 throw new AssertionError("Unexpected command encountered: " + script.nextCommand());
 }
 }

 assertEquals(commands.size(), 5);
 List<Command<Integer>> keepCommands = commands.get(KeepCommand.class);
 List<Command<Integer>> deleteCommands = commands.get(DeleteCommand.class);
 List<Command<Integer>> insertCommands = commands.get(InsertCommand.class);

 assertEquals(keepCommands.size(), 5);
 assertEquals(deleteCommands.size(), 1);
 assertEquals(insertCommands.size(), 1);

 assertEquals(keepCommands.get(0).getObject(), 1);
 assertEquals(keepCommands.get(1).getObject(), 3);
 assertEquals(keepCommands.get(2).getObject(), 2);
 assertEquals(keepCommands.get(3).getObject(), 4);
 assertEquals(keepCommands.get(4).getObject(), 5);

 assertEquals(deleteCommands.get(0).getIndex(), 1);
 assertEquals(deleteCommands.get(0).getObject(), 2);

 assertEquals(insertCommands.get(0).getIndex(), 1);
 assertEquals(insertCommands.get(0).getObject(), 5);
 }

 @Test
 public void testSequencesComparatorDifferentElements() {
 List<Integer> seq1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> seq2 = Arrays.asList(1, 2, 6, 7, 8);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 Equator<Integer> equator = new DefaultEquator<>();
 EditScript<Integer> script = comparator.getScript();

 ArrayListMultimap<Class<?>, Command<Integer>> commands = ArrayListMultimap.decorate(new HashMap<>());
 while (!script.isEmpty()) {
 Commands:
 switch (script.nextCommand()) {
 case KEEP:
 Commands keep = (KeepCommand<Integer>) script.getCommand();
 commands.put(KeepCommand.class, keep);
 break;
 case DELETE:
 Commands delete = (DeleteCommand<Integer>) script.getCommand();
 commands.put(DeleteCommand.class, delete);
 break;
 case INSERT:
 Commands insert = (InsertCommand<Integer>) script.getCommand();
 commands.put(InsertCommand.class, insert);
 break;
 default:
 throw new AssertionError("Unexpected command encountered: " + script.nextCommand());
 }
 }

 assertEquals(commands.size(), 5);
 List<Command<Integer>> keepCommands = commands.get(KeepCommand.class);
 List<Command<Integer>> deleteCommands = commands.get(DeleteCommand.class);
 List<Command<Integer>> insertCommands = commands.get(InsertCommand.class);

 assertEquals(keepCommands.size(), 2);
 assertEquals(deleteCommands.size(), 3);
 assertEquals(insertCommands.size(), 0);

 assertEquals(keepCommands.get(0).getObject(), 1);
 assertEquals(keepCommands.get(1).getObject(), 4);

 assertEquals(deleteCommands.get(0).getIndex(), 1);
 assertEquals(deleteCommands.get(0).getObject(), 2);
 assertEquals(deleteCommands.get(1).getIndex(), 3);
 assertEquals(deleteCommands.get(1).getObject(), 5);
 assertEquals(deleteCommands.get(2).getIndex(), 4);
 assertEquals(deleteCommands.get(2).getObject(), 6);
 }

 @Test
 public void testSequencesComparatorNullSequence() {
 List<Integer> seq1 = null;
 List<Integer> seq2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 assertNull(comparator.getScript());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorNullSequence2() {
 List<Integer> seq1 = null;
 List<Integer> seq2 = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorNullEquator() {
 List<Integer> seq1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> seq2 = Arrays.asList(1, 2, 3, 4, 5);
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorEmptyLists() {
 List<Integer> seq1 = Collections.emptyList();
 List<Integer> seq2 = Collections.emptyList();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(seq1, seq2);
 }
}