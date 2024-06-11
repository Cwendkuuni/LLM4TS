import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.parser.ErrorReporter;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private ErrorReporter errorReporter;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    errorReporter = compiler.getDefaultErrorReporter();
    sourceFile = new SourceFile("test.js", "test code");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testParseValidCode() {
    String validCode = "function foo() { return 42; }";
    sourceFile.setCode(validCode);
    jsAst.parse(compiler);

    Node root = jsAst.getAstRoot(compiler);
    assertEquals(SCRIPT, root.getType());
    assertNotNull(root.getChild(0));

    Node functionNode = root.getChild(0);
    assertEquals(FUNCTION_DECLARATION, functionNode.getType());
    assertEquals("foo", functionNode.getIdentifier().getName());
    assertEquals(1, functionNode.getChildCount());

    Node returnNode = functionNode.getChild(0);
    assertEquals(RETURN_STATEMENT, returnNode.getType());
    assertEquals(NUMBER_LITERAL, returnNode.getChild(0).getType());
    assertEquals(42, ((Number) returnNode.getChild(0).getValue()).doubleValue());
  }

  @Test
  public void testParseWithErrors() {
    String errorCode = "function foo() { return 42";
    sourceFile.setCode(errorCode);

    jsAst.parse(compiler);

    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(JSError.Type.MISSING_BRACE, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testClearAst() {
    jsAst.parse(compiler);
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();

    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testSetSourceFile() {
    String newCode = "function bar() { return 43; }";
    sourceFile.setCode(newCode);
    jsAst.parse(compiler);

    assertEquals("test.js", jsAst.getSourceFile().getName());

    SourceFile newSourceFile = new SourceFile("test2.js", "test code 2");
    jsAst.setSourceFile(newSourceFile);

    assertEquals("test2.js", jsAst.getSourceFile().getName());
  }
}