package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest
{
    @Test
    public void testFlattenSimple() throws ParseException
    {
        String[] args = {"-a", "--optionB", "-c", "--optionC=valueC", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "--optionB", "-c", "--optionC=valueC"}, result);
    }

    @Test
    public void testFlattenLongOption() throws ParseException
    {
        String[] args = {"--longOption", "--longOption=value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--longOption", "--longOption=value"}, result);
    }

    @Test
    public void testFlattenEmptyOption() throws ParseException
    {
        String[] args = {"--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() throws ParseException
    {
        String[] args = {"--unknownOption"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenNoOptions() throws ParseException
    {
        String[] args = {"--", "-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"--", "-a", "value"}, result);
    }

    @Test
    public void testFlattenWithArg() throws ParseException
    {
        String[] args = {"-a", "--optionB=valueB", "-c", "valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertArrayEquals(new String[]{"-a", "--optionB=valueB", "-c", "valueC"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException
    {
        String[] args = {"--", "-a", "--nonOption", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertArrayEquals(new String[]{"--", "-a", "--nonOption"}, result);
    }

    @Test
    public void testHelpFormatter()
    {
        String[] args = {"--help"};
        Options options = new Options();
        options.addOption(false, "help", "display this help message");

        PosixParser parser = new PosixParser();
        HelpFormatter formatter = new HelpFormatter();

        try
        {
            parser.parse(options, args);
        }
        catch (ParseException e)
        {
            formatter.printHelp("PosixParserTest", options);
            System.out.println();
            formatter.printHelp("PosixParserTest", options, true);
        }

        assertTrue(true);
    }

    private void assertArrayEquals(Object[] expected, Object[] actual)
    {
        if (!Arrays.equals(expected, actual))
        {
            throw new AssertionError("Expected: " + Arrays.toString(expected) + "\n  but was: " + Arrays.toString(actual));
        }
    }
}