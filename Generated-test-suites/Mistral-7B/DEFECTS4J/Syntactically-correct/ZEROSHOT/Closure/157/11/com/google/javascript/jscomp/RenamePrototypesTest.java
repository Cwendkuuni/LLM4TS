import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import java.util.HashMap;
import java.util.Map;

public class RenamePrototypesTest {

  @SuppressWarnings("unchecked")
  private static <T> T mockNode() {
    return mock(Node.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> T mockCompilerInput() {
    return mock(CompilerInput.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> T mockAbstractCompiler() {
    return mock(AbstractCompiler.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> T mockVariableMap() {
    return mock(VariableMap.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> T mockCompilerPassManager() {
    return mock(CompilerPassManager.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> T mockNodeTraversal() {
    return mock(NodeTraversal.class);
  }

  private RenamePrototypes createTestee(boolean aggressiveRenaming) {
    CompilerInput input = mockCompilerInput();
    AbstractCompiler compiler = mockAbstractCompiler();
    VariableMap prevUsedRenameMap = mockVariableMap();
    char[] reservedCharacters = new char[0];

    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));
    when(compiler.getLifeCycleStage()).thenReturn(AbstractCompiler.LifeCycleStage.NORMALIZED);

    RenamePrototypes testee = new RenamePrototypes(compiler, aggressiveRenaming,
        reservedCharacters, prevUsedRenameMap);

    return testee;
  }

  @org.junit.Test
  public void testProcess() {
    AbstractCompiler compiler = mockAbstractCompiler();
    RenamePrototypes testee = createTestee(false);

    CompilerPassManager manager = mockCompilerPassManager();
    when(manager.getNextPass()).thenReturn(testee);

    Node root = mockNode();
    Node externs = mockNode();

    NodeTraversal traversal = mockNodeTraversal();
    when(compiler.getTraverser()).thenReturn(traversal);

    testee.process(externs, root);

    assertEquals(3, testee.properties.size());

    String oldName1 = "foo";
    String oldName2 = "bar_baz";
    String oldName3 = "qux__quux";

    String newName1 = "renamedFoo";
    String newName2 = "bar_baz_renamed";
    String newName3 = "qux__quux_renamed";

    Property prop1 = testee.properties.get(oldName1);
    Property prop2 = testee.properties.get(oldName2);
    Property prop3 = testee.properties.get(oldName3);

    assertEquals(prop1.count(), 1);
    assertEquals(prop1.oldName, oldName1);
    assertEquals(prop1.newName, newName1);

    assertEquals(prop2.count(), 2);
    assertEquals(prop2.oldName, oldName2);
    assertEquals(prop2.newName, newName2);

    assertEquals(prop3.count(), 1);
    assertEquals(prop3.oldName, oldName3);
    assertEquals(prop3.newName, newName3);

    assertEquals(externs.getClass(), CompilerInput.class);
    assertEquals(root.getClass(), Node.class);
  }

  @org.junit.Test
  public void testProcessWithAggressiveRenaming() {
    AbstractCompiler compiler = mockAbstractCompiler();
    RenamePrototypes testee = createTestee(true);

    CompilerPassManager manager = mockCompilerPassManager();
    when(manager.getNextPass()).thenReturn(testee);

    Node root = mockNode();
    Node externs = mockNode();

    NodeTraversal traversal = mockNodeTraversal();
    when(compiler.getTraverser()).thenReturn(traversal);

    testee.process(externs, root);

    Map<String, Property> properties = testee.properties;

    assertEquals(6, properties.size());

    String oldName1 = "foo";
    String oldName2 = "bar_baz";
    String oldName3 = "qux__quux";
    String oldName4 = "qux";
    String oldName5 = "indexof";
    String oldName6 = "toString";

    String newName1 = "renamedFoo";
    String newName2 = "bar_baz_renamed";
    String newName3 = "qux__quux_renamed";
    String newName4 = "renamedQux";
    String newName5 = "_indexOf";
    String newName6 = "_toString";

    Property prop1 = properties.get(oldName1);
    Property prop2 = properties.get(oldName2);
    Property prop3 = properties.get(oldName3);
    Property prop4 = properties.get(oldName4);
    Property prop5 = properties.get(oldName5);
    Property prop6 = properties.get(oldName6);

    assertEquals(prop1.count(), 1);
    assertEquals(prop1.oldName, oldName1);
    assertEquals(prop1.newName, newName1);

    assertEquals(prop2.count(), 2);
    assertEquals(prop2.oldName, oldName2);
    assertEquals(prop2.newName, newName2);

    assertEquals(prop3.count(), 1);
    assertEquals(prop3.oldName, oldName3);
    assertEquals(prop3.newName, newName3);

    assertEquals(prop4.count(), 1);
    assertEquals(prop4.oldName, oldName4);
    assertEquals(prop4.newName, newName4);

    assertEquals(prop5.count(), 1);
    assertEquals(prop5.oldName, oldName5);
    assertEquals(prop5.newName, newName5);

    assertEquals(prop6.count(), 1);
    assertEquals(prop6.oldName, oldName6);
    assertEquals(prop6.newName, newName6);
  }

  @org.junit.Test
  public void testProcessWithExistingProperties() {
    AbstractCompiler compiler = mockAbstractCompiler();
    RenamePrototypes testee = createTestee(false);

    CompilerPassManager manager = mockCompilerPassManager();
    when(manager.getNextPass()).thenReturn(testee);

    VariableMap prevUsedRenameMap = mockVariableMap();
    when(prevUsedRenameMap.lookupOldName("foo")).thenReturn("renamedFoo");
    when(prevUsedRenameMap.lookupOldName("bar_baz")).thenReturn("renamedBarBaz");

    Node root = mockNode();
    Node externs = mockNode();

    NodeTraversal traversal = mockNodeTraversal();
    when(compiler.getTraverser()).thenReturn(traversal);

    testee.process(externs, root);

    Map<String, Property> properties = testee.properties;

    assertEquals(3, properties.size());

    String oldName1 = "foo";
    String oldName2 = "bar_baz";

    Property prop1 = properties.get(oldName1);
    Property prop2 = properties.get(oldName2);

    assertEquals(prop1.count(), 1);
    assertEquals(prop1.oldName, oldName1);
    assertEquals(prop1.newName, "renamedFoo");

    assertEquals(prop2.count(), 2);
    assertEquals(prop2.oldName, oldName2);
    assertEquals(prop2.newName, "renamedBarBaz");
  }

  @org.junit.Test
  public void testProcessWithoutRenaming() {
    AbstractCompiler compiler = mockAbstractCompiler();
    RenamePrototypes testee = createTestee(false);

    CompilerPassManager manager = mockCompilerPassManager();
    when(manager.getNextPass()).thenReturn(testee);

    VariableMap prevUsedRenameMap = mockVariableMap();
    when(prevUsedRenameMap.lookupOldName("foo")).thenReturn("renamedFoo");
    when(prevUsedRenameMap.lookupOldName("bar_baz")).thenReturn("renamedBarBaz");

    Set<String> reservedNames = testee.reservedNames;

    assertEquals(2, reservedNames.size());

    String oldName1 = "indexOf";
    String oldName2 = "toString";

    assertEquals(reservedNames.contains(oldName1), true);
    assertEquals(reservedNames.contains(oldName2), true);
  }
}