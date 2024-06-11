import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

 @Mock
 private AbstractCompiler compiler;
 @Mock
 private Checker checker;
 @InjectMocks
 private CheckGlobalThis checkGlobalThis;

 @Test
 public void testNoGlobalThisUsage() {
 checkGlobalThis(compiler, checker, newNode("function foo() {}"), null);
 }

 @Test
 public void testGlobalThisUsageInConstructor() {
 checkGlobalThis(compiler, checker, newNode("function Foo() { this.x = 1; }"), null);
 }

 @Test
 public void testGlobalThisUsageInFunctionWithThisAnnotated() {
 checkGlobalThis(compiler, checker, newNode("function Foo() { @this this.x = 1; }"), null);
 }

 @Test
 public void testGlobalThisUsageInFunctionWithoutThisAnnotated() {
 checkGlobalThis(compiler, checker, newNode("function Foo() { this.x = 1; }"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignment() {
 checkGlobalThis(compiler, checker, newNode("var a = this;"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithLeftSidePropertyAccess() {
 checkGlobalThis(compiler, checker, newNode("var a = { x: this };"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithLeftSidePrototypePropertyAccess() {
 checkGlobalThis(compiler, checker, newNode("var a = { __proto__: { x: this } };"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithLeftSidePrototypeChainPropertyAccess() {
 checkGlobalThis(compiler, checker, newNode("var a = { get x() { return this.y; } }; var b = new a(); b.x;"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithRightSidePropertyAccess() {
 checkGlobalThis(compiler, checker, newNode("var a = {}; a.x = this;"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithRightSidePrototypePropertyAccess() {
 checkGlobalThis(compiler, checker, newNode("var a = { __proto__: {} }; a.__proto__.x = this;"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithRightSidePrototypeChainPropertyAccess() {
 checkGlobalThis(compiler, checker, newNode("var a = {}; var b = Object.create(a); b.x = this;"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithLeftSideFunctionDefinition() {
 checkGlobalThis(compiler, checker, newNode("var a = function() { this.x = 1; }"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithLeftSideFunctionDefinitionWithThisAnnotated() {
 checkGlobalThis(compiler, checker, newNode("var a = function Foo() { @this this.x = 1; }"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithLeftSideVariableDeclaration() {
 checkGlobalThis(compiler, checker, newNode("var a, x = this;"), null);
 }

 @Test
 public void testGlobalThisUsageInAssignmentWithLeftSideVariableDeclarationWithThisAnnotated() {
 checkGlobalThis(compiler, checker, newNode("var a, @this x; x = this;"), null);
 }

 @Test
 public void testShouldNotTraverseNonGlobalContexts() {
 checkGlobalThis(compiler, checker, newNode("function bar() {}"), newNode("function foo() { bar(); }"));
 }

 private void checkGlobalThis(AbstractCompiler compiler, Checker checker, Node node, Node parent) {
 CheckGlobalThis check = new CheckGlobalThis(compiler, WARNING);
 NodeTraversal traverser = new NodeTraversal(compiler, check);
 checker.check(new NodeTree(node), traverser);
 assertThat(checker.getDiagnostics().size(), is(0));
 }

 private Node newNode(String nodeSource) {
 return new Node(new RhinoCompiler().compile(nodeSource, null, null).getRootNode().getBody());
 }
}