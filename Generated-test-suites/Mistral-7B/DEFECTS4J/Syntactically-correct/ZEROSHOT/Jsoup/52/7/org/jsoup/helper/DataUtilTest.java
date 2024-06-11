package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File inputFile = new File("inputFile.xml");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        inputFile.createNewFile();
        try (RandomAccessFile file = new RandomAccessFile(inputFile, "rw");) {
            file.write(outputStream.toByteArray());
        }

        Document doc = DataUtil.load(inputFile, "UTF-8", "/test");
        assertNotNull(doc);
        assertEquals("/test/inputFile.xml", doc.baseUri());

        inputFile.deleteOnExit();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document doc = DataUtil.load(in, "UTF-8", "/test");
        assertNotNull(doc);
        assertEquals("/test", doc.baseUri());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes());

        Parser parser = mock(Parser.class);
        when(parser.parseInput(inputStreamContent, "/test")).thenReturn(new Document());

        Document doc = DataUtil.load(in, "UTF-8", "/test", parser);
        assertNotNull(doc);
        assertEquals("/test", doc.baseUri());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String inputStreamContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        InputStream in = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document doc = DataUtil.load(in, null, "/test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFileWithNullBaseUri() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        File inputFile = new File("inputFile.xml");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes());
        inputFile.createNewFile();
        try (RandomAccessFile file = new RandomAccessFile(inputFile, "rw");) {
            file.write(outputStream.toByteArray());
        }

        Document doc = DataUtil.load(inputFile, "UTF-8", null);
    }

    @Test
    public void testParseByteData() throws IOException {
        String documentContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes());

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", "/test", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("/test", doc.baseUri());
    }

    @Test
    public void testParseByteDataDetectCharset() throws IOException {
        String documentContent = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<html><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(documentContent.getBytes());

        Document doc = DataUtil.parseByteData(byteData, null, "/test", Parser.htmlParser());
        assertNotNull(doc);
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }
}