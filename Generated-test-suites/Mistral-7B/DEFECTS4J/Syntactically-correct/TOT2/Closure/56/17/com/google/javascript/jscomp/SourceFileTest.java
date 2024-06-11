import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

 // Test constructor with valid fileName
 @Test
 public void testSourceFileConstruction() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 assertEquals("testFile.js", sourceFile.getName());
 }

 // Test getLine method with valid line number
 @Test
 public void testGetLineValidLineNumber() throws IOException {
 SourceFile sourceFile = new SourceFile("testFile.js");
 sourceFile.setCode("line1\nline2\nline3");
 String line1 = sourceFile.getLine(1);
 assertEquals("line1", line1);
 }

 // Test getLine method with invalid line number
 @Test(expected = IllegalArgumentException.class)
 public void testGetLineInvalidLineNumber() throws IOException {
 SourceFile sourceFile = new SourceFile("testFile.js");
 sourceFile.setCode("line1\nline2\nline3");
 String line5 = sourceFile.getLine(5);
 }

 // Test getLine method with IOException
 @Test(expected = IOException.class)
 public void testGetLineIOException() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 sourceFile.setCode(null);
 String line1 = sourceFile.getLine(1);
 }

 // Test getRegion method with valid line number
 @Test
 public void testGetRegionValidLineNumber() throws IOException {
 SourceFile sourceFile = new SourceFile("testFile.js");
 sourceFile.setCode("line1\nline2\nline3");
 Region region = sourceFile.getRegion(2);
 assertEquals(2, region.getStartLineNumber());
 assertEquals(2, region.getEndLineNumber());
 assertEquals("line2", region.getCode());
 }

 // Test getRegion method with invalid line number
 @Test(expected = IllegalArgumentException.class)
 public void testGetRegionInvalidLineNumber() throws IOException {
 SourceFile sourceFile = new SourceFile("testFile.js");
 sourceFile.setCode("line1\nline2\nline3");
 Region region = sourceFile.getRegion(5);
 }

 // Test getRegion method with IOException
 @Test(expected = IOException.class)
 public void testGetRegionIOException() {
 SourceFile sourceFile = new SourceFile("testFile.js");
 sourceFile.setCode(null);
 Region region = sourceFile.getRegion(2);
 }

 // Test fromFile method with valid File and Charset
 @Test
 public void testSourceFileFromFileValid() throws IOException {
 File file = new File("testFile.js");
 SourceFile sourceFile = SourceFile.fromFile(file.getPath(), Charsets.UTF_8);
 assertEquals("testFile.js", sourceFile.getName());
 }

 // Test fromFile method with valid File and null Charset
 @Test
 public void testSourceFileFromFileValidNullCharset() throws IOException {
 File file = new File("testFile.js");
 SourceFile sourceFile = SourceFile.fromFile(file.getPath());
 assertEquals("testFile.js", sourceFile.getName());
 }

 // Test fromFile method with invalid File
 @Test(expected = IOException.class)
 public void testSourceFileFromFileInvalidFile() {
 File file = new File("invalidFile.js");
 SourceFile sourceFile = SourceFile.fromFile(file.getPath(), Charsets.UTF_8);
 }

 // Test fromCode method with valid fileName and code
 @Test
 public void testSourceFileFromCodeValid() {
 SourceFile sourceFile = SourceFile.fromCode("testFile.js", "code");
 assertEquals("testFile.js", sourceFile.getName());
 }

 // Test fromCode method with invalid fileName
 @Test(expected = IllegalArgumentException.class)
 public void testSourceFileFromCodeInvalidFileName() {
 SourceFile sourceFile = SourceFile.fromCode("", "code");
 }

 // Test fromCode method with null code
 @Test(expected = IllegalArgumentException.class)
 public void testSourceFileFromCodeInvalidCode() {
 SourceFile sourceFile = SourceFile.fromCode("testFile.js", null);
 }

 // Test fromInputStream method with valid fileName and InputStream
 @Test
 public void testSourceFileFromInputStreamValid() throws IOException {
 File file = new File("testFile.js");
 InputStream inputStream = Files.newInputStream(file.toPath());
 SourceFile sourceFile = SourceFile.fromInputStream("testFile.js", inputStream);
 assertEquals("testFile.js", sourceFile.getName());
 }

 // Test fromInputStream method with invalid fileName
 @Test(expected = IllegalArgumentException.class)
 public void testSourceFileFromInputStreamInvalidFileName() throws IOException {
 InputStream inputStream = Files.newInputStream(new File("testFile.js").toPath());
 SourceFile sourceFile = SourceFile.fromInputStream("", inputStream);
 }

 // Test fromInputStream method with null InputStream
 @Test(expected = IllegalArgumentException.class)
 public void testSourceFileFromInputStreamInvalidInputStream() throws IOException {
 File file = new File("testFile.js");
 SourceFile sourceFile = SourceFile.fromInputStream("testFile.js", null);
 }

 // Test fromReader method with valid fileName and Reader
 @Test
 public void testSourceFileFromReaderValid() throws IOException {
 File file = new File("testFile.js");
 Reader reader = Files.newReader(file.toPath(), Charsets.UTF_8);
 SourceFile sourceFile = SourceFile.fromReader("testFile.js", reader);
 assertEquals("testFile.js", sourceFile.getName());
 }

 // Test fromReader method with invalid fileName
 @Test(expected = IllegalArgumentException.class)
 public void testSourceFileFromReaderInvalidFileName() throws IOException {
 Reader reader = Files.newReader(new File("testFile.js").toPath(), Charsets.UTF_8);
 SourceFile sourceFile = SourceFile.fromReader("", reader);
 }

 // Test fromReader method with null Reader
 @Test(expected = IllegalArgumentException.class)
 public void testSourceFileFromReaderInvalidReader() throws IOException {
 File file = new File("testFile.js");
 SourceFile sourceFile = SourceFile.fromReader("testFile.js", null);
 }

 // Test fromGenerator method with valid fileName and Generator
 @Test
 public void testSourceFileFromGeneratorValid() {
 SourceFile sourceFile = SourceFile.fromGenerator("testFile.js", () -> "code");
 assertEquals("testFile.js", sourceFile.getName());
 }

 // Test fromGenerator method with invalid fileName
 @Test(expected = IllegalArgumentException.class)
 public void testSourceFileFromGeneratorInvalidFileName() {
 () -> {};
 SourceFile sourceFile = SourceFile.fromGenerator("", () -> "code");
 }

}