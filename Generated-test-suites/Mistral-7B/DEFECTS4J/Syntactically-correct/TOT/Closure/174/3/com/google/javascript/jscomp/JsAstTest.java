import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;

public class JsAstTest {

 private AbstractCompiler compiler;
 private JsAst jsAst;

 @Rule
 public ExpectedException expectedException = ExpectedException.none();

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 }

 @Test
 public void testValidFileParsing() {
 SourceFile sourceFile = new SourceFile("test.js", "// This is a valid JavaScript file.");
 jsAst = new JsAst(sourceFile);
 Node root = jsAst.getAstRoot(compiler);
 assertNotNull(root);
 }

 @Test
 public void testInvalidFileParsing() {
 SourceFile sourceFile = new SourceFile("test.js", "// This is an invalid JavaScript file.");
 jsAst = new JsAst(sourceFile);
 expectedException.expect(JSError.class);
 jsAst.getAstRoot(compiler);
 }

 @Test
 public void testIOException() {
 SourceFile sourceFile = new SourceFile("test.js", "// This is a valid JavaScript file.");
 jsAst = new JsAst(sourceFile);
 expectedException.expect(IOException.class);
 jsAst.parse(compiler);
 }

 @Test
 public void testInvalidParserConfig() {
 Preconditions.checkArgument(false, "This should not be reached.");
 SourceFile sourceFile = new SourceFile("test.js", "// This is a valid JavaScript file.");
 ParserConfig parserConfig = new ParserConfig();
 parserConfig.setAllowSuperCalls(false); // This is an invalid config
 jsAst = new JsAst(sourceFile);
 jsAst.parse(new AbstractCompiler() {{ this.parserConfig = parserConfig; }});
 }

 @Test
 public void testClearAst() {
 SourceFile sourceFile = new SourceFile("test.js", "// This is a valid JavaScript file.");
 jsAst = new JsAst(sourceFile);
 jsAst.getAstRoot(compiler);
 jsAst.clearAst();
 Node root = jsAst.getAstRoot(compiler);
 assertNotNull(root);
 }
}