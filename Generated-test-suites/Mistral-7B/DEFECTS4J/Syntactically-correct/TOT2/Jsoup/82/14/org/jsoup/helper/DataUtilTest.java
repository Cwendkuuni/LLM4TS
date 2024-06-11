import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertEquals(fileContent, doc.body().html());

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        Document doc = DataUtil.load(new File("nonExistentFile.html"), null, null);
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInvalidFile() throws IOException {
        File testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, "brokenFile", StandardCharsets.UTF_8);

        try {
            Document doc = DataUtil.load(testFile, null, null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        FileUtils.deleteQuietly(testFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertEquals(testData, doc.body().html());

        inputStream.close();
    }

    @Test
    public void testLoadInvalidInputStream() {
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            Document doc = DataUtil.load(inputStream, null, null);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testData = "<html><body>This is a test input stream.</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
        Assert.assertEquals(testData, doc.html());

        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() {
        String testData = "<html><body>This is a test input stream.</body></html>";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));

        try {
            Document doc = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceData = "Source data.";
        String targetData = "Target data.";

        InputStream sourceStream = new ByteArrayInputStream(sourceData.getBytes(StandardCharsets.UTF_8));
        OutputStream targetStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(sourceStream, targetStream);
        String targetContent = targetStream.toString(StandardCharsets.UTF_8.toString());
        Assert.assertEquals(sourceData, targetContent);

        sourceStream.close();
        targetStream.close();
    }
}