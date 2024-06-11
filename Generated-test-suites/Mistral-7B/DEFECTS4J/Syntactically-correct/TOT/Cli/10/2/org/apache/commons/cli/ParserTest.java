package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testBasicUsage() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, new String[]{"-a", "--b", "3"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(cmd);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.getOptionValue("a"));
        Assert.assertEquals(3, cmd.getOptionValue("b").length());
    }

    @Test
    public void testMissingOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        String[] args = new String[]{"--c"};

        try {
            parser.parse(options, args, null, false);
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Missing option"));
            assertTrue(message.contains("c"));
        }
    }

    @Test
    public void testMissingRequiredOption() {
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        String[] args = new String[]{"--b"};

        try {
            parser.parse(options, args, null, false);
            fail("Should have thrown MissingOptionException");
        } catch (ParseException e) {
            assertTrue(e.getCause() instanceof MissingOptionException);
        }
    }

    @Test
    public void testUnexpectedArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a", "value", "unused"};

        try {
            parser.parse(options, args, null, false);
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
            String message = e.getMessage();
            assertTrue(message.contains("unexpected argument"));
            assertTrue(message.contains("unused"));
        }
    }

    @Test
    public void testOptionGroup() {
        OptionGroup group = new OptionGroup("group", "Option Group");
        group.setRequired(true);

        Options options = new Options();
        options.addOptionGroup(group);
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        String[] args = new String[]{"--group", "--a"};

        try {
            cmd = parser.parse(options, args, null, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertTrue(group.isSelected());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("true"));
    }

    @Test
    public void testProperties() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Properties props = new Properties();
        props.setProperty("a", "true");

        Parser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, null, props, false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(cmd);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.getOptionValue("a").equals("true"));
    }
}