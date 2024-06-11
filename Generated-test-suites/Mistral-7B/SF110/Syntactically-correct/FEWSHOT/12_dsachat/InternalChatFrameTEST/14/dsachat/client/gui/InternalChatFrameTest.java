package dsachat.client.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;
    private MainFrame parentMock;
    private JList listMock;
    private JTextPane showTxtMock;
    private JTextField txtInputMock;
    private DefaultListModel listModelMock;
    private StyledDocument docMock;
    private JPopupMenu listPopMock;
    private JPopupMenu txtPopMock;

    @Before
    public void setUp() {
        parentMock = mock(MainFrame.class);
        listMock = mock(JList.class);
        showTxtMock = mock(JTextPane.class);
        txtInputMock = mock(JTextField.class);
        listModelMock = new DefaultListModel();
        docMock = mock(StyledDocument.class);
        listPopMock = mock(JPopupMenu.class);
        txtPopMock = mock(JPopupMenu.class);

        chatFrame = new InternalChatFrame(parentMock);
        chatFrame.listModel = listModelMock;
        chatFrame.list = listMock;
        chatFrame.showTxt = showTxtMock;
        chatFrame.txtInput = txtInputMock;
        chatFrame.doc = docMock;
        chatFrame.listPop = listPopMock;
        chatFrame.txtPop = txtPopMock;
    }

    @Test
    public void testAddText() {
        String line = "You: Hello";
        chatFrame.addText(line);
        verify(docMock, times(1)).insertString(eq(chatFrame.doc.getLength()), eq(line), any(Style.class));
        verify(docMock, times(1)).insertString(eq(chatFrame.doc.getLength()), eq("\n"), any(Style.class));

        line = ": World";
        chatFrame.addText(line);
        Style style = mock(Style.class);
        when(docMock.getStyle("regular")).thenReturn(style);
        verify(style, times(1)).setFontSize(eq(14));
        verify(style, times(1)).setForeground(eq(new Color(0.0f, 0.6f, 0.0f, 1.0f)));
        verify(docMock, times(1)).insertString(eq(chatFrame.doc.getLength()), eq(line), style);
        verify(docMock, times(1)).insertString(eq(chatFrame.doc.getLength()), eq("\n"), any(Style.class));
    }

    @Test
    public void testChangeStyle() {
        chatFrame.changeStyle(12, Color.RED, true, false, "name");
        Style style = mock(Style.class);
        when(docMock.getStyle("name")).thenReturn(style);
        verify(style, times(1)).setForeground(eq(Color.RED));
        verify(style, times(1)).setBold(eq(true));
        verify(style, times(1)).setItalic(eq(false));
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        verify(listModelMock, times(1)).add(eq("User1"));
        verify(docMock, times(1)).addStyle("User1", any(Style.class));
        verify(list, times(1)).ensureIndexIsVisible(eq(0));
        verify(list, times(1)).setSelectedIndex(eq(0));
        verify(list, times(1)).validate();
    }

    @Test
    public void testDeleteUser() {
        chatFrame.listModel.addElement("User1");
        chatFrame.deleteUser("User1");
        verify(listModelMock, times(1)).removeElement(eq("User1"));
    }

    @Test
    public void testGetUserlist() {
        String[] expectedUserlist = {"User1", "User2", "User3"};
        chatFrame.listModel.addElement("User1");
        chatFrame.listModel.addElement("User2");
        chatFrame.listModel.addElement("User3");
        String[] actualUserlist = chatFrame.getUserlist();
        assertArrayEquals(expectedUserlist, actualUserlist);
    }

    @Test
    public void testActionPerformed() {
        String user = "User1";
        chatFrame.listModel.addElement(user);
        when(listMock.getSelectedValue()).thenReturn(user);

        chatFrame.actionPerformed(mock(ActionEvent.class));
        verify(parentMock, times(1)).addStyle(eq(user), any(Style.class));

        when(listPopMock.getItem(0).getActionCommand()).thenReturn("chooseColor");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(mock(MouseEvent.class));
        verify(listPopMock, times(1)).show(eq(listMock), anyInt(), anyInt());

        when(listPopMock.getItem(1).getActionCommand()).thenReturn("bgColor");
        chatFrame.actionPerformed(mock(MouseEvent.class));
        verify(showTxtMock, times(1)).setBackground(any(Color.class));
    }
}