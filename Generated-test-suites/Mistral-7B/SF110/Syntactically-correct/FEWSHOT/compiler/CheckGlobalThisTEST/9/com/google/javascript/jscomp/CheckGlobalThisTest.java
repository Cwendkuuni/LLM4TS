package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckGlobalThis.GLOBAL_THIS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis check = new CheckGlobalThis(null);

    // Traverse a function that has no parent (script node)
    Node functionNode = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    assertTrue(check.shouldTraverse(new NodeTraversal(new TestCompiler(), null), functionNode, null));

    // Traverse a function that is a constructor or has the @constructor annotation
    JSDocInfo jsDoc = new JSDocInfo("test", null, null, true, null, null, null, null, null, null, null);
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", jsDoc, null, null);
    assertTrue(check.shouldTraverse(new NodeTraversal(new TestCompiler(), null), functionNode, null));

    // Traverse a function that has the @this annotation
    jsDoc = new JSDocInfo("test", null, null, null, null, null, "@this", null, null, null, null);
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", jsDoc, null, null);
    assertTrue(check.shouldTraverse(new NodeTraversal(new TestCompiler(), null), functionNode, null));

    // Traverse a function that is not in a prototype
    Node objectNode = AbstractNodeFactory.createObjectLiteral("o", null, null);
    Node functionNodeInObject = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    Node propertyNode = AbstractNodeFactory.createProperty("test", functionNodeInObject, null, null);
    Node assignNode = AbstractNodeFactory.createAssign("o", propertyNode, null);
    assertTrue(check.shouldTraverse(new NodeTraversal(new TestCompiler(), null), functionNodeInObject, assignNode));

    // Traverse a function that is a prototype method
    Node prototypeNode = AbstractNodeFactory.createObjectLiteral("p", null, null);
    propertyNode = AbstractNodeFactory.createProperty("prototype", prototypeNode, null, null);
    Node constructorNode = AbstractNodeFactory.createFunctionDeclaration("C", null, null, null);
    Node protoNode = AbstractNodeFactory.createProperty("prototype", constructorNode, null, null);
    Node assignNodeToPrototype = AbstractNodeFactory.createAssign(protoNode, propertyNode, null);
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    Node methodNode = AbstractNodeFactory.createMethodDeclaration("test", functionNode, null, null, null, null);
    Node assignNodeToMethod = AbstractNodeFactory.createAssign(methodNode, null, null);
    assertTrue(check.shouldTraverse(new NodeTraversal(new TestCompiler(), null), functionNode, assignNodeToMethod));

    // Don't traverse non-global functions
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    Node functionNodeInBlock = AbstractNodeFactory.createBlock("{}", null);
    Node assignNodeToFunctionInBlock = AbstractNodeFactory.createAssign(functionNodeInBlock, functionNode, null);
    assertFalse(check.shouldTraverse(new NodeTraversal(new TestCompiler(), null), functionNode, assignNodeToFunctionInBlock));

    // Don't traverse functions getting lent to a prototype
    Node objectNodeGettingLent = AbstractNodeFactory.createObjectLiteral("o", null, null);
    Node functionNodeGettingLent = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    Node propertyNodeGettingLent = AbstractNodeFactory.createProperty("prototype", functionNodeGettingLent, null, null);
    Node assignNodeToPrototypeGettingLent = AbstractNodeFactory.createAssign(propertyNodeGettingLent, null, null);
    Node objectNodeGettingLentParent = AbstractNodeFactory.createObjectLiteral("o", null, null);
    Node assignNodeToObjectGettingLentParent = AbstractNodeFactory.createAssign(objectNodeGettingLentParent, propertyNodeGettingLent, null);
    assertFalse(check.shouldTraverse(new NodeTraversal(new TestCompiler(), null), functionNodeGettingLent, assignNodeToObjectGettingLentParent));
  }

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis check = new CheckGlobalThis(null);

    // Report a THIS on the left side of an assignment
    Node assignNode = AbstractNodeFactory.createAssign("this", null, null);
    Node leftSide = AbstractNodeFactory.createThis();
    Node rootNode = AbstractNodeFactory.createAssign(leftSide, assignNode, null);
    assertTrue(check.shouldReportThis(rootNode));

    // Report a THIS with a property access
    Node propertyNode = AbstractNodeFactory.createGetProp("prototype", null);
    Node rootNodeWithPropertyAccess = AbstractNodeFactory.createGetProp(leftSide, propertyNode, null);
    assertTrue(check.shouldReportThis(rootNodeWithPropertyAccess));

    // Don't report a THIS inside a constructor or a function annotated with @constructor
    JSDocInfo jsDoc = new JSDocInfo("test", null, null, true, null, null, null, null, null, null, null);
    Node functionNode = AbstractNodeFactory.createFunctionDeclaration("test", jsDoc, null, null);
    Node constructorNode = AbstractNodeFactory.createMethodDeclaration("test", functionNode, null, null, null, null);
    Node thisNode = AbstractNodeFactory.createThis();
    Node rootNodeInsideConstructor = AbstractNodeFactory.createGetProp(thisNode, constructorNode, null);
    assertFalse(check.shouldReportThis(rootNodeInsideConstructor));

    // Don't report a THIS inside a function annotated with @this
    jsDoc = new JSDocInfo("test", null, null, null, null, null, "@this", null, null, null, null);
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", jsDoc, null, null);
    thisNode = AbstractNodeFactory.createThis();
    Node rootNodeInsideThisAnnotatedFunction = AbstractNodeFactory.createGetProp(thisNode, functionNode, null);
    assertFalse(check.shouldReportThis(rootNodeInsideThisAnnotatedFunction));
  }

  @Test
  public void testTraverse() {
    CheckGlobalThis check = new CheckGlobalThis(new TestCompiler());

    // Traverse a script node
    Node rootNode = AbstractNodeFactory.createScript("this.x = 1;");
    NodeTraversal traverser = new NodeTraversal(new TestCompiler(), null);
    check.visit(traverser, null, rootNode);
    traverser.traverse(rootNode);

    // Traverse a function that has no parent (script node)
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    Node functionNodeParent = null;
    Node assignNodeToFunction = AbstractNodeFactory.createAssign("this", functionNode, null);
    Node rootNodeWithFunction = AbstractNodeFactory.createAssign(new ThisNode(), assignNodeToFunction, null);
    check.visit(traverser, null, rootNodeWithFunction);
    traverser.traverse(rootNodeWithFunction);
    assertTrue(check.compiler.getDiagnostics().isEmpty());

    // Traverse a function that is a constructor or has the @constructor annotation
    jsDoc = new JSDocInfo("test", null, null, true, null, null, null, null, null, null, null);
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", jsDoc, null, null);
    Node constructorNode = AbstractNodeFactory.createMethodDeclaration("test", functionNode, null, null, null, null);
    Node thisNodeInsideConstructor = AbstractNodeFactory.createThis();
    Node rootNodeWithConstructor = AbstractNodeFactory.createGetProp(thisNodeInsideConstructor, constructorNode, null);
    check.visit(traverser, null, rootNodeWithConstructor);
    traverser.traverse(rootNodeWithConstructor);
    assertTrue(check.compiler.getDiagnostics().isEmpty());

    // Traverse a function that has the @this annotation
    jsDoc = new JSDocInfo("test", null, null, null, null, null, "@this", null, null, null, null);
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", jsDoc, null, null);
    thisNode = AbstractNodeFactory.createThis();
    Node rootNodeWithThisAnnotatedFunction = AbstractNodeFactory.createGetProp(thisNode, functionNode, null);
    check.visit(traverser, null, rootNodeWithThisAnnotatedFunction);
    traverser.traverse(rootNodeWithThisAnnotatedFunction);
    assertTrue(check.compiler.getDiagnostics().isEmpty());

    // Traverse a function that is not in a prototype
    Node objectNode = AbstractNodeFactory.createObjectLiteral("o", null, null);
    propertyNode = AbstractNodeFactory.createProperty("test", functionNode, null, null);
    Node assignNodeToObject = AbstractNodeFactory.createAssign(objectNode, propertyNode, null);
    Node rootNodeWithFunctionInObject = AbstractNodeFactory.createAssign(new ThisNode(), assignNodeToObject, null);
    check.visit(traverser, null, rootNodeWithFunctionInObject);
    traverser.traverse(rootNodeWithFunctionInObject);
    assertTrue(check.compiler.getDiagnostics().isEmpty());

    // Traverse a function that is a prototype method
    Node prototypeNode = AbstractNodeFactory.createObjectLiteral("p", null, null);
    propertyNode = AbstractNodeFactory.createProperty("prototype", prototypeNode, null, null);
    constructorNode = AbstractNodeFactory.createFunctionDeclaration("C", null, null, null);
    protoNode = AbstractNodeFactory.createProperty("prototype", constructorNode, null, null);
    assignNodeToPrototype = AbstractNodeFactory.createAssign(protoNode, null, null);
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    methodNode = AbstractNodeFactory.createMethodDeclaration("test", functionNode, null, null, null, null);
    Node thisNodeInsideMethod = AbstractNodeFactory.createThis();
    Node rootNodeWithMethod = AbstractNodeFactory.createGetProp(thisNodeInsideMethod, methodNode, null);
    check.visit(traverser, null, rootNodeWithMethod);
    traverser.traverse(rootNodeWithMethod);
    assertTrue(check.compiler.getDiagnostics().isEmpty());

    // Don't traverse non-global functions
    functionNode = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    Node functionNodeInBlock = AbstractNodeFactory.createBlock("{}", null);
    assignNodeToFunctionInBlock = AbstractNodeFactory.createAssign(functionNodeInBlock, functionNode, null);
    Node rootNodeWithFunctionInBlock = AbstractNodeFactory.createAssign(new ThisNode(), assignNodeToFunctionInBlock, null);
    check.visit(traverser, null, rootNodeWithFunctionInBlock);
    traverser.traverse(rootNodeWithFunctionInBlock);
    assertTrue(check.compiler.getDiagnostics().contains(GLOBAL_THIS));

    // Don't traverse functions getting lent to a prototype
    objectNode = AbstractNodeFactory.createObjectLiteral("o", null, null);
    functionNodeGettingLent = AbstractNodeFactory.createFunctionDeclaration("test", null, null, null);
    propertyNodeGettingLent = AbstractNodeFactory.createProperty("prototype", functionNodeGettingLent, null, null);
    assignNodeToPrototypeGettingLent = AbstractNodeFactory.createAssign(propertyNodeGettingLent, null, null);
    objectNodeGettingLentParent = AbstractNodeFactory.createObjectLiteral("o", null, null);
    assignNodeToObjectGettingLentParent = AbstractNodeFactory.createAssign(objectNodeGettingLentParent, propertyNodeGettingLent, null);
    Node rootNodeWithFunctionGettingLent = AbstractNodeFactory.createAssign(new ThisNode(), assignNodeToObjectGettingLentParent, null);
    check.visit(traverser, null, rootNodeWithFunctionGettingLent);
    traverser.traverse(rootNodeWithFunctionGettingLent);
    assertTrue(check.compiler.getDiagnostics().contains(GLOBAL_THIS));
  }
}