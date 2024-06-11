package de.devcity.parseargs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParserEmptyArgs() {
        ArgsParser argsParser = new ArgsParser();
        assertEquals(0, argsParser.getArgsCount());
    }

    @Test
    public void testArgsParserSingleArgument() {
        String[] args = new String[]{"test"};
        ArgsParser argsParser = new ArgsParser(args);
        assertEquals(1, argsParser.getArgsCount());
        assertEquals(args[0], argsParser.args[0]);
    }

    @Test
    public void testArgsParserMultipleArguments() {
        String[] args = new String[]{"test1", "test2", "test3"};
        ArgsParser argsParser = new ArgsParser(args);
        assertEquals(3, argsParser.getArgsCount());
        assertEquals(args[0], argsParser.args[0]);
        assertEquals(args[1], argsParser.args[1]);
        assertEquals(args[2], argsParser.args[2]);
    }

    @Test
    public void testArgsParserNullArgs() {
        String[] args = null;
        ArgsParser argsParser = new ArgsParser(args);
        assertEquals(0, argsParser.getArgsCount());
    }

    @Test
    public void testParseSwitchArgumentSingleShortKey() {
        String[] args = {"-t"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("-t");
        assertTrue(switchArgument.isPresent());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("-t", switchArgument.getKey());
    }

    @Test
    public void testParseSwitchArgumentSingleLongKey() {
        String[] args = {"--test"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("--test");
        assertTrue(switchArgument.isPresent());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("--test", switchArgument.getKey());
    }

    @Test
    public void testParseSwitchArgumentMultipleShortKeys() {
        String[] args = {"-a", "-b", "-c"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgumentA = argsParser.parseSwitchArgument("-a");
        SwitchArgument switchArgumentB = argsParser.parseSwitchArgument("-b");
        SwitchArgument switchArgumentC = argsParser.parseSwitchArgument("-c");
        assertTrue(switchArgumentA.isPresent());
        assertTrue(switchArgumentB.isPresent());
        assertTrue(switchArgumentC.isPresent());
        assertEquals(0, switchArgumentA.getIndex());
        assertEquals(1, switchArgumentB.getIndex());
        assertEquals(2, switchArgumentC.getIndex());
        assertEquals("-a", switchArgumentA.getKey());
        assertEquals("-b", switchArgumentB.getKey());
        assertEquals("-c", switchArgumentC.getKey());
    }

    @Test
    public void testParseSwitchArgumentMissingKey() {
        String[] args = {"-x"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("-y");
        assertFalse(switchArgument.isPresent());
    }

    @Test
    public void testParseSwitchArgumentKeyWithValue() {
        String[] args = {"--test=value"};
        ArgsParser argsParser = new ArgsParser(args);
        SwitchArgument switchArgument = argsParser.parseSwitchArgument("--test");
        assertTrue(switchArgument.isPresent());
        assertEquals(0, switchArgument.getIndex());
        assertEquals("--test", switchArgument.getKey());
        assertEquals("value", switchArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentSingleKeyValuePair() {
        String[] args = {"--key=value"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("--key");
        assertTrue(parameterArgument.isPresent());
        assertEquals(0, parameterArgument.getIndex());
        assertEquals("--key", parameterArgument.getKey());
        assertEquals("value", parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentMissingKey() {
        String[] args = {"--value"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgument = argsParser.parseParameterArgument("--key");
        assertFalse(parameterArgument.isPresent());
    }

    @Test
    public void testParseParameterArgumentMultipleKeyValuePairs() {
        String[] args = {"--key1=value1", "--key2=value2"};
        ArgsParser argsParser = new ArgsParser(args);
        ParameterArgument parameterArgumentKey1 = argsParser.parseParameterArgument("--key1");
        ParameterArgument parameterArgumentKey2 = argsParser.parseParameterArgument("--key2");
        assertTrue(parameterArgumentKey1.isPresent());
        assertTrue(parameterArgumentKey2.isPresent());
        assertEquals(0, parameterArgumentKey1.getIndex());
        assertEquals(1, parameterArgumentKey2.getIndex());
        assertEquals("--key1", parameterArgumentKey1.getKey());
        assertEquals("value1", parameterArgumentKey1.getValue());
        assertEquals("--key2", parameterArgumentKey2.getKey());
        assertEquals("value2", parameterArgumentKey2.getValue());
    }

    @Test
    public void testParseStringArguments() {
        String[] args = {"--key1=value1", "--key2=value2", "-a", "arg1", "-b", "arg2"};
        ArgsParser argsParser = new ArgsParser(args);
        List<StringArgument> stringArguments = argsParser.parseStringArgument();
        assertEquals(4, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertEquals("arg2", stringArguments.get(1).getValue());
        assertNull(stringArguments.get(2).getValue());
        assertNull(stringArguments.get(3).getValue());
    }
}