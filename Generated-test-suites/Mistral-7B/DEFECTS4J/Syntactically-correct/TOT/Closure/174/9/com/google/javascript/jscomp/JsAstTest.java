import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.test.ParseTests.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setup() throws Exception {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "test");
  }

  @Test
  public void testValidJavaScriptFile() {
    String validCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    Node root = jsAst.getAstRoot(compiler);
    Node functionNode = assertCompilesToFunctionDeclaration(root, "test");
    Node body = functionNode.getFirstChild();
    Node returnNode = body.getLastChild();
    assertEquals(1, returnNode.getFirstChild().getNumber().intValueExact());
  }

  @Test
  public void testInvalidJavaScriptFile() {
    String invalidCode = "function test() {\n" +
        "  var x = 1;\n" +
        "} someRandomText";

    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    assertTrue(compiler.hasHaltingErrors());
  }

  @Test
  public void testParseWithOldAst() {
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    sourceFile.setCode(code);
    jsAst = new JsAst(sourceFile);

    Node oldAst = jsAst.getAstRoot(compiler);
    Node newAstRoot = jsAst.getAstRoot(compiler);

    assertNotSame(oldAst, newAstRoot);
    assertEquals(oldAst.getChildCount(), newAstRoot.getChildCount());
  }

  @Test
  public void testClearAst() {
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";

    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);

    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node newAstRoot = jsAst.getAstRoot(compiler);

    assertNull(astRoot);
    assertNull(newAstRoot);
  }
}