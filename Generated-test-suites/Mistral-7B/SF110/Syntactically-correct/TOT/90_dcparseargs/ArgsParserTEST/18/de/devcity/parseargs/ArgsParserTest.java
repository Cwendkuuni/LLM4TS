package de.devcity.parseargs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        int actual = parser.getArgsCount();
        int expected = 0;
        assertEquals(expected, actual);

        args = new String[] {"arg1", "arg2", "arg3"};
        parser = new ArgsParser(args);
        actual = parser.getArgsCount();
        expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"--switchKey"});

        SwitchArgument expected = new SwitchArgument(0, "switchKey", true);
        SwitchArgument actual = parser.parseSwitchArgument("switchKey");

        assertEquals(expected, actual);

        parser = new ArgsParser();
        parser.setArgs(new String[] {"--no-switchKey"});
        actual = parser.parseSwitchArgument("switchKey");
        assertNull(actual);

        parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", "--switchKey", "arg3"});
        actual = parser.parseSwitchArgument("switchKey");
        assertEquals(expected, actual);

        parser = new ArgsParser();
        parser.setArgs(new String[] {"--switchKey1", "--switchKey2"});
        actual = parser.parseSwitchArgument("switchKey3");
        assertNull(actual);
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"--keyValue"});

        ParameterArgument expected = new ParameterArgument(0, "key", "Value");
        ParameterArgument actual = parser.parseParameterArgument("key");

        assertEquals(expected, actual);

        parser = new ArgsParser();
        parser.setArgs(new String[] {"--keyValue1", "arg1", "--keyValue2=value2", "arg3"});

        expected = new ParameterArgument(1, "key", "value2");
        actual = parser.parseParameterArgument("key");
        assertEquals(expected, actual);

        parser = new ArgsParser();
        parser.setArgs(new String[] {"--keyValue1", "arg1", "--keyValue2"});
        actual = parser.parseParameterArgument("key");
        assertNull(actual);

        parser = new ArgsParser();
        parser.setArgs(new String[] {"--keyValue1=value1", "--keyValue2=value2"});
        actual = parser.parseParameterArgument("key");
        assertNull(actual);
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", "--keyValue=value1", "arg3"});

        List<StringArgument> expected = new ArrayList<>();
        expected.add(new StringArgument(1, "--keyValue=value1"));
        List<StringArgument> actual = parser.parseStringArgument();

        assertEquals(expected, actual);

        parser = new ArgsParser();
        parser.setArgs(new String[] {"arg1", "--keyValue1=value1", "--keyValue2=value2", "arg3"});

        expected = new ArrayList<>();
        expected.add(new StringArgument(1, "--keyValue1=value1"));
        expected.add(new StringArgument(3, "--keyValue2=value2"));
        actual = parser.parseStringArgument();
        assertEquals(expected, actual);
    }
}