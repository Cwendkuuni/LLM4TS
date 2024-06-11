package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testOutputDocument() {
        Document doc = new JDOMFactory().newDocument();
        doc.setRootElement(new Element("root"));
        Element root = doc.getRootElement();
        root.addContent(new Element("child1").setText("Child 1"));
        root.addContent(new Element("child2").setText("Child 2"));

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(doc, writer);
            String xmlString = writer.toString();
            assertEquals("<root><child1>Child 1</child1><child2>Child 2</child2></root>", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputDocType() {
        Document doc = new JDOMFactory().newDocument();
        DocType docType = doc.getRootElement().getDocument().getDocType();

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(docType, writer);
            String xmlString = writer.toString();
            assertEquals("<!DOCTYPE root [ <!ATTLIST root attr CDATA #IMPLIED> ]>", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputElement() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        doc.setRootElement(root);

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(root, writer);
            String xmlString = writer.toString();
            assertEquals("<root>Root Element</root>", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputElementContent() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        Element child = new Element("child").setText("Child");
        root.addContent(child);

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.outputElementContent(root, writer);
            String xmlString = writer.toString();
            assertEquals("<child>Child</child>", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputList() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        Element child1 = new Element("child1").setText("Child 1");
        Element child2 = new Element("child2").setText("Child 2");
        List<Element> children = new JDOMFactory().newList(child1, child2);
        root.addContent(children);

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(children, writer);
            String xmlString = writer.toString();
            assertEquals("<child1></child1><child2></child2>", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputCDATA() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        Element child = new Element("child");
        child.addContent(new CDATA("This is CDATA content"));
        root.addContent(child);

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(child, writer);
            String xmlString = writer.toString();
            assertEquals("<![CDATA[This is CDATA content]]>", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputText() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        Text text = new Text("Text content");
        root.addContent(text);

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(text, writer);
            String xmlString = writer.toString();
            assertEquals("Text content", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputComment() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        Comment comment = new Comment("This is a comment");
        root.addContent(comment);

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(comment, writer);
            String xmlString = writer.toString();
            assertEquals("<!--This is a comment-->", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputProcessingInstruction() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        ProcessingInstruction pi = new ProcessingInstruction("xml-data", "data");
        root.addContent(pi);

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(pi, writer);
            String xmlString = writer.toString();
            assertEquals("<?xml-data data?>", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputEntityRef() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        EntityRef entityRef = new EntityRef("entity1");
        root.addContent(entityRef);

        XMLOutputter xmlOutputter = new XMLOutputter();
        Writer writer = null;
        try {
            writer = new StringWriter();
            xmlOutputter.output(entityRef, writer);
            String xmlString = writer.toString();
            assertEquals("&entity1;", xmlString);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    fail("IOException should not be thrown");
                }
            }
        }
    }

    @Test
    public void testOutputElementContentString() {
        Document doc = new JDOMFactory().newDocument();
        Element root = new Element("root");
        root.setText("Root Element");
        Element child = new Element("child").setText("Child");
        root.addContent(child);

        XMLOutputter xmlOutputter = new XMLOutputter();
        String xmlString = xmlOutputter.outputElementContentString(child);
        assertEquals("<child>Child</child>", xmlString);
    }
}