import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

 private AbstractCompiler compiler;
 private ScopeCreator scanner;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 scanner = new SyntacticScopeCreator(compiler);
 }

 @Test
 public void testCreateScope() {
 Node rootNode = createTestNode("function foo() {}");
 Scope scope = scanner.createScope(rootNode, null);
 assertEquals(scope.getName(), "foo");
 }

 @Test
 public void testCreateScopeWithArguments() {
 Node rootNode = createTestNode("function foo(a, b) {}");
 Scope scope = scanner.createScope(rootNode, null);
 assertEquals(scope.getVariable("arguments").getType(), JSType.ARRAY_TYPE);
 }

 @Test
 public void testCreateGlobalScope() {
 Node rootNode = createTestNode("(function() { var x = 1; })()");
 Scope scope = scanner.createScope(rootNode, null);
 assertEquals(scope.getVariable("x").getType(), JSType.NUMBER_TYPE);
 }

 @Test
 public void testCreateScopeWithDuplicateVariable() {
 Node rootNode = createTestNode("function foo() {" +
     "var x = 1;" +
     "var x = 2;" +
     "}");
 try {
 Scope scope = scanner.createScope(rootNode, null);
 fail("Expected duplication error");
 } catch (IllegalArgumentException e) {
 assertEquals(e.getMessage(), "Variable x first declared in foo");
 }
 }

 @Test
 public void testCreateScopeWithDuplicateArguments() {
 Node rootNode = createTestNode("function foo(x, x) {}");
 try {
 Scope scope = scanner.createScope(rootNode, null);
 fail("Expected duplication error");
 } catch (IllegalArgumentException e) {
 assertEquals(e.getMessage(), "Variable x first declared in foo");
 }
 }

 @Test
 public void testCreateScopeWithDuplicateGlobal() {
 Node rootNode = createTestNode("(function() {" +
     "var x = 1;" +
     "var x = 2;" +
     "})()");
 try {
 Scope scope = scanner.createScope(rootNode, null);
 fail("Expected duplication error");
 } catch (IllegalArgumentException e) {
 assertEquals(e.getMessage(), "Variable x first declared in null");
 }
 }

 @Test
 public void testCreateScopeWithFunctionDeclaration() {
 Node rootNode = createTestNode("function foo() {" +
     "function bar() {}" +
     "}");
 Scope scope = scanner.createScope(rootNode, null);
 assertEquals(scope.getFunction("bar").getName(), "bar");
 }

 @Test
 public void testCreateScopeWithCatchBlock() {
 Node rootNode = createTestNode("try {" +
     "throw new Error('test')" +
     "} catch (e) {}" +
     "function foo() {}");
 Scope scope = scanner.createScope(rootNode, null);
 assertEquals(scope.getVariable("e").getType(), JSType.ERROR_TYPE);
 }

 private Node createTestNode(String code) {
 Node rootNode = new Node(Token.SCRIPT);
 rootNode.setSource(new StringReader(code));
 return rootNode;
 }

}