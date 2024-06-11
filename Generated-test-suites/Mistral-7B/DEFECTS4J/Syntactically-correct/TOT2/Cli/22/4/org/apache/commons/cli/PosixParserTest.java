package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest
{
    @Test
    public void testFlattenSimple() throws ParseException
    {
        String[] args = new String[]{"--option1", "--option2=value", "-o", "--", "-x", "-y"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");
        options.addOption("o", true, "Output file");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        List<String> expectedArgs = Arrays.asList(
                "--option1",
                "--option2",
                "value",
                "-o",
                "",
                "--",
                "-x",
                "-y"
        );
        Assert.assertArrayEquals(expectedArgs.toArray(new String[0]), parsedArgs);
    }

    @Test
    public void testFlattenStopAtNonOption() throws ParseException
    {
        String[] args = new String[]{"--option1", "--", "nonOption", "-o", "--", "-x", "-y"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);
        List<String> expectedArgs = Arrays.asList(
                "--option1",
                "--",
                "nonOption",
                "-o",
                "--",
                "-x",
                "-y"
        );
        Assert.assertArrayEquals(expectedArgs.toArray(new String[0]), parsedArgs);

        // check that 'nonOption' was not added to the list of processed options
        Assert.assertFalse(options.hasOption("nonOption"));
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--invalidOption", "--", "-o", "--", "-x", "-y"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // the test should fail with a ParseException
    }

    @Test
    public void testBurstToken()
    {
        String[] args = new String[]{"--foo-bar-baz"};
        Options options = new Options();
        options.addOption("foo", true, "Foo");
        options.addOption("bar", true, "Bar");
        options.addOption("baz", true, "Baz");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expectedArgs = Arrays.asList(
                "--foo",
                "--bar",
                "baz"
        );
        Assert.assertArrayEquals(expectedArgs.toArray(new String[0]), parsedArgs);
    }
}