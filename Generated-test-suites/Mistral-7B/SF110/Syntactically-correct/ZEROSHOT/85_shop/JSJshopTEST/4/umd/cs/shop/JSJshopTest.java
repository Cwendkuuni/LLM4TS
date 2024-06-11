package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;

public class JSJshopTest {

    @Test
    public void testMain() {
        String domainFileName = "domainFile.shp";
        String problemFileName = "problemFile.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithInvalidArguments() {
        String[] args = {"", "", "invalid"};
        JSJshop jsJshop = new JSJshop(args);
    }

    @Test
    public void testParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        assertTrue(jsJshop.parserFile("testFile.shp"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetBufferedReaderWithInvalidFile() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.getBufferedReader("invalidFile.shp", null);
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsJshop = new JSJshop();
        BufferedReader bufferedReader = jsJshop.getBufferedReader("testFile.shp");
        assertNotNull(bufferedReader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        URL url = JSJshop.getAppletURL("testFile.shp", applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain myDomain"));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
        JSPlanningDomain domain = jsJshop.dom();
        assertEquals("myDomain", domain.getName());
    }

    @Test(expected = JSParserError.class)
    public void testProcessTokenWithInvalidToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defproblem invalid"));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
    }
}