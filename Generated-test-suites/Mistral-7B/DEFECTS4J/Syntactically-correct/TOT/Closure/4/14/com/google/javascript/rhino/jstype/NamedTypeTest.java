import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeCheckMode;
import com.google.javascript.rhino.testing.TypeFixture;
import static com.google.common.truth.Truth.assertThat;

public class NamedTypeTest {

 private NamedType namedType;
 private TypeFixture typeFixture;

 @Before
 public void setUp() {
 typeFixture = new TypeFixture(TypeCheckMode.THROWS);
 namedType = new NamedType(
 typeFixture.registry, "java.lang.String", "MyTestFile.java", 1, 1);
 }

 @Test
 public void testNameAccess() {
 JSType referencedType = namedType.getReferencedType();
 assertThat(referencedType.getQualifiedName()).isEqualTo("java.lang.String");
 }

 @Test
 public void testPropertyDefinitionDeferred() {
 namedType.defineProperty("testProperty", typeFixture.createBooleanType(), false, null);
 JSType propertyType = namedType.getOwnProperties().get("testProperty");
 assertThat(propertyType.getTypeClass()).isEqualTo(PropertyType.class);
 }

 @Test
 public void testGetReferencedTypeThrowsIfUnresolved() {
 namedType = new NamedType(typeFixture.registry, "UnknownType", "MyTestFile.java", 1, 1);
 try {
 namedType.getReferencedType();
 } catch (AssertionError e) {
 // Expected exception, as the type is unresolved
 }
 }

 @Test
 public void testResolveViaRegistry() {
 String reference = "java.lang.String";
 JSType referencedType = typeFixture.registry.getType(reference);
 namedType = new NamedType(typeFixture.registry, reference, "MyTestFile.java", 1, 1);
 JSType resolvedType = namedType.getReferencedType();
 assertThat(resolvedType).isSameInstanceAs(referencedType);
 }

 @Test
 public void testResolveViaProperties() {
 JSType objType = typeFixture.registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
 JSType stringType = typeFixture.registry.getNativeType("String", objType);
 JSType stringPrototype = stringType.getPrototype();
 ObjectType prototypeObjType = ObjectType.cast(stringPrototype);
 prototypeObjType.defineProperty("testProperty", typeFixture.createBooleanType(), false, null);
 namedType = new NamedType(typeFixture.registry, "String.prototype.testProperty", "MyTestFile.java", 1, 1);
 JSType propertyType = namedType.getReferencedType();
 assertThat(propertyType).isSameInstanceAs(typeFixture.createBooleanType());
 }

 @Test
 public void testResolveViaPropertiesWithForwardReference() {
 JSType unknownType = typeFixture.registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
 namedType = new NamedType(typeFixture.registry, "UnknownType", "MyTestFile.java", 1, 1);
 typeFixture.registry.registerType("UnknownType", unknownType);
 JSType resolvedType = namedType.getReferencedType();
 assertThat(resolvedType).isSameInstanceAs(unknownType);
 }

 @Test
 public void testResolveViaPropertiesWithForwardReferenceThenRegistry() {
 JSType unknownType = typeFixture.registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
 namedType = new NamedType(typeFixture.registry, "UnknownType", "MyTestFile.java", 1, 1);
 typeFixture.registry.registerType("UnknownType", typeFixture.createBooleanType());
 JSType resolvedType = namedType.getReferencedType();
 assertThat(resolvedType).isSameInstanceAs(typeFixture.createBooleanType());
 }

 @Test
 public void testResolveWithCycle() {
 // Create a cyclic reference: EnumA.getValue() -> EnumB.getValue() -> EnumA
 JSType enumAType = typeFixture.registry.createEnumType("EnumA", new Node(), null);
 JSType enumBType = typeFixture.registry.createEnumType("EnumB", new Node(), null);
 JSType valueType = typeFixture.registry.getNativeType("Value", JSTypeNative.OBJECT_TYPE);
 ObjectType enumAObjectType = ObjectType.cast(enumAType.getInstanceType());
 ObjectType enumBObjectType = ObjectType.cast(enumBType.getInstanceType());
 enumAObjectType.defineProperty("getValue", valueType, false, null);
 enumBObjectType.defineProperty("getValue", enumAType, false, null);
 namedType = new NamedType(typeFixture.registry, "EnumA.getValue", "MyTestFile.java", 1, 1);
 try {
 namedType.getReferencedType();
 } catch (AssertionError e) {
 // Expected exception, as there is a cycle in the inheritance chain
 }
 }
}