import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;

public class RecordTypeTest {

 @Test
 public void testRecordTypeConstruction() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 }});

 assertTrue(recordType.isSynthetic());
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
assertEquals(2, recordType.getPropertyCount());
 assertEquals("prop1", recordType.getPropertyNames()[0]);
 assertEquals("prop2", recordType.getPropertyNames()[1]);
 }

 @Test
 public void testRecordTypeConstructionDeclared() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING, true));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER, true));
 }}, true);

 assertFalse(recordType.isSynthetic());
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 assertEquals(2, recordType.getPropertyCount());
 assertEquals("prop1", recordType.getPropertyNames()[0]);
 assertEquals("prop2", recordType.getPropertyNames()[1]);
 }

 @Test
 public void testIsFrozen() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 }});
 recordType.setFrozen(true);

 assertFalse(recordType.defineProperty("prop1", JSType.BOOLEAN, false, null));
 }

 @Test
 public void testCheckRecordEquivalenceHelper() {
 RecordType recordType1 = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 }});
 RecordType recordType2 = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.STRING));
 }});
 RecordType recordType3 = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.BOOLEAN));
 }});

 assertTrue(recordType1.checkRecordEquivalenceHelper(recordType1, EquivalenceMethod.IDENTICAL));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));
 assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTICAL));
 }

 @Test
 public void testGetImplicitPrototype() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 }});
 assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getImplicitPrototype());
 }

 @Test
 public void testDefineProperty() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 }});
 assertFalse(recordType.defineProperty("prop3", JSType.BOOLEAN, false, null));
 assertTrue(recordType.defineProperty("prop1", JSType.STRING, false, null));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullRecordProperty() {
 RecordType recordType = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", null);
 }});
 }

 @Test
 public void testGetGreatestSubtypeHelper() {
 JSType typeA = JSType.OBJECT;
 JSType typeB = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.BOOLEAN));
 }});
 JSType greatestSubtype = typeA.getGreatestSubtype(typeB);

 assertEquals(typeB, greatestSubtype);

 typeA = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.NUMBER));
 }});
 greatestSubtype = typeA.getGreatestSubtype(typeB);

 assertEquals(typeB, greatestSubtype);

 typeA = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.BOOLEAN));
 put("prop4", new RecordTypeBuilder.RecordProperty("prop4", JSType.STRING));
 }});
 greatestSubtype = typeA.getGreatestSubtype(typeB);

 assertEquals(typeB, greatestSubtype);

 typeA = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.BOOLEAN));
 put("prop4", new RecordTypeBuilder.RecordProperty("prop4", JSType.NUMBER));
 }});
 greatestSubtype = typeA.getGreatestSubtype(typeB);

 assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype);
 }

 @Test
 public void testIsSubtype() {
 RecordType recordTypeA = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 }});
 RecordType recordTypeB = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.BOOLEAN));
 }});
 RecordType recordTypeC = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.INT));
 }});
 RecordType recordTypeD = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.NUMBER));
 }});

 assertTrue(recordTypeA.isSubtype(recordTypeD));
 assertTrue(recordTypeD.isSubtype(recordTypeA));
 assertTrue(recordTypeA.isSubtype(recordTypeC));
 assertFalse(recordTypeC.isSubtype(recordTypeA));
 assertFalse(recordTypeB.isSubtype(recordTypeA));
 }

 @Test
 public void testIsSubtypeWithInvariantTypes() {
 RecordType recordTypeA = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 }});
 RecordType recordTypeB = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.INTEGER));
 }});
 RecordType recordTypeC = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.STRING));
 }});

 assertTrue(recordTypeA.isSubtype(recordTypeB));
 assertTrue(recordTypeB.isSubtype(recordTypeA));
 assertFalse(recordTypeC.isSubtype(recordTypeA));
 assertFalse(recordTypeA.isSubtype(recordTypeC));
 }

 @Test
 public void testIsSubtypeWithNonInvariantTypes() {
 RecordType recordTypeA = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.NUMBER));
 }});
 RecordType recordTypeB = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.BOOLEAN));
 }});
 RecordType recordTypeC = new RecordType(
 new JSTypeRegistry(), new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
 put("prop1", new RecordTypeBuilder.RecordProperty("prop1", JSType.STRING));
 put("prop2", new RecordTypeBuilder.RecordProperty("prop2", JSType.BOOLEAN));
 put("prop3", new RecordTypeBuilder.RecordProperty("prop3", JSType.NUMBER));
 }});

 assertFalse(recordTypeA.isSubtype(recordTypeB));
 assertFalse(recordTypeB.isSubtype(recordTypeA));
 assertFalse(recordTypeC.isSubtype(recordTypeA));
 assertFalse(recordTypeA.isSubtype(recordTypeC));
 }
}