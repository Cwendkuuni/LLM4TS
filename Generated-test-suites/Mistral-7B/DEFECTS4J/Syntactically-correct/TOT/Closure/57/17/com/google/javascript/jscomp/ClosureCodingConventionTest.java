package com.google.javascript.jscomp.tests;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.SubclassRelationship;
import com.google.javascript.jscomp.SubclassType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = mock(FunctionType.class);
    FunctionType childCtor = mock(FunctionType.class);
    when(parentCtor.getPrototype()).thenReturn(parentPrototype);
    when(parentCtor.getSource()).thenReturn("ParentClass.js");
    when(childCtor.getPrototype()).thenReturn(childPrototype);
    when(childCtor.getSource()).thenReturn("ChildClass.js");

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);

    // Check that "superClass_" property is defined in child class
    assertThat(childPrototype.hasProperty("superClass_")).isTrue();
    assertThat(childPrototype.getProperty("superClass_")).isEqualTo(parentPrototype);

    // Check that "constructor" property is defined in child class
    assertThat(childPrototype.hasProperty("constructor")).isTrue();
    assertThat(childPrototype.getProperty("constructor")).isEqualTo(childCtor);
  }

  @Test
  public void testGetclasssDefinedByCall() {
    Node callNode = createCallNode("ChildClass.inherits(ParentClass)");
    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(callNode);

    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.INHERITS);
    assertThat(relationship.getSubclass()).isEqualTo("ChildClass");
    assertThat(relationship.getSuperclass()).isEqualTo("ParentClass");
  }

  @Test
  public void testGetclasssDefinedByCallWithPrototype() {
    Node callNode = createCallNode("ChildClass.prototype.mixin(ParentClass.prototype)");
    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(callNode);

    assertThat(relationship).isNotNull();
    assertThat(relationship.getType()).isEqualTo(SubclassType.MIXIN);
    assertThat(relationship.getSubclass()).isEqualTo("ChildClass.prototype");
    assertThat(relationship.getSuperclass()).isEqualTo("ParentClass.prototype");
  }

  @Test
  public void testGetclasssDefinedByCallInvalid() {
    Node callNode = createCallNode("ConditionalClass.inherits(ParentClass1) : BaseClass2");
    SubclassRelationship relationship = new ClosureCodingConvention().getclasssDefinedByCall(callNode);

    assertThat(relationship).isNull();
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = createCallNode("goog.provide('MyClass')");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);

    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = createCallNode("goog.require('MyClass')");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);

    assertThat(className).isEqualTo("MyClass");
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();

    assertThat(convention.isSuperClassReference("superClass_")).isTrue();
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = createCallNode("goog.addDependency('MyClass', [MyInterface])");
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);

    assertThat(typeNames).hasSize(1);
    assertThat(typeNames.get(0)).isEqualTo("MyClass");
  }

  private Node createCallNode(String callString) {
    Node callNode = new Node();
    callNode.setType(Token.CALL);
    Node callName = new Node();
    callName.setType(Token.GETPROP);
    callName.setString(callString);
    callNode.addChild(callName);
    return callNode;
  }

  private ObjectType parentPrototype = new ObjectType("ParentClass", null);
  private ObjectType childPrototype = new ObjectType("ChildClass", null);
}