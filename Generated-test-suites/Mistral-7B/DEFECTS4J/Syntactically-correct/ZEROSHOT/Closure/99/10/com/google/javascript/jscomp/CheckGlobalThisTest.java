import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private Checker checker;

  @Mock
  private NodeFactory nodeFactory;

  @Mock
  private CompilerOptions options;

  @Mock
  private Parser parser;

  @InjectMocks
  private CheckGlobalThis checkGlobalThis;

  @Test
  public void testShouldTraverseGlobalContext() {
    Node node = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, node, checker, options, null);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, null, node));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setIsConstructor(true);
    functionNode.setJSDocInfo(jsDoc);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setThisType(true);
    functionNode.setJSDocInfo(jsDoc);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setIsOverride(true);
    functionNode.setJSDocInfo(jsDoc);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWhenNotInGlobalContext() {
    Node functionNode = Node.newNode(Token.FUNCTION);

    Node parentNode = Node.newNode(Token.BLOCK);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWhenNotInFunctionContext() {
    Node functionNode = Node.newNode(Token.FUNCTION);

    Node parentNode = Node.newNode(Token.NUMBER, "5");
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN);
    Node leftHandSide = Node.newNode(Token.THIS);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, leftHandSide, assignNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentWithPrototypeProperty() {
    Node assignNode = Node.newNode(Token.ASSIGN, null, null, null, null, null, null, null, null);
    Node leftHandSide = Node.newNode(Token.THIS);
    Node rightHandSide = Node.newNode(Token.GETPROP, null, leftHandSide, Node.newString("prototype"));

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, rightHandSide, assignNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentWithPrototypeSubProperty() {
    Node assignNode = Node.newNode(Token.ASSIGN, null, null, null, null, null, null, null, null);
    Node leftHandSide = Node.newNode(Token.THIS);
    Node rightHandSide = Node.newNode(Token.GETPROP, null, leftHandSide, Node.newString("a.prototype.b"));

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, rightHandSide, assignNode));
  }

  @Test
  public void testShouldReportThisKeywordInGlobalContext() {
    Node node = Node.newNode(Token.THIS);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    checkGlobalThis.visit(traversal, node, parentNode);

    assertTrue(compiler.hasError(traversal.getErrors(), GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisKeywordInFunctionContext() {
    Node functionNode = Node.newNode(Token.FUNCTION);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    checkGlobalThis.visit(traversal, Node.newNode(Token.THIS), functionNode);

    assertFalse(compiler.hasError(traversal.getErrors(), GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisKeywordInFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setIsConstructor(true);
    functionNode.setJSDocInfo(jsDoc);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    checkGlobalThis.visit(traversal, Node.newNode(Token.THIS), functionNode);

    assertFalse(compiler.hasError(traversal.getErrors(), GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisKeywordInFunctionWithThisAnnotation() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setThisType(true);
    functionNode.setJSDocInfo(jsDoc);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    checkGlobalThis.visit(traversal, Node.newNode(Token.THIS), functionNode);

    assertFalse(compiler.hasError(traversal.getErrors(), GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisKeywordInFunctionWithOverrideAnnotation() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    JSDocInfo jsDoc = Node.newJSDocInfo();
    jsDoc.setIsOverride(true);
    functionNode.setJSDocInfo(jsDoc);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    checkGlobalThis.visit(traversal, Node.newNode(Token.THIS), functionNode);

    assertFalse(compiler.hasError(traversal.getErrors(), GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisKeywordInFunctionWithNoAnnotation() {
    Node functionNode = Node.newNode(Token.FUNCTION);

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    checkGlobalThis.visit(traversal, Node.newNode(Token.THIS), functionNode);

    assertFalse(compiler.hasError(traversal.getErrors(), GLOBAL_THIS));
  }

  @Test
  public void testShouldReportThisKeywordInPropertyAccess() {
    Node getPropNode = Node.newNode(Token.GETPROP, null, Node.newNode(Token.THIS), Node.newString("x"));

    Node parentNode = Node.newNode(Token.SCRIPT);
    NodeTraversal traversal = new NodeTraversal(parser, parentNode, checker, options, null);
    checkGlobalThis.visit(traversal, getPropNode, parentNode);

    assertTrue(compiler.hasError(traversal.getErrors(), GLOBAL_THIS));
  }
}