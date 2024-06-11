import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("a")
                .longOpt("argument")
                .required(true)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s")
                .longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_alreadySet() {
        Option option = Option.builder("s")
                .longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o")
                .longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        Option result = commandLine.getOption("option");
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("a")
                .longOpt("argument")
                .required(true)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("a")
                .longOpt("argument")
                .required(true)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value1"));
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s")
                .longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder()
                .longOpt("property")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty("property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testToString() {
        Option option = Option.builder("a")
                .longOpt("argument")
                .required(true)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "value1"));
        String result = commandLine.toString();
        String expected = "test value1";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder("o1")
                .longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2")
                .longOpt("option2")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        List expected = Arrays.asList(option1, option2);
        Assert.assertEquals(expected, options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1")
                .longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2")
                .longOpt("option2")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        Set expected = Arrays.asList("o1", "o2").hashCode();
        Assert.assertEquals(expected, optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("a")
                .longOpt("argument")
                .required(true)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s")
                .longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = Option.builder("a")
                .longOpt("argument")
                .required(true)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test argument1"));
        List normalised = commandLine.getNormalised();
        List expected = Arrays.asList("test argument1");
        Assert.assertEquals(expected, normalised);
    }
}