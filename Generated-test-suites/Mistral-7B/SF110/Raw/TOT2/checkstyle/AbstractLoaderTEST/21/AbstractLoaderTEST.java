import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.xml.sax.InputSource;
import org.junit.Test;

public class AbstractLoaderTest {
    @Test
    public void testParseInputSource() throws Exception {
        AbstractLoader loader = new AbstractLoader(new HashMap<>());
        InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>"));
        loader.parseInputSource(inputSource);
        assertNotNull(loader.parser);
        assertFalse(loader.parser.getFeature("http://xml.org/sax/features/namespaces"));
    }
}