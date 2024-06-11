import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("b", "bval", true);
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Option optionFromCommandLine = (Option) options.get(0);
        Assert.assertEquals("b", optionFromCommandLine.getLongOpt());
        Assert.assertEquals("bval", optionFromCommandLine.getValueDescriptor());
        Assert.assertTrue(optionFromCommandLine.isRequired());
        Assert.assertTrue(optionFromCommandLine.isValuePresentRequired());
        Assert.assertFalse(optionFromCommandLine.isHidden());
        Assert.assertFalse(optionFromCommandLine.isAllowedMultiple());
        Assert.assertFalse(optionFromCommandLine.isAllowedUnlimitedValues());
        Assert.assertFalse(optionFromCommandLine.isOptional());
        Assert.assertFalse(optionFromCommandLine.isArgNameRequired());
        Assert.assertTrue(optionFromCommandLine.isValueRequired());
        Assert.assertFalse(optionFromCommandLine.isDeferredValued());
        Assert.assertFalse(optionFromCommandLine.isMetaCharAllowed());
        Assert.assertFalse(optionFromCommandLine.isExcludes());
        Assert.assertFalse(optionFromCommandLine.isExcludesValues());
        Assert.assertEquals(Collections.singletonList('b'), optionFromCommandLine.getOptions());
        Assert.assertNull(optionFromCommandLine.getSpecifier());
        Assert.assertNull(optionFromCommandLine.getArgName());

        List triggers = option.getTriggers();
        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals("b", triggers.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", "aval", true);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        commandLine.addValue(option, "value3");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, false);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, false);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, false);
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));

        WriteableCommandLineImpl commandLineWithDifferentOption = new WriteableCommandLineImpl(new Option("b"), Collections.emptyList());
        Option differentOption = new Option("a", null, false);
        commandLineWithDifferentOption.addOption(differentOption);

        Assert.assertFalse(commandLine.hasOption(differentOption));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, false);
        commandLine.addOption(option);

        Option optionFromCommandLine = commandLine.getOption("a");
        Assert.assertEquals(option, optionFromCommandLine);

        Option differentOption = new Option("b", null, false);
        commandLine.addOption(differentOption);

        Option optionFromCommandLineWithDifferentOption = commandLine.getOption("b");
        Assert.assertNull(optionFromCommandLineWithDifferentOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, true);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        List emptyValues = commandLine.getValues(option, null);
        Assert.assertEquals(0, emptyValues.size());

        List defaultValues = new ArrayList() {{
            add("defaultValue1");
            add("defaultValue2");
        }};

        List valuesWithDefaults = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(2, valuesWithDefaults.size());
        Assert.assertEquals("value1", valuesWithDefaults.get(0));
        Assert.assertEquals("value2", valuesWithDefaults.get(1));
        Assert.assertEquals("defaultValue1", valuesWithDefaults.get(2));
        Assert.assertEquals("defaultValue2", valuesWithDefaults.get(3));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, true);
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List unfaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, unfaultedValues.size());

        commandLine.setDefaultValues(option, Collections.singletonList("defaultValue1"));
        unfaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, unfaultedValues.size());

        commandLine.addValue(option, "value3");
        unfaultedValues = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, unfaultedValues.size());
        Assert.assertEquals("value3", unfaultedValues.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, false);
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);

        commandLine.setDefaultSwitch(option, true);
        switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.setDefaultSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, false);
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");

        String propertyValue = commandLine.getProperty(option, "property1", "defaultValue");
        Assert.assertEquals("value1", propertyValue);

        commandLine.addProperty(option, "property2", "value2");
        propertyValue = commandLine.getProperty(option, "property2", "defaultValue");
        Assert.assertEquals("value2", propertyValue);

        commandLine.addProperty(option, "property1", "value3");
        propertyValue = commandLine.getProperty(option, "property1", "defaultValue");
        Assert.assertEquals("value3", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", null, false);
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));

        Set emptyProperties = commandLine.getProperties(new Option("b"));
        Assert.assertEquals(0, emptyProperties.size());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a", null, false));
        commandLine.addOption(new Option("b", "bval", true));
        commandLine.addOption(new Option("c", null, false));
        commandLine.addOption(new Option("d", null, false));
        commandLine.addValue(new Option("b"), "value1");
        commandLine.addValue(new Option("b"), "value2");
        commandLine.addSwitch(new Option("a"), true);
        commandLine.addSwitch(new Option("c"), false);
        commandLine.addProperty(new Option("a"), "property1", "value1");
        commandLine.addProperty(new Option("a"), "property2", "value2");

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "-a -b value1 value2 -c";
        Assert.assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a", null, false));
        commandLine.addOption(new Option("b", "bval", true));
        commandLine.addOption(new Option("c", null, false));
        commandLine.addOption(new Option("d", null, false));

        List options = commandLine.getOptions();
        Assert.assertEquals(4, options.size());
        Assert.assertTrue(options.contains(new Option("a", null, false)));
        Assert.assertTrue(options.contains(new Option("b", "bval", true)));
        Assert.assertTrue(options.contains(new Option("c", null, false)));
        Assert.assertTrue(options.contains(new Option("d", null, false)));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a", null, false));
        commandLine.addOption(new Option("b", "bval", true));
        commandLine.addOption(new Option("c", null, false));
        commandLine.addOption(new Option("d", null, false));

        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(4, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("a"));
        Assert.assertTrue(optionTriggers.contains("b"));
        Assert.assertTrue(optionTriggers.contains("c"));
        Assert.assertTrue(optionTriggers.contains("d"));
    }
}