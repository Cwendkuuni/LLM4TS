package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

@org.junit.Test
public void testGetSelectedValue() {
// Given
SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
Integer expectedValue = 1;

// When
comboBox.setSelectedValue(expectedValue);
Object actualValue = comboBox.getSelectedValue();

// Then
assertEquals(expectedValue, actualValue);
}

@org.junit.Test
public void testGetSelectedInt() {
// Given
SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
Integer integerValue = 1;
comboBox.setSelectedValue(integerValue);

// When
int actualValue = comboBox.getSelectedInt();

// Then
assertEquals(integerValue, actualValue);

// Test edge case with a Long value
Long longValue = 1L;
comboBox.setSelectedValue(longValue);
actualValue = comboBox.getSelectedInt();
assertEquals(1, actualValue); // Should be autoboxed to an Integer

// Test error case with a non-numeric value
String stringValue = "Test";
comboBox.setSelectedValue(stringValue);
try {
actualValue = comboBox.getSelectedInt();
fail("Expected IllegalArgumentException");
} catch (IllegalArgumentException e) {
// Pass
}
}

@org.junit.Test
public void testGetSelectedLong() {
// Given
SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
Long longValue = 1L;
comboBox.setSelectedValue(longValue);

// When
long actualValue = comboBox.getSelectedLong();

// Then
assertEquals(longValue, actualValue);
}

@org.junit.Test
public void testSetSelectedValue() {
// Given
SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
Integer integerValue = 1;

// When
comboBox.setSelectedValue(integerValue);

// Then
Object selectedValue = comboBox.getSelectedValue();
assertEquals(integerValue, selectedValue);
}

@org.junit.Test
public void testSetSelectedInt() {
// Given
SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
int integerValue = 1;

// When
comboBox.setSelectedInt(integerValue);

// Then
Object selectedValue = comboBox.getSelectedValue();
if (selectedValue instanceof Integer) {
assertEquals(integerValue, ((Integer)selectedValue).intValue());
} else if (selectedValue instanceof Long) {
assertEquals(integerValue, ((Long)selectedValue).longValue());
} else if (selectedValue instanceof Short) {
assertEquals(integerValue, ((Short)selectedValue).shortValue());
} else if (selectedValue instanceof Byte) {
assertEquals(integerValue, ((Byte)selectedValue).byteValue());
} else {
fail("Expected an Integer, Long, Short, or Byte, but got: " + selectedValue.getClass());
}
}

@org.junit.Test
public void testSetSelectedValueWithNullModel() {
// Given
SimpleComboBox comboBox = new SimpleComboBox(null);

// When
Integer integerValue = 1;
comboBox.setSelectedValue(integerValue);

// Then
assertNull(comboBox.getSelectedValue());
}

@org.junit.Test
public void testSetSelectedValueWithNonSimpleModel() {
// Given
SimpleComboBox comboBox = new SimpleComboBox(new JComboBoxModel());

// When
Integer integerValue = 1;
comboBox.setSelectedValue(integerValue);

// Then
assertNull(comboBox.getSelectedValue());
}

// Add test cases for other methods and error scenarios here

}