package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.lines);
        assertEquals(36, fileText.size());
    }

    @Test
    public void testFileTextConstructionFromList() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        final FileText fileText = new FileText(file, lines);
        assertNotNull(fileText.getFile());
        assertNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines);
        assertEquals(36, fileText.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithNonExistentFile() throws IOException {
        final File file = new File("non-existent-file.txt");
        new FileText(file, StandardCharsets.UTF_8.name());
    }

    @Test
    public void testFindLineBreaks() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        final int[] lineBreakPositions = fileText.findLineBreaks();
        assertEquals(37, lineBreakPositions.length);
        for (int i = 0; i < lineBreakPositions.length - 1; ++i) {
            assertTrue(lineBreakPositions[i + 1] > lineBreakPositions[i]);
        }
        assertEquals(13775, lineBreakPositions[lineBreakPositions.length - 1]);
    }

    @Test
    public void testLineColumn() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        final int pos = 321;
        final LineColumn lineColumn = fileText.lineColumn(pos);
        assertEquals(14, lineColumn.lineNumber);
        assertEquals(17, lineColumn.columnNumber);
    }

    @Test
    public void testGet() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals("package com.puppycrawl.tools.checkstyle.api;\n", fileText.get(0));
        assertEquals("import java.io.File;\nimport java.io.IOException;\nimport java.nio.charset.StandardCharsets;\nimport java.util.Arrays;\nimport java.util.List;\nimport java.util.regex.Pattern;\nimport java.util.Collections;\n", fileText.get(1));
        assertEquals("public final class FileText extends AbstractList<String> {\\n", fileText.get(2));
    }

    @Test
    public void testSize() {
        final File file = new File("src/test/resources/FileTextTest.java");
        final FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals(36, fileText.size());
    }
}