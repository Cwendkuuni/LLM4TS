package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    @Test
    public void testArgsParserConstruction() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsCount());
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserNullConstructor() {
        new ArgsParser(null);
    }

    @Test
    public void testArgsParserGetArgsLeftCount() {
        String[] args = new String[]{"arg1", "--key1=value1", "--key2=value2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = new String[]{"arg1", "--key1", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("key1");
        assertEquals(0, switchArgument.getPosition());
        assertTrue(switchArgument.isPresent());

        String[] args2 = new String[]{"arg1", "--key2", "--key1", "arg3"};
        ArgsParser parser2 = new ArgsParser(args2);
        SwitchArgument switchArgument2 = parser2.parseSwitchArgument("key1");
        assertEquals(1, switchArgument2.getPosition());
        assertTrue(switchArgument2.isPresent());

        String[] args3 = new String[]{"arg1", "--key1", "--key2=value2", "arg3"};
        ArgsParser parser3 = new ArgsParser(args3);
        SwitchArgument switchArgument3 = parser3.parseSwitchArgument("key2");
        assertEquals(2, switchArgument3.getPosition());
        assertFalse(switchArgument3.isPresent());

        String[] args4 = new String[]{"arg1", "--key1", "arg3"};
        ArgsParser parser4 = new ArgsParser(args4);
        SwitchArgument switchArgument4 = parser4.parseSwitchArgument("key3");
        assertEquals(-1, switchArgument4.getPosition());
        assertFalse(switchArgument4.isPresent());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = new String[]{"arg1", "--key1=value1", "--key2=value2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key1");
        assertEquals(0, parameterArgument.getPosition());
        assertEquals("value1", parameterArgument.getValue());
        assertNull(parser.innerArgs[0]);

        ParameterArgument parameterArgument2 = parser.parseParameterArgument("key2");
        assertEquals(1, parameterArgument2.getPosition());
        assertEquals("value2", parameterArgument2.getValue());
        assertNull(parser.innerArgs[1]);

        String[] args2 = new String[]{"arg1", "--key1=value1", "--key2=value2", "arg3", "--key1=value3"};
        ArgsParser parser2 = new ArgsParser(args2);
        ParameterArgument parameterArgument3 = parser2.parseParameterArgument("key1");
        assertEquals(0, parameterArgument3.getPosition());
        assertEquals("value3", parameterArgument3.getValue());
        assertNull(parser.innerArgs[0]);
    }

    @Test
    public void testParseStringArgument() {
        String[] args = new String[]{"arg1", "--key1=value1", "--key2=value2", "arg3", "--key1=value3", "--key3=value4"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals("value1", stringArguments.get(0).getValue());
        assertEquals("value3", stringArguments.get(1).getValue());
        assertEquals("value4", stringArguments.get(2).getValue());
    }

}