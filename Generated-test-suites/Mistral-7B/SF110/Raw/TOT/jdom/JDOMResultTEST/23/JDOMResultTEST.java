import org.jdom2.*;
import org.jdom2.transform.JDOMResult;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

    @Test
    public void testEmptyResult() throws SAXException, IOException {
        JDOMResult result = new JDOMResult();
        String xml = "<root></root>";
        SAXBuilder builder = new SAXBuilder();
        builder.setEntityResolver(new DefaultEntityResolver());
        InputSource inputSource = new InputSource(new StringReader(xml));
        builder.build(inputSource);
        result.setHandler(builder.getContentHandler());
        List<Content> actualResult = result.getResult();
        List<Content> expectedResult = new ArrayList<>();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSimpleResult() throws SAXException, IOException {
        JDOMResult result = new JDOMResult();
        String xml = "<root><element>Hello World</element></root>";
        SAXBuilder builder = new SAXBuilder();
        builder.setEntityResolver(new DefaultEntityResolver());
        InputSource inputSource = new InputSource(new StringReader(xml));
        builder.build(inputSource);
        result.setHandler(builder.getContentHandler());
        List<Content> actualResult = result.getResult();
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(new Element("root"));
        expectedResult.add(new Element("element").setText("Hello World"));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMultipleElements() throws SAXException, IOException {
        JDOMResult result = new JDOMResult();
        String xml = "<root><element1><subElement1>Data1</subElement1></element1><element2><subElement2>Data2</subElement2></element2></root>";
        SAXBuilder builder = new SAXBuilder();
        builder.setEntityResolver(new DefaultEntityResolver());
        InputSource inputSource = new InputSource(new StringReader(xml));
        builder.build(inputSource);
        result.setHandler(builder.getContentHandler());
        List<Content> actualResult = result.getResult();
        List<Content> expectedResult = new ArrayList<>();
        expectedResult.add(new Element("root"));
        expectedResult.add(new Element("element1").addContent(new Element("subElement1").setText("Data1")));
        expectedResult.add(new Element("element2").addContent(new Element("subElement2").setText("Data2")));
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = RuntimeException.class)
    public void testNullDocument() {
        JDOMResult result = new JDOMResult();
        result.setDocument(null);
        result.getDocument();
    }

    @Test
    public void testFactorySetter() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        Assert.assertEquals(factory, result.getFactory());
    }

    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        content.add(new Text("Hello World"));
        result.setResult(content);
        List<Content> actualResult = result.getResult();
        Assert.assertEquals(content, actualResult);
    }

    @Test
    public void testSetDocument() throws SAXException, IOException {
        JDOMResult result = new JDOMResult();
        String xml = "<root><element>Hello World</element></root>";
        SAXBuilder builder = new SAXBuilder();
        builder.setEntityResolver(new DefaultEntityResolver());
        InputSource inputSource = new InputSource(new StringReader(xml));
        Document document = builder.build(inputSource);
        result.setDocument(document);
        Document actualResult = result.getDocument();
        Assert.assertEquals(document, actualResult);
    }
}