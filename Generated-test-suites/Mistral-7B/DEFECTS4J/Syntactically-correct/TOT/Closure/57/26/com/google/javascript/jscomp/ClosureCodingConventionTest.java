package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import junit.framework.TestCase;

import java.util.List;

public class ClosureCodingConventionTest extends TestCase {

  private static final String CLASS_NAME = "MySubClass";
  private static final String SUPER_CLASS_NAME = "MySuperClass";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testApplySubclassRelationship() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    FunctionType parentCtor = createMockFunctionType(SUPER_CLASS_NAME);
    FunctionType childCtor = createMockFunctionType(CLASS_NAME);
    SubclassType relationship = convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);
    assertEquals(SubclassType.INHERITS, relationship);

    // Test case for superclass property and constructor
    assertEquals("superClass_", childCtor.getPrototype().getPropertyName("superClass_"));
    assertEquals(parentCtor, childCtor.getPrototype().getProperty("constructor"));
  }

  public void testGetclasssDefinedByCall() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node node = createMockNode("goog.inherits(MySubClass, MySuperClass)");
    SubclassRelationship relationship = convention.getclasssDefinedByCall(node);
    assertEquals(SubclassType.INHERITS, relationship.getType());
    assertEquals(CLASS_NAME, relationship.getSubclass().getQualifiedName());
    assertEquals(SUPER_CLASS_NAME, relationship.getSuperclass().getQualifiedName());
  }

  public void testExtractClassNameIfProvide() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node node = createMockNode("goog.provide('MyClass')");
    assertEquals("MyClass", convention.extractClassNameIfProvide(node, null));
  }

  public void testExtractClassNameIfRequire() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node node = createMockNode("goog.require('MyClass')");
    assertEquals("MyClass", convention.extractClassNameIfRequire(node, null));
  }

  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("anotherProperty"));
  }

  public void testEndsWithPrototype() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node node1 = createMockNode("a.b.c");
    assertFalse(convention.endsWithPrototype(node1));

    Node node2 = createMockNode("a.b.c.prototype");
    assertTrue(convention.endsWithPrototype(node2));
  }

  public void testGetExportPropertyFunction() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("goog.exportProperty", convention.getExportPropertyFunction());
  }

  public void testGetSingletonGetterClassName() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    Node node = createMockNode("goog.addSingletonGetter(MyClass, 'myProp')");
    assertEquals("myProp", convention.getSingletonGetterClassName(node));
  }

  public void testApplySingletonGetter() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    FunctionType functionType = createMockFunctionType("MyClass");
    FunctionType getterType = createMockFunctionType("MyClass.getter");
    ObjectType objectType = createMockObjectType("MyClass");

    convention.applySingletonGetter(functionType, getterType, objectType);

    assertEquals(getterType, functionType.getProperty("getInstance"));
    assertEquals(objectType, functionType.getProperty("instance_"));
  }

  private FunctionType createMockFunctionType(String name) {
    return new FunctionType(name, ImmutableList.<FunctionType>of());
  }

  private ObjectType createMockObjectType(String name) {
    return new ObjectType(name);
  }

  private Node createMockNode(String nodeString) {
    Node node = new Node(Token.STRING, nodeString);
    node.setSourcePosition(1, 1, 1);
    return node;
  }

  private NodeTraversal createMockNodeTraversal() {
    return new NodeTraversal() {
      @Override
      public FunctionType getFunctionType() {
        return null;
      }

      @Override
      public ObjectType getObjectType() {
        return null;
      }

      @Override
      public Node getParent() {
        return null;
      }

      @Override
      public Node getSource() {
        return null;
      }

      @Override
      public Node getSourceName() {
        return null;
      }

      @Override
      public void report(JSError error) {
        fail("Should not have reported an error");
      }

      @Override
      public void report(DiagnosticType diagnostic) {
        fail("Should not have reported a diagnostic");
      }

      @Override
      public void report(String message, int line, int column) {
        fail("Should not have reported a message");
      }
    };
  }
}