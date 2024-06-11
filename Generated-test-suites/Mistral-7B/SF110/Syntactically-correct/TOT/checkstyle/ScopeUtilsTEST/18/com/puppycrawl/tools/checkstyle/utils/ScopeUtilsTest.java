import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;

public class ScopeUtilsTest {

 @Test
 public void testGetScopeFromMods() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 ast.setType(1); // Set mock DetailAST type to represent a modifier list
 ast.setFirstChild(createToken("public"));
 ast.setNextSibling(createToken("private"));
 ast.setNextSibling(createToken("protected"));

 Scope expectedScope = Scope.PROTECTED;
 Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_NoMatchingModifier() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 ast.setType(1); // Set mock DetailAST type to represent a modifier list
 ast.setFirstChild(createToken("abstract"));
 ast.setNextSibling(createToken("final"));

 Scope expectedScope = Scope.PACKAGE;
 Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetScopeFromMods_MultipleMatchingModifiers() {
 DetailAST ast = new DetailAST(); // Create a mock DetailAST for testing
 ast.setType(1); // Set mock DetailAST type to represent a modifier list
 ast.setFirstChild(createToken("public"));
 ast.setNextSibling(createToken("protected"));
 ast.setNextSibling(createToken("private"));

 Scope expectedScope = Scope.PROTECTED; // Protected has the highest precedence
 Scope actualScope = ScopeUtils.getScopeFromMods(ast);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testGetSurroundingScope_InClass() {
 DetailAST astNode = createNode("ClassName { ... }"); // Create a DetailAST representing a class
 DetailAST scopeNode = ScopeUtils.getSurroundingScope(astNode);
 Scope expectedScope = Scope.CLASS;
 assertEquals(expectedScope, scopeNode);
 }

 @Test
 public void testGetSurroundingScope_InMethod() {
 DetailAST astNode = createNode("ClassName methodName() { ... }"); // Create a DetailAST representing a method
 DetailAST scopeNode = ScopeUtils.getSurroundingScope(astNode);
 Scope expectedScope = Scope.METHOD;
 assertEquals(expectedScope, scopeNode);
 }

 @Test
 public void testGetSurroundingScope_InAnnotation() {
 DetailAST astNode = createNode("@AnnotationName(value = \"value\")"); // Create a DetailAST representing an annotation
 DetailAST scopeNode = ScopeUtils.getSurroundingScope(astNode);
 Scope expectedScope = Scope.ANNOTATION;
 assertEquals(expectedScope, scopeNode);
 }

 @Test
 public void testGetSurroundingScope_InInterface() {
 DetailAST astNode = createNode("interface InterfaceName { ... }"); // Create a DetailAST representing an interface
 DetailAST scopeNode = ScopeUtils.getSurroundingScope(astNode);
 Scope expectedScope = Scope.INTERFACE;
 assertEquals(expectedScope, scopeNode);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_InInterfaceBlock() {
 DetailAST astNode = createNode("interface InterfaceName { ... }"); // Create a DetailAST representing an interface
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_InAnnotationBlock() {
 DetailAST astNode = createNode("@AnnotationName(value = \"value\")"); // Create a DetailAST representing an annotation
 boolean expectedResult = true;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_InCodeBlock() {
 DetailAST astNode = createNode("public class ClassName { int x = 10; }"); // Create a DetailAST representing a code block
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsInInterfaceOrAnnotationBlock_InMethod() {
 DetailAST astNode = createNode("public class ClassName { public void methodName() { } }"); // Create a DetailAST representing a method
 boolean expectedResult = false;
 boolean actualResult = ScopeUtils.isInInterfaceOrAnnotationBlock(astNode);
 assertEquals(expectedResult, actualResult);
 }

 private DetailAST createToken(String text) {
 DetailAST token = new DetailAST();
 token.setText(text);
 return token;
 }

 private DetailAST createNode(String nodeText) {
 DetailAST node = new DetailAST();
 node.setText(nodeText);
 return node;
 }
}