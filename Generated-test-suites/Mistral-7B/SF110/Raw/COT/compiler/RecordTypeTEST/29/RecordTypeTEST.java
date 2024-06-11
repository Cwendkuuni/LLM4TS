import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.testing.PrivateTester;

public class RecordTypeTest {

  private JSTypeRegistry registry;
  private RecordType recordType;
  private RecordTypeBuilder recordTypeBuilder;
  private PrivateTester privateTester;

  @Before
  public void setUp() {
    this.registry = new JSTypeRegistry();
    RecordProperty recordProperty = new RecordProperty("name", registry.getNullType());
    this.recordTypeBuilder = new RecordTypeBuilder(registry);
    this.privateTester = new PrivateTester(RecordType.class);

    this.recordType = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name", recordProperty);
      }
    }, true);
  }

  @Test
  public void testRecordTypeConstructorWithDeclared() {
    RecordType recordType = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name", new RecordProperty("name", this.registry.getStringType()));
      }
    }, true);

    assertTrue(recordType instanceof RecordType);
  }

  @Test
  public void testRecordTypeConstructorWithUndefinedProperties() {
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    try {
      new RecordType(this.registry, properties, true);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("RecordProperty associated with a property should not be null!"));
    }
  }

  @Test
  public void testRecordTypeConstructorWithUndeclaredProperties() {
    RecordType recordType = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name", new RecordProperty("name", this.registry.getStringType()));
      }
    }, false);

    assertTrue(recordType instanceof RecordType);
  }

  @Test
  public void testIsFrozen() {
    assertFalse(this.recordType.isFrozen());

    this.recordType.freeze();
    assertTrue(this.recordType.isFrozen());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType recordType1 = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
        put("name2", new RecordProperty("name2", this.registry.getStringType()));
      }
    }, true);

    RecordType recordType2 = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
        put("name2", new RecordProperty("name2", this.registry.getStringType()));
      }
    }, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, EquivalenceMethod.IDENTICAL));

    RecordType recordType3 = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
        put("name3", new RecordProperty("name3", this.registry.getStringType()));
      }
    }, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, EquivalenceMethod.IDENTICAL));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertNotNull(this.recordType.getImplicitPrototype());
    assertTrue(this.recordType.getImplicitPrototype() instanceof ObjectType);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "property";
    JSType propertyType = this.registry.getNumberType();

    assertFalse(this.recordType.hasProperty(propertyName));
    assertFalse(this.recordType.defineProperty(propertyName, propertyType, false, null));

    this.recordType.freeze();
    assertFalse(this.recordType.defineProperty(propertyName, propertyType, false, null));

    assertFalse(this.recordType.hasProperty(propertyName));

    this.recordType.defineProperty(propertyName, propertyType, true, null);
    assertTrue(this.recordType.hasProperty(propertyName));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    RecordType recordTypeA = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
      }
    }, true);

    RecordType recordTypeB = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
        put("name2", new RecordProperty("name2", this.registry.getStringType()));
      }
    }, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertNotNull(greestSubtype);
    assertTrue(greestSubtype instanceof RecordType);
  }

  @Test
  public void testIsSubtype() {
    RecordType recordTypeA = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
      }
    }, true);

    RecordType recordTypeB = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
        put("name2", new RecordProperty("name2", this.registry.getStringType()));
      }
    }, true);

    RecordType recordTypeC = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
        put("name3", new RecordProperty("name3", this.registry.getStringType()));
      }
    }, true);

    RecordType recordTypeD = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
      }
    }, false);

    RecordType recordTypeE = this.registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeA.isSubtype(recordTypeA));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeB.isSubtype(recordTypeB));
    assertTrue(recordTypeA.isSubtype(recordTypeD));
    assertTrue(recordTypeD.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeE));
    assertTrue(recordTypeE.isSubtype(recordTypeA));

    assertFalse(recordTypeA.isSubtype(recordTypeC));
    assertFalse(recordTypeC.isSubtype(recordTypeA));
  }

  @Test
  public void testIsRecordType() {
    assertFalse(this.registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isRecordType());
    assertTrue(this.recordType.isRecordType());
  }

  @Test
  public void testToMaybeRecordType() {
    assertSame(this.recordType, this.recordType.toMaybeRecordType());
  }

  @Test
  public void testGetGreatestSubtype() {
    RecordType recordTypeA = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
      }
    }, true);

    RecordType recordTypeB = new RecordType(this.registry, new HashMap<String, RecordProperty>() {
      {
        put("name1", new RecordProperty("name1", this.registry.getStringType()));
        put("name2", new RecordProperty("name2", this.registry.getStringType()));
      }
    }, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
    assertNotNull(greestSubtype);
    assertTrue(greestSubtype instanceof RecordType);

    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(this.registry);
    unionTypeBuilder.addAlternate(recordTypeB);
    JSType unionType = unionTypeBuilder.build();

    greatestSubtype = recordTypeA.getGreatestSubtype(unionType);
    assertNotNull(greestSubtype);
    assertTrue(greestSubtype.isEmptyType());
  }

  @Test
  public void testHasProperty() {
    assertFalse(this.recordType.hasProperty("name"));

    this.recordType.defineProperty("name", this.registry.getStringType(), true, null);
    assertTrue(this.recordType.hasProperty("name"));
  }

  @Test
  public void testGetPropertyType() {
    String propertyName = "name";

    JSType propertyType = this.recordType.getPropertyType(propertyName);
    assertNull(propertyType);

    this.recordType.defineProperty(propertyName, this.registry.getStringType(), true, null);
    propertyType = this.recordType.getPropertyType(propertyName);
    assertNotNull(propertyType);
    assertTrue(propertyType instanceof JSType);
  }

  private static enum EquivalenceMethod {
    STRICT,
    IDENTICAL,
  }
}