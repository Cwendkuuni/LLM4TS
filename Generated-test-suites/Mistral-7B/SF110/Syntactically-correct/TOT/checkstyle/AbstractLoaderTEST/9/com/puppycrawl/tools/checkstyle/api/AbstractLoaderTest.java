import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class AbstractLoaderTest {

 private AbstractLoader loader;
 private InputSource testInputSource;

 @Before
 public void setup() throws Exception {
 loader = new AbstractLoader(new HashMap<>()) {
 @Override
 public void error(SAXParseException exception) throws SAXException {
 throw new SAXParseException("Error in test", exception);
 }

 @Override
 public void fatalError(SAXParseException exception) throws SAXException {
 throw new SAXParseException("Fatal Error in test", exception);
 }
 };
 testInputSource = new InputSource(new ByteArrayInputStream("Test Data".getBytes()));
 }

 @Test
 public void testParseInputSource_WithValidInput() throws Exception {
 loader.parseInputSource(testInputSource);
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntity_WithInvalidPublicId() throws Exception {
 loader.resolveEntity("invalidId", "test.dtd");
 }

 @Test(expected = IOException.class)
 public void testResolveEntity_WithNullSystemId() throws Exception {
 loader.resolveEntity(null, "test.dtd");
 }

 @Test(expected = IOException.class)
 public void testResolveEntity_WithEmptySystemId() throws Exception {
 loader.resolveEntity("", "test.dtd");
 }

 @Test(expected = IOException.class)
 public void testResolveEntity_WithNullResourceName() throws Exception {
 loader.publicIdToResourceNameMap.put(null, "test.dtd");
 loader.parseInputSource(testInputSource);
 loader.publicIdToResourceNameMap.remove(null);
 }

 @Test
 public void testResolveEntity_WithValidPublicIdAndResourceName() throws Exception {
 loader.publicIdToResourceNameMap.put("publicId", "test.dtd");
 loader.parseInputSource(testInputSource);
 final InputSource result = loader.resolveEntity("publicId", null);
 assertNotNull(result);
 loader.publicIdToResourceNameMap.remove("publicId");
 }

 @Test(expected = SAXParseException.class)
 public void testError_WithSAXParseException() throws Exception {
 loader.parseInputSource(new InputSource(new ByteArrayInputStream("<?xml error:test>".getBytes())));
 }

 @Test(expected = SAXException.class)
 public void testFatalError_WithSAXException() throws Exception {
 loader.parseInputSource(new InputSource(new ByteArrayInputStream("<?xml fatal:test>".getBytes())));
 }
}