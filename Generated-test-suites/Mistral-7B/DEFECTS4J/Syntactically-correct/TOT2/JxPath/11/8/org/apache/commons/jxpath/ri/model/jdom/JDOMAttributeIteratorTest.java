import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMElement;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorInstance() {
        Element element = new JDOMElement("root", new Namespace("test", "test"));
        element.addAttribute(new Attribute("testAttr", "testValue", "test"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName("test:testAttr"));
        assertEquals(1, iterator.getPosition());
    }
}