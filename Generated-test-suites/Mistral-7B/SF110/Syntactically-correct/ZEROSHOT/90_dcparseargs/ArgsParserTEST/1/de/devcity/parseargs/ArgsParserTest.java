package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest
{
    @Test
    public void testArgsParser()
    {
        String[] args = new String[]{"arg1", "arg2", "--arg3", "--switchArg", "-arg5=value5", "-arg6=value6"};
        ArgsParser parser = new ArgsParser(args);

        assertEquals(args.length, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());

        assertEquals(args.length - 2, parser.getArgsLeftCount());

        SwitchArgument switchArg = parser.parseSwitchArgument("--arg3");
        assertEquals(-3, switchArg.getPosition());
        assertTrue(switchArg.isLong());

        SwitchArgument switchArg2 = parser.parseSwitchArgument("--switchArg");
        assertEquals(-2, switchArg2.getPosition());
        assertTrue(switchArg2.isLong());

        SwitchArgument switchArg3 = parser.parseSwitchArgument("--nonExistingArg");
        assertEquals(-1, switchArg3.getPosition());
        assertFalse(switchArg3.isLong());

        ParameterArgument paramArg = parser.parseParameterArgument("--arg5");
        assertEquals(-4, paramArg.getPosition());
        assertEquals("value5", paramArg.getValue());

        ParameterArgument paramArg2 = parser.parseParameterArgument("--arg6");
        assertEquals(-5, paramArg2.getPosition());
        assertEquals("value6", paramArg2.getValue());

        ParameterArgument paramArg3 = parser.parseParameterArgument("--nonExistingArg");
        assertEquals(-1, paramArg3.getPosition());
        assertNull(paramArg3.getValue());

        List<StringArgument> stringArgs = parser.parseStringArgument();
        List<StringArgument> expectedArgs = new ArrayList<StringArgument>();
        expectedArgs.add(new StringArgument(0, "arg1"));
        expectedArgs.add(new StringArgument(1, "arg2"));
        assertEquals(expectedArgs, stringArgs);
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserWithNullArgs()
    {
        String[] args = null;
        new ArgsParser(args);
    }
}