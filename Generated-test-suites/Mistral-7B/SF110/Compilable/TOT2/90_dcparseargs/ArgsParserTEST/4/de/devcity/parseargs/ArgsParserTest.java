package de.devcity.parseargs;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        assertEquals(2, parser.getArgsCount());
    }
}