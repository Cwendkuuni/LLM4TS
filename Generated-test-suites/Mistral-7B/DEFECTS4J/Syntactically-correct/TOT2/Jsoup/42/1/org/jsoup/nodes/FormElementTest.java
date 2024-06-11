import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Elements;
import org.jsoup.nodes.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        Assert.assertEquals(tag.tagName(), formElement.tagName());
        Assert.assertEquals(baseUri, formElement.baseUri());
        Assert.assertNotNull(formElement.elements());
        Assert.assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testElementsMethod() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Elements elements = new Elements();
        formElement.elements = elements;
        Assert.assertEquals(elements, formElement.elements());
    }

    @Test
    public void testAddElementMethod() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element element = new Element("input");
        FormElement newFormElement = formElement.addElement(element);
        Assert.assertEquals(newFormElement, formElement.addElement(element));
        Assert.assertEquals(1, formElement.elements().size());
    }

    @Test
    public void testAddNullElementMethod() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Assert.assertNull(formElement.addElement(null));
        Assert.assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmitMethod() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Connection connection = formElement.submit();
        Assert.assertNotNull(connection);
        Assert.assertEquals(FormElement.class, formElement.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodNoActionUrl() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.removeAttr("action");
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodInvalidMethod() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.attr("method", "INVALID_METHOD");
        formElement.submit();
    }

    @Test
    public void testFormDataMethod() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.add(new Element("input", new Attributes().put("name", "exampleName").put("value", "exampleValue")));
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(1, formData.size());
        Assert.assertEquals("exampleName", formData.get(0).key());
        Assert.assertEquals("exampleValue", formData.get(0).value());
    }

    @Test
    public void testFormDataMethodEmptyList() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataMethodWithMultipleElements() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        formElement.add(new Element("input", new Attributes().put("name", "exampleName1").put("value", "exampleValue1")));
        formElement.add(new Element("input", new Attributes().put("name", "exampleName2").put("value", "exampleValue2")));
        List<Connection.KeyVal> formData = formElement.formData();
        Assert.assertEquals("exampleName1", formData.get(0).key());
        Assert.assertEquals("exampleValue1", formData.get(0).value());
        Assert.assertEquals("exampleName2", formData.get(1).key());
        Assert.assertEquals("exampleValue2", formData.get(1).value());
    }

    @Test
    public void testEqualsMethod() {
        FormElement formElement1 = new FormElement(new Tag("form"), "", new Attributes());
        FormElement formElement2 = new FormElement(new Tag("form"), "", new Attributes());
        Object anotherObject = new Object();
        FormElement formElement3 = formElement1;
        Assert.assertTrue(formElement1.equals(formElement1));
        Assert.assertTrue(formElement1.equals(formElement3));
        Assert.assertFalse(formElement1.equals(formElement2));
        Assert.assertFalse(formElement1.equals(anotherObject));
    }

    @Test
    public void testHashCodeMethod() {
        FormElement formElement1 = new FormElement(new Tag("form"), "", new Attributes());
        FormElement formElement2 = new FormElement(new Tag("form"), "", new Attributes());
        Object anotherObject = new Object();
        FormElement formElement3 = formElement1;
        Assert.assertEquals(formElement1.hashCode(), formElement1.hashCode());
        Assert.assertEquals(formElement1.hashCode(), formElement3.hashCode());
        Assert.assertNotEquals(formElement1.hashCode(), formElement2.hashCode());
        Assert.assertNotEquals(formElement1.hashCode(), anotherObject.hashCode());
    }

    @Test
    public void testToStringMethod() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Assert.assertEquals("FormElement[form]\n" +
                "  tag: form\n" +
                "  baseUri: <empty>\n" +
                "  attributes: {}", formElement.toString());
    }
}