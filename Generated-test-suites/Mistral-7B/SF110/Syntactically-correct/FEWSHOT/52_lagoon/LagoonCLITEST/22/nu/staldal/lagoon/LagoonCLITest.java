package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.xtree.Element;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonProcessor;

public class LagoonCLITest {

    @Test
    public void testInitializationWithPropertyFile() {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
    }

    @Test(expected = LagoonException.class)
    public void testInitializationWithInvalidPropertyFile() {
        String[] args = new String[]{"test.non-existent.properties"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInitializationWithSitemapFile() {
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
        assertEquals(System.getProperty("user.dir"), LagoonCLI.properties.getProperty("targetURL"));
    }

    @Test
    public void testBuild() {
        String[] args = new String[]{"test.xml", "build"};
        LagoonCLI.main(args);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenReturn(true);
        LagoonCLI.main(new String[]{"test.xml", "build"});
        verify(processor).build(false);
    }

    @Test(expected = LagoonException.class)
    public void testBuildWithError() {
        String[] args = new String[]{"test.xml", "build"};
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenThrow(new LagoonException("Build error"));
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "test.url");
        LagoonCLI.main(args);
    }

    @Test
    public void testForceBuild() {
        String[] args = new String[]{"test.xml", "force"};
        LagoonCLI.main(args);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(true)).thenReturn(true);
        LagoonCLI.main(args);
        verify(processor).build(true);
    }

    @Test(expected = LagoonException.class)
    public void testForceBuildWithError() {
        String[] args = new String[]{"test.xml", "force"};
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(true)).thenThrow(new LagoonException("Force build error"));
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "test.url");
        LagoonCLI.main(args);
    }

    @Test
    public void testInterval() {
        String[] args = new String[]{"test.xml", "10000"};
        LagoonCLI.main(args);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenReturn(true);
        LagoonCLI.main(args);
        verify(processor).build(false);
        long interval = 10000L;
        while (true) {
            LagoonCLI.main(new String[]{"test.xml"});
            Thread.yield();
            if (System.currentTimeMillis() - interval > 10100L) {
                break;
            }
        }
    }

    @Test
    public void testInvalidInterval() {
        String[] args = new String[]{"test.xml", "-1000"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInput() {
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
        BufferedReader in = mock(BufferedReader.class);
        when(in.readLine()).thenReturn("b");
        when(in.readLine()).thenReturn("f");
        when(in.readLine()).thenReturn("q");
        System.setIn(in);
        String[] args2 = new String[]{"test.xml"};
        LagoonCLI.main(args2);
        verify(in).readLine();
        verify(in).readLine();
        verify(in).readLine();
    }

    @Test
    public void testInvalidInput() {
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
        BufferedReader in = mock(BufferedReader.class);
        when(in.readLine()).thenReturn("invalid");
        System.setIn(in);
        String[] args2 = new String[]{"test.xml"};
        LagoonCLI.main(args2);
    }

    @Test
    public void testSaxParse() throws SAXException {
        String xml = "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">" +
                "<url>" +
                "<loc>http://test.com/</loc>" +
                "</url>" +
                "</urlset>";
        Element sitemapTree = TreeBuilder.parseXML(new InputSource(new StringReader(xml)), false);
    }
}