package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('A');
        Assert.assertEquals(1, option.getArgs().length);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('B');
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('C');
        Assert.assertArrayEquals(new String[]{}, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArgsWithValue() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        Option option = optionBuilder.create('D');
        String[] args = {"arg1", "arg2"};
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = commandLineParser.parse(new String[]{"--d", "arg1", "arg2"}, OptionParser.DEFAULT_OPTIONS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(commandLine);
        Assert.assertEquals(args[0], option.getValue(0));
        Assert.assertEquals(args[1], option.getValue(1));
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("my-long-opt");
        Option option = optionBuilder.create('E');
        Assert.assertEquals("my-long-opt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("This is my option description");
        Option option = optionBuilder.create('F');
        Assert.assertEquals("This is my option description", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create('G');
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create('H');
        Assert.fail("An IllegalArgumentException should have been thrown");
    }
}