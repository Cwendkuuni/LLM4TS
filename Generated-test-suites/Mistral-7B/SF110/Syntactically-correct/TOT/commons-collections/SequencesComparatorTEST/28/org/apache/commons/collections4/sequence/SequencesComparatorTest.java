import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.util.EditScript;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

@RunWith(Parameterized.class)
public class SequencesComparatorTest {

 private List<Integer> sequence1;
 private List<Integer> sequence2;
 private SequencesComparator<Integer> comparator;

 @Parameters
 public static Collection<Object[]> data() {
 return List.of(new Object[] { Arrays.asList(1, 2, 3), Arrays.asList(2, 3, 1), Arrays.asList(1, 2, 3) },
 new Object[] { Arrays.asList(1, 1, 1), Arrays.asList(1, 1, 1), Arrays.asList(1, 1, 1) },
 new Object[] { Arrays.asList(1, 1, 1), Arrays.asList(1, 2, 1), Arrays.asList(1, 1, 1) },
 new Object[] { Arrays.asList("apple", "banana", "cherry"), Arrays.asList("banana", "cherry", "apple"), Arrays.asList("apple", "banana", "cherry") },
 new Object[] { Arrays.asList(null), Arrays.asList(Arrays.asList(1)), Arrays.asList(null) },
 new Object[] { Arrays.asList(Arrays.asList(1)), Arrays.asList(Arrays.asList(1, 1)), Arrays.asList(Arrays.asList(1)) });
 }

 @Before
 public void setup() {
 this.sequence1 = (List<Integer>) parameter[0];
 this.sequence2 = (List<Integer>) parameter[1];
 this.comparator = new SequencesComparator<>(this.sequence1, this.sequence2);
 }

 @Test
 public void testGetScript() {
 final EditScript<Integer> script = this.comparator.getScript();
 Assert.assertNotNull(script);
 }

 @Test(expected = RuntimeException.class)
 public void testGetScriptInternalError() {
 final SequencesComparator<Integer> comparator = new SequencesComparator<>(Arrays.asList(1), Arrays.asList(2), null);
 comparator.getScript();
 }

 @Test
 public void testKeepCommands() {
 final EditScript<Integer> script = this.comparator.getScript();
 final EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(this.sequence1.get(0)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(1)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(2)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(0)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(1)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(2)));
 Assert.assertEquals(expectedScript, script);
 }

 @Test
 public void testDeleteCommands() {
 final EditScript<Integer> script = this.comparator.getScript();
 final EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(this.sequence1.get(0)));
 expectedScript.append(new DeleteCommand<>(this.sequence1.get(1)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(2)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(0)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(1)));
 expectedScript.append(new KeepCommand<>(this.sequence2.get(2)));
 Assert.assertEquals(expectedScript, script);
 }

 @Test
 public void testInsertCommands() {
 final EditScript<Integer> script = this.comparator.getScript();
 final EditScript<Integer> expectedScript = new EditScript<>();
 expectedScript.append(new KeepCommand<>(this.sequence1.get(0)));
 expectedScript.append(new InsertCommand<>(this.sequence2.get(0)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(1)));
 expectedScript.append(new InsertCommand<>(this.sequence2.get(1)));
 expectedScript.append(new KeepCommand<>(this.sequence1.get(2)));
 expectedScript.append(new InsertCommand<>(this.sequence2.get(2)));
 Assert.assertEquals(expectedScript, script);
 }

 @Test
 public void testEmptySequenceComparator() {
 final List<Integer> emptyList = Collections.emptyList();
 final SequencesComparator<Integer> comparator = new SequencesComparator<>(emptyList, emptyList);
 final EditScript<Integer> script = comparator.getScript();
 final EditScript<Integer> expectedScript = new EditScript<>();
 Assert.assertEquals(expectedScript, script);
 }

 @Test
 public void testNullElementComparator() {
 final List<List<Integer>> nullLists = ListUtils.nestList(List.of(null), List.of(List.of(1)));
 final SequencesComparator<Integer> comparator = new SequencesComparator<>(nullLists.get(0), nullLists.get(1));
 final EditScript<Integer> script = comparator.getScript();
 final EditScript<Integer> expectedScript = new EditScript<>();
 Assert.assertEquals(expectedScript, script);
 }
}