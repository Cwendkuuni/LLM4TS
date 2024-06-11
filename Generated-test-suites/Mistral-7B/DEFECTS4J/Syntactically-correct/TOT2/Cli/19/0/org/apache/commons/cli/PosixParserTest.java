package org.junit.test;

import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;

public class PosixParserTest {

    @Test
    public void testParserFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "-a", "--bar=test", "-b", "--baz"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--foo", "-a", "--bar", "test", "--baz"}, parsedArguments);
    }

    @Test
    public void testParserFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "-a", "--bar=test", "-b", "--baz", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"--foo", "-a", "--bar", "test", "--baz", "nonOption"}, parsedArguments);
    }

    @Test(expected = ParseException.class)
    public void testParserFlattenWithInvalidOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "-a", "--invalidOption", "--bar=test", "-b", "--baz"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testBurstTokenForValidOption() {
        String token = "--foo";
        List<String> result = PosixParser.burstToken(token, false);
        Assert.assertEquals(Arrays.asList("--foo"), result);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenForInvalidOptionWithStopAtNonOption() {
        String token = "--invalidOption";
        PosixParser.burstToken(token, true);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenForInvalidOption() {
        String token = "--invalidOption";
        List<String> result = PosixParser.burstToken(token, false);
        Assert.assertEquals(Arrays.asList("--", "--invalidOption"), result);
    }
}