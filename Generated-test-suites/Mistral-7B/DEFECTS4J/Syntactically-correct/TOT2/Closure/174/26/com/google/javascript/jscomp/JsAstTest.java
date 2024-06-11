import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

 private AbstractCompiler compiler;
 private JsAst jsAst;
 private SourceFile sourceFile;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 sourceFile = new SourceFile("test.js", "test code");
 jsAst = new JsAst(sourceFile);
 }

 @Test
 public void testGetAstRoot() {
 Node astRoot = jsAst.getAstRoot(compiler);
 assertNotNull(astRoot);
 }

 @Test
 public void testClearAst() {
 jsAst.clearAst();
 assertNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testParseWithValidInput() {
 jsAst.parse(compiler);
 assertNotNull(jsAst.getAstRoot(compiler));
 }

 @Test(expected = IOException.class)
 public void testParseWithIOException() {
 jsAst.parse(compiler);
 compiler.reportError("expected IOException");
 jsAst.parse(compiler); // should throw exception
 }

 @Test
 public void testSetSourceFile() {
 SourceFile newSourceFile = new SourceFile("newFile.js", "new code");
 jsAst.setSourceFile(newSourceFile);
 assertEquals(newSourceFile.getName(), jsAst.getInputId().getName());
 }
}