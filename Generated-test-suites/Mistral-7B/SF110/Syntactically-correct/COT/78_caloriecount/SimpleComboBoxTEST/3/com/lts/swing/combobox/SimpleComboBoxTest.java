package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 // Test constructor
 @Test
 public void testSimpleComboBoxWithModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 }

 // Test setModel method
 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 ComboBoxModel anotherModel = new DefaultComboBoxModel();
 simpleComboBox.setModel(anotherModel);
 assertNotSame(simpleComboBox.mySimpleModel, anotherModel);
 }

 // Test getSelectedValue method with null model
 @Test(expected = NullPointerException.class)
 public void testGetSelectedValueWithNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 simpleComboBox.getSelectedValue();
 }

 // Test getSelectedValue method with integer value
 @Test
 public void testGetSelectedValueWithIntegerValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(new SimpleComboBoxModel(new Integer(1)));
 assertEquals(1, simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with long value
 @Test
 public void testGetSelectedValueWithLongValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(new SimpleComboBoxModel(new Long(2L)));
 assertEquals(2L, simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with short value
 @Test
 public void testGetSelectedValueWithShortValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(new SimpleComboBoxModel(new Short((short)3)));
 assertEquals(3, simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with byte value
 @Test
 public void testGetSelectedValueWithByteValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(new SimpleComboBoxModel(new Byte((byte)4)));
 assertEquals(4, simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with non-supported value
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueWithNonSupportedValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(new SimpleComboBoxModel(new String("test")));
 simpleComboBox.getSelectedValue();
 }

 // Test setSelectedValue method with null value
 @Test(expected = NullPointerException.class)
 public void testSetSelectedValueWithNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(null);
 }

 // Test setSelectedValue method with integer value
 @Test
 public void testSetSelectedValueWithIntegerValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(new Integer(5));
 assertEquals(5, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with long value
 @Test
 public void testSetSelectedValueWithLongValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(new Long(6L));
 assertEquals(6L, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with short value
 @Test
 public void testSetSelectedValueWithShortValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(new Short((short)7));
 assertEquals(7, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with byte value
 @Test
 public void testSetSelectedValueWithByteValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(new Byte((byte)8));
 assertEquals(8, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with non-supported value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithNonSupportedValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(new String("test"));
 }

 // Test getSelectedInt method with null value
 @Test
 public void testGetSelectedIntWithNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with integer value
 @Test
 public void testGetSelectedIntWithIntegerValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Integer(9)));
 assertEquals(9, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with long value
 @Test
 public void testGetSelectedIntWithLongValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Long(10L)));
 assertEquals(10, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with short value
 @Test
 public void testGetSelectedIntWithShortValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Short((short)11)));
 assertEquals(11, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with byte value
 @Test
 public void testGetSelectedIntWithByteValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Byte((byte)12)));
 assertEquals(12, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with non-supported value
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithNonSupportedValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new String("test")));
 simpleComboBox.getSelectedInt();
 }

 // Test getSelectedLong method with null value
 @Test
 public void testGetSelectedLongWithNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with integer value
 @Test
 public void testGetSelectedLongWithIntegerValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Integer(13)));
 assertEquals(13L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with long value
 @Test
 public void testGetSelectedLongWithLongValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Long(14L)));
 assertEquals(14L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with short value
 @Test
 public void testGetSelectedLongWithShortValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Short((short)15)));
 assertEquals(15L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with byte value
 @Test
 public void testGetSelectedLongWithByteValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Byte((byte)16)));
 assertEquals(16L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with non-supported value
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithNonSupportedValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new String("test")));
 simpleComboBox.getSelectedLong();
 }

}