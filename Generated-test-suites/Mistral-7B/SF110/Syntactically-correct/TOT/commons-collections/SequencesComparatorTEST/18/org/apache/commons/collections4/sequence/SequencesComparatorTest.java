import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.iterator.EditScriptIterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private List<Integer> expectedResult;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setUp() {
 list1 = Arrays.asList(1, 2, 3, 4, 5);
 list2 = Arrays.asList(1, 2, 4, 5, 3);
 expectedResult = Arrays.asList(1, 1, 2, 2, 3, 4, 4, 5, 5);
 }

 @Test
 public void testSequencesComparatorWithSameLists() {
 comparator = new SequencesComparator<>(list1, list1);
 assertEquals(expectedResult, createAndExecuteScript(comparator.getScript()));
 }

 @Test
 public void testSequencesComparatorWithEqualSequencesButDifferentOrders() {
 comparator = new SequencesComparator<>(list1, ListUtils.reverse(list1));
 assertEquals(expectedResult, createAndExecuteScript(comparator.getScript()));
 }

 @Test
 public void testSequencesComparatorWithDifferentSequences() {
 comparator = new SequencesComparator<>(list1, list2);
 assertEquals(expectedResult, createAndExecuteScript(comparator.getScript()));
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorWithDifferentLengths() {
 List<Integer> list3 = Arrays.asList(1, 2, 3);
 comparator = new SequencesComparator<>(list1, list3);
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorWithNullLists() {
 comparator = new SequencesComparator<>(null, list1);
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorWithNullEquator() {
 comparator = new SequencesComparator<>(list1, list2, null);
 }

 private EditScript<Integer> createAndExecuteScript(EditScript<Integer> script) {
 EditScriptIterator<Integer> iterator = new EditScriptIterator<Integer>(script);
 List<Integer> result = new ArrayList<Integer>();
 while (iterator.hasNext()) {
 result.add(iterator.next().getObject());
 }
 return script;
 }

}