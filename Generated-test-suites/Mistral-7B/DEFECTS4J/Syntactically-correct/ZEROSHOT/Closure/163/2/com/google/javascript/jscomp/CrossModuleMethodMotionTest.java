import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CrossModuleMethodMotion.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.util.Collections;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private JSModuleGraph moduleGraph;
  private IdGenerator idGenerator;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    compiler = PowerMockito.mock(AbstractCompiler.class);
    moduleGraph = new JSModuleGraph();
    idGenerator = new IdGenerator();
    passManager = new CompilerPassManager(compiler, moduleGraph, false, false);
  }

  @Test
  public void testMoveMethods() {
    SourceFile rootNode = new SourceFile("test.js", "test");
    Node externs = createNode("externs.js", "var Foo = function() {}");
    CompilerOptions options = new CompilerOptions();

    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(
        compiler, idGenerator, true);

    passManager.addPass(crossModuleMethodMotion);
    passManager.process(rootNode, externs, options);

    List<NameInfo> allNameInfo = Lists.newArrayList();
    // Add some NameInfos with no deepestCommonModuleRef for testing
    allNameInfo.add(createNameInfo("Foo", null, rootNode));
    allNameInfo.add(createNameInfo("Foo.prototype.bar", null, rootNode));

    crossModuleMethodMotion.moveMethods(allNameInfo);

    // No methods moved since allNameInfo contains only NameInfos with no deepestCommonModuleRef
    assertEquals(0, idGenerator.getCurrentId());
  }

  @Test
  public void testMoveMethodsAcrossModules() {
    SourceFile rootNode1 = new SourceFile("module1.js", "var Foo = function() {}");
    SourceFile rootNode2 = new SourceFile("module2.js", "var A = function() { Foo.prototype.b = function() {} }");

    Node externs = createNode("externs.js", "var Foo: function() {}");
    CompilerOptions options = new CompilerOptions();

    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(
        compiler, idGenerator, true);

    passManager.addPass(crossModuleMethodMotion);
    passManager.process(rootNode1, externs, options);
    passManager.process(rootNode2, null, options);

    List<NameInfo> allNameInfo = Lists.newArrayList();
    allNameInfo.add(createNameInfo("Foo", null, rootNode1));
    allNameInfo.add(createNameInfo("Foo.prototype.b", rootNode2, rootNode2));

    crossModuleMethodMotion.moveMethods(allNameInfo);

    // One method moved across modules
    assertEquals(1, idGenerator.getCurrentId());

    Node expectedStubCall = createNode("stubCall.js",
        "function() {" +
        "  return JSCompiler_stubMap[0].apply(this, arguments);" +
        "}");
    Node expectedUnstubCall = createNode("unstubCall.js",
        "JSCompiler_unstubMethod(0, function() {})");

    // Check stub function in top-most module
    Node stubFunction = compiler.getNodeForCodeInsertion(null).getChild(0);
    assertEquals(expectedStubCall.getCode(), stubFunction.getCode());

    // Check unstubbed method in deeper module
    JSModule deeperModule = moduleGraph.getModuleByName("module2");
    Node unstubFunction = deeperModule.getBody().getChild(0).getChild(0);
    assertEquals(expectedUnstubCall.getCode(), unstubFunction.getCode());
  }

  private NameInfo createNameInfo(String name, JSModule deepestCommonModuleRef, Node node) {
    Property property = createProperty("function() {}", "prototype", null, null, node, null, name, Collections.emptyList());
    return new NameInfo(name, null, property, deepestCommonModuleRef);
  }

  private Property createProperty(String value, String type, Node parent, Node valueParent, Node node, Node proto, String name,
      Collection<Symbol> declarations) {
    Property property = new Property();
    property.setValue(createNode(value));
    property.setType(type);
    property.setParent(parent);
    property.setValueParent(valueParent);
    property.setName(name);
    property.setDeclarations(declarations);
    return property;
  }

  private Node createNode(String code) {
    return new Node(Node.ROOT, new SourceFile("test.js", code));
  }

  private Node createNode(String fileName, String code) {
    return new Node(code, new SourceFile(fileName, code));
  }
}