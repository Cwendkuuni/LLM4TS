import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingValue() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        Option result = commandLine.getOption("longOption");
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value1"));
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value1"));
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Collections.singletonList("value1"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchWithDefaultValue() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        Boolean switchValue = commandLine.getSwitch(option, true);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new PropertyOption("property", true, "defaultValue");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "--property=value"));
        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testToString() {
        Option option = Option.builder("option").longOpt("longOption")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test1", "test2"));
        String result = commandLine.toString();
        String expected = "test1 test2";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder("option1").longOpt("longOption1")
                .required(false).build();
        Option option2 = Option.builder("option2").longOpt("longOption2")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test"));
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        Assert.assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("option1").longOpt("longOption1")
                .required(false).build();
        Option option2 = Option.builder("option2").longOpt("longOption2")
                .required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test"));
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(Arrays.asList("option1", "option2"), optionTriggers);
    }
}