import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

 private Node node;
 private NodeTraversal traversal;
 private MakeDeclaredNamesUnique renamer;

 @Before
 public void setup() {
 node = Node.objectLiteral();
 traversal = mock(NodeTraversal.class);
 renamer = new MakeDeclaredNamesUnique();
 }

 @Test
 public void testEnterScopeWithFunctionDeclarationAndNoConflicts() {
 // Given
 when(traversal.getScopeRoot()).thenReturn(node);
 when(traversal.getScope()).thenReturn(mock(Scope.class));

 // When
 renamer.enterScope(traversal);

 // Then
 Renamer renamerOnStack = nameStack.peek();
 assertEquals("rootRenamer", renamerOnStack.getClass().getName());
 }

 @Test
 public void testEnterScopeWithFunctionDeclarationAndConflicts() {
 // Given
 String conflictName = "conflictName";
 Node functionDeclaration = Node.functionDeclaration("functionDeclaration", Lists.newArrayList(
 Node.parameter("param1"), Node.parameter("param2"), Node.statement(Node.assignment(conflictName, Node.numberLiteral(1)))
 ));
 when(traversal.getScopeRoot()).thenReturn(Node.functionDeclaration("functionDeclaration", Lists.newArrayList()));
 when(traversal.getScope()).thenReturn(mock(Scope.class));
 when(traversal.getNodeAtIndexInParent(0)).thenReturn(functionDeclaration);

 // When
 renamer.enterScope(traversal);

 // Then
 Renamer renamerOnStack = nameStack.peek();
 assertEquals("ContextualRenamer", renamerOnStack.getClass().getName());
 assertEquals("functionDeclaration" + ContextualRenamer.UNIQUE_ID_SEPARATOR + "1",
 renamerOnStack.getReplacementName(conflictName));
 }

 @Test
 public void testExitScope() {
 // Given
 when(traversal.getScopeRoot()).thenReturn(node);
 when(traversal.getScope()).thenReturn(mock(Scope.class));
 Renamer renamerOnStack = new ContextualRenamer();
 nameStack.push(renamerOnStack);

 // When
 renamer.exitScope(traversal);

 // Then
 assertEquals(renamerOnStack, nameStack.pop());
 }

 @Test
 public void testShouldTraverseNode() {
 // Given
 when(traversal.getScopeRoot()).thenReturn(node);
 when(traversal.getNodeAtIndexInParent(0)).thenReturn(node);

 // When
 boolean result = renamer.shouldTraverse(traversal, node, node);

 // Then
 assertTrue(result);
 }

 @Test
 public void testVisitNameNodeWithNoRename() {
 // Given
 when(traversal.getScopeRoot()).thenReturn(node);
 when(traversal.getNodeAtIndexInParent(0)).thenReturn(node);

 // When
 renamer.visit(traversal, node, node);

 // Then
 assertEquals("", node.getString());
 }

 @Test
 public void testVisitNameNodeWithRename() {
 // Given
 String oldName = "oldName";
 String newName = "newName";
 when(traversal.getScopeRoot()).thenReturn(node);
 when(traversal.getNodeAtIndexInParent(0)).thenReturn(node);
 when(traversal.getScope()).thenReturn(mock(Scope.class));
 when(traversal.getCompiler()).thenReturn(mock(AbstractCompiler.class));

 // When
 renamer.visit(traversal, node, node);

 // Then
 assertEquals(newName, node.getString());
 }

 @Test
 public void testVisitFunctionNodeWithNoRename() {
 // Given
 when(traversal.getScopeRoot()).thenReturn(node);
 when(traversal.getNodeAtIndexInParent(0)).thenReturn(node);

 // When
 renamer.visit(traversal, node, node);

 // Then
 // This is expected to not do anything, as functions are not renamed.
 }
}