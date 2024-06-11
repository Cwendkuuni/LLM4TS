package de.devcity.parseargs;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, argsParser.getArgsCount());
    }
}