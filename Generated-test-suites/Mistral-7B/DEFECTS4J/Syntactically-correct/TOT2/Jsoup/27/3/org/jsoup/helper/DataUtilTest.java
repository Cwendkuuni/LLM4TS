import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        // Given
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("input.html");
        Document expectedDocument = Mockito.mock(Document.class);
        FileInputStream inputStreamMock = PowerMockito.mock(FileInputStream.class);

        PowerMockito.when(inputStreamMock.readAllBytes()).thenReturn("expectedHtmlContent".getBytes());

        // When
        Document actualDocument = DataUtil.load(inputFile, expectedCharset, baseUri);

        // Then
        assertNotNull(actualDocument);
        PowerMockito.verifyStatic();
        Parser parser = Parser.htmlParser();
        DataUtil.parseByteData(Mockito.any(ByteBuffer.class), Mockito.eq(expectedCharset), Mockito.eq(baseUri), Mockito.eq(parser));
    }

    // Edge case: null inputFile
    // ...
    // Error scenario: File not found
    // ...
}