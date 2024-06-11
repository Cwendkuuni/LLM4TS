package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        final String[] args = new String[]{"-Dprop1", "--foo", "-bar", "baz", "-Dprop2=value", "--"};
        Options options = new Options();
        options.addOption("D", "debug", false, "Print debug output.");
        options.addOption("f", "foo", true, "Sets the foo value.");
        options.addOption("b", "bar", true, "Sets the bar value.");

        GnuParser parser = new GnuParser();
        String[] result = parser.parse(options, args, true);

        String[] expected = new String[]{"-D", "prop1", "--", "foo", null, "bar", "baz", "-D", "prop2", "value", "--"};
        assertArrayEquals(expected, result);

        // Test with stopAtNonOption = false
        result = parser.parse(options, args, false).clone();
        expected = new String[]{"-D", "prop1", "--", "foo", "baz", "-D", "prop2", "value"};
        assertArrayEquals(expected, result);

        // Test with stopAtNonOption = true
        result = parser.parse(options, args, true).clone();
        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
        assertEquals(args[args.length - 2], result[result.length - 1]);
    }
}