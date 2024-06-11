import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

 private Compiler compiler;
 private MakeDeclaredNamesUnique makeDeclaredNamesUnique;

 @Before
 public void setup() {
 compiler = new Compiler();
 makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 compiler.addPass(makeDeclaredNamesUnique);
 }

 @Test
 public void testContextualRename() {
 // Given a simple program with no name conflicts
 String sourceCode = "function foo() {" +
 "  var x = 1;" +
 "  var y = 2;" +
 "  function bar() {" +
 "    var x = 3;" +
 "  }" +
 "}";
 Node ast = compiler.parse(sourceCode, null, null);

 NodeTraversal traversal = new NodeTraversal(ast, compiler);
 traversal.traverse();

 // Then the names should be unique in the function scope
 assertFalse(NodeUtil.isFunctionDeclaration(traversal.getScopeRoot().getFirstChild()));
 Node functionBody = traversal.getScopeRoot().getLastChild();
 List<Node> declarations = Lists.newArrayList();
 for (Node c = functionBody.getFirstChild(); c != null; c = c.getNext()) {
 if (NodeUtil.isVarDeclaration(c)) {
 declarations.add(c);
 }
 }
 for (Node declaration : declarations) {
 String oldName = declaration.getString();
 String newName = makeDeclaredNamesUnique.getReplacementName(oldName);
 assertEquals(oldName, newName);
 assertEquals(oldName, declaration.getString());
}

 // And the names should be unique in the local scope of the bar function
 Node functionBar = null;
 for (Node c = functionBody.getFirstChild(); c != null; c = c.getNext()) {
 if (c.getType() == Token.FUNCTION) {
 functionBar = c;
 break;
 }
 }
 Node barFunctionBody = functionBar.getLastChild();
 List<Node> barDeclarations = Lists.newArrayList();
 for (Node c = barFunctionBody.getFirstChild(); c != null; c = c.getNext()) {
 if (NodeUtil.isVarDeclaration(c)) {
 barDeclarations.add(c);
 }
 }
 for (Node declaration : barDeclarations) {
 String oldName = declaration.getString();
 String newName = makeDeclaredNamesUnique.getReplacementName(oldName);
 assertNotEquals(oldName, newName);
 assertEquals(newName, declaration.getString());
 }
 }

 @Test
 public void testContextualRenameWithConflictingNames() {
 // Given a program with conflicting names
 String sourceCode = "function foo() {" +
 "  var x = 1;" +
 "  var y = 2;" +
 "  function bar() {" +
 "    var x = 3;" +
 "  }" +
 "  var x = 4;" +
 "}";
 Node ast = compiler.parse(sourceCode, null, null);

 NodeTraversal traversal = new NodeTraversal(ast, compiler);
 traversal.traverse();

 // Then the names should be renamed to be unique
 assertFalse(NodeUtil.isFunctionDeclaration(traversal.getScopeRoot().getFirstChild()));
 Node functionBody = traversal.getScopeRoot().getLastChild();
 List<Node> declarations = Lists.newArrayList();
 for (Node c = functionBody.getFirstChild(); c != null; c = c.getNext()) {
 if (NodeUtil.isVarDeclaration(c)) {
 declarations.add(c);
 }
 }
 for (Node declaration : declarations) {
 String oldName = declaration.getString();
 String newName = makeDeclaredNamesUnique.getReplacementName(oldName);
 assertNotEquals(oldName, newName);
 assertEquals(newName, declaration.getString());
 }
 }

 @Test
 public void testInlineRename() {
 // Given a program with inline renaming
 String sourceCode = "function foo() {" +
 "  function bar() {" +
 "    var x = 1;" +
 "  }" +
 "}";
 MakeDeclaredNamesUnique inlineRenamer = new MakeDeclaredNamesUnique(
 new InlineRenamer(
 new Supplier<String>() {
 @Override
 public String get() {
 return "i";
 }
 },
 "_i",
 true));
 compiler.addPass(inlineRenamer);
 Node ast = compiler.parse(sourceCode, null, null);

 NodeTraversal traversal = new NodeTraversal(ast, compiler);
 traversal.traverse();

 // Then the names should be unique within the function scope
 assertFalse(NodeUtil.isFunctionDeclaration(traversal.getScopeRoot().getFirstChild()));
 Node functionBody = traversal.getScopeRoot().getLastChild();
 List<Node> declarations = Lists.newArrayList();
 for (Node c = functionBody.getFirstChild(); c != null; c = c.getNext()) {
 if (NodeUtil.isVarDeclaration(c)) {
 declarations.add(c);
 }
 }
 for (Node declaration : declarations) {
 String oldName = declaration.getString();
 String newName = inlineRenamer.getReplacementName(oldName);
 assertNotEquals(oldName, newName);
 assertEquals(newName, declaration.getString());
 }

 // And the names should be unique within the bar function scope
 Node functionBar = null;
 for (Node c = functionBody.getFirstChild(); c != null; c = c.getNext()) {
 if (c.getType() == Token.FUNCTION) {
 functionBar = c;
 break;
 }
 }
 Node barFunctionBody = functionBar.getLastChild();
 List<Node> barDeclarations = Lists.newArrayList();
 for (Node c = barFunctionBody.getFirstChild(); c != null; c = c.getNext()) {
 if (NodeUtil.isVarDeclaration(c)) {
 barDeclarations.add(c);
 }
 }
 for (Node declaration : barDeclarations) {
 String oldName = declaration.getString();
 String newName = inlineRenamer.getReplacementName(oldName);
 assertNotEquals(oldName, newName);
 assertEquals(newName, declaration.getString());
 }
 }
}