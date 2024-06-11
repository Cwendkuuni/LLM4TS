import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private Document document;

    @Before
    public void setUp() {
        inputStream = Mockito.mock(InputStream.class);
        outputStream = Mockito.mock(OutputStream.class);
        document = Mockito.mock(Document.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = Mockito.mock(File.class);
        String charsetName = "UTF-8";
        String baseUri = "http://test.com/";

        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(file.isFile()).thenReturn(true);
        Mockito.when(file.length()).thenReturn(1024);

        ByteArrayOutputStream outputStreamMock = new ByteArrayOutputStream();
        ByteArrayInputStream inputStreamMock = new ByteArrayInputStream(new byte[1024]);

        DataUtil.crossStreams(inputStreamMock, outputStreamMock);

        Mockito.when(file.getAbsolutePath()).thenReturn("/path/to/file");
        Mockito.when(file.getName()).thenReturn("file.html");

        Document loadedDocument = DataUtil.load(file, charsetName, baseUri);

        Mockito.verify(file).exists();
        Mockito.verify(file).isFile();
        Mockito.verify(file).length();
        Mockito.verify(inputStream).close();
        Mockito.verify(outputStream).close();
        Mockito.verifyNoMoreInteractions(inputStream, outputStream);

        Mockito.verify(document).setBaseUri(baseUri);

        assertEquals("/path/to/file", loadedDocument.baseUri());
        assertNotNull(loadedDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File file = Mockito.mock(File.class);

        Mockito.when(file.exists()).thenReturn(false);

        DataUtil.load(file, "UTF-8", "http://test.com/");
    }

    @Test
    public void testParseInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com/";
        Parser parser = Mockito.mock(Parser.class);

        ByteArrayInputStream inputStreamMock = new ByteArrayInputStream("<html><body>Test</body></html>".getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStreamMock, charsetName, baseUri, parser);

        Mockito.verify(parser).parseInput(Mockito.anyString(), Mockito.eq(baseUri));
        assertEquals("<html><body>Test</body></html>", document.body().html());
        assertNotNull(document);
        Mockito.verifyNoMoreInteractions(inputStreamMock, parser);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamIOException() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com/";
        Parser parser = Mockito.mock(Parser.class);

        ByteArrayInputStream inputStreamMock = new ByteArrayInputStream(new byte[1024]);

        DataUtil.parseInputStream(inputStreamMock, charsetName, baseUri, parser);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        OutputStream outputStream = Mockito.mock(OutputStream.class);

        DataUtil.crossStreams(inputStream, outputStream);

        Mockito.verify(inputStream).read();
        Mockito.verify(outputStream).write(Mockito.anyByte());
    }
}