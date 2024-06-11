import org.apache.commons.cli2.BaseOption;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.util.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for WriteableCommandLine interface
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        commandLine = TestUtils.createWriteableCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("v").valueSeparator(';').buildValueSet();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("u").build();
        commandLine.setDefaultValues(option, new ArrayList<>());
        commandLine.addValue(option, "defaultValue");
        commandLine.addValue(option, "value1");

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        values = commandLine.getValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue", values.get(0));
        Assert.assertEquals("value1", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("d").build();
        List<String> values = new ArrayList<>();
        values.add("defaultValue1");
        values.add("defaultValue2");
        commandLine.setDefaultValues(option, values);

        List<String> defaultValues = commandLine.getValues(option);
        Assert.assertEquals(2, defaultValues.size());
        Assert.assertEquals("defaultValue1", defaultValues.get(0));
        Assert.assertEquals("defaultValue2", defaultValues.get(1));

        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, undefaultedValues.size());
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").build();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.getValues().get(0));

        try {
            commandLine.addSwitch(option, false);
            Assert.fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("sw").build();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.getValues().get(0));

        Option option2 = Option.builder("sw2").build();
        commandLine.setDefaultSwitch(option2, false);
        Assert.assertFalse(option2.getValues().get(0));
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").build();
        commandLine.addProperty(option, "property", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getProperty(option, "property"));

        commandLine.addProperty(option, "property", "newValue");
        Assert.assertEquals("newValue", commandLine.getProperty(option, "property"));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addProperty("defaultProperty", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getProperty("defaultProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetCurrentOption() {
        Option option = Option.builder("t").build();
        ParseContext parseContext = new ParseContext();
        commandLine.parse(parseContext, options.toArray(new Option[0]));
        commandLine.setCurrentOption(option);

        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option, currentOption);
    }

    @Test
    public void testSetCurrentOption() {
        Option option = Option.builder("t").build();
        commandLine.setCurrentOption(option);

        Option newCurrentOption = Option.builder("n").build();
        commandLine.setCurrentOption(newCurrentOption);

        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(newCurrentOption, currentOption);
    }
}