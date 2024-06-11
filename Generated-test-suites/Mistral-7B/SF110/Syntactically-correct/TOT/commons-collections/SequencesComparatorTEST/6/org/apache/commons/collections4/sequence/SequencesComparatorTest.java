import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.iterators.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class SequencesComparatorTest {

 @Test
 public void testSequencesComparatorWithEqualSequences() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();

 Assert.assertEquals("Both sequences are equal, no operation is required", script.toString(), "");
 }

 @Test
 public void testSequencesComparatorWithIdenticalSequencesInReverseOrder() {
 List<Integer> sequence1 = Arrays.asList(3, 2, 1);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();

 Assert.assertEquals("Both sequences are identical but in reverse order, elements should be inserted in reverse order", script.toString(), "Insert 1, Insert 2, Insert 3");
 }

 @Test
 public void testSequencesComparatorWithSequencesContainingSameElementsButDifferentOrders() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(3, 2, 1);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();

 Assert.assertEquals("Elements are the same but sequences are in different orders, elements should be swapped", script.toString(), "Delete 0, Insert 2, Delete 1, Delete 0, Insert 1");
 }

 @Test
 public void testSequencesComparatorWithSequencesContainingSameElementsButDifferentSizes() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();

 Assert.assertEquals("Both sequences contain the same elements but one has fewer elements, missing elements should be inserted", script.toString(), "Insert 3");
 }

 @Test
 public void testSequencesComparatorWithSequencesContainingDifferentElements() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2, 4);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();

 Assert.assertEquals("Sequences contain different elements, missing elements should be inserted and extra elements should be deleted", script.toString(), "Delete 3, Insert 4");
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorWithNullSequences() {
 List<Integer> sequence1 = null;
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 try {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }
 catch (NullPointerException e) {
 // Expected NullPointerException, sequences cannot be null
 }
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorWithNullEquator() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 Equator<Integer> equator = null;
 try {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }
 catch (NullPointerException e) {
 // Expected NullPointerException, equator cannot be null
 }
 }

 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorWithDifferentTypesOfSequences() {
 List<String> sequence1 = Arrays.asList("apple", "banana", "orange");
 List<Integer> sequence2 = Arrays.asList(1, 2, 3);
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 try {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 }
 catch (ClassCastException e) {
 // Expected ClassCastException, sequences must be of the same type
 }
 }

 @Test
 public void testSequencesComparatorWithEmptySequences() {
 List<Integer> sequence1 = Collections.emptyList();
 List<Integer> sequence2 = Collections.emptyList();
 Equator<Integer> equator = DefaultEquator.defaultEquator();
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 EditScript<Integer> script = comparator.getScript();

 Assert.assertEquals("Both sequences are empty, no operation is required", script.toString(), "");
 }
}