package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlattenWithOptions() throws ParseException {
        String[] args = new String[]{"-Dproperty1=value1", "-Dproperty2=value2", "--foo", "-o", "output.txt", "--", "-bar=value3"};
        Options options = new Options();
        options.addOption("D", "debug", true, "Set the debug mode");
        options.addOption("f", "file", true, "Specify the output file");
        options.addOption("oo", "option_o", true, "Option o");
        options.addOption("ooo", "option_oo", true, "Option oo");

        GnuParser parser = new GnuParser();
        String[] parsedArgs = parser.parse(options, args);

        List<String> expectedTokens = Arrays.asList(
                "-D", "property1", "value1",
                "-D", "property2", "value2",
                "--",
                "-f", "output.txt",
                "-oo",
                "-ooo",
                "-bar", "value3"
        );

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithoutOptions() throws ParseException {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] parsedArgs = parser.parse(options, args);

        List<String> expectedTokens = Arrays.asList("arg1", "arg2", "arg3");

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), parsedArgs);
    }

    @Test
    public void testFlattenWithLongOptions() throws ParseException {
        String[] args = new String[]{"--longOption1=value1", "--longOption2=value2"};
        Options options = new Options();
        options.addOption("lo", "longOption", true, "Long option");

        GnuParser parser = new GnuParser();
        String[] parsedArgs = parser.parse(options, args);

        List<String> expectedTokens = Arrays.asList(
                "--lo", "longOption1", "value1",
                "--lo", "longOption2", "value2"
        );

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidArgument() throws ParseException {
        String[] args = new String[]{"-invalid", "arg1", "arg2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.parse(options, args);

        Assert.fail("Expected ParseException, but no exception was thrown");
    }

    @Test
    public void testFlattenWithDoubleDash() throws ParseException {
        String[] args = new String[]{"--", "arg1", "arg2"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] parsedArgs = parser.parse(options, args);

        List<String> expectedTokens = Arrays.asList("--", "arg1", "arg2");

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), parsedArgs);
    }
}