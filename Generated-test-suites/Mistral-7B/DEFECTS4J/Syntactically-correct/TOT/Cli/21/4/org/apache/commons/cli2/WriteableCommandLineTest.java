import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private CommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        commandLine = new DefaultParser().parse(options, new String[]{"--test", "--input", "test.txt"});
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("i").longOpt("input").valueRequired(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List<String> values = commandLine.getValuesForOption("input");
        Assert.assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("output").build();
        commandLine.addOption(option);
        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("output").build();
        List<String> defaults = new ArrayList<>();
        defaults.add("defaultOutput.txt");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getValuesForOption("output");
        Assert.assertEquals(defaults.get(0), values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("d").longOpt("debug").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("d").longOpt("debug").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.hasOption("debug"));
        Assert.assertEquals(true, commandLine.getOptionValue("debug", false));
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        commandLine.addProperty(option, "testProperty", "testValue");
        String value = commandLine.getOptionValue("property");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testAddPropertyDefault() {
        commandLine.addProperty("testProperty", "testValue");
        String value = commandLine.getOptionValue("property");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("--test"));
        Assert.assertFalse(commandLine.looksLikeOption("--testValue"));
    }

    @Test
    public void testParseWithInvalidArgument() {
        String[] args = new String[]{"--test", "--invalid"};
        try {
            commandLine = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            return;
        }
        Assert.fail();
    }
}