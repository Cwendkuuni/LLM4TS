package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import org.junit.*;
import org.mockito.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel panelUnderTest;

 @Before
 public void setUp() {
 JTailLogger.init();
 this.model = mock(JTailModel.class);
 this.panelUnderTest = new JTailPanel(this.model);
 }

 @Test
 public void testInitialization() {
 // Check that the JTailPanel has been initialized with the proper model
 assertEquals("Wrong model assigned to the JTailPanel", this.model, panelUnderTest.model);
 // Check that the JScrollPane and JScrollBar have been initialized
 assertNotNull("JScrollPane should not be null", panelUnderTest.scrollPane);
 assertNotNull("JScrollBar should not be null", panelUnderTest.verticalScrollBar);
 // Check that the table has been initialized with the proper model
 assertNotNull("DataTable should not be null", panelUnderTest.dataTable);
 // Check that the table has no rows at the beginning
 assertEquals("Wrong number of rows in the DataTable", 0, panelUnderTest.dataTable.getRowCount());
 }

 @Test
 public void testUpdateWithResizedWindow() {
 // Given : A resized window event
 final JTailModelEvent resizedEvent = mock(JTailModelEvent.class);
 when(resizedEvent.type).thenReturn(JTailModelEvent.Type.RESIZED);
 when(resizedEvent.minLine).thenReturn(0);
 when(resizedEvent.firstLine).thenReturn(1);
 when(resizedEvent.nbDisplayedLines).thenReturn(10);
 when(resizedEvent.maxLine).thenReturn(100);

 // When : The panel receives the resized event
 panelUnderTest.update(this.model, resizedEvent);

 // Then : The verticalScrollBar should be updated with the new minimum, value, extent and maximum
 final BoundedRangeModel rangeModel = panelUnderTest.verticalScrollBar.getModel();
 assertEquals("Wrong minimum value in the verticalScrollBar", 0, rangeModel.getMinimum());
 assertEquals("Wrong value in the verticalScrollBar", 1, rangeModel.getValue());
 assertEquals("Wrong extent value in the verticalScrollBar", 10, rangeModel.getExtent());
 assertEquals("Wrong maximum value in the verticalScrollBar", 100, rangeModel.getMaximum());

 // Then : The DataTable should be updated with the new content
 final String lineNumbers = this.model.getLineNumbers();
 final String lines = this.model.getContent();
 panelUnderTest.updateDataTable(lineNumbers, lines);
 assertEquals("Wrong number of rows in the DataTable", 11, panelUnderTest.dataTable.getRowCount());
 }

 @Test
 public void testUpdateWithScrolledFile() {
 // Given : A scrolled file event
 final JTailModelEvent scrolledEvent = mock(JTailModelEvent.class);
 when(scrolledEvent.type).thenReturn(JTailModelEvent.Type.SCROLLED);

 // When : The panel receives the scrolled event
 panelUnderTest.update(this.model, scrolledEvent);

 // Then : The DataTable should be updated with the new content
 final String lineNumbers = this.model.getLineNumbers();
 final String lines = this.model.getContent();
 panelUnderTest.updateDataTable(lineNumbers, lines);
 assertEquals("Wrong number of rows in the DataTable", panelUnderTest.dataTable.getRowCount(), lines.split("\n").length);
 }

 @Test
 public void testUpdateDataTableWithEmptyContent() {
 // Given : An empty content
 final String emptyContent = "";
 when(this.model.getContent()).thenReturn(emptyContent);

 // When : The panel updates the DataTable
 panelUnderTest.updateDataTable(this.model.getLineNumbers(), this.model.getContent());

 // Then : The DataTable should not contain any rows
 assertEquals("DataTable should be empty", 0, panelUnderTest.dataTable.getRowCount());
 }

 @Test
 public void testUpdateDataTableWithLargeContent() {
 // Given : A large content
 final String largeContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
 when(this.model.getContent()).thenReturn(largeContent);
 final int numRows = 20;
 when(this.model.getLineNumbers()).thenReturn(Mockito.mock(String.class));

 // When : The panel updates the DataTable
 panelUnderTest.updateDataTable(this.model.getLineNumbers(), this.model.getContent());

 // Then : The DataTable should contain the expected number of rows
 assertEquals("Wrong number of rows in the DataTable", numRows, panelUnderTest.dataTable.getRowCount());
 }
}