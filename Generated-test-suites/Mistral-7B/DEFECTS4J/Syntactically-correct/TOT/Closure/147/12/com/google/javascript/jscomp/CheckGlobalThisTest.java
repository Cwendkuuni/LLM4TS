import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilerResult;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.parser.bare.Parser;
import com.google.javascript.rhino.common.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Node node;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    compiler = new Compiler();
    sourceFile = new SourceFile("Test.js");
  }

  @Test
  public void testUnsafeAssignment() {
    String code = "this = 10;";
    node = Parser.parse(code, sourceFile).getFirstChild();
    CompilerResult result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());

    code = "(function(){ this = 10; })();";
    node = Parser.parse(code, sourceFile).getFirstChild();
    result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());

    code = "function evil(){ this = 10; }";
    node = Parser.parse(code, sourceFile).getFirstChild();
    result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());

    code = "function evil(){ var a = this; this.useful = 10; }";
    node = Parser.parse(code, sourceFile).getFirstChild();
    result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());
  }

  @Test
  public void testSafeAssignment() {
    String code = "function foo(){ var a = this; }";
    node = Parser.parse(code, sourceFile).getFirstChild();
    CompilerResult result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());

    code = "(function(){ var a = this; })();";
    node = Parser.parse(code, sourceFile).getFirstChild();
    result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());
  }

  @Test
  public void testPrototypeAccess() {
    String code = "function foo(){ this.prototype.x = 10; }";
    node = Parser.parse(code, sourceFile).getFirstChild();
    CompilerResult result = compiler.process(node, CheckLevel.WARNING);
    assertFalse(result.getDiagnostics().isEmpty());
    DiagnosticType diagnostic = result.getDiagnostics().get(0);
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType().getName());
  }

  @Test
  public void testSafePrototypeAccess() {
    String code = "function Foo(){}; Foo.prototype.x = 10;";
    node = Parser.parse(code, sourceFile).getFirstChild();
    CompilerResult result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithJSDoc() {
    String code = "/**\n * @constructor\n */\nfunction MyConstructor(){ this.x = 10; }";
    node = Parser.parse(code, sourceFile).getFirstChild();
    CompilerResult result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String code = "function MyFunction(@this) { this.x = 10; }";
    node = Parser.parse(code, sourceFile).getFirstChild();
    CompilerResult result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());
  }

  @Test
  public void testFunctionWithNoAnnotation() {
    String code = "function MyFunction() { this.x = 10; }";
    node = Parser.parse(code, sourceFile).getFirstChild();
    CompilerResult result = compiler.process(node, CheckLevel.WARNING);
    assertTrue(result.getDiagnostics().isEmpty());
  }

  private void assertEquals(Object expected, Object actual) {
    if (expected instanceof DiagnosticType && actual instanceof DiagnosticType) {
      assertEquals(((DiagnosticType)expected).getType(),
          ((DiagnosticType)actual).getType());
    } else {
      super.assertEquals(expected, actual);
    }
  }
}