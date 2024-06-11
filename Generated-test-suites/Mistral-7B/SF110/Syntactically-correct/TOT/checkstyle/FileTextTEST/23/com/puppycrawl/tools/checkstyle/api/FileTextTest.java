import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {
    // 
    @Test
    public void testFileTextConstruction() throws IOException {
        File testFile = new File("testFile.txt");
        Charset charset = StandardCharsets.UTF_8;

        FileText fileText = new FileText(testFile, charset.name());

        assertNotNull(fileText.getFile());
        assertEquals(charset, fileText.getCharset());

        List<String> expectedLines = Arrays.asList(
                "Line 1",
                "Line 2",
                "Line 3"
        );

        assertArrayEquals(expectedLines.toArray(new String[expectedLines.size()]), fileText.toLinesArray());

        String expectedFullText = "Line 1\n" +
                "Line 2\n" +
                "Line 3";

        assertEquals(expectedFullText, fileText.getFullText());

        int[] expectedLineBreaks = new int[expectedLines.size() + 1];
        expectedLineBreaks[0] = 0;
        for (int i = 0; i < expectedLines.size(); i++) {
            expectedLineBreaks[i + 1] = expectedLines.get(i).length() + expectedLineBreaks[i];
        }
        expectedLineBreaks[expectedLines.size() + 1] = expectedFullText.length();

        assertArrayEquals(expectedLineBreaks, fileText.findLineBreaks());
    }

    @Test
    public void testFileTextException() throws IOException {
        File testFile = new File("testFile.txt");
        Charset charset = StandardCharsets.UTF_8;

        when(testFile.exists()).thenReturn(false);

        assertThrows(FileNotFoundException.class, () -> new FileText(testFile, charset.name()));
    }

    @Test
    public void testFileTextCharsetException() throws IOException {
        File testFile = new File("testFile.txt");
        String unsupportedCharset = "UTF-16BE";

        when(testFile.exists()).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> new FileText(testFile, unsupportedCharset));
    }
}
// 