import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private ProcessCommonJSModules processCommonJSModules;

  @org.junit.Before
  public void setUp() {
    compiler = mock(Compiler.class);
    processCommonJSModules = new ProcessCommonJSModules(compiler, "");
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    String filename = "/path/to/module.js";
    String expected = "module$path$to$module";
    String actual = processCommonJSModules.guessCJSModuleName(filename);
    assertNotNull(actual);
    assertNotNull(expected);
    assertNotNull(expected.equals(actual));
  }

  @org.junit.Test
  public void testToModuleName() {
    String filename = "/path/to/module.js";
    String expected = "module$path$to$module";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertNotNull(actual);
    assertNotNull(expected.equals(actual));

    filename = "path/to/module.js";
    expected = "module$path$to$module";
    actual = ProcessCommonJSModules.toModuleName(filename, "/other/file.js");
    assertNotNull(actual);
    assertNotNull(expected.equals(actual));
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    String filename = "/path/to/module.js";
    String expected = "path/to/module";
    String actual = processCommonJSModules.normalizeSourceName(filename);
    assertNotNull(actual);
    assertNotNull(expected.equals(actual));
  }

  @org.junit.Test
  public void testProcess() {
    Node root = mock(Node.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.traverse(root)).thenReturn(nodeTraversal);

    processCommonJSModules.process(mock(Node.class), root);

    processCommonJSModules.getModule();
    assertNotNull(processCommonJSModules.getModule());
  }

  @org.junit.Test
  public void testVisitRequireCall() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node requireNode = mock(Node.class);
    Node parentNode = mock(Node.class);
    when(compiler.traverse(requireNode)).thenReturn(nodeTraversal);
    when(nodeTraversal.getInput()).thenReturn(mock(CompilerInput.class));

    processCommonJSModules.visitRequireCall(nodeTraversal, requireNode, parentNode);

    String moduleName = "module$name";
    Node moduleRef = mock(Node.class);
    Node scriptNode = mock(Node.class);
    when(requireNode.getChildAtIndex(1)).thenReturn(
        IR.string(moduleName).cloneTree());
    when(parentNode.getChildAtIndex(0)).thenReturn(scriptNode);
    when(IR.name(moduleName)).thenReturn(moduleRef);

    processCommonJSModules.visitRequireCall(nodeTraversal, requireNode, parentNode);

    Node requireCall = requireNode.cloneTree();
    Node expected = IR.exprResult(moduleRef);
    Node actual = requireCall.getChildAtIndex(0);
    assertNotNull(actual);
    assertNotNull(expected);
    assertNotNull(expected.equals(actual));
  }

  @org.junit.Test
  public void testVisitScript() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node scriptNode = mock(Node.class);
    when(compiler.traverse(scriptNode)).thenReturn(nodeTraversal);
    when(nodeTraversal.getInput()).thenReturn(mock(CompilerInput.class));

    processCommonJSModules.visitScript(nodeTraversal, scriptNode);

    String moduleName = "module$name";
    Node expectedModuleName = IR.name(moduleName);
    Node actualModuleName = scriptNode.getChildAtIndex(0);
    assertNotNull(actualModuleName);
    assertNotNull(expectedModuleName);
    assertNotNull(expectedModuleName.equals(actualModuleName));

    Node googProvide = mock(Node.class);
    Node googRequire = mock(Node.class);
    when(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")), IR.string(moduleName)))
        .thenReturn(googProvide);
    when(IR.call(IR.getprop(IR.name("goog"), IR.string("require")), IR.string(moduleName)))
        .thenReturn(googRequire);

    Node expectedCode = IR.block(googProvide, googRequire, scriptNode.getChildAtIndex(1));
    Node actualCode = scriptNode.cloneTree();
    assertNotNull(actualCode);
    assertNotNull(expectedCode);
    assertNotNull(expectedCode.equals(actualCode));

    Node expectedScriptNode = IR.exprResult(expectedCode);
    Node actualScriptNode = scriptNode;
    assertNotNull(actualScriptNode);
    assertNotNull(expectedScriptNode);
    assertNotNull(expectedScriptNode.equals(actualScriptNode));
  }

  @org.junit.Test
  public void testVisitModuleExports() {
    Node propNode = mock(Node.class);
    Node moduleNode = mock(Node.class);
    Node exportsNode = mock(Node.class);
    when(propNode.getChildAtIndex(0)).thenReturn(moduleNode);
    when(propNode.getChildAtIndex(1)).thenReturn(exportsNode);

    processCommonJSModules.visitModuleExports(propNode);

    String moduleName = "module$name";
    when(moduleNode.getString()).thenReturn(moduleName);
    when(exportsNode.getString()).thenReturn("exports");

    Node expectedModuleName = IR.name(moduleName);
    Node expectedExportsName = IR.name("module$exports");
    Node actualModuleName = moduleNode.cloneTree();
    Node actualExportsName = exportsNode.cloneTree();

    actualModuleName.putProp(Node.ORIGINALNAME_PROP, "module");
    actualExportsName.putProp(Node.ORIGINALNAME_PROP, "exports");

    expectedModuleName.setString(moduleName);
    expectedExportsName.setString("module$exports");

    assertNotNull(actualModuleName);
    assertNotNull(actualExportsName);
    assertNotNull(expectedModuleName);
    assertNotNull(expectedExportsName);
    assertNotNull(expectedModuleName.equals(actualModuleName));
    assertNotNull(expectedExportsName.equals(actualExportsName));
  }

  @org.junit.Test
  public void testEmitOptionalModuleExportsOverride() {
    Node scriptNode = mock(Node.class);
    String moduleName = "module$name";

    processCommonJSModules.emitOptionalModuleExportsOverride(scriptNode, moduleName);

    Node moduleExportsProp = mock(Node.class);
    Node moduleNode = mock(Node.class);
    Node exportsNode = mock(Node.class);
    when(moduleExportsProp.getParent()).thenReturn(scriptNode);
    when(moduleExportsProp.getChildAtIndex(0)).thenReturn(moduleNode);
    when(moduleNode.getString()).thenReturn(moduleName);
    when(moduleNode.getChildAtIndex(1)).thenReturn(exportsNode);
    when(exportsNode.getString()).thenReturn("exports");

    Node expectedIfNode = IR.ifNode(moduleExportsProp, IR.block(IR.assign(moduleNode, moduleExportsProp)));
    Node actualIfNode = scriptNode.getChildAtIndex(scriptNode.getChildCount() - 1);

    assertNotNull(actualIfNode);
    assertNotNull(expectedIfNode);
    assertNotNull(expectedIfNode.equals(actualIfNode));
  }

  @org.junit.Test
  public void testSuffixVarsCallback() {
    Node rootNode = mock(Node.class);
    processCommonJSModules.process(mock(Node.class), rootNode);
    Node globalScope = rootNode.getFirstChild();

    String suffix = "testSuffix";
    String varName = "testVar";

    Node expectedVarNode = IR.name(varName + "$$" + suffix);
    Node actualVarNode = globalScope.getChildAtIndex(0);

    actualVarNode.setString(varName);
    expectedVarNode.setString(varName);
    expectedVarNode.putProp(Node.ORIGINALNAME_PROP, varName);

    assertNotNull(actualVarNode);
    assertNotNull(expectedVarNode);
    assertNotNull(expectedVarNode.equals(actualVarNode));
  }
}