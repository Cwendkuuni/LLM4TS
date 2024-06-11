import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        String[] args = {"-a", "1", "-b", "2", "--c", "3"};
        Option optionA = new Option("a", "a").required(false).valueSeparator(' ').hasArg(true);
        Option optionB = new Option("b", "b").required(false).hasArg(true);
        Option optionC = new Option("c", "c").required(false).hasArg(false);
        WriteableCommandLine expected = new WriteableCommandLineImpl(optionC, Arrays.asList(args));

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = new DefaultParser().parse(optionC, args);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(expected, parsedCommandLine);
        Assert.assertEquals("1", expected.getValue(optionA.getLongOpt()));
        Assert.assertEquals("2", expected.getValue(optionB.getLongOpt()));
        Assert.assertNull(expected.getValue(optionC.getLongOpt()));
        Assert.assertFalse(expected.hasOption(optionC));
        Assert.assertTrue(expected.hasOption(optionA));
        Assert.assertTrue(expected.hasOption(optionB));
    }

    @Test
    public void testMissingArgument() {
        String[] args = {"--a"};
        Option optionA = new Option("a", "a").required(true).hasArg(true);

        try {
            new DefaultParser().parse(optionA, args);
            Assert.fail("Should have thrown a ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Missing value for option: a", e.getMessage());
        }
    }

    @Test
    public void testInvalidOption() {
        String[] args = {"--x"};
        Option optionA = new Option("a", "a").required(false).hasArg(false);

        try {
            new DefaultParser().parse(optionA, args);
            Assert.fail("Should have thrown a ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: x", e.getMessage());
        }
    }

    @Test
    public void testOptionWithValue() {
        String[] args = {"--option", "value"};
        Option option = new Option("option", "o").required(false).valueSeparator('=').hasArg(true);

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = new DefaultParser().parse(option, args);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals("value", parsedCommandLine.getValue(option.getLongOpt()));
    }

    @Test
    public void testSwitch() {
        String[] args = {"--switch"};
        Option option = new Option("switch", "s").required(false).hasArg(false);

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = new DefaultParser().parse(option, args);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertTrue(parsedCommandLine.hasOption(option));
    }

    @Test
    public void testProperties() {
        String[] args = {"--property", "key=value"};
        Option option = new Option("property", "p").required(false).valuesSeparator('=').hasArgs(2);

        WriteableCommandLine expected = new WriteableCommandLineImpl(option, Arrays.asList(args));
        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = new DefaultParser().parse(option, args);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(expected, parsedCommandLine);
        Assert.assertEquals("value", expected.getValue(option.getLongOpt()));
        Assert.assertEquals(Collections.singletonMap("key", "value"), expected.getProperties(option));
    }

    @Test
    public void testMixedOptionsAndArguments() {
        String[] args = {"--option1", "arg1", "--option2=value2", "--option3"};
        Option option1 = new Option("option1", "o1").required(false).hasArg(true);
        Option option2 = new Option("option2", "o2").required(false).valuesSeparator('=').hasArgs(1);
        Option option3 = new Option("option3", "o3").required(false);

        WriteableCommandLine expected = new WriteableCommandLineImpl(option3, Arrays.asList(args));

        CommandLine parsedCommandLine = null;
        try {
            parsedCommandLine = new DefaultParser().parse(option3, args);
        } catch (ParseException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertEquals(expected, parsedCommandLine);
        Assert.assertFalse(expected.hasOption(option1));
        Assert.assertEquals("arg1", expected.getValue(option1.getLongOpt()));
        Assert.assertEquals(true, expected.hasOption(option2));
        Assert.assertEquals("value2", expected.getValue(option2.getLongOpt()));
        Assert.assertFalse(expected.hasOption(option3));
    }
}