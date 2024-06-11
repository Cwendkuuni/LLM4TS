import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsAstTest {

 private AbstractCompiler compiler;
 private JsAst jsAst;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 String sourceCode = "function test() { return 42; }";
 SourceFile sourceFile = new SourceFile("test.js", sourceCode);
 jsAst = new JsAst(sourceFile);
 }

 @Test
 public void testValidParse() {
 Node root = jsAst.getAstRoot(compiler);
 assertEquals(IR.script(), root);
 }

 @Test
 public void testInvalidParse() {
 SourceFile sourceFile = new SourceFile("invalid.js", "invalid code");
 jsAst = new JsAst(sourceFile);
 assertNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testClearAst() {
 Node root = jsAst.getAstRoot(compiler);
 jsAst.clearAst();
 assertNull(jsAst.getAstRoot(compiler));
 }

 @Test(expected = JSError.class)
 public void testReadError() throws Exception {
 SourceFile sourceFile = new SourceFile("readError.js", null);
 jsAst = new JsAst(sourceFile);
 }

 @Test
 public void testSetSourceFile() {
 String newSourceCode = "function test() { return 43; }";
 SourceFile newSourceFile = new SourceFile("test.js", newSourceCode);
 jsAst.setSourceFile(newSourceFile);
 assertEquals(newSourceCode, jsAst.getSourceFile().getCode());
 }
}