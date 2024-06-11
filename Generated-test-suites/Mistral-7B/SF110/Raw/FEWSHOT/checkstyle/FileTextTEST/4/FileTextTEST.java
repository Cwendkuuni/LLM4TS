package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals(23, fileText.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsupportedCharset() throws IOException {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "XYZ");
    }

    @Test
    public void testFileTextCopyConstructor() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        FileText copyFileText = new FileText(fileText);
        assertEquals(fileText.getFile(), copyFileText.getFile());
        assertEquals(fileText.getCharset(), copyFileText.getCharset());
        assertArrayEquals(fileText.toLinesArray(), copyFileText.toLinesArray());
        assertNull(copyFileText.lineBreaks);
    }

    @Test
    public void testFileTextLinesConstructor() {
        File file = new File("src/test/resources/FileTextTest.java");
        List<String> lines = Arrays.asList(
                "package com.puppycrawl.tools.checkstyle.api;",
                "",
                "import java.io.File;",
                "import java.io.IOException;",
                "import java.nio.charset.Charset;",
                "import java.util.ArrayList;",
                "import java.util.Arrays;",
                "import java.util.List;",
                "",
                "public final class FileText extends AbstractList<String> {",
                "    // ... (constructors, fields, and methods omitted)",
                "}",
                "");
        FileText fileText = new FileText(file, lines);
        assertEquals(file, fileText.getFile());
        assertNull(fileText.getCharset());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
        assertNull(fileText.lineBreaks);
    }

    @Test
    public void testLineColumn() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        int pos = 26; // column number
        LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(5, lineColumn.getLineNo());
        assertEquals(5, lineColumn.getColumnNo());
    }

    @Test
    public void testGet() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("public final class FileText extends AbstractList<String> {", fileText.get(0));
        assertEquals("    // ... (constructors, fields, and methods omitted)", fileText.get(19));
    }

    @Test
    public void testSize() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(23, fileText.size());
    }

    @Test
    public void testFromLines() {
        File file = new File("src/test/resources/FileTextTest.java");
        List<String> lines = Arrays.asList(
                "package com.puppycrawl.tools.checkstyle.api;",
                "",
                "import java.io.File;",
                "import java.io.IOException;",
                "import java.nio.charset.Charset;",
                "import java.util.ArrayList;",
                "import java.util.Arrays;",
                "import java.util.List;",
                "",
                "public final class FileText extends AbstractList<String> {",
                "    // ... (constructors, fields, and methods omitted)",
                "}",
                "");
        FileText fileTextFromLines = FileText.fromLines(file, lines);
        assertEquals(file, fileTextFromLines.getFile());
        assertNull(fileTextFromLines.getCharset());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileTextFromLines.toLinesArray());
        assertNull(fileTextFromLines.lineBreaks);
    }

    @Test
    public void testFindLineBreaks() {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, "UTF-8");
        int[] lineBreaks = fileText.findLineBreaks();
        assertEquals(24, lineBreaks.length);
    }
}