import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    RecordProperty recordProperty1 = new RecordProperty("property1", int.class, null);
    RecordProperty recordProperty2 = new RecordProperty("property2", String.class, null);

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("property1", recordProperty1);
    properties.put("property2", recordProperty2);

    recordType = new RecordType(registry, properties, true);
  }

  @Test
  public void testIsSubtype() {
    JSType recordType1 = new PrototypeObjectType(registry, null, null);
    JSType recordType2 = new RecordType(registry, properties, true);
    JSType recordType3 = new RecordType(registry, properties, false);

    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();

    JSType recordType4 = new RecordType(registry, properties, true);

    // RecordType is a subtype of ObjectType
    assertTrue(recordType2.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

    // RecordType1 is not a subtype of RecordType2
    assertFalse(recordType1.isSubtype(recordType2));

    // RecordType1 is not a subtype of RecordType3
    assertFalse(recordType1.isSubtype(recordType3));

    // RecordType1 is not a subtype of RecordType4
    assertFalse(recordType1.isSubtype(recordType4));

    // RecordType2 is a subtype of RecordType2
    assertTrue(recordType2.isSubtype(recordType2));

    // RecordType2 is a subtype of RecordType3
    assertTrue(recordType2.isSubtype(recordType3));

    // RecordType2 is a subtype of RecordType4
    assertTrue(recordType2.isSubtype(recordType4));

    // RecordType3 is not a subtype of RecordType1
    assertFalse(recordType3.isSubtype(recordType1));

    // RecordType3 is a subtype of RecordType2
    assertTrue(recordType3.isSubtype(recordType2));

    // RecordType3 is a subtype of RecordType3
    assertTrue(recordType3.isSubtype(recordType3));

    // RecordType3 is a subtype of RecordType4
    assertTrue(recordType3.isSubtype(recordType4));

    // RecordType4 is a subtype of RecordType2
    assertTrue(recordType4.isSubtype(recordType2));

    // RecordType4 is a subtype of RecordType3
    assertTrue(recordType4.isSubtype(recordType3));

    // RecordType4 is a subtype of RecordType4
    assertTrue(recordType4.isSubtype(recordType4));

    // IntType is not a subtype of RecordType
    assertFalse(intType.isSubtype(recordType2));

    // StringType is not a subtype of RecordType
    assertFalse(stringType.isSubtype(recordType2));

    // IntType is a subtype of IntType
    assertTrue(intType.isSubtype(intType));

    // IntType is a subtype of StringType (NO_TYPE)
    assertFalse(intType.isSubtype(stringType));

    // IntType is a subtype of UnionType (IntType and StringType)
    UnionType unionType = new UnionTypeBuilder(registry).addAlternate(intType)
        .addAlternate(stringType).build();
    assertTrue(intType.isSubtype(unionType));

    // StringType is a subtype of UnionType (IntType and StringType)
    assertTrue(stringType.isSubtype(unionType));

    // RecordType2 is a subtype of UnionType (IntType and StringType)
    assertTrue(recordType2.isSubtype(unionType));
  }

  @Test
  public void testGetGreatestSubtype() {
    JSType recordType1 = new RecordType(registry, properties, true);
    JSType recordType2 = new RecordType(registry, properties, true);
    JSType recordType3 = new RecordType(registry, properties, false);

    JSType recordType4 = new RecordType(registry, properties, true);
    JSType recordType5 = new RecordType(registry, properties, false);

    JSType intType = registry.getIntType();
    JSType stringType = registry.getStringType();

    JSType recordType6 = new RecordType(registry, properties, true);
    JSType recordType7 = new RecordType(registry, properties, false);

    JSType intIntType = registry.getIntType();
    JSType stringStringType = registry.getStringType();

    JSType unionType = new UnionTypeBuilder(registry)
        .addAlternate(intType).addAlternate(stringType).build();

    JSType unionIntIntType = new UnionTypeBuilder(registry)
        .addAlternate(intIntType).addAlternate(intIntType).build();

    JSType unionIntString = new UnionTypeBuilder(registry)
        .addAlternate(intType).addAlternate(stringType).build();

    JSType unionStringInt = new UnionTypeBuilder(registry)
        .addAlternate(stringType).addAlternate(intType).build();

    JSType recordTypeInt = new RecordTypeBuilder(registry)
        .addProperty("property1", intType, null).build();

    JSType recordTypeString = new RecordTypeBuilder(registry)
        .addProperty("property1", stringType, null).build();

    JSType recordTypeIntString = new RecordTypeBuilder(registry)
        .addProperty("property1", intType, null)
        .addProperty("property1", stringType, null).build();

    JSType recordTypeUnionIntString = new RecordTypeBuilder(registry)
        .addProperty("property1", unionIntString, null).build();

    JSType recordTypeUnionStringInt = new RecordTypeBuilder(registry)
        .addProperty("property1", unionStringInt, null).build();

    JSType recordTypeUnionIntInt = new RecordTypeBuilder(registry)
        .addProperty("property1", unionIntIntType, null).build();

    JSType recordTypeRecord = new RecordTypeBuilder(registry)
        .addProperty("property1", recordTypeInt, null)
        .addProperty("property2", recordTypeString, null).build();

    JSType recordTypeRecordUnion = new RecordTypeBuilder(registry)
        .addProperty("property1", recordTypeUnionIntString, null)
        .addProperty("property2", recordTypeUnionStringInt, null).build();

    JSType recordTypeUnionRecord = new RecordTypeBuilder(registry)
        .addProperty("property1", recordTypeRecord, null).build();

    JSType recordTypeRecordUnionRecord = new RecordTypeBuilder(registry)
        .addProperty("property1", recordTypeRecordUnion, null).build();

    // GetGreatestSubtype of RecordType1 and RecordType2
    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of RecordType1 and RecordType1
    greatestSubtype = recordType1.getGreatestSubtype(recordType1);
    assertEquals(recordType1, greatestSubtype);

    // GetGreatestSubtype of RecordType1 and RecordType3
    greatestSubtype = recordType1.getGreatestSubtype(recordType3);
    assertEquals(recordType3, greatestSubtype);

    // GetGreatestSubtype of RecordType1 and RecordType4
    greatestSubtype = recordType1.getGreatestSubtype(recordType4);
    assertEquals(recordType4, greatestSubtype);

    // GetGreatestSubtype of RecordType3 and RecordType2
    greatestSubtype = recordType3.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of RecordType3 and RecordType3
    greatestSubtype = recordType3.getGreatestSubtype(recordType3);
    assertEquals(recordType3, greatestSubtype);

    // GetGreatestSubtype of RecordType3 and RecordType4
    greatestSubtype = recordType3.getGreatestSubtype(recordType4);
    assertEquals(recordType4, greatestSubtype);

    // GetGreatestSubtype of RecordType4 and RecordType2
    greatestSubtype = recordType4.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of RecordType4 and RecordType3
    greatestSubtype = recordType4.getGreatestSubtype(recordType3);
    assertEquals(recordType3, greatestSubtype);

    // GetGreatestSubtype of RecordType4 and RecordType4
    greatestSubtype = recordType4.getGreatestSubtype(recordType4);
    assertEquals(recordType4, greatestSubtype);

    // GetGreatestSubtype of IntType and RecordType
    greatestSubtype = intType.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of StringType and RecordType
    greatestSubtype = stringType.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of IntType and IntType
    greatestSubtype = intType.getGreatestSubtype(intType);
    assertEquals(intType, greatestSubtype);

    // GetGreatestSubtype of StringType and StringType
    greatestSubtype = stringType.getGreatestSubtype(stringType);
    assertEquals(stringType, greatestSubtype);

    // GetGreatestSubtype of UnionType (IntType and StringType) and IntType
    greatestSubtype = unionType.getGreatestSubtype(intType);
    assertEquals(intType, greatestSubtype);

    // GetGreatestSubtype of UnionType (IntType and StringType) and StringType
    greatestSubtype = unionType.getGreatestSubtype(stringType);
    assertEquals(stringType, greatestSubtype);

    // GetGreatestSubtype of UnionType (IntType and StringType) and RecordType
    greatestSubtype = unionType.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of IntType and UnionType (IntType and StringType)
    greatestSubtype = intType.getGreatestSubtype(unionType);
    assertEquals(unionType, greatestSubtype);

    // GetGreatestSubtype of StringType and UnionType (IntType and StringType)
    greatestSubtype = stringType.getGreatestSubtype(unionType);
    assertEquals(unionType, greatestSubtype);

    // GetGreatestSubtype of RecordType and UnionType (IntType and StringType)
    greatestSubtype = recordType2.getGreatestSubtype(unionType);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of UnionType (IntType and IntType) and IntType
    greatestSubtype = unionIntIntType.getGreatestSubtype(intIntType);
    assertEquals(intIntType, greatestSubtype);

    // GetGreatestSubtype of UnionType (IntType and IntType) and IntType
    greatestSubtype = unionIntIntType.getGreatestSubtype(intType);
    assertEquals(intType, greatestSubtype);

    // GetGreatestSubtype of UnionType (IntType and StringType) and IntType
    greatestSubtype = unionIntString.getGreatestSubtype(intType);
    assertEquals(intType, greatestSubtype);

    // GetGreatestSubtype of UnionType (IntType and StringType) and StringType
    greatestSubtype = unionIntString.getGreatestSubtype(stringType);
    assertEquals(stringType, greatestSubtype);

    // GetGreatestSubtype of UnionType (IntType and StringType) and RecordType
    greatestSubtype = unionIntString.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of UnionType (StringType and IntType) and IntType
    greatestSubtype = unionStringInt.getGreatestSubtype(intType);
    assertEquals(intType, greatestSubtype);

    // GetGreatestSubtype of UnionType (StringType and IntType) and StringType
    greatestSubtype = unionStringInt.getGreatestSubtype(stringType);
    assertEquals(stringType, greatestSubtype);

    // GetGreatestSubtype of UnionType (StringType and IntType) and RecordType
    greatestSubtype = unionStringInt.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of RecordType and UnionType (IntType and StringType)
    greatestSubtype = recordType2.getGreatestSubtype(unionType);
    assertEquals(recordType2, greatestSubtype);

    // GetGreatestSubtype of UnionType (RecordType and RecordType) and RecordType
    greatestSubtype = recordTypeRecord.getGreatestSubtype(recordTypeRecordUnion);
    assertEquals(recordTypeRecordUnion, greatestSubtype);

    // GetGreatestSubtype of RecordType and UnionType (RecordType and RecordType)
    greatestSubtype = recordTypeRecordUnion.getGreatestSubtype(recordTypeRecord);
    assertEquals(recordTypeRecord, greatestSubtype);

    // GetGreatestSubtype of UnionType (RecordType and RecordType) and RecordType
    greatestSubtype = recordTypeUnionRecord.getGreatestSubtype(recordTypeRecordUnionRecord);
    assertEquals(recordTypeRecordUnionRecord, greatestSubtype);

    // GetGreatestSubtype of RecordType and RecordType
    greatestSubtype = recordType2.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);
  }
}