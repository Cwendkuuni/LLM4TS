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
        String[] args = new String[]{"-a", "--long-arg=value", "-b", "--long-arg2=anothervalue", "--switch", "arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);

        assertEquals(args.length, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());

        assertEquals(6, parser.getArgsLeftCount());

        SwitchArgument switchArg = parser.parseSwitchArgument("switch");
        assertEquals(-1, switchArg.getIndex());
        assertFalse(switchArg.isSet());

        switchArg = parser.parseSwitchArgument("switch");
        assertEquals(-2, switchArg.getIndex());
        assertTrue(switchArg.isSet());

        ParameterArgument paramArg = parser.parseParameterArgument("long-arg");
        assertEquals(-1, paramArg.getIndex());
        assertNull(paramArg);

        paramArg = parser.parseParameterArgument("long-arg");
        assertEquals(0, paramArg.getIndex());
        assertEquals("value", paramArg.getValue());

        paramArg = parser.parseParameterArgument("long-arg2");
        assertEquals(1, paramArg.getIndex());
        assertEquals("anothervalue", paramArg.getValue());

        paramArg = parser.parseParameterArgument("not-existing-arg");
        assertEquals(-1, paramArg.getIndex());
        assertNull(paramArg);

        List<StringArgument> stringArgs = parser.parseStringArgument();
        List<StringArgument> expectedArgs = new ArrayList<StringArgument>();
        expectedArgs.add(new StringArgument(2, "arg1"));
        expectedArgs.add(new StringArgument(3, "arg2"));
        expectedArgs.add(new StringArgument(5, "arg3"));
        assertEquals(expectedArgs, stringArgs);

        parser = new ArgsParser(new String[0]);
        assertEquals(0, parser.getArgsCount());
        assertEquals(0, parser.getArgsLeftCount());
    }
}