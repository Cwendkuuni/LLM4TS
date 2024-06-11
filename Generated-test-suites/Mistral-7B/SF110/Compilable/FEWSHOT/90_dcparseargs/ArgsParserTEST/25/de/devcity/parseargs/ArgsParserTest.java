package de.devcity.parseargs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.devcity.parseargs.arguments.*;
import java.util.*;

public class ArgsParserTest {
    @Test
    public void testArgsParserInstanceCreation() {
        ArgsParser ap = new ArgsParser();
        ArgsParser apWithArgs = new ArgsParser(new String[]{"test1", "test2"});
    }

    @Test(expected = NullPointerException.class)
    public void testArgsParserInstanceCreationWithNullArray() {
        new ArgsParser(null);
    }

    @Test
    public void testArgsParserGetters() {
        String[] args = {"arg1", "arg2", "--key", "-k value", "--longKey", "arg5", "--", "arg7"};
        ArgsParser ap = new ArgsParser(args);
        assertEquals(7, ap.getArgsCount());
        assertEquals(args, ap.getArgs());
        assertEquals(5, ap.getArgsLeftCount());
    }

    @Test
    public void testParseSwitchArgument() {
        String[] args = {"--key", "--switchKey", "-k", "arg"};
        ArgsParser ap = new ArgsParser(args);
        SwitchArgument sa1 = ap.parseSwitchArgument("key");
        SwitchArgument sa2 = ap.parseSwitchArgument("switchKey");
        SwitchArgument sa3 = ap.parseSwitchArgument("k");
        SwitchArgument sa4 = ap.parseSwitchArgument("arg");
        SwitchArgument sa5 = ap.parseSwitchArgument("notExistingKey");
        
        assertTrue(sa1 != null);
        assertTrue(sa1.isLongKey());
        assertTrue(sa1.getKey().equals("key"));
        assertTrue(sa1.isSet());
        
        assertTrue(sa2 != null);
        assertTrue(sa2.isLongKey());
        assertTrue(sa2.getKey().equals("switchKey"));
        assertTrue(sa2.isSet());
        
        assertTrue(sa3 != null);
        assertFalse(sa3.isLongKey());
        assertTrue(sa3.getKey().equals("k"));
        assertTrue(sa3.isSet());
        
        assertTrue(sa4 == null);
        
        assertFalse(sa5 != null);
    }

    @Test
    public void testParseParameterArgument() {
        String[] args = {"--key=value", "--longKey=value2", "--param", "-p value3", "param4=value5", "--", "arg7"};
        ArgsParser ap = new ArgsParser(args);
        ParameterArgument pa1 = ap.parseParameterArgument("key");
        ParameterArgument pa2 = ap.parseParameterArgument("longKey");
        ParameterArgument pa3 = ap.parseParameterArgument("param");
        ParameterArgument pa4 = ap.parseParameterArgument("p");
        ParameterArgument pa5 = ap.parseParameterArgument("notExistingKey");
        ParameterArgument pa6 = ap.parseParameterArgument("value3");
        
        assertTrue(pa1 != null);
        assertTrue(pa1.isLongKey());
        assertTrue(pa1.getKey().equals("key"));
        assertTrue(pa1.getValue().equals("value"));
        
        assertTrue(pa2 != null);
        assertFalse(pa2.isLongKey());
        assertTrue(pa2.getKey().equals("p"));
        assertTrue(pa2.getValue().equals("value3"));
        
        assertFalse(pa3 != null);
        
        assertNull(pa4);
        
        assertTrue(pa5 == null);
        
        assertTrue(pa6 != null);
        assertTrue(pa6.getValue().equals("value3"));
    }

    @Test
    public void testParseStringArgument() {
        String[] args = {"--key=value1", "--longKey=value2", "arg1", "arg2", "arg3", "arg4", "--"};
        ArgsParser ap = new ArgsParser(args);
        List<StringArgument> stringArguments = ap.parseStringArgument();
        
        assertEquals(4, stringArguments.size());
        StringArgument sa1 = stringArguments.get(0);
        StringArgument sa2 = stringArguments.get(1);
        StringArgument sa3 = stringArguments.get(2);
        StringArgument sa4 = stringArguments.get(3);
        
        assertTrue(sa1.getValue().equals("arg1"));
        assertTrue(sa2.getValue().equals("arg2"));
        assertTrue(sa3.getValue().equals("arg3"));
        assertTrue(sa4.getValue().equals("arg4"));
    }
}