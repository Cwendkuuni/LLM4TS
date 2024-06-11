package org.junit.test;

import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import static org.junit.Assert.assertArrayEquals;

public class GnuParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] args = new String[]{"-Dprop=val", "--option", "arg1", "arg2"};
        Options options = new Options();
        String[] expected = new String[]{"-D", "prop", "val", "--option", "arg1", "arg2"};
        String[] result = new GnuParser().flatten(options, args, false);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEmptyArguments() {
        String[] args = new String[] {};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, args, false);
        String[] expected = new String[] {};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testEmptyOptions() {
        String[] args = new String[]{"arg1", "arg2"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, args, false);
        String[] expected = new String[]{"arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testOptionWithoutValue() {
        String[] args = new String[]{"-opt"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, args, false);
        String[] expected = new String[]{"-opt"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testNonExistentOption() {
        String[] args = new String[]{"-nonexistent"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, args, false);
        String[] expected = new String[]{"-nonexistent"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testOptionWithSpaces() {
        String[] args = new String[]{"-option with spaces"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, args, false);
        String[] expected = new String[]{"-option", "with", "spaces"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testLongArgument() {
        String[] args = new String[]{"--long argument"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, args, false);
        String[] expected = new String[]{"--long argument"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testArgumentWithSpecialCharacters() {
        String[] args = new String[]{"-arg!@#%^%$%&*()_+-=[]{}|;:,.<>/?+-"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, args, false);
        String[] expected = new String[]{"-arg!@#%^%$%&*()_+-=[]{}|;:,.<>/?+-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMixedOptionsAndArguments() {
        String[] args = new String[]{"-Dprop=val", "--option", "arg1", "arg2", "-opt"};
        Options options = new Options();
        String[] result = new GnuParser().flatten(options, args, false);
        String[] expected = new String[]{"-D", "prop", "val", "--option", "arg1", "arg2", "-opt"};
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testStopAtNonOption() {
        String[] args = new String[]{"arg1", "arg2", "--option"};
        Options options = new Options();
        new GnuParser().flatten(options, args, true);
    }
}