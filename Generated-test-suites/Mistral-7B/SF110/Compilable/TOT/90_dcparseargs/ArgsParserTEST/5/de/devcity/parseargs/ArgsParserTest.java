package de.devcity.parseargs;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        String[] args = new String[]{"arg1", "arg2", "arg3"};
        ArgsParser parser = new ArgsParser(args);
        assertEquals(3, parser.getArgsCount());
    }
}