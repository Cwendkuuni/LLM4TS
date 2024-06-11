import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
        Assert.assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch").required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = Option.builder("s").longOpt("switch").required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test value1 value2"));
        commandLine.addOption(option);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test value1"));
        commandLine.addOption(option);
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("s").longOpt("switch").required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--switch"));
        commandLine.addSwitch(option, true);
        boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("p").longOpt("property").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--property=test"));
        commandLine.addProperty(option, "property", "test");
        String propertyValue = commandLine.getProperty(option, "default");
        Assert.assertEquals("test", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = Option.builder("p").longOpt("property").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--property1=test1 --property2=test2"));
        commandLine.addProperty(option, "property1", "test1");
        commandLine.addProperty(option, "property2", "test2");
        Map properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("test1", properties.get("property1"));
        Assert.assertEquals("test2", properties.get("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("test1", "test2", "test3"));
        Assert.assertEquals("test1 test2 test3", commandLine.toString());
    }

    @Test
    public void testGetListOfOptions() {
        Option option1 = Option.builder("o1").longOpt("option1").required(false).build();
        Option option2 = Option.builder("o2").longOpt("option2").required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Option option1FromList = (Option) options.get(0);
        Option option2FromList = (Option) options.get(1);
        Assert.assertEquals(option1, option1FromList);
        Assert.assertEquals(option2, option2FromList);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1").required(false).build();
        Option option2 = Option.builder("o2").longOpt("option2").required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("o1"));
        Assert.assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("s").longOpt("switch").required(false).build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, false);
        commandLine.setDefaultSwitch(option, true);
        boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("test1 test2 test3"));
        List normalised = commandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("test1", normalised.get(0));
        Assert.assertEquals("test2", normalised.get(1));
        Assert.assertEquals("test3", normalised.get(2));
    }
}