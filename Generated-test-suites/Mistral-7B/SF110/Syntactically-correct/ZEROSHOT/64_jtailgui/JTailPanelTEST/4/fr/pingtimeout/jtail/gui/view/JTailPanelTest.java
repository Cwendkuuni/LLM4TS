package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.TailSizeController;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel tailPanel;

 @Before
 public void setup() {
 model = Mockito.spy(new JTailModel());
 tailPanel = new JTailPanel(model);
 }

 @Test
 public void testConstructor() {
 assertNotNull(tailPanel.getModel());
 assertNotNull(tailPanel.getScrollPane());
 assertNotNull(tailPanel.getDataTable());
 assertNotNull(tailPanel.getTableModel());
 assertEquals(2, tailPanel.getTableModel().getColumnCount());
 }

 @Test
 public void testGetCharacterHeight() {
 assertEquals(tailPanel.getCharacterHeight(), 18); // Assuming a default font size
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull(tailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 JTailModelEvent event = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 1, 10, 10, 20, 30, 40, 50);
 tailPanel.update(model, event);
 TableModel tableModel = tailPanel.getDataTable().getModel();
 TableModelEvent tableModelEvent = new TableModelEvent(tableModel, 0, 1);
 Mockito.verify(model).addObserver(Mockito.any());
 Mockito.verify(model).notifyObservers(Mockito.anyObject(), Mockito.eq(event));
 assertEquals(tableModel.getRowCount(), 1);
 assertEquals(tableModel.getValueAt(0, 0).toString(), "1");
 assertEquals(tableModel.getValueAt(0, 1).toString(), "");
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "line1\nline2\nline3\nline4\nline5";
 tailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(tailPanel.getDataTable().getModel().getRowCount(), 5);
 }

 @Test
 public void testSetAutoResizeModeAndResizeColumns() {
 tailPanel.getDataTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
 tailPanel.getDataTable().setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
 TableColumn numbersColumn = tailPanel.getDataTable().getColumnModel().getColumn(0);
 TableColumn linesColumn = tailPanel.getDataTable().getColumnModel().getColumn(1);
 tailPanel.getDataTable().doLayout();
 assertEquals(numbersColumn.getPreferredWidth(), 12 * 10);
 assertEquals(linesColumn.getPreferredWidth(), tailPanel.getDataTable().getWidth() - 12 * 10);
 }

 @Test
 public void testScrollBarAdjustmentController() {
 ScrollBarAdjustmentController scrollBarAdjustmentController = Mockito.spy(new ScrollBarAdjustmentController(tailPanel, model));
 JScrollBar scrollBar = Mockito.mock(JScrollBar.class);
 scrollBar.addAdjustmentListener(scrollBarAdjustmentController);
 TableModel tableModel = Mockito.mock(TableModel.class);
 tailPanel.getDataTable().setModel(tableModel);
 TableModelEvent tableModelEvent = new TableModelEvent(tableModel, 0, 1);
 scrollBarAdjustmentController.valueChanged(new AdjustmentEvent() {
 @Override
 public int getValue() {
 return 0;
 }

 @Override
 public int getMaximum() {
 return 5;
 }

 @Override
 public int getMinimum() {
 return 0;
 }

 @Override
 public void setValue(int value) {

 }

 @Override
 public void addChangeListener(ChangeListener changeListener) {

 }

 @Override
 public void removeChangeListener(ChangeListener changeListener) {

 }
 });
 scrollBarAdjustmentController.valueChanged(tableModelEvent);
 Mockito.verify(model).notifyObservers(Mockito.anyObject(), Mockito.eq(new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED)));
 }

 @Test
 public void testTailSizeController() {
 TailSizeController tailSizeController = Mockito.spy(new TailSizeController(tailPanel, model));
 JFrame frame = Mockito.mock(JFrame.class);
 tailPanel.setParent(frame);
 tailSizeController.componentResized(tailPanel);
 Mockito.verify(model).notifyObservers(Mockito.anyObject(), Mockito.eq(new JTailModelEvent(model, JTailModelEvent.Type.RESIZED)));
 }
}