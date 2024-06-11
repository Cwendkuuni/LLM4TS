package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class JSJshopTest {

    private JSJshop jsJshop;
    private String domainFileName = "domain.shp";
    private String problemFileName = "problem.shp";

    @Before
    public void setup() {
        jsJshop = new JSJshop();
    }

    @Test
    public void testMain() {
        String[] args = new String[]{"domain.shp", "problem.shp"};
        JSJshop.main(args);
        assertTrue(jsJshop.getSolution() != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentLength() {
        String[] args = new String[]{"domain.shp"};
        jsJshop = new JSJshop();
        jsJshop.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidArgumentNumber() {
        String[] args = new String[]{"domain.shp", "problem.shp", "invalid"};
        jsJshop = new JSJshop();
        jsJshop.main(args);
    }

    @Test
    public void testParsingDomainFile() {
        jsJshop = new JSJshop(domainFileName, null);
        assertNotNull(jsJshop.getTree());
    }

    @Test
    public void testParsingProblemFile() {
        jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test
    public void testParsingFileWithTaskAtom() {
        jsJshop = new JSJshop("domain.shp", new JSTaskAtom("task"));
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test
    public void testGetAddList() {
        jsJshop = new JSJshop(domainFileName, problemFileName);
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        jsJshop = new JSJshop(domainFileName, problemFileName);
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
    }

    @Test
    public void testSetFile() {
        jsJshop = new JSJshop();
        jsJshop.setFile(domainFileName, new JSTaskAtom("task"));
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException() {
        String invalidFileName = "invalidFile.shp";
        jsJshop = new JSJshop();
        jsJshop.setFile(invalidFileName, new JSTaskAtom("task"));
    }

    @Test
    public void testParser() {
        JSJshop jsJshopParser = new JSJshop();
        jsJshopParser.testParser();
    }

    @Test
    public void testBufferedReader() throws IOException {
        String dir = System.getProperty("user.dir");
        String file = "testFile.txt";
        BufferedReader br = jsJshop.getBufferedReader(dir, file);
        assertNotNull(br);
    }

    @Test
    public void testAppletURL() {
        String file = "testFile.txt";
        JApplet applet = new JFrame();
        URL url = jsJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }
}