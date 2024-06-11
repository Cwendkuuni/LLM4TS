import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;

/**
 * Tests for {@link ArrowType} class.
 */
public class ArrowTypeTest {

  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
  }

  @Test
  public void testArrowTypeCreation() {
    Node parameters = Node.newList();
    JSType type1 = registry.createParameterType("number");
    Node param1 = Node.newName("p1");
    param1.setJSType(type1);
    parameters.addChild(param1);

    Node param2 = Node.newName("p2");
    JSType type2 = registry.createParameterType("string");
    param2.setJSType(type2);
    parameters.addChild(param2);

    Node varArgs = Node.newVarArg();
    parameters.addChild(varArgs);

    JSTypeArrow arrowType = new JSTypeArrow(registry, parameters, UNKNOWN_TYPE);

    assertEquals("FunctionType with var args", arrowType.getClass().getName(), ArrowType.class.getName());
    assertNotNull(arrowType.parameters);
    assertEquals(3, arrowType.parameters.getChildCount());
    assertEquals("The first parameter should be of type number", type1, arrowType.parameters.getChild(0).getJSType());
    assertEquals("The second parameter should be of type string", type2, arrowType.parameters.getChild(1).getJSType());
    assertEquals("The last parameter should be of type [?]", registry.createParametersWithVarArgs(UNKNOWN_TYPE), arrowType.parameters.getChild(2));
  }

  @Test
  public void testArrowTypeIsSubtype() {
    Node parameters1 = Node.newList();
    Node param1 = Node.newName("p1");
    param1.setJSType(INT);
    parameters1.addChild(param1);

    JSTypeArrow arrowType1 = new JSTypeArrow(registry, parameters1, INT);

    Node parameters2 = Node.newList();
    Node param2 = Node.newName("p1");
    param2.setJSType(STRING);
    parameters2.addChild(param2);

    JSTypeArrow arrowType2 = new JSTypeArrow(registry, parameters2, INT);

    Node parameters3 = Node.newList();
    Node param3 = Node.newName("p1");
    param3.setJSType(INT);
    parameters3.addChild(param3);
    Node varArgs3 = Node.newVarArg();
    parameters3.addChild(varArgs3);

    JSTypeArrow arrowType3 = new JSTypeArrow(registry, parameters3, VOID);

    // arrowType1 should be a subtype of arrowType3 because INT is a subtype of VOID
    assertTrue("arrowType1 should be a subtype of arrowType3", arrowType1.isSubtype(arrowType3));

    // arrowType1 should not be a subtype of arrowType2 because INT is not a subtype of STRING
    assertFalse("arrowType1 should not be a subtype of arrowType2", arrowType1.isSubtype(arrowType2));

    // arrowType2 should not be a subtype of arrowType1 because STRING is not a subtype of INT
    assertFalse("arrowType2 should not be a subtype of arrowType1", arrowType2.isSubtype(arrowType1));

    // arrowType1 should be a subtype of arrowType1 (reflexive property)
    assertTrue("arrowType1 should be a subtype of arrowType1", arrowType1.isSubtype(arrowType1));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    Node parameters1 = Node.newList();
    Node param1 = Node.newName("p1");
    param1.setJSType(INT);
    parameters1.addChild(param1);

    Node parameters2 = Node.newList();
    Node param2 = Node.newName("p1");
    param2.setJSType(INT);
    parameters2.addChild(param2);

    Node varArgs = Node.newVarArg();
    parameters2.addChild(varArgs);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, INT);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, INT);

    // arrowType1 and arrowType2 should have equal parameters
    assertTrue("arrowType1 and arrowType2 should have equal parameters", arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeEquivalence() {
    Node parameters1 = Node.newList();
    Node param1 = Node.newName("p1");
    param1.setJSType(INT);
    parameters1.addChild(param1);

    Node parameters2 = Node.newList();
    Node param2 = Node.newName("p1");
    param2.setJSType(INT);
    parameters2.addChild(param2);

    Node varArgs = Node.newVarArg();
    parameters2.addChild(varArgs);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, INT);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, INT);

    // arrowType1 and arrowType2 should be equivalent
    assertEquals("arrowType1 should be equivalent to arrowType2", arrowType1, arrowType2);
  }

  @Test
  public void testArrowTypeHashCode() {
    Node parameters1 = Node.newList();
    Node param1 = Node.newName("p1");
    param1.setJSType(INT);
    parameters1.addChild(param1);

    Node parameters2 = Node.newList();
    Node param2 = Node.newName("p1");
    param2.setJSType(INT);
    parameters2.addChild(param2);

    Node varArgs = Node.newVarArg();
    parameters2.addChild(varArgs);

    ArrowType arrowType1 = new ArrowType(registry, parameters1, INT);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, INT);

    // hashCode should be equal if the ArrowTypes are equivalent
    int hashCode1 = arrowType1.hashCode();
    int hashCode2 = arrowType2.hashCode();
    assertEquals("hashCode for arrowType1 should be equal to hashCode for arrowType2", hashCode1, hashCode2);
  }

  @Test
  public void testArrowTypeLeastSupertype() {
    throw new UnsupportedOperationException("ArrowType does not implement getLeastSupertype()");
  }

  @Test
  public void testArrowTypeGreatestSubtype() {
    throw new UnsupportedOperationException("ArrowType does not implement getGreatestSubtype()");
  }

  @Test
  public void testArrowTypeToString() {
    Node parameters = Node.newList();
    Node param1 = Node.newName("p1");
    param1.setJSType(INT);
    parameters.addChild(param1);

    Node param2 = Node.newName("p2");
    param2.setJSType(STRING);
    parameters.addChild(param2);

    JSTypeArrow arrowType = new JSTypeArrow(registry, parameters, INT);

    assertEquals("toString should return FunctionType(number, string)", "FunctionType(number, string)", arrowType.toString());
  }
}