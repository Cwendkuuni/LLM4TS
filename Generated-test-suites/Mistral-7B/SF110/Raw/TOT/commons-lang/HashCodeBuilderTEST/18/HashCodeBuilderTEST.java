import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

 private Object simpleObject;
 private Object arrayObject;
 private Object recursiveObject;

 @Before
 public void setup() {
 simpleObject = new SimpleObject();
 arrayObject = new Object[] {1, 2, 3};
 recursiveObject = new RecursiveObject();
 }

 @Test
 public void testReflectionHashCodeForSimpleObject() {
 int expectedHashCode = HashCodeBuilder.reflectionHashCode(simpleObject, false);
 int actualHashCode = HashCodeBuilder.reflectionHashCode(simpleObject);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeForArrayObject() {
 int expectedHashCode = HashCodeBuilder.reflectionHashCode(arrayObject, false, Object[].class, "length");
 int actualHashCode = HashCodeBuilder.reflectionHashCode(arrayObject);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeForRecursiveObject() {
 int expectedHashCode = HashCodeBuilder.reflectionHashCode(recursiveObject, false, RecursiveObject.class, new String[]{"recursiveField"});
 int actualHashCode = HashCodeBuilder.reflectionHashCode(recursiveObject);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReflectionHashCodeForNullObject() {
 HashCodeBuilder.reflectionHashCode(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReflectionHashCodeWithNullReflectUpToClass() {
 HashCodeBuilder.reflectionHashCode(simpleObject, 17, 37, null, true, new String[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReflectionHashCodeWithNullExcludeFields() {
 HashCodeBuilder.reflectionHashCode(simpleObject, 17, 37, null, false, null);
 }

 @Test
 public void testReflectionHashCodeWithTestTransientsTrue() {
 int expectedHashCode = HashCodeBuilder.reflectionHashCode(simpleObject, 17, 37, true, null, new String[0]);
 int actualHashCode = HashCodeBuilder.reflectionHashCode(simpleObject, true);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testReflectionHashCodeWithTestTransientsFalse() {
 int expectedHashCode = HashCodeBuilder.reflectionHashCode(simpleObject, 17, 37, false, null, new String[0]);
 int actualHashCode = HashCodeBuilder.reflectionHashCode(simpleObject);
 assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testRegistrationAndUnRegistration() {
 HashCodeBuilder.register(simpleObject);
 HashCodeBuilder.unregister(simpleObject);
 HashCodeBuilder.register(arrayObject);
 HashCodeBuilder.unregister(arrayObject);
 HashCodeBuilder.register(recursiveObject);
 HashCodeBuilder.unregister(recursiveObject);
 Set<IDKey> registry = HashCodeBuilder.getRegistry();
 assertEquals(0, registry.size());
 }
}

class SimpleObject {

 private int id;

 public SimpleObject() {
 this.id = 1;
 }

 @Override
 public int hashCode() {
 return id;
 }
}

class RecursiveObject {

 private RecursiveObject recursiveField = new RecursiveObject();

 public RecursiveObject() {
 }

 @Override
 public int hashCode() {
 return HashCodeBuilder.reflectionHashCode(this);
 }
}