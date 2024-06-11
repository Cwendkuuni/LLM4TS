package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private static final String TEST_SEARCH_TERM = "JUnit";
 private static final int TEST_TAB_SIZE = 4;
 private static final int TEST_FLAGS = 0;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), TEST_TAB_SIZE);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 JFrame testFrame = new JFrame();
 JScrollPane scrollPane = new JScrollPane(searchableTextPane);
 testFrame.getContentPane().add(scrollPane);
 testFrame.pack();
 assertTrue(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight() {
 String text = "JUnit is a simple and lightweight Java testing framework.\n" +
                "JUnit can be used to test Java classes by writing repeatable, automated tests.\n" +
                "JUnit provides assertions to check that code produces the expected results.";
 searchableTextPane.setText(text);
 searchableTextPane.setDocument(searchableTextPane.getDocument());
 searchableTextPane.highlight(TEST_SEARCH_TERM, TEST_FLAGS);
 List<LineResult> lineResults = searchableTextPane.getHighlights();
 int i = 0;
 for (LineResult lineResult : lineResults) {
 String lineText = searchableTextPane.getDocument().getText(lineResult.getStartOffset(), lineResult.getLength()).toString();
 assertTrue(lineText.contains(TEST_SEARCH_TERM));
 i++;
 }
 assertEquals(3, i);
 }

 @Test
 public void testSearch() {
 searchableTextPane.setText("JUnit is a simple and lightweight Java testing framework.\n" +
                             "JUnit can be used to test Java classes by writing repeatable, automated tests.\n" +
                             "JUnit provides assertions to check that code produces the expected results.");
 searchableTextPane.setDocument(searchableTextPane.getDocument());
 int position = searchableTextPane.search(TEST_SEARCH_TERM);
 assertEquals(0, position);
 position = searchableTextPane.search(TEST_SEARCH_TERM, 10);
 assertEquals(-1, position);
 }

 @Test
 public void testSearchPrevious() {
 searchableTextPane.setText("JUnit is a simple and lightweight Java testing framework.\n" +
                             "JUnit can be used to test Java classes by writing repeatable, automated tests.\n" +
                             "JUnit provides assertions to check that code produces the expected results.");
 searchableTextPane.setDocument(searchableTextPane.getDocument());
 int position = searchableTextPane.search(TEST_SEARCH_TERM);
 assertEquals(0, position);
 position = searchableTextPane.search(TEST_SEARCH_TERM, 10);
 assertEquals(-1, position);
 position = searchableTextPane.search(TEST_SEARCH_TERM);
 assertEquals(3, position);
 position = searchableTextPane.search(TEST_SEARCH_TERM, -1);
 assertEquals(0, position);
 }

 @Test
 public void testRemoveHighlights() {
 String text = "JUnit is a simple and lightweight Java testing framework.\n" +
                "JUnit can be used to test Java classes by writing repeatable, automated tests.\n" +
                "JUnit provides assertions to check that code produces the expected results.";
 searchableTextPane.setText(text);
 searchableTextPane.setDocument(searchableTextPane.getDocument());
 searchableTextPane.highlight(TEST_SEARCH_TERM, TEST_FLAGS);
 List<LineResult> lineResults = searchableTextPane.getHighlights();
 searchableTextPane.removeHighlights();
 assertFalse(searchableTextPane.hasFocus());
 assertTrue(lineResults.isEmpty());
 }

 @Test
 public void testDocumentListener() throws Exception {
 DocumentListener documentListener = new DocumentListener() {
 @Override
 public void insertUpdate(DocumentEvent e) {
 // Test for insert update
 }

 @Override
 public void removeUpdate(DocumentEvent e) {
 // Test for remove update
 }

 @Override
 public void changedUpdate(DocumentEvent e) {
 // Test for changed update
 }
 };
 searchableTextPane.getDocument().addDocumentListener(documentListener);
 JTextAction action = new JTextAction() {
 @Override
 public void actionPerformed(Document doc) {
 // Perform an action
 }
 };
 searchableTextPane.getDocument().putProperty("JTextComponent.actionMap", action.getValue(JTextComponent.class));
 String text = "JUnit is a simple and lightweight Java testing framework.\n" +
               "JUnit can be used to test Java classes by writing repeatable, automated tests.\n" +
               "JUnit provides assertions to check that code produces the expected results.";
 searchableTextPane.setText(text);
 searchableTextPane.setDocument(searchableTextPane.getDocument());
 searchableTextPane.highlight(TEST_SEARCH_TERM, TEST_FLAGS);
 List<LineResult> lineResults = searchableTextPane.getHighlights();
 assertEquals(3, lineResults.size());
 }

}