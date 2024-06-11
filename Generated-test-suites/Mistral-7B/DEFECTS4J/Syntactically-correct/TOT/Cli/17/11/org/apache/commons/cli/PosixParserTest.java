package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flattening with valid options.
     */
    @Test
    public void testFlatteningWithValidOptions() throws ParseException {
        String[] args = {"--test", "-a", "1", "--optionB=valueB", "-o", "output.txt"};
        Options options = new Options();
        options.addOption("test", true, "Test option.");
        options.addOption("a", true, "Option A.");
        options.addOption("b", true, "Option B.");
        options.addOption("o", true, "Output file.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--test", "-a", "1", "--optionB=valueB", "-o", "output.txt"}, parsedArgs);
    }

    /**
     * Test flattening with invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningWithInvalidOption() throws ParseException {
        String[] args = {"--invalidOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }

    /**
     * Test flattening with long option and value.
     */
    @Test
    public void testFlatteningWithLongOptionAndValue() throws ParseException {
        String[] args = {"--longOption=longValue"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption=longValue"}, parsedArgs);
    }

    /**
     * Test flattening with multiple long options.
     */
    @Test
    public void testFlatteningWithMultipleLongOptions() throws ParseException {
        String[] args = {"--longOption1", "--longOption2=value2", "--longOption3=value3"};
        Options options = new Options();
        options.addOption("longOption1", true, "Long option 1.");
        options.addOption("longOption2", true, "Long option 2.");
        options.addOption("longOption3", true, "Long option 3.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--longOption1", "--longOption2=value2", "--longOption3=value3"}, parsedArgs);
    }

    /**
     * Test flattening with multiple options, some with values.
     */
    @Test
    public void testFlatteningWithMultipleOptions() throws ParseException {
        String[] args = {"-a", "1", "--optionB=valueB", "-o", "output.txt"};
        Options options = new Options();
        options.addOption("a", true, "Option A.");
        options.addOption("b", true, "Option B.");
        options.addOption("o", true, "Output file.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "1", "--optionB=valueB", "-o", "output.txt"}, parsedArgs);
    }

    /**
     * Test flattening with multiple options and only one with a value.
     */
    @Test
    public void testFlatteningWithMultipleOptionsWithOnlyOneValue() throws ParseException {
        String[] args = {"-a", "1", "--optionB", "output.txt"};
        Options options = new Options();
        options.addOption("a", true, "Option A.");
        options.addOption("b", true, "Option B.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "1", "--optionB", "output.txt"}, parsedArgs);
    }

    /**
     * Test flattening with multiple options, some without values.
     */
    @Test
    public void testFlatteningWithMultipleOptionsWithoutValues() throws ParseException {
        String[] args = {"-a", "--optionB", "-o"};
        Options options = new Options();
        options.addOption("a", true, "Option A.");
        options.addOption("b", true, "Option B.");
        options.addOption("o", true, "Output file.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "--optionB", "-o"}, parsedArgs);
    }

    /**
     * Test flattening with multiple options and a mix of values and no values.
     */
    @Test
    public void testFlatteningWithMultipleOptionsWithAMixOfValuesAndNoValues() throws ParseException {
        String[] args = {"-a", "1", "--optionB", "output.txt", "-c"};
        Options options = new Options();
        options.addOption("a", true, "Option A.");
        options.addOption("b", true, "Option B.");
        options.addOption("c", true, "Option C.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"-a", "1", "--optionB", "output.txt", "-c"}, parsedArgs);
    }

    /**
     * Test flattening with two hyphens.
     */
    @Test
    public void testFlatteningWithTwoHyphens() throws ParseException {
        String[] args = {"--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--"}, parsedArgs);
    }

    /**
     * Test flattening with long option -- and value.
     */
    @Test
    public void testFlatteningWithLongOptionAndValueDoubleHyphen() throws ParseException {
        String[] args = {"----longOption=longValue"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option.");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"----longOption=longValue"}, parsedArgs);
    }
}