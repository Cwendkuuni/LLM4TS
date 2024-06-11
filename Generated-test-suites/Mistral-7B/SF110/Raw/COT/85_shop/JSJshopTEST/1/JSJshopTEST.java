package umd.cs.shop; // replace with your package name if different

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JSJshopTest {
    

    @Test
    public void testEmptyConstructor() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.getTree());
        assertNull(jsjshop.getSolution());
    }

    @Test
    public void testDomainAndProblemFileConstructor() {
        JSJshop jsjshop = new JSJshop("domain.pl", "problem.pl");
        assertNotNull(jsjshop.getTree());
        assertNotNull(jsjshop.getSolution());
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidArgumentInMainMethod() {
        String[] args = {"invalidArg"};
        JSJshop.main(args);
    }

    @Test
    public void testParser() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
    }

    @Test
    public void testParseFileSuccessfully() throws IOException {
        JSJshop jsjshop = new JSJshop("test.shp");
        assertNotNull(jsjshop.getTree());
        assertNotNull(jsjshop.getSolution());
    }

    @Test(expected = JSParserError.class)
    public void testParseFileFailed() throws IOException {
        JSJshop jsjshop = new JSJshop("test.shp.error");
        assertNull(jsjshop.getTree());
        assertNull(jsjshop.getSolution());
    }

    @Test
    public void testGetBufferedReader() throws IOException {
        JSJshop jsjshop = new JSJshop();
        BufferedReader reader = jsjshop.getBufferedReader("test.txt");
        assertNotNull(reader);
        reader.close();
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetBufferedReaderWithNullFile() throws IOException {
        JSJshop jsjshop = new JSJshop();
        BufferedReader reader = jsjshop.getBufferedReader(null, "test.txt");
        assertNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JFrame frame = new JFrame();
        JSJshop jsjshop = new JSJshop();
        URL url = jsjshop.getAppletURL("test.html", frame);
        assertNotNull(url);
    }

    @Test(expected = MalformedURLException.class)
    public void testGetAppletURLWithNullFile() {
        JFrame frame = new JFrame();
        JSJshop jsjshop = new JSJshop();
        URL url = jsjshop.getAppletURL(null, frame);
        assertNull(url);
    }

    @Test
    public void testProcessToken_defdomain() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(defdomain myDomain)\n"));
        JSJshop jsjshop = new JSJshop();
        jsjshop.processToken(tokenizer);
        JSPlanningDomain domain = jsjshop.dom();
        assertNotNull(domain);
    }

    @Test
    public void testProcessToken_defproblem() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(defproblem myProblem)\n"));
        JSJshop jsjshop = new JSJshop();
        jsjshop.processToken(tokenizer);
        JSPlanningProblem problem = jsjshop.prob();
        assertNotNull(problem);
    }

    @Test
    public void testProcessToken_unknownCommand() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(foo)\n"));
        JSJshop jsjshop = new JSJshop();
        try {
            jsjshop.processToken(tokenizer);
            Assert.fail();
        } catch (JSParserError e) {
            // expected
        }
    }

    @Test
    public void testSolveAll() {
        JSJshop jsjshop = new JSJshop("test.shp");
        Vector<JSPlanningProblem> problemList = jsjshop.probSet;
        JSListPairPlanTStateNodes solutions = jsjshop.dom.solveAll(problemList.elementAt(0), false);
        assertTrue(solutions.size() > 0);
        JSPlan plan = solutions.elementAt(0).planS().plan();
        assertFalse(plan.isFailure());
    }

    @Test
    public void testSolveAll_noSolution() {
        JSJshop jsjshop = new JSJshop("test.shp");
        Vector<JSPlanningProblem> problemList = jsjshop.probSet;
        JSListPairPlanTStateNodes solutions = jsjshop.dom.solveAll(problemList.elementAt(0), false);
        assertTrue(solutions.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveAll_invalidFlag() {
        JSJshop jsjshop = new JSJshop("test.shp");
        Vector<JSPlanningProblem> problemList = jsjshop.probSet;
        jsjshop.dom.solveAll(problemList.elementAt(0), -1);
    }

    @Test
    public void testGetAddList() {
        JSJshop jsjshop = new JSJshop("test.shp");
        JSListLogicalAtoms addList = jsjshop.getAddList();
        assertNotNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsjshop = new JSJshop("test.shp");
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        assertNotNull(deleteList);
    }

    @Test
    public void testSetFile() {
        JSJshop jsjshop = new JSJshop();
        JSJshopNode tree = jsjshop.setFile("test.shp", new JSTaskAtom("p"))
                .getTree();
        assertNotNull(tree);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFile_nullFile() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.setFile(null, new JSTaskAtom("p"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFile_nullPredicate() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.setFile("test.shp", null);
    }

    
}