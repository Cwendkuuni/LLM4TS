import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ObjectType.OBJECT, ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", ObjectType.OBJECT, ImmutableList.of());
    SubclassType type = SubclassType.INHERITS;
    ClosureCodingConvention.applySubclassRelationship(parentCtor, childCtor, type);
    assertEquals(parentCtor, childCtor.getPrototype());
    assertEquals(childCtor, childCtor.getPrototype().getConstructor());
  }

  @Test
  public void testApplySubclassRelationshipWithMixin() {
    FunctionType parentPrototype = new FunctionType("ParentPrototype", ObjectType.OBJECT, ImmutableList.of());
    FunctionType childPrototype = new FunctionType("ChildPrototype", ObjectType.OBJECT, ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", ObjectType.OBJECT, ImmutableList.of());
    SubclassType type = SubclassType.MIXIN;
    ClosureCodingConvention.applySubclassRelationship(parentPrototype, childPrototype, type);
    Node parentClass = new Node(Token.UNDEFINED, "ParentClass");
    parentClass.setQualifiedName("ParentClass");
    Node childClass = new Node(Token.UNDEFINED, "ChildClass");
    childClass.setQualifiedName("ChildClass");
    childPrototype.defineDeclaredProperty("constructor", childCtor, parentPrototype.getSource());
    childPrototype.defineDeclaredProperty("superClass_", parentPrototype, parentPrototype.getSource());
    parentClass.setPrototype(parentPrototype);
    childClass.setPrototype(childPrototype);
    assertEquals(parentPrototype, childPrototype.getPrototype());
    assertEquals(childCtor, childPrototype.getPrototype().getConstructor());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = new Node(Token.CALL, "goog.provide", ImmutableList.of(new Node(Token.STRING, "X")));
    assertEquals("X", ClosureCodingConvention.extractClassNameIfProvide(node, null));
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = new Node(Token.CALL, "goog.require", ImmutableList.of(new Node(Token.STRING, "X")));
    assertEquals("X", ClosureCodingConvention.extractClassNameIfRequire(node, null));
  }

  @Test
  public void testIsSuperClassReference() {
    assertTrue(ClosureCodingConvention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testExtractClassNameIfGoog() {
    Node node = new Node(Token.CALL, new Node(Token.GETPROP,
        new Node(Token.STRING, "goog"), new Node(Token.STRING, "require")),
        ImmutableList.of(new Node(Token.ARRAYLIT, ImmutableList.of(
            new Node(Token.STRING, "X")))));
    assertEquals("X", ClosureCodingConvention.extractClassNameIfGoog(node, null, "goog.require"));
  }

  @Test
  public void testGetExportPropertyFunction() {
    assertEquals("goog.exportProperty", ClosureCodingConvention.getExportPropertyFunction());
  }

  @Test
  public void testGetExportSymbolFunction() {
    assertEquals("goog.exportSymbol", ClosureCodingConvention.getExportSymbolFunction());
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = new Node(Token.CALL, "goog.addDependency",
        ImmutableList.of(new Node(Token.STRING, "X"),
            new Node(Token.ARRAYLIT, ImmutableList.of(
                new Node(Token.STRING, "type1"),
                new Node(Token.STRING, "type2")))));
    List<String> result = ClosureCodingConvention.identifyTypeDeclarationCall(node);
    assertEquals(ImmutableList.of("type1", "type2"), result);
  }

  @Test
  public void testGetAbstractMethodName() {
    assertEquals("goog.abstractMethod", ClosureCodingConvention.getAbstractMethodName());
  }

  @Test
  public void testSingletonGetterClassName() {
    Node node = new Node(Token.CALL, "goog.addSingletonGetter",
        ImmutableList.of(new Node(Token.STRING, "getName")));
    assertEquals("getName", ClosureCodingConvention.getSingletonGetterClassName(node));
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("GetterFunction", ObjectType.OBJECT, ImmutableList.of());
    ObjectType objectType = new ObjectType("GetterObject");
    ClosureCodingConvention.applySingletonGetter(functionType, objectType, functionType);
    assertEquals(functionType, functionType.getPrototype().get("getInstance"));
    assertEquals(objectType, functionType.getPrototype().get("instance_"));
  }

  @Test(expected = AssertionError.class)
  public void testObjectLiteralCast() {
    FunctionType functionType = new FunctionType("FunctionType", ObjectType.OBJECT, ImmutableList.of());
    Node callNode = new Node(Token.CALL,
        new Node(Token.GETPROP, new Node(Token.STRING, "goog.reflect.object"),
            new Node(Token.STRING, "cast")),
        ImmutableList.of(functionType, new Node(Token.OBJECTLIT, ImmutableList.of())));
    ClosureCodingConvention.getObjectLiteralCast(null, callNode);
  }
}