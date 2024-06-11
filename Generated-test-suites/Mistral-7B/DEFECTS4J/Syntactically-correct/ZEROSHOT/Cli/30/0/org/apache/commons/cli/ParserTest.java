package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParser() throws Exception
    {
        // Test case for simple usage
        String[] args = new String[]{"-a", "1", "--b", "2", "--c", "3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", true, "b option");
        options.addOption("c", true, "c option");
        parser.parse(options, args, false);

        Assert.assertEquals(args.length, cmd.args().size());
        for (int i = 0; i < args.length; i++) {
            Assert.assertEquals(args[i], cmd.args().get(i));
        }

        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));

        // Test case for required options
        args = new String[]{"--a"};
        options = new Options();
        options.addOption("a", true, "a option").required(true);

        try {
            parser.parse(options, args, false);
            Assert.fail();
        } catch (MissingOptionException e) {
            // expected
        }

        // Test case for mixed options and arguments
        args = new String[]{"--a", "1", "-b", "valueB", "--c=valueC", "arg1", "arg2"};
        options = new Options();
        options.addOption("a", true, "a option").required(true);
        options.addOption("b", true, "b option").hasArg(true);
        options.addOption("c", true, "c option");

        parser.parse(options, args, false);

        Assert.assertEquals(args.length, cmd.args().size());
        Assert.assertEquals("arg1", cmd.args().get(args.length - 2));
        Assert.assertEquals("arg2", cmd.args().get(args.length - 1));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));

        // Test case for long options
        options = new Options();
        options.addOption("longOptionA", true, "long option A");
        args = new String[]{"--longOptionA=valueA"};
        parser.parse(options, args, false);

        Assert.assertEquals(args.length, cmd.args().size());
        Assert.assertTrue(cmd.hasOption("longOptionA"));
        Assert.assertEquals("valueA", cmd.getOptionValue("longOptionA"));

        // Test case for groups
        options = new Options();
        OptionGroup group = new OptionGroup("group1", "group1");
        group.setRequired(true);
        group.addOption("option1", true, "option1");
        group.addOption("option2", true, "option2");
        group.addOption("option3", true, "option3");
        options.addOptionGroup(group);

        args = new String[]{"--group1", "--option1", "--option2", "--option3"};
        parser.parse(options, args, false);

        Assert.assertEquals(args.length, cmd.args().size());
        Assert.assertEquals(Collections.emptyList(), cmd.args());
        Assert.assertTrue(group.isSelected());
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertTrue(cmd.hasOption("option3"));
    }
}