package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.SizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

 private JFrame frame;
 private JTailModel model;
 private JTailPanel panel;

 @Before
 public void setUp() {
 JTailLogger.init();
 this.model = Mockito.spy(new JTailModel());
 this.frame = new JFrame();
 this.panel = new JTailPanel(this.model);
 this.frame.add(this.panel);
 this.frame.pack();
 this.frame.setVisible(true);
 }

 @After
 public void tearDown() {
 this.frame.dispose();
 }

 @Test
 public void testInitialization() {
 assertEquals(0, this.panel.tableModel.getRowCount());
 assertNotNull(this.panel.verticalScrollBar);
 assertTrue(this.panel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateWithModelEventResize() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, 1, 1);
 this.model.notifyObservers(event);
 verify(this.panel, times(1)).update(this.model, event);
 }

 @Test
 public void testUpdateWithModelEventScrolled() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 1, 1, 1, 1, 1);
 this.model.notifyObservers(event);
 verify(this.panel, times(1)).update(this.model, event);
 }

 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
 this.model.setLineNumbers(lineNumbers);
 this.model.setContent(lines);
 this.panel.updateDataTable(lineNumbers, lines);
 assertArrayEquals(new String[]{"1", "Line 1"}, this.panel.tableModel.getRowData(0));
 assertArrayEquals(new String[]{"2", "Line 2"}, this.panel.tableModel.getRowData(1));
 assertArrayEquals(new String[]{"3", "Line 3"}, this.panel.tableModel.getRowData(2));
 assertArrayEquals(new String[]{"4", "Line 4"}, this.panel.tableModel.getRowData(3));
 assertArrayEquals(new String[]{"5", "Line 5"}, this.panel.tableModel.getRowData(4));
 }

 @Test
 public void testScrollBarAdjustmentController() {
 ScrollBarAdjustmentController controller = new ScrollBarAdjustmentController(this.panel, this.model);
 AdjustmentEvent event = new AdjustmentEvent(controller, AdjustmentEvent.ADJUSTING, 1, 1, 1);
 controller.valueChanged(event);
 this.model.setFirstLine(1);
 this.model.setNbDisplayedLines(5);
 this.model.setMaxLine(10);
 controller.valueChanged(event);
 assertEquals(5, this.model.getFirstLine());
 assertEquals(1, this.model.getNbDisplayedLines());
 assertEquals(10, this.model.getMaxLine());
 }

 @Test
 public void testSizeController() {
 SizeController controller = new SizeController(this.panel, this.model);
 ComponentEvent event = new ComponentEvent(this.panel, ComponentEvent.COMPONENT_RESIZED);
 controller.componentResized(event);
 this.model.setMinLine(1);
 this.model.setMaxLine(10);
 this.model.setNbDisplayedLines(5);
 controller.componentResized(event);
 assertEquals(1, this.model.getMinLine());
 assertEquals(5, this.model.getNbDisplayedLines());
 assertEquals(10, this.model.getMaxLine());
 }
}