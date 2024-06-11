package de.devcity.parseargs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

    @Test
    public void testConstructor() {
        ArgsParser parser = new ArgsParser();
        assertNull(parser.getArgs());
        assertEquals(0, parser.getArgsCount());
    }

    @Test
    public void testSetArgs() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3"};
        parser.setArgs(args);
        assertNotNull(parser.getArgs());
        assertEquals(3, parser.getArgsCount());
        assertEquals("arg1", parser.getArgs()[0]);
        assertEquals("arg2", parser.getArgs()[1]);
        assertEquals("arg3", parser.getArgs()[2]);
    }

    @Test
    public void testGetArgsLeftCount_noArgsLeft() {
        ArgsParser parser = new ArgsParser();
        assertEquals(0, parser.getArgsLeftCount());
    }

    @Test
    public void testGetArgsLeftCount_argsLeft() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1", "arg2", "arg3", "arg4"};
        parser.setArgs(args);
        assertEquals(1, parser.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument_short_key_present() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("test");
        assertTrue(result.isSet());
        assertEquals(0, result.getIndex());
    }

    @Test
    public void testParseSwitchArgument_short_key_absent() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("test");
        assertFalse(result.isSet());
        assertEquals(-1, result.getIndex());
    }

    @Test
    public void testParseSwitchArgument_long_key_present() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test-flag"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("test-flag");
        assertTrue(result.isSet());
        assertEquals(0, result.getIndex());
    }

    @Test
    public void testParseSwitchArgument_long_key_absent() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1"};
        parser.setArgs(args);
        SwitchArgument result = parser.parseSwitchArgument("test-flag");
        assertFalse(result.isSet());
        assertEquals(-1, result.getIndex());
    }

    @Test
    public void testParseParameterArgument_short_key_present() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test=value"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("test");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgument_short_key_absent() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("test");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_long_key_present() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test-flag=value"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("test-flag");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgument_long_key_absent() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test-flag"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("test-flag");
        assertNull(result);
    }

    @Test
    public void testParseParameterArgument_short_key_with_value_next() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test=value", "arg"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("test");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgument_short_key_with_value_previous() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg", "--test=value"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("test");
        assertNotNull(result);
        assertEquals(1, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgument_long_key_with_value_next() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--test-flag=value", "arg"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("test-flag");
        assertNotNull(result);
        assertEquals(0, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseParameterArgument_long_key_with_value_previous() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg", "--test-flag=value"};
        parser.setArgs(args);
        ParameterArgument result = parser.parseParameterArgument("test-flag");
        assertNotNull(result);
        assertEquals(1, result.getIndex());
        assertEquals("value", result.getValue());
    }

    @Test
    public void testParseStringArguments() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"--arg1=value1", "--arg2=value2"};
        parser.setArgs(args);
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(2, result.size());
        StringArgument stringArgument1 = result.get(0);
        assertEquals(0, stringArgument1.getIndex());
        assertEquals("value1", stringArgument1.getValue());
        StringArgument stringArgument2 = result.get(1);
        assertEquals(1, stringArgument2.getIndex());
        assertEquals("value2", stringArgument2.getValue());
    }

    @Test
    public void testParseStringArguments_noArguments() {
        ArgsParser parser = new ArgsParser();
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(0, result.size());
    }

    @Test
    public void testParseStringArguments_singleArgument() {
        ArgsParser parser = new ArgsParser();
        String[] args = {"arg1"};
        parser.setArgs(args);
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(1, result.size());
        StringArgument stringArgument = result.get(0);
        assertEquals(0, stringArgument.getIndex());
        assertNull(stringArgument.getValue());
    }
}