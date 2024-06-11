package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.net.URI;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileInstance() {
        File testFile = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals("testFile.txt", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultStringInstance() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        assertEquals("testString", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultDoubleInstance() {
        FSPathResult fsPathResult = new FSPathResult(3.14);
        assertEquals("3.14", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultDateInstance() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertEquals(testDate.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultURIInstance() {
        URI testURI = new URI("http://www.google.com");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertEquals(testURI.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultBooleanInstance() {
        Boolean testBoolean = Boolean.TRUE;
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultFileNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
        assertNull(fsPathResult.getFile());
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultStringNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
        assertNull(fsPathResult.getString());
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultDoubleNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
        assertNull(fsPathResult.getDouble());
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultDateNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
        assertNull(fsPathResult.getDate());
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultURINull() {
        FSPathResult fsPathResult = new FSPathResult(null);
        assertNull(fsPathResult.getURI());
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultBooleanFalse() {
        Boolean testBoolean = Boolean.FALSE;
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultBooleanTrue() {
        Boolean testBoolean = Boolean.TRUE;
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultGetters() {
        File testFile = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals(testFile, fsPathResult.getFile());
        assertEquals("testFile.txt", fsPathResult.toString());

        String testString = "testString";
        FSPathResult stringFSPathResult = new FSPathResult(testString);
        assertEquals(testString, fsPathResult.getString());
        assertEquals("testString", fsPathResult.toString());

        Double testDouble = 3.14;
        FSPathResult doubleFSPathResult = new FSPathResult(testDouble);
        assertEquals(testDouble, fsPathResult.getDouble());
        assertEquals("3.14", fsPathResult.toString());

        Date testDate = new Date();
        FSPathResult dateFSPathResult = new FSPathResult(testDate);
        assertEquals(testDate, fsPathResult.getDate());
        assertEquals(testDate.toString(), fsPathResult.toString());

        URI testURI = new URI("http://www.google.com");
        FSPathResult uriFSPathResult = new FSPathResult(testURI);
        assertEquals(testURI, fsPathResult.getURI());
        assertEquals(testURI.toString(), fsPathResult.toString());

        Boolean testBoolean = Boolean.TRUE;
        FSPathResult booleanFSPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean, fsPathResult.getBoolean());
        assertEquals(testBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringDifferentObjects() {
        File testFile1 = new File("testFile1.txt");
        File testFile2 = new File("testFile2.txt");
        String testString1 = "testString1";
        String testString2 = "testString2";
        Double testDouble1 = 3.14;
        Double testDouble2 = 2.718;
        Date testDate1 = new Date();
        Date testDate2 = new Date();
        URI testURI1 = new URI("http://www.google.com");
        URI testURI2 = new URI("http://www.yahoo.com");
        Boolean testBoolean1 = Boolean.TRUE;
        Boolean testBoolean2 = Boolean.FALSE;

        FSPathResult fsPathResult1 = new FSPathResult(testFile1);
        FSPathResult fsPathResult2 = new FSPathResult(testFile2);
        FSPathResult stringFSPathResult1 = new FSPathResult(testString1);
        FSPathResult stringFSPathResult2 = new FSPathResult(testString2);
        FSPathResult doubleFSPathResult1 = new FSPathResult(testDouble1);
        FSPathResult doubleFSPathResult2 = new FSPathResult(testDouble2);
        FSPathResult dateFSPathResult1 = new FSPathResult(testDate1);
        FSPathResult dateFSPathResult2 = new FSPathResult(testDate2);
        FSPathResult uriFSPathResult1 = new FSPathResult(testURI1);
        FSPathResult uriFSPathResult2 = new FSPathResult(testURI2);
        FSPathResult booleanFSPathResult1 = new FSPathResult(testBoolean1);
        FSPathResult booleanFSPathResult2 = new FSPathResult(testBoolean2);

        assertNotEquals(fsPathResult1, fsPathResult2);
        assertNotEquals(stringFSPathResult1, stringFSPathResult2);
        assertNotEquals(doubleFSPathResult1, doubleFSPathResult2);
        assertNotEquals(dateFSPathResult1, dateFSPathResult2);
        assertNotEquals(uriFSPathResult1, uriFSPathResult2);
        assertNotEquals(booleanFSPathResult1, booleanFSPathResult2);

        assertNotEquals(fsPathResult1.toString(), fsPathResult2.toString());
        assertNotEquals(stringFSPathResult1.toString(), stringFSPathResult2.toString());
        assertNotEquals(doubleFSPathResult1.toString(), doubleFSPathResult2.toString());
        assertNotEquals(dateFSPathResult1.toString(), dateFSPathResult2.toString());
        assertNotEquals(uriFSPathResult1.toString(), uriFSPathResult2.toString());
        assertNotEquals(booleanFSPathResult1.toString(), booleanFSPathResult2.toString());
    }
}