package de.devcity.parseargs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.arguments.*;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest
{
    @Test
    public void testArgsParserConstructionWithNullArgs()
    {
        try
        {
            new ArgsParser(null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {
            //expected
        }
    }

    @Test
    public void testArgsParserConstructionWithEmptyArgs()
    {
        ArgsParser parser = new ArgsParser(new String[0]);
        assertEquals(0, parser.getArgsCount());
        assertEquals(new String[0], parser.getArgs());
    }

    @Test
    public void testArgsParserConstructionWithArgs()
    {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());

        parser.setArgs(new String[]{"arg1", null, "arg3"});
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument()
    {
        ArgsParser parser = new ArgsParser(new String[]{"--test", "-t"});
        SwitchArgument switchArg1 = parser.parseSwitchArgument("test");
        SwitchArgument switchArg2 = parser.parseSwitchArgument("t");
        SwitchArgument switchArg3 = parser.parseSwitchArgument("invalid");

        assertEquals(-1, switchArg3.getIndex());
        assertEquals("invalid", switchArg3.getKey());
        assertFalse(switchArg3.isPresent());

        assertEquals(0, switchArg1.getIndex());
        assertEquals("test", switchArg1.getKey());
        assertTrue(switchArg1.isPresent());

        assertEquals(1, switchArg2.getIndex());
        assertEquals("t", switchArg2.getKey());
        assertTrue(switchArg2.isPresent());
    }

    @Test
    public void testParseParameterArgument()
    {
        ArgsParser parser = new ArgsParser(new String[]{"--key=value", "-k", "value2"});

        ParameterArgument paramArg1 = parser.parseParameterArgument("key");
        ParameterArgument paramArg2 = parser.parseParameterArgument("k");

        assertNull(paramArg1);
        assertNull(paramArg2);

        paramArg1 = parser.parseParameterArgument("value");
        paramArg2 = parser.parseParameterArgument("invalid");

        assertEquals(-1, paramArg2.getIndex());
        assertEquals("invalid", paramArg2.getKey());
        assertFalse(paramArg2.isPresent());

        assertEquals(0, paramArg1.getIndex());
        assertEquals("value", paramArg1.getKey());
        assertEquals("value", paramArg1.getValue());
        assertTrue(paramArg1.isPresent());

        parser.setArgs(new String[]{"--key=value", "-k", "value2", "arg"});
        paramArg1 = parser.parseParameterArgument("key");
        paramArg2 = parser.parseParameterArgument("k");
        paramArg2 = parser.parseParameterArgument("arg");

        assertEquals(0, paramArg1.getIndex());
        assertEquals("value", paramArg1.getValue());
        assertEquals("value", paramArg1.getKey());
        assertTrue(paramArg1.isPresent());

        assertEquals(2, paramArg2.getIndex());
        assertEquals("arg", paramArg2.getKey());
        assertNull(paramArg2.getValue());
        assertTrue(paramArg2.isPresent());
    }

    @Test
    public void testParseStringArgument()
    {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2"});
        List<StringArgument> stringArguments = parser.parseStringArgument();

        assertEquals(2, stringArguments.size());

        StringArgument stringArgument1 = stringArguments.get(0);
        StringArgument stringArgument2 = stringArguments.get(1);

        assertEquals(0, stringArgument1.getIndex());
        assertEquals("value1", stringArgument1.getValue());
        assertEquals("arg1", stringArgument1.getKey());
        assertTrue(stringArgument1.isPresent());

        assertEquals(1, stringArgument2.getIndex());
        assertEquals("value2", stringArgument2.getValue());
        assertEquals("arg2", stringArgument2.getKey());
        assertTrue(stringArgument2.isPresent());
    }

    @Test
    public void testParseStringArgumentsWithMixedTypes()
    {
        ArgsParser parser = new ArgsParser(new String[]{"--arg1=value1", "--arg2=value2", "-k", "arg"});
        List<StringArgument> stringArguments = parser.parseStringArgument();

        assertEquals(2, stringArguments.size());

        StringArgument stringArgument1 = stringArguments.get(0);
        StringArgument stringArgument2 = stringArguments.get(1);
        SwitchArgument switchArgument = parser.parseSwitchArgument("k");

        assertEquals(0, stringArgument1.getIndex());
        assertEquals("value1", stringArgument1.getValue());
        assertEquals("arg1", stringArgument1.getKey());
        assertTrue(stringArgument1.isPresent());

        assertEquals(2, stringArgument2.getIndex());
        assertEquals("arg", stringArgument2.getKey());
        assertNull(stringArgument2.getValue());
        assertTrue(stringArgument2.isPresent());

        assertEquals(3, switchArgument.getIndex());
        assertEquals("k", switchArgument.getKey());
        assertTrue(switchArgument.isPresent());
    }
}