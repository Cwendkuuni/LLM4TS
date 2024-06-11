import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import org.junit.Assert;
import org.junit.Test;

public class GeneratorBaseTest {

 private GeneratorBase generator;

 @Test
 public void testConstructor() {
 GeneratorBase generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 Assert.assertNotNull(generator);
 }

 @Test
 public void testVersion() {
 GeneratorBase generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 Assert.assertNotNull(generator.version());
 }

 @Test
 public void testIsEnabled_WRITE_NUMBERS_AS_STRINGS() {
 GeneratorBase generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK | Feature.WRITE_NUMBERS_AS_STRINGS.getMask(), null);
 Assert.assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 Assert.assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 @Test
 public void testEnable_WRITE_NUMBERS_AS_STRINGS() {
 GeneratorBase generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
 Assert.assertTrue(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

 generator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
 Assert.assertFalse(generator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
 }

 @Test
 public void testWriteString() throws IOException {
 String testString = "This is a test string";
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
 generator.writeString(testString);
 }

 @Test(expected=IOException.class)
 public void testWriteRawValue_WriteBinary() throws IOException {
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.writeRawValue("binary data", Base64Variant.STANDARD);
 }

 @Test
 public void testWriteObject_Null() throws IOException {
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.writeObject(null);
 generator.flush();
 }

 @Test
 public void testWriteObject_String() throws IOException {
 String testString = "This is a test string";
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.writeObject(testString);
 generator.flush();
 }

 @Test
 public void testWriteObject_BigDecimal() throws IOException {
 BigDecimal testDecimal = new BigDecimal("1234567890.1234567890");
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.writeObject(testDecimal);
 generator.flush();
 }

 @Test(expected=IOException.class)
 public void testWriteObject_InvalidObject() throws IOException {
 Object invalidObject = new Object() {
 @Override
 public String toString() {
 throw new RuntimeException("Cannot convert to string");
 }
 };
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.writeObject(invalidObject);
 }

 @Test
 public void testWriteTree_Null() throws IOException {
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 TreeNode rootNode = null;
 generator.writeTree(rootNode);
 generator.flush();
 }

 @Test
 public void testWriteTree_String() throws IOException {
 String testString = "This is a test string";
 TreeNode rootNode = new ArrayNode();
 rootNode.add(testString);
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.writeTree(rootNode);
 generator.flush();
 }

 @Test(expected=IOException.class)
 public void testWriteTree_InvalidTreeNode() throws IOException {
 Object invalidTreeNode = new Object() {
 @Override
 public void serializeContents(JsonGenerator generator, SerializerProvider provider) throws IOException {
 throw new RuntimeException("Cannot serialize");
 }
 };
 TreeNode rootNode = new ObjectNode();
 rootNode.set("invalid", invalidTreeNode);
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.writeTree(rootNode);
 }

 @Test
 public void testClose() throws IOException {
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.close();
 }

 @Test(expected=IOException.class)
 public void testWriteAfterClose() throws IOException {
 generator = new GeneratorBase(GeneratorBase.DERIVED_FEATURES_MASK, null);
 generator.close();
 generator.writeString("This should not be written");
 }
}