import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSJshopTest {
    

    @Test
    public void testJSJshopConstructorWithValidArguments() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        assertNotNull(jsjshop);
    }

    @Test
    public void testJSJshopGetTree() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSJshopNode tree = jsjshop.getTree();
        assertNotNull(tree);
    }

    @Test
    public void testJSJshopGetSolution() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSPairPlanTSListNodes solution = jsjshop.getSolution();
        assertNotNull(solution);
    }

    @Test
    public void testJSJshopGetAddList() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSListLogicalAtoms addList = jsjshop.getAddList();
        assertNotNull(addList);
    }

    @Test
    public void testJSJshopGetDeleteList() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        assertNotNull(deleteList);
    }

    @Test
    public void testJSJshopSetFile() {
        String nameFile = "file.shp";
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jsjshop = new JSJshop();
        JSListLogicalAtoms addList = jsjshop.setFile(nameFile, pred);
        assertNotNull(addList);
    }

    @Test
    public void testJSJshopTestParser() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
    }

    @Test
    public void testJSJshopParserFile() {
        String libraryFile = "library.shp";
        JSJshop jsjshop = new JSJshop();
        boolean result = jsjshop.parserFile(libraryFile);
        assertTrue(result);
    }

    @Test
    public void testJSJshopGetBufferedReader() {
        String dir = ".";
        String file = "file.txt";
        JSJshop jsjshop = new JSJshop();
        BufferedReader br = jsjshop.getBufferedReader(dir, file);
        assertNotNull(br);
    }

    @Test
    public void testJSJshopGetBufferedReaderWithApplet() {
        JApplet applet = new JFrame();
        String file = "file.txt";
        JSJshop jsjshop = new JSJshop();
        BufferedReader br = jsjshop.getBufferedReader(null, file, applet);
        assertNotNull(br);
    }

    @Test
    public void testJSJshopGetAppletURL() {
        String file = "file.txt";
        JApplet applet = new JFrame();
        URL url = JSJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test
    public void testJSJshopProcessToken() {
        String nameDomainFile = "domain.shp";
        JSJshop jsjshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        jsjshop.processToken(tokenizer);
        fail();
    }

    @Test
    public void testJSJshopGetPlanningDomain() {
        String nameDomainFile = "domain.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, null);
        JSPlanningDomain dom = jsjshop.dom();
        assertNotNull(dom);
    }

    @Test
    public void testJSJshopGetPlanningProblem() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSPlanningProblem prob = jsjshop.prob();
        assertNotNull(prob);
    }

    @Test
    public void testJSJshopGetPlan() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSPlan plan = jsjshop.sol();
        assertNotNull(plan);
    }

    @Test
    public void testJSJshopGetTreeWithNullSolution() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSJshopNode tree = jsjshop.tree();
        assertNull(tree);
    }

    @Test
    public void testJSJshopGetSolutionWithNullTree() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSPairPlanTSListNodes solution = jsjshop.getSolution();
        assertNull(solution);
    }

    @Test
    public void testJSJshopGetAddListWithEmptyList() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSListLogicalAtoms addList = jsjshop.getAddList();
        assertTrue(addList.isEmpty());
    }

    @Test
    public void testJSJshopGetDeleteListWithEmptyList() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        assertTrue(deleteList.isEmpty());
    }

    
}