import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Symbol;
import com.google.javascript.jscomp.analyzer.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.analyzer.NameInfo;
import com.google.javascript.jscomp.analyzer.Property;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private IdGenerator idGenerator;
  private NodeFactory nodeFactory;
  private AnalyzePrototypeProperties analyzer;
  private JSModule moduleA;
  private JSModule moduleB;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    idGenerator = mock(IdGenerator.class);
    nodeFactory = new NodeFactory();
    analyzer = new AnalyzePrototypeProperties(compiler, null, false, false);
    passManager = new CompilerPassManager(compiler);

    moduleA = new MockJSModule("moduleA");
    moduleB = new MockJSModule("moduleB");
  }

  @Test
  public void testMoveMethods() {
    // Given
    when(compiler.getModuleGraph()).thenReturn(new MockModuleGraph());
    when(compiler.getNodeForCodeInsertion(null)).thenReturn(nodeFactory.rootNode());
    when(compiler.parseSyntheticCode(anyString())).thenAnswer(invocation -> invocation.getArgumentAt(0));

    Symbol symbolA = new Property(nodeFactory.newName("propA"), nodeFactory.newFunction(null), null);
    Node prototypeA = nodeFactory.newObjectLiteral();
    prototypeA.addChild(prototypeA.setChildIndex(prototypeA.addChild(nodeFactory.newProperty("propA", symbolA)), 0));
    when(analyzer.getAllNameInfo()).thenReturn(getAllNameInfo());

    passManager.registerPass(new CrossModuleMethodMotion(compiler, idGenerator, false));

    // When
    passManager.process(nodeFactory.newProgram(), null);

    // Then
    Node nodeA = moduleA.getRootNode().getChild(0);
    Node stubNodeA = nodeFactory.getChildAtIndex(nodeA, 1);
    assertEquals("JSCompiler_stubMethod(id)", stubNodeA.toString());

    Node unstubNodeB = moduleB.getRootNode().getChild(0);
    Node bodyNodeB = nodeFactory.getChildAtIndex(unstubNodeB, 1);
    assertEquals("function(){\n  return JSCompiler_unstubMethod_body;\n}", bodyNodeB.toString());
  }

  private Collection<NameInfo> getAllNameInfo() {
    List<NameInfo> nameInfos = new ArrayList<>();
    NameInfo nameInfo = new NameInfo("moduleA", null, Arrays.asList(symbolA), moduleA);
    nameInfos.add(nameInfo);
    return nameInfos;
  }

  private static class MockJSModule implements JSModule {
    private final String name;

    public MockJSModule(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Node getRootNode() {
      return nodeFactory.newProgram();
    }
  }

  private static class MockModuleGraph implements JSModuleGraph {
    @Override
    public int getModuleCount() {
      return 2;
    }

    @Override
    public JSModule getModuleByName(String name) {
      if (name.equals("moduleA")) {
        return moduleA;
      }
      return moduleB;
    }

    @Override
    public boolean dependsOn(JSModule from, JSModule to) {
      return true;
    }
  }
}