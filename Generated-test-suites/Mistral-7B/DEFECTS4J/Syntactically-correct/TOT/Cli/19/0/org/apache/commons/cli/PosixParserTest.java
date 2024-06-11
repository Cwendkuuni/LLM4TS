package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenPositiveCases() throws ParseException {
        String[] args = {"--foo=bar", "-o", "--opt1", "--opt2=test", "-a", "-b", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addRequiredOption("foo", true, "f");
        options.addOption("opt1", false, "o");
        options.addOption("opt2", true, "t");
        options.addOption("a", false, "a");
        options.addOption("b", false, "b");

        DefaultParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[] {"--foo", "bar", "--opt1", "--opt2", "test", "-a", "-b", "--", "arg1", "arg2"}, parsedArgs);
    }

    @Test
    public void testFlattenEdgeCases() throws ParseException {
        String[] args = {"--foo=bar", "--foo=baz", "-o", "--opt1", "--opt2=test", "-a", "-b", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("opt1", false, "o");
        options.addOption("opt2", true, "t");
        options.addOption("a", false, "a");
        options.addOption("b", false, "b");

        DefaultParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[] {"--foo", "bar", "--foo", "baz", "--opt1", "--opt2", "test", "-a", "-b", "--", "arg1", "arg2"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenErrorScenarios() throws ParseException {
        String[] args = {"--foobar"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        DefaultParser parser = new PosixParser();
        parser.parse(options, args, false);

        // This test case should throw a ParseException
    }
}