package de.devcity.parseargs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.devcity.parseargs.arguments.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
        assertEquals("arg3", args[2]);
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", "arg2", "arg3"});
        String[] args = parser.getArgs();
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
        assertEquals("arg3", args[2]);
    }

    @Test
    public void testGetArgsLeftCount() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgumentShortKey() {
        ArgsParser parser = new ArgsParser(new String[] {"-s"});
        assertEquals(0, parser.getArgsLeftCount());
        SwitchArgument switchArgument = parser.parseSwitchArgument("s");
        assertEquals(0, parser.getArgsLeftCount());
        assertTrue(switchArgument.isPresent());
        assertEquals(0, switchArgument.getIndex());
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        ArgsParser parser = new ArgsParser(new String[] {"--longKey"});
        assertEquals(0, parser.getArgsLeftCount());
        SwitchArgument switchArgument = parser.parseSwitchArgument("longKey");
        assertEquals(0, parser.getArgsLeftCount());
        assertTrue(switchArgument.isPresent());
        assertEquals(0, switchArgument.getIndex());
    }

    @Test
    public void testParseSwitchArgumentMissingKey() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2"});
        SwitchArgument switchArgument = parser.parseSwitchArgument("unknown");
        assertEquals(2, parser.getArgsLeftCount());
        assertFalse(switchArgument.isPresent());
        assertNull(switchArgument.getKey());
    }

    @Test
    public void testParseParameterArgumentShortKey() {
        ArgsParser parser = new ArgsParser(new String[] {"-p=value"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("p");
        assertEquals(0, parser.getArgsLeftCount());
        assertTrue(parameterArgument.isPresent());
        assertEquals("value", parameterArgument.getValue());
        assertEquals(0, parameterArgument.getIndex());
    }

    @Test
    public void testParseParameterArgumentLongKeyWithValue() {
        ArgsParser parser = new ArgsParser(new String[] {"--longKey=value"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("longKey");
        assertEquals(0, parser.getArgsLeftCount());
        assertTrue(parameterArgument.isPresent());
        assertEquals("value", parameterArgument.getValue());
        assertEquals(0, parameterArgument.getIndex());
    }

    @Test
    public void testParseParameterArgumentLongKeyWithoutValue() {
        ArgsParser parser = new ArgsParser(new String[] {"--longKey"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("longKey");
        assertEquals(1, parser.getArgsLeftCount());
        assertFalse(parameterArgument.isPresent());
        assertNull(parameterArgument.getValue());
        assertEquals(-1, parser.getLastIndexOf("--longKey"));
    }

    @Test
    public void testParseParameterArgumentMissingKey() {
        ArgsParser parser = new ArgsParser(new String[] {"arg1", "arg2"});
        ParameterArgument parameterArgument = parser.parseParameterArgument("unknown");
        assertEquals(2, parser.getArgsLeftCount());
        assertFalse(parameterArgument.isPresent());
        assertNull(parameterArgument.getValue());
    }

    @Test
    public void testParseStringArguments() {
        ArgsParser parser = new ArgsParser(new String[] {"-a=value1", "-b=value2", "-c=value3"});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals("value1", stringArgument1.getValue());
        assertEquals(0, stringArgument1.getIndex());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals("value2", stringArgument2.getValue());
        assertEquals(1, stringArgument2.getIndex());

        StringArgument stringArgument3 = stringArguments.get(2);
        assertEquals("value3", stringArgument3.getValue());
        assertEquals(2, stringArgument3.getIndex());

        assertEquals(0, parser.getArgsLeftCount());
    }
}