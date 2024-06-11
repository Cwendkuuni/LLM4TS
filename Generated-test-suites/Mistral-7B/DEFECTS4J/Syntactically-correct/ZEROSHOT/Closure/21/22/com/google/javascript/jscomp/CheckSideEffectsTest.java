import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.NodeTraversal.AbstractCallback;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private NodeTraversal nodeTraversal;
  private AbstractCallback nodeCallback;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    nodeTraversal = new NodeTraversal(compiler);
    nodeCallback = new AbstractPostOrderCallback() {};
  }

  @org.junit.Test
  public void testVisitNoOpStatement() {
    Node root = Node.newBlock(new Token(0, ""));
    nodeTraversal.traverse(checkSideEffects, root, null);
  }

  @org.junit.Test
  public void testVisitCommaStatement() {
    Node root = Node.newBlock(new Token(0, ""), Node.newComma(), Node.newIdentifier("x"));
    nodeTraversal.traverse(checkSideEffects, root, null);
  }

  @org.junit.Test
  public void testVisitSimpleOperator() {
    Node root = Node.newBlock(new Token(0, "x + y"));
    nodeTraversal.traverse(checkSideEffects, root, null);
  }

  @org.junit.Test
  public void testVisitStringConcatenation() {
    Node root = Node.newBlock(
        new Token(0, ""),
        Node.newIdentifier("s"),
        Node.newString(" this string is "),
        Node.newIdentifier("s").add(Node.newString(" continued on the next line but you forgot the +")),
        Node.newIdentifier("x")
    );
    nodeTraversal.traverse(checkSideEffects, root, null);
  }

  @org.junit.Test
  public void testVisitStatementInsideBlock() {
    Node root = Node.newBlock(
        new Token(0, ""),
        Node.newBlock(new Token(0, ""), Node.newIdentifier("x").assign(Node.newName(Node.IDENTIFIER_QUALIFIED, "foo")))
    );
    nodeTraversal.traverse(checkSideEffects, root, null);
  }

  @org.junit.Test
  public void testVisitStatementWithSideEffectFreeCode() {
    Node root = Node.newBlock(
        new Token(0, ""),
        Node.newIdentifier("x").assign(Node.newString("this string is ")),
        Node.newIdentifier("x").assign(Node.newString("continued on the next line but you forgot the +")),
        Node.newExpressionStatement(Node.newName(Node.IDENTIFIER_QUALIFIED, "x").assign(Node.newName("foo"))),
        Node.newExpressionStatement(Node.newName("foo"))
    );
    nodeTraversal.traverse(checkSideEffects, root, null);
  }

  @org.junit.Test
  public void testVisitStatementWithSideEffect() {
    Node root = Node.newBlock(
        new Token(0, ""),
        Node.newExpressionStatement(Node.newName("console").log(Node.newString("Hello World")))
    );
    nodeTraversal.traverse(checkSideEffects, root, null);
  }

  @org.junit.Test
  public void testVisitStatementInsideLoop() {
    Node root = Node.newBlock(
        new Token(0, ""),
        Node.newFor(
            new Token(0, ""),
            Node.newIdentifier("i"),
            Node.newNumber(0),
            Node.newNumber(10),
            new Node[] { Node.newIdentifier("i").assign(Node.newName("++i")) },
            Node.newBlock(
                Node.newToken(Token.KEYWORD, "if"),
                Node.newBooleanLiteral(true),
                Node.newToken(Token.KEYWORD, "else"),
                Node.newExpressionStatement(Node.newName("console").log(Node.newString("i: "))),
                Node.newToken(Token.KEYWORD, "break")
            )
        )
    );
    nodeTraversal.traverse(checkSideEffects, root, null);
  }

  @org.junit.Test
  public void testProtectSideEffectFreeCode() {
    nodeTraversal.traverse(checkSideEffects, Node.newBlock(new Token(0, ""), Node.newIdentifier("x").assign(Node.newString("Hello World"))), null);
    checkSideEffects.protectSideEffects();
    assertEquals(1, checkSideEffects.problemNodes.size());
    Node problemNode = checkSideEffects.problemNodes.get(0);
    Node replacement = (Node) problemNode.getChild(0);
    assertEquals(Node.CALL, replacement.getType());
    assertEquals(PROTECTOR_FN, replacement.getFirstChild().getString());
  }

  @org.junit.Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = Node.newBlock(Node.newCall(Node.newName(Node.IDENTIFIER_QUALIFIED, PROTECTOR_FN), Lists.newArrayList(Node.newIdentifier("x"))));
    nodeTraversal.traverse(checkSideEffects, root, null);
    checkSideEffects.protectSideEffects();

    StripProtection stripProtection = new StripProtection(compiler);
    nodeTraversal.traverse(stripProtection, root, null);

    assertEquals(0, root.getChildCount());
  }
}