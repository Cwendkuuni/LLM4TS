import org.apache.commons.cli2.BaseComponent;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultParser().parse(OptionBuilder.withName("test")
                .createParser());
    }

    @Test
    public void testAddOption() {
        Option option = OptionBuilder.withName("option")
                .createOption();
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = OptionBuilder.withName("value")
                .createOption();
        commandLine.addOption(option);
        commandLine.addValue(option, "test");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals("test", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueWithOptionNotPresent() {
        Option option = OptionBuilder.withName("value")
                .createOption();
        commandLine.addValue(option, "test");
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = OptionBuilder.withName("values")
                .createOption();
        commandLine.addOption(option);
        commandLine.addValue(option, "test1");
        commandLine.addValue(option, "test2");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("test1", values.get(0));
        Assert.assertEquals("test2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withName("defaultValues")
                .createOption();
        List defaults = new ArrayList<>(Arrays.asList("default1", "default2"));
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withName("switch")
                .createOption();
        commandLine.addSwitch(option, true);
        Assert.assertTrue(option.getValues().get(0).booleanValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = OptionBuilder.withName("switch")
                .createOption();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withName("defaultSwitch")
                .createOption();
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(option.getValues().get(0).booleanValue());
    }

    @Test
    public void testAddProperty() {
        Option option = OptionBuilder.withName("property")
                .createOption();
        commandLine.addProperty(option, "propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", option.getValues().get(0).getString(BaseComponent.DEFAULT));
    }

    @Test
    public void testAddPropertyToDefault() {
        commandLine.addProperty("defaultProperty", "defaultValue");
        Assert.assertEquals("defaultValue", commandLine.getDefaultProperty("defaultProperty"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertFalse(commandLine.looksLikeOption("--option value"));
    }

    @Test
    public void testGetCurrentOption() {
        Option option = OptionBuilder.withName("currentOption")
                .createOption();
        commandLine.parse(new String[]{"--currentOption", "value"});
        Option currentOption = commandLine.getCurrentOption();
        Assert.assertEquals(option, currentOption);
    }
}