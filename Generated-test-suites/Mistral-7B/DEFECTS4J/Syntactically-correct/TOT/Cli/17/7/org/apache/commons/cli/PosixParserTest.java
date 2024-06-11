package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a string argument");
        options.addOption("d", "default", "Option D with a default value");

        String[] arguments = {"--a", "--b=value1", "--c=value2", "arg1", "arg2", "--", "--d", "arg3"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b=value1", "--c=value2", "arg1", "arg2", "--d=arg3"}, parsedArguments);

        String[] arguments2 = {"--a", "--b=value1", "--c", "arg1", "arg2", "--d=arg3", "--e"};
        try {
            parser.flatten(options, arguments2, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        String[] arguments3 = {"--a", "--b=value1", "--c=value2", "--d=arg3", "--e"};
        try {
            parser.flatten(options, arguments3, true);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }
}