import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.common.collect.Maps;
import static org.junit.Assert.*;

public class RecordTypeTest {

 private JSTypeRegistry registry;
 private RecordType recordType;
 private RecordType recordType2;
 private RecordType recordType3;

 @Before
 public void setUp() {
 registry = new JSTypeRegistry();
 RecordProperty recordProperty = new RecordProperty("name", registry.getStringType());
 RecordProperty recordProperty2 = new RecordProperty("age", registry.getNumberType());
 RecordProperty recordProperty3 = new RecordPersonProperty();
 Map<String, RecordProperty> properties = Maps.newHashMap();
 properties.put("name", recordProperty);
 properties.put("age", recordProperty2);
 properties.put("address", recordProperty3);
 recordType = new RecordType(registry, properties);
 recordType2 = new RecordType(registry, properties, false);
 recordType3 = new RecordType(registry, properties, false);
 }

 @Test
 public void testIsSynthetic() {
 assertFalse(recordType.isSynthetic());
 assertTrue(recordType2.isSynthetic());
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 assertTrue(recordType.checkRecordEquivalenceHelper(recordType, true));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType2, true));
 assertFalse(recordType.checkRecordEquivalenceHelper(recordType, false));
 }

 @Test
 public void testGetImplicitPrototype() {
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 assertFalse(recordType.defineProperty("newProperty", registry.getStringType(), false, null));
 assertTrue(recordType2.defineProperty("newProperty", registry.getStringType(), false, null));
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType2);
 assertEquals(recordType, greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
 assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

 greatestSubtype = recordType.getGreatestSubtypeHelper(registry.getNativeType(JSTypeNative.NO_TYPE));
 assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE), greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 // RecordType is a subtype of RecordType
 assertTrue(recordType.isSubtype(recordType));
 // RecordType is not a subtype of UnionType
 assertFalse(recordType.isSubtype(new UnionTypeBuilder(registry)
 .addAlternate(registry.getNativeType(JSTypeNative.STRING_TYPE))
 .addAlternate(registry.getNativeType(JSTypeNative.NUMBER_TYPE))
 .build()));
 // RecordType with additional properties is a subtype of RecordType
 JSType recordProperty4 = new RecordPersonProperty();
 Map<String, RecordProperty> properties4 = Maps.newHashMap();
 properties4.putAll(properties);
 properties4.put("gender", recordProperty4);
 RecordType recordType4 = new RecordType(registry, properties4, false);
 assertTrue(recordType.isSubtype(recordType4));
 }

 @Test
 public void testToString() {
 assertEquals("{name: string, age: number}", recordType.toString());
 }
}

class RecordPersonProperty extends JSType {
 @Override
 public String toString() {
 return "Person";
 }
}