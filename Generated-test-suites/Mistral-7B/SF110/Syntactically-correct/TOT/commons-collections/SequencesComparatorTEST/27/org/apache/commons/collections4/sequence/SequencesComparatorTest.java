import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.iterators.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SequencesComparatorTest {

 @Test
 public void testEqualsSequences() {
 List<Integer> list1 = new ArrayList<>();
 List<Integer> list2 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 list1.add(3);
 list2.add(1);
 list2.add(2);
 list2.add(3);

 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();

 assertEquals(script.size(), 0);
 }

 @Test
 public void testUnequalSequencesWithSameLength() {
 List<Integer> list1 = new ArrayList<>();
 List<Integer> list2 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 list1.add(3);
 list2.add(1);
 list2.add(4);
 list2.add(3);

 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();

 assertEquals(script.size(), 1);
 assertEquals(script.getCommand(0), new DeleteCommand<>(1));
 }

 @Test
 public void testUnequalSequencesWithDifferentLength() {
 List<Integer> list1 = new ArrayList<>();
 List<Integer> list2 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 list1.add(3);
 list2.add(1);
 list2.add(2);
 list2.add(3);
 list2.add(4);

 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();

 assertEquals(script.size(), 1);
 assertEquals(script.getCommand(0), new InsertCommand<>(4));
 }

 @Test(expected = RuntimeException.class)
 public void testDifferentDataTypes() {
 List<Integer> list1 = new ArrayList<>();
 List<String> list2 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 list1.add(3);
 list2.add("a");
 list2.add("b");
 list2.add("c");

 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLists() {
 List<Integer> list1 = null;
 List<Integer> list2 = null;
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyLists() {
 List<Integer> list1 = new ArrayList<>();
 List<Integer> list2 = new ArrayList<>();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMismatchedListSizes() {
 List<Integer> list1 = new ArrayList<>();
 List<Integer> list2 = new ArrayList<>();
 list1.add(1);
 list1.add(2);
 list1.add(3);
 list2.add(1);
 list2.add(2);
 list2.add(4);

 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 }
}