import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TestValues;
import com.google.javascript.rhino.jstype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

 private static final String NAMED_TYPE_REFERENCE = "MyNamedType";
 private static final String NAMED_TYPE_ALIAS = "MyNamedTypeAlias";
 private static final String ENUM_NAME = "MyEnum";
 private static final String ENUM_CONSTANT = "MyEnumConstant";
 private static final String NAMED_TYPE_WITH_ENUM = "MyTypeWithEnum";
 private static final String NAMED_TYPE_WITH_FUNCTION = "MyTypeWithFunction";
 private static final String NAMED_TYPE_CYCLE = "MyTypeCycle";
 private static final int LINE_NUMBER = 1;
 private static final int CHAR_NUMBER = 1;

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 errorReporter = new ErrorReporter() {};
 }

 @Test
 public void testNamedTypeReference() {
 // Create a named type reference
 JSType namedType = new NamedType(registry, NAMED_TYPE_REFERENCE, null, LINE_NUMBER, CHAR_NUMBER);

 // Validate the type reference
 registry.setValidator(new Predicate<JSType>() {
 @Override
 public boolean apply(JSType input) {
 return input instanceof ObjectType;
 }
 });
 assertTrue(namedType.isValid(errorReporter));

 // Create a type that the named type references
 ObjectType referencedType = new ObjectType(registry, getClass(), LINE_NUMBER, CHAR_NUMBER);

 // Set the referenced type for the named type
 namedType.setReferencedType(referencedType);

 // Validate the named type after setting the referenced type
 registry.setValidator(new Predicate<JSType>() {
 @Override
 public boolean apply(JSType input) {
 return input.isNamedType();
 }
 });
 assertTrue(namedType.isValid(errorReporter));
 }

 @Test
 public void testNamedTypeReferenceWithEnum() {
 // Create a named type reference with an enum type
 JSType enumType = new EnumType(registry, ENUM_NAME, LINE_NUMBER, CHAR_NUMBER);
 JSType enumElementType = new EnumElementType(
 registry, ENUM_NAME, ENUM_CONSTANT, LINE_NUMBER, CHAR_NUMBER);
 enumType.setElementsType(enumElementType);
 JSType namedType = new NamedType(
 registry, NAMED_TYPE_WITH_ENUM, null, LINE_NUMBER, CHAR_NUMBER);
 registry.setValidator(new Predicate<JSType>() {
 @Override
 public boolean apply(JSType input) {
 return input.isNamedType() &&
 ((NamedType) input).getReferencedType() == enumType;
 }
 });
 assertTrue(namedType.isValid(errorReporter));
 }

 @Test
 public void testNamedTypeReferenceWithFunction() {
 // Create a named type reference with a function type
 FunctionType functionType = new FunctionType(
 registry, ImmutableList.of(new JSType[] { JSTypeNative.VOID_TYPE }),
 JSTypeNative.VOID_TYPE, LINE_NUMBER, CHAR_NUMBER);
 JSType namedType = new NamedType(
 registry, NAMED_TYPE_WITH_FUNCTION, null, LINE_NUMBER, CHAR_NUMBER);
 registry.setValidator(new Predicate<JSType>() {
 @Override
 public boolean apply(JSType input) {
 return input.isNamedType() &&
 ((NamedType) input).getReferencedType() == functionType;
 }
 });
 assertTrue(namedType.isValid(errorReporter));
 }

 @Test
 public void testNamedTypeCycle() {
 // Create a named type cycle
 JSType cycleType1 = new NamedType(
 registry, NAMED_TYPE_CYCLE, null, LINE_NUMBER, CHAR_NUMBER);
 JSType cycleType2 = new NamedType(
 registry, NAMED_TYPE_CYCLE, null, LINE_NUMBER, CHAR_NUMBER);
 cycleType1.setReferencedType(cycleType2);
 cycleType2.setReferencedType(cycleType1);

 // Validate the types and check for error reports
 registry.setValidator(new Predicate<JSType>() {
 @Override
 public boolean apply(JSType input) {
 return input instanceof ErrorReporter.Error;
 }
 });
 List<Error> errors = registry.getErrors();
 assertEquals(1, errors.size());
 assertEquals("Cycle detected in inheritance chain of type " + NAMED_TYPE_CYCLE,
 errors.get(0).getMessage());
 }

 @Test
 public void testNamedTypeResolverViaRegistry() {
 // Test resolving a named type via the registry
 registry.registerType(NAMED_TYPE_REFERENCE, new ObjectType(
 registry, getClass(), LINE_NUMBER, CHAR_NUMBER));
 JSType namedType = new NamedType(
 registry, NAMED_TYPE_REFERENCE, null, LINE_NUMBER, CHAR_NUMBER);
 registry.setValidator(new Predicate<JSType>() {
 @Override
 public boolean apply(JSType input) {
 return input instanceof ObjectType;
 }
 });
 assertTrue(namedType.isValid(errorReporter));
 }

 @Test
 public void testNamedTypeResolverViaProperties() {
 // Test resolving a named type via properties
 ObjectType parentType = new ObjectType(
 registry, getClass(), LINE_NUMBER, CHAR_NUMBER);
 parentType.setProperty("namedType", new ObjectType(
 registry, getClass(), LINE_NUMBER + 1, CHAR_NUMBER + 1));
 JSType namedType = new NamedType(
 registry, "namedType", null, LINE_NUMBER, CHAR_NUMBER);
 registry.setValidator(new Predicate<JSType>() {
 @Override
 public boolean apply(JSType input) {
 return input instanceof ObjectType;
 }
 });
 assertTrue(namedType.isValid(errorReporter));
 }

 @Test
 public void testNamedTypePropertyDefinition() {
 // Test defining a property on a named type
 JSType namedType = new NamedType(
 registry, NAMED_TYPE_REFERENCE, null, LINE_NUMBER, CHAR_NUMBER);
 ObjectType parentType = new ObjectType(
 registry, getClass(), LINE_NUMBER, CHAR_NUMBER);
 parentType.setProperty("property", new JSTypeNative.StringType());
 namedType.setReferencedType(parentType);

 // Define the property on the named type
 namedType.defineProperty("property", JSTypeNative.STRING_TYPE, false, null);

 // Validate the property and check its type
 registry.setValidator(new Predicate<JSType>() {
 @Override
 public boolean apply(JSType input) {
 return input.isPropertyType();
 }
 });
 JSType property = ((NamedType) namedType).getPropertyType("property");
 assertEquals(JSTypeNative.STRING_TYPE, property);
 }
}