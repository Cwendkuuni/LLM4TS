package de.devcity.parseargs;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {
    @Test
    public void testParseArgsCount() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        assertEquals(0, parser.getArgsCount());

        args = new String[]{"arg1"};
        parser = new ArgsParser(args);
        assertEquals(1, parser.getArgsCount());

        args = new String[]{"arg1", "arg2", "arg3"};
        parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testParseArgs() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        String[] expectedArgs = new String[0];
        assertArrayEquals(expectedArgs, parser.getArgs());

        args = new String[]{"arg1"};
        parser = new ArgsParser(args);
        expectedArgs = new String[]{"arg1"};
        assertArrayEquals(expectedArgs, parser.getArgs());

        args = new String[]{"arg1", "arg2", "arg3"};
        parser = new ArgsParser(args);
        expectedArgs = new String[]{"arg1", "arg2", "arg3"};
        assertArrayEquals(expectedArgs, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());

        args = new String[]{"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = new String[0].clone();
        ArgsParser parser = new ArgsParser();
        parser.setArgs(args);
        assertEquals(0, parser.getArgsLeftCount());

        args = new String[]{"arg1", null, "arg3"}.clone();
        parser.setArgs(args);
        assertEquals(2, parser.getArgsLeftCount());

        args = new String[0].clone();
        parser.setArgs(args);
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentShort() {
        String[] args = new String[]{"-s", "arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("-s");
        assertEquals(0, result.getIndex());
        assertTrue(result.isSet());

        result = parser.parseSwitchArgument("-t");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isSet());
    }

    @Test
    public void testParseSwitchArgumentLong() {
        String[] args = new String[]{"--switch", "arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument result = parser.parseSwitchArgument("--switch");
        assertEquals(0, result.getIndex());
        assertTrue(result.isSet());

        result = parser.parseSwitchArgument("--other");
        assertEquals(-1, result.getIndex());
        assertFalse(result.isSet());
    }

    @Test
    public void testParseParameterArgumentShort() {
        String[] args = new String[]{"-p", "arg1", "value1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("-p");
        assertEquals(0, result.getIndex());
        assertEquals("value1", result.getValue());

        result = parser.parseParameterArgument("-q");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test
    public void testParseParameterArgumentLong() {
        String[] args = new String[]{"--param", "arg1", "value1", "--param2", "arg2", "value2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument result = parser.parseParameterArgument("--param");
        assertEquals(0, result.getIndex());
        assertEquals("value1", result.getValue());

        result = parser.parseParameterArgument("--param2");
        assertEquals(3, result.getIndex());
        assertEquals("value2", result.getValue());

        result = parser.parseParameterArgument("--nonexistent");
        assertEquals(-1, result.getIndex());
        assertNull(result.getValue());
    }

    @Test
    public void testParseStringArguments() {
        String[] args = new String[]{"--string1", "value1", "--string2", "value2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> result = parser.parseStringArgument();
        List<StringArgument> expected = Arrays.asList(new StringArgument(0, "value1"), new StringArgument(2, "value2"));
        assertEquals(expected, result);
    }
}