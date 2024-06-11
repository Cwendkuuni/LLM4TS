package de.devcity.parseargs;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"-a", "--long-a"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("a");
        assertEquals("a", switchArgument.getKey());
        assertTrue(switchArgument.isLong());
        assertEquals(0, parser.getArgsLeftCount());

        assertNull(parser.parseSwitchArgument("b"));
        assertEquals(-1, parser.parseSwitchArgument("a").getIndex());
    }

    @Test
    public void testParseSwitchArgumentLongFormFirst() {
        String[] args = {"--long-a", "-a"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("a");
        assertEquals("a", switchArgument.getKey());
        assertTrue(switchArgument.isLong());
        assertEquals(0, parser.getArgsLeftCount());

        assertNull(parser.parseSwitchArgument("b"));
        assertEquals(0, parser.parseSwitchArgument("a").getIndex());
    }

    @Test
    public void testParseSwitchArgumentMiddle() {
        String[] args = {"a", "--long-b", "-c"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("b");
        assertEquals("b", switchArgument.getKey());
        assertTrue(switchArgument.isLong());
        assertEquals(1, parser.getArgsLeftCount());

        assertNull(parser.parseSwitchArgument("a"));
        assertEquals(1, parser.parseSwitchArgument("c").getIndex());
    }

    @Test
    public void testParseSwitchArgumentNoneFound() {
        String[] args = {"a", "b"};
        ArgsParser parser = new ArgsParser(args);
        SwitchArgument switchArgument = parser.parseSwitchArgument("c");
        assertNull(switchArgument);
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"--key=value", "-k=value2"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key");
        assertEquals("value", parameterArgument.getValue());
        assertEquals(0, parser.getArgsLeftCount());

        ParameterArgument parameterArgument2 = parser.parseParameterArgument("k");
        assertEquals("value2", parameterArgument2.getValue());
        assertEquals(0, parser.getArgsLeftCount());

        assertNull(parser.parseParameterArgument("unknown"));
        assertEquals(-1, parser.parseParameterArgument("key").getIndex());
    }

    @Test
    public void testParseParameterArgumentLongFormFirst() {
        String[] args = {"--key=value", "--long-key=value2", "-k=value3"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key");
        assertEquals("value", parameterArgument.getValue());
        assertEquals(0, parser.getArgsLeftCount());

        ParameterArgument parameterArgument2 = parser.parseParameterArgument("long-key");
        assertEquals("value2", parameterArgument2.getValue());
        assertEquals(0, parser.getArgsLeftCount());

        ParameterArgument parameterArgument3 = parser.parseParameterArgument("k");
        assertEquals("value3", parameterArgument3.getValue());
        assertEquals(0, parser.getArgsLeftCount());

        assertNull(parser.parseParameterArgument("unknown"));
        assertEquals(-1, parser.parseParameterArgument("key").getIndex());
    }

    @Test
    public void testParseParameterArgumentMiddle() {
        String[] args = {"a", "--key=value", "-b"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("key");
        assertEquals("value", parameterArgument.getValue());
        assertEquals(1, parser.getArgsLeftCount());

        assertNull(parser.parseParameterArgument("a"));
        assertEquals(1, parser.parseParameterArgument("b").getIndex());
    }

    @Test
    public void testParseParameterArgumentNoneFound() {
        String[] args = {"a", "b"};
        ArgsParser parser = new ArgsParser(args);
        ParameterArgument parameterArgument = parser.parseParameterArgument("c");
        assertNull(parameterArgument);
        assertEquals(2, parser.getArgsLeftCount());
    }

    @Test
    public void testParseStringArguments() {
        String[] args = {"--arg1=value1", "--arg2=value2", "--arg3=value3"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        List<StringArgument> expected = new ArrayList<>(Arrays.asList(
                new StringArgument(0, "value1"),
                new StringArgument(1, "value2"),
                new StringArgument(2, "value3")
        ));
        assertEquals(expected, stringArguments);
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testEmptyArgsParser() {
        ArgsParser parser = new ArgsParser();
        assertEquals(0, parser.getArgsCount());
        assertEquals(0, parser.getArgsLeftCount());
    }
}