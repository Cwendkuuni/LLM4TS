
package de.devcity.parseargs.test;

import de.devcity.parseargs.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        assertEquals(3, parser.getArgsCount());
    }

    @Test
    public void testParseSwitchArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--switchKey", "-otherArg", "--switchKey2"});
        assertEquals(new SwitchArgument(1, "switchKey", true), parser.parseSwitchArgument("switchKey"));
        assertEquals(new SwitchArgument(-1, "switchKey2", false), parser.parseSwitchArgument("switchKey2"));
    }

    @Test
    public void testParseParameterArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--paramKey=value", "-otherArg", "--paramKey2=value2"});
        assertEquals(new ParameterArgument(0, "paramKey", "value"), parser.parseParameterArgument("paramKey"));
        assertNull(parser.parseParameterArgument("paramKey3"));
        assertEquals(new ParameterArgument(2, "paramKey2", "value2"), parser.parseParameterArgument("paramKey2"));
    }

    @Test
    public void testParseStringArgument() {
        ArgsParser parser = new ArgsParser(new String[]{"--stringArg=value1", "--stringArg2=value2"});
        List<StringArgument> stringArgs = parser.parseStringArgument();
        assertEquals(2, stringArgs.size());
        assertEquals(new StringArgument(0, "stringArg"), stringArgs.get(0));
        assertEquals(new StringArgument(1, "stringArg2"), stringArgs.get(1));
    }

    @Test
    public void testNullArgsArray() {
        try {
            new ArgsParser(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}
