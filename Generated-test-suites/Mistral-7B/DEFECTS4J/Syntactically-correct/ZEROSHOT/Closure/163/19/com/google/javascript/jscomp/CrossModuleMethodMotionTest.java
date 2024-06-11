import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Compiler.factory;
import static com.google.javascript.jscomp.JSError.make;
import static com.google.javascript.rhino.Node.*;
import static com.google.javascript.rhino.IR.*;

import java.util.Collections;

import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() {
    // Given
    Compiler compiler = factory(null);
    AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        abstractCompiler, idGenerator, true);

    // When
    Node root = parseCompilationUnit("source.js");
    Node externsRoot = parseCompilationUnit("externs.js");
    pass.process(externsRoot, root);

    // Then
    // No errors were reported
    assertTrue(compiler.getDiagnostics().isEmpty());

    // Check that the method was moved
    Node movedMethod = findNode("A.prototype.b", root);
    assertNotNull(movedMethod);
    assertTrue(movedMethod.isFunctionDeclaration());
    assertFalse(movedMethod.isGetterDef());
    assertFalse(movedMethod.isSetterDef());
    assertNull(movedMethod.getPrototype());
    Node newParent = findNode("A.prototype", root);
    assertEquals(movedMethod, newParent.getFirstChild());

    // Check that the method was stubbed in the original module
    Node originalModule = findNode("module1.js", root);
    Node originalMethod = findNode("A.prototype.b", originalModule);
    assertNotNull(originalMethod);
    assertFalse(originalMethod.isFunctionDeclaration());
    Node stubCall = originalMethod.getFirstChild();
    assertEquals(STUB_METHOD_NAME, stubCall.getQualifiedName());
    assertEquals(1, stubCall.getNumChildren());
    Node stubIdNode = stubCall.getFirstChild();
    assertEquals(Number.class, stubIdNode.getType());

    // Check that the method was unstubbed in the deeper module
    Node deeperModule = findNode("module2.js", root);
    Node unstubParent = deeperModule;
    Node newUnstubCall = null;
    for (Node child : deeperModule.getChildren()) {
      if (child.isCode()) {
        unstubParent = child;
        for (Node grandChild : child.getChildren()) {
          if (grandChild.isCall() && grandChild.getQualifiedName().equals(UNSTUB_METHOD_NAME)) {
            newUnstubCall = grandChild;
            break;
          }
        }
      }
      if (newUnstubCall != null) {
        break;
      }
    }
    assertNotNull(newUnstubCall);
    assertEquals(2, newUnstubCall.getNumChildren());
    Node stubIdNode = newUnstubCall.getFirstChild();
    Node unstubBody = newUnstubCall.getLastChild();
    assertTrue(stubIdNode.isNumberLiteral());
    assertEquals(Number.class, stubIdNode.getType());
    assertTrue(unstubBody.isFunction());

    // Check that the stub function was added to the top-most module
    Node topModule = findNode("top_level.js", root);
    Node declarations = topModule;
    for (Node child : topModule.getChildren()) {
      if (child.isCompilationUnit()) {
        declarations = child;
        break;
      }
    }
    Node stubDeclarations = findNode(STUB_DECLARATIONS, declarations);
    assertNotNull(stubDeclarations);
  }

  private Node findNode(String name, Node container) {
    for (Node child : container.getChildren()) {
      if (child.isGetterDef() || child.isSetterDef() || child.isFunctionDeclaration()) {
        Node nameNode = child.getFirstChild();
        if (nameNode != null && nameNode.isIdentifier() && nameNode.getString().equals(name)) {
          return child;
        }
      } else if (child.isObjectLiteral()) {
        Node prototype = child.getFirstChild();
        if (prototype != null && prototype.isObjectLiteral()) {
          for (Node property : prototype.getChildren()) {
            if (property.isProperty()) {
              Node nameNode = property.getFirstChild();
              if (nameNode != null && nameNode.isIdentifier() && nameNode.getString().equals(name)) {
                return property;
              }
            }
          }
        }
      }
    }
    return null;
  }

  private Node parseCompilationUnit(String fileName) {
    return parseTree("file:\"" + fileName + "\";", null).getFirstChild();
  }
}