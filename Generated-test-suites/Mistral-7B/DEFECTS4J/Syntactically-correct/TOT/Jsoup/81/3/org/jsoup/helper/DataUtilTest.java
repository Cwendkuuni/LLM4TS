package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String filePath = "path/to/valid-charset.html";
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(new File(filePath), expectedCharset, baseUri);

        // Assertions
        assertThat(doc.outputSettings().charset(), is(expectedCharset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "path/to/invalid-charset.html";
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(new File(filePath), null, baseUri);

        // Assertions
        assertThat(doc.outputSettings().charset(), is(DataUtil.defaultCharset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputData = "<html lang='en'>\n<head>\n<meta charset='%s'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>".formatted(charset);
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputStream, charset, baseUri);

        // Assertions
        assertThat(doc.outputSettings().charset(), is(charset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charset = "GBK"; // A non-supported charset
        String inputData = "<html lang='en'>\n<head>\n<meta charset='%s'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>".formatted(charset);
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputStream, charset, baseUri);

        // Assertions
        assertThat(doc.outputSettings().charset(), is(DataUtil.defaultCharset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String inputData = "<html lang='en'>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputStream, null, baseUri);

        // Assertions
        assertThat(doc.outputSettings().charset(), is(DataUtil.defaultCharset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testLoadInputStreamWithBOM() throws IOException {
        String inputData = "\uFEFF<html lang='en'>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";

        Document doc = DataUtil.load(inputStream, null, baseUri);

        // Assertions
        assertThat(doc.outputSettings().charset(), is(DataUtil.defaultCharset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charset = "UTF-8";
        String inputData = "<html lang='en'>\n<head>\n<meta charset='%s'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>".formatted(charset);
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";

        Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());

        // Assertions
        assertThat(doc.outputSettings().charset(), is(charset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charset = "GBK"; // A non-supported charset
        String inputData = "<html lang='en'>\n<head>\n<meta charset='%s'>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>".formatted(charset);
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";

        // Assertions
        try {
            Document doc = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
            assertThat(doc, is(nullValue()));
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String inputData = "<html lang='en'>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";

        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());

        // Assertions
        assertThat(doc.outputSettings().charset(), is(DataUtil.defaultCharset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testParseInputStreamWithBOM() throws IOException {
        String inputData = "\uFEFF<html lang='en'>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com/";

        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());

        // Assertions
        assertThat(doc.outputSettings().charset(), is(DataUtil.defaultCharset));
        // Add more assertions to test the structure and content of the loaded document
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "<html lang='en'>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello, World!</h1>\n</body>\n</html>";
        ByteArrayInputStream input = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(input, output);

        // Assertions
        String result = output.toString(StandardCharsets.UTF_8.name());
        Document doc = Jsoup.parse(result);

        // Assertions
        assertThat(doc.outputSettings().charset(), is(DataUtil.defaultCharset));
        // Add more assertions to test the structure and content of the output document
    }
}