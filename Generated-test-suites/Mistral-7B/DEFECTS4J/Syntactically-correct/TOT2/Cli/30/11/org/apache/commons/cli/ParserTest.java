package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testEmptyArgs() throws ParseException
    {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = new String[0];
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertNull(cmd.getOptions());
        Assert.assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testSimpleOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(1, cmd.getOptions().size());
        Option opt = (Option) cmd.getOptions().get(0);
        Assert.assertEquals("output", opt.getKey());
        Assert.assertEquals("output.txt", Arrays.toString(opt.getValues()));
        Assert.assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testSimpleRequiredOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path").required(true);
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"--input", "input.txt", "-o"};
        try
        {
            parser.parse(options, args, false);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("Missing required option 'o'", e.getMessage());
        }

        String[] argsWithOutput = {"--input", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, argsWithOutput, false);

        Assert.assertEquals(1, cmd.getOptions().size());
        Option opt = (Option) cmd.getOptions().get(0);
        Assert.assertEquals("output", opt.getKey());
        Assert.assertEquals("output.txt", Arrays.toString(opt.getValues()));
        Assert.assertArrayEquals(new String[]{"input.txt"}, cmd.getArgs());
    }

    @Test
    public void testSimpleOptionGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        group.setRequired(true);
        Option option1 = new Option("o1", "output1", true, "Output file path 1");
        group.addOption(option1);
        Option option2 = new Option("o2", "output2", true, "Output file path 2");
        group.addOption(option2);
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        String[] args = {"-g", "output1.txt", "--input", "input.txt"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(1, cmd.getOptions().size());
        OptionGroup groupOption = (OptionGroup) cmd.getOptions().get(0);
        Assert.assertEquals("group", groupOption.getName());
        Option opt1 = (Option) groupOption.getOption("o1");
        Assert.assertEquals("output1", opt1.getKey());
        Assert.assertEquals("output1.txt", Arrays.toString(opt1.getValues()));
        Option opt2 = (Option) groupOption.getOption("o2");
        Assert.assertNull(opt2);
        Assert.assertArrayEquals(new String[]{"input.txt"}, cmd.getArgs());
    }

    @Test
    public void testSimpleOptionWithArg() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        option.setArgName("file");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"-o", "output.txt", "--input", "input.txt"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(1, cmd.getOptions().size());
        Option opt = (Option) cmd.getOptions().get(0);
        Assert.assertEquals("output", opt.getKey());
        Assert.assertEquals("output.txt", Arrays.toString(opt.getValues()));
        Assert.assertArrayEquals(new String[]{"input.txt"}, cmd.getArgs());
    }

    @Test
    public void testMixedOptions() throws ParseException
    {
        Options options = new Options();
        Option option1 = new Option("o1", "output1", true, "Output file path 1");
        Option option2 = new Option("o2", "output2", true, "Output file path 2");
        Option option3 = new Option("i", "input", true, "Input file path");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        String[] args = {"-i", "input.txt", "--output1", "output1.txt", "--output2", "output2.txt"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(3, cmd.getOptions().size());
        Option opt1 = (Option) cmd.getOptions().get(0);
        Assert.assertEquals("output1", opt1.getKey());
        Assert.assertEquals("output1.txt", Arrays.toString(opt1.getValues()));
        Option opt2 = (Option) cmd.getOptions().get(1);
        Assert.assertEquals("output2", opt2.getKey());
        Assert.assertEquals("output2.txt", Arrays.toString(opt2.getValues()));
        Option opt3 = (Option) cmd.getOptions().get(2);
        Assert.assertEquals("input", opt3.getKey());
        Assert.assertEquals("input.txt", Arrays.toString(opt3.getValues()));
        Assert.assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testMissingOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"--input", "input.txt"};
        try
        {
            parser.parse(options, args, false);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("Missing option 'o'", e.getMessage());
        }
    }

    @Test
    public void testMissingValueForRequiredOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path").required(true);
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"--input", "input.txt"};
        try
        {
            parser.parse(options, args, false);
            Assert.fail();
        }
        catch (MissingArgumentException e)
        {
            Assert.assertEquals("The option 'o' requires an argument.", e.getMessage());
        }
    }

    @Test
    public void testInvalidValueForOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        option.setValuesPattern("[a-z]");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"-o", "123", "--input", "input.txt"};
        try
        {
            parser.parse(options, args, false);
            Assert.fail();
        }
        catch (IllegalArgumentException e)
        {
            Assert.assertEquals("The value '123' does not match the pattern: [a-z]", e.getMessage());
        }
    }

    @Test
    public void testMultipleValuesForOption() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file paths");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"-o", "output1.txt", "-o", "output2.txt", "--input", "input.txt"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(1, cmd.getOptions().size());
        Option opt = (Option) cmd.getOptions().get(0);
        Assert.assertEquals("output", opt.getKey());
        Assert.assertArrayEquals(new String[]{"output1.txt", "output2.txt"}, opt.getValues());
        Assert.assertArrayEquals(new String[]{"input.txt"}, cmd.getArgs());
    }

    @Test
    public void testMultipleValuesForOptionWithArg() throws ParseException
    {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        option.setArgName("files");
        options.addOption(option);

        Parser parser = new DefaultParser();
        String[] args = {"-o", "output1.txt,output2.txt", "--input", "input.txt"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(1, cmd.getOptions().size());
        Option opt = (Option) cmd.getOptions().get(0);
        Assert.assertEquals("output", opt.getKey());
        List<String> values = Arrays.asList(opt.getValues());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("output1.txt", values.get(0));
        Assert.assertEquals("output2.txt", values.get(1));
        Assert.assertArrayEquals(new String[]{"input.txt"}, cmd.getArgs());
    }
}