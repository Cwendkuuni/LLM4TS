import static org.junit.Assert.*;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int result = parser.getArgsCount();
        assertEquals(2, result);
    }
}