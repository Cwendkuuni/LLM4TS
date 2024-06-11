package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest<T> {

 private List<T> sequence1;
 private List<T> sequence2;
 private Equator<? super T> equator;
 private SequencesComparator<T> comparator;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 List<T> list1 = new ArrayList<>();
 list1.add(new Object(){});
 list1.add(new Object(){});
 list1.add(new Object(){});
 this.sequence1 = Collections.unmodifiableList(list1);

 List<T> list2 = new ArrayList<>();
 list2.add(new Object(){});
 list2.add(new Object(){});
 list2.add(new Object(){});
 this.sequence2 = Collections.unmodifiableList(list2);

 this.equator = DefaultEquator.defaultEquator();
 }

 @Test
 public void testSequencesComparatorConstructionWithLists() {
 this.comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 assertNotNull(this.comparator.getScript());
 }

 @Test
 public void testSequencesComparatorConstructionWithListsAndEquator() {
 this.comparator = new SequencesComparator<>(this.sequence1, this.sequence2, this.equator);
 assertNotNull(this.comparator.getScript());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorWithNullSequence1() {
 try {
 new SequencesComparator<>(null, this.sequence2, this.equator);
 fail();
 } catch (IllegalArgumentException e) {
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorWithNullSequence2() {
 try {
 new SequencesComparator<>(this.sequence1, null, this.equator);
 fail();
 } catch (IllegalArgumentException e) {
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSequencesComparatorWithNullEquator() {
 try {
 new SequencesComparator<>(this.sequence1, this.sequence2, null);
 fail();
 } catch (IllegalArgumentException e) {
 }
 }

 @Test
 public void testGetScript() {
 EditScript<Object> script = this.comparator.getScript();
 assertNotNull(script);
 }

 @Test
 public void testBuildSnakeBasicCase() {
 int start = 0;
 int diag = 1;
 int end1 = 2;
 int end2 = 2;
 Snake expected = new Snake(start, end1, diag);
 Snake actual = this.comparator.buildSnake(start, diag, end1, end2);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMiddleSnakeBasicCase() {
 int start1 = 0;
 int end1 = 2;
 int start2 = 0;
 int end2 = 2;
 Snake expected = null;
 Snake actual = this.comparator.getMiddleSnake(start1, end1, start2, end2);
 assertEquals(expected, actual);
 }

 @Test
 public void testBuildScriptBasicCase() {
 int start1 = 0;
 int end1 = 2;
 int start2 = 0;
 int end2 = 2;
 this.comparator.buildScript(start1, end1, start2, end2, null);
 }

 @Test
 public void testBuildScriptBasicCaseWithDifferentSizes() {
 int start1 = 0;
 int end1 = 2;
 int start2 = 0;
 int end2 = 3;
 EditScript<Object> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(this.sequence1.get(start1)));
 expectedScript.append(new DeleteCommand<>(this.sequence1.get(start1 + 1)));
 expectedScript.append(new InsertCommand<>(this.sequence2.get(start2)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(start1 + 2)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(start2 + 1)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(start2 + 2)));
 EditScript<Object> actualScript = this.comparator.getScript();
 assertEquals(expectedScript, actualScript);
 }

 @Test
 public void testBuildScriptEdgeCaseWithDifferentSizesAndSameElements() {
 int start1 = 0;
 int end1 = 2;
 int start2 = 0;
 int end2 = 2;
 this.sequence1.set(1, this.sequence2.get(0));
 EditScript<Object> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(this.sequence1.get(start1)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(start1 + 1)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(start1 + 1)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(start2)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(start2 + 1)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(start2 + 1)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(start2 + 1)));
 EditScript<Object> actualScript = this.comparator.getScript();
 assertEquals(expectedScript, actualScript);
 }

 @Test
 public void testBuildScriptEdgeCaseWithDifferentSizesAndDifferentElements() {
 int start1 = 0;
 int end1 = 2;
 int start2 = 0;
 int end2 = 3;
 this.sequence1.set(1, new Object(){});
 this.sequence2.set(1, new Object(){});
 this.sequence2.set(2, new Object(){});
 EditScript<Object> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(this.sequence1.get(start1)));
 expectedScript.append(new DeleteCommand<>(this.sequence1.get(start1 + 1)));
 expectedScript.append(new InsertCommand<>(this.sequence2.get(start2)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(start1 + 2)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(start2 + 1)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(start2 + 2)));
 EditScript<Object> actualScript = this.comparator.getScript();
 assertEquals(expectedScript, actualScript);
 }

 @Test(expected = RuntimeException.class)
 public void testBuildSnakeExceptionCase() {
 int start = 0;
 int diag = 2;
 int end1 = 2;
 int end2 = 2;
 try {
 this.comparator.buildSnake(start, diag, end1, end2);
 fail();
 } catch (RuntimeException e) {
 }
 }
}