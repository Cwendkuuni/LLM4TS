import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;

public class RenameVarsTest {
  @SuppressWarnings("unchecked")
  @org.junit.Before
  public void setup() {
    // Create a mock AbstractCompiler for testing purposes.
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));
    Node node = createNode("function foo() { var a = 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, node, new BasicErrorReporter());
    renameVars.process(node, node);
    renameVars.getVariableMap();
  }

  @org.junit.Test
  public void testRenameGlobalVariables() {
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("a", variableMap.lookupName("a"));
  }

  @org.junit.Test
  public void testRenameLocalVariables() {
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("L0", variableMap.lookupName("a"));
  }

  @org.junit.Test
  public void testRenameGlobalVariablesWithPrefix() {
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "prefix_", false, false, false, null, null, null);
    Node node = createNode("function foo() { var a = 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("prefix_a", variableMap.lookupName("a"));
  }

  @org.junit.Test
  public void testRenameLocalVariablesWithPrefix() {
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "prefix_", false, false, false, null, null, null);
    Node node = createNode("function foo() { var a = 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("prefix_L0", variableMap.lookupName("a"));
  }

  @org.junit.Test
  public void testNoRenameVariablesWithPrefix() {
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "prefix_", true, false, false, null, null, null);
    Node node = createNode("function foo() { var a = 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("a", variableMap.lookupName("a"));
  }

  @org.junit.Test
  public void testRenameVariablesWithReservedNames() {
    Set<String> reservedNames = new HashSet<>();
    reservedNames.add("console");
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "", false, false, false, null, null, reservedNames);
    Node node = createNode("console.log(1)");
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("console$", variableMap.lookupName("console"));
  }

  @org.junit.Test
  public void testRenameVariablesWithReservedCharacters() {
    char[] reservedCharacters = {'$'};
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "", false, false, false, null, reservedCharacters, null);
    Node node = createNode("var $a = 1");;
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("$a$", variableMap.lookupName("$a"));
  }

  @org.junit.Test
  public void testRenameVariablesWithExterns() {
    Set<String> externNames = new HashSet<>();
    externNames.add("console");
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "", false, false, false, null, null, externNames);
    Node node = createNode("console.log(1)");
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("console$", variableMap.lookupName("console"));
  }

  @org.junit.Test
  public void testRenameVariablesWithLocalRenamingOnly() {
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "", true, false, false, null, null, null);
    Node node = createNode("function foo() { var a = 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("a", variableMap.lookupName("a"));
  }

  @org.junit.Test
  public void testRenameVariablesWithPreserveAnonymousFunctionNames() {
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "", false, true, false, null, null, null);
    Node node = createNode("function foo() { function bar() {} }");
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("bar", variableMap.lookupName("function bar() {}"));
  }

  @org.junit.Test
  public void testRenameVariablesWithGeneratePseudoNames() {
    RenameVars renameVars = new RenameVars(mock(AbstractCompiler.class), "", false, false, true, null, null, null);
    Node node = createNode("function foo() { var a = 1; }");
    NodeTraversal nodeTraversal = new NodeTraversal(mock(AbstractCompiler.class), node, new BasicErrorReporter());
    renameVars.process(node, node);
    VariableMap variableMap = renameVars.getVariableMap();
    assertEquals("$a$$", variableMap.lookupName("a"));
  }

  private Node createNode(String code) {
    JsParser parser = new JsParser(new BasicErrorManager(), null);
    return parser.parse(code, null, null).getTree();
  }
}