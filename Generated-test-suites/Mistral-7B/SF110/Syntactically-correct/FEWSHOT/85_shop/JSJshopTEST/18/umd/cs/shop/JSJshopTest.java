package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

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
        String file = "testFile.txt";
        BufferedReader reader = jsJshop.getBufferedReader(".", file);
        assertNotNull(reader);
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
        assertEquals("myDomain", jsJshop.dom.getName());
    }

    @Test
    public void testSetFile() {
        JSJshop jsJshop = new JSJshop();
        String nameFile = "testFile.shp";
        JSTaskAtom pred = null;
        JSPairPlanTSListNodes expectedSolution = new JSPairPlanTSListNodes(new JSPairPlanTState(new JSPlan(), new JSTState()), new Vector());
        JSJshopNodes expectedTree = new JSJshopNode(expectedSolution.listNodes().elementAt(expectedSolution.size() - 1), expectedSolution.listNodes());
        JSJshop actualSolution = jsJshop.setFile(nameFile, pred);
        JSJshopNodes actualTree = actualSolution.getTree();
        assertEquals(expectedSolution, actualSolution.getSolution());
        assertEquals(expectedTree, actualTree);
    }

    @Test
    public void testMain() throws IOException {
        String domainFileName = "testDomainFile.shp";
        String problemFileName = "testProblemFile.shp";
        String[] args = new String[]{"testDomainFile.shp", "testProblemFile.shp"};
        JSJshop jsJshop = new JSJshop();
        jsJshop.main(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithInvalidArgs() {
        String[] args = new String[]{"testFile.shp", "testProblemFile.shp", "3"};
        JSJshop jsJshop = new JSJshop();
        jsJshop.main(args);
    }
}