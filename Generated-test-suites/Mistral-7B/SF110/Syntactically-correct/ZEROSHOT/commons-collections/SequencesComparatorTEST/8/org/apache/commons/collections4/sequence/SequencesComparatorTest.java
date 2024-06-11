package org.apache.commons.collections4.sequence;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SequencesComparatorTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparatorWithDefaultEquator() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 6, 4, 5, 7);

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> result = new ArrayList<>();
 script.applyTo(sequence1);
 assertEquals(sequence2, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testSequencesComparatorWithCustomEquator() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 List<Integer> sequence2 = Arrays.asList(1, 2, 6, 4, 5, 7);

 Equator<Integer> customEquator = new Equator<Integer>() {
 @Override
 public boolean equate(Object o1, Object o2) {
 Integer i1 = (Integer)o1;
 Integer i2 = (Integer)o2;
 return i1.intValue() <= i2.intValue();
 }
 };

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, customEquator);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> result = new ArrayList<>();
 script.applyTo(sequence1);
 assertTrue(Arrays.asList(1, 2, 4, 5).containsAll(sequence2));
 }

 @SuppressWarnings("unchecked")
 @Test(expected = RuntimeException.class)
 public void testSequencesComparatorWithInvalidArguments() {
 List<Integer> sequence1 = Arrays.asList(1, 2);
 List<Integer> sequence2 = null;

 try {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 }
 catch (NullPointerException e) {
 fail("Expected SequencesComparator to throw RuntimeException instead of NullPointerException");
 }
 }

 @SuppressWarnings("unchecked")
 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorWithDifferentSizes() {
 List<Integer> sequence1 = Arrays.asList(1, 2, 3);
 List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);

 try {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 fail("Expected IllegalArgumentException to be thrown");
 }
 catch (IllegalArgumentException e) {
 // Expected
 }
 }
}