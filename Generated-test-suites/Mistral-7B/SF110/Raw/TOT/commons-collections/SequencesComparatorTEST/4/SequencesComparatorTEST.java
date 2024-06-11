import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.seq.EditScript;
import org.apache.commons.collections4.seq.EditScriptEditor;
import org.apache.commons.collections4.seq.EditSequence;
import org.apache.commons.collections4.seq.SequencesComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SequencesComparatorTest {

    @Test
    public void testEmptySequences() {
        List<Integer> sequence1 = Collections.emptyList();
        List<Integer> sequence2 = Collections.emptyList();
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        EditSequence<Integer> sequence = new EditSequence<>();
        EditScriptEditor<Integer> editor = new EditScriptEditor<>(sequence);
        script.applyTo(editor);
        List<Integer> expected = Collections.emptyList();
        Assert.assertEquals(expected, editor.getSequence());
    }

    @Test
    public void testEqualSequences() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        EditSequence<Integer> sequence = new EditSequence<>();
        EditScriptEditor<Integer> editor = new EditScriptEditor<>(sequence);
        script.applyTo(editor);
        List<Integer> expected = Collections.emptyList();
        Assert.assertEquals(expected, editor.getSequence());
    }

    @Test
    public void testSequencesWithDifferentLengths() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3, 4);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        EditSequence<Integer> sequence = new EditSequence<>();
        EditScriptEditor<Integer> editor = new EditScriptEditor<>(sequence);
        script.applyTo(editor);
        List<Integer> expected = Arrays.asList(new DeleteCommand<>(1), new InsertCommand<>(4));
        Assert.assertEquals(expected, editor.getSequence());
    }

    @Test
    public void testSequencesWithDifferentElements() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(4, 5, 6);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        EditSequence<Integer> sequence = new EditSequence<>();
        EditScriptEditor<Integer> editor = new EditScriptEditor<>(sequence);
        script.applyTo(editor);
        List<Integer> expected = Arrays.asList(new DeleteCommand<>(1), new InsertCommand<>(4), new InsertCommand<>(5), new InsertCommand<>(6));
        Assert.assertEquals(expected, editor.getSequence());
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidEquator() {
        List<Integer> sequence1 = Arrays.asList(1, 2, 3);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2, new BrokenEquator<>());
    }

    @Test
    public void testSequencesWithSameElementsButDifferentOrder() {
        List<Integer> sequence1 = Arrays.asList(3, 2, 1);
        List<Integer> sequence2 = Arrays.asList(1, 2, 3);
        SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<Integer> script = comparator.getScript();
        EditSequence<Integer> sequence = new EditSequence<>();
        EditScriptEditor<Integer> editor = new EditScriptEditor<>(sequence);
        script.applyTo(editor);
        List<Integer> expected = Arrays.asList(new MoveCommand<>(0, 2), new MoveCommand<>(2, 0));
        Assert.assertEquals(expected, editor.getSequence());
    }
}