import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        String domainFile = "test_domain.shp";
        String problemFile = "test_problem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        assertNotNull(jsJshop);
    }

    @Test
    public void testJSJshop() {
        String domainFile = "test_domain.shp";
        String problemFile = "test_problem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test
    public void testSetFile() {
        String nameFile = "test_file.shp";
        JSTaskAtom pred = new JSTaskAtom("test_predicate");
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.setFile(nameFile, pred);
        assertNotNull(tree);
    }

    @Test
    public void testGetAddList() {
        String domainFile = "test_domain.shp";
        String problemFile = "test_problem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        String domainFile = "test_domain.shp";
        String problemFile = "test_problem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainInvalidArguments() {
        String[] args = {"test_domain.shp", "test_problem.shp", "invalid_argument"};
        JSJshop.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testMainInvalidNumberFormat() {
        String[] args = {"test_domain.shp", "test_problem.shp", "3abc"};
        JSJshop.main(args);
    }

    @Test
    public void testParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test
    public void testParserFileFalse() {
        String libraryFile = "test_file.shp";
        JSJshop jsJshop = new JSJshop();
        assertFalse(jsJshop.parserFile(libraryFile));
    }

    @Test
    public void testParserFileTrue() {
        String libraryFile = "test_file.shp";
        JSJshop jsJshop = new JSJshop();
        assertTrue(jsJshop.parserFile(libraryFile));
    }

    @Test
    public void testGetBufferedReader() {
        String dir = "test_dir";
        String file = "test_file.txt";
        JSJshop jsJshop = new JSJshop();
        BufferedReader reader = jsJshop.getBufferedReader(dir, file);
        assertNotNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        String file = "test_file.txt";
        URL url = JSJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        String input = "defdomain mydomain\n" +
                "(:init)\n" +
                "(:domain-type planner)\n" +
                "(:objects)\n" +
                "(:predicates)\n" +
                "(:action :name action1 :parameters (?x) :precondition (and (object ?x) (not (action-done ?x action1)))\n" +
                "(:action :name action2 :parameters (?x ?y) :precondition (and (object ?x) (object ?y) (not (adjacent ?x ?y)))\n" +
                "(:action :name action3 :parameters () :precondition (and))";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
    }
}