package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private NodeIterator iterator;
    private Element element;
    private Document document;

    @Before
    public void setUp() throws Exception {
        document = context.mock(Document.class);
        element = context.mock(Element.class);
        context.checking(new Expectations() {{
            allowing(document).createElement("element");
            will(returnValue(element));

            allowing(element).getNamespaceURI(Mockito.anyString());
            will(returnValue("http://example.com"));

            allowing(element).getTagName();
            will(returnValue("element"));

            allowing(element).getAttributeNode("testAttr");
            will(returnValue(createMock(Attr.class)));

            allowing(element).getAttributeNodes();
            will(returnValue(createMock(NodeList.class)));

            allowing(element).getAttributes();
            will(returnValue(createMock(NamedNodeMap.class)));
        }});

        iterator = new DOMAttributeIterator(new DOMPoint(document, element), new QName("*"));
    }

    @After
    public void tearDown() {
        iterator = null;
        element = null;
        document = null;
        context.assertIsSsatisfied();
    }

    @Test
    public void testEmptyIterator() {
        iterator = new DOMAttributeIterator(new DOMPoint(document, element), null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyElement() {
        iterator = new DOMAttributeIterator(new DOMPoint(document, element), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSingleAttribute() {
        Attr attribute = context.mock(Attr.class);
        context.checking(new Expectations() {{
            allowing(element).getAttributeNode("testAttr");
            will(returnValue(attribute));
        }});

        iterator = new DOMAttributeIterator(new DOMPoint(document, element), new QName("testAttr"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        assertSame(attribute, nodePointer.getNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        Attr attr1 = context.mock(Attr.class);
        Attr attr2 = context.mock(Attr.class);
        context.checking(new Expectations() {{
            allowing(element).getAttributeNodes();
            will(returnValue(createMock(NodeList.class)));

            allowing(element.getAttributeNodes()).getLength();
            will(returnValue(2));

            allowing(element.getAttributeNodes().item(0));
            will(returnValue(attr1));

            allowing(element.getAttributeNodes().item(1));
            will(returnValue(attr2));
        }});

        iterator = new DOMAttributeIterator(new DOMPoint(document, element), new QName(""));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer1 = iterator.next();
        assertSame(attr1, nodePointer1.getNode());
        assertTrue(iterator.hasNext());
        NodePointer nodePointer2 = iterator.next();
        assertSame(attr2, nodePointer2.getNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithAttributesInDifferentNamespaces() {
        Attr attr1 = context.mock(Attr.class);
        Attr attr2 = context.mock(Attr.class);
        String ns1 = "http://namespace1.com";
        String ns2 = "http://namespace2.com";

        context.checking(new Expectations() {{
            allowing(element).getNamespaceURI("prefix1");
            will(returnValue(ns1));

            allowing(element).getNamespaceURI("prefix2");
            will(returnValue(ns2));

            allowing(element).getAttributeNodes();
            will(returnValue(createMock(NodeList.class)));

            allowing(element.getAttributeNodes()).getLength();
            will(returnValue(2));

            allowing(element.getAttributeNodes().item(0));
            will(returnValue(attr1));
            allowing(attr1).getNamespaceURI();
            will(returnValue(ns1));

            allowing(element.getAttributeNodes().item(1));
            will(returnValue(attr2));
            allowing(attr2).getNamespaceURI();
            will(returnValue(ns2));
        }});

        iterator = new DOMAttributeIterator(new DOMPoint(document, element), new QName("prefix1:testAttr"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer1 = iterator.next();
        assertSame(attr1, nodePointer1.getNode());
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPoint(document, element), new QName("prefix2:testAttr"));
        assertTrue(iterator.hasNext());
        NodePointer nodePointer2 = iterator.next();
        assertSame(attr2, nodePointer2.getNode());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSelfClosingElement() {
        ProcessingInstruction pi = context.mock(ProcessingInstruction.class);
        context.checking(new Expectations() {{
            allowing(element).getTagName();
            will(returnValue("img"));

            allowing(element).getAttributes();
            will(returnValue(createMock(NamedNodeMap.class)));

            allowing(element).getAttributeNodes();
            will(returnValue(createMock(NodeList.class)));

            allowing(element).getChildNodes();
            will(returnValue(createMock(NodeList.class)));

            allowing(element.getChildNodes()).getLength();
            will(returnValue(1));

            allowing(element.getChildNodes().item(0));
            will(returnValue(pi));
        }});

        iterator = new DOMAttributeIterator(new DOMPoint(document, element), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithCertainConditionUnmet() {
        Attr attr = context.mock(Attr.class);

        context.checking(new Expectations() {{
            allowing(parent.getNamespaceURI("prefix"));
            will(returnValue("http://example.com"));

            allowing(element).getAttributeNodes();
            will(returnValue(createMock(NodeList.class)));

            allowing(element.getAttributeNodes()).getLength();
            will(returnValue(1));

            allowing(element.getAttributeNodes().item(0));
            will(returnValue(attr));

            allowing(attr).getPrefix();
            will(returnValue("prefix2"));
        }});

        iterator = new DOMAttributeIterator(new DOMPoint(document, element), new QName("prefix:testAttr"));
        assertFalse(iterator.hasNext());
    }

}