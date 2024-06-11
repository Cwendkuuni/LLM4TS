package de.devcity.parseargs;
import static org.junit.Assert.*;
import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;
import java.util.Arrays;
import java.util.List;

public class ArgsParserTest {

    @org.junit.Test
    public void testGetArgsCount() {
        String[] args = new String[0];
        ArgsParser parser = new ArgsParser(args);
        int expected = 0;
        int result = parser.getArgsCount();
        assertEquals(expected, result);

        args = new String[]{"arg1", "arg2", "arg3"};
        parser = new ArgsParser(args);
        expected = 3;
        result = parser.getArgsCount();
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testParseSwitchArgument() {
        String[] args = new String[]{"-a", "-b", "-c", "--a"};
        ArgsParser parser = new ArgsParser(args);

        SwitchArgument expected = new SwitchArgument(2, "a", true);
        SwitchArgument result = parser.parseSwitchArgument("a");
        assertEquals(expected, result);

        assertNull(parser.parseSwitchArgument("x"));
    }

    @org.junit.Test
    public void testParseStringArgument() {
        String[] args = new String[]{"arg1=value1", "arg2=value2", "-a"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> expected = Arrays.asList(
            new StringArgument(0, "arg1"),
            new StringArgument(1, "arg2")
        );
        List<StringArgument> result = parser.parseStringArgument();
        assertEquals(expected, result);

        args = new String[]{"arg1=value1", "arg2=value2"};
        parser = new ArgsParser(args);
        expected = Arrays.asList(
            new StringArgument(0, "arg1"),
            new StringArgument(1, "arg2")
        );
        result = parser.parseStringArgument();
        assertEquals(expected, result);

        args = new String[]{"arg1=value1"};
        parser = new ArgsParser(args);
        expected = Arrays.asList(new StringArgument(0, "arg1"));
        result = parser.parseStringArgument();
        assertEquals(expected, result);

        args = new String[]{"arg1=value1", "arg2=value2", "arg3=value3"};
        parser = new ArgsParser(args);
        expected = Arrays.asList(
            new StringArgument(0, "arg1"),
            new StringArgument(1, "arg2"),
            new StringArgument(2, "arg3")
        );
        result = parser.parseStringArgument();
        assertEquals(expected, result);

        args = new String[]{"arg1=value1", "arg2=value2"};
        parser = new ArgsParser(args);
        expected = Arrays.asList(new StringArgument(0, "arg1"), new StringArgument(1, "arg2"));
        result = parser.parseStringArgument();
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testParseParameterArgument() {
        String[] args = new String[]{"-a=value1", "-b=value2", "-c", "--a=value3"};
        ArgsParser parser = new ArgsParser(args);

        ParameterArgument expected = new ParameterArgument(1, "a", "value1");
        ParameterArgument result = parser.parseParameterArgument("a");
        assertEquals(expected, result);

        assertNull(parser.parseParameterArgument("x"));

        args = new String[]{"--a=value1"};
        parser = new ArgsParser(args);
        expected = new ParameterArgument(0, "a", "value1");
        result = parser.parseParameterArgument("a");
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testGetArgsLeftCount() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        int expected = 2;
        int result = parser.getArgsLeftCount();
        assertEquals(expected, result);

        args = new String[]{"arg1"};
        parser = new ArgsParser(args);
        expected = 0;
        result = parser.getArgsLeftCount();
        assertEquals(expected, result);
    }
}