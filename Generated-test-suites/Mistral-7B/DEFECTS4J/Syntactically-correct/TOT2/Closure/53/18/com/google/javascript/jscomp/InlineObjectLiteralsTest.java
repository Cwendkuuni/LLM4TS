import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

 private AbstractCompiler compiler;
 private CompilerOptions options;
 private InlineObjectLiterals pass;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 options = new CompilerOptions();
 pass = new InlineObjectLiterals(compiler, () -> "test_variable_" + System.nanoTime());
 CompilerPassManager manager = new CompilerPassManager(compiler, options);
 manager.addPass(pass);
 }

 @Test
 public void testInlineObjectLiteral() {
 String source = "var obj = {a: 1, b: 2};";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.NUMBER, objA.getType());
 assertEquals(1, objA.getNumber().getIntValue());

 Var objBVar = scope.getVar("obj.b");
 assertEquals("JSCompiler_object_inline_b_test_variable_", objBVar.name);
 Node objB = objBVar.getInitialValue();
 assertEquals(Token.NUMBER, objB.getType());
 assertEquals(2, objB.getNumber().getIntValue());
 }

 @Test
 public void testInlineObjectLiteralWithEmptyInitialValue() {
 String source = "var obj = {a: 1};";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.NULL_LITERAL, objA.getType());
 assertNull(objA.getFirstChild());
 }

 @Test
 public void testInlineObjectLiteralWithSelfReferentialAssignment() {
 String source = "var obj = {a: 1, b: {c: obj.a}};";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 assertThrows(IllegalArgumentException.class, () -> pass.getResultNode());
 }

 @Test
 public void testInlineObjectLiteralWithDuplicateKeys() {
 String source = "var obj = {a: 1, a: 2};";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 assertThrows(IllegalArgumentException.class, () -> pass.getResultNode());
 }

 @Test
 public void testInlineObjectLiteralWithUndefinedKey() {
 String source = "var obj = {a: 1, b: undefined};";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.NUMBER, objA.getType());
 assertEquals(1, objA.getNumber().getIntValue());

 Var objBVar = scope.getVar("obj.b");
 assertNull(objBVar);
 }

 @Test
 public void testInlineObjectLiteralWithMissingKey() {
 String source = "var obj = {a: 1};";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.NUMBER, objA.getType());
 assertEquals(1, objA.getNumber().getIntValue());

 Var objBVar = scope.getVar("obj.b");
 assertNull(objBVar);
 }

 @Test
 public void testInlineObjectLiteralWithInitialValueAssignment() {
 String source = "var obj = {a: 1}; obj.a = 2;";
 Node externs = Node.newTextTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.NUMBER, objA.getType());
 assertEquals(2, objA.getNumber().getIntValue());
 }

 @Test
 public void testInlineObjectLiteralWithFunctionExpression() {
 String source = "function foo() { var obj = {a: 1}; }";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.NUMBER, objA.getType());
 assertEquals(1, objA.getNumber().getIntValue());
 }

 @Test
 public void testInlineObjectLiteralWithInitialValueAssignmentInFunctionDeclaration() {
 String source = "function foo() { var obj = {a: 1}; }";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.NUMBER, objA.getType());
 assertEquals(1, objA.getNumber().getIntValue());

 Var fooVar = scope.getVar("foo");
 assertEquals("function", fooVar.name);
 Node foo = fooVar.getInitialValue();
 assertEquals(Token.FUNCTION_DECL, foo.getType());
 }

 @Test
 public void testInlineObjectLiteralWithObjectLiteralAsInitialValue() {
 String source = "function foo() { var obj = {a: 1}; }";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.OBJECTLIT, objA.getType());
 Node objAChild = objA.getFirstChild();
 Node objANodeA = objAChild.getFirstChild();
 assertEquals("a", objANodeA.getString());
 Node objANodeANodeValue = objANodeA.getNext();
 assertEquals(Token.NUMBER, objANodeANodeValue.getType());
 assertEquals(1, objANodeANodeValue.getNumber().getIntValue());
 }

 @Test
 public void testInlineObjectLiteralWithObjectLiteralAsInitialValueInFunctionDeclaration() {
 String source = "function foo() { var obj = {a: 1}; }";
 Node externs = Node.newTextNode("", null);
 Node root = Node.newTextNode(source, null);
 manager.process(externs, root);
 Node result = pass.getResultNode();

 assertEquals(Node.SCOPE, result.getType());
 Scope scope = result.asScope();
 Var objVar = scope.getVar("obj");
 assertEquals("JSCompiler_object_inline_a_test_variable_", objVar.name);
 Node objA = objVar.getInitialValue();
 assertEquals(Token.OBJECTLIT, objA.getType());
 Node objAChild = objA.getFirstChild();
 Node objANodeA = objAChild.getFirstChild();
 assertEquals("a", objANodeA.getString());
 Node objANodeANodeValue = objANodeA.getNext();
 assertEquals(Token.NUMBER, objANodeANodeValue.getType());
 assertEquals(1, objANodeANodeValue.getNumber().getIntValue());

 Var fooVar = scope.getVar("foo");
 assertEquals("function", fooVar.name);
 Node foo = fooVar.getInitialValue();
 assertEquals(Token.FUNCTION_DECL, foo.getType());
 }

 @After
 public void tearDown() {
 compiler.dispose();
 }
}