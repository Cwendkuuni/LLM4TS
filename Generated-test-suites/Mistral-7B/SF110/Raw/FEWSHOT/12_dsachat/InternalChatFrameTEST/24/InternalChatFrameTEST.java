package dsachat.client.gui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import javax.swing.event.MouseEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class InternalChatFrameTest {

 InternalChatFrame icf;
 MainFrame parentMock;
 JList listMock;
 DefaultListModel listModelMock;
 JTextPane showTxtMock;
 JTextField txtInputMock;
 JPopupMenu listPopMock;
 JPopupMenu txtPopMock;

 @Before
 public void setUp() {
 parentMock = Mockito.mock(MainFrame.class);
 icf = new InternalChatFrame(parentMock);
 listModelMock = new DefaultListModel();
 listMock = Mockito.mock(JList.class);
 showTxtMock = Mockito.mock(JTextPane.class);
 txtInputMock = Mockito.mock(JTextField.class);
 listPopMock = Mockito.mock(JPopupMenu.class);
 txtPopMock = Mockito.mock(JPopupMenu.class);

 icf.listModel = listModelMock;
 icf.list = listMock;
 icf.showTxt = showTxtMock;
 icf.txtInput = txtInputMock;
 icf.listPop = listPopMock;
 icf.txtPop = txtPopMock;
 }

 @Test
 public void testInternalChatFrameCreation() {
 assertNotNull(icf.list);
 assertNotNull(icf.showTxt);
 assertNotNull(icf.txtInput);
 assertNotNull(icf.parent);
 assertNotNull(icf.doc);
 assertNotNull(icf.listPop);
 assertNotNull(icf.txtPop);
 }

 @Test
 public void testAddText() {
 String line = "You: Hello World";
 icf.addText(line);
 String expected = "You: Hello World\n";
 String result = icf.showTxt.getText();
 assertEquals(expected, result);
 }

 @Test
 public void testAddStylesToDocument() {
 Style regular = Mockito.mock(Style.class);
 Style italic = Mockito.mock(Style.class);
 Style bold = Mockito.mock(Style.class);
 Style boldItalic = Mockito.mock(Style.class);
 Style name = Mockito.mock(Style.class);
 Style system = Mockito.mock(Style.class);

 icf.addStylesToDocument(icf.doc);
 Mockito.verify(icf.doc).addStyle("regular", regular);
 Mockito.verify(icf.doc).addStyle("italic", italic);
 Mockito.verify(icf.doc).addStyle("bold", bold);
 Mockito.verify(icf.doc).addStyle("bold-italic", boldItalic);
 Mockito.verify(icf.doc).addStyle("name", name);
 Mockito.verify(icf.doc).addStyle("system", system);
 }

 @Test
 public void testChangeStyle() {
 Style style = Mockito.mock(Style.class);
 icf.changeStyle(12, Color.BLUE, false, false, "name");
 Mockito.verify(style).setForeground(Color.BLUE);
 }

 @Test
 public void testAddUser() {
 icf.addUser("TestUser");
 String[] expected = {"TestUser"};
 String[] result = icf.getUserlist();
 assertArrayEquals(expected, result);
 }

 @Test
 public void testDeleteUser() {
 icf.addUser("TestUser1");
 icf.addUser("TestUser2");
 icf.deleteUser("TestUser1");
 String[] expected = {"TestUser2"};
 String[] result = icf.getUserlist();
 assertArrayEquals(expected, result);
 }

 @Test
 public void testGetChatlog() {
 String line1 = "User1: Hello World";
 String line2 = "User2: This is a test";
 icf.addText(line1);
 icf.addText(line2);
 String expected = "User1: Hello World\nUser2: This is a test\n";
 String result = icf.getChatlog();
 assertEquals(expected, result);
 }

 @Test
 public void testMouseEvent() {
 MouseEvent mouseEvent = Mockito.mock(MouseEvent.class);
 icf.maybeShowPopup(mouseEvent);
 Mockito.verify(listPopMock).show(icf.list, Mockito.anyInt(), Mockito.anyInt());
 Mockito.verify(txtPopMock).show(icf.list, Mockito.anyInt(), Mockito.anyInt());
 }
}