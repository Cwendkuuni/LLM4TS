import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.exceptions.ParseException;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testDefaultValues() throws ParseException {
        Option option = new Option("o", "output", true, "Output file name");
        option.setDefaultValue("default.txt");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        cmd.parse("-o", "test-output.txt");

        Assert.assertEquals("test-output.txt", cmd.getOptionValue("output", option.getDefaultValue()));

        cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        Assert.assertEquals("default.txt", cmd.getOptionValue("output", null));
    }

    @Test
    public void testDefaultSwitches() throws ParseException {
        Option option = new Option("d", "debug", false, "Enable debug mode");
        option.setDefaultValue(Boolean.TRUE);

        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        cmd.parse("--no-debug");

        Assert.assertFalse(cmd.hasOption(option));

        cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        Assert.assertTrue(cmd.hasOption(option, null));
    }

    @Test
    public void testValues() throws ParseException {
        Option option = new Option("i", "input", true, "Input file name");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        cmd.parse("--input file1.txt file2.txt");

        List<String> values = cmd.getValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("file1.txt", values.get(0));
        Assert.assertEquals("file2.txt", values.get(1));
    }

    @Test
    public void testSwitches() throws ParseException {
        Option option = new Option("q", "quiet", false, "Quiet mode");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        cmd.parse("--quiet");

        Assert.assertTrue(cmd.hasOption(option));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        Option option = new Option("x", "invalid", false, "Invalid option");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        cmd.parse("--invalid");
    }

    @Test
    public void testProperties() throws ParseException {
        Option option = new Option("p", "property", true, "Set property value");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        cmd.parse("--property myProperty=value");

        Assert.assertEquals("value", cmd.getProperty("myProperty", null));
    }

    @Test
    public void testToString() throws ParseException {
        Option option = new Option("i", "input", true, "Input file name");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        cmd.parse("--input file1.txt file2.txt");

        Assert.assertEquals("file1.txt file2.txt", cmd.toString());
    }

    @Test
    public void testGetOptions() throws ParseException {
        Option option1 = new Option("i", "input", true, "Input file name");
        Option option2 = new Option("o", "output", true, "Output file name");

        WriteableCommandLine cmd = new WriteableCommandLineImpl(null, Collections.singletonList("test"));
        cmd.parse("--input file1.txt --output file2.txt");

        List<Option> options = cmd.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }
}