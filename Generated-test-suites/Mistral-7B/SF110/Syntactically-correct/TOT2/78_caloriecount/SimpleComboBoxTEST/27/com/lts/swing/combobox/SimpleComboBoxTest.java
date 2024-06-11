import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class SimpleComboBoxTest {
    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertSame(model, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        simpleComboBox.setModel(model);
        assertSame(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithValue() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        Object value = new Integer(1);
        model.setSelectedValue(value);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueNull() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        Object value = new Integer(1);
        model.setSelectedValue(value);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongNonPrimitiveValue() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        Object value = new String("test");
        model.setSelectedValue(value);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.getSelectedLong();
    }

    @Test
    public void testGetSelectedLongIntegerValue() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        Object value = new Integer(1);
        model.setSelectedValue(value);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongLongValue() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        Object value = new Long(1);
        model.setSelectedValue(value);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testEmptyModel() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testNullValue() {
        SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        model.setSelectedValue(null);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNull(simpleComboBox.getSelectedValue());
    }
}