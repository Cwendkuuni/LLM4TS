import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.PrototypeObjectType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnknownType;

public class RecordTypeTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder builder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    builder = RecordTypeBuilder.create(registry);
  }

  @Test
  public void testEquals() {
    RecordType typeA = createRecordType("{a: Int, b: Int}");
    RecordType typeB = createRecordType("{a: Int, b: Int}");

    assertTrue(typeA.equals(typeB));
  }

  @Test
  public void testNotEquals() {
    RecordType typeA = createRecordType("{a: Int, b: Int}");
    RecordType typeB = createRecordType("{a: Int, c: Int}");

    assertFalse(typeA.equals(typeB));
  }

  @Test
  public void testIsSubtype() {
    RecordType typeA = createRecordType("{a: Int, b: Int}");
    RecordType typeB = createRecordType("{a: Int}");

    assertFalse(typeB.isSubtype(typeA));

    RecordType typeC = createRecordType("{a: Int, b: Int}");
    RecordType typeD = createRecordType("{a: Int, b: Int, c: Int}");

    assertFalse(typeC.isSubtype(typeD));

    RecordType typeE = createRecordType("{a: Int, b: Int}");
    RecordType typeF = createRecordType("{a: Int, b: Int, c: Int}");
    RecordType typeG = createRecordType("{a: Int, b: Int}");

    assertTrue(typeC.isSubtype(typeG));
    assertTrue(typeG.isSubtype(typeC));
    assertTrue(typeE.isSubtype(typeF));
    assertTrue(typeF.isSubtype(typeE));
    assertTrue(typeE.isSubtype(typeE));
    assertTrue(typeF.isSubtype(typeF));

    JSType typeX = JSTypeFactory.nativeObject();
    assertTrue(typeX.isSubtype(typeE));
    assertFalse(typeE.isSubtype(typeX));

    JSType typeY = registry.getNativeType(JSTypeNative.NO_TYPE);
    assertFalse(typeY.isSubtype(typeE));
    assertFalse(typeE.isSubtype(typeY));
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType typeA = createRecordType("{a: Int, b: Int}");
    RecordType typeB = createRecordType("{a: Int, b: Int}");

    assertTrue(typeA.isEquivalentTo(typeB));

    RecordType typeC = createRecordType("{a: Int, b: Int}");
    RecordType typeD = createRecordType("{a: String, b: Int}");

    assertFalse(typeC.isEquivalentTo(typeD));
  }

  @Test
  public void testGetLeastSupertype() {
    RecordType typeA = createRecordType("{a: Int, b: Int}");
    RecordType typeB = createRecordType("{a: Int, c: Int}");

    RecordType leastCommon = typeA.getLeastSupertype(typeB);
    assertEquals(typeB, leastCommon);

    RecordType typeC = createRecordType("{a: Int, b: Int, c: Int}");
    RecordType typeD = createRecordType("{a: Int, b: Int}");

    RecordType leastCommon2 = typeC.getLeastSupertype(typeD);
    assertEquals(typeD, leastCommon2);
  }

  @Test
  public void testGetGreatestSubtype() {
    RecordType typeA = createRecordType("{a: Int, b: Int}");
    RecordType typeB = createRecordType("{a: Int, c: Int}");

    JSType greatestCommon = typeA.getGreatestSubtype(typeB);
    assertEquals(UnknownType.UNKNOWN, greatestCommon);

    RecordType typeC = createRecordType("{a: Int, b: Int}");
    RecordType typeD = createRecordType("{a: Int, c: Int}");
    RecordType typeE = createRecordType("{a: Int, c: Int, d: Int}");

    JSType greatestCommon2 = typeC.getGreatestSubtype(typeE);
    assertEquals(typeC, greatestCommon2);

    RecordType typeF = createRecordType("{a: Int, b: Int}");
    RecordType typeG = createRecordType("{a: String, b: Int}");

    JSType greatestCommon3 = typeF.getGreatestSubtype(typeG);
    assertEquals(UnionType.create(typeF, typeG), greatestCommon3);
  }

  private RecordType createRecordType(String recordTypeDefinition) {
    Map<String, RecordProperty> properties = Maps.newHashMap();

    String[] propertyDefinitions = recordTypeDefinition.substring(1,
        recordTypeDefinition.length() - 1)
        .split(", ");

    for (String propertyDefinition : propertyDefinitions) {
      String[] propertyParts = propertyDefinition.split(": ");
      properties.put(propertyParts[0], new RecordProperty(propertyParts[1]));
    }

    return new RecordType(registry, properties);
  }
}