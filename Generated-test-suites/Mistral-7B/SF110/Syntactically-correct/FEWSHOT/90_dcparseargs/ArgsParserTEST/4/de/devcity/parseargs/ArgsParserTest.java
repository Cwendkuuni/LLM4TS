package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[5];
        parser.setArgs(args);
        assertEquals(5, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        String[] result = parser.getArgs();
        assertEquals(args.length, result.length);
        Arrays.sort(args);
        Arrays.sort(result);
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], result[i]);
        }
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertEquals(args.length, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"--arg1", "value1", "--arg2", "value2"};
        parser.setArgs(args);
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentNullKey() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--switch"});
        SwitchArgument result = parser.parseSwitchArgument(null);
        assertNull(result);
    }

    @Test
    public void testParseSwitchArgumentShortKey() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-s"});
        SwitchArgument result = parser.parseSwitchArgument("s");
        assertTrue(result.isPresent());
        assertEquals(0, result.getIndex());
        assertEquals("s", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseSwitchArgumentShortKeyNotPresent() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-s", "arg"});
        SwitchArgument result = parser.parseSwitchArgument("s");
        assertFalse(result.isPresent());
    }

    @Test
    public void testParseSwitchArgumentShortKeyNextToValue() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-s", "arg"});
        SwitchArgument result = parser.parseSwitchArgument("s");
        assertFalse(result.isPresent());
        parser.setArgs(new String[]{"-s", "arg", "-s"});
        result = parser.parseSwitchArgument("s");
        assertTrue(result.isPresent());
        assertEquals(1, result.getIndex());
        assertEquals("s", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseSwitchArgumentShortKeyMultiple() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-s", "-s", "arg"});
        SwitchArgument result = parser.parseSwitchArgument("s");
        assertTrue(result.isPresent());
        assertEquals(0, result.getIndex());
        assertEquals("s", result.getKey());
        assertTrue(result.isSet());
        result = parser.parseSwitchArgument("s");
        assertTrue(result.isPresent());
        assertEquals(1, result.getIndex());
        assertEquals("s", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--long-switch"});
        SwitchArgument result = parser.parseSwitchArgument("long-switch");
        assertTrue(result.isPresent());
        assertEquals(0, result.getIndex());
        assertEquals("long-switch", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseSwitchArgumentLongKeyNextToValue() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--long-switch", "arg"});
        SwitchArgument result = parser.parseSwitchArgument("long-switch");
        assertFalse(result.isPresent());
        parser.setArgs(new String[]{"--long-switch", "--long-switch", "arg"});
        result = parser.parseSwitchArgument("long-switch");
        assertTrue(result.isPresent());
        assertEquals(0, result.getIndex());
        assertEquals("long-switch", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseSwitchArgumentLongKeyMultiple() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--long-switch", "--long-switch", "arg"});
        SwitchArgument result = parser.parseSwitchArgument("long-switch");
        assertTrue(result.isPresent());
        assertEquals(0, result.getIndex());
        assertEquals("long-switch", result.getKey());
        assertTrue(result.isSet());
        result = parser.parseSwitchArgument("long-switch");
        assertTrue(result.isPresent());
        assertEquals(1, result.getIndex());
        assertEquals("long-switch", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseParameterArgumentNullKey() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--param=value"});
        ParameterArgument result = parser.parseParameterArgument(null);
        assertNull(result);
    }

    @Test
    public void testParseParameterArgumentShortKey() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-p=value"});
        ParameterArgument result = parser.parseParameterArgument("p");
        assertEquals("value", result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("p", result.getKey());
    }

    @Test
    public void testParseParameterArgumentShortKeyNotPresent() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-p=value", "arg"});
        ParameterArgument result = parser.parseParameterArgument("p");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgumentShortKeyNextToAnotherShortKey() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-p=value", "-s", "arg"});
        ParameterArgument result = parser.parseParameterArgument("p");
        assertEquals("value", result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("p", result.getKey());
        result = parser.parseParameterArgument("s");
        assertEquals(1, result.getIndex());
        assertEquals("s", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseParameterArgumentShortKeyNextToValue() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-p=value", "arg"});
        ParameterArgument result = parser.parseParameterArgument("p");
        assertEquals("value", result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("p", result.getKey());
        result = parser.parseParameterArgument("p");
        assertNull(result);
        parser.setArgs(new String[]{"-p=value", "arg", "-p=value2"});
        result = parser.parseParameterArgument("p");
        assertEquals("value2", result.getValue());
        assertEquals(2, result.getIndex());
        assertEquals("p", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseParameterArgumentShortKeyMultiple() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"-p=value1", "-p=value2", "arg"});
        ParameterArgument result = parser.parseParameterArgument("p");
        assertEquals("value1", result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("p", result.getKey());
        result = parser.parseParameterArgument("p");
        assertEquals("value2", result.getValue());
        assertEquals(1, result.getIndex());
        assertEquals("p", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseParameterArgumentLongKey() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--long-param=value"});
        ParameterArgument result = parser.parseParameterArgument("long-param");
        assertEquals("value", result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("long-param", result.getKey());
    }

    @Test
    public void testParseParameterArgumentLongKeyNextToValue() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--long-param=value", "arg"});
        ParameterArgument result = parser.parseParameterArgument("long-param");
        assertNull(result);
        parser.setArgs(new String[]{"--long-param=value", "--long-param=value2", "arg"});
        result = parser.parseParameterArgument("long-param");
        assertEquals("value2", result.getValue());
        assertEquals(2, result.getIndex());
        assertEquals("long-param", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseParameterArgumentLongKeyMultiple() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--long-param1=value1", "--long-param2=value2", "arg"});
        ParameterArgument result = parser.parseParameterArgument("long-param1");
        assertEquals("value1", result.getValue());
        assertEquals(0, result.getIndex());
        assertEquals("long-param1", result.getKey());
        result = parser.parseParameterArgument("long-param2");
        assertEquals("value2", result.getValue());
        assertEquals(1, result.getIndex());
        assertEquals("long-param2", result.getKey());
        assertTrue(result.isSet());
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--string=value1", "--string=value2"});
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(2, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals("value" + (i + 1), result.get(i).getValue());
        }
    }
}