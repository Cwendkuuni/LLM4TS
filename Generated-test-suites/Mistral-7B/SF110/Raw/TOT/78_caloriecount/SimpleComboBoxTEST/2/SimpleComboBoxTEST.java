import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class SimpleComboBoxTest {

 @Test
 public void testSimpleComboBoxConstruction() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 ComboBoxModel newModel = new SimpleComboBoxModel();
 simpleComboBox.setModel(newModel);
 assertNotNull(simpleComboBox.mySimpleModel);
 assertSame(newModel, simpleComboBox.mySimpleModel);
 }

 @Test
 public void testGetSelectedValueNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueStringValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement("One"); addElement("Two"); addElement("Three"); }});
 simpleComboBox.setSelectedIndex(0);
 assertEquals("One", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueIntegerValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement(1); addElement(2); addElement(3); }});
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedValueLongValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement(1L); addElement(2L); addElement(3L); }});
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedValueShortValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement((short)1); addElement((short)2); addElement((short)3); }});
 simpleComboBox.setSelectedIndex(1);
 assertEquals((short)2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedValueByteValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement((byte)1); addElement((byte)2); addElement((byte)3); }});
 simpleComboBox.setSelectedIndex(1);
 assertEquals((byte)2, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueWrongType() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement("One"); addElement("Two"); addElement("Three"); }});
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.getSelectedLong();
 }

 @Test
 public void testSetSelectedValueNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setSelectedValue(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueStringValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement("One"); addElement("Two"); addElement("Three"); }});
 simpleComboBox.setSelectedValue("One");
 assertEquals("One", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueIntegerValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement(1); addElement(2); addElement(3); }});
 simpleComboBox.setSelectedValue(2);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedValueLongValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement(1L); addElement(2L); addElement(3L); }});
 simpleComboBox.setSelectedValue(2L);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testSetSelectedValueShortValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement((short)1); addElement((short)2); addElement((short)3); }});
 simpleComboBox.setSelectedValue((short)2);
 assertEquals((short)2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedValueByteValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement((byte)1); addElement((byte)2); addElement((byte)3); }});
 simpleComboBox.setSelectedValue((byte)2);
 assertEquals((byte)2, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWrongType() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
 simpleComboBox.setModel(new DefaultComboBoxModel() {{ addElement("One"); addElement("Two"); addElement("Three"); }});
 simpleComboBox.setSelectedValue(3);
 }
}