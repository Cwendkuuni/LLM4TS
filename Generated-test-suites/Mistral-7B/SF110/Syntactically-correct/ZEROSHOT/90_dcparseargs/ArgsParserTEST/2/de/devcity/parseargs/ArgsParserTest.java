package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.devcity.parseargs.arguments.*;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest
{
    private ArgsParser argsParser;

    @Before
    public void setup()
    {
        String[] args = new String[]{"-a", "--test=-123", "--long=-X", "-x=-y", "z"};
        argsParser = new ArgsParser(args);
    }

    @Test
    public void testGetArgsCount()
    {
        assertEquals(argsParser.getArgsCount(), 6);
    }

    @Test
    public void testGetArgs()
    {
        assertArrayEquals(argsParser.getArgs(), new String[]{"-a", "--test=-123", "--long=-X", "-x=-y", "z"});
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull()
    {
        argsParser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount()
    {
        assertEquals(argsParser.getArgsLeftCount(), 3);
    }

    @Test
    public void testParseSwitchArgument()
    {
        SwitchArgument switchArg = argsParser.parseSwitchArgument("-a");
        assertEquals(switchArg.getKey(), "-a");
        assertEquals(switchArg.isLong(), false);
        assertEquals(switchArg.getIndex(), 0);

        switchArg = argsParser.parseSwitchArgument("--long=-X");
        assertEquals(switchArg.getKey(), "--long");
        assertEquals(switchArg.isLong(), true);
        assertEquals(switchArg.getIndex(), 2);

        switchArg = argsParser.parseSwitchArgument("--unknown");
        assertNull(switchArg);

        switchArg = argsParser.parseSwitchArgument("--");
        assertNull(switchArg);
    }

    @Test
    public void testParseParameterArgument()
    {
        ParameterArgument paramArg = argsParser.parseParameterArgument("--test");
        assertNull(paramArg);

        paramArg = argsParser.parseParameterArgument("--test=-123");
        assertEquals(paramArg.getKey(), "--test");
        assertEquals(paramArg.getValue(), "-123");
        assertEquals(paramArg.getIndex(), 1);

        paramArg = argsParser.parseParameterArgument("-x");
        assertEquals(paramArg.getKey(), "-x");
        assertNull(paramArg.getValue());
        assertEquals(paramArg.getIndex(), 3);

        paramArg = argsParser.parseParameterArgument("-x=-y");
        assertEquals(paramArg.getKey(), "-x");
        assertEquals(paramArg.getValue(), "-y");
        assertEquals(paramArg.getIndex(), 3);

        paramArg = argsParser.parseParameterArgument("--unknown=-123");
        assertNull(paramArg);

        paramArg = argsParser.parseParameterArgument("--");
        assertNull(paramArg);
    }

    @Test
    public void testParseStringArgument()
    {
        List<StringArgument> stringArgs = argsParser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<StringArgument>();
        expected.add(new StringArgument(4, "z"));
        assertEquals(stringArgs, expected);

        argsParser.setArgs(new String[]{"-a", "--test=-123", "--long=-X", "-x=-y", "123", "456"});
        stringArgs = argsParser.parseStringArgument();
        expected.clear();
        expected.add(new StringArgument(0, "-a"));
        expected.add(new StringArgument(5, "456"));
        assertEquals(stringArgs, expected);

        argsParser.setArgs(new String[]{"--"});
        stringArgs = argsParser.parseStringArgument();
        assertEquals(stringArgs.size(), 0);
    }
}