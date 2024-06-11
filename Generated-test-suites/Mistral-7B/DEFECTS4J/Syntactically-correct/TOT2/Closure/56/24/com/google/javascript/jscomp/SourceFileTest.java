import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

 // Test constructor with valid fileName
 @Test
 public void testValidFileName() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 assertEquals("testFile.js", sourceFile.getName());
 }

 // Test getLine method with valid line number
 @Test
 public void testGetLineValidLineNumber() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("testFile.java"));
 String expectedLine = "public class TestClass {\n";
 String actualLine = sourceFile.getLine(1);
 assertEquals(expectedLine, actualLine);
 }

 // Test getLine method with invalid line number
 @Test(expected = IllegalArgumentException.class)
 public void testGetLineInvalidLineNumber() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("testFile.java"));
 sourceFile.getLine(100);
 }

 // Test getCode method with valid file
 @Test
 public void testGetCodeValidFile() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("testFile.java"));
 String expectedCode = "public class TestClass {\n" +
 "  public void testMethod() {\n" +
 "    int a = 5;\n" +
 "    int b = 10;\n" +
 "    int sum = a + b;\n" +
 "    assertEquals(15, sum);\n" +
 "  }\n" +
 "}";
 String actualCode = sourceFile.getCode();
 assertEquals(expectedCode, actualCode);
 }

 // Test getCode method with invalid file
 @Test(expected = IOException.class)
 public void testGetCodeInvalidFile() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("does_not_exist.java"));
 sourceFile.getCode();
 }

 // Test getRegion method with valid line number
 @Test
 public void testGetRegionValidLineNumber() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("testFile.java"));
 String expectedRegion = "public class TestClass {\n" +
 "  public void testMethod() {\n" +
 "    int a = 5;\n" +
 "    int b = 10;\n" +
 "    int sum = a + b;\n" +
 "    assertEquals(15, sum);\n" +
 "  }\n";
 String actualRegion = sourceFile.getRegion(1).getCode();
 assertEquals(expectedRegion, actualRegion);
 }

 // Test getRegion method with invalid line number
 @Test
 public void testGetRegionInvalidLineNumber() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("testFile.java"));
 Region region = sourceFile.getRegion(100);
 assertNull(region);
 }

 // Test isExtern method with valid file
 @Test
 public void testIsExternValidFile() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("testFile.java"));
 assertFalse(sourceFile.isExtern());
 }

 // Test isExtern method with extern file
 @Test
 public void testIsExternExternFile() {
 SourceFile sourceFile = new Preloaded("externFile.js", "externFilePath.js",
 "// Extern file.");
 assertTrue(sourceFile.isExtern());
 }

 // Test getLineOffset method with valid line number
 @Test
 public void testGetLineOffsetValidLineNumber() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("testFile.java"));
 int expectedOffset = 15;
 int actualOffset = sourceFile.getLineOffset(2);
 assertEquals(expectedOffset, actualOffset);
 }

 // Test getLineOffset method with invalid line number
 @Test(expected = IllegalArgumentException.class)
 public void testGetLineOffsetInvalidLineNumber() throws IOException {
 SourceFile sourceFile = new OnDisk(new File("testFile.java"));
 sourceFile.getLineOffset(100);
 }
}