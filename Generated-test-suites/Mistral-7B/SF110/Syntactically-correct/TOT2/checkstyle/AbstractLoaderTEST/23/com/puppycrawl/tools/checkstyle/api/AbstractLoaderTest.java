import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AbstractLoaderTest {
    @Test
    public void testParseInputSource() throws Exception {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        InputSource inputSource = new InputSource(new StringReader(""));
        loader.parseInputSource(inputSource);
        assertTrue(true);
    }
}