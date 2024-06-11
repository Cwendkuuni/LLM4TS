import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsCode = "function evil() { this.useful = undefined; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, WARNING).traverse(traverser, rootNode, null);

    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("No warnings expected", 1, numWarnings);
    DiagnosticType warning = compiler.getDiagnostics(0);
    assertEquals("dangerous use of the global 'this' object", warning.getMessage());
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String jsCode = "function MyConstructor() { this.myProperty = 'constructor'; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, WARNING).traverse(traverser, rootNode, null);

    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("No warnings expected", 0, numWarnings);
  }

  @Test
  public void testCheckGlobalThis_Prototype() {
    String jsCode = "function MyPrototype() {} MyPrototype.prototype.myMethod = function() { this.myProperty = 'prototype'; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, WARNING).traverse(traverser, rootNode, null);

    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("No warnings expected", 0, numWarnings);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    String jsCode = "function evil() { this.useful = undefined; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(ERROR, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, ERROR).traverse(traverser, rootNode, null);

    int numErrors = compiler.getNumErrors();
    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("One error expected", 1, numErrors);
    assertEquals("No warnings expected", 0, numWarnings);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignment() {
    String jsCode = "var a = function() { this.useful = undefined; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, WARNING).traverse(traverser, rootNode, null);

    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("No warnings expected", 0, numWarnings);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypeProperty() {
    String jsCode = "function evil() { this.prototype.useful = undefined; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, WARNING).traverse(traverser, rootNode, null);

    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("No warnings expected", 0, numWarnings);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypeSubproperty() {
    String jsCode = "function evil() { this.prototype.subProperty.useful = undefined; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, WARNING).traverse(traverser, rootNode, null);

    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("No warnings expected", 0, numWarnings);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    String jsCode = "function MyFunction@this() { this.myProperty = 'this'; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, WARNING).traverse(traverser, rootNode, null);

    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("No warnings expected", 0, numWarnings);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithOverriddenAnnotation() {
    String jsCode = "function MyFunction@override@this() { this.myProperty = 'this'; }";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setWarningLevel(WARNING, CheckGlobalThis.GLOBAL_THIS);
    AbstractCompiler compiler = new AbstractCompiler(options);
    Checker checker = new Checker(compiler, null);

    Node rootNode = checker.parse(jsCode, null, null);
    NodeTraversal traverser = new NodeTraversal(checker, rootNode);
    new CheckGlobalThis(compiler, WARNING).traverse(traverser, rootNode, null);

    int numWarnings = compiler.getNumDiagnostics();
    assertEquals("No warnings expected", 0, numWarnings);
  }
}