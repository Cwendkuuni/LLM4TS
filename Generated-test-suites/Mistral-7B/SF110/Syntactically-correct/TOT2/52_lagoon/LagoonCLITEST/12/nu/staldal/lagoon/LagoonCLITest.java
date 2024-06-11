import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import static org.mockito.Mockito.*;
import org.xml.sax.*;
import static org.mockito.MockitoAnnotations.*;

public class LagoonCLITest {

    private static final String PROPERTY_FILE = "property_file.xml";
    private static final String SITEMAP_FILE = "sitemap_file.xml";
    private static final String TARGET_URL = "http://example.com";
    private static final String SOURCE_DIR = "/tmp/source";

    @Test
    public void testMain() {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testInvalidArgument() {
        String[] args = new String[0];
        LagoonCLI.main(args);
    }

    @Test
    public void testBuildMode() {
        String[] args = new String[]{"property_file.xml", "build"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.build);
    }

    @Test
    public void testForceMode() {
        String[] args = new String[]{"property_file.xml", "force"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.force);
    }

    @Test(expected = java.lang.NumberFormatException.class)
    public void testInvalidInterval() {
        String[] args = new String[]{"property_file.xml", "invalid_interval"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidInterval() {
        String[] args = new String[]{"property_file.xml", "5000"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.interval > 0);
    }

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.properties = properties;

        String result = LagoonCLI.getProperty("targetURL");

        assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testMissingProperty() {
        LagoonCLI.properties = new Properties();

        try {
            LagoonCLI.getProperty("non_existent_property");
        } catch (LagoonException e) {
            assertEquals("Property non_existent_property not specified", e.getMessage());
        }
    }

    @Test
    public void testShowTimeShort() {
        long ms = 123;
        LagoonCLI.showTime(ms);
        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("in " + ms + " ms"));
    }

    @Test
    public void testShowTimeLong() {
        long ms = 4567;
        LagoonCLI.showTime(ms);
        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("in " + ms / 1000L + " s"));
    }

    @Test
    public void testParseXML() throws SAXException, IOException {
        InputSource xmlInputSource = new InputSource(new FileInputStream(SITEMAP_FILE));
        Element sitemapTree = TreeBuilder.parseXML(xmlInputSource, false);
        verify(mock(TreeBuilder.class)).parseXML(xmlInputSource, false);
    }

    @Test(expected = SAXException.class)
    public void testParseXMLException() throws SAXException, IOException {
        InputSource xmlInputSource = new InputSource(new FileInputStream(SITEMAP_FILE));
        TreeBuilder treeBuilderMock = mock(TreeBuilder.class);
        when(treeBuilderMock.parseXML(xmlInputSource, false)).thenThrow(new SAXException());

        Element sitemapTree = TreeBuilder.parseXML(xmlInputSource, false);
    }

    @BeforeClass
    public static void initMocks() {
        initMocks(this);
    }
}