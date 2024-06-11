import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstructor() {
        Element element = new Element("root");
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
    }
}