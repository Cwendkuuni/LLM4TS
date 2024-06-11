import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DiagnosticPosition;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private IdGenerator idGenerator;
  private CompilerPassManager passManager;
  private JSModule deepestCommonModuleRef;
  private JSModule module;
  private Node root;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    idGenerator = mock(IdGenerator.class);
    passManager = mock(CompilerPassManager.class);
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph());
    when(compiler.getPassManager()).thenReturn(passManager);

    root = Node.root();
    deepestCommonModuleRef = new JSModule("deepestCommonModule");
    module = new JSModule("module");
  }

  @Test
  public void testNullDeepestCommonModuleError() {
    when(passManager.getCompiler()).thenReturn(compiler);
    when(passManager.getPass(CompilerPass.Type.CROSS_MODULE_METHOD_MOTION)).thenReturn(
        new CrossModuleMethodMotion(compiler, idGenerator, false));

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);

    pass.process(null, root);

    assertNull(pass.analyzer);
    assertSame(NULL_COMMON_MODULE_ERROR, compiler.getLastDiagnostic());
  }

  @Test
  public void testMoveMethod() {
    when(passManager.getCompiler()).thenReturn(compiler);
    when(passManager.getPass(CompilerPass.Type.CROSS_MODULE_METHOD_MOTION)).thenReturn(
        new CrossModuleMethodMotion(compiler, idGenerator, false));

    // Declare a method in the deepestCommonModuleRef
    Node methodDeclaration = IR.function(
        IR.identifier("method"),
        new Node[0],
        Node.FunctionType.NORMAL,
        Node.EMPTY_NODE_LIST,
        Node.NORMAL_FUNCTION);
    deepestCommonModuleRef.setBody(methodDeclaration);

    // Declare the same method in the module
    Node methodDeclarationInModule = methodDeclaration.cloneTree();
    Node methodCall = IR.call(IR.getprop(methodDeclarationInModule.cloneTree(), IR.string("method")), Node.NEW_ARRAY_EMPTY);
    methodCall.putBooleanProp(Node.IS_CALL, true);
    methodCall.putBooleanProp(Node.IS_NAMED_CALL, false);
    Node methodAssignment = IR.assign(IR.getprop(root.cloneTree(), IR.string("module.method")), methodCall);
    root.addChildToBack(methodAssignment);

    // Declare a name info for the method
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.readsClosureVariables()).thenReturn(false);

    // Test that the method is not moved if it is a getter or a setter
    Property getterProperty = mock(Property.class);
    when(getterProperty.getValue().isGetterDef()).thenReturn(true);
    when(nameInfo.getDeclarations()).thenReturn(new NameInfo.DeclarationList(getterProperty));
    pass.moveMethods(new NameInfo[0]);
    assertSame(methodDeclarationInModule, IR.getprop(root, "module.method"));

    // Test that the method is moved if it is not a getter or a setter
    Property functionProperty = mock(Property.class);
    when(functionProperty.getValue().isFunction()).thenReturn(true);
    when(nameInfo.getDeclarations()).thenReturn(new NameInfo.DeclarationList(functionProperty));
    pass.moveMethods(new NameInfo[0]);
    assertNull(IR.getprop(root, "module.method"));
    assertSame(
        IR.call(
            IR.name(STUB_METHOD_NAME),
            IR.number(idGenerator.newId())),
        IR.getprop(root, "module.method"));
  }

  @Test
  public void testUnstubMethod() {
    when(passManager.getCompiler()).thenReturn(compiler);
    when(passManager.getPass(CompilerPass.Type.CROSS_MODULE_METHOD_MOTION)).thenReturn(
        new CrossModuleMethodMotion(compiler, idGenerator, false));

    // Declare a method in the deepestCommonModuleRef
    Node methodDeclaration = IR.function(
        IR.identifier("method"),
        new Node[0],
        Node.FunctionType.NORMAL,
        Node.EMPTY_NODE_LIST,
        Node.NORMAL_FUNCTION);
    deepestCommonModuleRef.setBody(methodDeclaration);

    // Declare the same method in the module with a stub
    Node methodDeclarationInModule = methodDeclaration.cloneTree();
    Node methodCall = IR.call(IR.getprop(methodDeclarationInModule.cloneTree(), IR.string("method")), Node.NEW_ARRAY_EMPTY);
    methodCall.putBooleanProp(Node.IS_CALL, true);
    methodCall.putBooleanProp(Node.IS_NAMED_CALL, false);
    Node stub = IR.call(IR.name(STUB_METHOD_NAME), IR.number(idGenerator.newId()));
    Node methodAssignment = IR.assign(IR.getprop(root.cloneTree(), IR.string("module.method")), stub);
    root.addChildToBack(methodAssignment);

    // Declare a name info for the method
    NameInfo nameInfo = mock(NameInfo.class);
    when(nameInfo.getDeepestCommonModuleRef()).thenReturn(deepestCommonModuleRef);
    when(nameInfo.isReferenced()).thenReturn(true);
    when(nameInfo.readsClosureVariables()).thenReturn(false);

    // Test that the method is not moved if it is already stubbed
    when(nameInfo.getDeclarations()).thenReturn(new NameInfo.DeclarationList(IR.PROPERTY));
    pass.moveMethods(new NameInfo[0]);
    assertSame(
        IR.call(
            IR.name(STUB_METHOD_NAME),
            IR.number(idGenerator.newId())),
        IR.getprop(root, "module.method"));

    // Test that the method is unstubbed if it is not already stubbed
    Node unstubMethod = IR.function(
        IR.identifier("unstubMethod"),
        new Node[0],
        Node.FunctionType.NORMAL,
        Node.EMPTY_NODE_LIST,
        Node.NORMAL_FUNCTION);
    Node unstubMethodBody = IR.block(new Node[0]);
    Node unstubCall = IR.call(IR.name(UNSTUB_METHOD_NAME), IR.number(idGenerator.newId()), unstubMethodBody);
    Node unstubAssignment = IR.assign(IR.getprop(methodDeclaration, IR.string("method")), unstubCall);
    methodDeclaration.replaceChild(methodCall, unstubAssignment);

    when(nameInfo.getDeclarations()).thenReturn(
        new NameInfo.DeclarationList(new Property(methodDeclaration, unstubCall)));
    pass.moveMethods(new NameInfo[0]);
    assertSame(unstubMethodBody, IR.getprop(deepestCommonModuleRef, "method.method"));
  }

  @Test
  public void testStubDeclaration() {
    when(passManager.getCompiler()).thenReturn(compiler);
    when(passManager.getPass(CompilerPass.Type.CROSS_MODULE_METHOD_MOTION)).thenReturn(
        new CrossModuleMethodMotion(compiler, idGenerator, false));

    // Test that the stub declarations are added when there are any stubs
    when(passManager.getCompiler().getNodeForCodeInsertion(null)).thenReturn(root);
    pass.process(root, root);
    assertSame(STUB_DECLARATIONS, IR.getfirstChild(root));

    // Test that the stub declarations are not added when there are no stubs
    when(passManager.getCompiler().getNodeForCodeInsertion(null)).thenReturn(null);
    pass.process(root, root);
    assertNull(IR.getfirstChild(root));
  }
}