package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

    @Test
    public void testEmptyOptions() throws ParseException {
        Options options = new Options();
        String[] args = new String[0];

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEqual(args, parsedArgs);
        assertEquals(0, options.getOptions().size());
    }

    @Test
    public void testBasicOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--option"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEqual(new String[]{"--option"}, parsedArgs);
        assertEquals(1, options.getOptions().size());
        Option option = options.getOption("option");
        assertSame(OptionType.NO_ARGUMENT, option.getType());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionWithValue() throws ParseException {
        Options options = new Options();
        options.addOption("option", true, "o");
        String[] args = {"--option", "value"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEqual(new String[]{"--option", "value"}, parsedArgs);
        assertEquals(1, options.getOptions().size());
        Option option = options.getOption("option");
        assertTrue(option.isSet());
        assertEquals("value", option.getValue());
    }

    @Test
    public void testMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] args = {"--option1", "--option2"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEqual(new String[]{"--option1", "--option2"}, parsedArgs);
        assertEquals(2, options.getOptions().size());
        Option option1 = options.getOption("option1");
        Option option2 = options.getOption("option2");
        assertTrue(option1.isSet());
        assertTrue(option2.isSet());
        assertFalse(option1.getValue().isEmpty());
        assertFalse(option2.getValue().isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() throws ParseException {
        Options options = new Options();
        String[] args = {"--unknownOption"};

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testBurstedOption() throws ParseException {
        Options options = new Options();
        options.addOption("longOption", true, "lo");
        String[] args = {"--longOption", "value1", "value2"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEqual(new String[]{"--longOption", "value1", "value2"}, parsedArgs);
        assertEquals(1, options.getOptions().size());
        Option option = options.getOption("longOption");
        assertTrue(option.isSet());
        assertEquals("value1value2", option.getValue());
    }

    @Test
    public void testMixedOptions() throws ParseException {
        Options options = new Options();
        options.addOption("shortOption", true, "s");
        options.addOption("longOption", true, "lo");
        String[] args = {"-s", "--longOption", "--shortOption"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);
        Assert.assertArrayEqual(new String[]{"-s", "--longOption", "--shortOption"}, parsedArgs);
        assertEquals(2, options.getOptions().size());
        Option shortOption = options.getOption("shortOption");
        Option longOption = options.getOption("longOption");
        assertTrue(shortOption.isSet());
        assertTrue(longOption.isSet());
        assertFalse(shortOption.getValue().isEmpty());
        assertFalse(longOption.getValue().isEmpty());
    }

    @Test(expected = ParseException.class)
    public void testInvalidToken() throws ParseException {
        Options options = new Options();
        String[] args = {"--invalidToken"};

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        Options options = new Options();

        String[] args = {"--invalidOption="};

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionWithValue() throws ParseException {
        Options options = new Options();
        options.addOption("invalidOption", true, "io");

        String[] args = {"--invalidOption=value"};

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("option", true, "o");
        options.addOption("invalidOption", false, "io");
        String[] args = {"--option", "--invalidOption", "value"};

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, true);
        Assert.assertArrayEqual(new String[]{"--option"}, parsedArgs);
        assertEquals(1, options.getOptions().size());
        Option option = options.getOption("option");
        assertTrue(option.isSet());
        assertEquals("value", option.getValue());
        assertEquals(1, parsedArgs.length);
    }
}