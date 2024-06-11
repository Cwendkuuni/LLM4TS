import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PositionableStringTokenizer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        options.addOption("d", "debug", false, "debug mode");

        parser = new PosixParser();
    }

    @Test
    public void testFlattenSimple() {
        String[] args = {"java", "PosixParserTest", "--input", "test.txt"};

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("java");
        expected.add("PosixParserTest");
        expected.add("--input");
        expected.add("test.txt");

        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenOptionWithSingleArgument() {
        String[] args = {"java", "PosixParserTest", "--input", "test.txt", "--output", "output.txt"};

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("java");
        expected.add("PosixParserTest");
        expected.add("--input");
        expected.add("test.txt");
        expected.add("--output");
        expected.add("output.txt");

        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenOptionWithMultipleArguments() {
        String[] args = {"java", "PosixParserTest", "--input", "test.txt", "--output", "output.txt", "--debug"};

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("java");
        expected.add("PosixParserTest");
        expected.add("--input");
        expected.add("test.txt");
        expected.add("--output");
        expected.add("output.txt");
        expected.add("--debug");

        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenLongOption() {
        String[] args = {"java", "PosixParserTest", "--long-option", "value"};

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("java");
        expected.add("PosixParserTest");
        expected.add("--long-option");
        expected.add("value");

        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenOptionWithoutArgument() {
        String[] args = {"java", "PosixParserTest", "--debug"};

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("java");
        expected.add("PosixParserTest");
        expected.add("--debug");

        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenDoubleDash() {
        String[] args = {"java", "PosixParserTest", "--", "value"};

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("java");
        expected.add("PosixParserTest");
        expected.add("--");
        expected.add("value");

        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenDoubleDashWithArguments() {
        String[] args = {"java", "PosixParserTest", "--", "option", "value"};

        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("java");
        expected.add("PosixParserTest");
        expected.add("--");
        expected.add("option");
        expected.add("value");

        assertEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNonOption() {
        String[] args = {"java", "PosixParserTest", "arg"};

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenNonOptionWithStopAtNonOption() {
        String[] args = {"java", "PosixParserTest", "arg"};

        String[] result = parser.flatten(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("arg");

        assertEquals(expected.toArray(new String[expected.size()]), result);
    }
}