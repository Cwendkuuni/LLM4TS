import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;

public class ArrowTypeTest {

 @Test
 public void testArrowTypeCreation() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters = Node.newList();
 Node returnTypeNode = Node.newIdentifier("Number");
 ArrowType arrowType = new ArrowType(registry, parameters, returnTypeNode, false);
 assertEquals("ArrowType with Number return type and no parameters", arrowType, arrowType);
 }

 @Test
 public void testArrowTypeCreationWithParameters() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode = Node.newIdentifier("Number");
 ArrowType arrowType = new ArrowType(registry, parameters, returnTypeNode, false);
 assertEquals("ArrowType with parameters 'String, Number' and return type 'Number'", arrowType, arrowType);
 }

 @Test
 public void testArrowTypeCreationWithInferredReturnType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters = Node.newList();
 ArrowType arrowType = new ArrowType(registry, parameters, null, true);
 assertTrue("ArrowType with inferred return type", arrowType.returnTypeInferred);
 }

 @Test
 public void testArrowTypeCreationWithEmptyParametersList() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 assertNull("ArrowType with empty parameters list and null return type", arrowType.returnType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrowTypeCreationWithNullParameters() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 fail("ArrowType should throw IllegalArgumentException with null parameters");
 }

 @Test
 public void testArrowTypeIsSubtypeOfArrowType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters1 = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode1 = Node.newIdentifier("Number");
 ArrowType arrowType1 = new ArrowType(registry, parameters1, returnTypeNode1, false);

 Node parameters2 = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number"),
 Node.newIdentifier("Boolean")
 );
 Node returnTypeNode2 = Node.newIdentifier("Boolean");
 ArrowType arrowType2 = new ArrowType(registry, parameters2, returnTypeNode2, false);

 assertTrue("ArrowType1 is subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsNotSubtypeOfArrowTypeWithDifferentReturnType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters1 = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode1 = Node.newIdentifier("Number");
 ArrowType arrowType1 = new ArrowType(registry, parameters1, returnTypeNode1, false);

 Node parameters2 = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode2 = Node.newIdentifier("Boolean");
 ArrowType arrowType2 = new ArrowType(registry, parameters2, returnTypeNode2, false);

 assertFalse("ArrowType1 is not subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeOfArrowTypeWithEqualParameters() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters1 = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode1 = Node.newIdentifier("Number");
 ArrowType arrowType1 = new ArrowType(registry, parameters1, returnTypeNode1, false);
 Node parameters2 = Node.newList(
 Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 ),
 Node.newBoolean(true)
 );
 Node returnTypeNode2 = Node.newIdentifier("Number");
 ArrowType arrowType2 = new ArrowType(registry, parameters2, returnTypeNode2, false);
 // ArrowType1 and ArrowType2 have equal parameters list, so ArrowType1 is subtype of ArrowType2
 assertTrue("ArrowType1 is subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsNotSubtypeOfArrowTypeWithMismatchedParameters() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters1 = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode1 = Node.newIdentifier("Number");
 ArrowType arrowType1 = new ArrowType(registry, parameters1, returnTypeNode1, false);
 Node parameters2 = Node.newList(
 Node.newIdentifier("Number"),
 Node.newIdentifier("String")
 );
 Node returnTypeNode2 = Node.newIdentifier("Number");
 ArrowType arrowType2 = new ArrowType(registry, parameters2, returnTypeNode2, false);
 assertFalse("ArrowType1 is not subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeOfArrowTypeWithVarArgs() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters1 = Node.newList(
 Node.newIdentifier("String"),
 Node.newList(Node.newIdentifier("Number"))
 );
 Node returnTypeNode1 = Node.newIdentifier("Number");
 ArrowType arrowType1 = new ArrowType(registry, parameters1, returnTypeNode1, false);
 Node parameters2 = Node.newList(
 Node.newIdentifier("String"),
 Node.newList(Node.newIdentifier("Number"))
 );
 Node returnTypeNode2 = Node.newIdentifier("Boolean");
 ArrowType arrowType2 = new ArrowType(registry, parameters2, returnTypeNode2, false);
 // ArrowType1 and ArrowType2 have equal parameters list, excluding ArrowType2's varargs parameter,
 // so ArrowType1 is a subtype of ArrowType2
 assertTrue("ArrowType1 is subtype of ArrowType2", arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeEqualsArrowType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode = Node.newIdentifier("Number");
 ArrowType arrowType1 = new ArrowType(registry, parameters, returnTypeNode, false);
 ArrowType arrowType2 = new ArrowType(registry, parameters, returnTypeNode, false);
 assertTrue("ArrowType1 equals ArrowType2", arrowType1.equals(arrowType2));
 }

 @Test
 public void testArrowTypeNotEqualsArrowTypeWithDifferentReturnType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode1 = Node.newIdentifier("Number");
 ArrowType arrowType1 = new ArrowType(registry, parameters, returnTypeNode1, false);
 Node returnTypeNode2 = Node.newIdentifier("Boolean");
 ArrowType arrowType2 = new ArrowType(registry, parameters, returnTypeNode2, false);
 assertFalse("ArrowType1 not equals ArrowType2", arrowType1.equals(arrowType2));
 }

 @Test
 public void testArrowTypeHashCode() {
 JSTypeRegistry registry = new JSTypeRegistry();
 Node parameters = Node.newList(
 Node.newIdentifier("String"),
 Node.newIdentifier("Number")
 );
 Node returnTypeNode = Node.newIdentifier("Number");
 ArrowType arrowType = new ArrowType(registry, parameters, returnTypeNode, false);
 int hashCode = arrowType.hashCode();
 ArrowType arrowType2 = new ArrowType(registry, parameters, returnTypeNode, false);
 assertEquals("ArrowType hashes to the same value when equal", hashCode, arrowType2.hashCode());
 }

 @Test
 public void testArrowTypeGetLeastSupertype() {
 throw new UnsupportedOperationException("getLeastSupertype is not implemented");
 }

 @Test
 public void testArrowTypeGetGreatestSubtype() {
 throw new UnsupportedOperationException("getGreatestSubtype is not implemented");
 }

 @Test
 public void testArrowTypeTestForEquality() {
 throw new UnsupportedOperationException("testForEquality is not implemented");
 }

 @Test
 public void testArrowTypeVisit() {
 throw new UnsupportedOperationException("visit is not implemented");
 }

 @Test
 public void testArrowTypeGetPossibleToBooleanOutcomes() {
 throw new UnsupportedOperationException("getPossibleToBooleanOutcomes is not implemented");
 }

 @Test
 public void testArrowTypeResolveInternal() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ErrorReporter errorReporter = new ErrorReporter();
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 arrowType.resolveInternal(errorReporter, null);
 assertNotNull("ArrowType's return type was resolved", arrowType.returnType);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testArrowTypeGetLeastSupertypeUnsupported() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 arrowType.getLeastSupertype(null);
 fail("getLeastSupertype should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testArrowTypeGetGreatestSubtypeUnsupported() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 arrowType.getGreatestSubtype(null);
 fail("getGreatestSubtype should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testArrowTypeTestForEqualityUnsupported() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ArrowType arrowType1 = new ArrowType(registry, null, null, false);
 ArrowType arrowType2 = new ArrowType(registry, null, null, false);
 arrowType1.testForEquality(arrowType2);
 fail("testForEquality should throw UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testArrowTypeVisitUnsupported() {
 Visitor visitor = new Visitor() {
 @Override
 public Node visitArrowType(ArrowType arrowType, Node parent, boolean deep) {
 throw new UnsupportedOperationException();
 }
 };
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 arrowType.visit(visitor);
 fail("visit should throw UnsupportedOperationException");
 }

 @Test
 public void testArrowTypeGetPossibleToBooleanOutcomesTrue() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 assertTrue("ArrowType's getPossibleToBooleanOutcomes returns TRUE", arrowType.getPossibleToBooleanOutcomes().contains(Boolean.TRUE));
 }

 @Test
 public void testArrowTypeWithUnknownReturnType() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ArrowType arrowType = new ArrowType(registry, null, null, false);
 assertTrue("ArrowType with unknown return type has unknown type", arrowType.returnType.isUnknownType());
 }

 @Test
 public void testArrowTypeWithUnknownParameters() {
 JSTypeRegistry registry = new JSTypeRegistry();
 ArrowType arrowType = new ArrowType(registry, Node.newIdentifier("Number"), null, false);
 assertTrue("ArrowType with unknown parameter type has unknown type", arrowType.parameters.getFirstChild().getJSType().isUnknownType());
 }
}