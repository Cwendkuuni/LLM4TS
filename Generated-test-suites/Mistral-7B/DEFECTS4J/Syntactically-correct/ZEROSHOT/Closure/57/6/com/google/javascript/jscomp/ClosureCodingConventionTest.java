package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import java.util.List;

public class ClosureCodingConventionTest {

  private final static String CLASS_NAME = "ClosureCodingConventionTest";
  private final static String PARENT_CLASS_NAME = "ParentClass";
  private final static String CHILD_CLASS_NAME = "ChildClass";

  @org.junit.Before
  public void setUp() throws Exception {
    SourceFile source = new SourceFile(CLASS_NAME + ".js", null);
    ClosureCodingConvention codingConvention = new ClosureCodingConvention();
    NodeTraversal traversal = new NodeTraversal(source, codingConvention);
    Node rootNode = traversal.parse();
    codingConvention.init(null, rootNode);
  }

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = mockParentFunctionType();
    FunctionType childCtor = mockChildFunctionType();
    SubclassType subclassType = SubclassType.INHERITS;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, subclassType);

    FunctionType expectedParentPrototype = parentCtor.getPrototype();
    FunctionType actualParentPrototype = childCtor.getPrototype();
    assertEquals(expectedParentPrototype, actualParentPrototype);

    ObjectType expectedConstructor = new ObjectType(CHILD_CLASS_NAME,
        ImmutableList.<FunctionType>of(parentCtor), null);
    ObjectType actualConstructor = childCtor.getPrototype();
    assertEquals(expectedConstructor, actualConstructor);
  }

  @org.junit.Test
  public void testApplySubclassRelationshipWithMixin() {
    FunctionType parentPrototype = mockFunctionType(PARENT_CLASS_NAME,
        ImmutableList.<FunctionType>of());
    FunctionType childPrototype = mockFunctionType(CHILD_CLASS_NAME,
        ImmutableList.<FunctionType>of(parentPrototype));
    FunctionType parentCtor = new FunctionType(PARENT_CLASS_NAME,
        ImmutableList.<FunctionType>of(parentPrototype), null);
    FunctionType childCtor = new FunctionType(CHILD_CLASS_NAME,
        ImmutableList.<FunctionType>of(parentCtor), childPrototype);
    SubclassType subclassType = SubclassType.MIXIN;
    codingConvention.applySubclassRelationship(parentCtor, childCtor, subclassType);

    FunctionType expectedChildPrototype = childPrototype;
    FunctionType actualChildPrototype = childCtor.getPrototype();
    assertEquals(expectedChildPrototype, actualChildPrototype);
  }

  @org.junit.Test
  public void testExtractClassNameFromProvide() {
    Node rootNode = getNodeByPath(CLASS_NAME + ".js", "goog.provide",
        "goog.require", "ChildClass");
    String expectedClassName = CHILD_CLASS_NAME;
    String actualClassName = codingConvention.extractClassNameIfProvide(rootNode, rootNode);
    assertEquals(expectedClassName, actualClassName);
  }

  @org.junit.Test
  public void testExtractClassNameFromRequire() {
    Node rootNode = getNodeByPath(CLASS_NAME + ".js", "goog.require",
        "ChildClass");
    String expectedClassName = CHILD_CLASS_NAME;
    String actualClassName = codingConvention.extractClassNameIfRequire(rootNode, rootNode);
    assertEquals(expectedClassName, actualClassName);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    String expectedExportPropertyFunction = "goog.exportProperty";
    String actualExportPropertyFunction = codingConvention.getExportPropertyFunction();
    assertEquals(expectedExportPropertyFunction, actualExportPropertyFunction);
  }

  @org.junit.Test
  public void testGetSingletonGetterClassName() {
    Node rootNode = getNodeByPath(CLASS_NAME + ".js",
        "goog.addSingletonGetter", "ChildClass", "getName");
    String expectedSingletonGetterClassName = "getName";
    String actualSingletonGetterClassName = codingConvention
        .getSingletonGetterClassName(rootNode);
    assertEquals(expectedSingletonGetterClassName, actualSingletonGetterClassName);
  }

  @org.junit.Test
  public void testIsSuperClassReference() {
    String superClassPropertyName = "superClass_";
    boolean expectedResult = true;
    boolean actualResult = codingConvention.isSuperClassReference(superClassPropertyName);
    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testIdentifyTypeDeclarationCall() {
    Node rootNode = getNodeByPath(CLASS_NAME + ".js",
        "goog.addDependency", "goog.ArrayType");
    List<String> expectedTypeNames = ImmutableList.of("ArrayType");
    List<String> actualTypeNames = codingConvention.identifyTypeDeclarationCall(rootNode);
    assertEquals(expectedTypeNames, actualTypeNames);
  }

  @org.junit.Test
  public void testIsPropertyTestFunction() {
    Node rootNode = getNodeByPath(CLASS_NAME + ".js", "goog.isString");
    boolean expectedResult = true;
    boolean actualResult = codingConvention.isPropertyTestFunction(rootNode);
    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testGetObjectLiteralCast() {
    Node rootNode = getNodeByPath(CLASS_NAME + ".js",
        "goog.reflect.object", "goog.ArrayType");
    ObjectLiteralCast expectedObjectLiteralCast = new ObjectLiteralCast(
        "ArrayType", rootNode.getNext());
    ObjectLiteralCast actualObjectLiteralCast = codingConvention
        .getObjectLiteralCast(new NodeTraversal(rootNode), rootNode);
    assertEquals(expectedObjectLiteralCast, actualObjectLiteralCast);
  }

  @org.junit.Test
  public void testPropertyTestFunctions() {
    Node rootNode = getNodeByPath(CLASS_NAME + ".js", "goog.isDef");
    boolean expectedResult = true;
    boolean actualResult = codingConvention.isPropertyTestFunction(rootNode);
    assertEquals(expectedResult, actualResult);
  }

  @org.junit.Test
  public void testBind() {
    Node rootNode = getNodeByPath(CLASS_NAME + ".js", "goog.bind", "fn",
        "self", "args");
    Bind expectedBind = new Bind("fn", new Node("self"), "args");
    Bind actualBind = codingConvention.describeFunctionBind(rootNode);
    assertEquals(expectedBind, actualBind);
  }

  private FunctionType mockFunctionType(String name, List<FunctionType> prototypeList) {
    FunctionType functionType = new FunctionType(name, prototypeList, null);
    return functionType;
  }

  private FunctionType mockParentFunctionType() {
    FunctionType functionType = mockFunctionType(PARENT_CLASS_NAME,
        ImmutableList.<FunctionType>of());
    return functionType;
  }

  private FunctionType mockChildFunctionType() {
    FunctionType functionType = mockFunctionType(CHILD_CLASS_NAME,
        ImmutableList.<FunctionType>of());
    return functionType;
  }

  private Node getNodeByPath(String fileName, String... nodePaths) {
    SourceFile sourceFile = new SourceFile(fileName, null);
    NodeTraversal traversal = new NodeTraversal(sourceFile.getSourceCode(),
        new DefaultCodingConvention());
    Node rootNode = traversal.parse();
    for (Node currentNode = rootNode; currentNode != null; ) {
      for (String nodePath : nodePaths) {
        if (currentNode.getQualifiedName().equals(nodePath)) {
          return currentNode;
        }
        currentNode = currentNode.getNext();
      }
    }
    fail("Node not found: " + nodePaths);
    return null;
  }
}