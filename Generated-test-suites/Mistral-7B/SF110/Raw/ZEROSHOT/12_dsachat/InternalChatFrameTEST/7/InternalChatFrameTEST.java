package dsachat.client.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;

import org.junit.*;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private JTextPane showTxt;
 private JTextField txtInput;
 private MainFrame parent;
 private DefaultListModel listModel;
 private StyledDocument doc;
 private JPopupMenu listPop;
 private JPopupMenu txtPop;

 @Before
 public void setUp() {
 chatFrame = new InternalChatFrame(new MainFrame()) {
 @Override
 public void addText(String line) {
 // method not used in test
 }

 @Override
 protected void addStylesToDocument(StyledDocument doc) {
 // method not used in test
 }
 };
 parent = chatFrame.parent;
 list = chatFrame.list;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 listModel = chatFrame.listModel;
 doc = chatFrame.doc;
 listPop = chatFrame.listPop;
 txtPop = chatFrame.txtPop;
 }

 @Test
 public void testCreateGUI() {
 assertNotNull(chatFrame);
 assertNotNull(list);
 assertNotNull(showTxt);
 assertNotNull(txtInput);
 assertNotNull(parent);
 assertNotNull(listModel);
 assertNotNull(doc);
 assertNotNull(listPop);
 assertNotNull(txtPop);
 }

 @Test
 public void testAddUser() {
 chatFrame.addUser("User1");
 assertEquals(1, listModel.size());
 assertEquals("User1", listModel.get(0));
 }

 @Test
 public void testDeleteUser() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 assertEquals(2, listModel.size());
 chatFrame.deleteUser("User1");
 assertEquals(1, listModel.size());
 assertNotSame("User1", listModel.get(0));
 }

 @Test
 public void testGetUserlist() {
 chatFrame.addUser("User1");
 chatFrame.addUser("User2");
 String[] uList = chatFrame.getUserlist();
 assertEquals(2, uList.length);
 assertEquals("User1", uList[0]);
 assertEquals("User2", uList[1]);
 }

 @Test
 public void testActionPerformedWhenChooseColor() {
 ListSelectionModel selectionModel = list.getSelectionModel();
 selectionModel.setSelectionInterval(0, 0);
 list.setSelectedValue("User1");
 selectionModel.addListSelectionListener(new ListSelectionListener() {
 @Override
 public void valueChanged(ListSelectionEvent e) {
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "chooseColor"));
 }
 });
 JColorChooserDialog dialog = spy(JColorChooserDialog.class);
 doReturn(dialog).when(chatFrame).showDialog(eq(chatFrame), eq("Choose Color for user User1"), any(Color.class));
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "chooseColor"));
 verify(dialog).setVisible(true);
 }

 @Test
 public void testActionPerformedWhenBgColor() {
 JColorChooserDialog dialog = mock(JColorChooserDialog.class);
 doReturn(dialog).when(chatFrame).showDialog(eq(chatFrame), eq("Choose Color for user User1"), any(Color.class));
 chatFrame.actionPerformed(new ActionEvent(chatFrame, 0, "bgColor"));
 verify(dialog).setVisible(true);
 }

 @Test
 public void testActionPerformedWhenWhisper() {
 String wUser = "/w User1 ";
 assertEquals(wUser, txtInput.getText());
 }

 @Test
 public void testMouseClickedWhenList() {
 chatFrame.maybeShowPopup(mock(MouseEvent.class));
 assertNull(listPop.isVisible());
 }

 @Test
 public void testMouseClickedWhenShowTxt() {
 chatFrame.maybeShowPopup(mock(MouseEvent.class));
 assertNull(txtPop.isVisible());
 }

 @Test
 public void testGetChatlog() {
 String log = "Log1\nLog2\nLog3";
 doc.insertString(0, log, doc.getStyle("regular"));
 assertEquals(log, chatFrame.getChatlog());
 }
}