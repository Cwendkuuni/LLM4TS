package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");
        options.addOption("c", 2, "option C with two arguments");
        options.addOption("d", 3, "option D with three arguments");

        String[] args = {"--a", "--b=value", "--c arg1 arg2", "--d arg1 arg2 arg3"};
        DefaultParser parser = new PosixParser();
        try {
            String[] parsedArgs = parser.parse(options, args, true);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "value", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3"}, parsedArgs);
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c", "--d"};
        try {
            String[] parsedArgs = parser.parse(options, args, true);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "--d"}, parsedArgs);
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"-a", "--b=value", "--c arg1 arg2", "--d arg1 arg2 arg3", "--"};
        try {
            String[] parsedArgs = parser.parse(options, args, true);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "value", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3"}, parsedArgs);
            // eat the rest
            Assertions.assertEquals(Arrays.asList("--").equals(Arrays.asList(parsedArgs[parsedArgs.length - 1].split("--"))), true);
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "-b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--"};
        try {
            String[] parsedArgs = parser.parse(options, args, true);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3"}, parsedArgs);
            // eat the rest
            Assertions.assertEquals(Arrays.asList("--").equals(Arrays.asList(parsedArgs[parsedArgs.length - 1].split("--"))), true);
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "invalid"};
        try {
            String[] parsedArgs = parser.parse(options, args, true);
            Assertions.fail();
        } catch (ParseException e) {
            Assertions.assertEquals("unrecognized option: invalid", e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--e"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "--e"}, parsedArgs);
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3"}, parsedArgs);
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--e"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "--e"}, parsedArgs);
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--e"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "--e"}, parsedArgs);
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--e=value"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "--e"}, parsedArgs);
            Assertions.assertEquals(options.getOptionValue("e"), "value");
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--e=value", "-"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "--e"}, parsedArgs);
            // eat the rest
            Assertions.assertEquals(Arrays.asList("--").equals(Arrays.asList(parsedArgs[parsedArgs.length - 1].split("--"))), true);
            Assertions.assertEquals(options.getOptionValue("e"), "value");
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--e=value", "--f"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "--e", "--f"}, parsedArgs);
            Assertions.assertEquals(options.getOptionValue("e"), "value");
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--e=value", "--f"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "--e", "--f"}, parsedArgs);
            Assertions.assertEquals(options.getOptionValue("e"), "value");
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }

        args = new String[]{"--a", "--b", "--c arg1 arg2", "--d arg1 arg2 arg3", "--e=value", "--f=value2"};
        try {
            String[] parsedArgs = parser.parse(options, args, false);
            Assertions.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "arg2", "--d", "arg1", "arg2", "arg3", "--e", "--f"}, parsedArgs);
            Assertions.assertEquals(options.getOptionValue("e"), "value");
            Assertions.assertEquals(options.getOptionValue("f"), "value2");
        } catch (ParseException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testHelp() {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");
        options.parse(new String[0]);

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("PosixParserTest", Arrays.asList(options.getOptions()), true);
        System.out.println(help);
    }
}