import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeUtil;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    CompilerPass compilerPass = mock(CompilerPass.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getModuleGraph()).thenReturn(mock(JSModuleGraph.class));

    SimpleDefinitionFinder simpleDefinitionFinder = new SimpleDefinitionFinder(compiler);

    CompilerPassContext compilerPassContext = new CompilerPassContext() {
      @Override
      public AbstractCompiler getCompiler() {
        return compiler;
      }
    };

    Node root = Node.newText("(function() {\n" +
        "  function A() {\n" +
        "    this.total = 0;\n" +
        "  }\n" +
        "  A.prototype.accumulate = function(value) {\n" +
        "    this.total += value;\n" +
        "    return this.total;\n" +
        "  };\n" +
        "  var a = new A();\n" +
        "  var total = a.accumulate(2);\n" +
        "})()");

    compilerPass.process(null, root, compilerPassContext);

    List<Node> expectedNodes = ImmutableList.of(
        Node.newText("(function() {\n" +
            "  var JSCompiler_StaticMethods_accumulate = function(self, value) {\n" +
            "    self.total += value;\n" +
            "    return self.total;\n" +
            "  };\n" +
            "  var A = function() {\n" +
            "    this.total = 0;\n" +
            "  };\n" +
            "  A.prototype.accumulate = JSCompiler_StaticMethods_accumulate;\n" +
            "  var a = new A();\n" +
            "  var total = JSCompiler_StaticMethods_accumulate(a, 2);\n" +
            "})()"),
        Node.newText("(function() {\n" +
            "  var JSCompiler_StaticMethods_accumulate = function(self, value) {\n" +
            "    self.total += value;\n" +
            "    return self.total;\n" +
            "  };\n" +
            "  A.prototype.accumulate = JSCompiler_StaticMethods_accumulate;\n" +
            "})().call(this)")
    );

    List<Node> actualNodes = Lists.newArrayList(root.getChildren());

    for (int i = 0; i < expectedNodes.size(); i++) {
      Node expectedNode = expectedNodes.get(i);
      Node actualNode = actualNodes.get(i);
      assertNotNull(expectedNode);
      assertNotNull(actualNode);
      NodeUtil.assertNodeEquals(expectedNode, actualNode);
    }
  }
}