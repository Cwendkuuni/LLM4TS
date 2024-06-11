package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"--option1", "-o", "value1", "--option2", "-v"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--option1",
                "--option1=value1",
                "--option2"
        );

        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        String[] args = {"-invalidOption", "--option1=value1", "--option2"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        String[] args = {"--option1", "-o", "value1", "--option2", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--option1",
                "--option1=value1",
                "--option2",
                "nonOption"
        );

        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        String[] args = {"--longOptionWithValue=value"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--longOptionWithValue",
                "--longOptionWithValue=value"
        );

        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownLongOption() {
        Options options = new Options();
        String[] args = {"--unknownOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenEmptyArguments() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList();

        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }

    @Test
    public void testFlattenSingleArgument() {
        Options options = new Options();
        String[] args = {"singleArgument"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList("singleArgument");

        Assert.assertArrayEquals(expected.toArray(), parsedArgs);
    }
}