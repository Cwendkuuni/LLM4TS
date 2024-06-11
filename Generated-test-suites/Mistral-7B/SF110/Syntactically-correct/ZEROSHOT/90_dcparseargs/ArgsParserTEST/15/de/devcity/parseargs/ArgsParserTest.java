package de.devcity.parseargs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsParserTest
{
    @Test
    public void testArgsParserInitialization()
    {
        ArgsParser parser = new ArgsParser();
        assertEquals(0, parser.getArgsCount());
        assertNull(parser.getArgs());

        String[] args = {"arg1", "arg2"};
        ArgsParser parserWithArgs = new ArgsParser(args);
        assertEquals(2, parserWithArgs.getArgsCount());
        assertArrayEquals(args, parserWithArgs.getArgs());
    }

    @Test
    public void testArgsParserGetArgsLeftCount()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--key1", "arg1", "--key2", "arg2", "--key2"});
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--switchKey1", "--switchKey2"});
        SwitchArgument switchArgument1 = parser.parseSwitchArgument("switchKey1");
        assertTrue(switchArgument1.getIndex() >= 0);
        assertEquals("switchKey1", switchArgument1.getKey());
        assertTrue(switchArgument1.isSet());

        SwitchArgument switchArgument2 = parser.parseSwitchArgument("switchKey3");
        assertFalse(switchArgument2.getIndex() >= 0);
        assertEquals("switchKey3", switchArgument2.getKey());
        assertFalse(switchArgument2.isSet());

        parser.setArgs(new String[]{"-s", "--switchKey1", "--switchKey2"});
        SwitchArgument switchArgument3 = parser.parseSwitchArgument("switchKey1");
        assertTrue(switchArgument3.getIndex() >= 0);
        assertEquals("switchKey1", switchArgument3.getKey());
        assertTrue(switchArgument3.isSet());

        SwitchArgument switchArgument4 = parser.parseSwitchArgument("switchKey2");
        assertTrue(switchArgument4.getIndex() >= 0);
        assertEquals("switchKey2", switchArgument4.getKey());
        assertTrue(switchArgument4.isSet());

        SwitchArgument switchArgument5 = parser.parseSwitchArgument("switchKey3");
        assertFalse(switchArgument5.getIndex() >= 0);
        assertEquals("switchKey3", switchArgument5.getKey());
        assertFalse(switchArgument5.isSet());
    }

    @Test
    public void testParseParameterArgument()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--key1=value1", "--key2=value2", "--key3"});
        ParameterArgument parameterArgument1 = parser.parseParameterArgument("key1");
        assertTrue(parameterArgument1.getIndex() >= 0);
        assertEquals("key1", parameterArgument1.getKey());
        assertEquals("value1", parameterArgument1.getValue());

        ParameterArgument parameterArgument2 = parser.parseParameterArgument("key2");
        assertTrue(parameterArgument2.getIndex() >= 0);
        assertEquals("key2", parameterArgument2.getKey());
        assertEquals("value2", parameterArgument2.getValue());

        ParameterArgument parameterArgument3 = parser.parseParameterArgument("key3");
        assertFalse(parameterArgument3.getIndex() >= 0);
        assertEquals("key3", parameterArgument3.getKey());
        assertNull(parameterArgument3.getValue());

        parser.setArgs(new String[]{"-k1=value1", "arg1", "--key2=value2", "arg2"});
        ParameterArgument parameterArgument4 = parser.parseParameterArgument("k1");
        assertFalse(parameterArgument4.getIndex() >= 0);
        assertEquals("k1", parameterArgument4.getKey());
        assertNull(parameterArgument4.getValue());

        ParameterArgument parameterArgument5 = parser.parseParameterArgument("key2");
        assertTrue(parameterArgument5.getIndex() >= 0);
        assertEquals("key2", parameterArgument5.getKey());
        assertEquals("value2", parameterArgument5.getValue());
    }

    @Test
    public void testParseStringArguments()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[]{"--arg1=value1", "--arg2=value2"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals("arg1", stringArgument1.getKey());
        assertEquals("value1", stringArgument1.getValue());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals("arg2", stringArgument2.getKey());
        assertEquals("value2", stringArgument2.getValue());
    }
}