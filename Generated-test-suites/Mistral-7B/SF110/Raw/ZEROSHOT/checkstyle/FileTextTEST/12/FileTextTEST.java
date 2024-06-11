package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.Lists;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws Exception {
        File file = new File("testfile.txt");
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText1 = new FileText(file, "UTF-8");
        FileText fileText2 = new FileText(fileText1);
        FileText fileText3 = new FileText(file, lines);

        assertEquals(file, fileText1.getFile());
        assertEquals(file, fileText2.getFile());
        assertEquals(file, fileText3.getFile());

        assertEquals(StandardCharsets.UTF_8, fileText1.getCharset());
        assertEquals(null, fileText2.getCharset());
        assertEquals(null, fileText3.getCharset());

        assertEquals("Line 1\nLine 2\nLine 3", fileText1.getFullText());
        assertEquals(null, fileText2.getFullText());
        assertEquals("Line 1\nLine 2\nLine 3", fileText3.getFullText());

        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText1.toLinesArray());
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText3.toLinesArray());

        // Test findLineBreaks()
        int[] lineBreaks = fileText1.findLineBreaks();
        assertEquals(4, lineBreaks.length);
        assertEquals(0, lineBreaks[0]);
        assertEquals(3, lineBreaks[1]);
        assertEquals(7, lineBreaks[2]);
        assertEquals(12, lineBreaks[3]);

        // Test lineColumn()
        LineColumn lineColumn1 = fileText1.lineColumn(0);
        LineColumn lineColumn2 = fileText1.lineColumn(3);
        LineColumn lineColumn3 = fileText1.lineColumn(13);

        assertEquals(1, lineColumn1.getLineNumber());
        assertEquals(1, lineColumn1.getColumnNumber());
        assertEquals(1, lineColumn2.getLineNumber());
        assertEquals(4, lineColumn2.getColumnNumber());
        assertEquals(2, lineColumn3.getLineNumber());
        assertEquals(1, lineColumn3.getColumnNumber());

        // Test get() and size()
        assertEquals("Line 1", fileText1.get(0));
        assertEquals("Line 2", fileText1.get(1));
        assertEquals("Line 3", fileText1.get(2));
        assertEquals(3, fileText1.size());
    }

    @Test
    public void testFileTextFromLines() throws Exception {
        File file = new File("testfile.txt");
        List<String> lines = Lists.newArrayList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);

        assertEquals(file, fileText.getFile());
        assertEquals(null, fileText.getCharset());
        assertEquals("Line 1\nLine 2\nLine 3", fileText.getFullText());
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, fileText.toLinesArray());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileTextConstructionWithInvalidFile() throws Exception {
        File file = new File("non-existent-file.txt");
        new FileText(file, "UTF-8");
    }
}