package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParsedOptions;
import org.junit.Test;

/**
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParser() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "alpha", true, "alpha option");
        options.addOption("b", "beta", false, "beta option");
        options.addOption("c", "char", false, "character option");

        DefaultParser parser = new DefaultParser();
        String[] args = new String[]{"-a", "--alpha", "--beta", "C:/file.txt", "D:/folder"};
        CommandLine commandLine = parser.parse(options, args, false);

        ParsedOptions parsedOptions = commandLine.parseOptionMap(options);

        assertTrue(parsedOptions.isSet("alpha"));
        assertFalse(parsedOptions.isSet("beta"));
        assertFalse(parsedOptions.isSet("char"));
        assertNull(parsedOptions.getValue("char"));
        assertEquals("C:/file.txt", parsedOptions.getValue("file"));
        assertEquals("D:/folder", parsedOptions.getValue("folder"));

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("test", options);
        System.out.println(help);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParserAbstractMethod() {
        DefaultParser parser = new DefaultParser();
        parser.setOptions(null);
        parser.parse(null, null, null, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() {
        Options options = new Options();
        options.addOption("a", "alpha", true, "alpha option");
        DefaultParser parser = new DefaultParser();
        String[] args = new String[]{"-a"};
        parser.parse(options, args, null, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() {
        Options options = new Options();
        options.addOption("a", "alpha", true, "alpha option");
        DefaultParser parser = new DefaultParser();
        String[] args = new String[]{"--x", "-a"};
        parser.parse(options, args, null, false);
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        Options options = new Options();
        options.addOption("a", "alpha", true, "alpha option");
        DefaultParser parser = new DefaultParser();
        String[] args = new String[]{"-a", "--alpha=1", "--beta"};
        parser.parse(options, args, null, false);
    }
}