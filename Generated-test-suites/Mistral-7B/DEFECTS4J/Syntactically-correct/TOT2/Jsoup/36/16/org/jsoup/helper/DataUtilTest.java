import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        Document document = DataUtil.load(file, null, "");
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFileWithSpecifiedCharset.html");
        Document document = DataUtil.load(file, "UTF-16", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        File file = new File("testFile.html");
        FileInputStream fileInputStream = new FileInputStream(file);
        Document document = DataUtil.load(fileInputStream, null, "");
        assertNotNull(document);
        fileInputStream.close();
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        File file = new File("testFileWithSpecifiedCharset.html");
        FileInputStream fileInputStream = new FileInputStream(file);
        Document document = DataUtil.load(fileInputStream, "UTF-16", "");
        assertNotNull(document);
        fileInputStream.close();
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        File file = new File("testFile.html");
        FileInputStream fileInputStream = new FileInputStream(file);
        Document document = DataUtil.load(fileInputStream, null, "", DataUtil.Parser.xmlParser());
        assertNotNull(document);
        fileInputStream.close();
    }
}