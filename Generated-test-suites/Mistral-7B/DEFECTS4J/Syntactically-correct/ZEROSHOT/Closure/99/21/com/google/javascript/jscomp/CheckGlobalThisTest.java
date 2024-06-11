import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilationResult;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DeclarationChecker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.parser.bare.BareParser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private CompilationResult compile(String jsCode) {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CheckLevel.WARNING);

    Parser parser = new BareParser();
    AbstractCompiler compiler = new DeclarationChecker(new Checker(), options);

    Node rootNode = parser.parse(jsCode, "Test", null);
    NodeFactory nodeFactory = new NodeFactory();

    NodeTraversal traversal = new NodeTraversal(rootNode, compiler);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    traversal.traverse(checker);

    return compiler.getResult();
  }

  @Test
  public void testSafeUsesOfThis() {
    String jsCode = "function Foo() {}";
    CompilationResult result = compile(jsCode);
    assertFalse(result.getDiagnostics().hasError());
  }

  @Test
  public void testUnsafeUseOfThisOnLeftSideOfAssignment() {
    String jsCode = "var a = this;";
    CompilationResult result = compile(jsCode);
    assertTrue(result.getDiagnostics().hasError());
    assertEquals(1, result.getDiagnostics().getErrorCount());
  }

  @Test
  public void testUnsafeUseOfThisInPrototypePropertyAccess() {
    String jsCode = "var obj = { get x() { return this; } }; obj.x.y = 42;";
    CompilationResult result = compile(jsCode);
    assertTrue(result.getDiagnostics().hasError());
    assertEquals(1, result.getDiagnostics().getErrorCount());
  }

  @Test
  public void testUnsafeUseOfThisInPrototypeMethod() {
    String jsCode = "function Foo() {} Foo.prototype.x = function() { return this; };";
    CompilationResult result = compile(jsCode);
    assertFalse(result.getDiagnostics().hasError());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotation() {
    String jsCode = "function @constructor Foo() {}";
    CompilationResult result = compile(jsCode);
    assertFalse(result.getDiagnostics().hasError());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotationWithName() {
    String jsCode = "function @constructor Foo() {} function Foo() {}";
    CompilationResult result = compile(jsCode);
    assertFalse(result.getDiagnostics().hasError());
  }

  @Test
  public void testSafeUseOfThisInFunctionAnnotationWithNoName() {
    String jsCode = "function @constructor() {}";
    CompilationResult result = compile(jsCode);
    assertFalse(result.getDiagnostics().hasError());
  }

  @Test
  public void testSafeUseOfThisInFunctionWithNoAnnotation() {
    String jsCode = "function Foo() {} Foo.prototype.x = function() { return this; };";
    CompilationResult result = compile(jsCode);
    assertFalse(result.getDiagnostics().hasError());
  }

  @Test
  public void testSafeUseOfThisInAssignmentRightSide() {
    String jsCode = "var a = function() { return this; }; a = this;";
    CompilationResult result = compile(jsCode);
    assertFalse(result.getDiagnostics().hasError());
  }

  @Test
  public void testSafeUseOfThisInObjectLiterals() {
    String jsCode = "var obj = { x: function() { return this; } };";
    CompilationResult result = compile(jsCode);
    assertFalse(result.getDiagnostics().hasError());
  }

  private int getDiagnosticsErrorCount(Diagnostics diagnostics) {
    return diagnostics.getErrorCount();
  }

  private boolean hasError(Diagnostics diagnostics) {
    return diagnostics.hasError();
  }

  private DiagnosticType getWarningType(String warningTypeId) {
    return DiagnosticType.warning(warningTypeId, "");
  }
}