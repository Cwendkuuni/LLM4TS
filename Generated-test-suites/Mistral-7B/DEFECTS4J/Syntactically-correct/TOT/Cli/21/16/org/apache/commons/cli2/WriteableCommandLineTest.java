import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * WriteableCommandLineTest
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultParser().parse(TestOption.class);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "Output file name");
        commandLine.addOption(option);

        Assert.assertEquals(0, commandLine.getOptions().size());
        Assert.assertEquals(1, commandLine.getOptions().size());

        Option addedOption = commandLine.getOption("o");
        Assert.assertEquals("output", addedOption.getLongOpt());
        Assert.assertEquals(true, addedOption.getValueSpecifier() instanceof StringValueSpecifier);
        Assert.assertEquals("Output file name", addedOption.getDescription());
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("i", "input", 1, "Input file name");
        commandLine.addOption(option);
        commandLine.addValue(option, "input1.txt");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("input1.txt", values.get(0));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("i", "input", 1, "Input file name");
        commandLine.addOption(option);
        commandLine.addValue(option, "input1.txt");

        List values = commandLine.getValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("input1.txt", values.get(0));

        commandLine.addValue(option, "input2.txt");
        values = commandLine.getValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("input1.txt", values.get(0));
        Assert.assertEquals("input2.txt", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode");
        commandLine.addOption(option);

        Assert.assertFalse(commandLine.getValue(option.getLongOpt()));

        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getValue(option.getLongOpt()));

        try {
            commandLine.addSwitch(option, true);
            Assert.fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode");
        commandLine.addOption(option);

        Assert.assertFalse(commandLine.getValue(option.getLongOpt()));

        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getDefaultValue(option.getLongOpt()));

        Assert.assertFalse(commandLine.getValue(option.getLongOpt()));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "Property value");
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getValue(option.getLongOpt()));

        commandLine.addProperty(option, "propertyName2", "propertyValue2");
        Assert.assertEquals("propertyValue2", commandLine.getValue(option.getLongOpt()));

        commandLine.addProperty(option, "propertyName", "propertyValue3");
        Assert.assertEquals("propertyValue3", commandLine.getValue(option.getLongOpt()));
    }

    @Test
    public void testAddDefaultProperty() {
        Option option = new Option("p", "property", true, "Property value");

        commandLine.addProperty(option, "propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", commandLine.getDefaultValue(option.getLongOpt()));

        commandLine.addProperty(option, "propertyName2", "propertyValue2");
        Assert.assertEquals("propertyValue2", commandLine.getDefaultValue(option.getLongOpt()));

        commandLine.setDefaultValues(option, new ArrayList<>());
        Assert.assertNull(commandLine.getDefaultValue(option.getLongOpt()));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("--input"));
        Assert.assertTrue(commandLine.looksLikeOption("-i"));
        Assert.assertFalse(commandLine.looksLikeOption("input"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("d", "debug", false, "Enable debug mode");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }
}

class TestOption {
}