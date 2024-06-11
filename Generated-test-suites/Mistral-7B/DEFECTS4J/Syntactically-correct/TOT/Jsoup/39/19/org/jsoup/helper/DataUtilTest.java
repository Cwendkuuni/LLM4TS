import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    @BeforeClass
    public static void init() {
        DataUtil.parser = Mockito.spy(Parser.htmlParser());
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body>Hello World</body></html>";
        File inputFile = new File("test.html");
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = Mockito.mock(ByteBuffer.class);
        Mockito.when(byteData.asReadOnlyBuffer()).thenReturn(fileContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = DataUtil.parser.parseInput(fileContent, null);

        Mockito.when(DataUtil.readToByteBuffer(inputFile)).thenReturn(byteData);
        Mockito.when(DataUtil.parseByteData(byteData, "UTF-8", null, parser)).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(inputFile, "UTF-8", null);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body>Hello World</body></html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = Mockito.mock(ByteBuffer.class);
        Mockito.when(byteData.asReadOnlyBuffer()).thenReturn(fileContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = DataUtil.parser.parseInput(fileContent, null);

        Mockito.when(DataUtil.parseByteData(byteData, null, null, parser)).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)), null, null);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<html><head><meta charset=\"gb2312\"><title>Test</title></head><body>Hello World</body></html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = Mockito.mock(ByteBuffer.class);
        Mockito.when(byteData.asReadOnlyBuffer()).thenReturn(fileContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = DataUtil.parser.parseInput(fileContent, null);

        Mockito.when(DataUtil.parseByteData(byteData, "UTF-8", null, parser)).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)), null, null);

        assertEquals(expectedDocument, actualDocument);

        Mockito.verify(parser, Mockito.times(1)).parseInput(fileContent, null);
        Mockito.verify(parser, Mockito.times(1)).parseInput(expectedDocument.html(), null);

        Document documentWithGB2312 = DataUtil.parser.parseInput(expectedDocument.html(), "gb2312");
        Document actualDocumentWithGB2312 = DataUtil.load(new ByteArrayInputStream(expectedDocument.html().getBytes("gb2312")), "gb2312", null);

        assertEquals(documentWithGB2312, actualDocumentWithGB2312);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String fileContent = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body>Hello World</body></html>";
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteData = Mockito.mock(ByteBuffer.class);
        Mockito.when(byteData.asReadOnlyBuffer()).thenReturn(fileContent.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = DataUtil.parser.parseInput(fileContent, null);

        Parser customParser = Mockito.mock(Parser.class);
        Mockito.when(customParser.parseInput(fileContent, null)).thenReturn(expectedDocument);

        Mockito.when(DataUtil.parseByteData(byteData, "UTF-8", null, parser)).thenReturn(expectedDocument);

        Document actualDocument = DataUtil.load(new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)), "UTF-8", null, customParser);

        assertEquals(expectedDocument, actualDocument);
        Mockito.verify(customParser, Mockito.times(1)).parseInput(fileContent, null);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        File inputFile = new File("test.html");
        Mockito.when(DataUtil.readToByteBuffer(inputFile)).thenThrow(new IOException());

        DataUtil.load(inputFile, "UTF-8", null);
    }
}