import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.Node;
import com.google.javascript.rhino.jstype.UnknownType;

import java.util.Arrays;

public class ArrowTypeTest {

 private static final JSTypeRegistry REGISTRY = new JSTypeRegistry();

 @BeforeClass
 public static void setUp() {
 Registry.init(REGISTRY);
 }

 @Test
 public void testArrowTypeWithNullParametersAndReturnType() {
 ArrowType arrowType = new ArrowType(REGISTRY, null, null);
 assertEquals("[ArrowType]", arrowType.toString());
 assertFalse(arrowType.hasAnyTemplate());
 assertNull(arrowType.returnType);
 assertNull(arrowType.parameters);
 }

 @Test
 public void testArrowTypeWithParametersAndReturnType() {
 Node parametersNode = REGISTRY.createNode(Node.LIST);
 Node param1 = REGISTRY.createNode(Node.NAME, "x");
 Node param2 = REGISTRY.createNode(Node.NAME, "y");
 parametersNode.addChild(param1);
 parametersNode.addChild(param2);
 ArrowType arrowType = new ArrowType(REGISTRY, parametersNode, REGISTRY.getTypeByName("int"), false);
 assertEquals("[ArrowType]", arrowType.toString());
 assertFalse(arrowType.hasAnyTemplate());
 assertEquals("(x: int, y: int)", arrowType.toStringForAnnotations());
 assertEquals(2, arrowType.numberOfParameters());
 assertEquals("int", arrowType.getParameterTypeAt(0).getName());
 assertEquals("int", arrowType.getParameterTypeAt(1).getName());
 assertEquals("int", arrowType.getReturnType().getName());
 }

 @Test
 public void testArrowTypeWithInferredReturnType() {
 Node parametersNode = REGISTRY.createNode(Node.LIST);
 Node param1 = REGISTRY.createNode(Node.NAME, "x");
 Node param2 = REGISTRY.createNode(Node.NAME, "y");
 parametersNode.addChild(param1);
 parametersNode.addChild(param2);
 ArrowType arrowType = new ArrowType(REGISTRY, parametersNode, null, true);
 assertEquals("[ArrowType]", arrowType.toString());
 assertFalse(arrowType.hasAnyTemplate());
 assertEquals("(x: any, y: any)", arrowType.toStringForAnnotations());
 assertEquals(2, arrowType.numberOfParameters());
 assertNull(arrowType.getReturnType());
 }

 @Test
 public void testArrowTypeEquality() {
 ArrowType arrowType1 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("int"), false);
 ArrowType arrowType2 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("int"), false);
 assertTrue(arrowType1.equals(arrowType2));

 ArrowType arrowType3 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("int"), true);
 assertFalse(arrowType1.equals(arrowType3));

 ArrowType arrowType4 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("int"), false);
 Node paramNode = REGISTRY.createNode(Node.LIST);
 paramNode.addChild(REGISTRY.createNode(Node.NAME, "x"));
 ArrowType arrowType5 = new ArrowType(REGISTRY, paramNode, REGISTRY.getTypeByName("int"), false);
 assertFalse(arrowType1.equals(arrowType5));
 }

 @Test
 public void testArrowTypeHashCode() {
 ArrowType arrowType1 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("int"), false);
 ArrowType arrowType2 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("int"), false);
 int hashCode1 = arrowType1.hashCode();
 int hashCode2 = arrowType2.hashCode();
 // Changing the return type should change the hashcode
 ArrowType arrowType3 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("float"), false);
 assertNotEquals(hashCode1, arrowType3.hashCode());

 // Changing the parameters should change the hashcode
 ArrowType arrowType4 = new ArrowType(REGISTRY, REGISTRY.createNode(Node.LIST), REGISTRY.getTypeByName("int"), false);
 assertNotEquals(hashCode1, arrowType4.hashCode());
 }

 @Test
 public void testArrowTypeSubtyping() {
 ArrowType arrowType1 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("int"), false);
 ArrowType arrowType2 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("float"), false);
 ArrowType arrowType3 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("Object"), false);
 ArrowType arrowType4 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("int"), true);
 ArrowType arrowType5 = new ArrowType(REGISTRY, null, REGISTRY.getTypeByName("Object"), true);
 ArrowType arrowType6 = new ArrowType(REGISTRY, null, UnknownType.INSTANCE, false);
 ArrowType arrowType7 = new ArrowType(REGISTRY, null, null, false);

 assertTrue(arrowType1.isSubtype(arrowType1));
 assertFalse(arrowType1.isSubtype(arrowType2));
 assertFalse(arrowType1.isSubtype(arrowType3));
 assertFalse(arrowType1.isSubtype(arrowType4));
 assertFalse(arrowType1.isSubtype(arrowType5));
 assertFalse(arrowType1.isSubtype(arrowType6));
 assertFalse(arrowType1.isSubtype(arrowType7));

 assertTrue(arrowType2.isSubtype(arrowType4)); // Widening int to Object
 assertFalse(arrowType2.isSubtype(arrowType1));
 assertFalse(arrowType3.isSubtype(arrowType1));
 assertFalse(arrowType3.isSubtype(arrowType4));
 assertFalse(arrowType4.isSubtype(arrowType2));
 assertFalse(arrowType4.isSubtype(arrowType3));
 assertFalse(arrowType5.isSubtype(arrowType1));
 assertFalse(arrowType5.isSubtype(arrowType4));
 assertFalse(arrowType6.isSubtype(arrowType1));
 assertFalse(arrowType6.isSubtype(arrowType4));
 assertFalse(arrowType7.isSubtype(arrowType1));
 assertFalse(arrowType7.isSubtype(arrowType4));
 }

 @Test
 public void testArrowTypeWithTemplateParameters() {
 ArrowType arrowType = new ArrowType(REGISTRY, null, null);
 Node paramList = REGISTRY.createNode(Node.LIST);
 Node param1 = REGISTRY.createNode(Node.NAME, "T");
 Node param2 = REGISTRY.createNode(Node.NAME, "U");
 paramList.addChild(param1);
 paramList.addChild(param2);
 arrowType.setParameters(paramList);
 assertEquals("[ArrowType<T, U>]", arrowType.toStringForAnnotations());
 }

 @Test
 public void testArrowTypeWithTemplateParametersAndReturnType() {
 ArrowType arrowType = new ArrowType(REGISTRY, null, null);
 Node paramList = REGISTRY.createNode(Node.LIST);
 Node param1 = REGISTRY.createNode(Node.NAME, "T");
 Node param2 = REGISTRY.createNode(Node.NAME, "U");
 paramList.addChild(param1);
 paramList.addChild(param2);
 arrowType.setParameters(paramList);
 arrowType.setReturnType(REGISTRY.getTypeForName("Function<[T], U>"));
 assertEquals("[Function<[T], U>]", arrowType.toStringForAnnotations());
 }

 @Test
 public void testArrowTypeWithTemplateParametersAndInferredReturnType() {
 ArrowType arrowType = new ArrowType(REGISTRY, null, null);
 Node paramList = REGISTRY.createNode(Node.LIST);
 Node param1 = REGISTRY.createNode(Node.NAME, "T");
 Node param2 = REGISTRY.createNode(Node.NAME, "U");
 paramList.addChild(param1);
 paramList.addChild(param2);
 arrowType.setParameters(paramList);
 arrowType.setReturnTypeInferred(true);
 assertEquals("[Function<[T], U>]", arrowType.toStringForAnnotations());
 }

 @Test
 public void testArrowTypeWithTemplateParametersAndVarArgs() {
 ArrowType arrowType = new ArrowType(REGISTRY, null, null);
 Node paramList = REGISTRY.createNode(Node.LIST);
 Node param1 = REGISTRY.createNode(Node.NAME, "T");
 Node param2 = REGISTRY.createNode(Node.LIST);
 Node varArgs = REGISTRY.createNode(Node.NAME, "V");
 param2.addChild(param1);
 param2.addChild(varArgs);
 paramList.addChild(param1);
 paramList.addChild(param2);
 arrowType.setParameters(paramList);
 arrowType.setReturnType(REGISTRY.getTypeForName("Function<[T], V[]>"));
 assertEquals("[Function<[T], V[]>]", arrowType.toStringForAnnotations());
 }
}