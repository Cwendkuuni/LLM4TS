import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.JSDocInfo.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
  }

  // Test for the constructor
  @Test
  public void constructorTest() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    assertNotNull(checkGlobalThis);
  }

  // Test for shouldTraverse() method
  @Test
  public void shouldTraverseTest() {
    NodeTraversal traversal = new NodeTraversal(null, new CheckGlobalThis(compiler));
    Node functionNode = new Node(FUNCTION, null, null);
    Node parentNode = new Node(SCRIPT, null, null);
    Node functionParentNode = new Node(NAME, null, null);
    functionParentNode.setJSDocInfo(new JSDocInfo(null, new String[]{"@constructor"}));
    parentNode.addChild(functionParentNode);
    parentNode.addChild(functionNode);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));

    Node functionNodeWithThisAnnot = new Node(FUNCTION, null, null);
    Node functionParentNodeWithThisAnnot = new Node(NAME, null, null);
    functionParentNodeWithThisAnnot.setJSDocInfo(new JSDocInfo(null, new String[]{"@this"}));
    parentNode.addChild(functionParentNodeWithThisAnnot);
    parentNode.addChild(functionNodeWithThisAnnot);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNodeWithThisAnnot, parentNode));

    Node functionNodeWithNoLogicalPlace = new Node(FUNCTION, null, null);
    Node functionParentNodeWithNoLogicalPlace = new Node(BLOCK, null, null);
    parentNode.addChild(functionParentNodeWithNoLogicalPlace);
    parentNode.addChild(functionNodeWithNoLogicalPlace);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNodeWithNoLogicalPlace, parentNode));

    Node constructorNode = new Node(CONSTRUCTOR, null, null);
    Node constructorParentNode = new Node(SCRIPT, null, null);
    Node constructorGrandParentNode = new Node(SCRIPT, null, null);
    constructorGrandParentNode.addChild(constructorParentNode);
    constructorParentNode.addChild(constructorNode);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, constructorNode, constructorParentNode));

    Node prototypeMethodNode = new Node(FUNCTION, null, null);
    Node prototypeMethodParentNode = new Node(NAME, null, null);
    prototypeMethodParentNode.setJSDocInfo(new JSDocInfo(null, new String[]{"@constructor", "@prototype"}));
    Node prototypeMethodGrandParentNode = new Node(NAME, null, null);
    prototypeMethodGrandParentNode.setJSDocInfo(new JSDocInfo(null, new String[]{"prototype"}));
    prototypeMethodGrandParentNode.addChild(prototypeMethodParentNode);
    prototypeMethodGrandParentNode.addChild(prototypeMethodNode);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, prototypeMethodNode, prototypeMethodGrandParentNode));

    Node functionNodeInObjectLiteral = new Node(FUNCTION, null, null);
    Node functionParentNodeInObjectLiteral = new Node(ASSIGN, null, null);
    Node functionGrandParentNodeInObjectLiteral = new Node(BLOCK, null, null);
    Node objectLiteralKeyNode = new Node(STRING_KEY, null, null);
    objectLiteralKeyNode.setString("prototype");
    functionGrandParentNodeInObjectLiteral.addChild(objectLiteralKeyNode);
    functionGrandParentNodeInObjectLiteral.addChild(functionParentNodeInObjectLiteral);
    functionParentNodeInObjectLiteral.addChild(functionNodeInObjectLiteral);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNodeInObjectLiteral, functionGrandParentNodeInObjectLiteral));

    Node functionNodeLentToPrototype = new Node(FUNCTION, null, null);
    Node functionParentNodeLentToPrototype = new Node(GETPROP, null, null);
    Node functionGrandParentNodeLentToPrototype = new Node(NAME, null, null);
    functionGrandParentNodeLentToPrototype.setJSDocInfo(new JSDocInfo(null, new String[]{"prototype"}));
    functionGrandParentNodeLentToPrototype.addChild(functionParentNodeLentToPrototype);
    functionParentNodeLentToPrototype.addChild(functionNodeLentToPrototype);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNodeLentToPrototype, functionGrandParentNodeLentToPrototype));
  }

  // Test for visit() method
  @Test
  public void visitTest() {
    NodeTraversal traversal = new NodeTraversal(null, new CheckGlobalThis(compiler));
    Node functionNode = new Node(FUNCTION, null, null);
    Node parentNode = new Node(SCRIPT, null, null);
    Node functionParentNode = new Node(NAME, null, null);
    Node thisNode = new Node(THIS, null, null);
    Node functionNodeThisChild = new Node(NAME, null, null);
    Node functionNodeThisParent = new Node(ASSIGN, null, null);
    Node functionNodeThisGrandParent = new Node(SCRIPT, null, null);
    functionNodeThisParent.addChild(functionNodeThisChild);
    functionNodeThisParent.addChild(thisNode);
    functionNodeThisGrandParent.addChild(functionNodeThisParent);
    functionNodeThisGrandParent.addChild(functionNode);
    functionNodeThisGrandParent.addChild(parentNode);
    checkGlobalThis.visit(traversal, functionNode, parentNode);
    assertEquals(GLOBAL_THIS, traversal.getLatestError().getDiagnosticType());
  }
}