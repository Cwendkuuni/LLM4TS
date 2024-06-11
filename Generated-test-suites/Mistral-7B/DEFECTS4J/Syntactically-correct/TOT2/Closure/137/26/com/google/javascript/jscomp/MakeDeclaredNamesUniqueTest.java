import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MakeDeclaredNamesUniqueTest {

 private static class TestScope extends Scope {
 @Override
 public void declare(String name, Node node, Node parent, Node declaration) {
 super.declare(name, node, parent, declaration);
 }
 }

 @Test
 public void testMakeDeclaredNamesUnique_NoConflicts() {
 // Given
 NodeTraversal traversal = mock(NodeTraversal.class);
 TestScope scope = new TestScope();
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setScope(scope);

 // When
 NodeTraversal.traverse(makeDeclaredNamesUnique, testCode, traversal);

 // Then
 List<String> expectedDeclaredNames = Arrays.asList(
 "name1", "name2", "name3", "name4");
 assertEquals(expectedDeclaredNames, getDeclaredNames(scope));
 }

 @Test
 public void testMakeDeclaredNamesUnique_OneConflict() {
 // Given
 NodeTraversal traversal = mock(NodeTraversal.class);
 TestScope scope = new TestScope();
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setScope(scope);

 // Set up the test case with one conflict
 String conflictingName = "name1";
 Node conflictingNode = Node.newTextNode("conflictingName");
 Node conflictingDeclarationNode = Node.newTextNode("name1");
 scope.declare(conflictingName, conflictingNode, null, conflictingDeclarationNode);

 String newName = "name5";
 Node newNameNode = Node.newTextNode(newName);
 Node newDeclarationNode = Node.newTextNode(newName);
 scope.declare(newName, newNameNode, null, newDeclarationNode);

 // When
 NodeTraversal.traverse(makeDeclaredNamesUnique, testCode, traversal);

 // Then
 List<String> expectedDeclaredNames = Arrays.asList(
 "name1$$0", "name2", "name3", "name4", "name5");
 assertEquals(expectedDeclaredNames, getDeclaredNames(scope));

 // Verify that the conflicting name was renamed
 assertEquals(conflictingName, makeDeclaredNamesUnique.getReplacementName("name1$$0"));
 }

 @Test
 public void testMakeDeclaredNamesUnique_MultipleConflicts() {
 // Given
 NodeTraversal traversal = mock(NodeTraversal.class);
 TestScope scope = new TestScope();
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setScope(scope);

 // Set up the test case with multiple conflicts
 String conflictingName1 = "name1";
 Node conflictingNode1 = Node.newTextNode("conflictingName1");
 Node conflictingDeclarationNode1 = Node.newTextNode("name1");
 scope.declare(conflictingName1, conflictingNode1, null, conflictingDeclarationNode1);

 String conflictingName2 = "name2";
 Node conflictingNode2 = Node.newTextNode("conflictingName2");
 Node conflictingDeclarationNode2 = Node.newTextNode("name2");
 scope.declare(conflictingName2, conflictingNode2, null, conflictingDeclarationNode2);

 String newName1 = "name6";
 Node newNameNode1 = Node.newTextNode(newName1);
 Node newDeclarationNode1 = Node.newTextNode(newName1);
 scope.declare(newName1, newNameNode1, null, newDeclarationNode1);

 String newName2 = "name7";
 Node newNameNode2 = Node.newTextNode(newName2);
 Node newDeclarationNode2 = Node.newTextNode(newName2);
 scope.declare(newName2, newNameNode2, null, newDeclarationNode2);

 // When
 NodeTraversal.traverse(makeDeclaredNamesUnique, testCode, traversal);

 // Then
 List<String> expectedDeclaredNames = Arrays.asList(
 "name1$$1", "name1$$2", "name2", "name3", "name4", "name6", "name7");
 assertEquals(expectedDeclaredNames, getDeclaredNames(scope));

 // Verify that the conflicting names were renamed
 assertEquals(conflictingName1, makeDeclaredNamesUnique.getReplacementName("name1$$1"));
 assertEquals(conflictingName2, makeDeclaredNamesUnique.getReplacementName("name1$$2"));
 }

 @Test
 public void testMakeDeclaredNamesUnique_ReusesUniqueNames() {
 // Given
 NodeTraversal traversal = mock(NodeTraversal.class);
 TestScope scope = new TestScope();
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setScope(scope);
 makeDeclaredNamesUnique.setNameStack(new ArrayDeque<Renamer>());
 makeDeclaredNamesUnique.setRootRenamer(makeDeclaredNamesUnique);

 // Set up the test case where the same name is used in multiple scopes
 String conflictingName = "name1";
 Node conflictingNode1 = Node.newTextNode("conflictingName1");
 Node conflictingDeclarationNode1 = Node.newTextNode("name1");
 scope.declare(conflictingName, conflictingNode1, null, conflictingDeclarationNode1);
 Node parentNode1 = Node.newTextNode("function1(){}");
 Node parentNode1Declaration = Node.newTextNode("function1(){}");
 scope.declare(parentNode1, parentNode1, null, parentNode1Declaration);
 TestScope childScope1 = new TestScope();
 makeDeclaredNamesUnique.setScope(childScope1);
 Node childScope1Declaration = Node.newTextNode("function2(){}");
 scope.declare(childScope1Declaration, parentNode1, null, null);
 TestScope childScope2 = new TestScope();
 makeDeclaredNamesUnique.setScope(childScope2);
 Node childScope2Declaration = Node.newTextNode("function3(){}");
 scope.declare(childScope2Declaration, parentNode1, null, null);
 String newName = "name1";
 Node newNameNode = Node.newTextNode(newName);
 Node newDeclarationNode = Node.newTextNode(newName);
 scope.declare(newNameNode, null, parentNode1, newDeclarationNode);

 // When
 NodeTraversal.traverse(makeDeclaredNamesUnique, testCode, traversal);

 // Then
 List<String> expectedDeclaredNames = Arrays.asList(
 "name1", "function1(){}", "function2(){}", "function3(){}",
 "name1$$1", "name1$$2", "name1$$3");
 assertEquals(expectedDeclaredNames, getDeclaredNames(scope));

 // Verify that the conflicting names were renamed in the correct scope
 assertEquals(conflictingName, makeDeclaredNamesUnique.getReplacementName("name1$$1"));
 assertEquals(conflictingName, makeDeclaredNamesUnique.getReplacementName("name1$$2"));
 assertEquals(conflictingName, makeDeclaredNamesUnique.getReplacementName("name1$$3"));

}

 @Test
 public void testMakeDeclaredNamesUnique_GlobalNamesAreNotRenamed() {
 // Given
 NodeTraversal traversal = mock(NodeTraversal.class);
 TestScope globalScope = new TestScope();
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setScope(globalScope);

 // When
 NodeTraversal.traverse(makeDeclaredNamesUnique, testCode, traversal);

 // Then
 List<String> expectedDeclaredNames = Arrays.asList(
 "name1", "name2", "name3", "name4", "name5");
 assertEquals(expectedDeclaredNames, getDeclaredNames(globalScope));

 // Verify that global names were not renamed
 assertEquals("name1", makeDeclaredNamesUnique.getReplacementName("name1"));
 }

 @Test(expected = RuntimeException.class)
 public void testMakeDeclaredNamesUnique_ParentRenamerIsNotSet_ThrowsException() {
 // Given
 NodeTraversal traversal = mock(NodeTraversal.class);
 TestScope scope = new TestScope();
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setScope(scope);
 makeDeclaredNamesUnique.setNameStack(new ArrayDeque<Renamer>());
 makeDeclaredNamesUnique.setRootRenamer(null);

 // When and Then
 // This test case should throw an exception
 }

 @Test(expected = RuntimeException.class)
 public void testMakeDeclaredNamesUnique_EmptyNameStack_ThrowsException() {
 // Given
 NodeTraversal traversal = mock(NodeTraversal.class);
 TestScope scope = new TestScope();
 MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
 makeDeclaredNamesUnique.setScope(scope);
 makeDeclaredNamesUnique.setNameStack(new ArrayDeque<Renamer>());
 makeDeclaredNamesUnique.setRootRenamer(makeDeclaredNamesUnique);
 makeDeclaredNamesUnique.popNameStack();

 // When and Then
 // This test case should throw an exception
 }

 private List<String> getDeclaredNames(Scope scope) {
 List<String> declaredNames = new ArrayList<>();
 Iterator<Map.Entry<String, Var>> iterator = scope.getAllVars().iterator();
 while (iterator.hasNext()) {
 String name = iterator.next().getKey();
 declaredNames.add(name);
 }
 return declaredNames;
 }

 private static Node testCode = NodeUtil.parseJs(
 "function testFunction() {" +
 "  var name1 = 'test';" +
 "  var name2 = 'test2';" +
 "  var name3 = 'test3';" +
 "  var name4 = 'test4';" +
 "}"
 );
}