import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals("value1", values.get(0));

        writeableCommandLine.addValue(option, "value2");
        values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Assert.assertEquals(option, writeableCommandLine.getOption("trigger"));
    }

    @Test
    public void testGetValues() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addSwitch(option, false);
        Assert.assertFalse(writeableCommandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        when(option.getProperties()).thenReturn(properties);
        String property = writeableCommandLine.getProperty("property");
        Assert.assertEquals("value", property);
    }

    @Test
    public void testAddProperty() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("property", "value");
        when(option.getProperties()).thenReturn(properties);
        writeableCommandLine.addProperty(option, "property2", "value2");
        properties = (Properties) writeableCommandLine.optionToProperties.get(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("value", properties.getProperty("property"));
        Assert.assertEquals("value2", properties.getProperty("property2"));
    }

    @Test
    public void testGetProperties() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        when(option.getProperties()).thenReturn(properties);
        Map propertiesMap = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, propertiesMap.size());
        Assert.assertEquals("value1", propertiesMap.get("property1"));
        Assert.assertEquals("value2", propertiesMap.get("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--option"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-o"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addValue(new Option(), "arg1");
        writeableCommandLine.addValue(new Option(), "arg2");
        String toString = writeableCommandLine.toString();
        Assert.assertEquals("arg1 arg2", toString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(new Option());
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(new Option());
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("trigger"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        writeableCommandLine.setDefaultValues(option, defaults);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = mock(Option.class);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addValue(new Option(), "arg1");
        writeableCommandLine.addValue(new Option(), "arg2");
        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
        Assert.assertEquals("arg2", normalised.get(1));
    }
}