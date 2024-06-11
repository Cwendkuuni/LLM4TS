package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the PosixParser class.
 */
public class PosixParserTest
{
    /**
     * Test flatten method with simple options.
     */
    @Test
    public void testFlattenSimpleOptions()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a string argument");

        String[] args = {"-a", "-b", "1", "--c", "--foo", "--bar"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expectedArgs = Arrays.asList(new String[]{"-a", "--", "b", "1", "--c", "--foo", "--bar"});
        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test flatten method with long options.
     */
    @Test
    public void testFlattenLongOptions()
    {
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", 1, "Option Bar with an integer argument");

        String[] args = {"--foo", "--bar=1", "--baz"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expectedArgs = Arrays.asList(new String[]{"--foo", "--", "bar", "1", "--baz"});
        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test flatten method with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenStopAtNonOption()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");

        String[] args = {"-a", "-b", "1", "--foo", "--bar", "-c"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        List<String> expectedArgs = Arrays.asList(new String[]{"-a", "--foo", "--bar", "-c"});
        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }

    /**
     * Test flatten method with complex options.
     */
    @Test
    public void testFlattenComplexOptions()
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with a string argument");

        String[] args = {"-acd", "1", "--e=f", "g"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expectedArgs = Arrays.asList(new String[]{"-a", "--", "c", "d", "--", "e", "f", "g"});
        Assert.assertArrayEquals(expectedArgs.toArray(new String[expectedArgs.size()]), parsedArgs);
    }
}