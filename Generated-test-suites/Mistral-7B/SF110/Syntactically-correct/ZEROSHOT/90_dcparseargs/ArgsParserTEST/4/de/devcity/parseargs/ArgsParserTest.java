package de.devcity.parseargs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.arguments.*;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest {

    @Test
    public void testArgsParserConstructionWithNullArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = null;
        try {
            ArgsParser parser = new ArgsParser(args);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetArgsCount() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testSetArgs() {
        String[] originalArgs = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser();
        parser.setArgs(originalArgs);
        String[] newArgs = new String[]{"newArg1", "newArg2"};
        parser.setArgs(newArgs);
        assertArrayEquals(newArgs, parser.getArgs());
        assertNull(parser.getArgs()); // Testing clearing args
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = new String[]{"arg1", "--key", "arg3", "--key2", "arg4"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentWithShortKey() {
        String[] args = new String[]{"-a", "-b", "-c"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("a");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isPresent());
        switchArgument = parser.parseSwitchArgument("c");
        assertEquals(2, switchArgument.getIndex());
        assertTrue(switchArgument.isPresent());
    }

    @Test
    public void testParseSwitchArgumentWithLongKey() {
        String[] args = new String[]{"--longKey", "arg1", "--longKey2"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("longKey");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isPresent());
        switchArgument = parser.parseSwitchArgument("longKey2");
        assertEquals(1, switchArgument.getIndex());
        assertTrue(switchArgument.isPresent());
    }

    @Test
    public void testParseSwitchArgumentWhenKeyIsNotPresent() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("key");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isPresent());
    }

    @Test
    public void testParseParameterArgumentWithShortKey() {
        String[] args = new String[]{"-a=value1", "-b=value2", "-c=value3"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("a");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
        parameterArgument = parser.parseParameterArgument("c");
        assertEquals(2, parameterArgument.getIndex());
        assertEquals("value3", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithLongKey() {
        String[] args = new String[]{"--longKey=value1", "arg1", "--longKey2=value2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("longKey");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
        parameterArgument = parser.parseParameterArgument("longKey2");
        assertEquals(1, parameterArgument.getIndex());
        assertEquals("value2", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWhenKeyIsNotPresent() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWhenKeyIsPresentButNoValue() {
        String[] args = new String[]{"--longKey"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("longKey");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseStringArgument() {
        String[] args = new String[]{"--stringArg=value1", "--stringArg2=value2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        StringArgument firstArgument = stringArguments.get(0);
        StringArgument secondArgument = stringArguments.get(1);
        assertEquals("value1", firstArgument.getValue());
        assertEquals("value2", secondArgument.getValue());
    }

    @Test
    public void testParseStringArgumentWhenNoStringArgumentsPresent() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(0, stringArguments.size());
    }
}