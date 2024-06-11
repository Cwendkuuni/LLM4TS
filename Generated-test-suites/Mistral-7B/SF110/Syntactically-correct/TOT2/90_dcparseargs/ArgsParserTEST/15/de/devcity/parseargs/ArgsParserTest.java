package de.devcity.parseargs.test;

import de.devcity.parseargs.arguments.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCount() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        assertEquals(0, parser.getArgsCount());

        args = new String[]{"arg1", "arg2"};
        parser = new ArgsParser(args);
        assertEquals(2, parser.getArgsCount());
    }

    @Test
    public void testParseSwitchArgumentShortKey() {
        String[] args = {"-a"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(new SwitchArgument(0, "a", true), parser.parseSwitchArgument("a"));
    }

    @Test
    public void testParseSwitchArgumentLongKey() {
        String[] args = {"--long-key"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(new SwitchArgument(0, "long-key", true), parser.parseSwitchArgument("long-key"));
    }

    @Test
    public void testParseSwitchArgumentNull() {
        String[] args = {"arg1", "--key=value", "--", "--long-key"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(new SwitchArgument(-1, "key", false), parser.parseSwitchArgument("key"));
    }

    @Test
    public void testGetArgsLeftCount() {
        String[] args = {"arg1", null, "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(2, parser.getArgsLeftCount());

        String[] args2 = {"arg1", "arg2", "arg3", null};
        ArgsParser parser2 = new ArgsParser(args2);
        assertEquals(2, parser2.getArgsLeftCount());
    }

    @Test
    public void testParseParameterArgumentShortKey() {
        String[] args = {"-a=value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(new ParameterArgument(0, "a", "value"), parser.parseParameterArgument("a"));
    }

    @Test
    public void testParseParameterArgumentLongKey() {
        String[] args = {"--long-key=value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(new ParameterArgument(0, "long-key", "value"), parser.parseParameterArgument("long-key"));
    }

    @Test
    public void testParseParameterArgumentNull() {
        String[] args = {"--key=value", "--", "--long-key=another-value"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(new ParameterArgument(-1, "key", "value"), parser.parseParameterArgument("key"));
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"arg1", "--key=value", "--", "arg3", "--long-key=another-value"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        assertEquals("value", stringArguments.get(0).getValue());
        assertEquals("another-value", stringArguments.get(1).getValue());
    }
}