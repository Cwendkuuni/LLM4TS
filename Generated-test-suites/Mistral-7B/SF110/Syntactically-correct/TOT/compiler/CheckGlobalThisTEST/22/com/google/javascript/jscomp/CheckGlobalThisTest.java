import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler(new CompilerOptions());
 }

 @Test
 public void testSafeUseOfThis() {
 Node node = new Node("function safeUseOfThis() { return this; }");
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() == 1);
 }

 @Test
 public void testFunctionWithThisAnnotation() {
 Node node = new Node("@this\nfunction thisAnnotatedFunction() { return this; }");
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() == 1);
 }

 @Test
 public void testPrototypeMethod() {
 String code = "class MyClass {\n" +
         " constructor() { this.name = 'Alice'; }\n" +
         " method() { return this.name; }\n" +
         "}";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() > 0);
 }

 @Test
 public void testGlobalThisWarning() {
 String code = "function globalThisFunction() { return this; }";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertEquals(1, node.getChildren().size());
 assertEquals(GLOBAL_THIS.getMessage(), node.getFirstChild().toString());
 }

 @Test
 public void testFunctionWithPrototypePropertyAccess() {
 String code = "var MyClass = function() { this.name = 'Alice'; };\n" +
         "MyClass.prototype.method = function() { return this.name; }";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() > 0);
 }

 @Test
 public void testFunctionWithPrototypeMethod() {
 String code = "class MyClass {\n" +
         " constructor() { this.name = 'Alice'; }\n" +
         " method() { return this.name; }\n" +
         "}";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() > 0);
 Node methodNode = NodeUtil.getFirstDescendantByKind(node, Node.FUNCTION);
 assertEquals(methodNode.getFirstChild().getString(), "method");
}

 @Test
 public void testFunctionWithNoThisAnnotation() {
 String code = "function noThisAnnotationFunction() { return this; }";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() == 1);
 }

 @Test
 public void testAssignLeftSide() {
 String code = "var a = this;";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertEquals(1, node.getChildren().size());
 assertEquals(GLOBAL_THIS.getMessage(), node.getFirstChild().toString());
 }

 @Test
 public void testAssignRightSide() {
 String code = "var a = { get x() { return this; } };\n" +
         "a.x = this;";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() > 0);
 }

 @Test
 public void testFunctionWithObjectLiteralKey() {
 String code = "var myObject = {\n" +
         " method: function() { return this; };\n" +
         "};";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() > 0);
 }

 @Test
 public void testFunctionWithConstructorAnnotation() {
 String code = "@constructor\n" +
         "function MyConstructor() { this.name = 'Alice'; }";
 Node node = new Node(code);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() == 1);
 }

 @Test
 public void testFunctionWithInterfaceAnnotation() {
 String code = "@interface\n" +
         "MyInterface {\n" +
         " method(): void;\n" +
         "}";
 String functionCode = "function MyFunction implements MyInterface {\n" +
         " method() { return this; } }\n";
 Node node = new Node(functionCode);
 NodeTraversal traversal = new NodeTraversal(compiler);
 traversal.traverse(node, new CheckGlobalThis(compiler));
 traversal.detach();
 assertTrue(node.getChildCount() > 0);
 }
}