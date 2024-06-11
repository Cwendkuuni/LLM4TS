import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

 @Test
 public void testMakeDeclaredNamesUniqueBasic() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { var a = 1; var a = 2; }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getFirstChild().getString());
 assertEquals("a2", ast.getFirstChild().getLastChild().getFirstChild().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueFunctionDeclaration() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo(a) { var a = 1; }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueFunctionCall() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { return bar(); } function bar() {}");
 nodeTraversal.traverse(ast, null);
 assertNull(ast.getFirstChild().getLastChild().getFirstChild());
 }

 @Test(expected = AssertionError.class)
 public void testMakeDeclaredNamesUniqueRecursiveFunctionDeclaration() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { return foo(); }");
 nodeTraversal.traverse(ast, null);
 }

 @Test
 public void testMakeDeclaredNamesUniqueGlobalVariable() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("var a = 1; function foo() { var a = 2; }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getString());
 assertEquals("a2", ast.getLastChild().getFirstChild().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariable() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { var a = 1; var a; }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("a2", ast.getFirstChild().getLastChild().getNext().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariablesWithSameName() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { var a = 1; var a = 2; }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("a2", ast.getFirstChild().getLastChild().getNext().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariablesWithDifferentNames() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { var a = 1; var b = 2; }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("b1", ast.getFirstChild().getLastChild().getNext().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariablesWithSameNameInDifferentScopes() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { var a = 1; if (true) { var a = 2; } }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("a2", ast.getFirstChild().getLastChild().getNext().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariablesWithSameNameInSameScope() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { var a = 1; var a = 2; }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("a2", ast.getFirstChild().getLastChild().getLastChild().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariablesWithSameNameInParentScope() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { var a = 1; function bar() { var a = 2; } }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getString());
 assertEquals("a2", ast.getLastChild().getFirstChild().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariablesWithSameNameInSameParentScope() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { function bar() { var a = 1; var a = 2; } }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("a2", ast.getFirstChild().getLastChild().getLastChild().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariablesWithSameNameInSameParentScopeDifferentScopes() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { if (true) { function bar() { var a = 1; } var a = 2; } }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("a2", ast.getFirstChild().getLastChild().getLastChild().getString());
 }

 @Test
 public void testMakeDeclaredNamesUniqueBlockVariablesWithSameNameInSameParentScopeDifferentScopesDifferentNames() {
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 NodeTraversal nodeTraversal = new NodeTraversal(null, null);
 nodeTraversal.addPass(makeDeclaredNamesUnique);
 Node ast = NodeUtil.parseString("function foo() { if (true) { function bar() { var a = 1; } var b = 2; } }");
 nodeTraversal.traverse(ast, null);
 assertEquals("a1", ast.getFirstChild().getLastChild().getFirstChild().getString());
 assertEquals("b1", ast.getFirstChild().getLastChild().getLastChild().getString());
 }

}