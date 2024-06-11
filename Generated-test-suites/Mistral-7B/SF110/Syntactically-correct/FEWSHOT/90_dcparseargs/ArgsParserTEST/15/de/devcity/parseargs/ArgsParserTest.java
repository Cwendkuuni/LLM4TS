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
        ArgsParser emptyArgsParser = new ArgsParser(new String[0]);

        assertTrue(Arrays.equals(new String[0], argsParser.getArgs()));
        assertTrue(Arrays.equals(new String[0], emptyArgsParser.getArgs()));
        assertEquals(0, argsParser.getArgsCount());
        assertEquals(0, emptyArgsParser.getArgsCount());
    }

    @Test
    public void testArgsParserBasicSetterAndGetters() {
        String[] args = {"arg1", "arg2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);

        String[] argsArray = argsParser.getArgs();
        assertEquals(3, argsParser.getArgsCount());
        assertTrue(Arrays.equals(args, argsArray));
    }

    @Test
    public void testArgsParserGetArgsLeftCount() {
        String[] args = {"--foo", "bar", "-arg", "argValue", "--arg2", "-arg3"};
        ArgsParser argsParser = new ArgsParser(args);

        assertEquals(4, argsParser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"--foo", "bar", "-arg", "argValue", "--arg2", "-arg3", "-f"};
        ArgsParser argsParser = new ArgsParser(args);

        SwitchArgument switchArgument = argsParser.parseSwitchArgument("-f");
        assertTrue(switchArgument.isSet());
        assertEquals(5, switchArgument.getIndex());
        assertEquals("-f", switchArgument.getKey());

        switchArgument = argsParser.parseSwitchArgument("--bar");
        assertFalse(switchArgument.isSet());
        assertEquals(-1, switchArgument.getIndex());
        assertEquals("--bar", switchArgument.getKey());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"--foo=1", "bar", "-arg", "argValue", "--arg2=value2", "arg3=value3", "--arg3=value4"};
        ArgsParser argsParser = new ArgsParser(args);

        ParameterArgument parameterArgument = argsParser.parseParameterArgument("--foo");
        assertNull(parameterArgument);

        parameterArgument = argsParser.parseParameterArgument("--arg2");
        assertEquals("value2", parameterArgument.getValue());
        assertEquals(6, parameterArgument.getIndex());
        assertEquals("--arg2", parameterArgument.getKey());

        parameterArgument = argsParser.parseParameterArgument("--arg3");
        assertEquals("value4", parameterArgument.getValue());
        assertEquals(8, parameterArgument.getIndex());
        assertEquals("--arg3", parameterArgument.getKey());

        parameterArgument = argsParser.parseParameterArgument("--unknown");
        assertNull(parameterArgument);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"--foo=1", "bar", "-arg", "argValue", "--arg2=value2", "arg3=value3", "--arg3=value4", "stringArg1=value1", "stringArg2=value2"};
        ArgsParser argsParser = new ArgsParser(args);

        List<StringArgument> stringArguments = argsParser.parseStringArgument();

        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals("value1", stringArgument1.getValue());
        assertEquals(7, stringArgument1.getIndex());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals("value2", stringArgument2.getValue());
        assertEquals(9, stringArgument2.getIndex());
    }
}