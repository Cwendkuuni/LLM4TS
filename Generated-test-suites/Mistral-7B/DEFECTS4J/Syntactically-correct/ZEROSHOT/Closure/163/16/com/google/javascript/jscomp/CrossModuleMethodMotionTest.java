import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.IDeclaration;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Logger;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.rhino.IR;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class CrossModuleMethodMotionTest {

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    JSModuleGraph moduleGraph = mock(JSModuleGraph.class);
    Logger loggerMock = mock(Logger.class);
    when(loggerMock.getLogger(CrossModuleMethodMotion.class.getName())).thenReturn(loggerMock);
    when(loggerMock.isLoggable(Level.SEVERE)).thenReturn(true);
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(compiler.getProcessor()).thenReturn(mock(Processor.class));
    when(compiler.getLogger()).thenReturn(loggerMock);

    CompilerPass pass = crossModuleMethodMotion;
    Node root = Node.nullLiteral();
    Node externRoot = Node.nullLiteral();
    pass.process(externRoot, root);
  }

  @org.junit.Test
  public void testNullCommonModuleRef() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    JSModuleGraph moduleGraph = mock(JSModuleGraph.class);
    Logger loggerMock = mock(Logger.class);
    when(loggerMock.getLogger(CrossModuleMethodMotion.class.getName())).thenReturn(loggerMock);
    when(loggerMock.isLoggable(Level.SEVERE)).thenReturn(true);
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(compiler.getProcessor()).thenReturn(mock(Processor.class));
    when(compiler.getLogger()).thenReturn(loggerMock);

    // If there are < 2 modules, then we will never move anything,
    // so we're done.
    when(moduleGraph.getModuleCount()).thenReturn(1);
    CompilerPass pass = crossModuleMethodMotion;
    Node externRoot = Node.nullLiteral();
    Node root = Node.nullLiteral();
    pass.process(externRoot, root);

    // Test the error reporting
    assertSame(DiagnosticType.error(DiagnosticType.JSC_INTERNAL_ERROR_MODULE_DEPEND, "null deepest common module"),
        compiler.getLastDiagnostic());
  }

  @org.junit.Test
  public void testMoveMethods() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    JSModuleGraph moduleGraph = mock(JSModuleGraph.class);
    Logger loggerMock = mock(Logger.class);
    when(loggerMock.getLogger(CrossModuleMethodMotion.class.getName())).thenReturn(loggerMock);
    when(loggerMock.isLoggable(Level.SEVERE)).thenReturn(true);
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(compiler.getProcessor()).thenReturn(mock(Processor.class));
    when(compiler.getLogger()).thenReturn(loggerMock);

    // Create modules
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    JSModule moduleC = mock(JSModule.class);
    when(moduleGraph.getModule(0)).thenReturn(moduleA);
    when(moduleGraph.getModule(1)).thenReturn(moduleB);
    when(moduleGraph.getModule(2)).thenReturn(moduleC);

    // Create name infos
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.getName()).thenReturn("test");
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.readsClosureVariables()).thenReturn(false);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(moduleB);
    when(nameInfo.getDeclarations()).thenReturn(Collections.singletonList(mock(IDeclaration.class)));
    List<NameInfo> allNameInfo = new ArrayList<>();
    allNameInfo.add(nameInfo);

    // Create declarations
    IDeclaration declaration = mock(IDeclaration.class);
    when(declaration.getValue()).thenReturn(Node.nullLiteral());
    when(declaration.getModule()).thenReturn(moduleC);
    when(declaration.getName()).thenReturn("test");
    when(nameInfo.getDeclarations()).thenReturn(Collections.singletonList(declaration));

    // Create property
    Node property = mock(Node.class);
    when(property.isFunction()).thenReturn(true);
    when(property.getParent()).thenReturn(Node.nullLiteral());
    when(declaration.getValue()).thenReturn(property);

    // Process
    when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    Node externRoot = Node.nullLiteral();
    Node root = Node.nullLiteral();
    pass.process(externRoot, root);

    // Verify method was moved
    Node movedMethod = pass.getMovedMethod(nameInfo);
    assertSame(property, movedMethod);

    // Verify stub was generated
    Node stubCall = pass.getStubCall(nameInfo);
    assertSame(IR.call(IR.name(STUB_METHOD_NAME), IR.number(0)).copyInformationFromForTree(property), stubCall);

    // Verify unstub was generated
    Node unstubCall = pass.getUnstubCall(nameInfo);
    Node body = IR.exprResult(property).getFirstChild();
    assertSame(IR.call(IR.name(UNSTUB_METHOD_NAME), IR.number(0), body), unstubCall);
  }

  @org.junit.Test
  public void testNoMove() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    JSModuleGraph moduleGraph = mock(JSModuleGraph.class);
    Logger loggerMock = mock(Logger.class);
    when(loggerMock.getLogger(CrossModuleMethodMotion.class.getName())).thenReturn(loggerMock);
    when(loggerMock.isLoggable(Level.SEVERE)).thenReturn(true);
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(compiler.getProcessor()).thenReturn(mock(Processor.class));
    when(compiler.getLogger()).thenReturn(loggerMock);

    // Create modules
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    when(moduleGraph.getModule(0)).thenReturn(moduleA);
    when(moduleGraph.getModule(1)).thenReturn(moduleB);

    // Create name info
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.getName()).thenReturn("test");
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.readsClosureVariables()).thenReturn(false);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(moduleA);
    when(nameInfo.getDeclarations()).thenReturn(Collections.singletonList(mock(IDeclaration.class)));

    // Process
    when(analyzer.getAllNameInfo()).thenReturn(Collections.singletonList(nameInfo));
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    Node externRoot = Node.nullLiteral();
    Node root = Node.nullLiteral();
    pass.process(externRoot, root);

    // Verify no method was moved
    assertNull(pass.getMovedMethod(nameInfo));
  }

  @org.junit.Test
  public void testGenerateStubs() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    JSModuleGraph moduleGraph = mock(JSModuleGraph.class);
    Logger loggerMock = mock(Logger.class);
    when(loggerMock.getLogger(CrossModuleMethodMotion.class.getName())).thenReturn(loggerMock);
    when(loggerMock.isLoggable(Level.SEVERE)).thenReturn(true);
    AnalyzePrototypeProperties analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    when(compiler.getProcessor()).thenReturn(mock(Processor.class));
    when(compiler.getLogger()).thenReturn(loggerMock);

    // Process
    when(analyzer.getAllNameInfo()).thenReturn(Collections.emptyList());
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    Node externRoot = Node.nullLiteral();
    Node root = Node.nullLiteral();
    pass.process(externRoot, root);

    // Verify no stubs were generated
    assertNull(pass.getIdGenerator().hasGeneratedAnyIds());

    // Generate stubs
    when(analyzer.getAllNameInfo()).thenReturn(Collections.singletonList(mock(NameInfo.class)));
    pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    externRoot = Node.nullLiteral();
    root = Node.nullLiteral();
    pass.process(externRoot, root);

    // Verify stubs were generated
    assertTrue(pass.getIdGenerator().hasGeneratedAnyIds());
  }
}