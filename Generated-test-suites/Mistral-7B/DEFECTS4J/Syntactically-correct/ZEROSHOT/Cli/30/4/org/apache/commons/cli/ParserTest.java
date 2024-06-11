package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        // Test case 1: Simple usage with no options
        String[] args = {"MyProgram"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("MyProgram", cmd.getArgs().get(0));

        // Test case 2: Simple usage with options
        args = new String[]{"MyProgram", "--option1", "value1"};
        parser = new DefaultParser();
        cmd = parser.parse(new Options().addOption("o", "option1", true, "Option 1"), args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertTrue(cmd.hasOption("option1"));

        // Test case 3: Usage with multiple options
        args = new String[]{"MyProgram", "--option1", "value1", "--option2", "value2"};
        parser = new DefaultParser();
        cmd = parser.parse(new Options().addOption("o", "option1", true, "Option 1")
                .addOption("o", "option2", true, "Option 2"), args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));

        // Test case 4: Usage with required options
        args = new String[]{"MyProgram", "--option1", "value1", "--option3"};
        parser = new DefaultParser();
        Options options = new Options().addOption("o", "option1", true, "Option 1")
                .addOption("r", "option3", true, "Required Option 3");
        cmd = parser.parse(options, args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertFalse(cmd.hasOption("option3"));
        Assert.assertThrows(MissingOptionException.class, () -> cmd.getOptionValue("option3"));

        // Test case 5: Usage with long options
        args = new String[]{"MyProgram", "--long-option1", "value1", "--long-option2", "value2"};
        parser = new DefaultParser();
        cmd = parser.parse(new Options().addOption("l", "long-option1", true, "Long Option 1")
                .addOption("l", "long-option2", true, "Long Option 2"), args, null, false);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("long-option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("long-option2"));
        Assert.assertTrue(cmd.hasOption("long-option1"));
        Assert.assertTrue(cmd.hasOption("long-option2"));

        // Test case 6: Usage with options and arguments
        args = new String[]{"MyProgram", "--option1", "value1", "arg1", "--option2", "value2"};
        parser = new DefaultParser();
        cmd = parser.parse(new Options().addOption("o", "option1", true, "Option 1")
                .addOption("a", "option2", true, "Option 2 with argument"), args, null, false);
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));

        // Test case 7: Usage with multiple values for an option
        args = new String[]{"MyProgram", "--option", "value1", "--option", "value2"};
        parser = new DefaultParser();
        cmd = parser.parse(new Options().addOption("o", "option", true, "Option with multiple values"), args, null, false);
        List<String> values = (List<String>) cmd.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertTrue(cmd.hasOption("option"));

        // Test case 8: Usage with options and properties
        Properties properties = new Properties();
        properties.put("option1", "value1");
        properties.put("option2.value", "value2");
        args = new String[]{"MyProgram"};
        parser = new DefaultParser();
        cmd = parser.parse(new Options().addOption("o", "option1", true, "Option 1")
                .addOption("p", "option2.value", true, "Option 2 with value"), args, properties, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2.value"));
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2.value"));

        // Test case 9: Usage with groups and required options
        args = new String[]{"MyProgram", "--group1", "--option1", "value1", "--group2", "--option3"};
        parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("Group 1");
        group1.setRequired(true);
        group1.addOption(new Option("g1", "option1", true, "Option 1 in group 1"));
        options.addOptionGroup(group1);
        OptionGroup group2 = new OptionGroup("Group 2");
        group2.addOption(new Option("g2", "option3", true, "Option 3 in group 2"));
        options.addOptionGroup(group2);
        cmd = parser.parse(options, args, null, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertFalse(cmd.hasOption("option3"));
        Assert.assertThrows(MissingOptionException.class, () -> cmd.getOptionValue("option3"));
        Assert.assertTrue(group1.isSelected());
        Assert.assertTrue(group2.isSelected());
    }
}