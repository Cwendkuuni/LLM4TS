import static org.junit.Assert.*;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class FileTextTest {

    @Test
    public void testFileTextConstructorWithFileAndCharset() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file path", file.getPath(), fileText.getFile().getPath());
        assertNotNull("Expected fullText to be set", fileText.getFullText());
        assertEquals("Expected number of lines to be greater than zero", fileText.size(), fileText.toLinesArray().length);
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructorWithUnsupportedCharset() throws IOException {
        File file = new File("test.txt");
        new FileText(file, "XYZ");
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("test.txt");
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, "UTF-8");
        FileText copiedFileText = new FileText(fileText);
        assertEquals("Expected both file paths to be equal", fileText.getFile().getPath(), copiedFileText.getFile().getPath());
        assertArrayEquals("Expected both line arrays to be equal", fileText.toLinesArray(), copiedFileText.toLinesArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTextCopyConstructorWithNullFileText() {
        new FileText(null);
    }

    @Test
    public void testFileTextFromLinesConstructor() {
        File file = new File("test.txt");
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertEquals("Expected file paths to be equal", fileText.getFile().getPath(), file.getPath());
        assertArrayEquals("Expected both line arrays to be equal", fileText.toLinesArray(), lines.toArray(new String[lines.size()]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileTextFromLinesConstructorWithNullFile() {
        new FileText(null, Lists.newArrayList("Line 1"));
    }

    @Test
    public void testGetFileMethod() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected file paths to be equal", fileText.getFile().getPath(), file.getPath());
    }

    @Test
    public void testGetCharsetMethod() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("Expected charset names to be equal", "UTF-8", fileText.getCharset().name());
    }

    @Test
    public void testGetFullTextMethod() {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull("Expected fullText to be set", fileText.getFullText());
    }

    @Test
    public void testToLinesArrayMethod() {
        File file = new File("test.txt");
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, lines);
        assertArrayEquals("Expected both line arrays to be equal", fileText.toLinesArray(), lines.toArray(new String[lines.size()]));
    }

    @Test
    public void testFindLineBreaksMethod() {
        String fileContent = "FirstLine\nSecondLine\nThirdLine";
        List<String> lines = Arrays.asList("FirstLine", "SecondLine", "ThirdLine");
        FileText fileText = new FileText(new File("test.txt"), fileContent);
        int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals("Expected lineBreaks to have correct length", lines.size() + 1, lineBreaks.length);
        for (int i = 0; i < lines.size(); ++i) {
            assertEquals("Expected lineBreaks to match line numbers", i + 1, lineBreaks[i] + 1);
        }
        assertEquals("Expected lineBreaks to have correct last value", fileContent.length(), lineBreaks[lines.size()]);
    }

    @Test
    public void testLineColumnMethod() {
        File file = new File("test.txt");
        String fileContent = "FirstLine\nLine With Space   \nThirdLine";
        List<String> lines = Arrays.asList("FirstLine", "Line With Space   ", "ThirdLine");
        FileText fileText = new FileText(file, fileContent);
        int pos = 19; // Column number 4 in the second line
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals("Expected lineNumber to be correct", 2, lineColumn.lineNumber);
        assertEquals("Expected columnNumber to be correct", 4, lineColumn.columnNumber);
    }

    @Test
    public void testGetMethod() {
        File file = new File("test.txt");
        String fileContent = "FirstLine\nLine One\nThirdLine";
        List<String> lines = Arrays.asList("FirstLine", "Line One", "ThirdLine");
        FileText fileText = new FileText(file, fileContent);
        assertEquals("Expected lines to match", lines.get(1), fileText.get(1));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetMethodWithNegativeIndex() {
        File file = new File("test.txt");
        String fileContent = "FirstLine\nLine One\nThirdLine";
        List<String> lines = Arrays.asList("FirstLine", "Line One", "ThirdLine");
        FileText fileText = new FileText(file, fileContent);
        fileText.get(-1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetMethodWithIndexGreaterThanSize() {
        File file = new File("test.txt");
        String fileContent = "FirstLine\nLine One\nThirdLine";
        List<String> lines = Arrays.asList("FirstLine", "Line One", "ThirdLine");
        FileText fileText = new FileText(file, fileContent);
        fileText.get(lines.size());
    }
}