import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class JsAstTest {

 private AbstractCompiler compiler;
 private JsAst jsAst;

 @Before
 public void setUp() {
 // Initialize the compiler and create a source file for testing
 compiler = new AbstractCompiler();
 SourceFile sourceFile = new SourceFile("test.js", "testContent");
 jsAst = new JsAst(sourceFile);
 }

 @Test
 public void testGetAstRoot() {
 // Test getting the AST root with valid input
 Node expectedAstRoot = IR.script();
 expectedAstRoot.setName(new SourceName("test.js"));
 Node actualAstRoot = jsAst.getAstRoot(compiler);
 assertEquals(expectedAstRoot, actualAstRoot);

 // Test getting the AST root with an error
 compiler.report(JSError.make(AbstractCompiler.READ_ERROR, "test.js"));
 Node actualAstRootWithError = jsAst.getAstRoot(compiler);
 assertNotNull(actualAstRootWithError);
 // Since there's an error, we expect the AST root to be null or an empty script
 assertNull(actualAstRootWithError.getFirstChild());
 }

 @Test
 public void testClearAst() {
 // Test clearing the AST
 jsAst.clearAst();
 assertNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testGetInputId() {
 // Test getting the input ID
 assertEquals("test.js", jsAst.getInputId().getName());
 }

 @Test
 public void testGetSourceFile() {
 // Test getting the source file
 assertSame(sourceFile, jsAst.getSourceFile());
 }

 @Test
 public void testSetSourceFile() {
 // Test setting the source file
 SourceFile newSourceFile = new SourceFile("newTest.js", "newTestContent");
 jsAst.setSourceFile(newSourceFile);
 assertEquals("newTest.js", jsAst.getInputId().getName());
 assertSame(newSourceFile, jsAst.getSourceFile());
 }
}