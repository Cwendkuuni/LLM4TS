package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {
    private JTailModel model;
    private JTailPanel jTailPanel;

    @Before
    public void setUp() {
        model = mock(JTailModel.class);
        jTailPanel = new JTailPanel(model);
    }

    @Test
    public void testJTailPanelConstruction() {
        assertEquals(model, jTailPanel.getModel());
        // Add more assertions as needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJTailPanelConstructionWithNullModel() {
        new JTailPanel(null);
    }

    @Test
    public void testUpdateWithNullObservable() {
        final JTailPanel jTailPanel = new JTailPanel(mock(JTailModel.class));
        jTailPanel.update(null, new Object());
    }

    @Test
    public void testUpdateWithWrongObservableType() {
        final JTailPanel jTailPanel = new JTailPanel(mock(JTailModel.class));
        jTailPanel.update(new Object(), new Object());
    }

    @Test
    public void testUpdateDataTableWithNullLineNumbersAndLines() {
        final JTailPanel jTailPanel = new JTailPanel(mock(JTailModel.class));
        jTailPanel.updateDataTable(null, null);
    }

    @Test
    public void testUpdateDataTableWithLargeStrings() {
        final JTailPanel jTailPanel = new JTailPanel(mock(JTailModel.class));
        final String lineNumbers = new String(new char[1024 * 1024]);
        final String lines = new String(new char[1024 * 1024]);
        jTailPanel.updateDataTable(lineNumbers, lines);
    }
}