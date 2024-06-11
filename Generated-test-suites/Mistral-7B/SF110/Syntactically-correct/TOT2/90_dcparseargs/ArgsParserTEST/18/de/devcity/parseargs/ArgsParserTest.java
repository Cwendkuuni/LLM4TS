import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

    @Test
    public void testGetArgsCountWithEmptyArgs() {
        ArgsParser parser = new ArgsParser(new String[]{});
        int count = parser.getArgsCount();
        assertEquals(0, count);
    }

    @Test
    public void testGetArgsCountWithNullArgs() {
        assertThrows(NullPointerException.class, () -> new ArgsParser(null));
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = new String[]{"-s", "arg1", "--switch", "arg2", "--switch", "arg3"};
        ArgsParser parser = new ArgsParser(args);

        SwitchArgument switchArgument1 = parser.parseSwitchArgument("-s");
        assertEquals(0, switchArgument1.getIndex());
        assertEquals("-s", switchArgument1.getKey());
        assertTrue(switchArgument1.isSwitch());

        SwitchArgument switchArgument2 = parser.parseSwitchArgument("--switch");
        assertEquals(3, switchArgument2.getIndex());
        assertEquals("switch", switchArgument2.getKey());
        assertTrue(switchArgument2.isSwitch());

        SwitchArgument switchArgument3 = parser.parseSwitchArgument("--longSwitch");
        assertEquals(-1, switchArgument3.getIndex());
        assertEquals("longSwitch", switchArgument3.getKey());
        assertFalse(switchArgument3.isSwitch());
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = new String[]{"-p", "value1", "-p", "value2", "--parameter", "value3"};
        ArgsParser parser = new ArgsParser(args);

        ParameterArgument parameterArgument1 = parser.parseParameterArgument("-p");
        assertEquals(0, parameterArgument1.getIndex());
        assertEquals("value1", parameterArgument1.getValue());

        ParameterArgument parameterArgument2 = parser.parseParameterArgument("-p");
        assertEquals(1, parameterArgument2.getIndex());
        assertEquals("value2", parameterArgument2.getValue());

        ParameterArgument parameterArgument3 = parser.parseParameterArgument("--parameter");
        assertEquals(3, parameterArgument3.getIndex());
        assertEquals("value3", parameterArgument3.getValue());
    }

    @Test
    public void testParseStringArgument() {
        String[] args = new String[]{"-s", "value1", "--string", "value2", "arg3", "--string", "value3"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();

        assertEquals(2, stringArguments.size());
        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals(1, stringArgument1.getIndex());
        assertEquals("value1", stringArgument1.getValue());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals(5, stringArgument2.getIndex());
        assertEquals("value3", stringArgument2.getValue());
    }

    @Test
    public void testParseStringArgumentWithEmptyArgs() {
        ArgsParser parser = new ArgsParser(new String[]{});
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(0, stringArguments.size());
    }

    @Test
    public void testParseStringArgumentWithNullArgs() {
        assertThrows(NullPointerException.class, () -> new ArgsParser(null));
    }

    @Test
    public void testParseStringArgumentWithArgsContainingNullStrings() {
        String[] args = new String[]{"arg1", null, "arg3"};
        ArgsParser parser = new ArgsParser(args);
        List<StringArgument> stringArguments = parser.parseStringArgument();
        assertEquals(2, stringArguments.size());
        StringArgument stringArgument1 = stringArguments.get(0);
        assertEquals(0, stringArgument1.getIndex());
        assertNotNull(stringArgument1.getValue());

        StringArgument stringArgument2 = stringArguments.get(1);
        assertEquals(1, stringArgument2.getIndex());
        assertNull(stringArgument2.getValue());
    }
}