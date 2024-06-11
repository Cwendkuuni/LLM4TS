package org.apache.commons.cli.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest
{
    /**
     * Test flattening with valid options.
     */
    @Test
    public void testFlatteningValidOptions()
    {
        String[] arguments = { "--foo", "--bar=value", "-a", "--baz=qux", "-c", "--qux", "--" };
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");
        options.addOption("baz", true, "Baz option");
        options.addOption("qux", true, "Qux option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar=value", "-a", "--baz=qux", "--qux", "--"}, result);
    }

    /**
     * Test flattening with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningInvalidOption()
    {
        String[] arguments = { "--invalidOption" };
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test flattening with non-option argument.
     */
    @Test
    public void testFlatteningNonOptionArgument()
    {
        String[] arguments = { "test" };
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--", "test"}, result);
    }

    /**
     * Test flattening with stopAtNonOption set to true.
     */
    @Test
    public void testFlatteningStopAtNonOption()
    {
        String[] arguments = { "--foo", "test", "--bar=value" };
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--foo", "test", "--bar=value"}, result);
    }
}