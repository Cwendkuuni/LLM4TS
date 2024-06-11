import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Sequences;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.Equator;
import org.apache.commons.collections4.util.EditScript;
import org.apache.commons.collections4.util.EditScriptIterator;
import org.apache.commons.collections4.util.EditScriptUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupyter.annotations.DisplayName;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@DisplayName("Tests for SequencesComparator class")
public class SequencesComparatorTest {

    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    private List<String> list4;
    private SequencesComparator<String> comparator;

    @Before
    public void setup() {
        list1 = Arrays.asList("apple", "banana", "cherry", "date");
        list2 = Arrays.asList("cherry", "apple", "banana", "date", "fig");
        list3 = Arrays.asList("apple", "banana", "cherry", "date");
        list4 = Collections.emptyList();
    }

    @Test
    @DisplayName("Test SequencesComparator with equal lists")
    public void testEqualLists() {
        comparator = new SequencesComparator<>(list1, list1, DefaultEquator.defaultEquator());
        EditScript<String> script = comparator.getScript();
        assertEquals(0, script.length());
    }

    @Test
    @DisplayName("Test SequencesComparator with lists that differ only in order")
    public void testDifferOnlyInOrder() {
        comparator = new SequencesComparator<>(list1, list2, DefaultEquator.defaultEquator());
        EditScript<String> script = comparator.getScript();
        EditScriptIterator<String> iterator = EditScriptUtil.getInsertDeleteIterator(script);
        List<String> expected = Arrays.asList("delete", "insert");
        ListUtils.isEqualCollection(iterator.getOperations(), expected);
    }

    @Test
    @DisplayName("Test SequencesComparator with empty list")
    public void testEmptyList() {
        comparator = new SequencesComparator<>(list1, list4, DefaultEquator.defaultEquator());
        EditScript<String> script = comparator.getScript();
        assertNull(script);
    }

    @Test(expected = RuntimeException.class)
    @DisplayName("Test SequencesComparator with unmatched lists")
    public void testUnmatchedLists() {
        list3.add("fig");
        comparator = new SequencesComparator<>(list1, list3, DefaultEquator.defaultEquator());
        comparator.getScript();
    }
}