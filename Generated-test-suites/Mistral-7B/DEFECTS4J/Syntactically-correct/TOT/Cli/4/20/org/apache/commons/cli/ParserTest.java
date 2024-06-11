import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ParserTest {

    @Test
    public void testBasicUsage() throws ParseException {
        String[] args = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testMissingOption() throws ParseException {
        String[] args = new String[]{"--nonexistent"};
        Options options = new Options();
        options.addOption("a", true, "option A");

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing option: nonexistent", e.getMessage());
        }
    }

    @Test
    public void testMissingArgument() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "option A").requiredArg();

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            Assert.fail();
        } catch (MissingArgumentException e) {
            Assert.assertEquals("Missing argument for option: a", e.getMessage());
        }
    }

    @Test
    public void testInvalidArgument() throws ParseException {
        String[] args = new String[]{"-a", "invalid"};
        Options options = new Options();
        options.addOption("a", true, "option A");

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, args);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid value for option: 'a'. The value 'invalid' is not of the expected type.", e.getMessage());
        }
    }

    @Test
    public void testParseProperties() throws ParseException {
        String[] args = new String[]{"--foo=value1", "--bar=value2"};
        Properties properties = new Properties();
        properties.setProperty("foo", "value1");
        properties.setProperty("bar", "value2");

        Options options = new Options();
        options.addOption("foo", true, "option Foo");
        options.addOption("bar", true, "option Bar");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals(true, cmd.hasOption("foo"));
        Assert.assertEquals("value1", cmd.getOptionValue("foo"));
        Assert.assertEquals(true, cmd.hasOption("bar"));
        Assert.assertEquals("value2", cmd.getOptionValue("bar"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParsePropertiesWithMissingOption() throws ParseException {
        String[] args = new String[]{"--nonexistent=value"};
        Properties properties = new Properties();
        properties.setProperty("nonexistent", "value");

        Options options = new Options();
        options.addOption("foo", true, "option Foo");

        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, args, properties, false);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing option: nonexistent", e.getMessage());
        }
    }
}