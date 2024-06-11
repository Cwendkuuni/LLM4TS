import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SimpleSourceFile;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test cases for the {@code CheckGlobalThis} class.
 */
public class CheckGlobalThisTest {

  @Test
  public void testBasic() {
    String jsCode = "function evil() { this.useful = undefined; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[0];
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               hasItem(is(instanceOf(diagnosticType))));
  }

  @Test
  public void testFunctionDeclaration() {
    String jsCode = "function evil() { this.useful = undefined; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[0];
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               hasItem(is(instanceOf(diagnosticType))));
  }

  @Test
  public void testFunctionExpression() {
    String jsCode = "var evil = function() { this.useful = undefined; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.VAR, 0)[0];
    assertThat(node.getChildNodesByType(Token.FUNCTION, 0)[0].getFirstChild().getDiagnosticMessages(),
               hasItem(is(instanceOf(CheckGlobalThis.GLOBAL_THIS))));
  }

  @Test
  public void testFunctionWithThisAnnotation() {
    String jsCode = "function evil@this() { this.useful = undefined; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[0];
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               not(hasItem(is(instanceOf(diagnosticType)))));
  }

  @Test
  public void testFunctionConstructor() {
    String jsCode = "function Evil() { this.useful = undefined; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[0];
    JSDocInfo jsDocInfo = node.getJSDocInfo();
    assertThat(jsDocInfo, is(instanceOf(JSDocInfo.Constructor)));
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               not(hasItem(is(instanceOf(CheckGlobalThis.GLOBAL_THIS)))));
  }

  @Test
  public void testPrototypeMethod() {
    String jsCode = "function Foo() {} Foo.prototype.bar = function() { this.useful = undefined; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[1];
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               not(hasItem(is(instanceOf(diagnosticType)))));
  }

  @Test
  public void testFunctionWithMultipleAssignments() {
    String jsCode = "function evil() { this.useful = undefined; a = this; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[0];
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               hasItem(is(instanceOf(diagnosticType))));
  }

  @Test
  public void testFunctionWithMultipleThisUses() {
    String jsCode = "function evil() { this.useful = undefined; this.anotherUseful = undefined; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[0];
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               hasItem(is(instanceOf(diagnosticType))));
  }

  @Test
  public void testFunctionWithNoAnnotationOrPrototype() {
    String jsCode = "function evil() { this.useful = undefined; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[0];
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               hasItem(is(instanceOf(diagnosticType))));
  }

  @Test
  public void testFunctionWithNoAssignmentOrPropertyAccess() {
    String jsCode = "function evil() { this; }";
    SimpleSourceFile sourceFile = new SimpleSourceFile("Test.js", jsCode);

    Compiler compiler = new Compiler();
    Checker checker = new Checker(compiler);
    checker.check(sourceFile, new CheckGlobalThis(compiler, CheckLevel.WARNING));

    Node node = sourceFile.getTree().getChildNodesByType(Token.FUNCTION, 0)[0];
    DiagnosticType diagnosticType = CheckGlobalThis.GLOBAL_THIS;
    assertThat(node.getFirstChild().getDiagnosticMessages(),
               not(hasItem(is(instanceOf(diagnosticType)))));
  }

  // Helper method for testing the existence of a diagnostic message in a list.
  private static <T> boolean hasItem(Iterable<? extends T> iterable, T item) {
    return iterable.contains(item);
  }
}