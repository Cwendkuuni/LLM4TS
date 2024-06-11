package de.devcity.parseargs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testArgsParser_emptyArray() {
        ArgsParser parser = new ArgsParser();
        assertEquals(0, parser.getArgsCount());
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testArgsParser_nullArray() {
        try {
            new ArgsParser(null);
            fail("Should not reach this point.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testArgsParser_singleArgument() {
        String[] args = {"arg"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(1, parser.getArgsCount());
        assertEquals("arg", parser.args[0]);
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testArgsParser_singleSwitchArgument() {
        String[] args = {"--test"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(1, parser.getArgsCount());
        assertEquals(0, parser.getArgsLeftCount());
        assertEquals("test", parser.parseSwitchArgument("test").getKey());
    }

    @Test
    public void testArgsParser_singleParameterArgument() {
        String[] args = {"-p", "value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(2, parser.getArgsCount());
        assertEquals(1, parser.getArgsLeftCount());
        assertEquals("p", parser.parseSwitchArgument("p").getKey());
        assertEquals("value", parser.parseParameterArgument("p").getValue());
    }

    @Test
    public void testArgsParser_multipleArguments() {
        String[] args = {"arg1", "arg2", "--test", "value", "--longtest", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(6, parser.getArgsCount());
        assertEquals(3, parser.getArgsLeftCount());
        assertEquals("test", parser.parseSwitchArgument("test").getKey());
        assertEquals("value", parser.parseParameterArgument("test").getValue());
        assertEquals("longtest", parser.parseSwitchArgument("longtest").getKey());
        assertEquals("value2", parser.parseParameterArgument("longtest").getValue());
    }

    @Test
    public void testArgsParser_multipleParameterArguments() {
        String[] args = {"arg1", "--param1", "value1", "--param2", "value2"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(5, parser.getArgsCount());
        assertEquals(2, parser.getArgsLeftCount());
        assertEquals("param1", parser.parseSwitchArgument("param1").getKey());
        assertEquals("value1", parser.parseParameterArgument("param1").getValue());
        assertEquals("param2", parser.parseSwitchArgument("param2").getKey());
        assertEquals("value2", parser.parseParameterArgument("param2").getValue());
    }

    @Test
    public void testArgsParser_parseStringArguments() {
        String[] args = {"arg1", "--param1", "value1", "--param2", "value2", "--param3", "value3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(7, parser.getArgsCount());
        assertEquals(0, parser.getArgsLeftCount());
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(3, stringArguments.size());
        assertEquals("value1", stringArguments.get(0).getValue());
        assertEquals("value2", stringArguments.get(1).getValue());
        assertEquals("value3", stringArguments.get(2).getValue());
    }

}