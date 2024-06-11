package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    private DataUtil dataUtil;

    @Before
    public void setup() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><body><h1>Hello World</h1></body></html>";
        File file = new File("test.html");
        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(createDocument("test.html"));
        Mockito.when(parser.parseInput(Mockito.any(InputStream.class), Mockito.anyString(), Mockito.anyString())).thenAnswer(i -> createDocument("test.html"));

        Document document = dataUtil.load(file, "UTF-8", "");

        assertNotNull(document);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", document.html());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document document = dataUtil.load(inputStream, "UTF-8", "");

        assertNotNull(document);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", document.html());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><body><h1>Hello World</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.parseInputStream(inputStream, "UTF-8", "", parser);

        assertNotNull(document);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", document.html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceFileContent = "Source File Content";
        String targetFileContent = "Target File Content";
        File sourceFile = new File("source.txt");
        File targetFile = new File("target.txt");
        InputStream sourceInputStream = new FileInputStream(sourceFile);
        OutputStream targetOutputStream = new ByteArrayOutputStream();

        dataUtil.crossStreams(sourceInputStream, targetOutputStream);

        assertEquals(sourceFileContent, targetFileContent);
    }

    @Test
    public void testParseInputStreamWithBom() throws IOException {
        String bomContent = "\uFEFF<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream bomInputStream = new ByteArrayInputStream(bomContent.getBytes(StandardCharsets.UTF_8));

        Document document = dataUtil.parseInputStream(bomInputStream, "UTF-8", "", parser);

        assertNotNull(document);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", document.html());
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "Invalid charset";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer buffer = dataUtil.readToByteBuffer(inputStream);

        dataUtil.parseInputStream(inputStream, "INVALID_CHARSET", "", parser);
    }

    private Document createDocument(String name) {
        return new Document("") {
            @Override
            public String name() {
                return name;
            }
        };
    }
}