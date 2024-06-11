package dsachat.client.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InternalChatFrameTest {

 private InternalChatFrame icf;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;
 private MainFrame parentMock;

 @Before
 public void setUp() {
 parentMock = mock(MainFrame.class);
 icf = new InternalChatFrame(parentMock);
 list = icf.list;
 showTxt = icf.showTxt;
 txtInput = icf.txtInput;
 }

 @Test
 public void testInternalChatFrameConstructor() {
 assertTrue(icf.isVisible());
 assertNotNull(list);
 assertNotNull(showTxt);
 assertNotNull(txtInput);
 assertNotNull(parentMock);
 }

 @Test
 public void testCreateGUI() {
 GridBagConstraints c = new GridBagConstraints();
 JSplitPane split = new JSplitPane(1);
 JScrollPane scrList = new JScrollPane(list);
 JScrollPane scrTxt = new JScrollPane(showTxt);
 JPanel right = new JPanel(new GridBagLayout());
 assertSame(icf, split.getTopComponent());
 assertSame(scrList, split.getLeftComponent());
 assertSame(right, split.getRightComponent());
 assertSame(showTxt, scrTxt.getViewportView());
 assertSame(list, scrList.getViewportView());
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello";
 icf.addText(line);
 String text = showTxt.getText();
 assertTrue(text.contains(line));
 }

 @Test
 public void testAddStylesToDocument() {
 StyledDocument doc = icf.doc;
 Style def = StyleContext.getDefaultStyleContext().getStyle("default");
 Style regular = doc.getStyle("regular");
 Style italic = doc.getStyle("italic");
 Style bold = doc.getStyle("bold");
 Style boldItalic = doc.getStyle("bold-italic");
 Style name = doc.getStyle("name");
 Style system = doc.getStyle("system");
 Style small = doc.getStyle("small");
 Style large = doc.getStyle("large");

 assertEquals(def, doc.getStyle("default"));
 assertEquals(regular, doc.getStyle("regular"));
 assertEquals(italic, doc.getStyle("italic"));
 assertEquals(bold, doc.getStyle("bold"));
 assertEquals(boldItalic, doc.getStyle("bold-italic"));
 assertEquals(name, doc.getStyle("name"));
 assertEquals(system, doc.getStyle("system"));
 assertEquals(small, doc.getStyle("small"));
 assertEquals(large, doc.getStyle("large"));
 }

 @Test
 public void testChangeStyle() {
 icf.changeStyle(12, Color.RED, true, false, "name");
 Style name = icf.doc.getStyle("name");
 assertEquals(Color.RED, name.getBackground());
 assertFalse(name.isBold());
 assertFalse(name.isItalic());
 }

 @Test
 public void testAddUser() {
 icf.addUser("User1");
 String[] userlist = icf.getUserlist();
 assertEquals(1, userlist.length);
 assertEquals("User1", userlist[0]);
 }

 @Test
 public void testDeleteUser() {
 icf.addUser("User1");
 icf.addUser("User2");
 icf.deleteUser("User1");
 String[] userlist = icf.getUserlist();
 assertEquals(1, userlist.length);
 assertEquals("User2", userlist[0]);
 }

 @Test
 public void testGetUserlist() {
 icf.addUser("User1");
 icf.addUser("User2");
 String[] userlist = icf.getUserlist();
 assertEquals(2, userlist.length);
 }

 @Test
 public void testActionPerformed() {
 String name = "User1";
 when(parentMock.hero.getName()).thenReturn(name);
 icf.list.setSelectedValue(name, true);
 icf.actionPerformed(mock(ActionEvent.class));
 verify(parentMock).heroNameChanged(name);
 }

 @Test
 public void testMouseClicked() {
 icf.maybeShowPopup(mock(MouseEvent.class));
 verify(list, never()).showPopupMenu(any(MouseEvent.class));
 }

 @Test
 public void testGetChatlog() {
 String log = "User1: Hello\nUser2: World";
 icf.showTxt.setText(log);
 assertEquals(log, icf.getChatlog());
 }
}