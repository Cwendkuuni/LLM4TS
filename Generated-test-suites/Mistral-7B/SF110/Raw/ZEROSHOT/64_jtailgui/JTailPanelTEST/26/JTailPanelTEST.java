package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.TailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

import junit.framework.TestSuite;
import junit.runner.JUnitCore;

@RunWith(JUnitCore.class)
public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTable dataTable;
 private DefaultTableModel tableModel;
 private JScrollPane scrollPane;
 private JScrollBar verticalScrollBar;
 private JFrame frame;
 private JTailModel model;

 @Before
 public void setUp() {
 JTailLogger.init();
 model = Mockito.spy(new JTailModel());
 jTailPanel = new JTailPanel(model);
 dataTable = jTailPanel.dataTable;
 tableModel = (DefaultTableModel) jTailPanel.tableModel;
 scrollPane = jTailPanel.scrollPane;
 verticalScrollBar = jTailPanel.verticalScrollBar;
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }

 @After
 public void tearDown() {
 frame.dispose();
 jTailPanel = null;
 model = null;
 }

 @Test
 public void testJTailPanelConstruction() {
 // Test that the JTailPanel is constructed correctly
 assertEquals("Unexpected JTailPanel width", 655, jTailPanel.getWidth());
 assertEquals("Unexpected JTailPanel height", 1100, jTailPanel.getHeight());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getDataTable());
 assertNotNull(jTailPanel.getScrollPane());
 assertNotNull(jTailPanel.getTableModel());
 }

 @Test
 public void testCharacterHeight() {
 // Test getCharacterHeight method
 assertEquals("Unexpected character height", 15, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testVerticalScrollBar() {
 // Test getVerticalScrollBar method
 assertNotNull(jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithModelEvent() {
 // Test update method with JTailModelEvent.Type.RESIZED
 model.notifyObservers(new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, "resized", 1, 1, 10, 10, 10, 5));
 SwingUtilities.invokeLater(() -> {
 JTailPanelTest.this.assertLineNumberAndContentInTable(Arrays.asList("1", "2", "3", "4"), Arrays.asList("line1", "line2", "line3", "line4"));
 JTailPanelTest.this.assertLinesDisplayed(1, 4);
 JTailPanelTest.this.assertScrollBarValues(1, 4, 10);
 });

 // Test update method with JTailModelEvent.Type.SCROLLED
 model.notifyObservers(new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, "scrolled", 1, 1, 1, 1, 1, 5));
 SwingUtilities.invokeLater(() -> {
 JTailPanelTest.this.assertLineNumberAndContentInTable(Arrays.asList("1", "2", "3", "4"), Arrays.asList("line1", "line2", "line3", "line4"));
 JTailPanelTest.this.assertLinesDisplayed(1, 4);
 JTailPanelTest.this.assertScrollBarValues(1, 4, 5);
 });
 }

 @Test
 public void testUpdateDataTable() {
 // Test updateDataTable method
 String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n21\n22\n23\n24\n25\n26\n27\n28\n29\n30\n31\n32\n33\n34\n35\n36\n37\n38\n39\n40\n41\n42\n43\n44\n45\n46\n47\n48\n49\n50";
 String lines = "line1\nline2\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\nline11\nline12\nline13\nline14\nline15\nline16\nline17\nline18\nline19\nline20\nline21\nline22\nline23\nline24\nline25\nline26\nline27\nline28\nline29\nline30\nline31\nline32\nline33\nline34\nline35\nline36\nline37\nline38\nline39\nline40\nline41\nline42\nline43\nline44\nline45\nline46\nline47\nline48\nline49\nline50";
 JTailPanelTest.this.updateDataTable(jTailPanel, lineNumbers, lines);
 SwingUtilities.invokeLater(() -> {
 JTailPanelTest.this.assertLineNumberAndContentInTable(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"), Arrays.asList("line1", "line2", "line3", "line4", "line5", "line6", "line7", "line8", "line9", "line10", "line11", "line12", "line13", "line14", "line15", "line16", "line17", "line18", "line19", "line20", "line21", "line22", "line23", "line24", "line25", "line26", "line27", "line28", "line29", "line30", "line31", "line32", "line33", "line34", "line35", "line36", "line37", "line38", "line39", "line40", "line41", "line42", "line43", "line44", "line45", "line46", "line47", "line48", "line49", "line50"));
 });
 }

 @Test
 public void testFontMetrics() {
 // Test fontMetrics method
 Font font = new Font("Serif", Font.BOLD, 12);
 FontMetrics metrics = new FontMetrics(font, jTailPanel);
 assertEquals("Unexpected character width", 6, metrics.charWidth('0'));
 assertEquals("Unexpected string width", 13, metrics.stringWidth("0"));
 }

 @Test
 public void testSizeController() {
 // Test TailSizeController
 int width = 800;
 int height = 600;
 JTailSizeController sizeController = new JTailSizeController(jTailPanel, model);
 SwingUtilities.invokeLater(() -> {
 frame.setSize(new Dimension(width, height));
 frame.add(jTailPanel);
 frame.setVisible(true);
 });
 SwingUtilities.invokeLater(() -> {
 assertEquals("Unexpected JTailPanel width", width, jTailPanel.getWidth());
 assertEquals("Unexpected JTailPanel height", height, jTailPanel.getHeight());
 });
 }

 @Test
 public void testScrollBarAdjustmentController() {
 // Test ScrollBarAdjustmentController
 int numberOfLines = 10;
 int firstLine = 1;
 int maxLine = 10 + numberOfLines;
 JScrollBarAdjustmentController scrollBarAdjustmentController = new ScrollBarAdjustmentController(jTailPanel, model);
 SwingUtilities.invokeLater(() -> {
 scrollBarAdjustmentController.valueChanged(new AdjustmentEvent() {
 @Override
 public int getValue() {
 return firstLine;
 }

 @Override
 public int getExtent() {
 return numberOfLines;
 }

 @Override
 public int getMinimum() {
 return 0;
 }

 @Override
 public int getMaximum() {
 return maxLine;
 }
 });
 });
 SwingUtilities.invokeLater(() -> {
 assertEquals("Unexpected first line", firstLine, jTailPanel.getModel().getFirstLine());
 assertEquals("Unexpected max line", maxLine, jTailPanel.getModel().getMaxLine());
 assertEquals("Unexpected nb displayed lines", numberOfLines, jTailPanel.getModel().getNbDisplayedLines());
 });
 }

 private void updateDataTable(JTailPanel jTailPanel, String lineNumbers, String lines) {
 SwingUtilities.invokeLater(() -> {
 jTailPanel.updateDataTable(lineNumbers, lines);
 });
 }

 private void assertLineNumberAndContentInTable(List<String> lineNumbers, List<String> lines) {
 for (int i = 0; i < lineNumbers.size(); i++) {
 assertEquals("Unexpected line number " + i, lineNumbers.get(i), dataTable.getValueAt(i, 0).toString());
 assertEquals("Unexpected line content " + i, lines.get(i), dataTable.getValueAt(i, 1).toString());
 }
 }

 private void assertLinesDisplayed(int firstLine, int lastLine) {
 assertTrue("Unexpected number of lines displayed : " + dataTable.getRowCount(), dataTable.getRowCount() >= lastLine);
 for (int i = 0; i < dataTable.getRowCount(); i++) {
 int lineNumber = Integer.parseInt(dataTable.getValueAt(i, 0).toString());
 assertTrue("Unexpected line number displayed : " + lineNumber, lineNumber >= firstLine && lineNumber <= lastLine);
 }
 }

 private void assertScrollBarValues(int firstLine, int lastLine, int scrollBarValue) {
 assertTrue("Unexpected scroll bar value : " + scrollBarValue, scrollBarValue >= firstLine && scrollBarValue <= lastLine);
 }
}