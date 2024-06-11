package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

import umd.cs.shop.JSJshop;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args = new String[]{"domainDef-file-name", "problemDef-file-name"};
        JSJshop.main(args);
    }

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        assertTrue(jsJshop.parserFile("path/to/valid/file.shp"));
    }

    @Test(expected = JSParserError.class)
    public void testParserFileWithError() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("path/to/invalid/file.shp");
    }

    @Test
    public void testSetFile() {
        JSJshop jsJshop = new JSJshop();
        JSTaskAtom pred = new JSTaskAtom("test");
        JSJshopNode tree = jsJshop.setFile("path/to/valid/file.shp", pred).getTree();
        assertNotNull(tree);
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsJshop = new JSJshop();
        String dir = "path/to/directory/";
        String file = "valid/file.shp";
        BufferedReader br = jsJshop.getBufferedReader(dir, file);
        assertNotNull(br);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        String file = "valid/file.shp";
        URL url = JSJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        JSJshop jsJshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
        jsJshop.processToken(tokenizer);
        fail("Expected JSParserError to be thrown");
    }

    @Test
    public void testDom() {
        JSJshop jsJshop = new JSJshop();
        JSPlanningDomain dom = jsJshop.dom();
        assertNotNull(dom);
    }

    @Test
    public void testProb() {
        JSJshop jsJshop = new JSJshop();
        JSPlanningProblem prob = jsJshop.prob();
        assertNotNull(prob);
    }

    @Test
    public void testSol() {
        JSJshop jsJshop = new JSJshop();
        JSPlan sol = jsJshop.sol();
        assertNull(sol);
    }

    @Test
    public void testTree() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.tree();
        assertNull(tree);
    }
}