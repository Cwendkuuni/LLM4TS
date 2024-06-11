package de.devcity.parseargs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsParserTest {

    @Test
    public void testArgsParserInitialization() {
        ArgsParser argsParser = new ArgsParser();
        assertNull(argsParser.getArgs());
        assertEquals(0, argsParser.getArgsCount());
        assertEquals(0, argsParser.getArgsLeftCount());
    }

    @Test
    public void testArgsParserWithArguments() {
        String[] args = {"arg1", "--key", "arg3", "--key=value", "-k", "arg5"};
        ArgsParser argsParser = new ArgsParser(args);
        assertEquals(args, argsParser.getArgs());
        assertEquals(args.length, argsParser.getArgsCount());
        assertEquals(args.length - 2, argsParser.getArgsLeftCount());

        String[] expectedSwitchArguments = {"--key", "-k"};
        List<SwitchArgument> switchArguments = argsParser.parseSwitchArguments(null);
        assertTrue(switchArguments.size() == expectedSwitchArguments.length);
        for (int i = 0; i < switchArguments.size(); ++i) {
            SwitchArgument switchArgument = switchArguments.get(i);
            String expectedKey = expectedSwitchArguments[i];
            assertEquals(expectedKey, switchArgument.getKey());
            assertTrue(switchArgument.isLong());
            assertEquals(i, switchArgument.getIndex());
        }

        String[] expectedParameterArguments = {"arg1", "arg3", "arg5"};
        List<ParameterArgument> parameterArguments = argsParser.parseParameterArguments(null);
        assertTrue(parameterArguments.size() == expectedParameterArguments.length);
        for (int i = 0; i < parameterArguments.size(); ++i) {
            ParameterArgument parameterArgument = parameterArguments.get(i);
            String expectedKey = "k"; // Assuming the key for the third argument is 'k'
            String expectedValue = null; // No value for switch arguments
            assertEquals(expectedKey, parameterArgument.getKey());
            assertNull(parameterArgument.getValue());
            assertEquals(i, parameterArgument.getIndex());
        }

        List<StringArgument> stringArguments = argsParser.parseStringArguments();
        assertEquals(0, stringArguments.size());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"--key", "arg1", "-k", "arg3", "--key=value"};
        ArgsParser argsParser = new ArgsParser(args);

        SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("key");
        SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("k");
        SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("invalid");

        assertEquals("key", switchArgument1.getKey());
        assertEquals(0, switchArgument1.getIndex());
        assertTrue(switchArgument1.isLong());

        assertEquals("k", switchArgument2.getKey());
        assertEquals(2, switchArgument2.getIndex());
        assertTrue(switchArgument2.isLong());

        assertNull(switchArgument3);
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"arg1", "--key=value", "arg3", "-k=value2", "arg5"};
        ArgsParser argsParser = new ArgsParser(args);

        ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("key");
        ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("invalid");

        assertEquals("key", parameterArgument1.getKey());
        assertEquals(1, parameterArgument1.getIndex());
        assertEquals("value", parameterArgument1.getValue());

        assertNull(parameterArgument2);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"--key=value1", "--custom=value2", "arg3"};
        ArgsParser argsParser = new ArgsParser(args);

        List<StringArgument> stringArguments = argsParser.parseStringArguments();

        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals("key", stringArgument1.getKey());
        assertEquals("value1", stringArgument1.getValue());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals("custom", stringArgument2.getKey());
        assertEquals("value2", stringArgument2.getValue());

        assertTrue(stringArguments.size() == 2);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgs() {
        new ArgsParser(null);
    }
}