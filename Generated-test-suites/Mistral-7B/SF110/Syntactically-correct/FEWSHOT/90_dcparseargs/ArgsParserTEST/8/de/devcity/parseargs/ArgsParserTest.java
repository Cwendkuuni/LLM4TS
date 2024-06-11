package de.devcity.parseargs;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        assertEquals(0, parser.getArgsCount());

        String[] args = new String[]{"arg1", "arg2"};
        parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser();
        assertNull(parser.getArgs());

        String[] args = new String[]{"arg1", "arg2"};
        parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());

        parser.setArgs(null);
        assertNull(parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        assertEquals(0, parser.getArgsLeftCount());

        String[] args = new String[]{"--key1", "-k value1", "arg2"};
        parser = new ArgsParser(args);
        assertEquals(1, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser();

        // Test with null input
        assertNull(parser.parseSwitchArgument(null));

        // Test with empty input
        parser = new ArgsParser(new String[0]);
        assertNull(parser.parseSwitchArgument("key"));

        // Test with no matching switch argument
        parser = new ArgsParser(new String[]{"arg1", "--key", "arg3"});
        assertNull(parser.parseSwitchArgument("switch"));

        // Test with matching long switch argument
        parser = new ArgsParser(new String[]{"--switch", "arg1"});
        SwitchArgument result = parser.parseSwitchArgument("switch");
        assertEquals(0, result.getIndex());
        assertTrue(result.isPresent());

        // Test with matching short switch argument
        parser = new ArgsParser(new String[]{"-s", "arg1"});
        result = parser.parseSwitchArgument("s");
        assertEquals(0, result.getIndex());
        assertTrue(result.isPresent());
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser();

        // Test with null input
        assertNull(parser.parseParameterArgument(null));

        // Test with no matching parameter argument
        parser = new ArgsParser(new String[]{"arg1", "--key", "arg3"});
        assertNull(parser.parseParameterArgument("param"));

        // Test with matching long parameter argument
        parser = new ArgsParser(new String[]{"--key=value", "arg1"});
        ParameterArgument result = parser.parseParameterArgument("key");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());

        // Test with matching short parameter argument
        parser = new ArgsParser(new String[]{"-k=value", "arg1"});
        result = parser.parseParameterArgument("k");
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();

        // Test with empty input
        parser = new ArgsParser(new String[0]);
        List<StringArgument> result = parser.parseStringArgument();
        assertEmpty(result);

        // Test with multiple string arguments
        parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        result = parser.parseStringArgument();
        assertEquals(2, result.size());
        assertEquals("value1", result.get(0).getValue());
        assertEquals("value2", result.get(1).getValue());

        // Test with a single string argument
        parser = new ArgsParser(new String[]{"--arg=value"});
        result = parser.parseStringArgument();
        assertEquals(1, result.size());
        assertEquals("value", result.get(0).getValue());

        // Test with no string arguments
        parser = new ArgsParser(new String[]{"arg1"});
        result = parser.parseStringArgument();
        assertEmpty(result);
    }
}