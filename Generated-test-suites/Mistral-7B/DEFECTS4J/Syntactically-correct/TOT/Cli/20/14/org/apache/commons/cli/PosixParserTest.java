package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        String[] args = {"--foo", "--bar=baz", "-a", "--quux", "-x", "-y", "--", "z", "w", "e"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("quux", null, "q");
        DefaultParser parser = new DefaultParser();
        try {
            String[] result = parser.parse(options, args, false);
            List<String> expected = Arrays.asList("--foo", "--bar", "baz", "-a", "--quux", "-x", "--", "z", "w", "e");
            Assert.assertArrayEquals(expected.toArray(), result);
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "--bar=baz", "-a", "--", "z", "w", "e", "-x"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        DefaultParser parser = new DefaultParser();
        try {
            String[] result = parser.parse(options, args, true);
            List<String> expected = Arrays.asList("--foo", "--bar=baz", "-a", "--", "z", "w", "e", "-x");
            Assert.assertArrayEquals(expected.toArray(), result);
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithInvalidOption() {
        String[] args = {"--foo", "--bar=baz", "-a", "--quux-xyz", "-x"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("quux", null, "q");
        DefaultParser parser = new DefaultParser();
        try {
            String[] result = parser.parse(options, args, false);
            List<String> expected = Arrays.asList("--foo", "--bar", "baz", "-a", "--quux-xyz", "-x");
            Assert.assertArrayEquals(expected.toArray(), result);
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: quux-xyz", e.getMessage());
        }
    }

    @Test
    public void testFlattenWithMixedOptionsAndValues() {
        String[] args = {"--foo=bar", "--quux=xyz", "-a", "--bar=baz", "-x", "--", "z", "w", "e"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("quux", null, "q");
        DefaultParser parser = new DefaultParser();
        try {
            String[] result = parser.parse(options, args, false);
            List<String> expected = Arrays.asList("--foo", "bar", "--quux", "xyz", "-a", "--bar", "baz", "-x", "--", "z", "w", "e");
            Assert.assertArrayEquals(expected.toArray(), result);
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        String[] args = {"--foo=longlonglongoptionvalue"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        DefaultParser parser = new DefaultParser();
        try {
            String[] result = parser.parse(options, args, false);
            List<String> expected = Arrays.asList("--foo", "longlonglongoptionvalue");
            Assert.assertArrayEquals(expected.toArray(), result);
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithUnmatchedArguments() {
        String[] args = {"--foo", "bar"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        DefaultParser parser = new DefaultParser();
        try {
            String[] result = parser.parse(options, args, false);
            List<String> expected = Arrays.asList("--foo", "bar");
            Assert.assertArrayEquals(expected.toArray(), result);
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithEmptyArguments() {
        String[] args = {};
        Options options = new Options();
        DefaultParser parser = new DefaultParser();
        try {
            String[] result = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[0], result);
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithMixedArguments() {
        String[] args = {"--foo", "bar", "baz", "--quux"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("quux", null, "q");
        DefaultParser parser = new DefaultParser();
        try {
            String[] result = parser.parse(options, args, false);
            List<String> expected = Arrays.asList("--foo", "bar", "--quux");
            Assert.assertArrayEquals(expected.toArray(), result);
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception: " + e.getMessage());
        }
    }

    @Test
    public void testHelp() {
        String[] args = {"--help"};
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        DefaultParser parser = new DefaultParser();
        try {
            parser.parse(options, args, true);
            String help = formatter.printHelp("Test Program", options);
            System.out.println(help);
        } catch (ParseException e) {
            Assert.fail("Parsing should not have thrown an exception: " + e.getMessage());
        }
    }
}