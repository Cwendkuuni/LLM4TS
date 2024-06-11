import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class SimpleComboBoxTest {

 private SimpleComboBox myComboBox;
 private SimpleComboBoxModel myModel;

 @Test
 public void testConstructor() {
 myModel = new SimpleComboBoxModel();
 myComboBox = new SimpleComboBox(myModel);
 assertSame(myModel, myComboBox.getModel());
 }

 @Test
 public void testSetModel() {
 myModel = new SimpleComboBoxModel();
 myComboBox = new SimpleComboBox(null);
 myComboBox.setModel(myModel);
 assertSame(myModel, myComboBox.getModel());

 SimpleComboBoxModel wrongModel = new ComboBoxModel() {};
 myComboBox.setModel(wrongModel);
 assertFalse(myComboBox.getModel() instanceof SimpleComboBoxModel);
 }

 @Test
 public void testGetSelectedValue() {
 myModel.addElement("One");
 myModel.addElement("Two");
 myModel.setSelectedIndex(1);
 myComboBox = new SimpleComboBox(myModel);
 assertEquals("Two", myComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValue() {
 myModel.addElement("One");
 myModel.addElement("Two");
 myComboBox = new SimpleComboBox(myModel);
 myComboBox.setSelectedValue("Two");
 assertEquals("Two", myComboBox.getSelectedValue());

 myComboBox.setSelectedValue("Three");
 assertEquals("Two", myComboBox.getSelectedValue()); // Should not change selected value
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedInt_NullValue() {
 myComboBox = new SimpleComboBox(myModel);
 assertNull(myComboBox.getSelectedValue());
 assertFalse(myComboBox.getModel() instanceof SimpleComboBoxModel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedInt_NotANumber() {
 myModel.addElement("One");
 myModel.addElement("Two");
 myModel.setSelectedIndex(1);
 myComboBox = new SimpleComboBox(myModel);
 assertEquals("Two", myComboBox.getSelectedValue());
 myComboBox.setSelectedValue("Three");
 assertEquals(-1, myComboBox.getSelectedInt()); // Should return -1 for non-number values
 }

 @Test
 public void testGetSelectedInt_Byte() {
 myModel.addElement((byte)1);
 myModel.addElement((byte)2);
 myModel.setSelectedIndex(1);
 myComboBox = new SimpleComboBox(myModel);
 assertEquals(1, myComboBox.getSelectedInt()); // Byte is promoted to int during comparison
 }

 @Test
 public void testGetSelectedInt_Short() {
 myModel.addElement((short)1);
 myModel.addElement((short)2);
 myModel.setSelectedIndex(1);
 myComboBox = new SimpleComboBox(myModel);
 assertEquals(1, myComboBox.getSelectedInt()); // Short is promoted to int during comparison
 }

 @Test
 public void testGetSelectedInt_Integer() {
 myModel.addElement(1);
 myModel.addElement(2);
 myModel.setSelectedIndex(1);
 myComboBox = new SimpleComboBox(myModel);
 assertEquals(2, myComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedInt_Long() {
 myModel.addElement(1L);
 myModel.addElement(2L);
 myModel.setSelectedIndex(1);
 myComboBox = new SimpleComboBox(myModel);
 assertEquals(2, myComboBox.getSelectedInt()); // Long is promoted to int during comparison
 }

 @Test
 public void testGetSelectedLong() {
 myModel.addElement(1L);
 myModel.addElement(2L);
 myModel.setSelectedIndex(1);
 myComboBox = new SimpleComboBox(myModel);
 assertEquals(2L, myComboBox.getSelectedLong());
 }
}