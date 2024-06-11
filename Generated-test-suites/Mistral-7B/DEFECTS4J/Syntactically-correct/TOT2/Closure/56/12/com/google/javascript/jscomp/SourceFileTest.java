import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

 @Test
 public void testGetLine() {
 SourceFile sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("class Test {\n" +
 "  void test() {\n" +
 "  }\n" +
 "}");
 String expected = "class Test {\n";
 String actual = sourceFile.getLine(1);
 assertEquals(expected, actual);
 } catch (IOException e) {
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testGetLineInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("class Test {\n" +
 "  void test() {\n" +
 "  }\n" +
 "}");
 assertNull(sourceFile.getLine(10));
 } catch (IOException e) {
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testGetRegion() {
 SourceFile sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("class Test {\n" +
 "  void test() {\n" +
 "    int a = 5;\n" +
 "    int b = 10;\n" +
 "    int c = a + b;\n" +
 "  }\n" +
 "}");
 Region region = sourceFile.getRegion(3);
 assertEquals("int a = 5;\nint b = 10;", region.getCode());
 } catch (IOException e) {
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testGetRegionInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("class Test {\n" +
 "  void test() {\n" +
 "    int a = 5;\n" +
 "    int b = 10;\n" +
 "    int c = a + b;\n" +
 "  }\n" +
 "}");
 Region region = sourceFile.getRegion(5);
 assertNull(region);
 } catch (IOException e) {
 fail("Exception should not be thrown");
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetLineNegativeLineNumber() {
 SourceFile sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("class Test {\n" +
 "  void test() {\n" +
 "    int a = 5;\n" +
 "    int b = 10;\n" +
 "    int c = a + b;\n" +
 "  }\n" +
 "}");
 sourceFile.getLine(-1);
 fail("IllegalArgumentException should be thrown");
 } catch (IOException e) {
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testGetLineOffset() {
 SourceFile sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("class Test {\n" +
 "  void test() {\n" +
 "    int a = 5;\n" +
 "    int b = 10;\n" +
 "    int c = a + b;\n" +
 "  }\n" +
 "}");
 int lineNumber = 2;
 int lineOffset = sourceFile.getLineOffset(lineNumber);
 int expected = 32;
 int actual = lineOffset;
 assertEquals(expected, actual);
 } catch (IOException e) {
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testGetNumLines() {
 SourceFile sourceFile = new SourceFile("TestFile.java");
 try {
 sourceFile.setCode("class Test {\n" +
 "  void test() {\n" +
 "    int a = 5;\n" +
 "    int b = 10;\n" +
 "    int c = a + b;\n" +
 "  }\n" +
 "}");
 int numLines = sourceFile.getNumLines();
 int expected = 4;
 int actual = numLines;
 assertEquals(expected, actual);
 } catch (IOException e) {
 fail("Exception should not be thrown");
 }
 }
}