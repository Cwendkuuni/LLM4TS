import static org.junit.Assert.*;
import com.google.javascript.jscomp.ClosureCodingConvention;

public class ClosureCodingConventionTest {
  private final ClosureCodingConvention convention = new ClosureCodingConvention();

  @org.junit.Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", VOID_TYPE);
    FunctionType childCtor = new FunctionType("ChildCtor", VOID_TYPE);
    SubclassType type = SubclassType.INHERITS;
    convention.applySubclassRelationship(parentCtor, childCtor, type);
    assertNotNull(childCtor.getPrototype().getDeclaredProperty("constructor"));
    assertNotNull(childCtor.getPrototype().getDeclaredProperty("superClass_"));
  }

  @org.junit.Test
  public void testExtractClassNameIfProvide() {
    Node node = NodeFactory.createCall(
        NodeFactory.createName("goog.provide"),
        NodeFactory.createArrayLiteral(NodeFactory.createName("X"))
    );
    String className = convention.extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @org.junit.Test
  public void testGetExportPropertyFunction() {
    String functionName = convention.getExportPropertyFunction();
    assertEquals("goog.exportProperty", functionName);
  }

  @org.junit.Test
  public void testApplySingletonGetter() {
    FunctionType functionType = new FunctionType("FunctionType", VOID_TYPE);
    FunctionType getterType = new FunctionType("GetterType", ObjectType.getType("Object"));
    ObjectType objectType = ObjectType.getType("Object");
    convention.applySingletonGetter(functionType, getterType, objectType);
    assertNotNull(functionType.getPrototype().getDeclaredProperty("getInstance"));
    assertNotNull(functionType.getPrototype().getDeclaredProperty("instance_"));
  }
}