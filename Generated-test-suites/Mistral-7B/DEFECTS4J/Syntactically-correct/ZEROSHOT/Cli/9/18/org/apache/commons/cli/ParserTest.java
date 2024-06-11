package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest {

    /**
     * Test parse method with valid options and arguments.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseValidOptionsAndArguments() throws ParseException {
        String[] args = {"-a", "1", "-b", "2", "--foo", "3", "-c", "4"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", "a", true, "Argument A");
        options.addOption("b", "b", false, "Argument B");
        options.addOption("c", "c", false, "Argument C");
        options.addOption("f", "f", false, "Option F");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getOptionValue("f"));
        Assert.assertEquals(3, cmd.getListOptions().size());
        Assert.assertEquals("2", cmd.getArgList().get(0));
        Assert.assertEquals("4", cmd.getArgList().get(1));
    }

    /**
     * Test parse method with only required options.
     *
     * @throws ParseException expected
     */
    @Test(expected = MissingOptionException.class)
    public void testParseOnlyRequiredOptions() throws ParseException {
        String[] args = {"--a", "--b"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("a", "a", true, "Option A");
        options.addRequiredOption("b", "b", true, "Option B");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        // MissingOptionException is expected
    }

    /**
     * Test parse method with invalid option.
     *
     * @throws ParseException expected
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws ParseException {
        String[] args = {"--x", "-a", "1", "-b", "2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "a", false, "Option A");
        options.addOption("b", "b", false, "Option B");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        // UnrecognizedOptionException is expected
    }

    /**
     * Test parse method with properties.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseProperties() throws ParseException {
        String[] args = {"--a=1", "--b=2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "a", false, "Option A");
        options.addOption("b", "b", false, "Option B");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, new Properties());

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    /**
     * Test parse method with stopAtNonOption.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"--foo", "-a", "1", "--bar", "-b", "2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "a", false, "Option A");
        options.addOption("f", "f", false, "Option F");
        options.addOption("b", "b", false, "Option B");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, null, null, true);

        Assert.assertEquals(3, cmd.getArgList().size());
        Assert.assertEquals("1", cmd.getArgList().get(0));
        Assert.assertEquals("1", cmd.getArgList().get(1));
        Assert.assertEquals("2", cmd.getArgList().get(2));
        Assert.assertEquals(2, cmd.getListOptions().size());
        Assert.assertEquals("foo", cmd.getListOptions().get(0).getKey());
        Assert.assertEquals("bar", cmd.getListOptions().get(1).getKey());
    }

    /**
     * Test parse method with empty args.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseEmptyArgs() throws ParseException {
        String[] args = {};
        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        Assert.assertTrue(cmd.getArgList().isEmpty());
        Assert.assertTrue(cmd.getListOptions().isEmpty());
    }

    /**
     * Test parse method with only properties.
     *
     * @throws ParseException expected
     */
    @Test
    public void testParseOnlyProperties() throws ParseException {
        String[] args = {};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "a", false, "Option A");
        options.addOption("b", "b", false, "Option B");

        Properties properties = new Properties();
        properties.put("a", "1");
        properties.put("b", "2");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, properties);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    /**
     * Test parse method with invalid arg index.
     *
     * @throws ParseException expected
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseInvalidArgIndex() throws ParseException {
        String[] args = {"-a", "1", "-b", "2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", "a", false, "Option A");
        options.addOption("b", "b", false, "Option B");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, null, null, true);

        cmd.getArg(3);

        // IndexOutOfBoundsException is expected
    }
}