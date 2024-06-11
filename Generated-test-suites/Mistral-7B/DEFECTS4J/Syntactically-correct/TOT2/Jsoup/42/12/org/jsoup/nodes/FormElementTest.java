import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {
    @Test
    public void testElements() {
        Element tag = new Element("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        Element element = new Element("input");
        formElement.addElement(element);

        Elements expectedElements = new Elements(element);
        Elements actualElements = formElement.elements();

        assertEquals(expectedElements, actualElements);
    }

    @Test
    public void testEmptiesFormData() {
        FormElement formElement = new FormElement(new Element("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        assertNull(formData);
    }

    @Test
    public void testNullBaseUri() {
        FormElement formElement = new FormElement(new Element("form"), null, new Attributes());
        assertNotNull(formElement.elements());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNullBaseUri() {
        FormElement formElement = new FormElement(new Element("form"), null, new Attributes());
        formElement.submit();
    }

    @Test
    public void testSubmitWithEmptyFormData() {
        FormElement formElement = new FormElement(new Element("form"), "", new Attributes());
        List<Connection.KeyVal> formData = formElement.formData();
        Connection connection = formElement.submit();
        connection.execute();
    }

    @Test
    public void testInvalidFormControlAttributes() {
        Element inputElement = new Element("input");
        inputElement.attr("name", "test");
        FormElement formElement = new FormElement(new Element("form"), "", new Attributes());
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNull(formData);
    }

    @Test
    public void testValidFormControlElements() {
        Element inputElement = new Element("input");
        inputElement.attr("name", "test");
        FormElement formElement = new FormElement(new Element("form"), "", new Attributes());
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(1, formData.size());
    }

    @Test
    public void testMultipleFormControlElements() {
        Element inputElement1 = new Element("input");
        inputElement1.attr("name", "test1");
        Element inputElement2 = new Element("input");
        inputElement2.attr("name", "test2");
        FormElement formElement = new FormElement(new Element("form"), "", new Attributes());
        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
    }
}