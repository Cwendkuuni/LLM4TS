import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {
    private JTailModel model;
    private JTailPanel panel;

    @Before
    public void setup() {
        model = mock(JTailModel.class);
        when(model.getLineNumbers()).thenReturn("1\n2\n3");
        when(model.getContent()).thenReturn("line1\nline2\nline3");
        when(model.addObserver(any())).thenAnswer(invocation -> invocation.getArgument(0));
        panel = new JTailPanel(model);
        panel.addObserver(any());
    }

    @Test
    public void testJTailPanelConstructor() {
        JTailLogger.debug("New JTailPanel created");
        assertEquals("Building a new JTailPanel", JTailLogger.getLastLogMessage());
    }

    @Test
    public void testUpdateJailModelEventTypeResized() {
        final TableModelListener tableModelListener = mock(TableModelListener.class);
        TableModelEvent tableModelEvent = new TableModelEvent(panel.tableModel, 0, 0, 3, TableModelEvent.ALL_COLUMNS, TableModelEvent.FIRST_ROW);
        panel.update(model, tableModelEvent);
        verify(tableModelListener).tableChanged(eq(tableModelEvent));
        assertEquals(3, panel.tableModel.getRowCount());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullObservable() {
        panel.update(null, new TableModelEvent(panel.tableModel, 0, 0, 3, TableModelEvent.ALL_COLUMNS, TableModelEvent.FIRST_ROW));
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullArg() {
        panel.update(model, null);
    }

    @Test
    public void testUpdateDataTableWithNullLineNumbers() {
        panel.updateDataTable(null, "line1\nline2\nline3");
        assertEquals(0, panel.tableModel.getRowCount());
    }

    @Test
    public void testUpdateDataTableWithNullLines() {
        panel.updateDataTable("1\n2\n3", null);
        assertEquals(3, panel.tableModel.getRowCount());
    }

    @Test
    public void testUpdateDataTableWithEmptyLines() {
        panel.updateDataTable("1\n2\n3", "");
        assertEquals(3, panel.tableModel.getRowCount());
    }

    @Test
    public void testUpdateDataTableWithLongerLines() {
        when(model.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
        panel.updateDataTable(model.getLineNumbers(), "line1\nline2\nline3\nline4\nline5\nline6");
        assertEquals(6, panel.tableModel.getRowCount());
    }

    @Test
    public void testVerticalScrollBarBehaviorWithMinLineFirstLineOne() {
        final BoundedRangeModel rangeModel = panel.verticalScrollBar.getModel();
        rangeModel.setValueIsAdjusting(true);
        rangeModel.setMinimum(0);
        rangeModel.setValue(0);
        rangeModel.setExtent(1);
        rangeModel.setMaximum(2);
        rangeModel.setValueIsAdjusting(false);
        assertEquals(0, panel.verticalScrollBar.getValue());
        assertEquals(false, panel.verticalScrollBar.isEnabled());
    }
}