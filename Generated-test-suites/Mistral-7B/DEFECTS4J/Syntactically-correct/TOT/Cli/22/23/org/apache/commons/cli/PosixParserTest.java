import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test flattening of simple options.
     */
    @Test
    public void testSimpleOptions()
    {
        String[] args = new String[]{"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        try
        {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);
        }
        catch (ParseException e)
        {
            // Should not reach here as there are no parse errors.
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    /**
     * Test flattening of an option with an argument.
     */
    @Test
    public void testOptionWithArgument()
    {
        String[] args = new String[]{"--option1=value1", "--option2=value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1")
                .requiresArg()
                .argName("value");
        options.addOption("option2", true, "o2")
                .requiresArg()
                .argName("value");

        PosixParser parser = new PosixParser();
        try
        {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--option1=value1", "--option2=value2"}, result);
        }
        catch (ParseException e)
        {
            // Should not reach here as there are no parse errors.
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    /**
     * Test handling of non-option tokens.
     */
    @Test
    public void testNonOptionTokens()
    {
        String[] args = new String[]{"--option1", "nonOption1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        try
        {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--option1", "--", "nonOption1", "--option2"}, result);
        }
        catch (ParseException e)
        {
            // Should not reach here as there are no parse errors.
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    /**
     * Test handling of invalid option tokens.
     */
    @Test
    public void testInvalidOptionTokens()
    {
        String[] args = new String[]{"--invalidOption", "--option1"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        try
        {
            String[] result = parser.flatten(options, args, false);
            Assert.fail("Expected parse exception, but got: " + Arrays.toString(result));
        }
        catch (ParseException e)
        {
            // Expected parse exception.
        }
    }

    /**
     * Test handling of long options with arguments.
     */
    @Test
    public void testLongOptionsWithArguments()
    {
        String[] args = new String[]{"--longOption1=value1", "--longOption2=value2"};
        Options options = new Options();
        options.addOption("longOption1", true, "lo1")
                .longOpt("long-option1")
                .requiresArg()
                .argName("value");
        options.addOption("longOption2", true, "lo2")
                .longOpt("long-option2")
                .requiresArg()
                .argName("value");

        PosixParser parser = new PosixParser();
        try
        {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--longOption1=value1", "--longOption2=value2"}, result);
        }
        catch (ParseException e)
        {
            // Should not reach here as there are no parse errors.
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    /**
     * Test handling of mixed options and non-options.
     */
    @Test
    public void testMixedOptionsAndNonOptions()
    {
        String[] args = new String[]{"--option1", "--option2", "nonOption1"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        try
        {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{"--option1", "--option2", "--", "nonOption1"}, result);
        }
        catch (ParseException e)
        {
            // Should not reach here as there are no parse errors.
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }

    /**
     * Test handling of stopAtNonOption.
     */
    @Test
    public void testStopAtNonOption()
    {
        String[] args = new String[]{"--option1", "--option2", "nonOption1"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        try
        {
            String[] result = parser.flatten(options, args, true);
            Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);
        }
        catch (ParseException e)
        {
            // Should not reach here as there are no parse errors.
            Assert.fail("Unexpected parse exception: " + e.getMessage());
        }
    }
}