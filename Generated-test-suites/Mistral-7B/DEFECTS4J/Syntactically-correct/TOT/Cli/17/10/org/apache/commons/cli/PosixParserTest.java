package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link PosixParser} class.
 */
public class PosixParserTest {

    /**
     * Test flattening with valid options and arguments.
     */
    @Test
    public void testFlatteningValidOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 2, "Option C with argument");

        String[] args = {"--a", "--b=1", "-c", "valueC"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b", "1", "-c", "valueC"}, flattenedArgs);
    }

    /**
     * Test flattening with valid options without arguments.
     */
    @Test
    public void testFlatteningValidOptionsWithoutArguments() {
        Options options = new Options();
        options.addOption("a");
        options.addOption("b");

        String[] args = {"--a", "--b"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b"}, flattenedArgs);
    }

    /**
     * Test flattening with a single hyphen.
     */
    @Test
    public void testFlatteningSingleHyphen() {
        Options options = new Options();

        String[] args = {"-"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"-"}, flattenedArgs);
    }

    /**
     * Test flattening with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningInvalidOptions() {
        Options options = new Options();
        options.addOption("a");

        String[] args = {"--z"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test flattening with a mix of valid and invalid options.
     */
    @Test
    public void testFlatteningMixOfValidAndInvalidOptions() {
        Options options = new Options();
        options.addOption("a");
        options.addOption("b");

        String[] args = {"--a", "--z", "--b"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--z", "--b"}, flattenedArgs);
    }

    /**
     * Test flattening with the '--' special token.
     */
    @Test
    public void testFlatteningSpecialToken() {
        Options options = new Options();
        options.addOption("a");

        String[] args = {"--", "value"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--", "value"}, flattenedArgs);
    }

    /**
     * Test flattening with the '--' special token and an argument.
     */
    @Test
    public void testFlatteningSpecialTokenWithArgument() {
        Options options = new Options();
        options.addOption("a");

        String[] args = {"--", "value", "argument"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--", "value", "argument"}, flattenedArgs);
    }
}