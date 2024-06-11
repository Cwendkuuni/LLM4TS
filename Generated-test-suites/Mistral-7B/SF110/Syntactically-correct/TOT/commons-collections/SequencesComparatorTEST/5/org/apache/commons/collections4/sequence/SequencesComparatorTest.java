import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;
 private List<Integer> sequence3;
 private List<Integer> sequence4;
 private Equator<Integer> equator;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setUp() {
 sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 sequence2 = Arrays.asList(5, 4, 3, 2, 1);
 sequence3 = Arrays.asList(1, 2, 3);
 sequence4 = Arrays.asList();
 equator = DefaultEquator.defaultEquator();
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }

 @Test
 public void testGetScript_SameSequences() {
 final EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testGetScript_DifferentSequences_DiagonalSnake() {
 sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 sequence2 = Arrays.asList(5, 4, 3, 2, 1);
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 final EditScript<Integer> script = comparator.getScript();
 List<Command<Integer>> commands = script.getCommands();
 assertEquals(5, commands.size());
 for (int i = 0; i < 5; ++i) {
 if (i % 2 == 0) {
 assertEquals(KeepCommand.class, commands.get(i).getClass());
 } else {
 assertEquals(InsertCommand.class, commands.get(i).getClass());
 }
 }
 }

 @Test
 public void testGetScript_DifferentSequences_VerticalSnake() {
 sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 sequence2 = Arrays.asList(5, 4, 3, 2, 1);
 comparator = new SequencesComparator<>(sequence1, sequence2, new Equator<Integer>() {
 @Override
 public boolean equate(final Object o1, final Object o2) {
 return o1.equals(o2) || o1.intValue() == o2.intValue() && Math.abs(o1.intValue() - o2.intValue()) == 1;
 }
 });
 final EditScript<Integer> script = comparator.getScript();
 List<Command<Integer>> commands = script.getCommands();
 assertEquals(6, commands.size());
 for (int i = 0; i < 6; ++i) {
 if (i % 2 == 0) {
 assertEquals(KeepCommand.class, commands.get(i).getClass());
 } else {
 assertEquals(DeleteCommand.class, commands.get(i).getClass());
 }
 }
 }

 @Test(expected = RuntimeException.class)
 public void testGetScript_DifferentSequences_Exception() {
 sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 sequence2 = Arrays.asList(5, 4, 3, 2, 1);
 comparator = new SequencesComparator<>(sequence1, sequence2, new Equator<Integer>() {
 @Override
 public boolean equate(final Object o1, final Object o2) {
 return false;
 }
 });
 }

 @Test
 public void testGetScript_EmptySequence() {
 sequence1 = Arrays.asList();
 sequence2 = Arrays.asList();
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 final EditScript<Integer> script = comparator.getScript();
 assertTrue(script.isEmpty());
 }

 @Test
 public void testGetScript_EmptyAndNonEmptySequences() {
 sequence1 = Arrays.asList();
 sequence2 = Arrays.asList(1);
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 final EditScript<Integer> script = comparator.getScript();
 List<Command<Integer>> commands = script.getCommands();
 assertEquals(1, commands.size());
 assertEquals(DeleteCommand.class, commands.get(0).getClass());
 }
}