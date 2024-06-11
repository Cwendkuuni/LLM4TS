import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.ModuleLoader;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Nodeops;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private IdGenerator idGenerator;
  private CompilerPassManager passManager;
  private SourceFile rootSourceFile;
  private Node rootNode;
  private JSModule rootModule;
  private CompilerPass crossModuleMethodMotion;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    compiler = mock(AbstractCompiler.class);
    idGenerator = mock(IdGenerator.class);
    passManager = mock(CompilerPassManager.class);
    when(compiler.getModuleGraph()).thenReturn(new ModuleLoader());
    rootSourceFile = mock(SourceFile.class);
    rootNode = Nodeops.createRoot();
    rootModule = mock(JSModule.class);
    when(compiler.getNodeForCodeInsertion(ArgumentMatchers.any())).then(
        invocation -> rootModule);

    when(compiler.parseSyntheticCode(ArgumentMatchers.anyString()))
        .then(invocation -> Nodeops.createRoot());

    when(compiler.getPassManager()).thenReturn(passManager);

    crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator,
        true);
  }

  @Test
  public void testMoveMethodsWhenNoModules() {
    crossModuleMethodMotion.process(rootNode, rootNode);
  }

  @Test
  public void testMoveMethodsWhenOneModule() {
    when(rootModule.getName()).thenReturn("module1");
    when(moduleLoader.getModule(ArgumentMatchers.anyString()))
        .thenReturn(rootModule);

    crossModuleMethodMotion.process(rootNode, rootNode);
  }

  @Test
  public void testMoveMethodsWhenTwoModules() {
    when(rootModule.getName()).thenReturn("module1");
    JSModule module2 = mock(JSModule.class);
    when(moduleLoader.getModule(ArgumentMatchers.eq("module2")))
        .thenReturn(module2);

    crossModuleMethodMotion.process(rootNode, rootNode);
  }

  @Test
  public void testMoveMethodWhenNotFunction() {
    when(rootModule.getName()).thenReturn("module1");
    when(rootModule.getDeclaredVariables()).thenReturn(Nodeops.createList());

    Node methodNode = Nodeops.createMethod("foo", null);
    Node propertyNode = Nodeops.createProperty(Nodeops.createThis(), methodNode);

    rootNode.addChildToFront(propertyNode);

    crossModuleMethodMotion.process(rootNode, rootNode);

    assertFalse(methodNode.isFunction());
  }

  @Test
  public void testMoveMethodWhenNotPrototype() {
    when(rootModule.getName()).thenReturn("module1");
    JSModule module2 = mock(JSModule.class);
    when(moduleLoader.getModule(ArgumentMatchers.eq("module2")))
        .thenReturn(module2);
    Node prototypeNode = Nodeops.createProperty(Nodeops.createObject(),
        Nodeops.createString("prototype"));

    Node methodNode = Nodeops.createMethod("foo", null);
    Node methodPropertyNode = Nodeops.createProperty(PrototypeNode, methodNode);
    Node prototypeMethodNode = Nodeops.createProperty(prototypeNode,
        methodPropertyNode);

    rootNode.addChildToFront(prototypeNode);

    crossModuleMethodMotion.process(rootNode, rootNode);

    assertNull(methodPropertyNode);
  }

  @Test
  public void testMoveMethodWhenAlreadyInDeepestModule() {
    when(rootModule.getName()).thenReturn("module1");
    JSModule module2 = mock(JSModule.class);
    when(moduleLoader.getModule(ArgumentMatchers.eq("module2")))
        .thenReturn(module2);
    Node prototypeNode = Nodeops.createProperty(Nodeops.createObject(),
        Nodeops.createString("prototype"));
    Node methodNode = Nodeops.createMethod("foo", null);
    Node methodPropertyNode = Nodeops.createProperty(prototypeNode,
        methodNode);

    rootNode.addChildToFront(prototypeNode);

    crossModuleMethodMotion.process(rootNode, rootNode);

    assertNull(methodPropertyNode);
  }

  @Test
  public void testMoveMethodWhenNotReferenced() {
    when(rootModule.getName()).thenReturn("module1");
    JSModule module2 = mock(JSModule.class);
    when(moduleLoader.getModule(ArgumentMatchers.eq("module2")))
        .thenReturn(module2);
    Node prototypeNode = Nodeops.createProperty(Nodeops.createObject(),
        Nodeops.createString("prototype"));
    Node methodNode = Nodeops.createMethod("foo", null);
    Node methodPropertyNode = Nodeops.createProperty(prototypeNode,
        methodNode);
    Node unreferencedMethodPropertyNode = Nodeops.createProperty(
        Nodeops.createObject(), Nodeops.createString("unreferencedFoo"));

    rootNode.addChildToFront(prototypeNode);
    rootNode.addChildToFront(unreferencedMethodPropertyNode);

    crossModuleMethodMotion.process(rootNode, rootNode);

    assertNull(methodPropertyNode);
  }

  @Test
  public void testMoveMethodWhenReadsClosureVariables() {
    when(rootModule.getName()).thenReturn("module1");
    JSModule module2 = mock(JSModule.class);
    when(moduleLoader.getModule(ArgumentMatchers.eq("module2")))
        .thenReturn(module2);
    Node prototypeNode = Nodeops.createProperty(Nodeops.createObject(),
        Nodeops.createString("prototype"));
    Node methodNode = Nodeops.createMethod("foo", null);
    Node methodPropertyNode = Nodeops.createProperty(prototypeNode,
        methodNode);
    Node closureVariableNode = Nodeops.createVariable("x");
    Node closureVariableAssignmentNode = Nodeops.createAssignment(
        methodNode, closureVariableNode);

    rootNode.addChildToFront(prototypeNode);

    crossModuleMethodMotion.process(rootNode, rootNode);

    assertNull(methodPropertyNode);
  }

  @Test
  public void testMoveMethodWithGettersAndSetters() {
    when(rootModule.getName()).thenReturn("module1");
    JSModule module2 = mock(JSModule.class);
    when(moduleLoader.getModule(ArgumentMatchers.eq("module2")))
        .thenReturn(module2);
    Node prototypeNode = Nodeops.createProperty(Nodeops.createObject(),
        Nodeops.createString("prototype"));
    Node getterNode = Nodeops.createGetmethod("getFoo", null, null);
    Node setterNode = Nodeops.createSetmethod("setFoo", null, null);
    Node methodNode = Nodeops.createMethod("foo", null);
    Node methodPropertyNode = Nodeops.createProperty(prototypeNode, methodNode);
    Node getterPropertyNode = Nodeops.createProperty(prototypeNode, getterNode);
    Node setterPropertyNode = Nodeops.createProperty(prototypeNode, setterNode);

    rootNode.addChildToFront(prototypeNode);

    crossModuleMethodMotion.process(rootNode, rootNode);

    assertNull(methodPropertyNode);
  }

  @Test(expected = AssertionError.class)
  public void testMoveMethodWhenNullDeepestCommonModuleRef() {
    when(rootModule.getName()).thenReturn("module1");
    JSModule module2 = mock(JSModule.class);
    when(moduleLoader.getModule(ArgumentMatchers.eq("module2")))
        .thenReturn(module2);
    Node prototypeNode = Nodeops.createProperty(Nodeops.createObject(),
        Nodeops.createString("prototype"));
    Node methodNode = Nodeops.createMethod("foo", null);
    Node methodPropertyNode = Nodeops.createProperty(prototypeNode,
        methodNode);

    rootNode.addChildToFront(prototypeNode);
    when(methodPropertyNode.getDeepestCommonModuleRef())
        .thenReturn(null);

    crossModuleMethodMotion.process(rootNode, rootNode);
  }
}