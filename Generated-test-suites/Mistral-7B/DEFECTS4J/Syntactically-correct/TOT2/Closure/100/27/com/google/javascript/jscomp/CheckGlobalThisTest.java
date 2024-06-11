import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.TokenType.*;

import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    // Given: A compiler instance and a check level
    Compiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    // When: Traversing a node tree with global 'this' usage
    Node rootNode = createNode("function evil() { this.useful = undefined; }");
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // Then: A warning should be reported
    DiagnosticType warning = CHECKGLOBALTHIS.GLOBAL_THIS;
    assertEquals(1, compiler.getDiagnostics().length);
    assertEquals(warning, compiler.getDiagnostics()[0].getType());
  }

  @org.junit.Test
  public void testCheckGlobalThis_Constructor() {
    // Given: A compiler instance and a check level
    Compiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    // When: Traversing a node tree with a constructor function
    Node rootNode = createNode("function MyConstructor() {}");
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // Then: No warnings should be reported
    assertEquals(0, compiler.getDiagnostics().length);
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    // Given: A compiler instance and a check level
    Compiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    // When: Traversing a node tree with a function annotated with @this
    Node rootNode = createNode("/** @this MyType */ function MyFunction() {}");
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // Then: No warnings should be reported
    assertEquals(0, compiler.getDiagnostics().length);
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithNoLogicalPlaceForThisAnnotation() {
    // Given: A compiler instance and a check level
    Compiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    // When: Traversing a node tree with a function where there's no logical place to put a @this annotation
    Node rootNode = createNode("function MyFunction() { this.property = 42; }");
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // Then: No warnings should be reported
    assertEquals(0, compiler.getDiagnostics().length);
  }

  @org.junit.Test
  public void testCheckGlobalThis_FunctionWithAssignmentToVariable() {
    // Given: A compiler instance and a check level
    Compiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, WARNING);

    // When: Traversing a node tree with a function where 'this' is assigned to a variable
    Node rootNode = createNode("function evil() { var a = this; }");
    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    // Then: No warnings should be reported
    assertEquals(0, compiler.getDiagnostics().length);
  }

  private Node createNode(String code) {
    return RhinoCommon.parseBlock(code, null);
  }

  
  
}