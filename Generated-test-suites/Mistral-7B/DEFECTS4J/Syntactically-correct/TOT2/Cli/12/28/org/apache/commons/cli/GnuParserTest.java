package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-D", "property=value", "arg1", "--", "arg2", "-opt", "arg3"};
        String[] expected = {"-D", "property=value", "arg1", "--", "arg2", "-opt"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenEmptyArguments() throws ParseException {
        Options options = new Options();
        String[] actual = new GnuParser().flatten(options, new String[0], false);
        String[] expected = new String[0];
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenEmptyOptions() throws ParseException {
        String[] arguments = {"arg1", "--", "arg2"};
        Assert.assertThrows(NullPointerException.class, () -> new GnuParser().flatten(null, arguments, false));
    }

    @Test
    public void testFlattenInvalidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-I", "invalidOption"};
        Assert.assertThrows(ParseException.class, () -> new GnuParser().flatten(options, arguments, false));
    }

    @Test
    public void testFlattenEmptyArgumentAfterDelimiter() throws ParseException {
        Options options = new Options();
        String[] arguments = {"arg1", "--", "", "arg2"};
        String[] expected = {"arg1", "--", "", "arg2"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenMultipleDelimiters() throws ParseException {
        Options options = new Options();
        String[] arguments = {"arg1", "--", "--", "arg2"};
        String[] expected = {"arg1", "--"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongArgument() throws ParseException {
        Options options = new Options();
        String[] arguments = {"arg1", "--longOptionWithMultipleWords"};
        String[] expected = {"arg1", "--longOptionWithMultipleWords"};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenLongOptionWithoutEqualSign() throws ParseException {
        Options options = new Options();
        String[] arguments = {"arg1", "--longOption"};
        String[] expected = {"arg1", "--longOption", ""};
        String[] actual = new GnuParser().flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testFlattenNonExistentShortOption() throws ParseException {
        Options options = new Options();
        String[] arguments = {"-X", "arg1"};
        Assert.assertThrows(ParseException.class, () -> new GnuParser().flatten(options, arguments, false));
    }

}