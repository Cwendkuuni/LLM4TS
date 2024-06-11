import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.iterators.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SequencesComparatorTest {

 private List<Integer> list1;
 private List<Integer> list2;
 private List<Integer> expectedResult;
 private SequencesComparator<Integer>comparator;

 @Before
 public void setUp() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
 list2 = new ArrayList<>(Arrays.asList(1, 3, 2, 4));
 expectedResult = new ArrayList<>(Arrays.asList(1, 1, 2, 3, 3, 4, 4));
 }

 @Test
 public void testSequencesComparator_SameOrder() {
 comparator = new SequencesComparator<>(list1, list1);
 assertEquals(expectedResult, comparator.getScript().apply(list1));
 }

 @Test
 public void testSequencesComparator_DifferentOrders_DiagonalSnake() {
 comparator = new SequencesComparator<>(list1, list2);
 assertEquals(expectedResult, comparator.getScript().apply(list1));
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparator_DifferentSizes() {
 list2.add(5);
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test(expected = NullPointerException.class)
 public void testSequencesComparator_NullLists() {
 comparator = new SequencesComparator<>(null, list2);
 comparator = new SequencesComparator<>(list1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparator_EmptyLists() {
 list1.clear();
 list2.clear();
 comparator = new SequencesComparator<>(list1, list2);
 }

 @Test
 public void testSequencesComparator_EqualElements_DifferentPositions() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 1, 3, 1, 4));
 list2 = new ArrayList<>(Arrays.asList(1, 3, 1, 2, 1, 4));
 comparator = new SequencesComparator<>(list1, list2);
 expectedResult = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 2, 3, 3, 4, 4));
 assertEquals(expectedResult, comparator.getScript().apply(list1));
 }

 @Test
 public void testSequencesComparator_DifferentElements() {
 list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
 list2 = new ArrayList<>(Arrays.asList(1, 5, 6, 7));
 comparator = new SequencesComparator<>(list1, list2);
 expectedResult = new ArrayList<>();
 assertEquals(expectedResult, comparator.getScript().apply(list1));
 }
}