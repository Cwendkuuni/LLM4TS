package de.devcity.parseargs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParser_EmptyArgs() {
        ArgsParser parser = new ArgsParser();
        assertEquals(0, parser.getArgsCount());
    }

    @Test
    public void testArgsParser_SimpleArgs() {
        String[] args = {"-a", "-b", "--c", "--d=value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(4, parser.getArgsCount());
        assertEquals(args, parser.getArgs());

        String[] expectedInnerArgs = {"a", "b", "c", "d=value"};
        assertArrayEquals(expectedInnerArgs, parser.innerArgs);

        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testArgsParser_ParsingSwitchArguments() {
        String[] args = {"--foo", "--bar", "-a", "-b", "--baz"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(5, parser.getArgsCount());

        SwitchArgument switchArgFoo = parser.parseSwitchArgument("foo");
        SwitchArgument switchArgBar = parser.parseSwitchArgument("bar");
        SwitchArgument switchArgBaz = parser.parseSwitchArgument("baz");

        assertEquals(0, parser.getArgsLeftCount());
        assertEquals(0, parser.getArgsIndex("foo"));
        assertEquals("foo", switchArgFoo.getKey());
        assertEquals(true, switchArgFoo.isPresent());

        assertEquals(1, parser.getArgsIndex("bar"));
        assertEquals("bar", switchArgBar.getKey());
        assertEquals(true, switchArgBar.isPresent());

        assertEquals(2, parser.getArgsIndex("baz"));
        assertEquals("baz", switchArgBaz.getKey());
        assertEquals(true, switchArgBaz.isPresent());

        assertEquals(-1, parser.parseSwitchArgument("qux").getArgsIndex());
        assertNull(parser.parseSwitchArgument("qux"));
    }

    @Test
    public void testArgsParser_ParsingParameterArguments() {
        String[] args = {"--key=value", "-a", "--key2=value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsCount());

        ParameterArgument paramArgKey = parser.parseParameterArgument("key");
        ParameterArgument paramArgKey2 = parser.parseParameterArgument("key2");

        assertEquals(0, parser.getArgsLeftCount());
        assertEquals(0, parser.getArgsIndex("key"));
        assertEquals("value", paramArgKey.getValue());
        assertEquals("value2", paramArgKey2.getValue());

        assertEquals(-1, parser.parseParameterArgument("qux").getArgsIndex());
        assertNull(parser.parseParameterArgument("qux"));
    }

    @Test
    public void testArgsParser_ParsingStringArguments() {
        String[] args = {"--foo", "-a", "str1", "--key=value1", "-b", "str2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(6, parser.getArgsCount());

        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(4, stringArgs.size());

        StringArgument stringArgFoo = stringArgs.get(0);
        StringArgument stringArgStr1 = stringArgs.get(1);
        StringArgument stringArgValue1 = stringArgs.get(2);
        StringArgument stringArgStr2 = stringArgs.get(3);

        assertEquals(0, parser.getArgsLeftCount());
        assertEquals("foo", stringArgFoo.getKey());
        assertEquals(null, stringArgFoo.getValue());

        assertEquals(1, parser.getArgsIndex("str1"));
        assertEquals("str1", stringArgStr1.getKey());
        assertEquals("str1", stringArgStr1.getValue());

        assertEquals(3, parser.getArgsIndex("key"));
        assertEquals("value1", stringArgValue1.getValue());

        assertEquals(4, parser.getArgsIndex("str2"));
        assertEquals("str2", stringArgStr2.getKey());
        assertEquals("str2", stringArgStr2.getValue());

        assertEquals(-1, parser.parseStringArgument().getArgsIndex());
        assertNull(parser.parseStringArgument());
    }
}