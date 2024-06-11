import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithRequiredShortOption()
    {
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("inputFile")
                .isRequired()
                .hasArg()
                .withArgName("file")
                .create('i');
        options.addOption(option);

        CommandLine cmd = CommandLineParser.parse(options, new String[]{"-i", "test.txt"});
        assertNotNull("Option value should not be null", cmd.getOptionValue("inputFile"));
        assertEquals("test.txt", cmd.getOptionValue("inputFile"));

        try
        {
            CommandLineParser.parse(options, new String[]{"-i"});
            fail("Option 'inputFile' is required");
        }
        catch (ParseException e)
        {
            // expected exception
        }

        try
        {
            CommandLineParser.parse(options, new String[]{"-h"});
            fail("Should have thrown an exception, since we're testing the 'i' option");
        }
        catch (ParseException e)
        {
            // expected exception
        }
    }

    @Test
    public void testOptionBuilderWithOptionalShortOption()
    {
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("inputFile")
                .isRequired(false)
                .hasArg()
                .withArgName("file")
                .create('i');
        options.addOption(option);

        CommandLine cmd = CommandLineParser.parse(options, new String[]{"-i", "test.txt"});
        assertNotNull("Option value should not be null", cmd.getOptionValue("inputFile"));
        assertEquals("test.txt", cmd.getOptionValue("inputFile"));

        CommandLine cmd2 = CommandLineParser.parse(options, new String[]{"-h"});
        assertNull("Option 'inputFile' should be null when not provided");
    }

    @Test
    public void testOptionBuilderWithOptionalLongOption()
    {
        Options options = new Options();
        Option option = OptionBuilder.withLongOpt("inputFile")
                .isRequired(false)
                .hasArg()
                .withArgName("file")
                .create("inputFile");
        options.addOption(option);

        CommandLine cmd = CommandLineParser.parse(options, new String[]{"--inputFile", "test.txt"});
        assertNotNull("Option value should not be null", cmd.getOptionValue("inputFile"));
        assertEquals("test.txt", cmd.getOptionValue("inputFile"));

        CommandLine cmd2 = CommandLineParser.parse(options, new String[]{"--help"});
        assertNull("Option 'inputFile' should be null when not provided");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderMissingLongOpt()
    {
        Options options = new Options();

        OptionBuilder.instance.hasArg().create('i');
    }
}