package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testArgsParserConstructionWithEmptyArgs() {
        ArgsParser argsParser = new ArgsParser();
        assertEquals(0, argsParser.getArgsCount());
    }

    @Test
    public void testArgsParserConstructionWithArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        assertEquals(3, argsParser.getArgsCount());
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserConstructionWithNullArgs() {
        ArgsParser argsParser = new ArgsParser(null);
    }

    @Test
    public void testGetArgsCount() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        assertEquals(3, argsParser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test
    public void testSetArgs() {
        ArgsParser argsParser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        argsParser.setArgs(args);
        assertEquals(3, argsParser.getArgsCount());
        assertArrayEquals(args, argsParser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsWithNullArgs() {
        ArgsParser argsParser = new ArgsParser();
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", "key1=value1", "key2=value2"};
        ArgsParser argsParser = new ArgsParser(args);
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentWithValidShortKey() {
        String[] args = {"arg1", "-a", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("a");
        assertEquals(1, switchArgument.getIndex());
        assertTrue(switchArgument.isLongKey());
        assertFalse(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgumentWithValidLongKey() {
        String[] args = {"arg1", "--long-key"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("long-key");
        assertEquals(0, switchArgument.getIndex());
        assertFalse(switchArgument.isLongKey());
        assertFalse(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgumentWithValidLongKeyAndValue() {
        String[] args = {"arg1", "--long-key=value"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("long-key");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isLongKey());
        assertEquals("value", switchArgument.getValue());
    }

    @Test
    public void testParseSwitchArgumentWithInvalidShortKey() {
        String[] args = {"arg1", "-a", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("b");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isLongKey());
        assertFalse(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgumentWithInvalidLongKey() {
        String[] args = {"arg1", "--invalid-key", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("invalid-key");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isLongKey());
        assertFalse(switchArgument.hasValue());
    }

    @Test
    public void testParseSwitchArgumentWithValidShortKeyAndNextArgument() {
        String[] args = {"arg1", "-a", "arg3", "arg4"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("a");
        assertEquals(1, switchArgument.getIndex());
        assertFalse(switchArgument.isLongKey());
        assertFalse(switchArgument.hasValue());
        assertEquals("arg3", argsParser.getArgs()[switchArgument.getIndex() + 1]);
    }

    @Test
    public void testParseSwitchArgumentWithInvalidShortKeyAndNextArgument() {
        String[] args = {"arg1", "-b", "arg3", "arg4"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("a");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isLongKey());
        assertFalse(switchArgument.hasValue());
        assertEquals("arg3", argsParser.getArgs()[switchArgument.getIndex() + 1]);
    }

    @Test
    public void testParseParameterArgumentWithValidShortKey() {
        String[] args = {"arg1", "-k=value", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        assertEquals(1, parameterArgument.getIndex());
        assertFalse(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithValidLongKey() {
        String[] args = {"arg1", "--long-key=value", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("long-key");
        assertEquals(0, parameterArgument.getIndex());
        assertTrue(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentWithInvalidShortKey() {
        String[] args = {"arg1", "-k=value", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("l");
        assertEquals(-1, parameterArgument.getIndex());
        assertFalse(parameterArgument.isLongKey());
        assertFalse(parameterArgument.hasValue());
    }

    @Test
    public void testParseParameterArgumentWithInvalidLongKey() {
        String[] args = {"arg1", "--long-key=value", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("invalid-key");
        assertEquals(-1, parameterArgument.getIndex());
        assertFalse(parameterArgument.isLongKey());
        assertFalse(parameterArgument.hasValue());
    }

    @Test
    public void testParseParameterArgumentWithValidShortKeyAndNextArgument() {
        String[] args = {"arg1", "-k=value", "arg3", "arg4"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        assertEquals(1, parameterArgument.getIndex());
        assertFalse(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
        assertEquals("arg3", argsParser.getArgs()[parameterArgument.getIndex() + 1]);
    }

    @Test
    public void testParseParameterArgumentWithValidShortKeyAndNoNextArgument() {
        String[] args = {"arg1", "-k=value"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("k");
        assertEquals(0, parameterArgument.getIndex());
        assertFalse(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
        assertEquals(null, argsParser.getArgs()[parameterArgument.getIndex() + 1]);
    }

    @Test
    public void testParseParameterArgumentWithValidLongKeyAndNextArgument() {
        String[] args = {"arg1", "--long-key=value", "arg3", "arg4"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("long-key");
        assertEquals(0, parameterArgument.getIndex());
        assertTrue(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
        assertEquals("arg3", argsParser.getArgs()[parameterArgument.getIndex() + 1]);
    }

    @Test
    public void testParseParameterArgumentWithValidLongKeyAndNoNextArgument() {
        String[] args = {"arg1", "--long-key=value"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("long-key");
        assertEquals(0, parameterArgument.getIndex());
        assertTrue(parameterArgument.isLongKey());
        assertEquals("value", parameterArgument.getValue());
        assertEquals(null, argsParser.getArgs()[parameterArgument.getIndex() + 1]);
    }

    @Test
    public void testParseStringArguments() {
        String[] args = {"arg1", "--key1=value1", "--key2=value2", "--key3=value3"};
        ArgsParser argsParser = new ArgsParser(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        List<StringArgument> expectedStringArguments = new ArrayList<>();
        expectedStringArguments.add(new StringArgument(1, "value1"));
        expectedStringArguments.add(new StringArgument(3, "value2"));
        expectedStringArguments.add(new StringArgument(5, "value3"));
        assertEquals(expectedStringArguments, stringArguments);
    }
}