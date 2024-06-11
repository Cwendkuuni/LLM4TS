package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testSimpleUsage() {
        String[] args = new String[]{"--test1", "arg1", "--test2=arg2"};
        Options options = new Options();
        options.addOption("test1", true, "t1");
        options.addOption("test2", true, "t2").hasArg(true);

        List<String> expectedTokens = Arrays.asList("--test1", "arg1", "--test2", "arg2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        List<String> actualTokens = Arrays.asList(flattenedArgs);

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = new String[]{"--test1", "arg1", "-x", "--test2=arg2"};
        Options options = new Options();
        options.addOption("test1", true, "t1");
        options.addOption("test2", true, "t2").hasArg(true);

        List<String> expectedTokens = Arrays.asList("--test1", "arg1", "-x", "--test2", "arg2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);
        List<String> actualTokens = Arrays.asList(flattenedArgs);

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = new String[]{"--invalidOption", "arg1", "--test2=arg2"};
        Options options = new Options();
        options.addOption("test1", true, "t1");
        options.addOption("test2", true, "t2").hasArg(true);

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testMissingArgForOption() {
        String[] args = new String[]{"--test1", "--test2"};
        Options options = new Options();
        options.addOption("test1", true, "t1");
        options.addOption("test2", true, "t2").hasArg(true);

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testBursting() {
        String[] args = new String[]{"--test-long-option", "arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addOption("test-long-option", true, "tl").hasArg(true);

        List<String> expectedTokens = Arrays.asList("--test-long-option", "arg1", "arg2", "arg3");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        List<String> actualTokens = Arrays.asList(flattenedArgs);

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testBurstingWithArg() {
        String[] args = new String[]{"--test-long-option=value", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("test-long-option", true, "tl").hasArg(true);

        List<String> expectedTokens = Arrays.asList("--test-long-option", "value", "arg1", "arg2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        List<String> actualTokens = Arrays.asList(flattenedArgs);

        Assert.assertEquals(expectedTokens, actualTokens);
    }

    @Test
    public void testBurstingWithNoArgument() {
        String[] args = new String[]{"--test-long-option"};
        Options options = new Options();
        options.addOption("test-long-option", true, "tl").hasArg(false);

        List<String> expectedTokens = Arrays.asList("--test-long-option");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        List<String> actualTokens = Arrays.asList(flattenedArgs);

        Assert.assertEquals(expectedTokens, actualTokens);
    }
}