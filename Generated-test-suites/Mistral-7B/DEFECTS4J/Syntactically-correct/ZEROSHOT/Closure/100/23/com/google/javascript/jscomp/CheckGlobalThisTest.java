import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.NodeType.ASSIGN;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.THIS;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SyntheticSourceFile;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

 private Checker checker;
 private AbstractCompiler compiler;
 private NodeTraversal traversal;

 @Before
 public void setUp() {
 checker = new Checker();
 compiler = Mockito.mock(AbstractCompiler.class);
 traversal = new NodeTraversal(compiler, null, null, null);
 }

 @Test
 public void testNoGlobalThisWarning() {
 CompilerOptions options = new CompilerOptions(new SyntheticSourceFile("test.js", ""));
 CheckGlobalThis check = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
 check.visit(traversal, null, null);
 String code = "function a() {}";
 Node node = Node.newNode(FUNCTION, code);
 traversal.traverse(node);
 check.shouldTraverse(traversal, node, null);
 check.visit(traversal, node, null);
 Mockito.verify(compiler, Mockito.times(0)).report(Mockito.any());
 }

 @Test
 public void testFunctionWithJSDocWarning() {
 CompilerOptions options = new CompilerOptions(new SyntheticSourceFile("test.js", ""));
 CheckGlobalThis check = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
 check.visit(traversal, null, null);
 String code = "/** @constructor */ function a() {}";
 Node node = Node.newNode(FUNCTION, code);
 traversal.traverse(node);
 check.shouldTraverse(traversal, node, null);
 check.visit(traversal, node, null);
 Mockito.verify(compiler, Mockito.times(0)).report(Mockito.any());
 }

 @Test
 public void testFunctionWithoutJSDocWarning() {
 CompilerOptions options = new CompilerOptions(new SyntheticSourceFile("test.js", ""));
 CheckGlobalThis check = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
 check.visit(traversal, null, null);
 String code = "function a() {}";
 Node node = Node.newNode(FUNCTION, code);
 traversal.traverse(node);
 check.shouldTraverse(traversal, node, null);
 check.visit(traversal, node, null);
 Mockito.verify(compiler, Mockito.times(1)).report(Mockito.argThat(diagnostic -> {
 return diagnostic.getMessage().equals("dangerous use of the global 'this' object");
 }));
 }

 @Test
 public void testAssignmentWithGlobalThisWarning() {
 CompilerOptions options = new CompilerOptions(new SyntheticSourceFile("test.js", ""));
 CheckGlobalThis check = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
 check.visit(traversal, null, null);
 String code = "var a = this;";
 Node left = Node.newNode(ASSIGN, code);
 Node right = Node.newNode(THIS, null);
 Node assign = Node.newNode(ASSIGN, null);
 assign.setLeft(left);
 assign.setRight(right);
 traversal.traverse(assign);
 check.shouldTraverse(traversal, assign, null);
 check.visit(traversal, assign, null);
 Mockito.verify(compiler, Mockito.times(1)).report(Mockito.argThat(diagnostic -> {
 return diagnostic.getMessage().equals("dangerous use of the global 'this' object");
 }));
 }

 @Test
 public void testAssignmentWithLocalThisVariableWarning() {
 CompilerOptions options = new CompilerOptions(new SyntheticSourceFile("test.js", ""));
 CheckGlobalThis check = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
 check.visit(traversal, null, null);
 String code = "var a = function() { this.x = 0; }; var b = this;";
 Node functionNode = Node.newNode(FUNCTION, code);
 Node assign1 = Node.newNode(ASSIGN, null);
 assign1.setLeft(Node.newNode(THIS, null));
 assign1.setRight(Node.newNode(GETPROP, code, "x", null));
 Node assign2 = Node.newNode(ASSIGN, null);
 assign2.setLeft(Node.newNode(VAR, null, "b", null, null, null, Node.newNode(THIS, null), null));
 assign2.setRight(Node.newNode(NULL_LITERAL, null));
 Node assign3 = Node.newNode(ASSIGN, null);
 assign3.setLeft(Node.newNode(VAR, null, "a", null, null, null, null, null));
 assign3.setRight(assign1);
 Node assign4 = Node.newNode(ASSIGN, null);
 assign4.setLeft(assign2);
 assign4.setRight(assign3);
 traversal.traverse(assign4);
 check.shouldTraverse(traversal, assign4, null);
 check.visit(traversal, assign4, null);
 Mockito.verify(compiler, Mockito.times(0)).report(Mockito.any());
 }

 @Test
 public void testAssignmentWithLocalThisPropertyWarning() {
 CompilerOptions options = new CompilerOptions(new SyntheticSourceFile("test.js", ""));
 CheckGlobalThis check = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
 check.visit(traversal, null, null);
 String code = "function a() { this.x = 0; } var b = a.prototype; var c = this;";
 Node functionNode = Node.newNode(FUNCTION, code);
 Node getProp1 = Node.newNode(GETPROP, code, "prototype", null);
 Node getProp2 = Node.newNode(GETPROP, getProp1, "constructor", null);
 Node assign1 = Node.newNode(ASSIGN, null);
 assign1.setLeft(Node.newNode(THIS, null));
 assign1.setRight(Node.newNode(GETPROP, code, "x", null));
 Node assign2 = Node.newNode(ASSIGN, null);
 assign2.setLeft(Node.newNode(VAR, null, "b", null, null, null, null, null));
 assign2.setRight(assign1);
 Node assign3 = Node.newNode(ASSIGN, null);
 assign3.setLeft(Node.newNode(VAR, null, "c", null, null, null, null, null));
 assign3.setRight(Node.newNode(THIS, null));
 Node assign4 = Node.newNode(ASSIGN, null);
 assign4.setLeft(assign2);
 assign4.setRight(assign3);
 traversal.traverse(assign4);
 check.shouldTraverse(traversal, assign4, null);
 check.visit(traversal, assign4, null);
 Mockito.verify(compiler, Mockito.times(0)).report(Mockito.any());
 }
}