import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import junit.framework.TestCase;

public class ArrowTypeTest extends TestCase {

  private static ErrorReporter t = new ErrorReporter() {
    @Override
    public void fatal(String message) {
      fail(message);
    }
  };

  private static JSTypeRegistry registry = new JSTypeRegistry();

  public void testArrowTypeCreation() {
    Node parameters = Node.objectLiteral();
    JSType returnType = registry.getBooleanType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals("Expected ArrowType instance", arrowType.getClass(), arrowType.getClass());
  }

  public void testIsSubtype_SameArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral();
    Node parameters2 = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    assertTrue("Expected true", arrowType1.isSubtype(arrowType2));
  }

  public void testIsSubtype_DifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  public void testIsSubtype_DifferentParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral(Node.stringLiteral("string"));
    Node parameters2 = Node.objectLiteral(Node.numberLiteral(1));
    JSType returnType = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  public void testIsSubtype_MissingParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral(Node.stringLiteral("string"), Node.numberLiteral(1));
    Node parameters2 = Node.objectLiteral(Node.numberLiteral(1));
    JSType returnType = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    assertFalse("Expected false", arrowType1.isSubtype(arrowType2));
  }

  public void testIsSubtype_SameParameterTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral(Node.stringLiteral("string"));
    Node parameters2 = Node.objectLiteral(Node.stringLiteral("string"));
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    assertTrue("Expected true", arrowType1.isSubtype(arrowType2));
  }

  public void testIsEquivalentTo_SameArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral();
    Node parameters2 = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    assertTrue("Expected true", arrowType1.isEquivalentTo(arrowType2));
  }

  public void testIsEquivalentTo_DifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    assertFalse("Expected false", arrowType1.isEquivalentTo(arrowType2));
  }

  public void testIsEquivalentTo_DifferentParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral(Node.stringLiteral("string"));
    Node parameters2 = Node.objectLiteral(Node.numberLiteral(1));
    JSType returnType = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);
    assertFalse("Expected false", arrowType1.isEquivalentTo(arrowType2));
  }

  public void testHashCode_SameArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral();
    Node parameters2 = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    assertEquals("Expected equal hashcodes", hashCode1, hashCode2);
  }

  public void testHashCode_DifferentArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    assertNotSame("Expected different hashcodes", hashCode1, hashCode2);
  }

  public void testGetLeastSupertype_SameArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral();
    Node parameters2 = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    ArrowType leastSupertype = (ArrowType) arrowType1.getLeastSupertype(arrowType2);
    assertEquals("Expected unchanged arrowType1", arrowType1, arrowType1);
    assertEquals("Expected unchanged arrowType2", arrowType2, arrowType2);
    assertEquals("Expected equal leastSupertype", arrowType1, leastSupertype);
  }

  public void testGetGreatestSubtype_SameArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters1 = Node.objectLiteral();
    Node parameters2 = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getBooleanType();
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);
    ArrowType greatestSubtype = (ArrowType) arrowType1.getGreatestSubtype(arrowType2);
    assertEquals("Expected unchanged arrowType1", arrowType1, arrowType1);
    assertEquals("Expected unchanged arrowType2", arrowType2, arrowType2);
    assertEquals("Expected equal greatestSubtype", arrowType2, greatestSubtype);
  }

  public void testGetGreatestSubtype_DifferentArrowTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.objectLiteral();
    JSType returnType1 = registry.getBooleanType();
    JSType returnType2 = registry.getNumberType();
    ArrowType arrowType1 = new ArrowType(registry, parameters, returnType1);
    ArrowType arrowType2 = new ArrowType(registry, parameters, returnType2);
    ArrowType greatestSubtype = (ArrowType) arrowType1.getGreatestSubtype(arrowType2);
    assertEquals("Expected unchanged arrowType1", arrowType1, arrowType1);
    assertEquals("Expected unchanged arrowType2", arrowType2, arrowType2);
    assertNull("Expected null greatestSubtype");
  }

  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.objectLiteral(Node.stringLiteral("string"));
    JSType returnType = registry.getBooleanType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals("Expected proper representation of ArrowType",
        "ArrowType{parameters=Parameters{type=ObjectLiteral, named=false, elements=[Parameter{name=null, varargs=false, type=String}], parametersSize=1}, returnType=Boolean}", arrowType.toString());
  }

  public void testResolve() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.objectLiteral();
    JSType returnType = registry.getBooleanType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    StaticScope scope = new StaticScope<JSType>(registry);
    JSType resolvedArrowType = arrowType.resolve(t, scope);
    assertTrue("Expected ArrowType instance", arrowType.getClass().isInstance(resolvedArrowType));
  }

  public void testHasUnknownParamsOrReturn() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.objectLiteral(Node.stringLiteral("string"), Node.numberLiteral(1));
    JSType returnType = registry.getBooleanType();
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertTrue("Expected false", arrowType.hasUnknownParamsOrReturn());

    JSTypeRegistry registry2 = new JSTypeRegistry();
    Node parameters2 = Node.objectLiteral(Node.stringLiteral("string"), null);
    JSType returnType2 = registry2.getBooleanType();
    ArrowType arrowType2 = new ArrowType(registry2, parameters2, returnType2);
    assertTrue("Expected true", arrowType2.hasUnknownParamsOrReturn());

    JSTypeRegistry registry3 = new JSTypeRegistry();
    Node parameters3 = Node.objectLiteral(Node.stringLiteral("string"), Node.numberLiteral(1), Node.numberLiteral(2));
    JSType returnType3 = registry3.getBooleanType();
    ArrowType arrowType3 = new ArrowType(registry3, parameters3, returnType3);
    assertFalse("Expected false", arrowType3.hasUnknownParamsOrReturn());

    JSTypeRegistry registry4 = new JSTypeRegistry();
    Node parameters4 = Node.objectLiteral(Node.stringLiteral("string"), null, Node.numberLiteral(2));
    JSType returnType4 = registry4.getBooleanType();
    ArrowType arrowType4 = new ArrowType(registry4, parameters4, returnType4);
    assertTrue("Expected true", arrowType4.hasUnknownParamsOrReturn());

    JSTypeRegistry registry5 = new JSTypeRegistry();
    Node parameters5 = Node.objectLiteral(Node.stringLiteral("string"), Node.numberLiteral(1), Node.numberLiteral(2), null);
    JSType returnType5 = registry5.getBooleanType();
    ArrowType arrowType5 = new ArrowType(registry5, parameters5, returnType5);
    assertTrue("Expected true", arrowType5.hasUnknownParamsOrReturn());

    JSTypeRegistry registry6 = new JSTypeRegistry();
    Node parameters6 = Node.objectLiteral(Node.stringLiteral("string"), Node.numberLiteral(1), Node.numberLiteral(2), null, Node.numberLiteral(3));
    JSType returnType6 = registry6.getBooleanType();
    ArrowType arrowType6 = new ArrowType(registry6, parameters6, returnType6);
    assertFalse("Expected false", arrowType6.hasUnknownParamsOrReturn());
  }
}