package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private MainFrame parent;
 private JList list;
 private StyledDocument doc;
 private JTextPane showTxt;
 private JTextField txtInput;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parent = mock(MainFrame.class);
 chatFrame = new InternalChatFrame(parent);
 list = chatFrame.list;
 doc = chatFrame.doc;
 showTxt = chatFrame.showTxt;
 txtInput = chatFrame.txtInput;
 }

 @After
 public void cleanup() {
 chatFrame.dispose();
 }

 @Test
 public void testAddText() {
 String line = "User1: Hello";
 chatFrame.addText(line);
 assertEquals(line, chatFrame.getChatlog().substring(0, chatFrame.getChatlog().lastIndexOf("\n") + line.length()));

 line = "User2: World";
 chatFrame.addText(line);
 assertEquals(line, chatFrame.getChatlog().substring(chatFrame.getChatlog().lastIndexOf("\n") + line.length()));
 }

 @Test
 public void testAddStylesToDocument() {
 StyleConstants.setFontFamily(chatFrame.getDocument().getStyle("default"), "Serif");
 chatFrame.addStylesToDocument(doc);
 assertEquals("Serif", doc.getStyle("regular").getName());
 }

 @Test
 public void testChangeStyle() {
 String name = "User1";
 chatFrame.addUser(name);
 Style userStyle = doc.getStyle(name);
 Style expectedStyle = chatFrame.getDocument().getStyle("name");
 assertEquals(expectedStyle.getName(), userStyle.getName());
 assertEquals(expectedStyle.getFontSize(), userStyle.getFontSize(), 0.1);
 assertEquals(expectedStyle.getForeground(), userStyle.getForeground());

 chatFrame.changeStyle(12, Color.RED, true, false, name);
 userStyle = doc.getStyle(name);
 assertEquals(expectedStyle.getName(), userStyle.getName());
 assertEquals(12, userStyle.getFontSize(), 0.1);
 assertNotEquals(expectedStyle.getForeground(), userStyle.getForeground());
 }

 @Test
 public void testAddUserAndDeleteUser() {
 chatFrame.addUser("User1");
 assertEquals(1, chatFrame.getUserlist().length);
 chatFrame.deleteUser("User1");
 assertEquals(0, chatFrame.getUserlist().length);
 }

 @Test
 public void testActionPerformed() {
 String name = "User1";
 chatFrame.addUser(name);
 list.setSelectedValue(name, true);
 chatFrame.actionPerformed(new ActionEvent(list, 0, "chooseColor"));
 verify(parent).setChatFrameColor(name, mock(Color.class));

 name = "User2";
 chatFrame.addUser(name);
 list.setSelectedValue(name, true);
 chatFrame.actionPerformed(new ActionEvent(list, 0, "bgColor"));
 verify(parent).setChatFrameBackground(name, mock(Color.class));
 }

 @Test
 public void testMouseClicked() {
 doNothing().when(chatFrame).maybeShowPopup(any(MouseEvent.class));
 chatFrame.createGUI();
 list.addMouseListener(chatFrame);
 list.add(new Object[]{"User1", null});
 list.addMouseListener(new ListCellRenderer() {
 @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
 return new JLabel((String) value, SwingConstants.LEFT);
 }});

 MouseEvent event = new MouseEvent(list, 1, System.currentTimeMillis(), 0, 10, 10, 1, false);
 chatFrame.mouseClicked(event);
 verify(chatFrame, times(1)).maybeShowPopup(event);
 }
}