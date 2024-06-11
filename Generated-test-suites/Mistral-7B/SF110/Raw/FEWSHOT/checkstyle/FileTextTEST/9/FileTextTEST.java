package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextCreationFromFile() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();
        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("Line 1");
            writer.println("Line 2");
            writer.println("Line 3");
        }

        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithEmptyContent() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(0, fileText.size());

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithNullCharset() {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try {
            new FileText(file, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithUnsupportedCharset() {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try {
            new FileText(file, "X-UNSUPPORTED-CHARSET");
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // Expected exception
        }

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithLines() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithEmptyLines() {
        final File file = new File("testFile.txt");
        file.createNewFile();

        List<String> lines = Collections.emptyList();
        FileText fileText = FileText.fromLines(file, lines);
        assertEquals(0, fileText.size());

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithNullLines() {
        final File file = new File("testFile.txt");
        file.createNewFile();

        List<String> lines = null;
        try {
            FileText fileText = FileText.fromLines(file, lines);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected exception
        }

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithInvalidLines() {
        final File file = new File("testFile.txt");
        file.createNewFile();

        List<Object> lines = Arrays.asList(1, 2.0, "Line 3");
        try {
            FileText fileText = FileText.fromLines(file, lines);
            fail("ClassCastException expected");
        } catch (ClassCastException e) {
            // Expected exception
        }

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithInvalidCharset() {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try {
            new FileText(file, "ISO-8859-15");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharset() {
        final File file = new File("testFile.txt");
        file.createNewFile();

        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(0, fileText.size());

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharsetAndContent() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("Line 1");
            writer.println("Line 2");
            writer.println("Line 3");
        }

        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharsetAndEmptyContent() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(0, fileText.size());

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharsetAndNullContent() {
        final File file = new File("testFile.txt");
        file.createNewFile();

        FileText fileText = new FileText(file, null);
        assertEquals(0, fileText.size());

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharsetAndContentClone() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("Line 1");
            writer.println("Line 2");
            writer.println("Line 3");
        }

        FileText fileText = new FileText(file, "UTF-8");
        FileText clonedFileText = new FileText(fileText);

        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));

        assertEquals(3, clonedFileText.size());
        assertEquals("Line 1", clonedFileText.get(0));
        assertEquals("Line 2", clonedFileText.get(1));
        assertEquals("Line 3", clonedFileText.get(2));

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharsetAndContentStringArray() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("Line 1");
            writer.println("Line 2");
            writer.println("Line 3");
        }

        String[] lines = {"Line 1", "Line 2", "Line 3"};
        FileText fileText = new FileText(file, "UTF-8", lines);
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharsetAndContentList() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("Line 1");
            writer.println("Line 2");
            writer.println("Line 3");
        }

        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        FileText fileText = new FileText(file, "UTF-8", lines);
        assertEquals(3, fileText.size());
        assertEquals("Line 1", fileText.get(0));
        assertEquals("Line 2", fileText.get(1));
        assertEquals("Line 3", fileText.get(2));

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharsetAndContentListWithNull() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("Line 1");
            writer.println("Line 2");
            writer.println("Line 3");
        }

        List<String> lines = null;
        try {
            FileText fileText = new FileText(file, "UTF-8", lines);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected exception
        }

        file.deleteOnExit();
    }

    @Test
    public void testFileTextCreationFromFileWithValidCharsetAndContentListWithMixedTypes() throws IOException {
        final File file = new File("testFile.txt");
        file.createNewFile();

        try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
            writer.println("Line 1");
            writer.println("Line 2");
            writer.println("Line 3");
        }

        List<Object> lines = Arrays.asList("Line 1", 2, "Line 3");
        try {
            FileText fileText = new FileText(file, "UTF-8", lines);
            fail("ClassCastException expected");
        } catch (ClassCastException e) {
            // Expected exception
        }

        file.deleteOnExit();
    }

    @Test
    public void testFindLineBreaks() {
        final String fileContent = "Line 1\nLine 2\nLine 3\nLine 4";
        FileText fileText = new FileText(Collections.singletonList(fileContent), null);

        int[] lineBreaks = fileText.findLineBreaks();
        assertEquals(4, lineBreaks.length);
        assertEquals(0, lineBreaks[0]);
        assertEquals(fileContent.length(), lineBreaks[3]);
    }

    @Test
    public void testFindLineBreaksWithEmptyContent() {
        FileText fileText = new FileText(Collections.emptyList(), null);
        int[] lineBreaks = fileText.findLineBreaks();
        assertEquals(0, lineBreaks.length);
    }
}