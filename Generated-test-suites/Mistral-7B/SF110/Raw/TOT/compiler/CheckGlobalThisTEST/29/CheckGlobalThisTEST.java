import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUsesOfThis() {
    Compiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Test prototype method
    Node prototypeMethodNode = createNode("function Prototype() { this.prototypeMethod = 'safe'; }");
    NodeTraversal prototypeMethodTraversal = new NodeTraversal(prototypeMethodNode, checkGlobalThis);
    prototypeMethodTraversal.traverse();
    assertEquals(0, checkGlobalThis.getNumDiagnostics());

    // Test constructor
    Node constructorNode = createNode("function Constructor() { this.constructorMethod = 'safe'; }");
    NodeTraversal constructorTraversal = new NodeTraversal(constructorNode, checkGlobalThis);
    constructorTraversal.traverse();
    assertEquals(0, checkGlobalThis.getNumDiagnostics());

    // Test @this annotation
    Node thisAnnotationNode = createNode("function ThisAnnotated() { @this this.thisAnnotatedMethod = 'safe'; }");
    NodeTraversal thisAnnotationTraversal = new NodeTraversal(thisAnnotationNode, checkGlobalThis);
    thisAnnotationTraversal.traverse();
    assertEquals(0, checkGlobalThis.getNumDiagnostics());

    // Test no logical place to put a this annotation
    Node noThisAnnotationNode = createNode("function NoThisAnnotation() { this.noThisAnnotationMethod = 'safe'; }");
    NodeTraversal noThisAnnotationTraversal = new NodeTraversal(noThisAnnotationNode, checkGlobalThis);
    noThisAnnotationTraversal.traverse();
    assertEquals(0, checkGlobalThis.getNumDiagnostics());
  }

  @Test
  public void testUnsafeUsesOfThis() {
    Compiler compiler = new Compiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    // Test assignment to this on left side
    Node unsafeAssignmentNode = createNode("function UnsafeAssignment() { this = 'unsafe'; }");
    NodeTraversal unsafeAssignmentTraversal = new NodeTraversal(unsafeAssignmentNode, checkGlobalThis);
    unsafeAssignmentTraversal.traverse();
    assertEquals(1, checkGlobalThis.getNumDiagnostics());
    assertEquals("JSC_USED_GLOBAL_THIS", checkGlobalThis.getDiagnostics().get(0).getType().getName());

    // Test assignment to this on right side of property access
    Node unsafePropertyAccessNode = createNode("function UnsafePropertyAccess() { this.unsafeProperty = 'unsafe'; }");
    NodeTraversal unsafePropertyAccessTraversal = new NodeTraversal(unsafePropertyAccessNode, checkGlobalThis);
    unsafePropertyAccessTraversal.traverse();
    assertEquals(1, checkGlobalThis.getNumDiagnostics());
    assertEquals("JSC_USED_GLOBAL_THIS", checkGlobalThis.getDiagnostics().get(0).getType().getName());
  }

  private Node createNode(String code) {
    Node rootNode = new Node(Token.SCRIPT, 0, code);
    Compiler compiler = new Compiler();
    NodeTraversal traversal = new NodeTraversal(rootNode, null);
    compiler.process(rootNode, traversal);
    return rootNode;
  }

  private void assertEquals(int expected, int actual) {
    int result = checkGlobalThis.getNumDiagnostics();
    if (expected != result) {
      fail("Expected " + expected + ", but got " + result);
    }
  }
}