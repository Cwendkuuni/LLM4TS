import static org.junit.Assert.*;
import static org.junit.Assume.*;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("sample.txt");
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        assertEquals(file, fileText.getFile());
        assertEquals("UTF-8", fileText.getCharset().name());
        // Check the functionality of other methods
    }

    @Test
    public void testFileTextCopyConstructor() {
        FileText fileText = new FileText(new File("sample.txt"), "UTF-8");
        FileText copiedFileText = new FileText(fileText);
        assertEquals(fileText, copiedFileText);
        // Check the equality of other fields
    }

    @Test
    public void testFileTextFromLinesConstructor() {
        File file = new File("sample.txt");
        List<String> lines = Files.readLines(file, Charsets.UTF_8);
        FileText fileText = new FileText(file, lines);
        assertEquals(file, fileText.getFile());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
        // Check the functionality of other methods
    }

    @Test
    public void testFileTextConstructorWithNonExistentFileAndCharset() {
        File file = new File("non-existent-file.txt");
        String charsetName = "UTF-8";
        assertThrows(FileNotFoundException.class, () -> new FileText(file, charsetName));
    }

    @Test
    public void testFileTextWithUnsupportedCharset() {
        File file = new File("sample.txt");
        String charsetName = "XYZ";
        assertThrows(IllegalStateException.class, () -> new FileText(file, charsetName));
    }

    @Test
    public void testReadFileError() {
        File file = new File("sample.txt");
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        assertThrows(IOException.class, () -> FileText.readFile(file, decoder));
    }

    @Test
    public void testInvalidLineBreakPositions() {
        FileText fileText = new FileText(new File("sample.txt"), "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();
        // Check if lineBreaks have correct values
    }

    @Test
    public void testNegativeLineNumber() {
        FileText fileText = new FileText(new File("sample.txt"), "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(-1);
        assertNull(lineColumn);
    }

    @Test
    public void testOutOfBoundsException() {
        FileText fileText = new FileText(new File("sample.txt"), "UTF-8");
        String line = fileText.get(fileText.size());
        assertNull(line);
    }

    @Test
    public void testMalformedInputAndUnmappableCharacters() {
        File file = new File("sample-malformed.txt");
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        FileText fileText = new FileText(file, decoder);
        // Test malformed input and unmappable characters handling
    }

    @Test
    public void testLineColumn() {
        FileText fileText = new FileText(new File("sample.txt"), "UTF-8");
        LineColumn lineColumn = fileText.lineColumn(1);
        assertEquals(1, lineColumn.getLineNumber());
        // Test other functionality of lineColumn method
    }
}