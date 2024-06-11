package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

import umd.cs.shop.JSJshop;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.getTree());
        assertNull(jsjshop.getSolution());
        JSJshop jsjshop1 = new JSJshop("domain.txt", "problem.txt");
        assertNotNull(jsjshop1.getTree());
        assertNotNull(jsjshop1.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopWithInvalidArgs() {
        new JSJshop(new String[0]);
    }

    @Test
    public void testParserFile() {
        JSJshop jsjshop = new JSJshop();
        assertTrue(jsjshop.parserFile("validFile.txt"));
    }

    @Test(expected = IOException.class)
    public void testParserFileWithIOException() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.parserFile("invalidFile.txt");
    }

    @Test(expected = JSParserError.class)
    public void testParserFileWithParserError() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.parserFile("errorFile.txt");
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsjshop = new JSJshop();
        String dir = ".";
        String file = "validFile.txt";
        BufferedReader reader = jsjshop.getBufferedReader(dir, file);
        assertNotNull(reader);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBufferedReaderWithNullApplet() {
        JSJshop jsjshop = new JSJshop();
        JApplet applet = null;
        String file = "validFile.txt";
        jsjshop.getBufferedReader(applet, file);
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsjshop = new JSJshop();
        JApplet applet = new JFrame();
        String file = "validFile.txt";
        URL url = jsjshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAppletURLWithNullApplet() {
        JSJshop jsjshop = new JSJshop();
        JApplet applet = null;
        String file = "validFile.txt";
        jsjshop.getAppletURL(file, applet);
    }

    @Test(expected = MalformedURLException.class)
    public void testGetAppletURLWithMalformedURL() {
        JSJshop jsjshop = new JSJshop();
        JApplet applet = new JFrame();
        String file = "invalidURL.txt";
        jsjshop.getAppletURL(file, applet);
    }
}