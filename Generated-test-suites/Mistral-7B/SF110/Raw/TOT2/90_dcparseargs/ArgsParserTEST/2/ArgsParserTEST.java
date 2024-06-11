import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
        int expected = 3;
        int actual = argsParser.getArgsCount();
        assertEquals(expected, actual);
    }
}