package org.apache.commons.collections4.sequence;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.junit.Test;

public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;
 private Equator<Integer> equator;
 private SequencesComparator<Integer> comparator;

 @Before
 public void setup() {
 sequence1 = Arrays.asList(1, 2, 3, 4, 5);
 sequence2 = Arrays.asList(5, 4, 3, 2, 1);
 equator = DefaultEquator.defaultEquator();
 }

 @Test
 public void testSequencesComparatorConstructionWithSequences() {
 comparator = new SequencesComparator<>(sequence1, sequence2);
 assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vDown.length);
 assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vUp.length);
 }

 @Test
 public void testSequencesComparatorConstructionWithSequencesAndEquator() {
 comparator = new SequencesComparator<>(sequence1, sequence2, equator);
 assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vDown.length);
 assertEquals(sequence1.size() + sequence2.size() + 2, comparator.vUp.length);
 }

 @Test
 public void testGetScript() {
 EditScript<Integer> script = comparator.getScript();
 // The actual test logic for getScript method goes here
 }

 @Test(expected = RuntimeException.class)
 public void testGetScriptWithInternalError() {
 comparator = new SequencesComparator<>(Arrays.asList(1, 2), Arrays.asList(1, 2), DefaultEquator.defaultEquator());
 comparator.getMiddleSnake(0, 1, 0, 1);
 }

 @Test
 public void testBuildScriptBasicScenario1() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(0, 5, 0, 5, script);
 result = script.getCommands();
 assertEquals(10, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 if (result.get(i) instanceof KeepCommand) {
 assertEquals(sequence1.get(i / 3), ((KeepCommand<Integer>) result.get(i)).getElement());
 } else if (result.get(i) instanceof DeleteCommand) {
 assertEquals(sequence1.get(i / 3), ((DeleteCommand<Integer>) result.get(i)).getElement());
 } else {
 assertEquals(sequence2.get(i / 3), ((InsertCommand<Integer>) result.get(i)).getElement());
 }
 }
 }

 @Test
 public void testBuildScriptBasicScenario2() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(0, 5, 5, 5, script);
 result = script.getCommands();
 assertEquals(10, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 if (result.get(i) instanceof KeepCommand) {
 assertEquals(sequence1.get(i / 3), ((KeepCommand<Integer>) result.get(i)).getElement());
 } else if (result.get(i) instanceof DeleteCommand) {
 assertEquals(sequence1.get(i / 3), ((DeleteCommand<Integer>) result.get(i)).getElement());
 } else {
 assertEquals(sequence2.get(i / 3), ((InsertCommand<Integer>) result.get(i)).getElement());
 }
 }
 }

 @Test
 public void testBuildScriptBasicScenario3() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(0, 5, 0, 6, script);
 result = script.getCommands();
 assertEquals(11, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 if (i < 9) {
 if (result.get(i) instanceof KeepCommand) {
 assertEquals(sequence1.get(i / 3), ((KeepCommand<Integer>) result.get(i)).getElement());
 } else if (result.get(i) instanceof DeleteCommand) {
 assertEquals(sequence1.get(i / 3), ((DeleteCommand<Integer>) result.get(i)).getElement());
 } else {
 assertEquals(sequence2.get(i / 3), ((InsertCommand<Integer>) result.get(i)).getElement());
 }
 } else {
 assertEquals(sequence2.get(result.size() - 1), ((InsertCommand<Integer>) result.get(i)).getElement());
 }
 }
 }

 @Test
 public void testBuildScriptBasicScenario4() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(5, 5, 0, 5, script);
 result = script.getCommands();
 assertEquals(10, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 if (result.get(i) instanceof KeepCommand) {
 assertEquals(sequence2.get(i / 3), ((KeepCommand<Integer>) result.get(i)).getElement());
 } else if (result.get(i) instanceof DeleteCommand) {
 assertEquals(sequence2.get(i / 3), ((DeleteCommand<Integer>) result.get(i)).getElement());
 } else {
 assertEquals(sequence1.get(i / 3), ((InsertCommand<Integer>) result.get(i)).getElement());
 }
 }
 }

 @Test
 public void testBuildScriptBasicScenario5() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(5, 5, 5, 5, script);
 result = script.getCommands();
 assertEquals(5, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 if (result.get(i) instanceof KeepCommand) {
 assertEquals(sequence1.get(i / 3), ((KeepCommand<Integer>) result.get(i)).getElement());
 } else if (result.get(i) instanceof DeleteCommand) {
 assertEquals(sequence1.get(i / 3), ((DeleteCommand<Integer>) result.get(i)).getElement());
 } else {
 assertEquals(sequence2.get(i / 3), ((InsertCommand<Integer>) result.get(i)).getElement());
 }
 }
 }

 @Test
 public void testBuildScriptEdgeCase1() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(0, 5, 0, 1, script);
 result = script.getCommands();
 assertEquals(6, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 assertEquals(sequence1.get(i / 3), ((KeepCommand<Integer>) result.get(i)).getElement());
 assertEquals(sequence2.get(i / 3), ((InsertCommand<Integer>) result.get(i + 1)).getElement());
 }
 }

 @Test
 public void testBuildScriptEdgeCase2() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(0, 5, 5, 0, script);
 result = script.getCommands();
 assertEquals(11, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 assertEquals(sequence1.get(i / 3), ((KeepCommand<Integer>) result.get(i)).getElement());
 assertEquals(sequence2.get(result.size() - 1), ((InsertCommand<Integer>) result.get(i + 1)).getElement());
 }
 }

 @Test
 public void testBuildScriptEdgeCase3() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(0, 5, 0, 0, script);
 result = script.getCommands();
 assertEquals(1, result.size());
 assertEquals(sequence1.get(0), ((KeepCommand<Integer>) result.get(0)).getElement());
 }

 @Test
 public void testBuildScriptEdgeCase4() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(5, 5, 5, 5, script);
 result = script.getCommands();
 assertEquals(0, result.size());
 }

 @Test
 public void testBuildScriptEdgeCase5() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(5, 5, 5, 6, script);
 result = script.getCommands();
 assertEquals(1, result.size());
 assertEquals(sequence2.get(5), ((InsertCommand<Integer>) result.get(0)).getElement());
 }

 @Test
 public void testBuildScriptEdgeCase6() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(0, 5, 1, 5, script);
 result = script.getCommands();
 assertEquals(11, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 assertEquals(sequence1.get(i / 3), ((KeepCommand<Integer>) result.get(i)).getElement());
 assertEquals(sequence2.get(i / 3), ((InsertCommand<Integer>) result.get(i + 1)).getElement());
 }
 }

 @Test
 public void testBuildScriptEdgeCase7() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(5, 5, 1, 5, script);
 result = script.getCommands();
 assertEquals(11, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 assertEquals(sequence2.get(result.size() - 1 - i / 3), ((InsertCommand<Integer>) result.get(i)).getElement());
 assertEquals(sequence1.get(i / 3), ((KeepCommand<Integer>) result.get(i + 1)).getElement());
 }
 }

 @Test
 public void testBuildScriptEdgeCase8() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(0, 5, 5, 1, script);
 result = script.getCommands();
 assertEquals(11, result.size());
 for (int i = 0; i < result.size(); i += 3) {
 assertEquals(sequence2.get(i / 3), ((InsertCommand<Integer>) result.get(i)).getElement());
 assertEquals(sequence1.get(result.size() - 1 - i / 3), ((KeepCommand<Integer>) result.get(i + 1)).getElement());
 }
 }

 @Test
 public void testBuildScriptEdgeCase9() {
 EditScript<Integer> script = new EditScript<>();
 List<Integer> result = new ArrayList<>();
 comparator.buildScript(5, 5, 5, 1, script);
 result = script.getCommands();
 assertEquals(1, result.size());
 assertEquals(sequence2.get(4), ((InsertCommand<Integer>) result.get(0)).getElement());
 }
}