package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblem.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        JSJshopNode tree = jsJshop.getTree();
        JSPairPlanTSListNodes solution = jsJshop.getSolution();

        // Test for null tree and solution
        assertNull(jsJshop.getTree());
        assertNull(jsJshop.getSolution());

        // Test for non-null tree and solution
        assertNotNull(tree);
        assertNotNull(solution);

        // Test the tree and solution with print methods
        tree.print();
        solution.planS().plan().printPlan();
    }

    @Test
    public void testJSJshopSetFile() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblem.shp";
        String nameFile = "testFile.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.setFile(nameFile, pred).getTree();

        // Test for null tree
        assertNull(jsJshop.getTree());

        // Test for non-null tree
        assertNotNull(tree);
        tree.print();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopSetFileInvalidFile() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblem.shp";
        String nameFile = "invalidFile.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        JSJshop jsJshop = new JSJshop();
        jsJshop.setFile(nameFile, pred);
    }

    @Test
    public void testParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test
    public void testBufferedReader() {
        String dir = "src/umd/cs/shop/";
        String file = "JSJshop.java";
        BufferedReader br = new JSJshop().getBufferedReader(dir, file);

        // Test for null buffered reader
        assertNull(new JSJshop().getBufferedReader(null, file));

        // Test for non-null buffered reader
        assertNotNull(br);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        URL url = new JSJshop().getAppletURL(null, applet);

        // Test for null url
        assertNull(new JSJshop().getAppletURL(null, null));

        // Test for non-null url
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        new JSJshop().processToken(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testProcessTokenParserError() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        new JSJshop().processToken(tokenizer);
    }
}