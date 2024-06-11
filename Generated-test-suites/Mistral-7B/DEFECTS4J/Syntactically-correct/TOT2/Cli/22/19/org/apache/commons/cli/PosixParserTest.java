package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for PosixParser class
 */
public class PosixParserTest
{
    @Test
    public void testFlattenBasic() throws ParseException
    {
        String[] args = new String[] {"--foo", "--bar=baz", "-a", "-b", "-c=d", "-e", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        String[] expected = new String[] {"--foo", "--bar", "baz", "-a", "-b", "-c", "d"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException
    {
        String[] args = new String[] {"--foo", "--bar=baz", "-a", "-b", "-c=d", "nonOption", "--"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, true);
        String[] expected = new String[] {"--foo", "--bar", "baz", "-a", "-b", "-c", "d", "nonOption"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException
    {
        String[] args = new String[] {"--invalidOption", "--foo"};
        PosixParser parser = new PosixParser();
        parser.flatten(new Options(), args, false);
    }

    @Test
    public void testBurstToken()
    {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-foo-bar", false);
        String[] expected = new String[] {"-foo", "-bar"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption()
    {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-foo-bar-nonOption", true);
        String[] expected = new String[] {"-foo", "-bar", "--", "nonOption"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenWithEmptyToken()
    {
        PosixParser parser = new PosixParser();
        parser.burstToken("", false);
    }

    @Test
    public void testBurstTokenWithSingleCharacterOption()
    {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-a", false);
        String[] expected = new String[] {"-a"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithLongOption()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "l", "A long option");
        String[] result = parser.burstToken("-l", false);
        String[] expected = new String[] {"--longOption"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenWithLongOptionAndArgument()
    {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "l", "A long option");
        String[] result = parser.burstToken("-l arg", false);
        String[] expected = new String[] {"--longOption", "arg"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptions() throws ParseException
    {
        String[] args = new String[] {"--longOption1", "--longOption2=value2", "--longOption3=value3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        String[] expected = new String[] {"--longOption1", "--longOption2", "value2", "--longOption3", "value3"};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithLongOptionsAndShortOptions() throws ParseException
    {
        String[] args = new String[] {"--foo", "--longOption1", "--longOption2=value2", "--longOption3=value3", "-a"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        String[] expected = new String[] {"--foo", "--longOption1", "--longOption2", "value2", "--longOption3", "value3", "-a"};
        Assert.assertArrayEquals(expected, result);
    }
}