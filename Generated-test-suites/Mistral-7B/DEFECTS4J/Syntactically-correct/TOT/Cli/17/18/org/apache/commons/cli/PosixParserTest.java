package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    @Test
    public void testBasicUsage() throws ParseException {
        String[] args = {"-a", "--optionB=valueB", "valueC", "--", "valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        assertTrue(options.isSetOption("a"));
        assertTrue(options.isSetOption("b"));
        assertEquals("valueB", options.getOptionValue("b"));
        assertEquals("valueD", options.getOptionValue(""));
        assertFalse(options.isSetOption("unknown"));
    }

    @Test
    public void testBurstingSingleOption() throws ParseException {
        String[] args = {"--optionABC=value"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A")
                .requiredArg()
                .argName("value");
        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        assertTrue(options.isSetOption("optionA"));
        assertEquals("value", options.getOptionValue("optionA"));
    }

    @Test(expected = ParseException.class)
    public void testBurstingInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        Options options = new Options();
        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testBurstingWithStopAtNonOption() throws ParseException {
        String[] args = {"--invalidOption", "valueC", "--", "valueD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        DefaultParser parser = new DefaultParser();
        parser.setStopAtNonOptions(true);
        parser.parse(options, args, false);

        assertNull(options.getOptionValue("a"));
        assertEquals("--invalidOption", options.getOptionValue(""));
        assertFalse(options.isSetOption("invalidOption"));
        assertEquals("valueC", options.getOptionValue(""));
        assertEquals("valueD", options.getOptionValue(""));
    }

    @Test
    public void testHelp() {
        String[] args = {"--help"};
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        DefaultParser parser = new DefaultParser();

        try {
            parser.parse(options, args, false);
        } catch (ParseException e) {
            formatter.printHelp("PosixParserTest", options);
            System.out.println();
            System.out.println("Expected Help Message");
            Assert.fail();
        }

        String expectedHelpMessage = "Commons CLI v1.3\n\n" +
                "Options:\n" +
                "\n" +
                "   --help                            prints this help message and exits\n" +
                "\n" +
                "   --version                        prints version information and exits\n";

        String actualHelpMessage = System.console().reader().readLine();

        assertEquals(expectedHelpMessage, actualHelpMessage);
    }
}