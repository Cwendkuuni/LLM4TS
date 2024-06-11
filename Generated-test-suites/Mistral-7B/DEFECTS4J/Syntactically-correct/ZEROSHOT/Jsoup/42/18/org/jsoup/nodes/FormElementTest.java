package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class FormElementTest {

 private static final String TAG_NAME = "form";
 private static final String BASE_URI = "http://example.com";
 private static final String FORM_ACTION_URL = "http://example.com/submit";
 private static final String FORM_NAME = "myForm";
 private static final String FORM_ELEMENT_ID = "myFormElement";
 private static final String FORM_CONTROL_INPUT_NAME = "input";
 private static final String FORM_CONTROL_SELECT_NAME = "select";
 private static final String FORM_CONTROL_SELECT_OPTION_VALUE_1 = "Option 1";
 private static final String FORM_CONTROL_SELECT_OPTION_VALUE_2 = "Option 2";
 private static final String FORM_CONTROL_CHECKBOX_NAME = "checkbox";
 private static final String FORM_CONTROL_CHECKBOX_VALUE = "checkedValue";

 @Test
 public void testFormElementConstruction() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 assertNotNull(formElement);
 assertSame(TAG_NAME, formElement.tagName());
 assertEquals(BASE_URI, formElement.baseUri());
 assertNull(formElement.attr("action"));
 assertNull(formElement.attr("method"));
 assertNotNull(formElement.elements());
 }

 @Test
 public void testFormElementElements() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Element formControlInput = Mockito.mock(Element.class);
 Element formControlSelect = Mockito.mock(Element.class);
 Element formControlCheckbox = Mockito.mock(Element.class);

 formElement.addElement(formControlInput);
 formElement.addElement(formControlSelect);
 formElement.addElement(formControlCheckbox);

 List<Element> elements = formElement.elements();
 assertEquals(3, elements.size());
 assertSame(formControlInput, elements.get(0));
 assertSame(formControlSelect, elements.get(1));
 assertSame(formControlCheckbox, elements.get(2));
 }

 @Test
 public void testFormElementSubmit() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes().put("action", FORM_ACTION_URL));
 Connection connection = formElement.submit();

 assertNotNull(connection);
 assertEquals(FORM_ACTION_URL, connection.url());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormElementSubmitWithEmptyActionUrl() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 formElement.submit();
 }

 @Test
 public void testFormElementFormDataEmptyForm() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(0, formData.size());
 }

 @Test
 public void testFormElementFormDataWithSingleInput() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Element formControlInput = Mockito.mock(Element.class);
 formControlInput.attr("name", FORM_CONTROL_INPUT_NAME);
 formControlInput.attr("value", "inputValue");
 formElement.addElement(formControlInput);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals(FORM_CONTROL_INPUT_NAME, keyVal.key());
 assertEquals("inputValue", keyVal.val());
 }

 @Test
 public void testFormElementFormDataWithSingleSelect() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Element formControlSelect = Mockito.mock(Element.class);
 formControlSelect.attr("name", FORM_CONTROL_SELECT_NAME);
 Element formControlSelectOptionValue1 = Mockito.mock(Element.class);
 formControlSelectOptionValue1.attr("value", FORM_CONTROL_SELECT_OPTION_VALUE_1);
 formControlSelect.add(formControlSelectOptionValue1);
 formElement.addElement(formControlSelect);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals(FORM_CONTROL_SELECT_NAME, keyVal.key());
 assertEquals(FORM_CONTROL_SELECT_OPTION_VALUE_1, keyVal.val());
 }

 @Test
 public void testFormElementFormDataWithSingleSelectAndSelectedOption() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Element formControlSelect = Mockito.mock(Element.class);
 formControlSelect.attr("name", FORM_CONTROL_SELECT_NAME);
 Element formControlSelectOptionValue1 = Mockito.mock(Element.class);
 formControlSelectOptionValue1.attr("value", FORM_CONTROL_SELECT_OPTION_VALUE_1);
 formControlSelect.add(formControlSelectOptionValue1);
 formElement.addElement(formControlSelect);
 formControlSelectOptionValue1.mockReturnValue(Mockito.mock(Element.class));
 formControlSelectOptionValue1.thenReturn(formControlSelectOptionValue2);
 formControlSelect.thenReturn(formControlSelect);
 formControlSelect.thenReturn(formControlSelect);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals(FORM_CONTROL_SELECT_NAME, keyVal.key());
 assertEquals(FORM_CONTROL_SELECT_OPTION_VALUE_1, keyVal.val());
 }

 @Test
 public void testFormElementFormDataWithSingleCheckbox() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Element formControlCheckbox = Mockito.mock(Element.class);
 formControlCheckbox.attr("name", FORM_CONTROL_CHECKBOX_NAME);
 formControlCheckbox.attr("checked", "checked");
 formControlCheckbox.attr("value", FORM_CONTROL_CHECKBOX_VALUE);
 formElement.addElement(formControlCheckbox);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(1, formData.size());
 Connection.KeyVal keyVal = formData.get(0);
 assertEquals(FORM_CONTROL_CHECKBOX_NAME, keyVal.key());
 assertEquals(FORM_CONTROL_CHECKBOX_VALUE, keyVal.val());
 }

 @Test
 public void testFormElementFormDataWithMultipleControls() {
 FormElement formElement = new FormElement(new Tag(TAG_NAME), BASE_URI, new Attributes());
 Element formControlInput = Mockito.mock(Element.class);
 formControlInput.attr("name", FORM_CONTROL_INPUT_NAME);
 formControlInput.attr("value", "inputValue");
 formElement.addElement(formControlInput);

 Element formControlSelect = Mockito.mock(Element.class);
 formControlSelect.attr("name", FORM_CONTROL_SELECT_NAME);
 Element formControlSelectOptionValue1 = Mockito.mock(Element.class);
 formControlSelectOptionValue1.attr("value", FORM_CONTROL_SELECT_OPTION_VALUE_1);
 formControlSelect.add(formControlSelectOptionValue1);
 formElement.addElement(formControlSelect);

 Element formControlCheckbox = Mockito.mock(Element.class);
 formControlCheckbox.attr("name", FORM_CONTROL_CHECKBOX_NAME);
 formControlCheckbox.attr("checked", "checked");
 formControlCheckbox.attr("value", FORM_CONTROL_CHECKBOX_VALUE);
 formElement.addElement(formControlCheckbox);

 List<Connection.KeyVal> formData = formElement.formData();
 assertEquals(3, formData.size());
 Connection.KeyVal inputKeyVal = formData.get(0);
 assertEquals(FORM_CONTROL_INPUT_NAME, inputKeyVal.key());
 assertEquals("inputValue", inputKeyVal.val());

 Connection.KeyVal selectKeyVal1 = formData.get(1);
 assertEquals(FORM_CONTROL_SELECT_NAME, selectKeyVal1.key());
 assertEquals(FORM_CONTROL_SELECT_OPTION_VALUE_1, selectKeyVal1.val());

 Connection.KeyVal checkboxKeyVal = formData.get(2);
 assertEquals(FORM_CONTROL_CHECKBOX_NAME, checkboxKeyVal.key());
 assertEquals(FORM_CONTROL_CHECKBOX_VALUE, checkboxKeyVal.val());
 }
}