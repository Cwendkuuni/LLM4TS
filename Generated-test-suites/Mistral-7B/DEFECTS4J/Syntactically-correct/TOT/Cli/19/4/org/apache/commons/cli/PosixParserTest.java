package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link PosixParser} class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with argument");
        options.addOption("c", 3, "Option C with three arguments");
        options.addOption("d", 4, "Option D with four arguments");

        PosixParser parser = new PosixParser();
        String[] args = {"--a", "--b", "value1", "--c", "arg1", "arg2", "arg3", "--d", "arg1", "arg2", "arg3", "arg4"};
        String[] expected = {"--a", "--b", "value1", "-c", "-d", "arg1", "arg2", "arg3", "arg4"};

        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"--x", "--a"};

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with argument");

        PosixParser parser = new PosixParser();
        String[] args = {"--a", "--b", "-", "invalidOption", "value"};

        String[] result = parser.flatten(options, args, true);
        String[] expected = {"--a", "--b", "-", "invalidOption", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", 2, "Option B with argument");

        PosixParser parser = new PosixParser();
        parser.init();
        parser.flatten(options, new String[]{"--a", "--b", "value"}, false);

        assertEquals(2, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--b", parser.tokens.get(1));

        assertNull(parser.currentOption);

        parser.process("value");

        assertEquals(3, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--b", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));

        assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("a", 2, "Option A with two arguments");
        options.addOption("b", 3, "Option B with three arguments");

        PosixParser parser = new PosixParser();
        String[] args = {"-aab", "arg1", "arg2"};

        parser.flatten(options, args, false);

        assertEquals(4, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-a", parser.tokens.get(1));
        assertEquals("arg1", parser.tokens.get(2));
        assertEquals("arg2", parser.tokens.get(3));

        assertNull(parser.currentOption);

        parser.burstToken("-aab", false);

        assertEquals(5, parser.tokens.size());
        assertEquals("-a", parser.tokens.get(0));
        assertEquals("-a", parser.tokens.get(1));
        assertEquals("arg1", parser.tokens.get(2));
        assertEquals("arg2", parser.tokens.get(3));
        assertEquals("b", parser.tokens.get(4));

        Options bOptions = new Options();
        bOptions.addOption("b", 3, "Option B with three arguments");

        parser.init();
        parser.burstToken("-cbcd", false);

        assertEquals(6, parser.tokens.size());
        assertEquals("-c", parser.tokens.get(0));
        assertEquals("-c", parser.tokens.get(1));
        assertEquals("b", parser.tokens.get(2));
        assertEquals("c", parser.tokens.get(3));
        assertEquals("d", parser.tokens.get(4));
        assertEquals("d", parser.tokens.get(5));

        assertNull(parser.currentOption);
    }

    @Test
    public void testProcessOptionWithArg() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with argument");

        PosixParser parser = new PosixParser();
        parser.init();
        parser.flatten(options, new String[]{"--a", "--b=value"}, false);

        assertEquals(3, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--b=value", parser.tokens.get(1));

        assertNull(parser.currentOption);

        parser.process("value");

        assertEquals(4, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--b=value", parser.tokens.get(1));
        assertEquals("value", parser.tokens.get(2));

        Option bOption = options.getOption("b");
        assertTrue(bOption.hasArg());
        assertTrue(bOption.hasArgs());
        assertTrue(bOption.isRequired());

        assertEquals(bOption, parser.currentOption);
        assertEquals("value", parser.currentOption.getValue());
    }

    @Test
    public void testProcessOptionWithMultipleArgs() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("c", 3, "Option C with three arguments");

        PosixParser parser = new PosixParser();
        parser.init();
        parser.flatten(options, new String[]{"--a", "--c=arg1 arg2 arg3"}, false);

        assertEquals(4, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--c=arg1 arg2 arg3", parser.tokens.get(1));

        assertNull(parser.currentOption);

        parser.process("arg1 arg2 arg3");

        assertEquals(7, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--c=arg1 arg2 arg3", parser.tokens.get(1));
        assertEquals("arg1", parser.tokens.get(2));
        assertEquals("arg2", parser.tokens.get(3));
        assertEquals("arg3", parser.tokens.get(4));
        assertEquals("arg3", parser.tokens.get(5));
        assertEquals("arg3", parser.tokens.get(6));

        Option cOption = options.getOption("c");
        assertTrue(cOption.hasArg());
        assertFalse(cOption.hasArgs());
        assertFalse(cOption.isRequired());

        assertEquals(cOption, parser.currentOption);
        assertSame(Arrays.asList("arg1", "arg2", "arg3"), parser.currentOption.getArgs());
    }

    @Test
    public void testProcessOptionWithArgAndValue() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with argument and value");

        PosixParser parser = new PosixParser();
        parser.init();
        parser.flatten(options, new String[]{"--a", "--b=value:value2"}, false);

        assertEquals(4, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--b=value:value2", parser.tokens.get(1));

        assertNull(parser.currentOption);

        parser.process("value3 value4");

        assertEquals(6, parser.tokens.size());
        assertEquals("--a", parser.tokens.get(0));
        assertEquals("--b=value:value2", parser.tokens.get(1));
        assertEquals("value3", parser.tokens.get(2));
        assertEquals("value4", parser.tokens.get(3));
        assertEquals("value3", parser.tokens.get(4));
        assertEquals("value4", parser.tokens.get(5));

        Option bOption = options.getOption("b");
        assertTrue(bOption.hasArg());
        assertTrue(bOption.hasArgs());
        assertTrue(bOption.isRequired());

        assertEquals(bOption, parser.currentOption);
        assertSame(Arrays.asList("value2", "value3", "value4"), parser.currentOption.getArgs());
    }

    @Test
    public void testHelpFormatter() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B with argument");
        options.addOption("c", 3, "Option C with three arguments");

        PosixParser parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.formatHelp("Parser Test", parser.getClass(), options, true);

        assertTrue(help.contains("--a"));
        assertTrue(help.contains("--b <ARG>"));
        assertTrue(help.contains("--c <ARG> <ARG> <ARG>"));

        System.out.println(help);
    }
}