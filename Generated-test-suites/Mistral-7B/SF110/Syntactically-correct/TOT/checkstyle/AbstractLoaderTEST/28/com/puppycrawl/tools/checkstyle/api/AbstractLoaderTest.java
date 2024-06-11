import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {

 private AbstractLoader loader;

 @Before
 public void setUp() throws ParserConfigurationException, SAXException, IOException {
 loader = new AbstractLoader("testPublicId", "testDTD.dtd");
 }

 @Test
 public void testParseValidInput() throws SAXException, IOException {
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>"));
 loader.parseInputSource(inputSource);
 assertNotNull(loader);
 }

 @Test
 public void testResolveEntityWithValidPublicId() throws SAXException, SAXException, IOException {
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<!DOCTYPE test [ \n<!ENTITY test SYSTEM \"test:test.dtd\" > \n]>"));
 loader.parseInputSource(inputSource);
 assertNotNull(loader.publicIdToResourceNameMap);
 assertNotNull(loader.publicIdToResourceNameMap.get("testPublicId"));
 }

 @Test
 public void testResolveEntityWithInvalidPublicId() throws SAXException, SAXException, IOException {
 InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<!DOCTYPE test [ \n<!ENTITY invalidPublicId SYSTEM \"test:test.dtd\" > \n]>"));
 loader.parseInputSource(inputSource);
 assertNull(loader.publicIdToResourceNameMap.get("invalidPublicId"));
 }

 @Test(expected = SAXException.class)
 public void testErrorDuringParsing() throws SAXException, IOException, ParserConfigurationException {
 final ByteArrayInputStream inputStream = new ByteArrayInputStream("<xml>".getBytes());
 loader.parseInputSource(new InputSource(inputStream));
 }
}