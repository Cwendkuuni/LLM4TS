package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.test.EditableListTestHelper;
import org.junit.Before;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;
 private Equator<Integer> equator;
 private SequencesComparator<Integer> comparator;
 private EditScript<Integer> script;

 @Before
 public void setUp() {
 sequence1 = new ArrayList<>();
 sequence2 = new ArrayList<>();
 equator = DefaultEquator.defaultEquator();
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 script = comparator.getScript();
 }

 @Test
 public void testConstructorWithSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 assertNotNull(comparator.getScript());
 }

 @Test
 public void testConstructorWithSequencesAndEquator() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 Equator<Integer> equator = new Equator<Integer>() {
 @Override
 public boolean equate(Object obj1, Object obj2) {
 return obj1.equals(obj2);
 }
 };
 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 assertNotNull(comparator.getScript());
 }

 @Test
 public void testGetScript_EmptySequences() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(new ArrayList<>(), new ArrayList<>());
 assertNotNull(comparator.getScript());
 assertEquals(new EditScript<Integer>(), comparator.getScript());
 }

 @Test
 public void testGetScript_IdenticalSequences() {
 sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 sequence2 = new ArrayList<>(sequence1);
 comparator = new SequencesComparator<>(sequence1, sequence2);
 assertNotNull(comparator.getScript());
 assertEquals(new EditScript<Integer>(), comparator.getScript());
 }

 @Test
 public void testGetScript_SequencesWithSameElementsButDifferentOrder() {
 sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 sequence2 = new ArrayList<>(Arrays.asList(5, 4, 3, 2, 1));
 comparator = new SequencesComparator<>(sequence1, sequence2);
 assertNotNull(comparator.getScript());

 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new InsertCommand<>(5, 0));
 expectedScript.append(new DeleteCommand<>(1, 0));
 expectedScript.append(new DeleteCommand<>(1, 4));
 expectedScript.append(new InsertCommand<>(1, 4));
 expectedScript.append(new DeleteCommand<>(3, 3));
 expectedScript.append(new InsertCommand<>(3, 2));
 expectedScript.append(new DeleteCommand<>(4, 3));
 expectedScript.append(new InsertCommand<>(4, 3));
 expectedScript.append(new InsertCommand<>(5, 4));

 assertEquals(expectedScript, comparator.getScript());
 }

 @Test
 public void testGetScript_SequencesWithDifferentElements() {
 sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 sequence2 = new ArrayList<>(Arrays.asList(6, 7, 8, 9, 10));
 comparator = new SequencesComparator<>(sequence1, sequence2);
 assertNotNull(comparator.getScript());

 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new InsertCommand<>(6, 0));
 expectedScript.append(new InsertCommand<>(7, 1));
 expectedScript.append(new InsertCommand<>(8, 2));
 expectedScript.append(new InsertCommand<>(9, 3));
 expectedScript.append(new InsertCommand<>(10, 4));
 expectedScript.append(new DeleteCommand<>(1, 0));
 expectedScript.append(new DeleteCommand<>(2, 1));
 expectedScript.append(new DeleteCommand<>(3, 2));
 expectedScript.append(new DeleteCommand<>(4, 3));
 expectedScript.append(new DeleteCommand<>(5, 4));

 assertEquals(expectedScript, comparator.getScript());
 }

 @Test (expected = RuntimeException.class)
 public void testGetScript_InternalError() {
 sequence1 = new ArrayList<>();
 sequence2 = new ArrayList<>();
 comparator = new SequencesComparator<>(sequence1, sequence2);
 comparator.buildScript(0, 0, 0, 0, script);
 }

 @Test
 public void testBuildSnake_SimpleCase() {
 int start = 1;
 int diag = 1;
 int end1 = 4;
 int end2 = 4;
 Snake actualSnake = comparator.buildSnake(start, diag, end1, end2);
 Snake expectedSnake = new Snake(start, end1, diag);
 assertEquals(expectedSnake, actualSnake);
 }

 @Test
 public void testBuildSnake_EdgeCase() {
 int start = 1;
 int diag = 1;
 int end1 = 4;
 int end2 = 5;
 Snake actualSnake = comparator.buildSnake(start, diag, end1, end2);
 assertNull(actualSnake);
 }

 @Test
 public void testBuildSnake_ComplexCase() {
 int start = 1;
 int diag = 2;
 int end1 = 5;
 int end2 = 5;
 Snake actualSnake = comparator.buildSnake(start, diag, end1, end2);
 Snake expectedSnake = new Snake(start, 3, diag);
 assertEquals(expectedSnake, actualSnake);
 }

 @Test
 public void testGetMiddleSnake_SimpleCase() {
 int start1 = 1;
 int end1 = 4;
 int start2 = 1;
 int end2 = 4;
 Snake actualSnake = comparator.getMiddleSnake(start1, end1, start2, end2);
 assertNull(actualSnake);
 }

 @Test
 public void testGetMiddleSnake_EdgeCase() {
 int start1 = 1;
 int end1 = 5;
 int start2 = 1;
 int end2 = 5;
 Snake actualSnake = comparator.getMiddleSnake(start1, end1, start2, end2);
 assertNull(actualSnake);
 }

 @Test
 public void testGetMiddleSnake_ComplexCase() {
 int start1 = 1;
 int end1 = 5;
 int start2 = 1;
 int end2 = 6;
 Snake actualSnake = comparator.getMiddleSnake(start1, end1, start2, end2);
 Snake expectedSnake = new Snake(1, 3, 2);
 assertEquals(expectedSnake, actualSnake);
 }

 @Test
 public void testBuildScript_SimpleCase() {
 int start1 = 1;
 int end1 = 4;
 int start2 = 1;
 int end2 = 4;
 comparator.buildScript(start1, end1, start2, end2, script);

 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(1));
 expectedScript.append(new KeepCommand<>(2));
 expectedScript.append(new KeepCommand<>(3));
 expectedScript.append(new KeepCommand<>(4));

 assertEquals(expectedScript, script);
 }

 @Test
 public void testBuildScript_ComplexCase() {
 int start1 = 1;
 int end1 = 5;
 int start2 = 1;
 int end2 = 6;
 comparator.buildScript(start1, end1, start2, end2, script);

 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(1));
 expectedScript.append(new KeepCommand<>(2));
 expectedScript.append(new InsertCommand<>(3, 3));
 expectedScript.append(new KeepCommand<>(4));
 expectedScript.append(new KeepCommand<>(5));

 assertEquals(expectedScript, script);
 }

 @Test
 public void testBuildScript_ComplexCaseWithLongSequences() {
 List<Integer> sequence1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
 List<Integer> sequence2 = new ArrayList<>(Arrays.asList(16, 17, 18, 19, 20, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4));
 comparator = new SequencesComparator<>(sequence1, sequence2);
 comparator.buildScript(0, sequence1.size(), 0, sequence2.size(), script);

 EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new DeleteCommand<>(1, 0));
 expectedScript.append(new InsertCommand<>(13, 0));
 expectedScript.append(new DeleteCommand<>(2, 1));
 expectedScript.append(new InsertCommand<>(12, 1));
 expectedScript.append(new DeleteCommand<>(3, 2));
 expectedScript.append(new InsertCommand<>(11, 2));
 expectedScript.append(new DeleteCommand<>(4, 3));
 expectedScript.append(new InsertCommand<>(10, 3));
 expectedScript.append(new DeleteCommand<>(5, 4));
 expectedScript.append(new InsertCommand<>(9, 4));
 expectedScript.append(new DeleteCommand<>(6, 5));
 expectedScript.append(new InsertCommand<>(8, 5));
 expectedScript.append(new DeleteCommand<>(7, 6));
 expectedScript.append(new InsertCommand<>(15, 6));
 expectedScript.append(new DeleteCommand<>(14, 7));
 expectedScript.append(new InsertCommand<>(13, 7));
 expectedScript.append(new DeleteCommand<>(11, 8));
 expectedScript.append(new InsertCommand<>(16, 8));
 expectedScript.append(new DeleteCommand<>(10, 9));
 expectedScript.append(new InsertCommand<>(17, 9));
 expectedScript.append(new DeleteCommand<>(8, 10));
 expectedScript.append(new InsertCommand<>(18, 10));
 expectedScript.append(new DeleteCommand<>(7, 11));
 expectedScript.append(new InsertCommand<>(19, 11));
 expectedScript.append(new DeleteCommand<>(6, 12));
 expectedScript.append(new InsertCommand<>(20, 12));
 expectedScript.append(new DeleteCommand<>(5, 13));
 expectedScript.append(new KeepCommand<>(1, 14));
 expectedScript.append(new KeepCommand<>(2, 15));
 expectedScript.append(new KeepCommand<>(3, 15));
 expectedScript.append(new KeepCommand<>(4, 15));
 expectedScript.append(new KeepCommand<>(5, 15));
 expectedScript.append(new KeepCommand<>(6, 15));
 expectedScript.append(new KeepCommand<>(7, 15));
 expectedScript.append(new KeepCommand<>(8, 15));
 expectedScript.append(new KeepCommand<>(9, 15));
 expectedScript.append(new KeepCommand<>(10, 15));
 expectedScript.append(new KeepCommand<>(11, 15));
 expectedScript.append(new KeepCommand<>(12, 15));
 expectedScript.append(new KeepCommand<>(13, 15));
 expectedScript.append(new KeepCommand<>(14, 15));
 expectedScript.append(new KeepCommand<>(15, 15));

 assertEquals(expectedScript, script);
 }
}