import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 @Test
 public void testFormElementCreation() {
 // Given
 Tag tag = Mockito.mock(Tag.class);
 String baseUri = "http://test.com";
 Attributes attributes = new Attributes();

 // When
 FormElement formElement = new FormElement(tag, baseUri, attributes);

 // Then
 assertNotNull(formElement);
 assertEquals(tag, formElement.tag());
 assertEquals(baseUri, formElement.baseUri());
 assertEquals(attributes, formElement.attributes());
 }

 @Test
 public void testEmptyFormElement() {
 // Given
 FormElement formElement = new FormElement(Tag.FORM, "", new Attributes());

 // Then
 assertEquals(new Elements(), formElement.elements());
 }

 @Test
 public void testFormElementWithElements() {
 // Given
 Tag formTag = Mockito.mock(Tag.class);
 Tag inputTag = Mockito.mock(Tag.class);
 Element element = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 element.tag = inputTag;
 Elements elements = new Elements();
 elements.add(element);
 FormElement formElement = new FormElement(formTag, "", new Attributes(), elements);

 // Then
 assertEquals(elements, formElement.elements());
 }

 @Test
 public void testFormElementSubmit() {
 // Given
 String baseUri = "http://test.com";
 String action = "http://test.com/submit";
 Tag formTag = Mockito.mock(Tag.class);
 formTag.attr("action", action);
 FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

 // When
 Connection connection = formElement.submit();

 // Then
 assertNotNull(connection);
 assertEquals( Connection.Method.GET, connection.method() );
 assertEquals( baseUri + action, connection.url() );
 }

 @Test(expected = IllegalStateException.class)
 public void testFormElementSubmitWithoutActionAttribute() {
 // Given
 String baseUri = "http://test.com";
 Tag formTag = Mockito.mock(Tag.class);
 FormElement formElement = new FormElement(formTag, baseUri, new Attributes());

 // When & Then
 formElement.submit();
 }

 @Test
 public void testFormData() {
 // Given
 String baseUri = "http://test.com";
 String inputName = "inputName";
 String inputValue = "inputValue";
 String selectName = "selectName";
 String selectOptionValue1 = "option1";
 String selectOptionValue2 = "option2";
 String checkboxName = "checkboxName";
 String checkboxValue = "checkboxValue";

 Tag formTag = Mockito.mock(Tag.class);
 formTag.attr("action", baseUri);

 Tag inputTag = Mockito.mock(Tag.class);
 inputTag.tagName("input");
 inputTag.attr("name", inputName);
 inputTag.attr("type", "text");
 inputTag.attr("value", inputValue);
 Elements elements = new Elements();
 elements.add(inputTag);
 FormElement formElement = new FormElement(formTag, "", new Attributes(), elements);

 Tag selectTag = Mockito.mock(Tag.class);
 selectTag.tagName("select");
 selectTag.attr("name", selectName);
 Elements options = new Elements();
 Element option1 = new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 };
 option1.tag = Mockito.mock(Tag.class);
 option1.tag.val(selectOptionValue1);
 option1.tag.attr("value", selectOptionValue1);
 options.add(option1);
 Element option2 = new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 };
 option2.tag = Mockito.mock(Tag.class);
 option2.tag.val(selectOptionValue2);
 option2.tag.attr("value", selectOptionValue2);
 option2.tag.attr("selected", "selected");
 Elements selectOptions = new Elements();
 selectOptions.addAll(Arrays.asList(option1, option2));
 selectTag.select("option[selected]")[0] = option2;
 selectTag.tag = Mockito.mock(Tag.class);
 selectTag.tag.attr("multiple", "multiple");
 Elements selectElements = new Elements();
 selectElements.add(selectTag);
 FormElement formElementWithSelect = new FormElement(formTag, "", new Attributes(), selectElements);

 Tag checkboxTag = Mockito.mock(Tag.class);
 checkboxTag.tagName("input");
 checkboxTag.attr("name", checkboxName);
 checkboxTag.attr("type", "checkbox");
 checkboxTag.hasAttr("checked", "");
 Element checkboxElement = new Element("input") {
 @Override
 public String tagName() {
 return "input";
 }
 };
 checkboxElement.tag = checkboxTag;
 FormElement formElementWithCheckbox = new FormElement(formTag, "", new Attributes(), Arrays.asList(checkboxElement));

 // When
 List<Connection.KeyVal> formData1 = formElement.formData();
 List<Connection.KeyVal> formData2 = formElementWithSelect.formData();
 List<Connection.KeyVal> formData3 = formElementWithCheckbox.formData();

 // Then
 assertEquals(0, formData1.size());
 assertEquals(1, formData2.size());
 assertEquals(1, formData3.size());

 assertEquals(inputName, formData1.get(0).key);
 assertEquals(inputValue, formData1.get(0).val);

 assertEquals(selectName, formData2.get(0).key);
 assertEquals(selectOptionValue2, formData2.get(0).val);

 assertEquals(checkboxName, formData3.get(0).key);
 assertEquals(checkboxValue, formData3.get(0).val);
 }

 @Test
 public void testFormDataWithMultipleSelect() {
 // Given
 String baseUri = "http://test.com";
 String inputName = "inputName";
 String inputValue = "inputValue";
 String selectName = "selectName";
 String selectOptionValue1 = "option1";
 String selectOptionValue2 = "option2";
 String selectOptionValue3 = "option3";

 Tag formTag = Mockito.mock(Tag.class);
 formTag.attr("action", baseUri);

 Tag inputTag = Mockito.mock(Tag.class);
 inputTag.tagName("input");
 inputTag.attr("name", inputName);
 inputTag.attr("type", "text");
 inputTag.attr("value", inputValue);
 Elements elements = new Elements();
 elements.add(inputTag);
 FormElement formElement = new FormElement(formTag, "", new Attributes(), elements);

 Tag selectTag = Mockito.mock(Tag.class);
 selectTag.tagName("select");
 selectTag.attr("name", selectName);
 selectTag.attr("multiple", "multiple");
 Elements options = new Elements();
 Element option1 = new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 };
 option1.tag = Mockito.mock(Tag.class);
 option1.tag.val(selectOptionValue1);
 option1.tag.attr("value", selectOptionValue1);
 options.add(option1);
 Element option2 = new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 };
 option2.tag = Mockito.mock(Tag.class);
 option2.tag.val(selectOptionValue2);
 option2.tag.attr("value", selectOptionValue2);
 option2.tag.attr("selected", "selected");
 Element option3 = new Element("option") {
 @Override
 public String tagName() {
 return "option";
 }
 };
 option3.tag = Mockito.mock(Tag.class);
 option3.tag.val(selectOptionValue3);
 option3.tag.attr("value", selectOptionValue3);
 option3.tag.attr("selected", "selected");
 Elements selectOptions = new Elements();
 selectOptions.addAll(Arrays.asList(option1, option2, option3));
 selectTag.select("option[selected]")[0] = option2;
 selectTag.select("option[selected]")[1] = option3;
 selectTag.tag = Mockito.mock(Tag.class);
 Elements selectElements = new Elements();
 selectElements.add(selectTag);
 FormElement formElementWithSelect = new FormElement(formTag, "", new Attributes(), selectElements);

 // When
 List<Connection.KeyVal> formData = formElementWithSelect.formData();

 // Then
 assertEquals(2, formData.size());

 assertEquals(inputName, formData.get(0).key);
 assertEquals(inputValue, formData.get(0).val);

 assertEquals(selectName, formData.get(1).key);
 List<String> selectValues = new ArrayList<>();
 selectValues.add(selectOptionValue2);
 selectValues.add(selectOptionValue3);
 assertEquals(selectValues, formData.get(1).val);
 }
}