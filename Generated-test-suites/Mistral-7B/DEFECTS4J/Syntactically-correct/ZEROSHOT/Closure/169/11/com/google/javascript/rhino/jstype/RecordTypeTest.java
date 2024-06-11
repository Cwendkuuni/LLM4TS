import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    registry = Mockito.spy(new JSTypeRegistry() {});
    errorReporter = Mockito.spy(new ErrorReporter() {});
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("property1", Mockito.mock(RecordProperty.class));
    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals("RecordType", recordType.getName());
    assertNotNull(recordType.getImplicitPrototype());
    assertTrue(recordType.isRecordType());
    assertTrue(recordType.isSynthetic());
  }

  @Test
  public void testRecordTypeWithProperties() {
    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("property1", Mockito.mock(RecordProperty.class));
    properties.put("property2", Mockito.mock(RecordProperty.class));

    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(2, recordType.getPropertyCount());

    assertEquals("property1", recordType.getPropertyNames().get(0));
    assertEquals("property2", recordType.getPropertyNames().get(1));

    assertEquals(recordType.getPropertyType("property1"),
        Mockito.argThat(type -> type.getName().equals("property1Type")));
    assertEquals(recordType.getPropertyType("property2"),
        Mockito.argThat(type -> type.getName().equals("property2Type")));

    assertFalse(recordType.hasProperty("property3"));
  }

  @Test
  public void testGetGreatestSubtype() {
    Map<String, RecordProperty> propertiesA = new LinkedHashMap<>();
    propertiesA.put("property1", Mockito.mock(RecordProperty.class));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new LinkedHashMap<>();
    propertiesB.put("property1", Mockito.mock(RecordProperty.class));
    propertiesB.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);

    // A is a subtype of B
    unionTypeBuilder.addAlternate(recordTypeA);
    RecordType greatestSubtypeAB = unionTypeBuilder.build();
    assertEquals(recordTypeB, greatestSubtypeAB.toMaybeRecordType());

    // B is not a subtype of A
    unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordTypeB);
    RecordType greatestSubtypeBA = unionTypeBuilder.build();
    assertNull(greatestSubtypeBA.toMaybeRecordType());

    // Neither A nor B is a subtype of C
    Map<String, RecordProperty> propertiesC = new LinkedHashMap<>();
    propertiesC.put("property3", Mockito.mock(RecordProperty.class));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(recordTypeA);
    unionTypeBuilder.addAlternate(recordTypeB);
    unionTypeBuilder.addAlternate(recordTypeC);

    RecordType greatestSubtypeABC = unionTypeBuilder.build();
    assertNull(greatestSubtypeABC.toMaybeRecordType());
  }

  @Test
  public void testIsSubtype() {
    Map<String, RecordProperty> propertiesA = new LinkedHashMap<>();
    propertiesA.put("property1", Mockito.mock(RecordProperty.class));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new LinkedHashMap<>();
    propertiesB.put("property1", Mockito.mock(RecordProperty.class));
    propertiesB.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new LinkedHashMap<>();
    propertiesC.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    Map<String, RecordProperty> propertiesD = new LinkedHashMap<>();
    propertiesD.put("property3", Mockito.mock(RecordProperty.class));
    RecordType recordTypeD = new RecordType(registry, propertiesD, true);

    Map<String, RecordProperty> propertiesE = new LinkedHashMap<>();
    propertiesE.put("property1", Mockito.mock(RecordProperty.class));
    propertiesE.put("property2", Mockito.mock(RecordProperty.class));
    propertiesE.put("property3", Mockito.mock(RecordProperty.class));
    RecordType recordTypeE = new RecordType(registry, propertiesE, true);

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));

    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));

    assertFalse(recordTypeA.isSubtype(recordTypeD));
    assertFalse(recordTypeD.isSubtype(recordTypeA));

    assertTrue(recordTypeA.isSubtype(recordTypeE));
    assertTrue(recordTypeE.isSubtype(recordTypeA));
  }

  @Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("property1", Mockito.mock(RecordProperty.class));
    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.defineProperty("property1", Mockito.mock(JSType.class),
        false, null));
    assertFalse(recordType.defineProperty("property2", Mockito.mock(JSType.class),
        true, null));

    SortedMap<String, JSType> actualProperties = recordType.properties;
    assertEquals(1, actualProperties.size());
    assertEquals("property1", actualProperties.keySet().iterator().next());
    assertEquals(recordType.getPropertyType("property1"),
        Mockito.argThat(type -> type.getName().equals("property1Type")));

    recordType.defineProperty("property1", Mockito.mock(JSType.class), false, null);
    actualProperties = recordType.properties;
    assertEquals(1, actualProperties.size());
    assertEquals("property1", actualProperties.keySet().iterator().next());
    assertEquals(recordType.getPropertyType("property1"),
        Mockito.argThat(type -> type.getName().equals("property1Type")));

    recordType.defineProperty("property2", Mockito.mock(JSType.class), true, null);
    actualProperties = recordType.properties;
    assertEquals(2, actualProperties.size());
    assertTrue(actualProperties.containsKey("property1"));
    assertTrue(actualProperties.containsKey("property2"));
    assertEquals(recordType.getPropertyType("property1"),
        Mockito.argThat(type -> type.getName().equals("property1Type")));
    assertEquals(recordType.getPropertyType("property2"),
        Mockito.argThat(type -> type.getName().equals("property2Type")));
  }

  @Test
  public void testCheckRecordEquivalence() {
    Map<String, RecordProperty> propertiesA = new LinkedHashMap<>();
    propertiesA.put("property1", Mockito.mock(RecordProperty.class));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    Map<String, RecordProperty> propertiesB = new LinkedHashMap<>();
    propertiesB.put("property1", Mockito.mock(RecordProperty.class));
    propertiesB.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new LinkedHashMap<>();
    propertiesC.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeA, true));
    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));
    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeC, true));
  }

  @Test
  public void testIsFrozen() {
    Map<String, RecordProperty> properties = new LinkedHashMap<>();
    properties.put("property1", Mockito.mock(RecordProperty.class));
    RecordType recordType = new RecordType(registry, properties, true);

    assertFalse(recordType.isFrozen());

    recordType.freeze();
    assertTrue(recordType.isFrozen());

    Map<String, RecordProperty> newProperties = new LinkedHashMap<>();
    newProperties.put("property1", Mockito.mock(RecordProperty.class));
    newProperties.put("property2", Mockito.mock(RecordProperty.class));

    RecordType newRecordType = new RecordType(registry, newProperties, true);

    assertFalse(recordType.defineProperty("property2", Mockito.mock(JSType.class),
        true, null));
    assertTrue(newRecordType.defineProperty("property2", Mockito.mock(JSType.class),
        true, null));

    assertFalse(recordType.hasProperty("property2"));
    assertTrue(newRecordType.hasProperty("property2"));
  }

  @Test
  public void testIsSubtypeWithUnknownTypes() {
    Map<String, RecordProperty> propertiesA = new LinkedHashMap<>();
    propertiesA.put("property1", Mockito.mock(RecordProperty.class));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    recordTypeA.getPropertyType("property1").setUnknownType(true);

    Map<String, RecordProperty> propertiesB = new LinkedHashMap<>();
    propertiesB.put("property1", Mockito.mock(RecordProperty.class));
    propertiesB.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);
    recordTypeB.getPropertyType("property1").setUnknownType(true);

    Map<String, RecordProperty> propertiesC = new LinkedHashMap<>();
    propertiesC.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testIsSubtypeWithInvariantTypes() {
    Map<String, RecordProperty> propertiesA = new LinkedHashMap<>();
    propertiesA.put("property1", Mockito.mock(RecordProperty.class));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    recordTypeA.getPropertyType("property1").setInvariant(true);

    Map<String, RecordProperty> propertiesB = new LinkedHashMap<>();
    propertiesB.put("property1", Mockito.mock(RecordProperty.class));
    propertiesB.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    Map<String, RecordProperty> propertiesC = new LinkedHashMap<>();
    propertiesC.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);
    recordTypeC.getPropertyType("property2").setInvariant(true);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
    assertFalse(recordTypeB.isSubtype(recordTypeA));
    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));

    Map<String, RecordProperty> propertiesD = new LinkedHashMap<>();
    propertiesD.put("property1", Mockito.mock(RecordProperty.class));
    propertiesD.put("property2", Mockito.mock(RecordProperty.class));
    RecordType recordTypeD = new RecordType(registry, propertiesD, true);
    recordTypeD.getPropertyType("property1").setInvariant(false);

    assertTrue(recordTypeA.isSubtype(recordTypeD));
    assertTrue(recordTypeD.isSubtype(recordTypeA));
  }
}