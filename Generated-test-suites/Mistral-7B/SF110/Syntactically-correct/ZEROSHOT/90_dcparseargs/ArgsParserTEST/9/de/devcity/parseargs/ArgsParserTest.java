package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.Arguments.*;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest
{
    @Test
    public void testArgsParser()
    {
        String[] args = new String[]{"arg1", "arg2", "--key1", "--key2=value1", "--key3", "-key4=value4"};
        ArgsParser parser = new ArgsParser(args);

        assertEquals(6, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());

        assertEquals(4, parser.getArgsLeftCount());

        SwitchArgument switchArgument = parser.parseSwitchArgument("key1");
        assertEquals(0, switchArgument.getIndex());
        assertTrue(switchArgument.isLong());

        switchArgument = parser.parseSwitchArgument("key3");
        assertEquals(3, switchArgument.getIndex());
        assertFalse(switchArgument.isLong());

        switchArgument = parser.parseSwitchArgument("nonExistingKey");
        assertEquals(-1, switchArgument.getIndex());
        assertFalse(switchArgument.isLong());

        ParameterArgument parameterArgument = parser.parseParameterArgument("key2");
        assertEquals(4, parameterArgument.getIndex());
        assertEquals("value1", parameterArgument.getValue());

        parameterArgument = parser.parseParameterArgument("nonExistingKey");
        assertEquals(-1, parameterArgument.getIndex());
        assertNull(parameterArgument);

        List<StringArgument> stringArguments = parser.parseStringArgument();
        List<StringArgument> expectedStringArguments = new ArrayList<StringArgument>();
        expectedStringArguments.add(new StringArgument(1, "arg1"));
        expectedStringArguments.add(new StringArgument(5, "value4"));
        assertEquals(expectedStringArguments, stringArguments);

        parser = new ArgsParser(new String[0]);
        stringArguments = parser.parseStringArgument();
        assertEquals(0, stringArguments.size());

        parser = new ArgsParser(new String[]{"arg1"});
        stringArguments = parser.parseStringArgument();
        assertEquals(1, stringArguments.size());
        assertEquals("arg1", stringArguments.get(0).getValue());
    }
}