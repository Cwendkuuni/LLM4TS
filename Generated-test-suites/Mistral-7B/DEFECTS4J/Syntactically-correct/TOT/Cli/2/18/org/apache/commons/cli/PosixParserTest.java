import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"--foo", "-o", "out.txt", "--bar=value"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean representing foo.");
        options.addOption("o", "o", true, "Output file.");
        options.addOption("bar", true, "A string representing bar with value.");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--foo", "-o", "--bar", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] args = {"--foo", "-b", "--bar", "-b", "-x", "value1", "-x", "value2"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean representing foo.");
        options.addOption("b", "b", false, "A boolean representing b.");
        options.addOption("bar", true, "A string representing bar.");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--foo", "--bar", "--b", "--b", "-x", "value1", "-x", "value2"};
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        String[] args = {"--invalid", "option"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithArg() {
        String[] args = {"--foo", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "A boolean representing foo.");
        options.addOption("foo", 1, "An integer representing arg for foo.");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--foo", "arg1", "arg2"};
        assertArrayEquals(expected, result);

        assertEquals(1, parser.getTokens().size());
        assertEquals("--foo arg1 arg2", parser.getTokens().get(0));
    }

    @Test
    public void testFlattenWithLongArg() {
        String[] args = {"--longOptionWithArg=value1", "value2"};
        Options options = new Options();
        options.addOption("longOptionWithArg", true, "A long option with arg.");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--longOptionWithArg", "value1", "value2"};
        assertArrayEquals(expected, result);

        assertEquals(1, parser.getTokens().size());
        assertEquals("--longOptionWithArg value1 value2", parser.getTokens().get(0));
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = {"--longOptionWithoutArg"};
        Options options = new Options();
        options.addOption("longOptionWithoutArg", true, "A long option without arg.");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--longOptionWithoutArg"};
        assertArrayEquals(expected, result);

        assertEquals(1, parser.getTokens().size());
        assertEquals("--longOptionWithoutArg", parser.getTokens().get(0));
    }
}