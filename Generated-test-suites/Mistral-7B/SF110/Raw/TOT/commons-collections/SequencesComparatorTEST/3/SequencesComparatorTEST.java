import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.equality.Equator;
import org.apache.commons.collections4.functors.ChainedPredicate;
import org.apache.commons.collections4.functors.PredicateCombiners;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.apache.commons.collections4.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SequencesComparatorTest {

    private List<String> sequence1;
    private List<String> sequence2;
    private List<String> expectedScript;
    private Equator<String> equator;

    @Before
    public void setUp() {
        sequence1 = Arrays.asList("apple", "banana", "cherry", "date");
        sequence2 = Arrays.asList("cherry", "banana", "apple", "date", "grape");
        equator = new DefaultEquator<>();
    }

    @Test
    public void testEmptySequences() {
        SequencesComparator<String> comparator = new SequencesComparator<>(Collections.emptyList(), Collections.emptyList());
        EditScript<String> script = comparator.getScript();
        assertTrue(script.isEmpty());
    }

    @Test
    public void testEquivalentSequences() {
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence1);
        EditScript<String> script = comparator.getScript();
        List<EditCommand<String>> commands = script.getCommands();
        assertEquals(4, commands.size());

        for (int i = 0; i < commands.size(); ++i) {
            assertEquals(KeepCommand.class, commands.get(i).getClass());
            assertEquals(sequence1.get(i), ((KeepCommand<String>) commands.get(i)).getElement());
        }
    }

    @Test
    public void testSequencesWithDifferences() {
        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        List<EditCommand<String>> commands = script.getCommands();
        assertEquals(11, commands.size());

        int i = 0;
        for (int j = 0; j < 4; ++j) {
            if (j % 2 == 0) {
                assertEquals(KeepCommand.class, commands.get(i).getClass());
                assertEquals(sequence1.get(j), ((KeepCommand<String>) commands.get(i)).getElement());
                ++i;
            } else {
                assertEquals(DeleteCommand.class, commands.get(i).getClass());
                assertEquals(sequence1.get(j), ((DeleteCommand<String>) commands.get(i)).getElement());
                ++i;

                assertEquals(InsertCommand.class, commands.get(i).getClass());
                assertEquals(sequence2.get(j), ((InsertCommand<String>) commands.get(i)).getElement());
                ++i;
            }
        }

        assertEquals(KeepCommand.class, commands.get(i).getClass());
        assertEquals(sequence2.get(4), ((KeepCommand<String>) commands.get(i)).getElement());
    }

    @Test
    public void testSequencesWithError() {
        List<String> sequence3 = Arrays.asList(null, "apple", null);
        List<String> sequence4 = Arrays.asList("cherry", "banana", "apple", null, "grape");

        try {
            new SequencesComparator<>(sequence3, sequence4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The lists must not contain null elements", e.getMessage());
        }

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence1, sequence2);
        EditScript<String> script = comparator.getScript();

        Transformer<Integer, Integer> identityTransformer = new Transformer<Integer, Integer>() {
            @Override
            public Integer transform(Integer input) {
                return input;
            }
        };
        Transformer<Integer, String> stringTransformer = new Transformer<Integer, String>() {
            @Override
            public String transform(Integer input) {
                return String.valueOf(input);
            }
        };

        List<Integer> numbers1 = ListUtils.integerSequence(0, 4);
        List<Integer> numbers2 = ListUtils.integerSequence(3, 7);
        List<Integer> expectedNumbers = ListUtils.integerSequence(0, 7);

        Transformer<Integer, EditCommand<String>> commandTransformer = new Transformer<Integer, EditCommand<String>>() {
            @Override
            public EditCommand<String> transform(Integer input) {
                if (input < 0) {
                    return new DeleteCommand<String>(null);
                } else if (input > 7) {
                    return new InsertCommand<String>("");
                }
                return new KeepCommand<String>(String.valueOf(input));
            }
        };

        EditScript<Integer> scriptNumbers = new SequencesComparator<>(numbers1, numbers2).getScript();
        List<EditCommand<Integer>> commandsNumbers = scriptNumbers.mapCommands(commandTransformer);

        List<Integer> actualNumbers = new ArrayList<>();
        for (EditCommand<String> command : script.getCommands()) {
            actualNumbers.add(Integer.parseInt(command.getElement().toString()));
        }

        List<Integer> expectedNumbers2 = new ArrayList<>(expectedNumbers);
        Collections.shuffle(expectedNumbers2);

        assertEquals(expectedNumbers, actualNumbers);
    }

    @Test
    public void testSequencesWithCustomEquator() {
        Equator<String> customEquator = ChainedPredicate.and(
                new Equator<String>() {
                    @Override
                    public boolean equate(String a, String b) {
                        return a.length() == b.length();
                    }
                },
                new Equator<String>() {
                    @Override
                    public boolean equate(String a, String b) {
                        return a.compareTo(b) <= 0;
                    }
                });

        List<String> sequence5 = Arrays.asList("apple", "banana", "cherry", "date");
        List<String> sequence6 = Arrays.asList("cherry", "banana", "apple", "date", "grape");

        SequencesComparator<String> comparator = new SequencesComparator<>(sequence5, sequence6, customEquator);
        EditScript<String> script = comparator.getScript();

        List<EditCommand<String>> commands = script.getCommands();
        assertEquals(11, commands.size());

        int i = 0;
        for (int j = 0; j < 4; ++j) {
            if (j % 2 == 0) {
                assertEquals(KeepCommand.class, commands.get(i).getClass());
                assertEquals(sequence5.get(j), ((KeepCommand<String>) commands.get(i)).getElement());
                ++i;
            } else {
                assertEquals(DeleteCommand.class, commands.get(i).getClass());
                assertEquals(sequence5.get(j), ((DeleteCommand<String>) commands.get(i)).getElement());
                ++i;

                assertEquals(InsertCommand.class, commands.get(i).getClass());
                assertEquals(sequence6.get(j), ((InsertCommand<String>) commands.get(i)).getElement());
                ++i;
            }
        }

        assertEquals(KeepCommand.class, commands.get(i).getClass());
        assertEquals(sequence6.get(4), ((KeepCommand<String>) commands.get(i)).getElement());
    }
}