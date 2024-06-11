package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.POSixParser;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link PosixParser} class.
 */
public class PosixParserTest {

    @Test
    public void testSimpleOptions() {
        String[] args = {"-a", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        POSixParser parser = new POSixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        String[] expectedArgs = {"-a", "--optionB", "valueB"};
        assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testMultipleOptions() {
        String[] args = {"-a", "--optionB=valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        POSixParser parser = new POSixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        String[] expectedArgs = {"-a", "--optionB", "valueB", "--optionC", "valueC"};
        assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testLongOptions() {
        String[] args = {"--longOptionA", "--longOptionB=valueB"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");

        POSixParser parser = new POSixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        String[] expectedArgs = {"--longOptionA", "--longOptionB", "valueB"};
        assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testNoOptions() {
        String[] args = {"arg1", "arg2"};
        Options options = new Options();

        POSixParser parser = new POSixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"arg1", "arg2"}, parsedArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        String[] args = {"-x", "--invalidOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        POSixParser parser = new POSixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testStopAtNonOption() {
        String[] args = {"arg1", "-a", "arg3", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        POSixParser parser = new POSixParser();
        String[] parsedArgs = parser.flatten(options, args, true);
        String[] expectedArgs = {"arg1", "-a", "--optionB", "valueB"};
        assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testLongOptionWithValue() {
        String[] args = {"--longOptionWithValue=value"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true, "Long Option With Value");

        POSixParser parser = new POSixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        String[] expectedArgs = {"--longOptionWithValue", "value"};
        assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testBursting() {
        String[] args = {"--optionA", "--optionB=valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");
        options.addOption("optionC", true, "Option C");

        POSixParser parser = new POSixParser();
        String[] parsedArgs = parser.flatten(options, args, false);
        String[] expectedArgs = {"--optionA", "--optionB", "valueB", "--optionC", "valueC"};
        assertArrayEquals(expectedArgs, parsedArgs);
    }

    @Test
    public void testBurstingWithStopAtNonOption() {
        String[] args = {"--optionA", "--optionB=valueB", "--optionC=valueC", "arg"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");
        options.addOption("optionC", true, "Option C");

        POSixParser parser = new POSixParser();
        String[] parsedArgs = parser.flatten(options, args, true);
        String[] expectedArgs = {"--optionA", "--optionB", "valueB", "--optionC", "valueC", "arg"};
        assertArrayEquals(expectedArgs, parsedArgs);
    }
}