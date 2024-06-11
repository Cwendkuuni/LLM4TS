import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

    

    @org.junit.Test
    public void testGetSelectedValue_WithNullModel_ReturnsNull() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @org.junit.Test
    public void testGetSelectedValue_WithStringValue_ReturnsSelectedString() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option 1");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Option 1", simpleComboBox.getSelectedValue());
    }

    @org.junit.Test
    public void testGetSelectedValue_WithIntegerValue_ReturnsSelectedInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedValue());
    }

    @org.junit.Test
    public void testGetSelectedValue_WithLongValue_ReturnsSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Long(1L));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1L, simpleComboBox.getSelectedValue());
    }

    @org.junit.Test
    public void testGetSelectedValue_WithShortValue_ReturnsSelectedShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Short((short) 1));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals((short) 1, simpleComboBox.getSelectedValue());
    }

    @org.junit.Test
    public void testGetSelectedValue_WithByteValue_ReturnsSelectedByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Byte((byte) 1));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals((byte) 1, simpleComboBox.getSelectedValue());
    }

    
}