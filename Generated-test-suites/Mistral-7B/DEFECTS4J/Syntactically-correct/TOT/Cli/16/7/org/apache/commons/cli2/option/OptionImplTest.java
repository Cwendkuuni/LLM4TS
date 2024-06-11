import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionImplTest {

    @Test
    public void testOptionWithNoPrefix() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("optionA")
                .withDescription("This is option A")
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(new String[]{"-optionA", "arg"}, cmd);
        Assert.assertEquals("optionA", option.getLongOpt());
        Assert.assertTrue(option.isSet(cmd));
        Assert.assertNull(cmd.getOptionValue(option.getLongOpt()));
    }

    @Test
    public void testOptionWithPrefix() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("prefixOption")
                .withDescription("This is option A with prefix")
                .withPrefix("p")
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(new String[]{"-poptionA", "arg"}, cmd);
        Assert.assertEquals("optionA", option.getLongOpt());
        Assert.assertTrue(option.isSet(cmd));
        Assert.assertNull(cmd.getOptionValue(option.getLongOpt()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidPrefix() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("optionA")
                .withDescription("This is option A")
                .withPrefix("z") // invalid prefix
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(new String[]{"-zoptionA", "arg"}, cmd);
    }

    @Test
    public void testOptionAsRequired() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("optionB")
                .withDescription("This is option B")
                .isRequired()
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        try {
            parser.parse(new String[]{}, cmd);
            Assert.fail("OptionB is required but no argument was provided");
        } catch (ParseException e) {
            // expected
        }

        parser.parse(new String[]{"-optionB", "arg"}, cmd);
        Assert.assertEquals("optionB", option.getLongOpt());
        Assert.assertTrue(option.isSet(cmd));
        Assert.assertEquals("arg", cmd.getOptionValue(option.getLongOpt()));
    }

    @Test(expected = ParseException.class)
    public void testOptionAsNotRequiredButMissing() throws ParseException {
        Option option = new OptionBuilder()
                .withLongOpt("optionC")
                .withDescription("This is option C")
                .build();

        CommandLineParser parser = new DefaultParser();
        WriteableCommandLine cmd = new DefaultWriteableCommandLine();

        parser.parse(new String[]{}, cmd);
        // OptionC should not be set, but no exception should be thrown
        Assert.assertFalse(option.isSet(cmd));
        Assert.assertNull(cmd.getOptionValue(option.getLongOpt()));

        try {
            parser.parse(new String[0], cmd);
            Assert.fail("OptionC should have been required but no argument was provided");
        } catch (ParseException e) {
            // expected
        }
    }
}