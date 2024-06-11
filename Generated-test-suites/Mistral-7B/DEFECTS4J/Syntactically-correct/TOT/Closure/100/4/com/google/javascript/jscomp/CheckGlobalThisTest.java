import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String code = "function evil() { this.useful = undefined; }";

    Checker compiler = new Compiler().createChecker(Checker.Type.SIMPLE_CHECKS, new Diagnostics());
    SourceFile source = new SourceFile("Test.js", code);
    Node root = source.getAST();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    Diagnostics diagnostics = checker.getDiagnostics();
    assertThat(diagnostics.getNumberOfErrors(), is(1));
    assertThat(diagnostics.getDiagnostics()[0].getMessage(), is("dangerous use of the global 'this' object"));
  }

  @Test
  public void testCheckGlobalThis_Prototype() {
    String code = "var o = {}; o.evil = function() { this.useful = undefined; }";

    Checker compiler = new Compiler().createChecker(Checker.Type.SIMPLE_CHECKS, new Diagnostics());
    SourceFile source = new SourceFile("Test.js", code);
    Node root = source.getAST();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    Diagnostics diagnostics = checker.getDiagnostics();
    assertThat(diagnostics.getNumberOfErrors(), is(0));
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String code = "function EvilConstructor() { this.useful = undefined; }";

    Checker compiler = new Compiler().createChecker(Checker.Type.SIMPLE_CHECKS, new Diagnostics());
    SourceFile source = new SourceFile("Test.js", code);
    Node root = source.getAST();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    Diagnostics diagnostics = checker.getDiagnostics();
    assertThat(diagnostics.getNumberOfErrors(), is(0));
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithThis() {
    String code = "/** @this {Object} */ function EvilFunction() { this.useful = undefined; }";

    Checker compiler = new Compiler().createChecker(Checker.Type.SIMPLE_CHECKS, new Diagnostics());
    SourceFile source = new SourceFile("Test.js", code);
    Node root = source.getAST();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    Diagnostics diagnostics = checker.getDiagnostics();
    assertThat(diagnostics.getNumberOfErrors(), is(0));
  }

  @Test
  public void testCheckGlobalThis_NoLogicalPlaceToPutThisAnnotation() {
    String code = "function evil() { this.useful = function() {}.useful = undefined; }";

    Checker compiler = new Compiler().createChecker(Checker.Type.SIMPLE_CHECKS, new Diagnostics());
    SourceFile source = new SourceFile("Test.js", code);
    Node root = source.getAST();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    Diagnostics diagnostics = checker.getDiagnostics();
    assertThat(diagnostics.getNumberOfErrors(), is(0));
  }

  @Test
  public void testCheckGlobalThis_AssignmentToVariableOrObject() {
    String code = "var a = this; a.useful = undefined;";

    Checker compiler = new Compiler().createChecker(Checker.Type.SIMPLE_CHECKS, new Diagnostics());
    SourceFile source = new SourceFile("Test.js", code);
    Node root = source.getAST();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    Diagnostics diagnostics = checker.getDiagnostics();
    assertThat(diagnostics.getNumberOfErrors(), is(0));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypeProperty() {
    String code = "function EvilFunction() { this.prototype.useful = undefined; }";

    Checker compiler = new Compiler().createChecker(Checker.Type.SIMPLE_CHECKS, new Diagnostics());
    SourceFile source = new SourceFile("Test.js", code);
    Node root = source.getAST();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    Diagnostics diagnostics = checker.getDiagnostics();
    assertThat(diagnostics.getNumberOfErrors(), is(0));
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypeSubProperty() {
    String code = "function EvilFunction() { this.prototype.sub.useful = undefined; }";

    Checker compiler = new Compiler().createChecker(Checker.Type.SIMPLE_CHECKS, new Diagnostics());
    SourceFile source = new SourceFile("Test.js", code);
    Node root = source.getAST();

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    Diagnostics diagnostics = checker.getDiagnostics();
    assertThat(diagnostics.getNumberOfErrors(), is(0));
  }
}