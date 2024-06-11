package de.devcity.parseargs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;

public class ArgsParserTest {

    @Test
    public void testArgsParserConstructor() {
        String[] args = new String[]{"test", "arg1", "arg2", "--key1", "--longKey1=value1", "--key2=value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = new String[]{"test", "arg1", "arg2", "--key1", "--longKey1=value1", "--key2=value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(4, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = new String[]{"test", "--key1", "--longKey1=value1", "--key2", "--longKey2=value2"};
        ArgsParser parser = new ArgsParser(args);

        SwitchArgument switchArgument1 = parser.parseSwitchArgument("key1");
        SwitchArgument switchArgument2 = parser.parseSwitchArgument("key2");
        SwitchArgument switchArgument3 = parser.parseSwitchArgument("longKey3");

        assertEquals(-1, switchArgument3.getIndex());
        assertEquals("key1", switchArgument1.getKey());
        assertTrue(switchArgument1.isLong());
        assertFalse(switchArgument2.isLong());
        assertEquals("key2", switchArgument2.getKey());
        assertSame(1, switchArgument1.getIndex());
        assertSame(2, switchArgument2.getIndex());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = new String[]{"test", "--key1=value1", "arg2", "--key2=value2", "arg3", "--key3=value3"};
        ArgsParser parser = new ArgsParser(args);

        ParameterArgument parameterArgument1 = parser.parseParameterArgument("key1");
        ParameterArgument parameterArgument2 = parser.parseParameterArgument("key3");

        assertNull(parameterArgument2);
        assertEquals("value1", parameterArgument1.getValue());
        assertEquals(1, parameterArgument1.getIndex());
    }

    @Test(expected = NullPointerException.class)
    public void testParseParameterArgumentNullArgs() {
        ArgsParser parser = new ArgsParser(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testParseParameterArgumentIndexOutOfBounds() {
        String[] args = new String[]{"test", "--key1=value1"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseParameterArgument("notExistingKey");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseParameterArgumentEmptyKey() {
        String[] args = new String[]{"test", "--key1=value1", "--"};
        ArgsParser parser = new ArgsParser(args);
        parser.parseParameterArgument("");
    }

    @Test
    public void testParseStringArgument() {
        String[] args = new String[]{"test", "--key1=value1", "--key2=value2", "--key3=value3", "arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();

        assertEquals(5, stringArguments.size());
        StringArgument stringArgument1 = stringArguments.get(0);
        StringArgument stringArgument2 = stringArguments.get(4);
        assertEquals("arg1", stringArgument1.getValue());
        assertEquals("arg2", stringArgument2.getValue());
    }

}