import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    // Given a simple NodeTraversal instance
    AbstractCompiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = IR.parse("{}"); // Empty JavaScript object

    // When processing the root Node
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.traverse(checkSideEffects);

    // Then no warnings should be generated for an empty JavaScript object
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Given a Node with a simple expression
    root = IR.parse("var x = 5 + 3;");
    nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.traverse(checkSideEffects);

    // When processing the root Node with a simple expression
    // Then no warnings should be generated
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Given a Node with a declaration statement followed by an expression statement
    root = IR.parse("var x = 5;\n5 + 3;");
    nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.traverse(checkSideEffects);

    // When processing the root Node with a declaration statement followed by an expression statement
    // Then no warnings should be generated
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Given a Node with an assignment statement followed by an expression statement
    root = IR.parse("x = 5;\n5 + 3;");
    nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.traverse(checkSideEffects);

    // When processing the root Node with an assignment statement followed by an expression statement
    // Then no warnings should be generated
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Given a Node with a simple expression followed by an assignment statement
    root = IR.parse("5 + 3 = x;");
    nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.traverse(checkSideEffects);

    // When processing the root Node with a simple expression followed by an assignment statement
    // Then a warning should be generated for the incorrect assignment operator
    assertEquals(1, checkSideEffects.problemNodes.size());

    // Given a Node with a statement containing an expression with a missing '+' operator
    root = IR.parse("x = 5 ; 3;");
    nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.traverse(checkSideEffects);

    // When processing the root Node with a statement containing an expression with a missing '+' operator
    // Then a warning should be generated
    assertEquals(1, checkSideEffects.problemNodes.size());

    // Given a Node with a complex expression
    root = IR.parse("(5 + 3) * 2 + (4 + 6) * 3;");
    nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.traverse(checkSideEffects);

    // When processing the root Node with a complex expression
    // Then no warnings should be generated
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Given a Node with a statement containing a simple operator that does not have a side effect
    root = IR.parse("++x;");
    nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.traverse(checkSideEffects);

    // When processing the root Node with a statement containing a simple operator that does not have a side effect
    // Then no warnings should be generated
    assertEquals(0, checkSideEffects.problemNodes.size());
  }
}