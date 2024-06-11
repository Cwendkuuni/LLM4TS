package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JFrame parentFrame;
 private ListSelectionListener listSelectionListener;

 @Before
 public void setUp() {
 parentFrame = new JFrame();
 listSelectionListener = new ListSelectionListener() {
 @Override
 public void valueChanged(ListSelectionEvent e) {
 // TODO Auto-generated method stub
 }
 };
 chatFrame = new InternalChatFrame(parentFrame);
 }

 @Test
 public void testConstructor() {
 assertNotNull(chatFrame.getTitle());
 assertEquals("Chat", chatFrame.getTitle());
 assertNotNull(chatFrame.list);
 assertNotNull(chatFrame.showTxt);
 assertNotNull(chatFrame.txtInput);
 assertNotNull(chatFrame.parent);
 assertNotNull(chatFrame.doc);
 assertNotNull(chatFrame.listPop);
 assertNotNull(chatFrame.txtPop);
 }

 @Test
 public void testAddText() throws BadLocationException {
 // given
 String line = "TestUser: TEST_MESSAGE";
 String styleName = "TestUser";
 Document doc = chatFrame.doc;
 Style s = doc.getStyle(styleName);
 // when
 chatFrame.addText(line);
 // then
 Element element = doc.getElement(doc.getLength() - 1);
 assertEquals(line, element.getAttributes().getAttribute(StyleConstants.CharacterAttributeName.FOREGROUND));
 assertEquals(styleName, element.getAttributes().getAttribute(StyleConstants.NameAttributeName));
 // style check
 assertEquals(StyleConstants.BOLD, s.isBold());
 assertEquals(StyleConstants.ITALIC, s.isItalic());
 }

 @Test
 public void testAddStylesToDocument() {
 StyleContext styleContext = StyleContext.getDefaultStyleContext();
 DefaultStyledDocument defaultStyledDocument = new DefaultStyledDocument();
 Style regular = defaultStyledDocument.addStyle("regular", styleContext.getStyle("default"));
 chatFrame.addStylesToDocument(defaultStyledDocument);
 // then
 Style name = defaultStyledDocument.getStyle("name");
 assertEquals(Color.BLUE, name.getBackground());
 assertEquals(16, name.getFontSize());
 Style regularBold = defaultStyledDocument.getStyle("bold");
 assertEquals(false, regularBold.isBold());
 Style regularItalic = defaultStyledDocument.getStyle("italic");
 assertEquals(false, regularItalic.isItalic());
 Style regularBoldItalic = defaultStyledDocument.getStyle("bold-italic");
 assertEquals(false, regularBoldItalic.isBold());
 assertEquals(true, regularBoldItalic.isItalic());
 }

 @Test
 public void testAddUser() {
 // given
 String user = "TestUser";
 chatFrame.listModel.add(0, user);
 // when
 chatFrame.addUser(user);
 // then
 assertEquals(1, chatFrame.listModel.getSize());
 assertEquals(user, (String) chatFrame.listModel.get(0));
 }

 @Test
 public void testDeleteUser() {
 // given
 String user = "TestUser";
 chatFrame.listModel.add(0, user);
 // when
 chatFrame.deleteUser(user);
 // then
 assertEquals(0, chatFrame.listModel.getSize());
 assertNull(chatFrame.list.getSelectedValue());
 }

 @Test
 public void testGetUserlist() {
 // given
 String user1 = "User1";
 String user2 = "User2";
 chatFrame.listModel.add(user1);
 chatFrame.listModel.add(user2);
 // when
 String[] userlist = chatFrame.getUserlist();
 // then
 assertEquals(2, userlist.length);
 assertEquals(user1, userlist[0]);
 assertEquals(user2, userlist[1]);
 }

 @Test
 public void testListSelection() {
 // given
 String user1 = "User1";
 String user2 = "User2";chatFrame.listModel.add(user1);
 chatFrame.listModel.add(user2);
 chatFrame.list.setModel(chatFrame.listModel);
 // when
 chatFrame.list.addListSelectionListener(listSelectionListener);
 chatFrame.list.setSelectedValue(user1, true);
 // then
 assertEquals(user1, chatFrame.list.getSelectedValue());
 }

 @After
 public void tearDown() {
 parentFrame.dispose();
 chatFrame = null;
 }
}