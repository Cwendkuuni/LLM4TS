import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private CompilerPassManager passManager;
  private AbstractCompiler abstractCompiler;
  private IdGenerator idGenerator;
  private JSModuleGraph moduleGraph;
  private AnalyzePrototypeProperties analyzer;

  @Before
  public void setup() {
    compiler = mock(Compiler.class);
    passManager = mock(CompilerPassManager.class);
    abstractCompiler = mock(AbstractCompiler.class);
    when(compiler.getContext()).thenReturn(abstractCompiler);
    moduleGraph = mock(JSModuleGraph.class);
    idGenerator = mock(IdGenerator.class);
    when(abstractCompiler.getCompilerPassManager()).thenReturn(passManager);
    when(abstractCompiler.getModuleGraph()).thenReturn(moduleGraph);
    when(abstractCompiler.getIdGenerator()).thenReturn(idGenerator);
    analyzer = new AnalyzePrototypeProperties(
        abstractCompiler, moduleGraph, true, false);
  }

  @Test
  public void testMoveMethods() {
    // Given a CrossModuleMethodMotion instance
    CrossModuleMethodMotion crossModuleMethodMotion =
        new CrossModuleMethodMotion(abstractCompiler, idGenerator, true);

    // When processing the root node
    crossModuleMethodMotion.process(Node.NULL, Node.NULL);

    // Then the moveMethods method is called
    ArgumentCaptor<Collection<NameInfo>> allNameInfoArgumentCaptor =
        ArgumentCaptor.forClass(Collection.class);
    verify(analyzer).process(Node.NULL, Node.NULL);
    verify(crossModuleMethodMotion).moveMethods(allNameInfoArgumentCaptor.capture());

    // And the STUB_DECLARATIONS code is inserted at the root level if any id is generated
    verify(moduleGraph).dependsOn(eq(moduleGraph), eq(Node.NULL));
    verify(idGenerator).hasGeneratedAnyIds();
    Node declarations = Node.NULL;
    if (idGenerator.hasGeneratedAnyIds()) {
      declarations = Node.parse(STUB_DECLARATIONS);
    }
    verify(compiler).getNodeForCodeInsertion(eq(null)).addChildrenToFront(
        eq(declarations.removeChildren()));
  }
}

class STUB_DECLARATIONS {
  static final String STUB_DECLARATIONS =
      "var JSCompiler_stubMap = [];" +
      "function JSCompiler_stubMethod(JSCompiler_stubMethod_id) {" +
      "  return function() {" +
      "    return JSCompiler_stubMap[JSCompiler_stubMethod_id].apply(" +
      "        this, arguments);" +
      "  };" +
      "}" +
      "function JSCompiler_unstubMethod(" +
      "    JSCompiler_unstubMethod_id, JSCompiler_unstubMethod_body) {" +
      "  return JSCompiler_stubMap[JSCompiler_unstubMethod_id] = " +
      "      JSCompiler_unstubMethod_body;" +
      "}";
}