package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.puppycrawl.tools.checkstyle.api.util.TextUtil;

public class FileTextTest {

    @Test
    public void testFileTextConstructionFromFile() throws IOException {
        File file = new File("src/test/resources/FileTextTest.java");
        FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
        assertEquals(new File("src/test/resources/FileTextTest.java"), fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertArrayEquals(getLinesFromFile(file), fileText.toLinesArray());

        FileText fileTextFromLines = FileText.fromLines(file, getLinesFromFile(file));
        assertEquals(fileText, fileTextFromLines);
    }

    @Test
    public void testFileTextConstructionFromFileAndLines() {
        String fileContent = "Line 1.\r\nLine 2.\r\nLine 3.";
        List<String> lines = Lists.newArrayList(fileContent.split("\r\n"));
        FileText fileText = new FileText(new File("testFile.txt"), lines);
        assertEquals("testFile.txt", fileText.getFile().getName());
        assertNull(fileText.getCharset());
        assertEquals(fileContent, fileText.getFullText());
        assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }

    @Test
    public void testFindLineBreaks() {
        FileText fileText = new FileText(new File("src/test/resources/FileTextTest.java"), StandardCharsets.UTF_8.name());
        int[] lineBreaks = fileText.findLineBreaks();
        assertArrayEquals(new int[] {0, 60, 121, 123, 125, 133, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301}, lineBreaks);
    }

    @Test
    public void testLineColumnMethod() {
        FileText fileText = new FileText(new File("src/test/resources/FileTextTest.java"), StandardCharsets.UTF_8.name());
        LineColumn lineColumn1 = fileText.lineColumn(1);
        LineColumn lineColumn2 = fileText.lineColumn(263);
        LineColumn lineColumn3 = fileText.lineColumn(264);
        LineColumn lineColumn4 = fileText.lineColumn(265);
        LineColumn lineColumn5 = fileText.lineColumn(266);
        LineColumn lineColumnInvalid = fileText.lineColumn(302);

        assertEquals(1, lineColumn1.getLineNumber());
        assertEquals(1, lineColumn1.getColumnNumber());
        assertEquals(263, lineColumn2.getLineNumber());
        assertEquals(1, lineColumn2.getColumnNumber());
        assertEquals(264, lineColumn3.getLineNumber());
        assertEquals(14, lineColumn3.getColumnNumber());
        assertEquals(265, lineColumn4.getLineNumber());
        assertEquals(3, lineColumn4.getColumnNumber());
        assertEquals(266, lineColumn5.getLineNumber());
        assertEquals(35, lineColumn5.getColumnNumber());
        assertNull(lineColumnInvalid);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLineColumnInvalidIndex() {
        FileText fileText = new FileText(new File("src/test/resources/FileTextTest.java"), StandardCharsets.UTF_8.name());
        fileText.lineColumn(301);
    }

    private String[] getLinesFromFile(File file) throws IOException {
        return TextUtil.readLinesFromFile(file, StandardCharsets.UTF_8);
    }
}