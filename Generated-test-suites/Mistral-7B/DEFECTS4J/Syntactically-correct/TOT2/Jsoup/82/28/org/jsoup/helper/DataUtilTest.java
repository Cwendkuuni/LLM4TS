import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DataUtilTest {
    @Test
    public void testLoadFileWithUtf8Charset() throws IOException {
        String charsetName = "UTF-8";
        File file = new File("path/to/your/file.html");
        Document document = DataUtil.load(file, charsetName, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("path/to/your/file.html");
        Document document = DataUtil.load(file, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithUtf8Charset() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/path/to/your/file.html");
        Document document = DataUtil.load(inputStream, charsetName, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/path/to/your/file.html");
        Document document = DataUtil.load(inputStream, null, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = getClass().getResourceAsStream("/path/to/your/file.html");
        Document document = DataUtil.load(inputStream, charsetName, null, DataUtil.Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("path/to/your/input.html");
        File outputFile = new File("path/to/your/output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        FileChecker.assertFilesEqual(inputFile, outputFile);

        inputStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCrossStreamsWithIOException() throws IOException {
        File inputFile = new File("path/to/your/input.html");
        File outputFile = new File("path/to/your/output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(null, outputStream);

        inputStream.close();
        outputStream.close();
    }
}