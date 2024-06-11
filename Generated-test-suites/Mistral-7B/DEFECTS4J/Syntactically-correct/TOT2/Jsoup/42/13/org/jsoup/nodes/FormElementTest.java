import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.FormElement;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        // Check if the FormElement instance was created correctly
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testEmptyFormElement() {
        Tag tag = new Tag("form");
        String baseUri = "";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        // Check if the FormElement instance was created correctly even when the baseUri is empty
        assertEquals("form", formElement.tagName());
        assertEquals("", formElement.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Tag tag = new Tag("form");
        String baseUri = null;
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElementsMethod() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element(new Tag("input"));
        formElement.addElement(inputElement);
        Elements elements = formElement.elements();
        // Check if the Elements returned by the elements() method contain the expected elements
        assertEquals(1, elements.size());
        assertEquals(inputElement, elements.get(0));
    }

    @Test
    public void testAddElementMethod() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element(new Tag("input"));
        formElement.addElement(inputElement);
        // Check if the inputElement was added to the FormElement
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(inputElement, elements.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMalformedFormControlElement() {
        Tag formTag = new Tag("form");
        Tag inputTag = new Tag("input");
        inputTag.attr("name", "");
        FormElement formElement = new FormElement(formTag, "", new Attributes());
        Element inputElement = new Element(inputTag);
        formElement.addElement(inputElement);
        Connection connection = formElement.submit();
    }

    @Test
    public void testEmptyFormData() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        List<Connection.KeyVal> formData = formElement.formData();
        // Check if an empty List is returned when there are no form control elements
        assertEquals(0, formData.size());
    }

    @Test
    public void testSubmitMethod() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element(new Tag("input", new Attributes().put("name", "username").put("type", "text")));
        formElement.addElement(inputElement);
        Connection connection = formElement.submit();
        // Check if the Connection object was created correctly
        assertEquals(Connection.Method.GET, connection.method());
        assertNotNull(connection.url());
        assertTrue(connection.timeout(5, TimeUnit.SECONDS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormSubmitWithInvalidAction() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element(new Tag("input", new Attributes().put("name", "username").put("type", "text")));
        formElement.addElement(inputElement);
        formElement.submit().execute(); // This will throw an IllegalArgumentException if the action URL is invalid
    }

    @Test
    public void testFormDataMethod() {
        Tag formTag = new Tag("form");
        String baseUri = "https://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element(new Tag("input", new Attributes().put("name", "username").put("type", "text")));
        Element textAreaElement = new Element(new Tag("textarea", new Attributes().put("name", "message").put("rows", "5")));
        formElement.addElement(inputElement);
        formElement.addElement(textAreaElement);
        List<Connection.KeyVal> formData = formElement.formData();
        // Check if the List returned by the formData() method contains the expected key-value pairs
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("", formData.get(0).val());
        assertEquals("message", formData.get(1).key());
        assertEquals("", formData.get(1).val());
    }

    @Test
    public void testEqualsMethod() {
        Tag formTag1 = new Tag("form");
        String baseUri1 = "https://example.com";
        Attributes attributes1 = new Attributes();
        FormElement formElement1 = new FormElement(formTag1, baseUri1, attributes1);
        Tag formTag2 = new Tag("form");
        String baseUri2 = "https://example.com";
        Attributes attributes2 = new Attributes();
        FormElement formElement2 = new FormElement(formTag2, baseUri2, attributes2);
        // Check if the equals() method returns true for identical FormElements
        assertTrue(formElement1.equals(formElement1));
        assertTrue(formElement1.equals(formElement2));
        // Check if the equals() method returns false for different FormElements
        assertFalse(formElement1.equals(new Object()));
        assertFalse(formElement1.equals(formElement2));
    }
}