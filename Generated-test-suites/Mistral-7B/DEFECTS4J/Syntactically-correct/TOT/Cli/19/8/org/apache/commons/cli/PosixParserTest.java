package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlattenEmpty() {
        String[] arguments = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[0], result);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testFlattenSingleOption() {
        String[] arguments = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--option"}, result);
        assertEquals(2, parser.tokens.size());
        assertEquals("o", parser.tokens.get(1).toString());
    }

    @Test
    public void testFlattenSingleValueOption() {
        String[] arguments = {"--option=value"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--option", "value"}, result);
        assertEquals(3, parser.tokens.size());
        assertEquals("o", parser.tokens.get(1).toString());
    }

    @Test
    public void testFlattenMultipleOptions() {
        String[] arguments = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--option1", "--option2"}, result);
        assertEquals(4, parser.tokens.size());
        assertEquals("o1", parser.tokens.get(1).toString());
        assertEquals("o2", parser.tokens.get(3).toString());
    }

    @Test
    public void testFlattenLongOption() {
        String[] arguments = {"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "lo");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOption"}, result);
        assertEquals(2, parser.tokens.size());
        assertEquals("lo", parser.tokens.get(1).toString());
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        String[] arguments = {"--longOption=value"};
        Options options = new Options();
        options.addOption("longOption", true, "lo");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--longOption", "value"}, result);
        assertEquals(3, parser.tokens.size());
        assertEquals("lo", parser.tokens.get(1).toString());
    }

    @Test
    public void testFlattenMultipleOptionsWithValues() {
        String[] arguments = {"--option1=value1", "--option2=value2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--option1", "value1", "--option2", "value2"}, result);
        assertEquals(6, parser.tokens.size());
        assertEquals("o1", parser.tokens.get(1).toString());
        assertEquals("o2", parser.tokens.get(4).toString());
    }

    @Test
    public void testFlattenSingleDash() {
        String[] arguments = {"-"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"-"}, result);
        assertEquals(1, parser.tokens.size());
    }

    @Test
    public void testFlattenDoubleDash() {
        String[] arguments = {"--"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--"}, result);
        assertEquals(1, parser.tokens.size());
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] arguments = {"--option", "nonOption"};
        Options options = new Options();
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);
        assertEquals(2, parser.tokens.size());
        assertEquals("nonOption", parser.tokens.get(1).toString());
    }

    @Test
    public void testFlattenBurstedOption() {
        String[] arguments = {"--long-option-argument"};
        Options options = new Options();
        options.addOption("longOption", true, "l");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--long-option", "argument"}, result);
        assertEquals(3, parser.tokens.size());
        assertEquals("l", parser.tokens.get(1).toString());
    }

    @Test
    public void testFlattenBurstedOptionWithValue() {
        String[] arguments = {"--long-option--value"};
        Options options = new Options();
        options.addOption("longOption", true, "l");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--long-option", "value"}, result);
        assertEquals(3, parser.tokens.size());
        assertEquals("l", parser.tokens.get(1).toString());
    }

    @Test
    public void testFlattenBurstedOptionWithMultipleValues() {
        String[] arguments = {"--long-option--value1--value2"};
        Options options = new Options();
        options.addOption("longOption", true, "l");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--long-option", "value1", "--long-option", "value2"}, result);
        assertEquals(5, parser.tokens.size());
        assertEquals("l", parser.tokens.get(1).toString());
        assertEquals("l", parser.tokens.get(3).toString());
    }

    @Test
    public void testFlattenBurstedOptionWithValueAndOtherOptions() {
        String[] arguments = {"--long-option--value", "--option"};
        Options options = new Options();
        options.addOption("longOption", true, "l");
        options.addOption("option", true, "o");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertEquals(new String[]{"--long-option", "value", "--option"}, result);
        assertEquals(4, parser.tokens.size());
        assertEquals("l", parser.tokens.get(1).toString());
        assertEquals("o", parser.tokens.get(3).toString());
    }
}