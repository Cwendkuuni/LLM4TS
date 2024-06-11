package com.example.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class FileTextTest {
@Test
public void testFileTextConstruction() throws IOException {
File file = new File("testFile.txt");
FileText fileText = new FileText(file, "UTF-8");
assertNotNull(fileText.getFile());
assertNotNull(fileText.getCharset());
assertNotNull(fileText.getFullText());
assertEquals(fileText.size(), Files.lines(file.toPath()).count());
}

@Test
public void testGetLine() {
FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
String expectedLine = "This is a test line.";
String line = fileText.get(0);
assertEquals(expectedLine, line);
}

@Test
public void testFileTextConstructionThrowsException() {
File file = new File("testFile.missing");
assertThrows(FileNotFoundException.class, () -> new FileText(file, "UTF-8"));
}
}