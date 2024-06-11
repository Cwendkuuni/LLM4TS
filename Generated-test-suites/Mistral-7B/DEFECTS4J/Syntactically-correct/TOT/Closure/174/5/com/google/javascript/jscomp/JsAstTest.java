import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.*;

public class JsAstTest {

 private AbstractCompiler compiler;
 private JsAst jsAst;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 compiler.init();
 CompilerOptions options = new CompilerOptions();
 jsAst = new JsAst(new SourceFile("test.js", "test.js"));
 }

 @Test
 public void testValidParse() {
 Node astRoot = jsAst.getAstRoot(compiler);
 assertNotNull(astRoot);
 }

 @Test
 public void testInvalidParse() {
 // Intentionally malformed code to trigger parse errors
 jsAst.setSourceFile(new SourceFile("testInvalid.js", "function x() { x = 5; }"));
 jsAst.parse(compiler);
 // Check that the AST is null to indicate parse failure
 assertNull(jsAst.getAstRoot(compiler));
 }

 @Test
 public void testClearAst() {
 // Parse valid code and store the AST
 Node validAst = jsAst.getAstRoot(compiler);
 // Clear the AST and ensure it is null
 jsAst.clearAst();
 assertNull(jsAst.getAstRoot(compiler));
 }
}