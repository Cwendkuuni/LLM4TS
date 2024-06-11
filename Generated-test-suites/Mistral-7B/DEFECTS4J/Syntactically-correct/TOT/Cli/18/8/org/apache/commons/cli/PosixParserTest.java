import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flatten method with simple options.
     */
    @Test
    public void testSimpleOptions() {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        options.addOption("b", "option-b", false, "Option B");

        String[] arguments = {"-a", "1", "--option-b", "2", "-c", "3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(6, result.length);
        assertEquals("--option-b", result[1]);
        assertEquals("2", result[2]);
        assertEquals("-c", result[3]);
        assertEquals("3", result[4]);
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    /**
     * Test flatten method with long options.
     */
    @Test
    public void testLongOptions() {
        Options options = new Options();
        options.addOption("long-option-a", "long-option-a", true, "Long Option A");

        String[] arguments = {"--long-option-a", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("--long-option-a", result[0]);
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    /**
     * Test flatten method with combination of simple and long options.
     */
    @Test
    public void testCombinationOptions() {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");
        options.addOption("long-option-b", true, "Long Option B");

        String[] arguments = {"-a", "1", "--long-option-b", "2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("--long-option-b", result[1]);
        assertEquals("2", result[2]);
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    /**
     * Test flatten method with optional argument.
     */
    @Test
    public void testOptionalArgument() {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A [arg]");

        String[] arguments = {"-a", "1"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);

        String[] argumentsWithArg = {"-a", "1", "arg"};
        String[] resultWithArg = parser.flatten(options, argumentsWithArg, false);

        assertEquals(2, resultWithArg.length);
        assertEquals("-a", resultWithArg[0]);
        assertEquals("arg", resultWithArg[1]);
    }

    /**
     * Test flatten method with multiple occurrences of the same option.
     */
    @Test
    public void testMultipleOccurrences() {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");

        String[] arguments = {"-a", "1", "-a", "2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("1", result[1]);
        assertEquals("-a", result[2]);
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    /**
     * Test flatten method with stopAtNonOption.
     */
    @Test(expected = ParseException.class)
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");

        String[] arguments = {"-a", "1", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("1", result[1]);

        String[] argumentsWithNonOption = {"-a", "1", "non-option"};
        parser = new PosixParser();
        parser.flatten(options, argumentsWithNonOption, true);

        assertNull(parser.tokens);
        assertEquals(true, parser.eatTheRest);
    }

    /**
     * Test flatten method with bursting.
     */
    @Test
    public void testBursting() {
        Options options = new Options();
        options.addOption("ab", "option-ab", true, "Option AB");

        String[] arguments = {"-ab", "12"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-ab", result[0]);
        assertEquals("12", result[1]);
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }

    /**
     * Test flatten method with combination of bursting and stopAtNonOption.
     */
    @Test
    public void testBurstingAndStopAtNonOption() {
        Options options = new Options();
        options.addOption("ab", "option-ab", true, "Option AB");

        String[] arguments = {"-ab", "12", "non-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(1, result.length);
        assertEquals("non-option", result[0]);
        assertNull(parser.tokens);
        assertEquals(true, parser.eatTheRest);
    }

    /**
     * Test flatten method with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        Options options = new Options();
        options.addOption("a", "option-a", true, "Option A");

        String[] arguments = {"-i", "1"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);

        assertNull(parser.tokens);
        assertEquals(true, parser.eatTheRest);

        String[] argumentsWithInvalidOption = {"-i", "1"};
        try {
            parser.flatten(options, argumentsWithInvalidOption, false);
        } catch (ParseException e) {
            assertEquals("Unrecognized option: -i", e.getMessage());
        }
    }
}