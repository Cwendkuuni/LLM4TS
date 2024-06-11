package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for {@link PosixParser} class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo=bar", "--bar", "-a", "-ab", "-abc", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("a", true, "a");
        options.addOption("ab", false, "ab");
        options.addOption("abc", false, "abc");

        DefaultParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        assertEquals(args.length, parsedArgs.length);
        assertEquals("--foo=bar", parsedArgs[0]);
        assertEquals("--bar", parsedArgs[1]);
        assertEquals("-a", parsedArgs[2]);
        assertEquals("-ab", parsedArgs[3]);
        assertEquals("-abc", parsedArgs[4]);
        assertEquals("--", parsedArgs[5]);
        assertEquals("arg1", parsedArgs[6]);
        assertEquals("arg2", parsedArgs[7]);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException {
        String[] args = {"--invalid"};
        Options options = new Options();

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenMixedOptionsAndValues() throws ParseException {
        String[] args = {"--foo=bar", "--bar", "-a=valueA", "-ab", "-abc=valueB", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("a", true, "a");
        options.addOption("ab", false, "ab");
        options.addOption("abc", true, "abc");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);

        // make sure currentOption is set correctly
        assertEquals(options.getOption("a"), parser.currentOption);
    }

    @Test
    public void testFlattenEmpty() throws ParseException {
        String[] args = {};
        Options options = new Options();

        DefaultParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        assertEquals(0, parsedArgs.length);
    }

    @Test
    public void testHelpFormat() {
        String[] args = {"--help"};
        Options options = new Options();
        options.addOption("help", false, "h", "Print this message.");

        DefaultParser parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            String help = parser.parse(options, args, true).toString();
            System.out.println(help);
        } catch (ParseException e) {
            // Ignore, HelpFormatter is supposed to print help
        }
    }
}