import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Checker checker;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 checker = new Checker(compiler, new CompilerOptions());
 }

 @Test
 public void testSafeUsage() {
 Node n = NodeFactory.createFunctionDeclaration("a", new Node[0], null, null);
 Node body = NodeFactory.createBlock(new Node[0]);
 n.setBody(body);

 Node result = checker.check(n, null);
 assertFalse(result.getDiagnostics().hasError(CheckGlobalThis.GLOBAL_THIS));
 }

 @Test
 public void testConstructor() {
 Node constructor = NodeFactory.createFunctionDeclaration("Constructor", new Node[0], null, null);
 constructor.getJSDocInfo().setConstructor(true);
 Node body = NodeFactory.createBlock(new Node[0]);
 constructor.setBody(body);

 Node result = checker.check(constructor, null);
 assertFalse(result.getDiagnostics().hasError(CheckGlobalThis.GLOBAL_THIS));
 }

 @Test
 public void testThisAnnotation() {
 Node annotatedThis = NodeFactory.createFunctionDeclaration("thisFunction", new Node[0], null, "this");
 Node body = NodeFactory.createBlock(new Node[0]);
 annotatedThis.setBody(body);

 Node result = checker.check(annotatedThis, null);
 assertFalse(result.getDiagnostics().hasError(CheckGlobalThis.GLOBAL_THIS));
 }

 @Test
 public void testFunctionWithNoLogicalPlaceForThisAnnotation() {
 Node functionWithNoThisAnnotation = NodeFactory.createFunctionDeclaration("functionWithNoThisAnnotation", new Node[0], null, null);
 Node body = NodeFactory.createBlock(NodeFactory.createExpressionStatement(NodeFactory.createAssign(NodeFactory.createVariable("x"), NodeFactory.createNumberLiteral(1))));
 functionWithNoThisAnnotation.setBody(body);

 Node result = checker.check(functionWithNoThisAnnotation, null);
 assertFalse(result.getDiagnostics().hasError(CheckGlobalThis.GLOBAL_THIS));
 }

 @Test
 public void testFunctionWithAssignmentToThisOnLeftSide() {
 Node assignment = NodeFactory.createExpressionStatement(NodeFactory.createAssign(NodeFactory.createVariable("x"), NodeFactory.createThis()));
 Node functionWithAssignment = NodeFactory.createFunctionDeclaration("functionWithAssignment", new Node[0], null, null);
 Node body = NodeFactory.createBlock(new Node[0]);
 body.addChild(assignment);
 functionWithAssignment.setBody(body);

 Node result = checker.check(functionWithAssignment, null);
 assertTrue(result.getDiagnostics().getErrorCount() > 0);
 }

 @Test
 public void testFunctionWithAssignmentToThisOnRightSide() {
 Node assignment = NodeFactory.createExpressionStatement(NodeFactory.createAssign(NodeFactory.createVariable("x"), NodeFactory.createNumberLiteral(1)));
 Node functionWithAssignment = NodeFactory.createFunctionDeclaration("functionWithAssignment", new Node[0], null, null);
 Node rightSide = NodeFactory.createThis();
 Node body = NodeFactory.createBlock(new Node[0]);
 body.addChild(assignment);
 body.addChild(rightSide);
 functionWithAssignment.setBody(body);

 Node result = checker.check(functionWithAssignment, null);
 assertFalse(result.getDiagnostics().hasError(CheckGlobalThis.GLOBAL_THIS));
 }

 @Test
 public void testPrototypeProperty() {
 Node functionWithPrototypeProperty = NodeFactory.createFunctionDeclaration("functionWithPrototypeProperty", new Node[0], null, null);
 Node body = NodeFactory.createBlock(new Node[0]);
 body.addChild(NodeFactory.createPropertyAssignment(NodeFactory.createThis(), "x", NodeFactory.createNumberLiteral(1)));
 functionWithPrototypeProperty.setBody(body);
 Node prototype = NodeFactory.createProperty(NodeFactory.createThis(), "prototype", null);
 body.addChild(NodeFactory.createPropertyAssignment(prototype, "x", NodeFactory.createNumberLiteral(2)));

 Node result = checker.check(functionWithPrototypeProperty, null);
 assertFalse(result.getDiagnostics().hasError(CheckGlobalThis.GLOBAL_THIS));
 }

 @Test
 public void testPrototypeSubProperty() {
 Node functionWithPrototypeSubProperty = NodeFactory.createFunctionDeclaration("functionWithPrototypeSubProperty", new Node[0], null, null);
 Node body = NodeFactory.createBlock(new Node[0]);
 Node prototype = NodeFactory.createProperty(NodeFactory.createThis(), "prototype", null);
 body.addChild(NodeFactory.createPropertyAssignment(prototype, "x", NodeFactory.createProperty(NodeFactory.createThis(), "y", null)));
 functionWithPrototypeSubProperty.setBody(body);

 Node result = checker.check(functionWithPrototypeSubProperty, null);
 assertFalse(result.getDiagnostics().hasError(CheckGlobalThis.GLOBAL_THIS));
 }
}