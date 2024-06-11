import static org.junit.Assert.*;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private CompilerPass pass;
  private Node root;

  @Before
  public void setUp() {
    compiler = new Compiler();
    safeNameIdSupplier = () -> "test_" + System.currentTimeMillis();
    pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
    root = Node.objectLiteral();
  }

  @Test
  public void testInlineSimpleObject() {
    ReferenceCollection referenceInfo = new ReferenceCollection();
    Scope scope = new Scope();

    Var v = scope.declareVar("obj", Node.objectLiteral());
    Reference reference = new Reference(null, v, null, null, null, null, null);
    referenceInfo.addReference(reference);

    pass.process(Node.SCOPE_NODE, root);

    List<Reference> references = referenceInfo.references;
    assertEquals(1, references.size());
    Reference ref = references.get(0);
    assertEquals(Node.VAR_DECL, ref.getParent().getType());
    String varName = ref.getNode().getString();
    assertEquals("JSCompiler_object_inline_test_" + System.currentTimeMillis(), varName);
  }

  @Test
  public void testInlineObjectWithInitialValue() {
    ReferenceCollection referenceInfo = new ReferenceCollection();
    Scope scope = new Scope();

    Var v = scope.declareVar("obj", Node.objectLiteral());
    Reference initReference = new Reference(Token.ASSIGN, v, null, null, null, null, null);
    initReference.setInitializingDeclaration(true);

    Var initVar = scope.declareVar("init", Node.numberLiteral(42));

    Reference init = new Reference(Token.ASSIGN, initVar, initReference, null, null, null, null);
    referenceInfo.addReference(init);

    pass.process(Node.SCOPE_NODE, root);

    List<Reference> references = referenceInfo.references;
    assertEquals(2, references.size());

    Reference refObj = references.get(0);
    assertEquals(Node.VAR_DECL, refObj.getParent().getType());
    String varNameObj = refObj.getNode().getString();
    assertEquals("JSCompiler_object_inline_test_" + System.currentTimeMillis(), varNameObj);

    Reference refInit = references.get(1);
    assertEquals(Node.ASSIGN, refInit.getParent().getType());
    assertEquals(Node.VAR, refInit.getGrandparent().getType());
    assertEquals("JSCompiler_object_inline_test_" + System.currentTimeMillis(), refInit.getNode().getString());
  }

  @Test
  public void testInlineObjectWithSingleKeyValue() {
    ReferenceCollection referenceInfo = new ReferenceCollection();
    Scope scope = new Scope();

    Var v = scope.declareVar("obj", Node.objectLiteral());
    Reference assignmentReference = new Reference(Token.ASSIGN, v, null, null, null, null, null);
    Node keyNode = Node.newString(Token.NAME, "key");
    Node valueNode = Node.numberLiteral(42);
    Reference keyValueReference = new Reference(Token.ASSIGN, null, assignmentReference, keyNode, null, valueNode, null);
    referenceInfo.addReference(keyValueReference);

    pass.process(Node.SCOPE_NODE, root);

    List<Reference> references = referenceInfo.references;
    assertEquals(2, references.size());

    Reference refObj = references.get(0);
    assertEquals(Node.VAR_DECL, refObj.getParent().getType());
    String varNameObj = refObj.getNode().getString();
    assertEquals("JSCompiler_object_inline_test_" + System.currentTimeMillis(), varNameObj);

    Reference refKeyValue = references.get(1);
    assertEquals(Node.ASSIGN, refKeyValue.getParent().getType());
    assertEquals(Node.GETPROP, refKeyValue.getGrandparent().getType());
    assertEquals("JSCompiler_object_inline_test_" + System.currentTimeMillis(), refKeyValue.getNode().getString());
  }

  @Test
  public void testInlineObjectWithMultipleKeyValues() {
    ReferenceCollection referenceInfo = new ReferenceCollection();
    Scope scope = new Scope();

    Var v = scope.declareVar("obj", Node.objectLiteral());
    Reference assignmentReference = new Reference(Token.ASSIGN, v, null, null, null, null, null);

    Node keyNode1 = Node.newString(Token.NAME, "key1");
    Node valueNode1 = Node.numberLiteral(1);
    Reference keyValueReference1 = new Reference(Token.ASSIGN, null, assignmentReference, keyNode1, null, valueNode1, null);
    referenceInfo.addReference(keyValueReference1);

    Node keyNode2 = Node.newString(Token.NAME, "key2");
    Node valueNode2 = Node.numberLiteral(2);
    Reference keyValueReference2 = new Reference(Token.ASSIGN, null, assignmentReference, keyNode2, null, valueNode2, null);
    referenceInfo.addReference(keyValueReference2);

    pass.process(Node.SCOPE_NODE, root);

    List<Reference> references = referenceInfo.references;
    assertEquals(4, references.size());

    Reference refObj = references.get(0);
    assertEquals(Node.VAR_DECL, refObj.getParent().getType());
    String varNameObj = refObj.getNode().getString();
    assertEquals("JSCompiler_object_inline_test_" + System.currentTimeMillis(), varNameObj);

    Reference refKeyValue1 = references.get(1);
    assertEquals(Node.ASSIGN, refKeyValue1.getParent().getType());
    assertEquals(Node.GETPROP, refKeyValue1.getGrandparent().getType());
    assertEquals("JSCompiler_object_inline_test_" + System.currentTimeMillis(), refKeyValue1.getNode().getString());

    Reference refKeyValue2 = references.get(2);
    assertEquals(Node.ASSIGN, refKeyValue2.getParent().getType());
    assertEquals(Node.GETPROP, refKeyValue2.getGrandparent().getType());
    assertEquals("JSCompiler_object_inline_test_" + System.currentTimeMillis(), refKeyValue2.getNode().getString());

    Reference refKey1 = references.get(3);
    assertEquals(Node.VAR_DECL, refKey1.getParent().getType());
    String varNameKey1 = refKey1.getNode().getString();
    assertEquals("JSCompiler_object_inline_test_key1_" + System.currentTimeMillis(), varNameKey1);
  }
}