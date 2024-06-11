import org.junit.Test;
import static org.junit.Assert.*;

public class ArgsParserTest {
    @Test
    public void testGetArgsCount() {
        ArgsParser parser = new ArgsParser(new String[]{"arg1", "arg2"});
        int actual = parser.getArgsCount();
        int expected = 2;
        assertEquals(expected, actual);
    }
}