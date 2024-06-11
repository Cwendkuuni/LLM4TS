import org.jdom2.JDOMFactory;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

 private static final String XML_DATA = "<root><child>Hello</child></root>";

 @Test
 public void testGetResultFromList() throws SAXException {
 JDOMResult result = new JDOMResult();
 JDOMFactory factory = new JDOMFactory();
 SAXBuilder builder = new SAXBuilder(factory);
 StringReader reader = new StringReader(XML_DATA);
 builder.build(reader);
 result.setResult(builder.getDocument().getContent());

 List<Content> expected = new ArrayList<Content>() {{
 add(new Element("root"));
 add(new Element("child").setText("Hello"));
 }};
 List<Content> actual = result.getResult();

 Assert.assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 Content expectedContent = expected.get(i);
 Content actualContent = actual.get(i);
 Assert.assertEquals(expectedContent.getName(), actualContent.getName());
 if (expectedContent instanceof Element) {
 Assert.assertEquals(((Element) expectedContent).getText(), ((Element) actualContent).getText());
 }
 }
 }

 @Test
 public void testGetResultFromDocument() throws SAXException, org.jdom2.JDOMException {
 JDOMResult result = new JDOMResult();
 JDOMFactory factory = new JDOMFactory();
 SAXBuilder builder = new SAXBuilder(factory);
 StringReader reader = new StringReader(XML_DATA);
 Document document = builder.build(reader);
 result.setDocument(document);

 Document expectedDocument = new Document(new Element("root"));
 Element child = new Element("child").setText("Hello");
 expectedDocument.setContent(new Element[] {child});

 Document actualDocument = result.getDocument();

 Assert.assertEquals(expectedDocument.getContentSize(), actualDocument.getContentSize());
 for (int i = 0; i < expectedDocument.getContentSize(); i++) {
 Content expectedContent = expectedDocument.getContent().get(i);
 Content actualContent = actualDocument.getContent().get(i);
 if (expectedContent instanceof Element) {
 Element expectedElement = (Element) expectedContent;
 Element actualElement = (Element) actualContent;
 Assert.assertEquals(expectedElement.getText(), actualElement.getText());
 }
 }
 }

 @Test(expected = RuntimeException.class)
 public void testGetResultWhenBothResultListAndDocumentAreNull() {
 JDOMResult result = new JDOMResult();
 result.retrieveResult();
 }

 @Test
 public void testEmptyResult() throws SAXException {
 JDOMResult result = new JDOMResult();
 SAXBuilder builder = new SAXBuilder();
 result.setHandler(builder);
 builder.setContentHandler(new ContentHandler() {
 @Override
 public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
 if ("root".equals(localName)) {
 result.setResult(new ArrayList<Content>());
 }
 }
 });
 builder.parse(new StringReader(""));

 List<Content> expected = new ArrayList<Content>();
 List<Content> actual = result.getResult();

 Assert.assertEquals(expected.size(), actual.size());
 }
}