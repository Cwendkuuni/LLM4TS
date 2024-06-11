import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a sample file content";
        File inputFile = new File("input.html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        inputFile.writeBytes(outputStream.toByteArray());

        Document expectedDocument = Parser.htmlParser().parseInput(fileContent, null);
        Document actualDocument = DataUtil.load(inputFile, StandardCharsets.UTF_8.name(), null);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.text(), actualDocument.text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a sample input stream content";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = Parser.htmlParser().parseInput(inputStreamContent, null);
        Document actualDocument = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null);

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.text(), actualDocument.text());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String docData = "This is a sample document with no charset specified";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = Parser.htmlParser().parseInput(docData, null);
        Document actualDocument = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.text(), actualDocument.text());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String charsetName = "ISO-8859-1";
        String docData = "This is a sample document with specified charset (ISO-8859-1)";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = Parser.htmlParser().parseInput(docData, null);
        Document actualDocument = DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());

        Assert.assertNotNull(actualDocument);
        Assert.assertEquals(expectedDocument.text(), actualDocument.text());
    }

    @Test(expected = IOException.class)
    public void testParseByteDataWithUnsupportedCharset() throws IOException {
        String charsetName = "ABC-123";
        String docData = "This is a sample document with unsupported charset (ABC-123)";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = Parser.htmlParser().parseInput(docData, null);
        DataUtil.parseByteData(byteData, charsetName, null, Parser.htmlParser());
    }
}