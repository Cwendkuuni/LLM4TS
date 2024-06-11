import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionWithNoPrefix() throws Exception {
        Option option = new OptionBuilder()
                .withLongOpt("long-option")
                .withDescription("A long option test")
                .create();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new WriteableCommandLine();

        try {
            parser.parse(new String[]{"--long-option"}, cmd);
            Assert.assertFalse("Expected an exception, but no exception was thrown.", false);
        } catch (Exception e) {
            // Expected
        }

        Assert.assertEquals("Expected no options to be present.", 0, cmd.getOptions().size());

        // Test with valid argument
        parser.parse(new String[]{"--long-option", "arg"}, cmd);
        Option optionPresent = cmd.getOption(Option.class, "long-option");
        Assert.assertNotNull("Expected an option to be present.", optionPresent);
        Assert.assertEquals("Expected the option value to be 'arg'.", "arg", optionPresent.getValue());
    }

    @Test
    public void testOptionWithPrefix() throws Exception {
        Option option = new OptionBuilder()
                .withLongOpt("long-option")
                .withDescription("A long option test")
                .withPrefix("my-prefix")
                .create();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new WriteableCommandLine();

        try {
            parser.parse(new String[]{"--my-prefix--long-option"}, cmd);
            Assert.assertFalse("Expected an exception, but no exception was thrown.", false);
        } catch (Exception e) {
            // Expected
        }

        Assert.assertEquals("Expected no options to be present.", 0, cmd.getOptions().size());

        // Test with valid argument
        parser.parse(new String[]{"--my-prefix--long-option", "arg"}, cmd);
        Option optionPresent = cmd.getOption(Option.class, "long-option");
        Assert.assertNotNull("Expected an option to be present.", optionPresent);
        Assert.assertEquals("Expected the option value to be 'arg'.", "arg", optionPresent.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidPrefix() throws Exception {
        Option option = new OptionBuilder()
                .withLongOpt("long-option")
                .withDescription("A long option test")
                .withPrefix("invalid-prefix")
                .create();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new WriteableCommandLine();

        parser.parse(new String[]{"--invalid-prefix--long-option"}, cmd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithMissingArgument() throws Exception {
        Option option = new OptionBuilder()
                .withLongOpt("long-option")
                .withDescription("A long option test")
                .isRequired(true)
                .create();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new WriteableCommandLine();

        parser.parse(new String[]{"--long-option"}, cmd);
        Option optionPresent = cmd.getOption(Option.class, "long-option");

        Assert.assertNotNull("Expected an option to be present.", optionPresent);
        Assert.assertNull("Expected the option value to be null.", optionPresent.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithTwoArguments() throws Exception {
        Option option = new OptionBuilder()
                .withLongOpt("long-option")
                .withDescription("A long option test")
                .isRequired(true)
                .create();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new WriteableCommandLine();

        parser.parse(new String[]{"--long-option", "arg1", "arg2"}, cmd);
    }
}