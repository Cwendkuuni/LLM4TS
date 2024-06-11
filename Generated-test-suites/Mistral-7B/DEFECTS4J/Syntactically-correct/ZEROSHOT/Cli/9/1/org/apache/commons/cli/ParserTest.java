package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = new String[]{"-a", "1", "--optionB", "2", "-o", "optionC", "--optionD=valueD"};

        Parser parser = new DefaultParser();
        parser.setOptions(createOptions());
        CommandLine cmd = parser.parse(args);

        Assert.assertArrayEquals(new String[]{"1"}, cmd.getOptionValues("a"));
        Assert.assertArrayEquals(new String[]{"2"}, cmd.getOptionValues("optionB"));
        Assert.assertNull(cmd.getOptionValues("unknownOption"));
        Assert.assertEquals("optionC", cmd.getOptionValue("o"));
        Assert.assertEquals("valueD", cmd.getOptionValue("optionD"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"-b"};

        Parser parser = new DefaultParser();
        parser.setOptions(createOptionsWithRequiredOptions());
        parser.parse(args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unknownOption"};

        Parser parser = new DefaultParser();
        parser.setOptions(createOptions());
        parser.parse(args);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = new String[]{"-c"};

        Parser parser = new DefaultParser();
        parser.setOptions(createOptionsWithOptionalArgOption());
        parser.parse(args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("myOption", "value");

        String[] args = new String[]{"--myOption"};

        Parser parser = new DefaultParser();
        parser.setOptions(createOptionsWithPropertyValueOption());
        parser.parse(args, properties);

        Assert.assertEquals("value", parser.cmd.getOptionValues("myOption")[0]);
    }

    private Options createOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with argument");
        options.addOption("o", true, "Option O");
        options.addOption("d", true, "Option D with value");
        return options;
    }

    private Options createOptionsWithRequiredOptions() {
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");
        return options;
    }

    private Options createOptionsWithOptionalArgOption() {
        Options options = new Options();
        options.addOption("c", true, "Option C with optional argument");
        return options;
    }

    private Options createOptionsWithPropertyValueOption() {
        Options options = new Options();
        options.addOption("myOption", true, "Option with property value");
        return options;
    }
}