package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.getTree());
        assertNull(jsjshop.getSolution());
        assertNull(jsjshop.getAddList());
        assertNull(jsjshop.getDeleteList());

        JSJshop jsjshop1 = new JSJshop("domain.pl", "problem.pl");
        assertNotNull(jsjshop1.getTree());
        assertNotNull(jsjshop1.getSolution());
        assertNotNull(jsjshop1.getAddList());
        assertNotNull(jsjshop1.getDeleteList());
    }

    @Test
    public void testJSJshopParser() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
    }

    @Test
    public void testJSJshopFile() {
        JSJshop jsjshop = new JSJshop();
        JSJshop jsjshop1 = jsjshop.setFile("file.pl", new JSTaskAtom("p"));
        assertNotNull(jsjshop1.getTree());
        assertNotNull(jsjshop1.getSolution());
        assertNotNull(jsjshop1.getAddList());
        assertNotNull(jsjshop1.getDeleteList());
    }

    @Test
    public void testJSJshopGetBufferedReader() {
        JSJshop jsjshop = new JSJshop();
        BufferedReader br = jsjshop.getBufferedReader("", "");
        assertNull(br);

        JApplet applet = new JApplet() {
            @Override
            public String getDocumentBase() {
                return "";
            }
        };

        BufferedReader br1 = jsjshop.getBufferedReader("/path/to/file", applet);
        assertNotNull(br1);
    }

    @Test
    public void testJSJshopGetAppletURL() {
        JApplet applet = new JApplet() {
            @Override
            public String getDocumentBase() {
                return "http://localhost:8080/";
            }
        };

        URL url = JSJshop.getAppletURL("/path/to/file", applet);
        assertNotNull(url);
    }
}