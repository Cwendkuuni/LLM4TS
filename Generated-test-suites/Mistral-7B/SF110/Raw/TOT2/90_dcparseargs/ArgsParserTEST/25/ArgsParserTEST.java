package de.devcity.parseargs.test;

import de.devcity.parseargs.arguments.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertArrayEquals(new String[]{"arg1", "arg2"}, parser.getArgs());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--key"});
        SwitchArgument result = parser.parseSwitchArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("key", result.getKey());
        assertEquals(true, result.isLong());

        // Test exception handling
        parser = new ArgsParser(new String[]{});
        try {
            result = parser.parseSwitchArgument("key");
        } catch (Exception e) {
            assertEquals("Key not found", e.getMessage());
        }

        parser = new ArgsParser(new String[]{"--arg"});
        try {
            result = parser.parseSwitchArgument("key");
        } catch (Exception e) {
            assertEquals("Key not found", e.getMessage());
        }
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--key=value"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("key", result.getKey());
        assertEquals("value", result.getValue());

        // Test exception handling
        parser = new ArgsParser(new String[]{});
        try {
            result = parser.parseParameterArgument("key");
        } catch (Exception e) {
            assertEquals("Key not found", e.getMessage());
        }

        parser = new ArgsParser(new String[]{"--key"});
        try {
            result = parser.parseParameterArgument("key");
        } catch (Exception e) {
            assertEquals("Key not found", e.getMessage());
        }

        parser = new ArgsParser(new String[]{"--key1=value1", "--key2=value2", "--key=value"});
        try {
            result = parser.parseParameterArgument("key");
            fail("Multiple keys with the same name should throw an exception");
        } catch (Exception e) {
            assertEquals("Multiple keys with the same name", e.getMessage());
        }
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(2, result.size());
        assertEquals("arg1", result.get(0).getKey());
        assertEquals("value1", result.get(0).getValue());
        assertEquals("arg2", result.get(1).getKey());
        assertEquals("value2", result.get(1).getValue());

        // Test exception handling
        parser = new ArgsParser(new String[]{});
        try {
            result = parser.parseStringArgument();
            fail("No string arguments should throw an exception");
        } catch (Exception e) {
            assertEquals("No string arguments", e.getMessage());
        }

        parser = new ArgsParser(new String[]{"--arg1=value1"});
        try {
            result = parser.parseStringArgument();
            fail("Only one string argument should throw an exception");
        } catch (Exception e) {
            assertEquals("Only one string argument", e.getMessage());
        }
    }
}