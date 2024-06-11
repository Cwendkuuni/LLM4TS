package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest
{
    @Test
    public void testArgsParserConstructor()
    {
        String[] args = new String[] {"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
    }

    @Test
    public void testArgsParserSetter()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[] {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertEquals(args.length, parser.getArgsCount());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserSetterNull()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsCount()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[] {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertEquals(args.length, parser.getArgsCount());
    }

    @Test
    public void testGetArgs()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[] {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testParseSwitchArgument()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[] {"-a", "--long-a"};
        parser.setArgs(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("a");
        assertEquals(0, switchArgument.getArgumentIndex());
        assertEquals("a", switchArgument.getKey());
        assertTrue(switchArgument.isLongKey());

        switchArgument = parser.parseSwitchArgument("long-a");
        assertEquals(0, switchArgument.getArgumentIndex());
        assertEquals("long-a", switchArgument.getKey());
        assertTrue(switchArgument.isLongKey());

        switchArgument = parser.parseSwitchArgument("b");
        assertEquals(-1, switchArgument.getArgumentIndex());
        assertEquals("b", switchArgument.getKey());
        assertFalse(switchArgument.isLongKey());
    }

    @Test
    public void testParseParameterArgument()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[] {"-a=value1", "--long-a=value2"};
        parser.setArgs(args);

        ParameterArgument parameterArgument = parser.parseParameterArgument("a");
        assertEquals(0, parameterArgument.getArgumentIndex());
        assertEquals("a", parameterArgument.getKey());
        assertEquals("value1", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("long-a");
        assertEquals(1, parameterArgument.getArgumentIndex());
        assertEquals("long-a", parameterArgument.getKey());
        assertEquals("value2", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("b");
        assertEquals(-1, parameterArgument.getArgumentIndex());
        assertEquals(null, parameterArgument.getKey());
        assertEquals(null, parameterArgument.getValue());
    }

    @Test
    public void testParseParameterArgumentsWithMissingValue()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[] {"-a", "--long-a"};
        parser.setArgs(args);

        ParameterArgument parameterArgument = parser.parseParameterArgument("a");
        assertEquals(0, parameterArgument.getArgumentIndex());
        assertEquals("a", parameterArgument.getKey());
        assertEquals(null, parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("long-a");
        assertEquals(0, parameterArgument.getArgumentIndex());
        assertEquals("long-a", parameterArgument.getKey());
        assertEquals(null, parameterArgument.getValue());
    }

    @Test
    public void testParseStringArgument()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[] {"--arg1=value1", "--arg2=value2"};
        parser.setArgs(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();

        assertEquals(2, stringArguments.size());

        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals(0, stringArgument1.getArgumentIndex());
        assertEquals("arg1", stringArgument1.getKey());
        assertEquals("value1", stringArgument1.getValue());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals(1, stringArgument2.getArgumentIndex());
        assertEquals("arg2", stringArgument2.getKey());
        assertEquals("value2", stringArgument2.getValue());
    }

    @Test
    public void testParseStringArgumentsWithEmptyValue()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[] {"--arg1=", "--arg2="};
        parser.setArgs(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();

        assertEquals(2, stringArguments.size());

        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals(0, stringArgument1.getArgumentIndex());
        assertEquals("arg1", stringArgument1.getKey());
        assertEquals(null, stringArgument1.getValue());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals(1, stringArgument2.getArgumentIndex());
        assertEquals("arg2", stringArgument2.getKey());
        assertEquals(null, stringArgument2.getValue());
    }
}