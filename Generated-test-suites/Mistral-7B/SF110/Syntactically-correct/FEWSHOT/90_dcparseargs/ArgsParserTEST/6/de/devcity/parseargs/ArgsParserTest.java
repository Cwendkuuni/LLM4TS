package de.devcity.parseargs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParser_emptyArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[0];
        parser.setArgs(args);
        assertEquals(0, parser.getArgsCount());
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testArgsParser_nullArgs() {
        try {
            ArgsParser parser = new ArgsParser();
            parser.setArgs(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testArgsParser() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
        assertEquals(2, parser.getArgsLeftCount());
        assertEquals("arg1", parser.args[0]);
        assertEquals("arg2", parser.args[1]);
    }

    @Test(expected = NullPointerException.class)
    public void testParseSwitchArgument_nullKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg"});
        parser.parseSwitchArgument(null);
    }

    @Test
    public void testParseSwitchArgument_shortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a", "-b"});
        SwitchArgument result = parser.parseSwitchArgument("-a");
        assertTrue(result.isFound());
        assertEquals(0, result.getIndex());
        assertEquals("-a", result.getKey());
        assertTrue(result.isLong());

        result = parser.parseSwitchArgument("-b");
        assertTrue(result.isFound());
        assertEquals(1, result.getIndex());
        assertEquals("-b", result.getKey());
        assertTrue(result.isLong());

        result = parser.parseSwitchArgument("-c");
        assertFalse(result.isFound());
        assertEquals(-1, result.getIndex());
        assertEquals("", result.getKey());
        assertFalse(result.isLong());
    }

    @Test
    public void testParseSwitchArgument_longKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "--arg2"});
        SwitchArgument result = parser.parseSwitchArgument("--arg1");
        assertTrue(result.isFound());
        assertEquals(0, result.getIndex());
        assertEquals("--arg1", result.getKey());
        assertTrue(result.isLong());

        result = parser.parseSwitchArgument("--arg2");
        assertTrue(result.isFound());
        assertEquals(1, result.getIndex());
        assertEquals("--arg2", result.getKey());
        assertTrue(result.isLong());

        result = parser.parseSwitchArgument("--arg3");
        assertFalse(result.isFound());
        assertEquals(-1, result.getIndex());
        assertEquals("", result.getKey());
        assertFalse(result.isLong());
    }

    @Test
    public void testParseParameterArgument_shortKey() {
        ArgsParser parser = new ArgsParser(new String[]{"-a=value1", "-b=value2", "-c"});
        ParameterArgument result = parser.parseParameterArgument("-a");
        assertTrue(result.isFound());
        assertEquals(0, result.getIndex());
        assertEquals("-a", result.getKey());
        assertEquals("value1", result.getValue());

        result = parser.parseParameterArgument("-b");
        assertTrue(result.isFound());
        assertEquals(1, result.getIndex());
        assertEquals("-b", result.getKey());
        assertEquals("value2", result.getValue());

        result = parser.parseParameterArgument("-c");
        assertFalse(result.isFound());
        assertEquals(-1, result.getIndex());
        assertEquals("", result.getKey());
        assertNull(result.getValue());
    }

    @Test
    public void testParseParameterArgument_longKey() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        ParameterArgument result = parser.parseParameterArgument("--arg1");
        assertTrue(result.isFound());
        assertEquals(0, result.getIndex());
        assertEquals("--arg1", result.getKey());
        assertEquals("value1", result.getValue());

        result = parser.parseParameterArgument("--arg2");
        assertTrue(result.isFound());
        assertEquals(1, result.getIndex());
        assertEquals("--arg2", result.getKey());
        assertEquals("value2", result.getValue());

        result = parser.parseParameterArgument("--arg3");
        assertFalse(result.isFound());
        assertEquals(-1, result.getIndex());
        assertEquals("", result.getKey());
        assertNull(result.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(2, result.size());
        StringArgument arg1 = result.get(0);
        StringArgument arg2 = result.get(1);
        assertEquals("--arg1", arg1.getKey());
        assertEquals("value1", arg1.getValue());
        assertEquals("--arg2", arg2.getKey());
        assertEquals("value2", arg2.getValue());
    }

    @Test
    public void testParseStringArgument_emptyList() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1", "--arg2"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(0, result.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseStringArgument_invalidIndex() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        parser.parseSwitchArgument("--arg3"); // consume third argument
        List<StringArgument> result = parser.parseStringArgument();
        fail("Expected IndexOutOfBoundsException");
    }
}