import com.google.javascript.rhino.JDTGlobal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry(new ErrorReporter() {
      @Override
      public void error(String message, Node node, int line, int lineOffset) {
        fail("Unexpected error: " + message);
      }
    });

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties, true);
    JSType recordTypeObject = recordType.toMaybeRecordType();

    // Test record type construction
    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.isSynthetic(), false);
    assertEquals(recordType.getPrettyPrint(), true);
    assertEquals(recordType.getImplicitPrototype(),
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType.getGreatestSubtypeHelper(JSType.OBJECT_TYPE),
        recordTypeObject);

    // Test record type properties
    assertEquals(recordType.getDeclaredProperties().length, 2);
    assertEquals(recordType.getDeclaredProperties()[0].getName(), "name");
    assertEquals(recordType.getDeclaredProperties()[0].getType(),
        registry.getStringType());
    assertEquals(recordType.getDeclaredProperties()[1].getName(), "age");
    assertEquals(recordType.getDeclaredProperties()[1].getType(),
        registry.getNumberType());

    // Test record type checking subtypes
    JSType stringType = registry.getStringType();
    JSType numberType = registry.getNumberType();
    JSType recordTypeStringAge = new RecordTypeBuilder(registry)
        .addProperty("name", stringType, null)
        .addProperty("age", numberType, null)
        .build();

    JSType recordTypeName = new RecordTypeBuilder(registry)
        .addProperty("name", stringType, null)
        .build();

    // Test record type isSubtype
    assertTrue(recordTypeStringAge.isSubtype(recordType));
    assertTrue(recordType.isSubtype(recordTypeName));
    assertFalse(recordType.isSubtype(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE)));
    assertFalse(recordTypeName.isSubtype(recordTypeStringAge));

    // Test record type checkRecordEquivalenceHelper
    RecordType otherRecord = new RecordType(registry, properties, false);
    assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, true));

    // Test record type defineProperty
    RecordType recordTypeNameAge = new RecordType(registry, properties, false);
    recordTypeNameAge.defineProperty("age", numberType, false, null);
    assertFalse(recordType.isEquivalentTo(recordTypeNameAge));

    // Test record type freeze
    recordTypeNameAge.freeze();
    assertFalse(recordTypeNameAge.defineProperty("name", stringType, false, null));

    // Test record type getImplicitPrototype
    assertTrue(recordType.getImplicitPrototype() instanceof ObjectType);

    // Test record type resolveInternal
    RecordType recordTypeNullName = new RecordType(registry, Properties.newHashMap(), true);
    recordTypeNullName.defineProperty("name", null, false, null);
    RecordType recordTypeEmptyName = new RecordType(registry, Properties.newHashMap(), true);
    RecordType recordTypeNameEmpty = new RecordType(registry, Properties.newHashMap(), true);
    recordTypeNameEmpty.defineProperty("name", stringType, false, null);

    JSType registryEmpty = new JSTypeRegistry(new ErrorReporter() {
      @Override
      public void error(String message, Node node, int line, int lineOffset) {
        fail("Unexpected error: " + message);
      }
    });

    JSType recordTypeRegistryEmpty = new RecordType(registryEmpty, properties, true);

    Node node = JDTGlobal.newLiteral(JDTGlobal.STRING_LITERAL, "test");
    RecordType recordTypeStringName = new RecordType(registry,
        Maps.newHashMap().put("name", new RecordProperty("name", stringType, node)),
        true);

    StaticScope<JSType> scope = new StaticScope<>(registry);
    scope.put("recordType", recordType);
    scope.put("registry", registry);
    JSType resolvedRecordType = recordTypeStringName.resolveInternal(
        new ErrorReporter() {
          @Override
          public void error(String message, Node node, int line, int lineOffset) {
            fail("Unexpected error: " + message);
          }
        }, scope);

    assertEquals(resolvedRecordType, recordTypeStringName);
    assertEquals(recordType.getGreatestSubtypeHelper(registryEmpty.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE)), registryEmpty.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE));
    assertEquals(recordType.getGreatestSubtypeHelper(registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE)), recordTypeObject);
    assertEquals(recordTypeStringName.getGreatestSubtypeHelper(recordType), recordTypeStringName);
    assertEquals(recordTypeStringName.getGreatestSubtypeHelper(registryEmpty.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE)), registryEmpty.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE));
  }
}