package umd.cs.shop;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        assertTrue(jsJshop.parserFile("testFile.shp"));
    }

    @Test(expected = JSParserError.class)
    public void testParserFileWithError() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("testFileWithError.shp");
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsJshop = new JSJshop();
        assertNotNull(jsJshop.getBufferedReader("testDir", "testFile.txt"));
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        URL url = JSJshop.getAppletURL("testFile.txt", applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain myDomain"));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
        assertTrue(jsJshop.dom != null);
    }

    @Test
    public void testDom() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("testFile.shp");
        JSPlanningDomain dom = jsJshop.dom();
        assertNotNull(dom);
    }

    @Test
    public void testProb() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("testFile.shp");
        JSPlanningProblem prob = jsJshop.prob();
        assertNotNull(prob);
    }

    @Test
    public void testSol() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("testFile.shp");
        JSPlan sol = jsJshop.sol();
        assertNotNull(sol);
    }

    @Test
    public void testTree() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("testFile.shp");
        JSJshopNode tree = jsJshop.tree();
        assertNotNull(tree);
    }
}