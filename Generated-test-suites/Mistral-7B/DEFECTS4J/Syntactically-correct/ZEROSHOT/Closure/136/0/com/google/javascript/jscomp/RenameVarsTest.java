import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;

public class RenameVarsTest {

 @Test
 public void testRenameVars() {
 Compiler compiler = mock(Compiler.class);
 CompilerInput input = mock(CompilerInput.class);
 NodeTraversal traversal = mock(NodeTraversal.class);
 Scope scope = mock(Scope.class);

 when(compiler.getTraversal()).thenReturn(traversal);
 when(traversal.getScope()).thenReturn(scope);

 RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);

 ListMultimap<String, Node> nodeMap = ArrayListMultimap.newSetMultimap();
 for (int i = 0; i < 5; i++) {
 String name = "var" + i;
 Node node = createNode(name);
 nodeMap.put(name, node);
 }

 when(scope.getVar(eq(NodeUtil.getIdentifier(nodeMap.get(0).get(0))))).thenReturn(createTestVar(0, nodeMap.get(0).get(0)));
 when(scope.getVar(eq(NodeUtil.getIdentifier(nodeMap.get(1).get(0))))).thenReturn(createTestVar(1, nodeMap.get(1).get(0)));
 when(scope.getVar(eq(NodeUtil.getIdentifier(nodeMap.get(2).get(0))))).thenReturn(createTestVar(2, nodeMap.get(2).get(0)));
 when(scope.getVar(eq(NodeUtil.getIdentifier(nodeMap.get(3).get(0))))).thenReturn(createTestVar(3, nodeMap.get(3).get(0)));
 when(scope.getVar(eq(NodeUtil.getIdentifier(nodeMap.get(4).get(0))))).thenReturn(createTestVar(4, nodeMap.get(4).get(0)));

 renameVars.process(createNode("externs"), createNode("root"));

 for (int i = 0; i < 5; i++) {
 String oldName = "var" + i;
 String newName = renameVars.getVariableMap().lookupName(oldName);
 assertEquals("Expected new name to match: " + oldName + " -> " + newName,
 "L" + i, newName);
 }
 }

 private Node createNode(String name) {
 Node node = new Node(Token.NAME, new int[0], null);
 node.setString(name);
 return node;
 }

 private Scope.Var createTestVar(int index, Node node) {
 Scope.Var var = mock(Scope.Var.class);
 when(var.getInput()).thenReturn(input);
 when(var.getLocalVarIndex()).thenReturn(index);
 when(var.getParentNode()).thenReturn(node);
 return var;
 }
}