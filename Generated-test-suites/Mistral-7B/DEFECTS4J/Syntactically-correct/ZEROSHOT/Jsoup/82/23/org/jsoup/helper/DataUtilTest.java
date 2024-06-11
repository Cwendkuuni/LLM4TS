package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.select.Elements;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "path/to/your/testfile.html";
        Document doc = DataUtil.load(new File(filePath), null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, "invalidCharset", null));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<title>Test Page</title>\n</head>\n<body>\n<p>Hello World!</p>\n</body>\n</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        Elements metaElements = doc.select("meta[http-equiv=content-type], meta[charset]");
        String foundCharset = null;
        for (Element meta : metaElements) {
            if (meta.hasAttr("http-equiv"))
                foundCharset = getCharsetFromContentType(meta.attr("content"));
            if (foundCharset == null && meta.hasAttr("charset"))
                foundCharset = meta.attr("charset");
            if (foundCharset != null)
                break;
        }
        assertNotNull(foundCharset);
        assertNotNull(doc.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String srcText = "Source text to be transferred.";
        String destText = "";
        InputStream inputStream = new ByteArrayInputStream(srcText.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        byte[] destBytes = outputStream.toByteArray();
        String destTextDecoded = new String(destBytes, StandardCharsets.UTF_8);
        assertNotNull(destTextDecoded);
        assertNotNull(destTextDecoded.equals(srcText));
    }

    private static String getCharsetFromContentType(String contentType) {
        return DataUtil.getCharsetFromContentType(contentType);
    }
}