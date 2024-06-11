import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.iterators.EditScriptIterator;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequencesComparatorTest {

 @Test
 public void testEmptySequences() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Integer> sequence2 = new ArrayList<>();

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> expectedResult = Collections.emptyList();
 List<Integer> actualResult = new ArrayList<>();

 EditScriptIterator<Integer> iterator = new EditScriptIterator<>(script);
 while (iterator.hasNext()) {
 Integer command = iterator.next();
 if (command instanceof KeepCommand) {
 actualResult.add(((KeepCommand<Integer>) command).getElement());
 }
 }

 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testEqualSequences() {
 List<Integer> sequence1 = new ArrayList<>(Collections.singletonList(1));
 List<Integer> sequence2 = new ArrayList<>(Collections.singletonList(1));

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> expectedResult = Collections.singletonList(1);
 List<Integer> actualResult = new ArrayList<>();

 EditScriptIterator<Integer> iterator = new EditScriptIterator<>(script);
 while (iterator.hasNext()) {
 Integer command = iterator.next();
 if (command instanceof KeepCommand) {
 actualResult.add(((KeepCommand<Integer>) command).getElement());
 }
 }

 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testDifferentSequences() {
 List<Integer> sequence1 = new ArrayList<>(Collections.singletonList(1));
 List<Integer> sequence2 = new ArrayList<>(Collections.singletonList(2));

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();

 List<Integer> expectedResult = Collections.singletonList(1);
 List<Integer> actualResult = new ArrayList<>();

 EditScriptIterator<Integer> iterator = new EditScriptIterator<>(script);
 while (iterator.hasNext()) {
 Integer command = iterator.next();
 if (command instanceof DeleteCommand) {
 actualResult.add(((DeleteCommand<Integer>) command).getElement());
 }
 }

 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidArguments() {
 List<Integer> sequence1 = new ArrayList<>();
 List<Object> sequence2 = new ArrayList<>();

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 }

 @Test(expected = RuntimeException.class)
 public void testMismatchedSizes() {
 List<Integer> sequence1 = new ArrayList<>(Collections.singletonList(1));
 List<Integer> sequence2 = new ArrayList<>(Collections.singletonList(2));

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 }

 @Test
 public void testCustomEquator() {
 List<String> sequence1 = new ArrayList<>(ListUtils.list(new String[]{"apple", "banana"}));
 List<String> sequence2 = new ArrayList<>(ListUtils.list(new String[]{"banana", "apple"}));

 SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2, new MyCustomEquator());
 EditScript<String> script = comparator.getScript();

 List<String> expectedResult = Collections.singletonList("apple");
 List<String> actualResult = new ArrayList<>();

 EditScriptIterator<String> iterator = new EditScriptIterator<>(script);
 while (iterator.hasNext()) {
 Integer command = iterator.next();
 if (command instanceof KeepCommand) {
 actualResult.add(((KeepCommand<String>) command).getElement());
 }
 }

 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testCustomEditScript() {
 List<Integer> sequence1 = new ArrayList<>(Collections.singletonList(1));
 List<Integer> sequence2 = new ArrayList<>(Collections.singletonList(2));

 SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
 EditScript<Integer> script = comparator.getScript();

 EditScript<Integer> customScript = new EditScript<>();
 customScript.append(new InsertCommand<>(2, 0));
 customScript.append(new InsertCommand<>(1, 1));

 List<Integer> result = new ArrayList<>();
 EditScriptIterator<Integer> iterator = new EditScriptIterator<>(ScriptUtils.mergeScripts(script, customScript));
 while (iterator.hasNext()) {
 Integer command = iterator.next();
 if (command instanceof InsertCommand) {
 result.add(((InsertCommand<Integer>) command).getElement());
 }
 }

 List<Integer> expectedResult = ListUtils.list(2, 1);
 List<Integer> actualResult = new ArrayList<>(result);
 Collections.sort(actualResult);
 assertEquals(expectedResult, actualResult);
 }
}