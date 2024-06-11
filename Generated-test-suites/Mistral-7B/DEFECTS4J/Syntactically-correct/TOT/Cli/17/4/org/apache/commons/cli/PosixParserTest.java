package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link PosixParser} class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = {"--foo", "--bar=value", "--baz=value1,value2", "-abc", "-def", "-g", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f", "foo option");
        options.addOption("bar", true, "b", "bar option with value");
        options.addOption("baz", true, "z", "baz option with multiple values");
        options.addOption("g", false, "g", "g option");

        DefaultParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar=value", "--baz=value1,value2", "-a", "-b", "--g", "--", "arg1", "arg2"}, flattenedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);

        // The test should throw ParseException since "invalidOption" is not a valid option.
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] args = {"--foo", "--bar=value", "--baz=value1,value2", "-abc", "-def", "-g", "--", "arg1", "arg2", "nonOption"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f", "foo option");
        options.addOption("bar", true, "b", "bar option with value");
        options.addOption("baz", true, "z", "baz option with multiple values");
        options.addOption("g", false, "g", "g option");

        DefaultParser parser = new PosixParser();
        String[] flattenedArgs = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar=value", "--baz=value1,value2", "-abc", "-def", "-g", "--arg1", "--arg2", "nonOption"}, flattenedArgs);
    }
}