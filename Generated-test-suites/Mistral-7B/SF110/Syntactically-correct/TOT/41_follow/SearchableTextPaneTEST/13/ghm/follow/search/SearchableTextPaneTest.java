package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Line.Border;
import javax.swing.text.LineElement;
import javax.swing.text.LineView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchableTextPaneTest {

 private SearchableTextPane textPane;
 private String term;

 @Before
 public void setUp() {
 textPane = new SearchableTextPane(new Font("Serif", Font.BOLD, 14), 4);
 term = "Java";
 }

 @Test
 public void testHighlight() {
 List<LineResult> lineResults = textPane.highlight(term, 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertTrue(lineResult.getNumWords() > 0);
 assertEquals(Color.YELLOW, textPane.getBackground());
 }

 @Test
 public void testEmptySearch() {
 List<LineResult> lineResults = textPane.highlight(null, 0);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testSearchWithEmptyText() {
 textPane.setText("");
 int searchPos = textPane.search(term);
 assertEquals(-1, searchPos);
 }

 @Test
 public void testSearchWithMultipleOccurrences() {
 String text = "Java is a programming language. Java is developed by Sun Microsystems. Java is widely used.";
 textPane.setText(text);
 int searchPos = textPane.search(term);
 assertEquals(0, searchPos);
 searchPos = textPane.search(term, searchPos + term.length());
 assertEquals(11, searchPos);
 }

 @Test
 public void testSearchWithError() {
 try {
 textPane.highlight(null, 0);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // expected
 }

 try {
 textPane.highlight("", 0);
 fail("EmptyStringIndexOutOfBoundsException expected");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }

 try {
 textPane.highlight("term", -1);
 fail("IndexOutOfBoundsException expected");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testSearchWithCaseSensitive() {
 textPane.setText("Java is a PROGRAMMING language. Java is DEVELOPED by Sun Microsystems. Java is WIDELY used.");
 int searchPos = textPane.search("java");
 assertEquals(-1, searchPos);
 int searchPos2 = textPane.search("Java");
 assertEquals(0, searchPos2);
 }

 @Test
 public void testSearchWithFlags() {
 textPane.setText("Java is a programming language. Java is developed by Sun Microsystems. Java is widely used.");
 textPane.search("Java", 0);
 textPane.search("developed", 0);
 textPane.search("used", 0);

 List<LineResult> lineResults = textPane.highlight("Java", 0);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(3, lineResult.getNumWords());
 }

 @Test
 public void testScrolling() {
 textPane.setText("Java is a programming language. Java is developed by Sun Microsystems. Java is widely used.");
 SwingUtilities.invokeLater(() -> textPane.setCaretPosition(textPane.getDocument().getLength()));
 textPane.search("Java", 0);
 JScrollPane scrollPane = new JScrollPane(textPane);
 JFrame frame = new JFrame();
 frame.getContentPane().add(scrollPane);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> {
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 textPane.requestFocus();
 textPane.setCaretPosition(textPane.getDocument().getLength() - term.length());
 SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
 });
 }

 @After
 public void tearDown() {
 textPane = null;
 textPane = null;
 }
}