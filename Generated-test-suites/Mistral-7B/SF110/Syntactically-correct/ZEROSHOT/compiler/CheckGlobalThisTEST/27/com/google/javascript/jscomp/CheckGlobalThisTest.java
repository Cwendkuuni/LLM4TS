import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static com.google.javascript.jscomp.NodeTraversal.AbstractTraversal.create;
import static com.google.javascript.jscomp.NodeTraversal.Callback.TraverseKind.Traverse;
import static com.google.javascript.rhino.JavaScriptContext.enterContext;
import static com.google.javascript.rhino.JavaScriptContext.exitContext;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private Checker checker;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    CompilerOptions options = new CompilerOptions(null);
    compiler = new AbstractCompiler(options);
    checker = new Checker(compiler, null);
  }

  @Test
  public void testCheckGlobalThis() {
    String code = "function evil() { this.useful = undefined; }";
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(1));

    Node diagnosticNode = diagnostics.get(0);
    DiagnosticType diagnosticType = diagnosticNode.getDiagnosticType();
    assertThat(diagnosticType, is(GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThisInConstructor() {
    String code = "function EvilConstructor() { this.useful = undefined; }";
    SourceFile sourceFile = new SourceFile("EvilConstructor.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(0));
  }

  @Test
  public void testCheckGlobalThisInPrototypeMethod() {
    String code = "function EvilPrototype() {} EvilPrototype.prototype.evil = function() { this.useful = undefined; }";
    SourceFile sourceFile = new SourceFile("EvilPrototype.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(0));
  }

  @Test
  public void testCheckGlobalThisInFunctionWithNoPlaceForThisAnnotation() {
    String code = "function evil() { var a = function() { this.useful = undefined; } }";
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(1));

    Node diagnosticNode = diagnostics.get(0);
    DiagnosticType diagnosticType = diagnosticNode.getDiagnosticType();
    assertThat(diagnosticType, is(GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThisInFunctionWithNoGlobalContext() {
    String code = "function evil() { this.useful = undefined; } function good() { }";
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(0));
  }

  @Test
  public void testCheckGlobalThisInFunctionWithPrototypeLentToIt() {
    String code = "function EvilPrototype() {} EvilPrototype.prototype.evil = function() { this.useful = undefined; }; var good = { prototype: { evil: function() { } } };";
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(0));
  }

  @Test
  public void testCheckGlobalThisInFunctionWithPrototypePropertyAccess() {
    String code = "function EvilPrototype() {} EvilPrototype.prototype.evil = function() { this.prototype.useful = undefined; };";
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(1));

    Node diagnosticNode = diagnostics.get(0);
    DiagnosticType diagnosticType = diagnosticNode.getDiagnosticType();
    assertThat(diagnosticType, is(GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThisInFunctionWithPrototypePropertyAccessThroughGet() {
    String code = "function EvilPrototype() {} EvilPrototype.prototype.evil = function() { this.getPrototype().useful = undefined; };";
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(1));

    Node diagnosticNode = diagnostics.get(0);
    DiagnosticType diagnosticType = diagnosticNode.getDiagnosticType();
    assertThat(diagnosticType, is(GLOBAL_THIS));
  }

  @Test
  public void testCheckGlobalThisInFunctionWithPrototypePropertyAccessThroughGetWithChain() {
    String code = "function EvilPrototype() {} EvilPrototype.prototype.evil = function() { this.getPrototype().getPrototype().useful = undefined; };";
    SourceFile sourceFile = new SourceFile("Test.js", code);
    Node rootNode = compiler.parseTree(sourceFile, null);

    NodeTraversal traverser = create(checker, rootNode, null);
    traverser.traverse(Traverse);
    List<Node> diagnostics = checker.getDiagnostics();
    assertThat(diagnostics, is(instanceOf(java.util.ArrayList.class)));
    assertThat(diagnostics.size(), is(1));

    Node diagnosticNode = diagnostics.get(0);
    DiagnosticType diagnosticType = diagnosticNode.getDiagnosticType();
    assertThat(diagnosticType, is(GLOBAL_THIS));
  }
}