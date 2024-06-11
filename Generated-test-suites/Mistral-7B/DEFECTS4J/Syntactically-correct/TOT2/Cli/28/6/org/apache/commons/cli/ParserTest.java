package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSimpleUsage() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-b", "2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);

        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"-c"};
        Parser parser = new DefaultParser();
        parser.setOptions(newOptions());
        parser.parse(args);
        fail("MissingRequiredOptionException expected");
    }

    @Test
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = new String[]{"--x", "-a", "1"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);
        assertNull(cmd.getOptionValue("x"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testUnrecognizedOptionWithValue() throws ParseException
    {
        String[] args = new String[]{"--x", "1", "-a", "1"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);
        assertNull(cmd.getOptionValue("x"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("1", cmd.getArgs()[0]);
    }

    @Test(expected = ParseException.class)
    public void testMultipleValuesForSingleValueOption() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-a", "2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);
        fail("MultipleValuesAllowedException expected");
    }

    @Test
    public void testMultipleValuesForMultipleValueOption() throws ParseException
    {
        String[] args = new String[]{"-l", "file1.csv", "-l", "file2.csv"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);
        List values = cmd.getListValues("l");
        assertEquals(2, values.size());
        assertEquals("file1.csv", values.get(0));
        assertEquals("file2.csv", values.get(1));
    }

    @Test
    public void testOptionWithLongArgument() throws ParseException
    {
        String[] args = new String[]{"--long-option", "value"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);
        assertEquals("value", cmd.getOptionValue("longOption"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testLongOptionWithShortSyntax() throws ParseException
    {
        String[] args = new String[]{"-lo", "value"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);
        assertEquals("value", cmd.getOptionValue("longOption"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testMissingValueForOptionalOption() throws ParseException
    {
        String[] args = new String[]{"-a"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);
        assertEquals(null, cmd.getOptionValue("a"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testValueForOptionalOption() throws ParseException
    {
        String[] args = new String[]{"-a", "value"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args);
        assertEquals("value", cmd.getOptionValue("a"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testMissingValueForOptionalOptionWithDefaultValue() throws ParseException
    {
        String[] args = new String[]{"-a"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptionsWithDefaultValues(), args);
        assertEquals("defaultValue", cmd.getOptionValue("a"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testValueForOptionalOptionWithCustomDefaultValue() throws ParseException
    {
        String[] args = new String[]{"-a", "value"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptionsWithCustomDefaultValues(), args);
        assertEquals("value", cmd.getOptionValue("a"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test(expected = ParseException.class)
    public void testMissingValueForRequiredOptionWithDefaultValue() throws ParseException
    {
        String[] args = new String[]{"-a"};
        Parser parser = new DefaultParser();
        parser.setOptions(newOptionsWithRequiredDefaultValues());
        parser.parse(args);
        fail("MissingRequiredOptionException expected");
    }

    @Test
    public void testValueForRequiredOption() throws ParseException
    {
        String[] args = new String[]{"-a", "value"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptionsWithRequiredValues(), args);
        assertEquals("value", cmd.getOptionValue("a"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testValueForRequiredOptionWithValueList() throws ParseException
    {
        String[] args = new String[]{"-a", "value1", "value2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptionsWithRequiredValueList(), args);
        List values = cmd.getListValues("a");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = ParseException.class)
    public void testMissingOptionalGroup() throws ParseException
    {
        String[] args = new String[]{"-g"};
        Parser parser = new DefaultParser();
        parser.setOptions(newOptionsWithGroup());
        parser.parse(args);
        fail("MissingCommandLineException expected");
    }

    @Test(expected = ParseException.class)
    public void testMissingGroupOption() throws ParseException
    {
        String[] args = new String[]{"--group"};
        Parser parser = new DefaultParser();
        parser.setOptions(newOptionsWithGroup());
        parser.parse(args);
        fail("MissingCommandLineException expected");
    }

    @Test
    public void testSelectedGroup() throws ParseException
    {
        String[] args = new String[]{"--group1", "-a"};
        Parser parser = new DefaultParser();
        parser.setOptions(newOptionsWithGroup());
        CommandLine cmd = parser.parse(args);
        OptionGroup group = parser.getOptions().getOptionGroup("group1");
        assertTrue(group.isSelected());
        assertEquals("a", cmd.getOptionValue("a"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testDeselectedGroup() throws ParseException
    {
        String[] args = new String[]{"--group2", "-a"};
        Parser parser = new DefaultParser();
        parser.setOptions(newOptionsWithGroup());
        CommandLine cmd = parser.parse(args);
        OptionGroup group = parser.getOptions().getOptionGroup("group2");
        assertFalse(group.isSelected());
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testMixedGroupUsage() throws ParseException
    {
        String[] args = new String[]{"--group1", "-a", "--group2", "-b"};
        Parser parser = new DefaultParser();
        parser.setOptions(newOptionsWithGroup());
        CommandLine cmd = parser.parse(args);
        OptionGroup group1 = parser.getOptions().getOptionGroup("group1");
        assertTrue(group1.isSelected());
        assertEquals("a", cmd.getOptionValue("a"));
        OptionGroup group2 = parser.getOptions().getOptionGroup("group2");
        assertFalse(group2.isSelected());
        assertEquals("", cmd.getOptionValue("b"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testProperties() throws ParseException
    {
        String[] args = new String[]{"-p", "prop1=value1", "-p", "prop2=value2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptionsWithProperties(), args);
        Properties properties = cmd.getProperties();
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("prop1"));
        assertEquals("value2", properties.getProperty("prop2"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testPropertiesWithDefaultValues() throws ParseException
    {
        String[] args = new String[]{"-p"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptionsWithPropertiesAndDefaultValues(), args);
        Properties properties = cmd.getProperties();
        assertEquals(2, properties.size());
        assertEquals("defaultValue1", properties.getProperty("prop1"));
        assertEquals("defaultValue2", properties.getProperty("prop2"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testStopAtNonOption() throws ParseException
    {
        String[] args = new String[]{"-a", "-n", "argument"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args, true);
        assertEquals("argument", cmd.getArgs()[0]);
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testStopAtNonOptionWithValues() throws ParseException
    {
        String[] args = new String[]{"-a", "-n", "argument1", "argument2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(newOptions(), args, true);
        List argsList = cmd.getArgs();
        assertEquals(2, argsList.size());
        assertEquals("argument1", argsList.get(0));
        assertEquals("argument2", argsList.get(1));
        assertEquals(0, cmd.getOptions().size());
    }

    private static Options newOptions()
    {
        Options options = new Options();
        Option optionA = new Option("a", true, false, "value for option a");
        Option optionB = new Option("b", true, false, "value for option b");
        Option optionLongOption = new Option("longOption", true, false, "value for long option");
        Option optionGroup = new Option("g", "group", false, "select group");
        OptionGroup group1 = new OptionGroup("group1", "group1 description");
        Option optionGroup1 = new Option("group1Option", true, false, "value for group1 option");
        group1.addOption(optionGroup1);
        Option optionGroup2 = new Option("group2Option", true, false, "value for group2 option");
        group1.addOption(optionGroup2);
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionLongOption);
        options.addOptionGroup(group1);
        return options;
    }

    private static Options newOptionsWithDefaultValues()
    {
        Options options = newOptions();
        optionA.setDefaultValue("defaultValue");
        optionB.setDefaultValue("defaultValue");
        return options;
    }

    private static Options newOptionsWithCustomDefaultValues()
    {
        Options options = newOptions();
        optionA.setDefaultValue("customDefaultValue");
        return options;
    }

    private static Options newOptionsWithRequiredValues()
    {
        Options options = newOptions();
        optionA = new Option("a", true, true, "required value for option a");
        optionB = new Option("b", true, true, "required value for option b");
        options.addOption(optionA);
        options.addOption(optionB);
        return options;
    }

    private static Options newOptionsWithRequiredValueList()
    {
        Options options = newOptions();
        optionA = new Option("a", true, false, "list of values for option a");
        optionA.setAllowMultipleValues(true);
        options.addOption(optionA);
        return options;
    }

    private static Options newOptionsWithGroup()
    {
        Options options = newOptions();
        Option optionGroup = new Option("g", "group", false, "select group");
        OptionGroup group = new OptionGroup("group", "group description");
        group.addOption(optionGroup);
        options.addOption(optionGroup);
        options.addOptionGroup(group);
        return options;
    }

    private static Options newOptionsWithProperties()
    {
        Options options = new Options();
        Option optionP = new Option("p", true, false, "property parameter");
        options.addOption(optionP);
        return options;
    }

    private static Options newOptionsWithPropertiesAndDefaultValues()
    {
        Options options = newOptionsWithProperties();
        optionP.setDefaultValue("defaultValue1");
        optionP.addValues("defaultValue2");
        return options;
    }
}