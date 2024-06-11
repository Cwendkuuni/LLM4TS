package de.devcity.parseargs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.devcity.parseargs.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ArgsParserTest
{
    @Test
    public void testArgsParserConstructor()
    {
        String[] args = new String[]{"arg1", "arg2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(args.length, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserNullConstructor()
    {
        new ArgsParser(null);
    }

    @Test
    public void testGetArgsCount()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testGetArgs()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, parser.getArgs());
    }

    @Test
    public void testSetArgs()
    {
        ArgsParser parser = new ArgsParser();
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertEquals(args.length, parser.getArgsCount());
        assertArrayEquals(args, parser.getArgs());
    }

    @Test(expected = NullPointerException.class)
    public void testSetArgsNull()
    {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(null);
    }

    @Test
    public void testGetArgsLeftCount()
    {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3", "arg4", "arg5"});
        assertEquals(5 - 2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument()
    {
        ArgsParser parser = new ArgsParser(new String[]{"-s", "--longSwitch", "arg1", "--anotherSwitch"});
        SwitchArgument result = parser.parseSwitchArgument("s");
        assertSame(0, result.getIndex());
        assertTrue(result.isValid());

        result = parser.parseSwitchArgument("longSwitch");
        assertSame(1, result.getIndex());
        assertTrue(result.isValid());

        result = parser.parseSwitchArgument("anotherSwitch");
        assertSame(3, result.getIndex());
        assertTrue(result.isValid());

        result = parser.parseSwitchArgument("invalid");
        assertSame(-1, result.getIndex());
        assertFalse(result.isValid());

        parser = new ArgsParser(new String[]{"-s", "--longSwitch"});
        result = parser.parseSwitchArgument("s");
        assertSame(0, result.getIndex());
        assertTrue(result.isValid());

        result = parser.parseSwitchArgument("longSwitch");
        assertSame(1, result.getIndex());
        assertTrue(result.isValid());

        result = parser.parseSwitchArgument("invalid");
        assertSame(-1, result.getIndex());
        assertFalse(result.isValid());
    }

    @Test
    public void testParseStringArgument()
    {
        ArgsParser parser = new ArgsParser(new String[]{"-s", "--longSwitch", "arg1=value1", "--anotherSwitch=value2"});
        List<StringArgument> result = parser.parseStringArgument();
        List<StringArgument> expected = Arrays.asList(new StringArgument(1, "value1"), new StringArgument(3, "value2"));
        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseStringArgumentMissingValue()
    {
        ArgsParser parser = new ArgsParser(new String[]{"-s", "--longSwitch", "arg1"});
        parser.parseStringArgument();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseSwitchArgumentInvalidKey()
    {
        ArgsParser parser = new ArgsParser(new String[]{"-s", "--longSwitch", "arg1"});
        parser.parseSwitchArgument("invalidKey");
    }
}