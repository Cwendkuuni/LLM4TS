package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
        options.addOption("d", false, "Option D without an argument");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--b=value", "-c arg1 arg2", "-d", "--"};
        String[] expected = {"-a", "--b", "value", "-c", "arg1", "arg2", "--"};

        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 2, "Option C with two arguments");
        options.addOption("d", false, "Option D without an argument");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--", "-c", "arg1", "arg2", "-d"};
        String[] expected = {"-a", "--", "-c", "arg1", "arg2"};

        String[] result = parser.flatten(options, args, true);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--b", "--"};
        Options options = null;

        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenNullArguments() {
        PosixParser parser = new PosixParser();
        String[] args = null;
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] args = {};
        Options options = new Options();

        parser.flatten(options, args, false);
    }
}