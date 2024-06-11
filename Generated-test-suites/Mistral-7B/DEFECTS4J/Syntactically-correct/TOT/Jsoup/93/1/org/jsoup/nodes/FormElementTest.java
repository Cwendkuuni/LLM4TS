import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @org.junit.Test
    public void testFormElementCreation() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("id", "myForm");
        FormElement formElement = new FormElement(formTag, baseUri, attributes);

        assertEquals("form", formElement.tagName());
        assertEquals("myForm", formElement.attr("id"));
        assertTrue(formElement instanceof FormElement);
    }

    @org.junit.Test
    public void testGetFormControls() {
        String html = "<html><body><form id='myForm'><input type='text' name='name' /><input type='text' name='age' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        Elements formControls = formElement.elements();
        assertEquals(2, formControls.size());

        List<Class<?>> expectedTypes = Arrays.asList(String.class, Integer.class);
        for (int i = 0; i < formControls.size(); i++) {
            Element element = formControls.get(i);
            Class<?> expectedType = expectedTypes.get(i);
            if (expectedType == String.class) {
                assertTrue(element instanceof Elements);
                assertEquals(1, ((Elements) element).size());
                assertEquals("input", element.tagName());
                assertEquals("text", ((Element) element.get(0)).attr("type"));
            } else {
                assertTrue(element instanceof Element);
                assertEquals("input", element.tagName());
                assertEquals("name", element.attr("name"));
            }
        }
    }

    @org.junit.Test
    public void testAddElement() {
        String html = "<html><body><form id='myForm'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "newInput");

        FormElement formElementAfterAdd = formElement.addElement(inputElement);

        Elements formControlsAfterAdd = formElementAfterAdd.elements();
        assertEquals(1, formControlsAfterAdd.size());
        assertEquals("input", formControlsAfterAdd.get(0).tagName());
        assertEquals("text", formControlsAfterAdd.get(0).attr("type"));
        assertEquals("newInput", formControlsAfterAdd.get(0).attr("name"));
    }

    @org.junit.Test
    public void testRemoveElement() {
        String html = "<html><body><form id='myForm'><input type='text' name='name' /><input type='text' name='age' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        Element inputToRemove = formElement.selectFirst("input[name='age']");

        FormElement formElementAfterRemove = formElement.removeChild(inputToRemove);

        Elements formControlsAfterRemove = formElementAfterRemove.elements();
        assertEquals(1, formControlsAfterRemove.size());
        assertNotEquals("age", formControlsAfterRemove.get(0).attr("name"));
    }

    @org.junit.Test
    public void testSubmitForm() {
        String html = "<html><body><form id='myForm' action='http://example.com' method='post'><input type='text' name='name' value='John' /><input type='text' name='age' value='25' /></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        Connection connection = formElement.submit();

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(2, formData.size());
        List<Connection.KeyVal> expectedFormData = Arrays.asList(
                new Connection.KeyVal("name", "John"),
                new Connection.KeyVal("age", "25")
        );

        for (int i = 0; i < formData.size(); i++) {
            Connection.KeyVal keyVal = formData.get(i);
            Connection.KeyVal expectedKeyVal = expectedFormData.get(i);
            assertEquals(expectedKeyVal.key(), keyVal.key());
            assertEquals(expectedKeyVal.value(), keyVal.value());
        }
    }

    @org.junit.Test
    public void testEmptyFormData() {
        String html = "<html><body><form id='myForm' action='http://example.com' method='post'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        List<Connection.KeyVal> formData = formElement.formData();

        assertEquals(0, formData.size());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSubmitFormWithoutAction() {
        String html = "<html><body><form id='myForm' method='post'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = doc.selectFirst("form");

        formElement.submit();
    }
}