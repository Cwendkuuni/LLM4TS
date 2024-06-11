package umd.cs.shop; // import necessary packages here

import org.junit.Assert;
import org.junit.Test;

public class JSJshopTest {
    

    @Test
    public void testJSJshopConstructorWithDomainAndProblemFiles() {
        JSJshop jSJshop = new JSJshop("domain.txt", "problem.txt");
        Assert.assertNotNull(jSJshop.getTree());
        Assert.assertNotNull(jSJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopConstructorWithInvalidArguments() {
        String[] args = {"domain.txt", "problem.txt", "invalidArgument"};
        JSJshop jSJshop = new JSJshop(args);
    }

    @Test
    public void testJSJshopConstructorWithSetFile() {
        JSJshop jSJshop = new JSJshop();
        JSTaskAtom pred = new JSTaskAtom("p", new JSAction("action"), null);
        jSJshop.setFile("file.txt", pred);
        Assert.assertNotNull(jSJshop.getTree());
        Assert.assertNotNull(jSJshop.getSolution());
    }

    @Test
    public void testParser() {
        JSJshop jSJshop = new JSJshop();
        jSJshop.testParser(); // This method does not return anything, so we cannot use Assert.assertEquals()
    }

    @Test(expected = IOException.class)
    public void testGetBufferedReaderWithNullAppletAndFile() {
        JSJshop jSJshop = new JSJshop();
        jSJshop.getBufferedReader(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBufferedReaderWithNullAppletAndFileAndNullFile() {
        JSJshop jSJshop = new JSJshop();
        jSJshop.getBufferedReader(null, null, null);
    }

    @Test
    public void testGetBufferedReaderWithFile() {
        JSJshop jSJshop = new JSJshop();
        BufferedReader br = jSJshop.getBufferedReader("path/to/file.txt", null);
        Assert.assertNotNull(br);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAppletURLWithNullAppletAndFile() {
        JSJshop jSJshop = new JSJshop();
        jSJshop.getAppletURL(null, "file.txt");
    }

    @Test
    public void testProcessTokenWithDefdomain() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(defdomain myDomain)\n"));
        JSJshop jSJshop = new JSJshop();
        jSJshop.processToken(tokenizer);
        Assert.assertEquals("myDomain", jSJshop.dom().getName());
    }

    @Test
    public void testParseFileWithValidFile() {
        JSJshop jSJshop = new JSJshop();
        String libraryFile = "path/to/valid.library";
        boolean result = jSJshop.parserFile(libraryFile);
        Assert.assertTrue(result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testParseFileWithInvalidFile() {
        JSJshop jSJshop = new JSJshop();
        String libraryFile = "path/to/invalid.library";
        jSJshop.parserFile(libraryFile);
    }

    
}