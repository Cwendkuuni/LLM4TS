package dsachat.client.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

	private MainFrame parent;
	private InternalChatFrame chatFrame;

	@Before
	public void setup() {
		parent = new MainFrame();
		chatFrame = new InternalChatFrame(parent);
		parent.getContentPane().add(chatFrame);
		parent.pack();
		parent.setVisible(true);
	}

	@After
	public void teardown() {
		SwingUtilities.invokeLater(() -> parent.dispose());
	}

	@Test
	public void testConstructor() {
		assertNotNull(chatFrame);
		assertEquals("Chat", chatFrame.getTitle());
		assertEquals(true, chatFrame.isFocusable());
		assertEquals(true, chatFrame.isResizable());
		assertEquals(false, chatFrame.isClosable());
		assertEquals(false, chatFrame.isMaximizable());
		assertEquals(false, chatFrame.isIconifiable());
	}

	@Test
	public void testAddText() {
		chatFrame.addText("You: Hello, World!");
		assertNotNull(chatFrame.showTxt);
		assertEquals("Hello, World!\n", chatFrame.showTxt.getText());

		chatFrame.addText("OtherUser: Hi!");
		assertEquals("Hello, World!\nOtherUser: Hi!\n", chatFrame.showTxt.getText());
	}

	@Test
	public void testAddStylesToDocument() {
		assertNotNull(chatFrame.doc);
		assertEquals(17, chatFrame.doc.getStyleCount());
		assertNotNull(chatFrame.doc.getStyle("regular"));
		assertNotNull(chatFrame.doc.getStyle("italic"));
		assertNotNull(chatFrame.doc.getStyle("bold"));
		assertNotNull(chatFrame.doc.getStyle("bold-italic"));
		assertNotNull(chatFrame.doc.getStyle("name"));
		assertNotNull(chatFrame.doc.getStyle("system"));
	}

	@Test
	public void testAddUser() {
		chatFrame.addText("You: Adding User");
		chatFrame.addUser("User1");
		JList<String> list = chatFrame.list;
		assertEquals(1, list.getModel().getSize());
		assertEquals("User1", list.getModel().getElementAt(0));
		assertEquals("User1", chatFrame.listModel.get(0));
		assertNotNull(chatFrame.doc.getStyle("User1"));
	}

	@Test
	public void testDeleteUser() {
		chatFrame.addText("You: Adding User1");
		chatFrame.addUser("User1");
		chatFrame.addText("You: Adding User2");
		chatFrame.addUser("User2");
		JList<String> list = chatFrame.list;
		assertEquals(2, list.getModel().getSize());
		chatFrame.deleteUser("User1");
		assertEquals(1, list.getModel().getSize());
		assertNull(chatFrame.listModel.get(0));
	}

	@Test
	public void testGetUserlist() {
		chatFrame.addText("You: Adding User1");
		chatFrame.addUser("User1");
		chatFrame.addText("You: Adding User2");
		chatFrame.addUser("User2");
		List<String> userlist = Arrays.asList("User1", "User2");
		assertEquals(userlist, Arrays.asList(chatFrame.getUserlist()));
	}

	@Test
	public void testActionPerformedWhenChooseColor() {
		final Color newColor = new Color(1.0f, 1.0f, 1.0f);
		SwingUtilities.invokeLater(() -> chatFrame.list.setSelectedValue("User1", true));
		SwingUtilities.invokeLater(() -> chatFrame.actionPerformed(new ActionEvent("chooseColorButton", 0, "chooseColor")));
		SwingUtilities.invokeLater(() -> JColorChooser.showDialog(chatFrame, "Choose Color for user User1", newColor));
		SwingUtilities.invokeLater(() -> chatFrame.changeStyle(12, new Color(255, 0, 0), false, false, "User1"));
		assertEquals(new Color(255, 0, 0), chatFrame.showTxt.getBackground());
	}

	@Test
	public void testActionPerformedWhenBgColor() {
		final Color newColor = new Color(1.0f, 1.0f, 1.0f);
		SwingUtilities.invokeLater(() -> chatFrame.actionPerformed(new ActionEvent("bgColorButton", 0, "bgColor")));
		SwingUtilities.invokeLater(() -> JColorChooser.showDialog(chatFrame, "Choose Color for user User1", newColor));
		SwingUtilities.invokeLater(() -> chatFrame.showTxt.setBackground(newColor));
		assertEquals(newColor, chatFrame.showTxt.getBackground());
	}

	@Test
	public void testActionPerformedWhenWhisper() {
		SwingUtilities.invokeLater(() -> chatFrame.list.setSelectedValue("User1", true));
		SwingUtilities.invokeLater(() -> chatFrame.txtInput.setText("/w User1 Hello"));
		SwingUtilities.invokeLater(() -> chatFrame.txtInput.doClick());
		SwingUtilities.invokeLater(() -> chatFrame.actionPerformed(new ActionEvent("sendButton", 0, "sendInput")));
		assertEquals("/w User1 Hello", chatFrame.getChatlog());
	}

}