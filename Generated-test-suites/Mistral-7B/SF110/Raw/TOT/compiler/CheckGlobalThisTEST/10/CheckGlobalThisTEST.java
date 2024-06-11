import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private CheckGlobalThis checkGlobalThis;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 checkGlobalThis = new CheckGlobalThis(compiler);
 }

 @Test
 public void testSafeUsesOfThis() {
 // Test prototype methods
 Node prototypeMethodNode = makeFunctionNode("function PrototypeMethod() {}");
 prototypeMethodNode.setIsVar(false);
 prototypeMethodNode.setIsFunction(true);
 Node prototypeObjectNode = makeBlockNode("var p = {pMethod: PrototypeMethod}");
 Node prototypeCallNode = makeCallNode("p.pMethod()");
 Node prototypeMethodCallParent = makeAssignNode("var x = p.pMethod");

 NodeTraversal traversal = new NodeTraversal(prototypeMethodNode, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(prototypeMethodNode));

 traversal = new NodeTraversal(prototypeCallNode, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(prototypeCallNode));

 traversal = new NodeTraversal(prototypeMethodCallParent, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(prototypeMethodCallParent));

 // Test constructors
 Node constructorNode = makeFunctionNode("function Constructor() {}");
 constructorNode.setIsVar(false);
 constructorNode.setIsConstructor(true);
 Node constructorCallNode = makeCallNode("new Constructor()");
 Node constructorMethodNode = makeFunctionNode("function ConstructorMethod() {}");
 constructorMethodNode.setIsVar(false);
 constructorMethodNode.setIsFunction(true);
 Node constructorMethodCallNode = makeCallNode("Constructor.prototype.ConstructorMethod()");
 Node constructorCallParent = makeAssignNode("var x = new Constructor()");
 Node constructorMethodCallParent = makeAssignNode("x.ConstructorMethod = ConstructorMethod");

 traversal = new NodeTraversal(constructorNode, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(constructorNode));

 traversal = new NodeTraversal(constructorCallNode, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(constructorCallNode));

 traversal = new NodeTraversal(constructorMethodNode, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(constructorMethodNode));

 traversal = new NodeTraversal(constructorMethodCallNode, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(constructorMethodCallNode));

 traversal = new NodeTraversal(constructorCallParent, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(constructorCallParent));

 traversal = new NodeTraversal(constructorMethodCallParent, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(constructorMethodCallParent));

 // Test functions with no logical place to put a this annotation
 Node noThisAnnotationNode = makeFunctionNode("function NoThisAnnotation() {}");
 Node noThisAnnotationCallNode = makeCallNode("NoThisAnnotation()");
 Node noThisAnnotationCallParent = makeAssignNode("var x = NoThisAnnotation");

 traversal = new NodeTraversal(noThisAnnotationNode, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(noThisAnnotationNode));

 traversal = new NodeTraversal(noThisAnnotationCallNode, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(noThisAnnotationCallNode));

 traversal = new NodeTraversal(noThisAnnotationCallParent, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(noThisAnnotationCallParent));
 }

 @Test
 public void testUnsafeUsesOfThis() {
 // Test assignments
 Node assignmentNode = makeAssignNode("this.x = 1");
 Node assignmentParentNode1 = makeBlockNode("{this.x = 1}");
 Node assignmentParentNode2 = makeBlockNode("{function f() {this.x = 1}}");
 Node assignmentParentNode3 = makeBlockNode("{function f() {var x = this}}");
 Node assignmentParentNode4 = makeBlockNode("{function f() {var x = this}}");
 Node assignmentParentNode5 = makeBlockNode("{function f() {this.x = this.x}}");

 traversal = new NodeTraversal(assignmentNode, checkGlobalThis);
 traversal.traverse();
 assertTrue(checkGlobalThis.shouldReportThis(assignmentNode));

 traversal = new NodeTraversal(assignmentParentNode1, checkGlobalThis);
 traversal.traverse();
 assertTrue(checkGlobalThis.shouldReportThis(assignmentParentNode1));

 traversal = new NodeTraversal(assignmentParentNode2, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(assignmentParentNode2));

 traversal = new NodeTraversal(assignmentParentNode3, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(assignmentParentNode3));

 traversal = new NodeTraversal(assignmentParentNode4, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(assignmentParentNode4));

 traversal = new NodeTraversal(assignmentParentNode5, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(assignmentParentNode5));

 // Test property accesses
 Node propertyAccessNode = makePropertyAccessNode("this.x");
 Node propertyAccessParentNode1 = makeBlockNode("{this.x}");
 Node propertyAccessParentNode2 = makeBlockNode("{function f() {return this.x}}");
 Node propertyAccessParentNode3 = makeBlockNode("{function f() {return this.y}}");

 traversal = new NodeTraversal(propertyAccessNode, checkGlobalThis);
 traversal.traverse();
 assertTrue(checkGlobalThis.shouldReportThis(propertyAccessNode));

 traversal = new NodeTraversal(propertyAccessParentNode1, checkGlobalThis);
 traversal.traverse();
 assertTrue(checkGlobalThis.shouldReportThis(propertyAccessParentNode1));

 traversal = new NodeTraversal(propertyAccessParentNode2, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(propertyAccessParentNode2));

 traversal = new NodeTraversal(propertyAccessParentNode3, checkGlobalThis);
 traversal.traverse();
 assertFalse(checkGlobalThis.shouldReportThis(propertyAccessParentNode3));
 }

 private Node makeCallNode(String code) {
 return NodeUtil.makeCall(makeLiteralNode(code), null, null);
 }

 private Node makeAssignNode(String code) {
 return NodeUtil.makeAssign(makeLiteralNode(code), null, null);
 }

 private Node makeFunctionNode(String code) {
 return NodeUtil.makeFunction(code, null);
 }

 private Node makeBlockNode(String code) {
 return NodeUtil.makeBlock(code, null);
 }

 private Node makeLiteralNode(String code) {
 return NodeUtil.makeLiteral(code, null);
 }
}