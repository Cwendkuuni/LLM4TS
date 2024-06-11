package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.MockitoAnnotations;

@RunWith(MockitoJUnitRunner.class)
public class InternalChatFrameTest {

 private InternalChatFrame chatFrame;
 private JList list;
 private DefaultListModel listModel;
 private JTextPane showTxt;
 private JTextField txtInput;
 private MainFrame parent;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.parent = mock(MainFrame.class);
 this.listModel = new DefaultListModel();
 this.list = new JList<>(this.listModel);
 this.showTxt = new JTextPane();
 this.txtInput = new JTextField();
 this.chatFrame = new InternalChatFrame(this.parent);
 this.chatFrame.list = this.list;
 this.chatFrame.listModel = this.listModel;
 this.chatFrame.showTxt = this.showTxt;
 this.chatFrame.txtInput = this.txtInput;
 }

 @Test
 public void testCreateGUI() {
 // Verify that the components are added correctly
 assertEquals(1, this.chatFrame.getContentPane().getComponentCount());
 assertTrue(this.chatFrame.getContentPane().getComponentAt(0) instanceof JSplitPane);
 JSplitPane splitPane = (JSplitPane) this.chatFrame.getContentPane().getComponentAt(0);
assertTrue(splitPane.getRightComponent() instanceof JPanel);
 assertTrue(splitPane.getLeftComponent() instanceof JScrollPane);
 JScrollPane leftPane = (JScrollPane) splitPane.getLeftComponent();
 assertTrue(leftPane.getViewport().getView() instanceof JList);
 this.list = (JList) leftPane.getViewport().getView();
 assertEquals(this.list, this.chatFrame.list);
 assertTrue(splitPane.getRightComponent() instanceof JPanel);
 JPanel rightPanel = (JPanel) splitPane.getRightComponent();
 assertTrue(rightPanel.getLayout() instanceof GridBagLayout);
 // Verify that the components are added to the grid bag layout correctly
 GridBagConstraints c = new GridBagConstraints();
 int gridY = 0;
 assertSame(this.showTxt, rightPanel.getComponent(0));
 assertSame(this.txtInput, rightPanel.getComponent(1));
 }

 @Test
 public void testAddText() {
 // Create test data
 String line1 = "User1: Hello";
 String line2 = "*User2*: Hi *User1*";
 String line3 = "*User2**: Hi User1*";
 String line4 = "*User1**: Hi User2";
 String line5 = "*User1**: This is a test";
 // Add test data to the document
 this.chatFrame.doc.insertString(0, line1, this.chatFrame.doc.getStyle("system"));
 this.chatFrame.addText(line2);
 this.chatFrame.addText(line3);
 this.chatFrame.addText(line4);
 this.chatFrame.addText(line5);
 // Verify that the text was added correctly
 assertEquals(5, this.listModel.size());
 assertEquals("User1", this.listModel.get(0));
 assertEquals("User2", this.listModel.get(1));
 assertEquals("User1", this.listModel.get(2));
 assertEquals("User2", this.listModel.get(3));
 assertEquals("User1", this.listModel.get(4));
 // Verify that the text styles were applied correctly
 Style nameStyle = this.chatFrame.doc.getStyle("name");
 Style regularStyle = this.chatFrame.doc.getStyle("regular");
 Style italicStyle = this.chatFrame.doc.getStyle("italic");
 Style boldStyle = this.chatFrame.doc.getStyle("bold");
 Style boldItalicStyle = this.chatFrame.doc.getStyle("bold-italic");
 Style systemStyle = this.chatFrame.doc.getStyle("system");
 assertTrue(this.chatFrame.doc.getStyle("name") != null);
 assertTrue(this.chatFrame.doc.getStyle("regular") != null);
 assertTrue(this.chatFrame.doc.getStyle("italic") != null);
 assertTrue(this.chatFrame.doc.getStyle("bold") != null);
 assertTrue(this.chatFrame.doc.getStyle("bold-italic") != null);
 assertTrue(this.chatFrame.doc.getStyle("system") != null);
 Style user1NameStyle = this.chatFrame.doc.getStyle("User1");
 Style user2NameStyle = this.chatFrame.doc.getStyle("User2");
 assertEquals(user1NameStyle, this.chatFrame.doc.getStyle("name").clone());
 assertEquals(user2NameStyle, this.chatFrame.doc.getStyle("name").clone());
 user1NameStyle.setName("User1");
 user2NameStyle.setName("User2");
 assertEquals(user1NameStyle, this.chatFrame.doc.getStyle("User1"));
 assertEquals(user2NameStyle, this.chatFrame.doc.getStyle("User2"));
 Style user1RegularStyle = this.chatFrame.doc.getStyle("User1");
 Style user2RegularStyle = this.chatFrame.doc.getStyle("User2");
 assertEquals(regularStyle, user1RegularStyle);
 assertEquals(regularStyle, user2RegularStyle);
 Style user1ItalicStyle = this.chatFrame.doc.getStyle("User1:");
 Style user2ItalicStyle = this.chatFrame.doc.getStyle("*User2*:");
 assertEquals(italicStyle, user1ItalicStyle);
 assertEquals(italicStyle, user2ItalicStyle);
 Style user1BoldStyle = this.chatFrame.doc.getStyle("*User1**:");
 Style user2BoldStyle = this.chatFrame.doc.getStyle("*User2**:");
 assertEquals(boldStyle, user1BoldStyle);
 assertEquals(boldStyle, user2BoldStyle);
 Style user1BoldItalicStyle = this.chatFrame.doc.getStyle("*User1**: *User1**");
 Style user2BoldItalicStyle = this.chatFrame.doc.getStyle("*User2**: *User2**");
 assertEquals(boldItalicStyle, user1BoldItalicStyle);
 assertEquals(boldItalicStyle, user2BoldItalicStyle);
 Style systemStyle1 = this.chatFrame.doc.getStyle(":");
 Style systemStyle2 = this.chatFrame.doc.getStyle("Hi");
 assertEquals(systemStyle, systemStyle1);
 assertEquals(systemStyle, systemStyle2);
 }

 @Test(expected = BadLocationException.class)
 public void testAddTextWithBadLocationException() {
 this.chatFrame.doc.insertString(this.chatFrame.doc.getLength(), "Test", this.chatFrame.doc.getStyle("regular"));
 this.chatFrame.doc.insertString(-1, "Test2", this.chatFrame.doc.getStyle("regular"));
 }

 @Test
 public void testAddUser() {
 // Test adding a new user to the list model
 this.chatFrame.addUser("User1");
 assertEquals(1, this.listModel.size());
 assertEquals("User1", this.listModel.get(0));
 // Test adding the same user again
 this.chatFrame.addUser("User1");
 assertEquals(1, this.listModel.size());
 // Test adding multiple users
 this.chatFrame.addUser("User2");
 this.chatFrame.addUser("User3");
 this.chatFrame.addUser("User4");
 assertEquals(4, this.listModel.size());
 // Test getting the user list
 String[] userList = this.chatFrame.getUserlist();
 assertEquals(4, userList.length);
 assertArrayEquals(new String[]{"User1", "User2", "User3", "User4"}, userList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddUserWithInvalidUserName() {
 this.chatFrame.addUser("");
 }

 @Test
 public void testDeleteUser() {
 // Test deleting a user from the list model
 this.chatFrame.addUser("User1");
 this.chatFrame.addUser("User2");
 this.chatFrame.deleteUser("User1");
 assertEquals(1, this.listModel.size());
 assertEquals("User2", this.listModel.get(0));
 // Test deleting a user that doesn't exist
 this.chatFrame.deleteUser("User3");
 assertEquals(1, this.listModel.size());
 }

 @Test
 public void testGetChatlog() {
 // Test getting the chat log from the text pane
 assertEquals("", this.chatFrame.getChatlog());
 // Test adding some text and getting the chat log
 this.chatFrame.addText("User1: Hello");
 this.chatFrame.addText("User2: Hi User1");
 assertEquals("User1: Hello\nUser2: Hi User1\n", this.chatFrame.getChatlog());
 }
}