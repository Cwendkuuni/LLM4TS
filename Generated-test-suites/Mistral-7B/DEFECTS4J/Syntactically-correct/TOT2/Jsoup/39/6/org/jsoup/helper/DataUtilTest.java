package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Assert.*;

public class DataUtilTest {
    // Test cases for the load method
    @Test
    public void testLoadFile() throws Exception {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, null, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileDoesNotExist() throws Exception {
        File inputFile = new File("does_not_exist.html");
        try {
            DataUtil.load(inputFile, "UTF-8", null);
            fail();
        } catch (FileNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testLoadFileIsDirectory() throws Exception {
        File inputFile = new File("test.html/");
        try {
            DataUtil.load(inputFile, "UTF-8", null);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testLoadEmptyFile() throws Exception {
        File inputFile = new File("empty_file.html");
        Document document = DataUtil.load(inputFile, "UTF-8", null);
        Assert.assertNotNull(document);
        Assert.assertEquals("", document.html());
    }

    @Test
    public void testLoadNullCharsetName() throws Exception {
        File inputFile = new File("test.html");
        try {
            DataUtil.load(inputFile, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadNullBaseUri() throws Exception {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    // Test cases for other methods
    // Add more test cases for other methods in the DataUtil class
}