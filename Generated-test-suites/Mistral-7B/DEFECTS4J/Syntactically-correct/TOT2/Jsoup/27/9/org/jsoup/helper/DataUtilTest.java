package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // assuming a valid test file exists
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Document document = DataUtil.load(file, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/test.html"); // assuming a valid test file exists
        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String expectedHtml = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\"/>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<title>Test page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello, world!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteData = ByteBuffer.allocate(expectedHtml.length());
        byteData.put(expectedHtml.getBytes());
        byteData.flip();

        String charsetName = "UTF-8";
        String baseUri = "http://localhost";
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals(expectedHtml, document.html());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Test input.";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(input.getBytes());

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Assert.assertArrayEquals(expectedByteBuffer.array(), byteData.array());
    }
}