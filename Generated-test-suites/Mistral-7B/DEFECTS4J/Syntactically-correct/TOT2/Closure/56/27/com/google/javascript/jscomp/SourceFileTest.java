import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SourceFileTest {

 @Test
 public void testGetLine() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedCode = "function test() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 2;
 String expectedLine = "  var x = 1;\n";
 String actualLine = sourceFile.getLine(lineNumber);
 assertEquals(expectedLine, actualLine);
 }

 @Test
 public void testGetLineInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedCode = "function test() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 10;
 assertNull(sourceFile.getLine(lineNumber));
 }

 @Test
 public void testGetLineIOException() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedCode = "function test() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 2;
 assertThrows(IOException.class, () -> sourceFile.getLine(lineNumber));
 }

 @Test
 public void testGetRegion() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedCode = "function test() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 2;
 Region expectedRegion = new Region(1, 2, "  var x = 1;\n");
 Region actualRegion = sourceFile.getRegion(lineNumber);
 assertEquals(expectedRegion, actualRegion);
 }

 @Test
 public void testGetRegionInvalidLineNumber() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedCode = "function test() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 10;
 assertNull(sourceFile.getRegion(lineNumber));
 }

 @Test
 public void testGetRegionIOException() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 String expectedCode = "function test() {\n" +
 "  var x = 1;\n" +
 "  return x;\n}";
 try {
 sourceFile.setCode(expectedCode);
 } catch (IOException e) {
 e.printStackTrace();
 }
 int lineNumber = 2;
 assertThrows(IOException.class, () -> sourceFile.getRegion(lineNumber));
 }
}