package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

import umd.cs.shop.JSJshop;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopInvalidArguments() {
        String[] args = {"domain.shp", "problem.shp", "invalidArg"};
        JSJshop jsJshop = new JSJshop(args);
    }

    @Test
    public void testJSJshopWithFileAndPredicate() {
        String nameFile = "nameFile.shp";
        JSTaskAtom predicate = new JSTaskAtom("predicate");
        JSJshop jsJshop = new JSJshop(nameFile, predicate);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test
    public void testParser() {
        String libraryFile = "library.shp";
        JSJshop jsJshop = new JSJshop();
        boolean parsed = jsJshop.parserFile(libraryFile);
        assertTrue(parsed);
    }

    @Test(expected = IOException.class)
    public void testGetBufferedReaderNullFile() {
        String dir = ".";
        JSJshop jsJshop = new JSJshop();
        jsJshop.getBufferedReader(dir, null);
    }

    @Test(expected = IOException.class)
    public void testGetBufferedReaderNullApplet() {
        String file = "file.txt";
        JSJshop jsJshop = new JSJshop();
        jsJshop.getBufferedReader(null, file);
    }

    @Test
    public void testGetBufferedReaderValidFile() {
        String dir = ".";
        String file = "file.txt";
        JSJshop jsJshop = new JSJshop();
        BufferedReader bufferedReader = jsJshop.getBufferedReader(dir, file);
        assertNotNull(bufferedReader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        String file = "file.txt";
        URL url = JSJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAppletURLNullFile() {
        JApplet applet = new JFrame();
        JSJshop.getAppletURL(null, applet);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAppletURLNullApplet() {
        String file = "file.txt";
        JSJshop.getAppletURL(file, null);
    }

    @Test
    public void testProcessToken() {
        String input = "defdomain MyDomain\n" +
                "(:start-state (and (and (not (not (at Location A)))\n" +
                "                     (not (at Location B)))\n" +
                "                   (and (at Location C) (clear Location D))))";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
        assertEquals(JSJshopVars.defdomain.toString(), tokenizer.sval);
    }

    @Test(expected = JSParserError.class)
    public void testProcessTokenInvalidToken() {
        String input = "defdomain MyDomain\n" +
                "%%%";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
    }
}