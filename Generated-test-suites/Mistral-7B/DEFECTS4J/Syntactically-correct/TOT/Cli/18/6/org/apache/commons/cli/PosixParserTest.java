import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link PosixParser} class.
 */
public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Output A");
        options.addOption("b", 1, "Output B with an argument");
        options.addOption("c", 2, "Output C with two arguments");
        options.addOption("d", 3, "Output D with three arguments");
    }

    /**
     * Tests parsing simple options.
     */
    @Test
    public void testSimpleOptions() {
        String[] args = {"--output", "A", "--output", "B", "--output", "C"};
        String[] expectedArgs = {"--output", "A", "--output", "B", "--output", "C"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling multiple options with the same id.
     */
    @Test
    public void testSameOptionIdWithMultipleValues() {
        String[] args = {"--output", "A", "--output", "B", "--output", "C", "--output", "D"};
        String[] expectedArgs = {"--output", "A", "--output", "B", "--output", "C", "--output", "D"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling long options.
     */
    @Test
    public void testLongOptions() {
        String[] args = {"--long-output", "ValueA", "--long-output", "ValueB"};
        String[] expectedArgs = {"--long-output", "ValueA", "--long-output", "ValueB"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling short options.
     */
    @Test
    public void testShortOptions() {
        String[] args = {"-a", "--output", "A", "-b", "ValueB", "-c", "ValueC", "-d", "ValueD"};
        String[] expectedArgs = {"-a", "--output", "A", "-b", "ValueB", "-c", "ValueC", "-d", "ValueD"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling short options with arguments.
     */
    @Test
    public void testShortOptionsWithArguments() {
        String[] args = {"-c", "ValueC1", "-c", "ValueC2"};
        String[] expectedArgs = {"-c", "ValueC1", "-c", "ValueC2"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling short options with a single argument.
     */
    @Test
    public void testShortOptionsWithSingleArgument() {
        String[] args = {"-b", "ValueB"};
        String[] expectedArgs = {"-b", "ValueB"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling options with required arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptionsWithRequiredArguments() {
        String[] args = {"--output"};
        new PosixParser().flatten(options, args, false);
    }

    /**
     * Tests handling long options with required arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLongOptionsWithRequiredArguments() {
        String[] args = {"--long-output"};
        new PosixParser().flatten(options, args, false);
    }

    /**
     * Tests handling short options with required arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShortOptionsWithRequiredArguments() {
        String[] args = {"-c"};
        new PosixParser().flatten(options, args, false);
    }

    /**
     * Tests handling mixed options (short and long)
     */
    @Test
    public void testMixedOptions() {
        String[] args = {"--output", "A", "-b", "ValueB", "-c", "ValueC"};
        String[] expectedArgs = {"--output", "A", "-b", "ValueB", "-c", "ValueC"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling stopAtNonOption flag.
     */
    @Test
    public void testStopAtNonOption() {
        String[] args = {"--output", "A", "-x", "NonOption"};
        String[] expectedArgs = {"--output", "A"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, true));
    }

    /**
     * Tests handling unknown options.
     */
    @Test
    public void testUnknownOptions() {
        String[] args = {"--unknown"};
        String[] expectedArgs = {"--unknown"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling empty arguments.
     */
    @Test
    public void testEmptyArguments() {
        String[] args = {};
        String[] expectedArgs = {};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling arguments with spaces.
     */
    @Test
    public void testArgumentsWithSpaces() {
        String[] args = {"--output", "Value with spaces"};
        String[] expectedArgs = {"--output", "Value with spaces"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }

    /**
     * Tests handling options with multiple values.
     */
    @Test
    public void testOptionsWithMultipleValues() {
        options.addOption("multi", true, "Output multiple");

        String[] args = {"--multi", "--multi"};
        String[] expectedArgs = {"--multi", "--multi"};
        assertArrayEquals(expectedArgs, new PosixParser().flatten(options, args, false));
    }
}