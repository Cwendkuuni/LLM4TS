import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.FormElement;
import org.jsoup.helper.Elements;
import static org.junit.Assert.*;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertNotNull(formElement);
    }

    @Test
    public void testEmptyElements() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Elements emptyElements = formElement.elements();
        assertTrue(emptyElements.isEmpty());
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element inputElement = new Element("input");
        FormElement formElementWithInput = formElement.addElement(inputElement);
        Elements elements = formElementWithInput.elements();
        assertTrue(elements.size() > 0);
        assertEquals(inputElement, elements.get(0));
    }

    @Test
    public void testRemoveElement() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Element inputElement = new Element("input");
        FormElement formElementWithInput = formElement.addElement(inputElement);
        FormElement formElementWithoutInput = formElementWithInput.removeChild(inputElement);
        Elements elements = formElementWithoutInput.elements();
        assertTrue(elements.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithInvalidActionUrl() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        Connection connection = formElement.submit();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testSubmitValidConnection() {
        FormElement formElement = new FormElement(new Tag("form"), "http://example.com/submit", new Attributes());
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormDataEmptyList() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertTrue(formData.isEmpty());
    }

    @Test
    public void testFormDataTextInput() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("name", "textInput"));
        Element textInput = new Element("input").attr("name", "textInput").attr("value", "testValue");
        formElement.addElement(textInput);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("textInput", keyVal.key());
        assertEquals("testValue", keyVal.value());
    }

    @Test
    public void testFormDataSelectInput() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("name", "selectInput"));
        Element selectElement = new Element("select");
        Elements options = new Elements();
        options.add(new Element("option").val("option1").text("Option 1"));
        options.add(new Element("option").val("option2").text("Option 2"));
        selectElement.appendText("Select an option");
        selectElement.attr("name", "selectInput");
        formElement.addElement(selectElement);
        Element selectedOption = options.get(0);
        selectElement.appendElement(selectedOption);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("selectInput", keyVal.key());
        assertEquals("option1", keyVal.value());
    }

    @Test
    public void testFormDataCheckboxInput() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("name", "checkboxInput"));
        Element checkboxElement = new Element("input").attr("name", "checkboxInput").attr("type", "checkbox");
        FormElement formElementWithCheckedCheckbox = formElement.addElement(checkboxElement).addElement(new Element("input").attr("name", "checkboxInput").attr("type", "checkbox").attr("checked", "checked"));
        List<Connection.KeyVal> formData = formElementWithCheckedCheckbox.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("checkboxInput", keyVal.key());
        assertEquals("on", keyVal.value());
    }

    @Test
    public void testFormDataRadioInput() {
        FormElement formElement = new FormElement(new Tag("form"), "", new Attributes().put("name", "radioInput"));
        Element radioElement1 = new Element("input").attr("name", "radioInput").attr("type", "radio").attr("value", "value1");
        Element radioElement2 = new Element("input").attr("name", "radioInput").attr("type", "radio").attr("value", "value2");
        formElement.addElement(radioElement1);
        formElement.addElement(radioElement2);
        Element selectedRadioElement = radioElement1;
        formElement.addElement(selectedRadioElement);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("radioInput", keyVal.key());
        assertEquals("value1", keyVal.value());
    }
}

//