import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private CheckGlobalThis checker;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 checker = new CheckGlobalThis(compiler, WARNING);
 }

 @Test
 public void testSafeUsesOfThis() {
 Node node = new Node("function Foo() { this.x = 1; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsesOfThisOnLeftSideOfAssignment() {
 Node node = new Node("function Foo() { var x = this; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsesOfThisOnLeftSideOfPropertyAccess() {
 Node node = new Node("function Foo() { this.prototype.x = 1; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsesOfThisInFunctionWithoutJSDoc() {
 Node node = new Node("function Foo() { this.x = 1; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsesOfThisInFunctionWithConstructorJSDoc() {
 Node node = new Node("/** @constructor */ function Foo() { this.x = 1; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsesOfThisInFunctionWithThisJSDoc() {
 Node node = new Node("function Foo() { /** @this {x: number} */ this.x = 1; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsesOfThisInFunctionWithNoLogicalPlaceForThisAnnotation() {
 Node node = new Node("function Foo() { this.bar = function() { this.x = 1; } }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testSafeUsesOfThisInPrototypeMethod() {
 Node node = new Node("function Foo() { } Foo.prototype.bar = function() { this.x = 1; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testSafeUsesOfThisInConstructorWithJSDoc() {
 Node node = new Node("/** @constructor */ function Foo() { this.x = 1; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testSafeUsesOfThisInFunctionWhereItIsAssignedToVariable() {
 Node node = new Node("function Foo() { var x = this; }");
 NodeTraversal traversal = new NodeTraversal(node, checker);
 checker.maybeTraverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }
}