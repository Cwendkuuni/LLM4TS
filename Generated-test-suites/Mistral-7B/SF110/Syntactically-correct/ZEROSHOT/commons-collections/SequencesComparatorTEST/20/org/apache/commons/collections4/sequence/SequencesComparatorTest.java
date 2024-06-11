package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private static final List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
 private static final List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 6, 4, 5));
 private static final Equator<Integer> defaultEquator = DefaultEquator.defaultEquator();

 @Test
 public void testSequencesComparator() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.toList();

 List<Integer> expectedList = new ArrayList<>(Arrays.asList(1, 2, 4, 5, 6));
 List<Integer> resultList = new ArrayList<>();

 for (EditCommand<Integer> command : commands) {
 switch (command.getType()) {
 case KEEP:
 resultList.add(((KeepCommand<Integer>) command).getObject());
 break;
 case DELETE:
 break;
 case INSERT:
 resultList.add(((InsertCommand<Integer>) command).getObject());
 break;
 }
 }

 assertEquals(expectedList, resultList);
 }

 @Test(expected = RuntimeException.class)
 public void testGetMiddleSnakeException() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 comparator.getMiddleSnake(-1, list1.size(), 0, list2.size());
 }

 @Test
 public void testGetMiddleSnake() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 Snake middleSnake = comparator.getMiddleSnake(0, list1.size(), 0, list2.size());

 assertEquals(2, middleSnake.getDiag());
 assertEquals(3, middleSnake.getStart());
 assertEquals(4, middleSnake.getEnd());
 }

 @Test
 public void testBuildScript() {
 SequencesComparator<Integer> comparator = new SequencesComparator<>(list1, list2);
 EditScript<Integer> script = comparator.getScript();
 List<EditCommand<Integer>> commands = script.toList();

 List<Integer> expectedList = new ArrayList<>(Arrays.asList(1, 2, 4, 5, 6));
 List<Integer> resultList = new ArrayList<>();

 for (EditCommand<Integer> command : commands) {
 switch (command.getType()) {
 case KEEP:
 resultList.add(((KeepCommand<Integer>) command).getObject());
 break;
 case DELETE:
 break;
 case INSERT:
 resultList.add(((InsertCommand<Integer>) command).getObject());
 break;
 }
 }

 assertEquals(expectedList, resultList);

 EditScript<Integer> script2 = new EditScript<>();
 comparator.buildScript(0, list1.size(), 0, list2.size(), script2);
 List<EditCommand<Integer>> commands2 = script2.toList();

 List<Integer> expectedList2 = new ArrayList<>(Arrays.asList(1, 2, 4, 5, 6));
 List<Integer> resultList2 = new ArrayList<>();

 for (EditCommand<Integer> command : commands2) {
 switch (command.getType()) {
 case KEEP:
 resultList2.add(((KeepCommand<Integer>) command).getObject());
 break;
 case DELETE:
 break;
 case INSERT:
 resultList2.add(((InsertCommand<Integer>) command).getObject());
 break;
 }
 }

 assertEquals(expectedList2, resultList2);
 }
}