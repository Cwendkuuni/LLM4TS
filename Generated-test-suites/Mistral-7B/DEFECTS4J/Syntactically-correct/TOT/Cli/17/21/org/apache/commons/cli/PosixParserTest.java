package org.apache.commons.cli.test;

import org.apache.commons.cli.BasicParseResult;
import org.apache.commons.cli.Commands;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing the PosixParser class of Apache Commons CLI.
 */
public class PosixParserTest {

    @Test
    public void testSimple() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"-a", "--b=3"};

        Parser parser = new PosixParser();
        BasicParseResult result = parser.parse(options, args, false);

        Assert.assertFalse(result.hasOptions());
        Assert.assertEquals(true, result.getBooleanOptionValue("a"));
        Assert.assertEquals(3, result.getOptionValue("b", 0));
    }

    @Test
    public void testDashDash() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--", "-b", "3"};

        Parser parser = new PosixParser();
        BasicParseResult result = parser.parse(options, args, false);

        Assert.assertEquals("Option B", result.getOptions().getOption("b").getLongOpt());
        Assert.assertEquals(3, result.getOptionValue("b", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"-z"};

        Parser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"-a", "--"};

        Parser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"-a", "-b", "3", "--", "-b", "4"};

        Parser parser = new PosixParser();
        BasicParseResult result = parser.parse(options, args, false);

        Assert.assertEquals(2, result.getValues("b").size());
        Assert.assertEquals(3, ((Integer) result.getValues("b").get(0)).intValue());
        Assert.assertEquals(4, ((Integer) result.getValues("b").get(1)).intValue());
    }

    @Test
    public void testLongOptions() {
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", 1, "Long Option B");

        String[] args = {"--longOptionB=4", "--longOptionA", "--"};

        Parser parser = new PosixParser();
        BasicParseResult result = parser.parse(options, args, false);

        Assert.assertEquals("longOptionA", result.getOptions().getOption("longOptionA").getLongOpt());
        Assert.assertEquals(true, result.getBooleanOptionValue("longOptionA"));
        Assert.assertEquals(4, result.getOptionValue("longOptionB", 0));
    }

    @Test
    public void testStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"abcd", "--stop"};

        Parser parser = new PosixParser();
        parser.setStopAtNonOption(true);
        BasicParseResult result = parser.parse(options, args, false);

        Assert.assertEquals(4, result.getArguments().length);
        Assert.assertArrayEquals(new String[]{"abcd"}, result.getArguments());
    }

    @Test
    public void testBursting() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        options.addOption("ac", true, "Option AC");
        options.addOption("bc", 1, "Option BC");

        String[] args = {"-ab", "-ac", "--bc=3"};

        Parser parser = new PosixParser();
        BasicParseResult result = parser.parse(options, args, false);

        Assert.assertEquals("Option AB", result.getOptions().getOption("ab").getLongOpt());
        Assert.assertEquals("Option AC", result.getOptions().getOption("ac").getLongOpt());
        Assert.assertEquals(3, result.getOptionValue("bc", 0));
    }

    @Test
    public void testBurstingWithArguments() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        options.addOption("ac", true, "Option AC");
        options.addOption("bc", 1, "Option BC");

        String[] args = {"-ab", "-ac", "--bc=3--", "arg1", "arg2"};

        Parser parser = new PosixParser();
        BasicParseResult result = parser.parse(options, args, false);

        Assert.assertEquals("Option AB", result.getOptions().getOption("ab").getLongOpt());
        Assert.assertEquals("Option AC", result.getOptions().getOption("ac").getLongOpt());
        Assert.assertEquals(3, result.getOptionValue("bc", 0));
        Assert.assertEquals(2, result.getArguments().length);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, result.getArguments());
    }

    @Test
    public void testHelp() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--help"};

        Parser parser = new PosixParser();
        BasicParseResult result = parser.parse(options, args, false);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java", options);
        System.out.println(formatter.getHelpText());
    }
}