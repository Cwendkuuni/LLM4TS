package org.junit.tests;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html"); // Replace this with a valid file path
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNotNull(doc);
    }

    @Test
    public void testLoadInvalidFileExtension() throws IOException {
        File testFile = new File("test.txt"); // Replace this with a valid file path
        Document doc = DataUtil.load(testFile, "UTF-8", "");
        assertNull(doc);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadNonExistentFile() throws IOException {
        File testFile = new File("non_existent_file.html"); // Replace this with a non-existent file path
        Document doc = DataUtil.load(testFile, "UTF-8", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInvalidCharsetName() throws IOException {
        File testFile = new File("test.html"); // Replace this with a valid file path
        Document doc = DataUtil.load(testFile, "", ""); // Empty charset name
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInvalidBaseUri() throws IOException {
        File testFile = new File("test.html"); // Replace this with a valid file path
        Document doc = DataUtil.load(testFile, "UTF-8", ""); // Empty base URI
        doc.outputSettings().baseUri("http://example.com"); // Set base URI and test loading with an empty base URI
        Document doc2 = DataUtil.load(testFile, "UTF-8", ""); // Empty base URI
        assertNull(doc2);
    }

    @Test
    public void testLoadEmptyInputStream() throws IOException {
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[0]);
        Document doc = DataUtil.load(testInputStream, "UTF-8", "");
        assertNull(doc);
    }
}