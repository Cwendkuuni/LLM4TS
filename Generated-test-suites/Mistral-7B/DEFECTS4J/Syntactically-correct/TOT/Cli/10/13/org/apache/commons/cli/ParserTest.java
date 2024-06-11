import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Parser} class.
 */
public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();

        options = new Options();
        options.addOption("a", "alpha", true, "Alpha option");
        options.addOption("b", "beta", false, "Beta option");
        options.addOption("c", "char", 'c', "Character option");
        options.addOption("d", "delta", 1, "Delta option with integer value");
        options.addOption("e", "epsilon", double.class, "Epsilon option with double value");
        options.addOptionGroup(new OptionGroup("group1", "Group 1"));
        options.addOption("g1", "group1", false, "Group 1 option", true, "group1-value");
        options.addOption("g2", "group2", false, "Group 2 option", true, "group2-value");
        options.addRequiredOption("r", "required", true, "Required option");
    }

    @Test
    public void testParseWithNoArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue("No options should be present", cmd.getOptions().isEmpty());
        assertTrue("No args should be present", cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException {
        String[] arguments = {"test"};
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue("Test option should not be present", cmd.getOptions().isEmpty());
        assertEquals("Test", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        String[] arguments = {"-a"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertTrue("Alpha option should be present", optionsList.size() > 0);
        Option alphaOption = optionsList.get(0);
        assertTrue("Alpha option should be set", alphaOption.isSet());
        assertFalse("Alpha option should not have value", alphaOption.getValue() == null);
    }

    @Test
    public void testParseWithOptionAndValue() throws ParseException {
        String[] arguments = {"-a", "valueA"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertTrue("Alpha option should be present", optionsList.size() > 0);
        Option alphaOption = optionsList.get(0);
        assertTrue("Alpha option should be set", alphaOption.isSet());
        assertEquals("valueA", alphaOption.getValue());
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        String[] arguments = {"-a", "-b"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertTrue("Alpha option should be present", optionsList.size() > 0);
        Option alphaOption = optionsList.get(0);
        assertTrue("Alpha option should be set", alphaOption.isSet());
        assertTrue("Beta option should be present", optionsList.size() > 1);
        Option betaOption = optionsList.get(1);
        assertFalse("Beta option should not have value", betaOption.getValue() == null);
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] arguments = {"--group1", "group1-value"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertTrue("OptionGroup1 should be present", optionsList.size() > 0);
        Option optionGroup1 = optionsList.get(0);
        assertTrue("OptionGroup1 should be set", optionGroup1.isSet());
        assertEquals("group1-value", optionGroup1.getValue());
    }

    @Test
    public void testParseWithIntegerOption() throws ParseException {
        String[] arguments = {"-d", "5"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertTrue("Delta option should be present", optionsList.size() > 0);
        Option deltaOption = optionsList.get(0);
        assertTrue("Delta option should be set", deltaOption.isSet());
        assertEquals(5, ((Integer) deltaOption.getValue()).intValue());
    }

    @Test
    public void testParseWithDoubleOption() throws ParseException {
        String[] arguments = {"-e", "3.14"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertTrue("Epsilon option should be present", optionsList.size() > 0);
        Option epsilonOption = optionsList.get(0);
        assertTrue("Epsilon option should be set", epsilonOption.isSet());
        assertEquals(3.14, ((Double) epsilonOption.getValue()).doubleValue(), 0.001);
    }

    @Test
    public void testParseWithRequiredOption() throws ParseException {
        String[] arguments = {"-r"};
        CommandLine cmd = parser.parse(options, arguments);
        List<Option> optionsList = cmd.getOptions();
        assertTrue("Required option should be present", optionsList.size() > 0);
        Option requiredOption = optionsList.get(0);
        assertTrue("Required option should be set", requiredOption.isSet());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);
        assertThrows(MissingOptionException.class, () -> cmd.getOption("r"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        String[] arguments = {"-x"};
        CommandLine cmd = parser.parse(options, arguments);
        assertThrows(UnrecognizedOptionException.class, () -> cmd.getOption("x"));
    }
}