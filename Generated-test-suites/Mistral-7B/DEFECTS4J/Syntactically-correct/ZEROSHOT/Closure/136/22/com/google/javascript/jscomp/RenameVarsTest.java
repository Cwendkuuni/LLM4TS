import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.VariableMap;
import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    String prefix = "my_prefix";
    boolean localRenamingOnly = false;
    boolean preserveAnonymousFunctionNames = false;
    boolean generatePseudoNames = false;
    VariableMap prevUsedRenameMap = null;
    char[] reservedCharacters = new char[]{'_'};
    Set<String> reservedNames = new HashSet<>();

    RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly,
        preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
        reservedCharacters, reservedNames);

    // Test empty input
    renameVars.process(Node.newNull(), Node.newNull());
    assertEquals(0, renameVars.assignments.size());

    // Test global variables
    CompilerInput input1 = new CompilerInput("input1.js", 1, 1);
    CompilerInput input2 = new CompilerInput("input2.js", 1, 1);
    Node externs = Node.newObjectLiteral();
    Node root = Node.newProgram(Node.newList(), Node.newList());

    Node nameNode1 = Node.newName("var1", 1, 1, input1);
    Node valueNode1 = Node.newString("value1");
    Node nameNode2 = Node.newName("var2", 1, 1, input2);
    Node valueNode2 = Node.newNumber(1);

    externs.addChildToStatistics(nameNode1);
    externs.addChildToStatistics(valueNode1);
    externs.addChildToStatistics(nameNode2);
    externs.addChildToStatistics(valueNode2);

    root.addChildToStatistics(externs);

    renameVars.process(externs, root);
    ListMultimap<String, CompilerInput> expectedGlobalMap =
        ArrayListMultimap.newSetMap(Maps.newHashMap());
    expectedGlobalMap.putAll(renameVars.getVariableMap().getVariableMap());
    assertEquals(expectedGlobalMap.size(), renameVars.assignments.size());
    for (Assignment assignment : renameVars.assignments.values()) {
      if (!assignment.newName.equals(assignment.oldName)) {
        assertEquals(input1.getSourceName(), assignment.input.getSourceName());
      }
    }

    // Test local variables
    Scope scope = mock(Scope.class);
    when(scope.getVar(anyString())).thenReturn(null);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, scope, root);
    Node localScope = Node.newBlock(Node.newList(), Node.newList());
    Node functionNode = Node.newFunction(Node.newName("func", 1, 1), Node.newList(),
        Node.newList(), Node.newList(), Node.newList(), Node.newList());
    Node statementNode = Node.newVariableDeclarationStatement(
        Node.newList(Node.newName("local1", 1, 1), Node.newName("local2", 1, 1)),
        Node.newList(valueNode1, valueNode2));
    localScope.addChildToStatistics(statementNode);
    functionNode.addChildToStatistics(localScope);
    root.addChildToStatistics(functionNode);

    NodeTraversal.traverse(nodeTraversal, root);

    nodeTraversal.setEnterCallback(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.NAME && n.getString().equals("local1")) {
          Preconditions.checkState(scope.getVar(n.getString()) == null);
          Node localNameNode = n;
          renameVars.globalNameNodes.clear();
          renameVars.localNameNodes.clear();
          renameVars.localTempNames.clear();
          renameVars.process(Node.newObjectLiteral(), root);
          String newName = renameVars.getVariableMap().lookupName(
              "local1", localNameNode.getInput());
          assertEquals("L 0", newName);
        }
      }
    });

    nodeTraversal.traverse(root);
  }
}