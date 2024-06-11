package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        // Given a test file
        File inputFile = new File("test.html");
        byte[] expectedContent = "Test content".getBytes();
        FileWriter fileWriter = new FileWriter(inputFile);
        fileWriter.write(new String(expectedContent));
        fileWriter.close();

        // When loading the file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document actualDocument = DataUtil.load(inputFile, charsetName, baseUri);

        // Then the document should be parsed correctly
        assertEquals(expectedContent.length, actualDocument.body().text().getBytes("UTF-8").length);
        assertEquals("Test content", actualDocument.body().text());

        // And the file should be closed
        PowerMockito.verifyPrivate(inputFile).verify("close");
    }

    @Test
    public void testLoadInputStream() throws Exception {
        // Given a test input stream
        byte[] inputContent = "Test content".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent);

        // When loading the input stream
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

        // Then the document should be parsed correctly
        assertEquals(inputContent.length, actualDocument.body().text().getBytes("UTF-8").length);
        assertEquals("Test content", actualDocument.body().text());

        // And the input stream should be closed
        PowerMockito.verifyPrivate(inputStream).verify("close");
    }

    @Test
    public void testParseByteData() throws Exception {
        // Given some byte data
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap("Test content".getBytes(Charset.forName(charsetName)));

        // When parsing the byte data
        String baseUri = "http://example.com";
        Parser parser = Mockito.mock(Parser.class);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Then the document should be parsed correctly
        Mockito.verify(parser).parseInput("Test content", baseUri);
        assertEquals("Test content", actualDocument.body().text());
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws Exception {
        // Given some byte data with a meta charset tag
        String charsetName = null;
        ByteBuffer byteData = ByteBuffer.wrap("Content-Type: text/html; charset=ISO-8859-1\n<html><meta charset=\"UTF-8\">Test content".getBytes());

        // When parsing the byte data
        String baseUri = "http://example.com";
        Parser parser = Mockito.mock(Parser.class);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        // Then the document should be parsed correctly with the detected charset
        Mockito.verify(parser).parseInput("Test content", baseUri);
        assertEquals("Test content", actualDocument.body().text());
        assertEquals("UTF-8", actualDocument.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws Exception {
        // Given a test input stream
        byte[] inputContent = "Test content".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent);

        // When reading the input stream to a byte buffer
        ByteBuffer expectedByteData = ByteBuffer.wrap(inputContent);
        ByteBuffer actualByteData = DataUtil.readToByteBuffer(inputStream);

        // Then the byte buffer should be equal to the expected byte data
        assertEquals(expectedByteData.capacity(), actualByteData.capacity());
        assertEquals(expectedByteData.limit(), actualByteData.limit());
        assertEquals(expectedByteData.position(), actualByteData.position());
        assertArrayEquals(expectedByteData.array(), actualByteData.array());
    }

    @Test
    public void testGetCharsetFromContentType() throws Exception {
        // Given some content types
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "application/xml";

        // When parsing the content types
        String actualCharset1 = DataUtil.getCharsetFromContentType(contentType1);
        String actualCharset2 = DataUtil.getCharsetFromContentType(contentType2);

        // Then the charsets should be correctly parsed
        assertEquals("UTF-8", actualCharset1);
        assertNull(actualCharset2);
    }
}