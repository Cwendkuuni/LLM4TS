import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.jstype.*;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ObjectType.INSTANCE,
        ImmutableList.<FunctionType>of());

    FunctionType childCtor = new FunctionType("ChildCtor", ObjectType.INSTANCE,
        ImmutableList.<FunctionType>of(parentCtor.getType()));

    SubclassType type = SubclassType.INHERITS;
    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    ObjectType parentPrototype = parentCtor.getPrototype();
    ObjectType childPrototype = childCtor.getPrototype();

    assertEquals("constructor", childPrototype, parentPrototype.getDeclaredField("constructor"));
    assertEquals("superClass_", childPrototype, parentCtor);
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = Node.newExpr("goog.provide('X')");
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testExtractClassNameIfRequire() {
    Node node = Node.newExpr("goog.require('X')");
    String className = new ClosureCodingConvention().extractClassNameIfRequire(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testGetExportPropertyFunction() {
    String exportPropertyFunction = new ClosureCodingConvention().getExportPropertyFunction();
    assertEquals("goog.exportProperty", exportPropertyFunction);
  }

  @Test
  public void testIdentifyTypeDeclarationCall() {
    Node node = Node.newExpr("goog.addDependency('X', [Type1, Type2])");
    List<String> typeNames = new ClosureCodingConvention().identifyTypeDeclarationCall(node);
    assertEquals(2, typeNames.size());
    assertEquals("Type1", typeNames.get(0));
    assertEquals("Type2", typeNames.get(1));
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
    assertFalse(convention.isSuperClassReference("anotherProperty"));
  }

  @Test
  public void testGetSingletonGetterClassName() {
    Node callNode = Node.newExpr("goog.addSingletonGetter(obj, getterName)");
    String getterClassName = new ClosureCodingConvention().getSingletonGetterClassName(callNode);
    assertEquals("getterName", getterClassName);
  }

  @Test
  public void testApplySingletonGetter() {
    FunctionType getterType = new FunctionType("getter", ObjectType.INSTANCE,
        ImmutableList.<FunctionType>of());

    FunctionType functionType = new FunctionType("obj", ObjectType.INSTANCE,
        ImmutableList.<FunctionType>of(getterType.getType()));

    ObjectType objectType = new ObjectType("ObjectType", getterType.getType(),
        ImmutableList.<FunctionType>of());

    new ClosureCodingConvention().applySingletonGetter(functionType, getterType, objectType);

    FunctionType instance = functionType.getDeclaredMethod("getInstance");
    ObjectType instanceType = instance.getReturnType();
    FunctionType getter = instance.getDeclaredMethod("getInstance").getPrototype()
        .getDeclaredMethod("get");
    ObjectType getterType = getter.getReturnType();

    assertEquals("ObjectType", instanceType.getName());
    assertEquals("get", getter.getName());
    assertEquals("Function", getterType.getBaseType().getName());
  }

  @Test
  public void testPropertyTestFunctions() {
    FunctionType isDef = new FunctionType("isDef", FunctionType.INSTANCE,
        ImmutableList.<FunctionType>of(FunctionType.STRING_TYPE, FunctionType.OBJECT_TYPE));

    FunctionType isNull = new FunctionType("isNull", FunctionType.INSTANCE,
        ImmutableList.<FunctionType>of(FunctionType.OBJECT_TYPE));

    FunctionType isDefAndNotNull = new FunctionType("isDefAndNotNull",
        FunctionType.BOOLEAN_TYPE,
        ImmutableList.<FunctionType>of(FunctionType.OBJECT_TYPE));

    FunctionType isString = new FunctionType("isString", FunctionType.BOOLEAN_TYPE,
        ImmutableList.<FunctionType>of(FunctionType.OBJECT_TYPE));

    FunctionType isNumber = new FunctionType("isNumber", FunctionType.BOOLEAN_TYPE,
        ImmutableList.<FunctionType>of(FunctionType.OBJECT_TYPE));

    FunctionType isBoolean = new FunctionType("isBoolean", FunctionType.BOOLEAN_TYPE,
        ImmutableList.<FunctionType>of(FunctionType.OBJECT_TYPE));

    FunctionType isFunction = new FunctionType("isFunction", FunctionType.BOOLEAN_TYPE,
        ImmutableList.<FunctionType>of(FunctionType.OBJECT_TYPE));

    FunctionType isArray = new FunctionType("isArray", FunctionType.BOOLEAN_TYPE,
        ImmutableList.<FunctionType>of(FunctionType.OBJECT_TYPE));

    FunctionType isObject = new FunctionType("isObject", FunctionType.BOOLEAN_TYPE,
        ImmutableList.<FunctionType>of(FunctionType.OBJECT_TYPE));

    ClosureCodingConvention convention = new ClosureCodingConvention();

    Node isDefNode = Node.newExpr("goog.isDef(obj, 'propertyName')");
    assertTrue(convention.isPropertyTestFunction(isDefNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isDefNode.getType().getReturnType());
    assertEquals("isDef", isDefNode.getFirstChild().getQualifiedName());

    Node isNullNode = Node.newExpr("goog.isNull(obj)");
    assertTrue(convention.isPropertyTestFunction(isNullNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isNullNode.getType().getReturnType());
    assertEquals("isNull", isNullNode.getFirstChild().getQualifiedName());

    Node isDefAndNotNullNode = Node.newExpr("goog.isDefAndNotNull(obj)");
    assertTrue(convention.isPropertyTestFunction(isDefAndNotNullNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isDefAndNotNullNode.getType().getReturnType());
    assertEquals("isDefAndNotNull", isDefAndNotNullNode.getFirstChild().getQualifiedName());

    Node isStringNode = Node.newExpr("goog.isString(obj)");
    assertTrue(convention.isPropertyTestFunction(isStringNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isStringNode.getType().getReturnType());
    assertEquals("isString", isStringNode.getFirstChild().getQualifiedName());

    Node isNumberNode = Node.newExpr("goog.isNumber(obj)");
    assertTrue(convention.isPropertyTestFunction(isNumberNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isNumberNode.getType().getReturnType());
    assertEquals("isNumber", isNumberNode.getFirstChild().getQualifiedName());

    Node isBooleanNode = Node.newExpr("goog.isBoolean(obj)");
    assertTrue(convention.isPropertyTestFunction(isBooleanNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isBooleanNode.getType().getReturnType());
    assertEquals("isBoolean", isBooleanNode.getFirstChild().getQualifiedName());

    Node isFunctionNode = Node.newExpr("goog.isFunction(obj)");
    assertTrue(convention.isPropertyTestFunction(isFunctionNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isFunctionNode.getType().getReturnType());
    assertEquals("isFunction", isFunctionNode.getFirstChild().getQualifiedName());

    Node isArrayNode = Node.newExpr("goog.isArray(obj)");
    assertTrue(convention.isPropertyTestFunction(isArrayNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isArrayNode.getType().getReturnType());
    assertEquals("isArray", isArrayNode.getFirstChild().getQualifiedName());

    Node isObjectNode = Node.newExpr("goog.isObject(obj)");
    assertTrue(convention.isPropertyTestFunction(isObjectNode));
    assertEquals(FunctionType.BOOLEAN_TYPE, isObjectNode.getType().getReturnType());
    assertEquals("isObject", isObjectNode.getFirstChild().getQualifiedName());
  }

  @Test
  public void testObjectLiteralCast() {
    Node node = Node.newExpr("goog.reflect.object.cast(obj, 'Type')");
    ObjectLiteralCast cast = new ClosureCodingConvention().getObjectLiteralCast(null, node);
    assertEquals("Type", cast.getTypeName());
    assertEquals("obj", cast.getObjectExpression());
  }
}