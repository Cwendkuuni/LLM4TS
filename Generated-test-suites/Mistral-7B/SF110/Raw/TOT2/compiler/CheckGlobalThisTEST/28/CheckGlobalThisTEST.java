import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @Test
  public void testSafeUseOfThis() {
    CheckGlobalThis checker = new CheckGlobalThis(new Compiler());

    Node safeNode = Node.newCall(Token.CALL, Token.IDENTIFIER, Node.newArray());
    Node safeNodeParent = Node.newAssign(Token.ASSIGN, Token.IDENTIFIER, safeNode);

    checker.shouldTraverse(null, safeNodeParent, null);
    checker.visit(null, null, safeNodeParent);

    assertFalse(checker.shouldReportThis(safeNode));
  }

  @Test
  public void testUnsafeUseOfThisAsFunction() {
    CheckGlobalThis checker = new CheckGlobalThis(new Compiler());

    Node unsafeNode = Node.newFunction(Token.FUNCTION, Node.newArray(), Node.newBlock(null));

    Node unsafeNodeParent = Node.newAssign(Token.ASSIGN, Token.IDENTIFIER, unsafeNode);

    checker.shouldTraverse(null, unsafeNodeParent, null);
    checker.visit(null, null, unsafeNodeParent);

    assertTrue(checker.shouldReportThis(unsafeNode));
  }

  @Test
  public void testUnsafeUseOfThisAsPropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(new Compiler());

    Node globalThis = Node.newThis();
    Node unsafeNode = Node.newGetProp(Token.GET, globalThis, Token.IDENTIFIER);

    Node parent = Node.newAssign(Token.ASSIGN, Token.IDENTIFIER, Node.newObjectLiteral(null));

    checker.shouldTraverse(null, parent, null);
    checker.visit(null, null, parent);

    checker.shouldTraverse(null, unsafeNode, parent);
    checker.visit(null, null, unsafeNode);

    assertTrue(checker.shouldReportThis(unsafeNode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckerThrowsErrorOnUnsafeUseOfThis() {
    CheckGlobalThis checker = new CheckGlobalThis(new Compiler());

    Node unsafeNode = Node.newThis();

    Node parent = Node.newAssign(Token.ASSIGN, Token.IDENTIFIER, Node.newObjectLiteral(null));
    Node assignmentNode = Node.newAssign(Token.ASSIGN, Node.newGetProp(Token.GET, unsafeNode, Token.IDENTIFIER), Node.newNull());
    parent.addChild(assignmentNode);

    checker.shouldTraverse(null, parent, null);
    checker.visit(null, null, parent);

    assertTrue(checker.compiler.hasErrors());
  }
}