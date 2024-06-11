import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeNative;

public class ArrowTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;

 @BeforeClass
 public static void setupClass() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter() {};
 }

 @Before
 public void setup() {
 registry.reset();
 errorReporter = new ErrorReporter() {};
 }

 @Test
 public void testArrowTypeCreation() {
 Node parametersNode = new Node(JSTypeNative.OBJECT_TYPE, null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.getNullType());
 assertEquals("ArrowType", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithParameters() {
 Node parametersNode = new Node(
 Node.createList(
 new Node(JSTypeNative.NUMBER_TYPE, null),
 new Node(JSTypeNative.STRING_TYPE, null)
 ), null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.getBooleanType());
 assertEquals("ArrowType[number, string] -> boolean", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithInferredReturnType() {
 Node parametersNode = new Node(new Node(JSTypeNative.OBJECT_TYPE, null), null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);
 assertEquals("ArrowType[?]", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithEmptyParameterList() {
 ArrowType arrowType = new ArrowType(registry, null, JSType.getNullType());
 assertEquals("ArrowType[] -> null", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithVarArgsParameter() {
 Node parametersNode = new Node(
 Node.createList(
 new Node(JSTypeNative.NUMBER_TYPE, null),
 new Node(JSTypeNative.VARARGS_TYPE, null)
 ), null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.getNullType());
 assertEquals("ArrowType[number, ...] -> null", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithOptionalParameter() {
 Node parametersNode = new Node(
 Node.createList(
 new Node(JSTypeNative.NUMBER_TYPE, null),
 new Node(JSTypeNative.OBJECT_TYPE, null),
 new Node(JSTypeNative.VARARGS_TYPE, null)
 ), null);
 Node optionalParamNode = new Node(new Node(JSTypeNative.OBJECT_TYPE, null), new Node(JSTypeNative.FALSE_LITERAL, null));
 parametersNode.setChildAt(1, optionalParamNode);
 ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.getNullType());
 assertEquals("ArrowType[number, Object?, ...] -> null", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithBothOptionalAndVarArgsParameters() {
 Node parametersNode = new Node(
 Node.createList(
 new Node(JSTypeNative.NUMBER_TYPE, null),
 new Node(JSTypeNative.OBJECT_TYPE, null),
 new Node(JSTypeNative.VARARGS_TYPE, null)
 ), null);
 Node optionalParamNode1 = new Node(new Node(JSTypeNative.OBJECT_TYPE, null), new Node(JSTypeNative.FALSE_LITERAL, null));
 Node optionalParamNode2 = new Node(new Node(JSTypeNative.OBJECT_TYPE, null), new Node(JSTypeNative.FALSE_LITERAL, null));
 parametersNode.setChildAt(1, optionalParamNode1);
 parametersNode.setChildAt(2, optionalParamNode2);
 ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.getNullType());
 assertEquals("ArrowType[number, Object?, Object?, ...] -> null", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithEmptyReturnType() {
 ArrowType arrowType = new ArrowType(registry, null, null);
 assertEquals("ArrowType[] -> void", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithNullReturnType() {
 ArrowType arrowType = new ArrowType(registry, null, JSType.getNullType());
 assertEquals("ArrowType[] -> null", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithNamedParameters() {
 Node parametersNode = new Node(
 Node.createList(
 new Node("p", new Node(JSTypeNative.NUMBER_TYPE, null)),
 new Node("q", new Node(JSTypeNative.STRING_TYPE, null))
 ), null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.getBooleanType());
 assertEquals("ArrowType[p: number, q: string] -> boolean", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithNamedAndDefaultParameters() {
 Node parametersNode = new Node(
 Node.createList(
 new Node("p", new Node(JSTypeNative.NUMBER_TYPE, null)),
 new Node("q", new Node(JSTypeNative.STRING_TYPE, null), new Node(JSTypeNative.STRING_LITERAL, "default")),
 new Node(JSTypeNative.VARARGS_TYPE, null)
 ), null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, JSType.getBooleanType());
 assertEquals("ArrowType[p: number, q: string, ...] -> boolean", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithEmptyParametersAndInferredReturnType() {
 ArrowType arrowType = new ArrowType(registry, null, null, true);
 assertEquals("ArrowType[] -> ?", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithInferredParametersAndNullReturnType() {
 Node parametersNode = new Node(JSTypeNative.VARARGS_TYPE, null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);
 assertEquals("ArrowType[...] -> null", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithInferredParametersAndInferredReturnType() {
 Node parametersNode = new Node(JSTypeNative.VARARGS_TYPE, null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);
 assertEquals("ArrowType[...] -> ?", arrowType.getTypeName());
 }

 @Test
 public void testArrowTypeCreationWithNamedInferredParametersAndInferredReturnType() {
 Node parametersNode = new Node(
 Node.createList(
 new Node("p", new Node(JSTypeNative.VARARGS_TYPE, null)),
 new Node("q", new Node(JSTypeNative.VARARGS_TYPE, null))
 ), null);
 ArrowType arrowType = new ArrowType(registry, parametersNode, null, true);
 assertEquals("ArrowType[p: ..., q: ...] -> ?", arrowType.getTypeName());
 }

 @Test
 public void testEquals() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType2 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType3 = new ArrowType(registry, null, JSType.getNumberType());
 ArrowType arrowType4 = new ArrowType(registry, null, JSType.getBooleanType(), false);

 assertTrue(arrowType1.equals(arrowType1));
 assertTrue(arrowType1.equals(arrowType2));
 assertFalse(arrowType1.equals(arrowType3));
 assertFalse(arrowType1.equals(arrowType4));
 }

 @Test
 public void testIsSubtype() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType2 = new ArrowType(registry, null, JSType.getNumberType());
 ArrowType arrowType3 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType4 = new ArrowType(registry, null, JSType.getNumberType(), false);

 assertTrue(arrowType1.isSubtype(arrowType1));
 assertTrue(arrowType1.isSubtype(arrowType3));
 assertFalse(arrowType1.isSubtype(arrowType2));
 assertFalse(arrowType1.isSubtype(arrowType4));
 }

 @Test
 public void testHashcode() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType2 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType3 = new ArrowType(registry, null, JSType.getNumberType());
 ArrowType arrowType4 = new ArrowType(registry, null, JSType.getBooleanType(), false);

 assertEquals(arrowType1.hashCode(), arrowType1.hashCode());
 assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
 assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());
 assertNotEquals(arrowType1.hashCode(), arrowType4.hashCode());
 }

 @Test
 public void testToString() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType2 = new ArrowType(registry, null, JSType.getNumberType());
 ArrowType arrowType3 = new ArrowType(registry, null, JSType.getBooleanType(), false);

 assertEquals("ArrowType", arrowType1.toString());
 assertEquals("ArrowType[number] -> number", arrowType2.toString());
 assertEquals("ArrowType[?]", arrowType3.toString());
 }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType2 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType3 = new ArrowType(registry, null, JSType.getNumberType());
 ArrowType arrowType4 = new ArrowType(registry, null, JSType.getBooleanType());
 ArrowType arrowType5 = new ArrowType(registry, null, JSType.getBooleanType(), false);

 assertTrue(arrowType1.hasEqualParameters(arrowType1, true));
 assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
 assertFalse(arrowType1.hasEqualParameters(arrowType3, true));
 assertFalse(arrowType1.hasEqualParameters(arrowType4, true));
 assertFalse(arrowType1.hasEqualParameters(arrowType5, true));

 assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
 assertTrue(arrowType1.hasEqualParameters(arrowType4, false));
 }
}