package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = "UTF-8";
        File inputFile = new File("test.html");
        FileUtils.writeStringToFile(inputFile, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(inputFile, charsetName, "");
        Assert.assertEquals("Test file content", fileContent, doc.body().text());

        // Edge case: Empty file
        InputStream emptyFileStream = getClass().getResourceAsStream("/emptyFile.html");
        doc = DataUtil.load(new File(""), "", ""); // Should be equivalent to empty file
        Assert.assertNull(doc);
        emptyFileStream.close();

        // Error case: File does not exist
        File nonExistentFile = new File("nonExistentFile.html");
        Assert.assertThrows(IOException.class, () -> DataUtil.load(nonExistentFile, charsetName, ""));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Test input stream content.";
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, charsetName, "");
        Assert.assertEquals("Test input stream content", inputStreamContent, doc.body().text());

        // Edge case: Empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        doc = DataUtil.load(emptyInputStream, charsetName, "");
        Assert.assertNull(doc);

        // Error case: Invalid charset
        String invalidCharset = "InvalidCharset";
        Assert.assertThrows(IOException.class, () -> DataUtil.load(inputStream, invalidCharset, ""));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><body>Test input stream content</body></html>";
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, "", Parser.htmlParser());
        Assert.assertEquals("Test input stream content", inputStreamContent, doc.body().text());

        // Edge case: Empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Assert.assertNull(DataUtil.parseInputStream(emptyInputStream, charsetName, "", Parser.htmlParser()));

        // Edge case: Invalid parser
        Parser invalidParser = new Parser() {
            @Override
            public Document parseInput(String document, String baseUri) throws IOException {
                throw new UnsupportedOperationException("This parser is not supported.");
            }
        };
        Assert.assertThrows(IOException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, "", invalidParser));

        // Error case: Invalid charset
        String invalidCharset = "InvalidCharset";
        Assert.assertThrows(IOException.class, () -> DataUtil.parseInputStream(inputStream, invalidCharset, "", Parser.htmlParser()));
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "Test input stream content.";
        String expectedOutput = "Test input stream content";
        String charsetName = "UTF-8";

        // Test normal case
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals(expectedOutput, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));

        // Edge case: Empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        OutputStream emptyOutputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(emptyInputStream, emptyOutputStream);
        Assert.assertEquals("", new String(emptyOutputStream.toByteArray(), StandardCharsets.UTF_8));

        // Error case: Input stream does not support reading
        InputStream errorInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Error reading input stream.");
            }
        };
        OutputStream errorOutputStream = new ByteArrayOutputStream();
        Assert.assertThrows(IOException.class, () -> DataUtil.crossStreams(errorInputStream, errorOutputStream));
    }
}