import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {
    @Test
    public void testParseInputSource() throws IOException, SAXException {
        AbstractLoader loader = new AbstractLoader();
        InputSource inputSource = new InputSource(new StringReader(""));
        loader.parseInputSource(inputSource);
        assertNotNull(loader);
    }
}