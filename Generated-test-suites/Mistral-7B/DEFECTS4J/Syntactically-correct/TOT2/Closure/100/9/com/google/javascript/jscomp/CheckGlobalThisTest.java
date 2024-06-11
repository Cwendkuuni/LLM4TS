import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  @Test
  public void testCheckGlobalThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);

    // Test a simple function that uses 'this' on the left side of an assignment
    Node functionNode = new Node(Token.FUNCTION, null);
    Node assignNode = new Node(Token.ASSIGN, null);
    Node lhs = new Node(Token.THIS, null);
    Node rhs = new Node(Token.IDENT, "result");
    assignNode.addChildrenToEnd(lhs, rhs);
    functionNode.addChildrenToEnd(assignNode);
    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildrenToEnd(functionNode);

    NodeTraversal traversal = new NodeTraversal(rootNode, checkGlobalThis);
    traversal.traverse();

    assertTrue(checkGlobalThis.shouldReportThis(lhs, functionNode));
    assertEquals(1, checkGlobalThis.compiler.getDiagnostics().size());
    assertNotNull(checkGlobalThis.compiler.getDiagnostics().get(0).getMessage());

    // Test a function that is annotated with @constructor
    JSDocInfo constructorAnnotation = new JSDocInfo();
    constructorAnnotation.setConstructor(true);
    functionNode.setJSDocInfo(constructorAnnotation);

    NodeTraversal traversal2 = new NodeTraversal(rootNode, checkGlobalThis);
    traversal2.traverse();

    assertFalse(checkGlobalThis.shouldReportThis(lhs, functionNode));
    assertEquals(0, checkGlobalThis.compiler.getDiagnostics().size());

    // Test a function that is annotated with @this
    JSDocInfo thisAnnotation = new JSDocInfo();
    thisAnnotation.setThisType(true);
    functionNode.setJSDocInfo(thisAnnotation);

    NodeTraversal traversal3 = new NodeTraversal(rootNode, checkGlobalThis);
    traversal3.traverse();

    assertFalse(checkGlobalThis.shouldReportThis(lhs, functionNode));
    assertEquals(0, checkGlobalThis.compiler.getDiagnostics().size());

    // Test a function that uses 'this' in a property access
    Node lhsPropertyAccess = new Node(Token.GETPROP, null, null, new Node(Token.THIS, null), new Node(Token.IDENT, "property"));
    Node rhsPropertyAccess = new Node(Token.IDENT, "value");
    assignNode = new Node(Token.ASSIGN, null);
    assignNode.addChildrenToEnd(lhsPropertyAccess, rhsPropertyAccess);
    functionNode.addChildrenToEnd(assignNode);

    NodeTraversal traversal4 = new NodeTraversal(rootNode, checkGlobalThis);
    traversal4.traverse();

    assertTrue(checkGlobalThis.shouldReportThis(lhsPropertyAccess, functionNode));
    assertEquals(1, checkGlobalThis.compiler.getDiagnostics().size());
    assertNotNull(checkGlobalThis.compiler.getDiagnostics().get(0).getMessage());
  }
}