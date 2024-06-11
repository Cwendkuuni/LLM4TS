import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class VarCheckTest extends CompilerPassTestCase {

  private AbstractCompiler compiler;
  private CompilerPass varCheck;

  @Before
  public void setUp() throws Exception {
    compiler = getCompiler();
    compiler.setSourceFile("Test.js");
    compiler.setOption(CompilerOptions.Warning.UNDEFINED_EXTERN, CheckLevel.ERROR);
    compiler.setOption(CompilerOptions.Warning.UNDEFINED_EXTERN_VAR, CheckLevel.ERROR);
    compiler.setOption(CompilerOptions.Warning.NAME_REFERENCE_IN_EXTERNS, CheckLevel.ERROR);

    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testGlobalVar() throws Exception {
    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createVar("x", null, null, null, false, null)
        )
    );
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    assertEquals(0, compiler.getNumDiagnostics());
  }

  @Test
  public void testUndefinedGlobalVar() throws Exception {
    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createExpressionStatement(
                NodeFactory.createAssign(
                    NodeFactory.createName("x"),
                    NodeFactory.createNull()
                )
            )
        )
    );
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    DiagnosticType diagnostic = compiler.getCurrentDiagnostic();
    assertEquals(DiagnosticType.error, diagnostic.getSeverity());
    assertEquals("JSC_UNDEFINED_VARIABLE", diagnostic.getType().getName());
    assertEquals("x is undefined", diagnostic.getMessage(null));
  }

  @Test
  public void testFunctionLocalVar() throws Exception {
    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createFunctionDeclaration("f", new Node[0], NodeFactory.createBlock(
                NodeFactory.createVar("x", null, null, NodeFactory.createName("x"), false, null),
                NodeFactory.createStatement(NodeFactory.createReturn(NodeFactory.createNumberLiteral(1)))
            ))
        )
    );
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    assertEquals(0, compiler.getNumDiagnostics());
  }

  @Test
  public void testUndefinedFunctionLocalVar() throws Exception {
    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createFunctionDeclaration("f", new Node[0], NodeFactory.createBlock(
                NodeFactory.createStatement(NodeFactory.createAssign(
                    NodeFactory.createName("x"), NodeFactory.createNull()
                )),
                NodeFactory.createStatement(NodeFactory.createReturn(NodeFactory.createNumberLiteral(1)))
            ))
        )
    );
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    DiagnosticType diagnostic = compiler.getCurrentDiagnostic();
    assertEquals(DiagnosticType.error, diagnostic.getSeverity());
    assertEquals("JSC_UNDEFINED_VARIABLE", diagnostic.getType().getName());
    assertEquals("x is undefined", diagnostic.getMessage(null));
  }

  @Test
  public void testFunctionParameter() throws Exception {
    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createFunctionDeclaration("f", new Node[]{NodeFactory.createParameter("x", null, null)}, NodeFactory.createBlock(
                NodeFactory.createStatement(NodeFactory.createReturn(NodeFactory.createName("x")))
            ))
        )
    );
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    assertEquals(0, compiler.getNumDiagnostics());
  }

  @Test
  public void testUndefinedFunctionParameter() throws Exception {
    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createFunctionDeclaration("f", new Node[]{NodeFactory.createParameter("x", null, null)}, NodeFactory.createBlock(
                NodeFactory.createStatement(NodeFactory.createAssign(
                    NodeFactory.createName("x"), NodeFactory.createNull()
                )),
                NodeFactory.createStatement(NodeFactory.createReturn(NodeFactory.createName("x")))
            ))
        )
    );
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    DiagnosticType diagnostic = compiler.getCurrentDiagnostic();
    assertEquals(DiagnosticType.error, diagnostic.getSeverity());
    assertEquals("JSC_UNDEFINED_VARIABLE", diagnostic.getType().getName());
    assertEquals("x is undefined", diagnostic.getMessage(null));
  }

  @Test
  public void testUndefinedExternVar() throws Exception {
    String code =
        "var x = 1;\n" +
        "exports.foo = function() {\n" +
        "  var y = x;\n" +
        "};";
    Node root = NodeFactory.createProgram(NodeFactory.createScript(NodeFactory.createString(code)));
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    DiagnosticType diagnostic = compiler.getCurrentDiagnostic();
    assertEquals(DiagnosticType.error, diagnostic.getSeverity());
    assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", diagnostic.getType().getName());
    assertEquals("x is not undefined in the externs.", diagnostic.getMessage(null));
  }

  @Test
  public void testDuplicateVarDeclaration() throws Exception {
    Node root = NodeFactory.createProgram(
        NodeFactory.createBlock(
            NodeFactory.createVar("x", null, null, null, false, null),
            NodeFactory.createVar("x", null, null, null, false, null)
        )
    );
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    DiagnosticType diagnostic = compiler.getCurrentDiagnostic();
    assertEquals(DiagnosticType.warning, diagnostic.getSeverity());
    assertEquals("duplicate", diagnostic.getMessage(null));
  }

  @Test
  public void testModuleDependency() throws Exception {
    String depModuleCode = "var x = 1;";
    String mainModuleCode = "require('dep.js').x;";

    Node depRoot = NodeFactory.createProgram(NodeFactory.createScript(NodeFactory.createString(depModuleCode)));
    Node mainRoot = NodeFactory.createProgram(NodeFactory.createScript(NodeFactory.createString(mainModuleCode)));

    JSModule depModule = new MockJSModule("dep.js");
    depModule.setAstRoot(compiler.process(depRoot, null).getAstRoot(compiler));

    JSModule mainModule = new MockJSModule("main.js");
    mainModule.setAstRoot(compiler.process(mainRoot, depModule).getAstRoot(compiler));

    NodeTraversal traversal = NodeTraversal.create(compiler, mainRoot);
    traversal.setInput(mainModule);
    traversal.traverse(varCheck);
    DiagnosticType diagnostic = compiler.getCurrentDiagnostic();
    assertEquals(DiagnosticType.error, diagnostic.getSeverity());
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", diagnostic.getType().getName());
    assertEquals("main.js cannot reference dep.js, defined in dep.js, since dep.js loads after main.js", diagnostic.getMessage(null));
  }

  @Test
  public void testMissingModuleDependency() throws Exception {
    String mainModuleCode = "require('dep.js').x;";

    Node root = NodeFactory.createProgram(NodeFactory.createScript(NodeFactory.createString(mainModuleCode)));
    NodeTraversal traversal = NodeTraversal.create(compiler, root);
    traversal.traverse(varCheck);
    DiagnosticType diagnostic = compiler.getCurrentDiagnostic();
    assertEquals(DiagnosticType.warning, diagnostic.getSeverity());
    assertEquals("missing module dependency; main.js should depend on dep.js because it references x", diagnostic.getType().getName());
    assertEquals("dep.js", diagnostic.getMessage(null).split(" ")[1]);
  }

  @Test
  public void testStrictModuleDependency() throws Exception {
    String depModuleCode = "var x = 1;";
    String mainModuleCode = "require('dep.js').x;";

    Node depRoot = NodeFactory.createProgram(NodeFactory.createScript(NodeFactory.createString(depModuleCode)));
    Node mainRoot = NodeFactory.createProgram(NodeFactory.createScript(NodeFactory.createString(mainModuleCode)));

    JSModule depModule = new MockJSModule("dep.js", true);
    depModule.setAstRoot(compiler.process(depRoot, null).getAstRoot(compiler));
    CompilerOptions options = compiler.getOptions();
    options.setError(JSError.make("", 0, 0, UNDEFINED_EXTERN_VAR_ERROR), CheckLevel.ERROR);

    JSModule mainModule = new MockJSModule("main.js");
    mainModule.setAstRoot(compiler.process(mainRoot, depModule, options).getAstRoot(compiler));

    NodeTraversal traversal = NodeTraversal.create(compiler, mainRoot);
    traversal.setInput(mainModule);
    traversal.traverse(varCheck);
    DiagnosticType diagnostic = compiler.getCurrentDiagnostic();
    assertEquals(DiagnosticType.disabled, diagnostic.getSeverity());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", diagnostic.getType().getName());
    assertEquals("main.js cannot reference dep.js, defined in dep.js", diagnostic.getMessage(null));
  }
}

class MockJSModule implements JSModule {
  private String name;
  private Node astRoot;
  private boolean isStrict = false;

  MockJSModule(String name) {
    this.name = name;
  }

  MockJSModule(String name, boolean isStrict) {
    this.name = name;
    this.isStrict = isStrict;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Node getAstRoot(AbstractCompiler compiler) {
    return astRoot;
  }

  @Override
  public boolean isStrict() {
    return isStrict;
  }

  public void setAstRoot(Node astRoot) {
    this.astRoot = astRoot;
  }
}