import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.SAXOutputter;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

public class SAXOutputterTest {

 private SAXOutputter saxOutputter;
 private Document document;

 @Before
 public void setup() {
 saxOutputter = new SAXOutputter();
 document = new Document("root");
 document.getRootElement().addContent(new Element("element1").setText("content1"));
 document.getRootElement().addContent(new Element("element2").setText("content2"));
 }

 @Test
 public void testOutputDocument() throws JDOMException, SAXException {
 SAXTarget target = saxOutputter.buildTarget(document);
 saxOutputter.output(document);
 // You can verify the generated XML here using Assert.assertEquals or any other assertion library
 }

 @Test
 public void testOutputNullDocument() throws JDOMException, SAXException {
 saxOutputter.output(null);
 // Verify that no exception was thrown or handle the expected exception
 }

 @Test
 public void testOutputList() throws JDOMException, SAXException {
 saxOutputter.output(document.getContent());
 // You can verify the generated XML here using Assert.assertEquals or any other assertion library
 }

 @Test
 public void testOutputElement() throws JDOMException, SAXException {
 saxOutputter.output(document.getRootElement());
 // You can verify the generated XML here using Assert.assertEquals or any other assertion library
 }

 @Test
 public void testOutputFragmentList() throws JDOMException, SAXException {
 SAXTarget target = saxOutputter.buildTarget(null);
 saxOutputter.outputFragment(document.getContent());
 // Verify the generated XML fragment here using Assert.assertEquals or any other assertion library
 }

 @Test
 public void testOutputFragmentElement() throws JDOMException, SAXException {
 SAXTarget target = saxOutputter.buildTarget(null);
 saxOutputter.outputFragment(document.getRootElement());
 // Verify the generated XML fragment here using Assert.assertEquals or any other assertion library
 }

 @Test
 public void testOutputFragmentContent() throws JDOMException, SAXException {
 SAXTarget target = saxOutputter.buildTarget(null);
 saxOutputter.outputFragment(new Text("Test content"));
 // Verify the generated XML fragment here using Assert.assertEquals or any other assertion library
 }

 @Test(expected = JDOMException.class)
 public void testOutputFragmentNullContent() throws JDOMException, SAXException {
 SAXTarget target = saxOutputter.buildTarget(null);
 saxOutputter.outputFragment(null);
 }
}