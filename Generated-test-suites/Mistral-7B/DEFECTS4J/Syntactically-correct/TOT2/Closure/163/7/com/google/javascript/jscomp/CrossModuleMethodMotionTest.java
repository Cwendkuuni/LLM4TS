import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;

public class CrossModuleMethodMotionTest {

  @Test
  public void testMoveMethod() {
    // Given
    Compiler compiler = new Compiler();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    Node root = new Node(); // initialize root node

    // When
    pass.process(null, root);

    // Then
    Node originalMethod = root.getFirstChild(); // assume original method exists
    Node stubFunction = root.getChildByPredicate(n -> n.isFunction() && n.getString().equals("JSCompiler_stubMethod")); // assume stub function is created
    Node unstubFunction = root.getChildByPredicate(n -> n.isCallExpression() && n.getFirstChild().getString().equals("JSCompiler_unstubMethod")); // assume unstub function is created

    // Typical use case
    Node callOriginalMethod = IR.call(originalMethod, IR.emptyArray()).copyInformationFromForTree(new Node(Node.CALL, null, IR.emptyArray()));
    Node result = IR.call(stubFunction, IR.number(0)).apply(null, new Object[]{null});
    assertEquals(result.getValue().toString(), callOriginalMethod.getValue().toString());

    // Edge case 1: null deepestCommonModuleRef
    Node nullDeepestCommonModule = new Node(Node.ASSIGN, null, new Node(Node.NULL), new Node(Node.IDENT, "deepestCommonModuleRef"));
    Node nullMethod = new Node(Node.ASSIGN, null, new Node(Node.NULL), new Node(Node.IDENT, "method"));
    Node nullMethodDecl = new Node(Node.ASSIGN, null, nullDeepestCommonModule, new Node(Node.PROP, nullDeepestCommonModule, "method"));
    Node nullDeclStatement = new Node(Node.ASSIGN, null, nullMethodDecl, new Node(Node.IDENT, "method"));
    root.addChildToFront(nullDeclStatement);
    pass.process(null, root);
    compiler.reportCodeChange();
    Node nullMethodCall = IR.call(stubFunction, IR.number(0)).apply(null, new Object[]{nullDeepestCommonModuleRef});
    assertNull(nullMethodCall.getValue());

    // Edge case 2: duplicate method with the same name in different modules
    Node duplicateMethod = new Node(Node.ASSIGN, null, new Node(Node.IDENT, "method"), new Node(Node.FUNCTION, null, null, null, null));
    Node duplicateMethodDeclaration = new Node(Node.ASSIGN, null, new Node(Node.IDENT, "method"), duplicateMethod);
    root.addChildToFront(duplicateMethodDeclaration);
    pass.process(null, root);
    compiler.reportCodeChange();
    Node duplicateMethodCall = IR.call(stubFunction, IR.number(0)).apply(null, new Object[]{null});
    assertSame(duplicateMethod.getParent(), duplicateMethodCall.getParent());

    // Error scenario: non-function property
    Node nonFunctionProperty = new Node(Node.ASSIGN, null, new Node(Node.IDENT, "property"), new Node(Node.NUMBER, 42));
    Node nonFunctionPropertyDeclaration = new Node(Node.ASSIGN, null, new Node(Node.IDENT, "property"), nonFunctionProperty);
    root.addChildToFront(nonFunctionPropertyDeclaration);
    try {
      pass.process(null, root);
      fail();
    } catch (AssertionError e) {
      // Expected
    }
  }
}