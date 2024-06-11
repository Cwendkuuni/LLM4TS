package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestUtils;

public class WriteableCommandLineImplTest {

    @org.junit.Test
    public void testWriteableCommandLineImpl() {
        Option opt = new Option("a", "anOption", true, "a description")
                .setValueSeparator(';')
                .setArgName("anArgument")
                .setLongOpt("anOtherOption");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Arrays.asList("a_value;another_value"));

        // test hasOption
        assertTrue(cmd.hasOption(opt));

        // test getOption
        Option optRet = cmd.getOption("anOption");
        assertSame(opt, optRet);

        // test getValues
        List values = cmd.getValues(opt, Collections.emptyList());
        assertEquals(Arrays.asList("a_value", "another_value"), values);

        // test getUndefaultedValues
        List unfaultedValues = cmd.getUndefaultedValues(opt);
        assertEquals(Arrays.asList("a_value", "another_value"), unfaultedValues);

        // test getSwitch
        Boolean switchValue = cmd.getSwitch(opt, false);
        assertFalse(switchValue);

        // test addProperty
        cmd.addProperty(opt, "aProperty", "aValue");
        Properties properties = (Properties) cmd.optionToProperties.get(opt);
        assertEquals("aValue", properties.getProperty("aProperty"));

        // test getProperty
        String propertyValue = cmd.getProperty(opt, "aProperty", "defaultValue");
        assertEquals("aValue", propertyValue);

        // test getProperties
        Set<String> propertiesKeys = cmd.getProperties(opt);
        assertEquals(Collections.singleton("aProperty"), propertiesKeys);

        // test getProperties
        Set<String> allPropertiesKeys = cmd.getProperties();
        assertTrue(allPropertiesKeys.isEmpty());

        // test looksLikeOption
        String trigger = "a";
        assertTrue(cmd.looksLikeOption(trigger));

        // test toString
        String commandLine = cmd.toString();
        assertEquals("anOption anArgument a_value anOtherOption another_value", commandLine);

        // test getOptions
        List<Option> options = cmd.getOptions();
        assertEquals(1, options.size());
        assertSame(opt, options.get(0));

        // test getOptionTriggers
        Set<String> optionTriggers = cmd.getOptionTriggers();
        assertEquals(Collections.singleton("anOption"), optionTriggers);

        // test setDefaultValues
        cmd.setDefaultValues(opt, Arrays.asList("defaultValue1", "defaultValue2"));
        List defaultValues = (List) cmd.defaultValues.get(opt);
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), defaultValues);

        // test setDefaultSwitch
        cmd.setDefaultSwitch(opt, true);
        Boolean defaultSwitch = (Boolean) cmd.defaultSwitches.get(opt);
        assertTrue(defaultSwitch);

        // test getNormalised
        List normalised = cmd.getNormalised();
        assertEquals(Arrays.asList("anOption", "anArgument", "a_value",
                "anOtherOption", "another_value"), normalised);

        // test addOption
        Option opt2 = new Option("b", "anOtherOption");
        cmd.addOption(opt2);
        List options2 = cmd.getOptions();
        assertEquals(2, options2.size());
        assertSame(opt, options2.get(0));
        assertSame(opt2, options2.get(1));

        // test addValue
        cmd.addValue(opt, "newValue");
        List values2 = cmd.getValues(opt, null);
        assertEquals(Arrays.asList("a_value", "another_value", "newValue"), values2);

        // test addSwitch
        cmd.addSwitch(opt, true);
        Boolean switchValue2 = cmd.getSwitch(opt, false);
        assertTrue(switchValue2);

        // test constructor
        Option opt3 = new Option("c", "anOption", false, "a description")
                .setLongOpt("anOtherOption");

        WriteableCommandLineImpl cmd2 = new WriteableCommandLineImpl(opt3);
        List options3 = cmd2.getOptions();
        assertEquals(1, options3.size());
        assertSame(opt3, options3.get(0));

        cmd2.addValue(opt3, "value1");
        List values3 = cmd2.getValues(opt3, null);
        assertEquals(Arrays.asList("value1"), values3);

        cmd2.addSwitch(opt3, true);
        Boolean switchValue3 = cmd2.getSwitch(opt3, false);
        assertTrue(switchValue3);
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithNullArguments() {
        Option opt = new Option("a", "anOption", true, "a description")
                .setValueSeparator(';')
                .setArgName("anArgument")
                .setLongOpt("anOtherOption");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt, null);
        assertEquals(Collections.emptyList(), cmd.getOptions());
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithEmptyArguments() {
        Option opt = new Option("a", "anOption", true, "a description")
                .setValueSeparator(';')
                .setArgName("anArgument")
                .setLongOpt("anOtherOption");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Collections.emptyList());
        assertEquals(Collections.emptyList(), cmd.getOptions());
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithOptionsWithoutArguments() {
        Option opt = new Option("a", "anOption", false, "a description")
                .setLongOpt("anOtherOption");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Collections.emptyList());
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithOptionsWithArguments() {
        Option opt = new Option("a", "anOption", true, "a description")
                .setValueSeparator(';')
                .setArgName("anArgument")
                .setLongOpt("anOtherOption");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Arrays.asList("arg1;arg2"));
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
        List values = cmd.getValues(opt, null);
        assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithOptionsWithArgumentsAndSwitches() {
        Option opt = new Option("a", "anOption", true, "a description")
                .setValueSeparator(';')
                .setArgName("anArgument")
                .setLongOpt("anOtherOption")
                .setRequiredOption(true);

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Arrays.asList("-a", "arg1;arg2"));
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
        List values = cmd.getValues(opt, null);
        assertEquals(Arrays.asList("arg1", "arg2"), values);
        assertTrue(cmd.getSwitch(opt, false));
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithMultiValueOptions() {
        Option opt = new Option("a", "anOption", true, "a description")
                .setValueSeparator(';')
                .setArgName("anArgument")
                .setLongOpt("anOtherOption")
                .setMultiValued(true);

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Arrays.asList("-a", "arg1", "-a", "arg2"));
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
        List values = cmd.getValues(opt, null);
        assertEquals(Arrays.asList("arg1", "arg2"), values);
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithPropertyOptions() {
        Option opt = new Option("a", "anOption", false, "a description")
                .setLongOpt("anOtherOption")
                .setValues(new String[]{"value1", "value2"})
                .setArgName("anArgument")
                .setPropertyNames(new String[]{"property1", "property2"})
                .setValueSeparator(';');

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Arrays.asList("-a", "--property1=value3;--property2=value4"));
        assertEquals(Collections.singletonList(opt), cmd.getOptions());
        Properties properties = (Properties) cmd.optionToProperties.get(opt);
        assertEquals(2, properties.size());
        assertEquals("value3", properties.getProperty("property1"));
        assertEquals("value4", properties.getProperty("property2"));
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithHiddenOptions() {
        Option opt = new Option("a", "anOption", false, "a description")
                .setHidden(true)
                .setLongOpt("anOtherOption")
                .setValues(new String[]{"value1", "value2"})
                .setArgName("anArgument")
                .setValueSeparator(';');

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Arrays.asList("-a", "--property1=value3;--property2=value4"));
        assertEquals(Collections.emptyList(), cmd.getOptions());
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithDefaultValues() {
        Option opt = new Option("a", "anOption", true, "a description")
                .setValueSeparator(';')
                .setArgName("anArgument")
                .setLongOpt("anOtherOption")
                .setDefaultValue("defaultValue");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt,
                Arrays.asList("arg1;arg2"));
        List values = cmd.getValues(opt, null);
        assertEquals(Arrays.asList("arg1", "arg2", "defaultValue"), values);
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithDefaultSwitch() {
        Option opt = new Option("a", "anOption", false, "a description")
                .setLongOpt("anOtherOption")
                .setDefaultValue(true);

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt);
        assertTrue(cmd.getSwitch(opt, false));
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithNoDefaultValues() {
        Option opt = new Option("a", "anOption", false, "a description")
                .setLongOpt("anOtherOption")
                .setDefaultValue(null);

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt);
        assertNull(cmd.getSwitch(opt, false));
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithNoDefaultSwitch() {
        Option opt = new Option("a", "anOption", false, "a description")
                .setLongOpt("anOtherOption")
                .setDefaultValue(false);

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt);
        assertFalse(cmd.getSwitch(opt, false));
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithNoValues() {
        Option opt = new Option("a", "anOption", false, "a description")
                .setLongOpt("anOtherOption");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt);
        assertNull(cmd.getValues(opt, null));
    }

    @org.junit.Test
    public void testWriteableCommandLineImplWithNoValuesAndNoDefaultValues() {
        Option opt = new Option("a", "anOption", false, "a description")
                .setLongOpt("anOtherOption");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(opt);
        assertNull(cmd.getValues(opt, null));
        assertFalse(cmd.getSwitch(opt, false));
    }
}