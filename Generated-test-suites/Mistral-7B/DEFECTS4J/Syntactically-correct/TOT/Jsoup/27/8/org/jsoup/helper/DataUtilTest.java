package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-8'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        Files.write(testFile.toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(document);
        Files.delete(testFile.toPath());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='gb2312'?>\n<html>\n<head>\n<meta charset='gb2312'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        File testFile = new File("testFile.html");
        Files.write(testFile.toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

        Document document = null;
        try {
            document = DataUtil.load(testFile, null, null);
        } catch (IOException e) {
            // expected
        } finally {
            Files.delete(testFile.toPath());
        }

        Assert.assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-8'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document document = DataUtil.load(byteArrayInputStream, "UTF-8", null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithoutCharset() throws IOException {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-8'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document document = null;
        try {
            document = DataUtil.load(byteArrayInputStream, null, null);
        } finally {
            byteArrayInputStream.close();
        }

        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<?xml version='1.0' encoding='gb2312'?>\n<html>\n<head>\n<meta charset='gb2312'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(testData.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        Document document = null;
        try {
            document = DataUtil.load(byteArrayInputStream, null, null);
        } finally {
            byteArrayInputStream.close();
        }

        Assert.assertNull(document);
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String testData = "<?xml version='1.0' encoding='UTF-8'?>\n<html>\n<head>\n<meta charset='UTF-8'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(testDataBytes);

        Document document = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String testData = "<?xml version='1.0' encoding='gb2312'?>\n<html>\n<head>\n<meta charset='gb2312'>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(testDataBytes);

        Document document = null;
        try {
            document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());
        } finally {
            byteBuffer.rewind();
        }

        Assert.assertNull(document);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals(expectedCharset, charset);
    }
}