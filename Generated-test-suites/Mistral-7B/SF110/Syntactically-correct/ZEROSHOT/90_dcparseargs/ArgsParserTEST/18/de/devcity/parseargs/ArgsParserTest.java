package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest
{
    @Test
    public void testArgsParserInstance()
    {
        ArgsParser argsParser = new ArgsParser();
        ArgsParser argsParserWithArgs = new ArgsParser(new String[]{"arg1", "arg2"});

        assertEquals(0, argsParser.getArgsCount());
        assertEquals(new String[]{"arg1", "arg2"}, argsParserWithArgs.getArgs());
    }

    @Test
    public void testGetArgsLeftCount()
    {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "--key", "arg4"});
        assertEquals(2, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument()
    {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key", "arg1", "arg2", "--key2"});

        SwitchArgument switchArgumentKey = argsParser.parseSwitchArgument("key");
        SwitchArgument switchArgumentKey2 = argsParser.parseSwitchArgument("key2");

        assertEquals(0, argsParser.getArgsLeftCount());
        assertEquals(0, switchArgumentKey.getIndex());
        assertEquals("key", switchArgumentKey.getKey());
        assertTrue(switchArgumentKey.isPresent());
        assertEquals(1, switchArgumentKey2.getIndex());
        assertEquals("key2", switchArgumentKey2.getKey());
        assertTrue(switchArgumentKey2.isPresent());

        try
        {
            argsParser.parseSwitchArgument("unknownKey");
            fail();
        }
        catch (SwitchArgumentNotFoundException e)
        {
            // expected
        }
    }

    @Test
    public void testParseParameterArgument()
    {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key=value1", "--key2=value2", "arg1", "--key=value3", "arg2"});

        ParameterArgument paramArgumentKey1 = argsParser.parseParameterArgument("key");
        ParameterArgument paramArgumentKey1Value1 = argsParser.parseParameterArgument("key");
        ParameterArgument paramArgumentKey2 = argsParser.parseParameterArgument("key2");

        assertEquals(1, argsParser.getArgsLeftCount());
        assertEquals(0, paramArgumentKey1.getIndex());
        assertEquals("key", paramArgumentKey1.getKey());
        assertEquals("value1", paramArgumentKey1Value1.getValue());
        assertTrue(paramArgumentKey1.isPresent());
        assertTrue(paramArgumentKey1Value1.isPresent());
        assertEquals(2, paramArgumentKey2.getIndex());
        assertEquals("key2", paramArgumentKey2.getKey());
        assertNull(paramArgumentKey2.getValue());
        assertTrue(paramArgumentKey2.isPresent());

        try
        {
            argsParser.parseParameterArgument("unknownKey");
            fail();
        }
        catch (ParameterArgumentNotFoundException e)
        {
            // expected
        }

        try
        {
            argsParser.parseParameterArgument("key");
            fail();
        }
        catch (ParameterArgumentAlreadyParsedException e)
        {
            // expected
        }
    }

    @Test
    public void testParseStringArgument()
    {
        ArgsParser argsParser = new ArgsParser(new String[]{"--key=value1", "--key2=value2", "--key3=value3", "arg1", "--key=value4", "arg2"});

        List<StringArgument> stringArguments = argsParser.parseStringArgument();

        assertEquals(2, argsParser.getArgsLeftCount());
        assertEquals(4, stringArguments.size());
        assertEquals(3, stringArguments.get(0).getIndex());
        assertEquals("arg1", stringArguments.get(0).getValue());
        assertTrue(stringArguments.get(0).isPresent());
        assertEquals(5, stringArguments.get(1).getIndex());
        assertEquals("arg2", stringArguments.get(1).getValue());
        assertTrue(stringArguments.get(1).isPresent());
        assertEquals(1, stringArguments.get(2).getIndex());
        assertEquals("value1", stringArguments.get(2).getValue());
        assertTrue(stringArguments.get(2).isPresent());
        assertEquals(2, stringArguments.get(3).getIndex());
        assertEquals("value2", stringArguments.get(3).getValue());
        assertTrue(stringArguments.get(3).isPresent());
    }
}