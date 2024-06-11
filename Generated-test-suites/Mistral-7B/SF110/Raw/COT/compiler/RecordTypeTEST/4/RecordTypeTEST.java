import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordTypeFrozen;
 private RecordType recordTypeWithDifferentProperties;
 private RecordType recordTypeWithDifferentTypes;
 private RecordType recordTypeWithInheritance;
 private RecordType recordTypeWithMissingProperty;
 private RecordType recordTypeWithSubtype;
 private RecordType recordTypeWithUnionType;

 @before
 public void setUp() {
 registry = new JSTypeRegistry();

 Map<String, RecordProperty> properties = new HashMap<>();
 properties.put("name", new RecordProperty("name", registry.getStringType()));
 properties.put("age", new RecordProperty("age", registry.getNumberType()));
 recordType = new RecordType(registry, properties, true);
 recordTypeFrozen = new RecordType(registry, properties, true);
 recordTypeFrozen.freeze();
 recordTypeWithDifferentProperties = new RecordType(registry, properties, false);
 recordTypeWithDifferentTypes = new RecordType(registry, new HashMap<>(), true);
 recordTypeWithInheritance = new RecordType(registry, properties, true);
 recordTypeWithMissingProperty = new RecordType(registry, new HashMap<>(), true);
 recordTypeWithSubtype = new RecordType(registry, new HashMap<>(), true);
 recordTypeWithUnionType = new RecordType(registry, new HashMap<>(), true);
 recordTypeWithSubtype.defineProperty("id", registry.getNumberType(), false, Node.newInt(1));

 RecordType recordTypeBuilder = new RecordTypeBuilder(registry);
 recordTypeWithSubtype.defineProperty("subType", recordTypeWithUnionType, false, Node.newNull());
 recordTypeWithUnionType.setSynthesized(true);
 recordTypeWithUnionType.addProperty("id", registry.getNumberType(), null, Node.newInt(2));
 recordTypeWithUnionType.addProperty("name", registry.getStringType(), null, Node.newString("Bob"));

 RecordType recordTypeWithInheritanceBuilder = new RecordTypeBuilder(registry);
 recordTypeWithInheritanceBuilder.setSynthesized(true);
 recordTypeWithInheritanceBuilder.addSupertype(recordType);
 recordTypeWithInheritanceBuilder.addProperty("newProperty", registry.getStringType(), null, Node.newString("New Property"));
 recordTypeWithInheritance = recordTypeWithInheritanceBuilder.build();
 }

 @Test
 public void testDeclared() {
 assertEquals(true, recordType.isDeclared());
 assertEquals(false, recordTypeFrozen.isDeclared());
 }

 @Test
 public void testIsFrozen() {
 assertEquals(false, recordType.isFrozen());
 assertEquals(true, recordTypeFrozen.isFrozen());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordTypeCopy = new RecordType(registry, properties, true);
 assertTrue(recordType.checkRecordEquivalenceHelper(recordTypeCopy, RecordType.EQUIVALENCE_BY_VALUE));

 RecordType recordTypeDifferentProperties = new RecordType(registry, properties, true);
 recordTypeDifferentProperties.defineProperty("newProperty", registry.getStringType(), false, Node.newString("New Property"));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeDifferentProperties, RecordType.EQUIVALENCE_BY_VALUE));

 RecordType recordTypeDifferentTypes = new RecordType(registry, new HashMap<>(), true);
 recordTypeDifferentTypes.defineProperty("name", registry.getBooleanType(), false, Node.newBoolean(true));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeDifferentTypes, RecordType.EQUIVALENCE_BY_VALUE));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordProperty newProperty = new RecordProperty("newProperty", registry.getStringType());
 recordTypeWithMissingProperty.defineProperty("newProperty", registry.getStringType(), false, Node.newString("New Property"));
 assertTrue(recordTypeWithMissingProperty.hasProperty("newProperty"));
 assertEquals(registry.getStringType(), recordTypeWithMissingProperty.getPropertyType("newProperty"));
 }

 @Test(expected = IllegalStateException.class)
 public void testDefineProperty_frozen() {
 recordTypeFrozen.defineProperty("newProperty", registry.getStringType(), false, Node.newString("New Property"));
 fail("Expected an IllegalStateException");
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(JSType.NULL);
 assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithDifferentTypes);
 assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithInheritance);
 assertEquals(recordTypeWithInheritance, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(recordTypeWithSubtype);
 UnionType unionType = (UnionType) greatestSubtype;
 assertTrue(unionType.containsType(recordTypeWithSubtype));
 assertEquals(2, unionType.getAlternates().size());
 }

 @Test
 public void testIsSubtype() {
 assertTrue(recordType.isSubtype(recordType));
 assertTrue(recordType.isSubtype(recordTypeWithDifferentProperties));

 assertFalse(recordTypeWithDifferentProperties.isSubtype(recordType));
 assertFalse(recordTypeWithDifferentTypes.isSubtype(recordType));

 assertTrue(recordTypeWithInheritance.isSubtype(recordType));
 assertFalse(recordType.isSubtype(recordTypeWithInheritance));

 assertTrue(recordType.isSubtype(recordTypeWithSubtype));
 assertTrue(recordTypeWithSubtype.isSubtype(recordType));
 }

 @Test
 public void testIsSubtype_recordWithUnionType() {
 assertTrue(recordTypeWithUnionType.isSubtype(recordTypeWithSubtype));
 assertTrue(recordTypeWithSubtype.isSubtype(recordTypeWithUnionType));
 }
}