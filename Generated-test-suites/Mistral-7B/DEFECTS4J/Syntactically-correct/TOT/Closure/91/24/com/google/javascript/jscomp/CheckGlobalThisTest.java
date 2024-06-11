import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

 private CheckGlobalThis checker;
 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 }

 @Test
 public void testSafeUsesOfThis() {
 Node functionNode = new Node(Token.FUNCTION, null);
 Node constructorNode = new Node(Token.FUNCTION, null);
 constructorNode.setJSDocInfo(new JSDocInfo(true));
 Node prototypeMethodNode = new Node(Token.FUNCTION, null);
 prototypeMethodNode.setJSDocInfo(new JSDocInfo(true, null, new String[]{"prototype"}));
 Node constructorAnnotationNode = new Node(Token.ANNOTATION, null);
 Node constructorAnnotationValueNode = new Node(Token.STRING, "@constructor");
 constructorAnnotationNode.setFirstChild(constructorAnnotationValueNode);
 Node thisAnnotationNode = new Node(Token.ANNOTATION, null);
 Node thisAnnotationValueNode = new Node(Token.STRING, "@this");
 thisAnnotationNode.setFirstChild(thisAnnotationValueNode);
 Node logicalPlaceNode = new Node(Token.NAME, "logicalPlace");
 Node logicalPlaceFunctionNode = new Node(Token.FUNCTION, logicalPlaceNode);
 Node assignmentNode = new Node(Token.ASSIGN, null);
 Node assignmentLhsNode = new Node(Token.NAME, "a");
 assignmentNode.setFirstChild(assignmentLhsNode);
 Node assignmentRhsNode = new Node(Token.THIS);
 assignmentLhsNode.setFirstChild(assignmentLhsNode);
 Node prototypePropertyAccessNode = new Node(Token.GETPROP, assignmentLhsNode);
 prototypePropertyAccessNode.setLastChild(new Node(Token.STRING, "prototype"));

 Node traversal = new NodeTraversal(new CompilerOptions(), checker);
 Node rootNode = new Node(Token.SCRIPT, null);
 rootNode.addChild(functionNode);
 rootNode.addChild(constructorNode);
 rootNode.addChild(prototypeMethodNode);
 rootNode.addChild(constructorAnnotationNode);
 rootNode.addChild(thisAnnotationNode);
 rootNode.addChild(logicalPlaceNode);
 rootNode.addChild(logicalPlaceFunctionNode);
 rootNode.addChild(assignmentNode);
 rootNode.addChild(assignmentRhsNode);
 rootNode.addChild(prototypePropertyAccessNode);

 traversal.traverse(rootNode, null);

 // No errors reported
 }

 @Test
 public void testUnsafeUsesOfThis() {
 Node functionNode = new Node(Token.FUNCTION, null);
 Node assignmentNode = new Node(Token.ASSIGN, null);
 Node assignmentLhsNode = new Node(Token.THIS);
 Node assignmentRhsNode = new Node(Token.STRING, "a");
 assignmentNode.setFirstChild(assignmentLhsNode);
 assignmentNode.setLastChild(assignmentRhsNode);
 Node nodeToTraverse = assignmentNode;

 Node traversal = new NodeTraversal(new CompilerOptions(), checker);
 Node rootNode = new Node(Token.SCRIPT, null);
 rootNode.addChild(functionNode);
 rootNode.addChild(nodeToTraverse);

 traversal.traverse(rootNode, null);

 // An error should be reported
 Node errorNode = checker.compiler.getErrorList().getErrors().get(0);
 assertEquals("dangerous use of the global 'this' object", errorNode.getMessage());
 }

 @Test
 public void testSkippedFunctions() {
 Node functionNode = new Node(Token.FUNCTION, null);
 Node constructorNode = new Node(Token.FUNCTION, null);
 constructorNode.setJSDocInfo(new JSDocInfo(true));
 Node prototypeMethodNode = new Node(Token.FUNCTION, null);
 prototypeMethodNode.setJSDocInfo(new JSDocInfo(true, null, new String[]{"prototype"}));
 Node constructorAnnotationNode = new Node(Token.ANNOTATION, null);
 Node constructorAnnotationValueNode = new Node(Token.STRING, "@constructor");
 constructorAnnotationNode.setFirstChild(constructorAnnotationValueNode);
 Node functionWithNoLogicalPlaceNode = new Node(Token.NAME, "noLogicalPlace");
 Node functionWithNoLogicalPlaceFunctionNode = new Node(Token.FUNCTION, functionWithNoLogicalPlaceNode);
 Node assignmentNode = new Node(Token.ASSIGN, null);
 Node assignmentLhsNode = new Node(Token.THIS);
 Node assignmentRhsNode = new Node(Token.STRING, "a");
 assignmentNode.setFirstChild(assignmentLhsNode);
 assignmentNode.setLastChild(assignmentRhsNode);
 Node prototypePropertyAccessNode = new Node(Token.GETPROP, assignmentLhsNode);
 prototypePropertyAccessNode.setLastChild(new Node(Token.STRING, "prototype"));

 Node traversal = new NodeTraversal(new CompilerOptions(), checker);
 Node rootNode = new Node(Token.SCRIPT, null);
 rootNode.addChild(functionNode);
 rootNode.addChild(constructorNode);
 rootNode.addChild(prototypeMethodNode);
 rootNode.addChild(constructorAnnotationNode);
 rootNode.addChild(functionWithNoLogicalPlaceNode);
 rootNode.addChild(functionWithNoLogicalPlaceFunctionNode);
 rootNode.addChild(assignmentNode);
 rootNode.addChild(assignmentRhsNode);
 rootNode.addChild(prototypePropertyAccessNode);

 traversal.traverse(rootNode, null);

 // No errors reported for functions that are skipped
 }
}