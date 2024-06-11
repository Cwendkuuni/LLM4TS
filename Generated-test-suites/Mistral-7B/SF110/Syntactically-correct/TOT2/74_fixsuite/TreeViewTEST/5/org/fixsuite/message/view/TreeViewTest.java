package org.fixsuite.message.view;

import static org.junit.Assert.*;

import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObject;
import org.jmock.lib.legacy.Mockery;
import org.jmock.lib.legacy.Test;
import org.jmock.lib.legacy.TestImmediate;
import org.jmock.lib.legacy.TestInvocation;
import org.jmock.lib.legacy.TestProbe;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private Mockery context;
    private Library libraryMock;
    private TreeView treeView;

    @Before
    public void setUp() {
        context = new Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        libraryMock = context.mock(Library.class);
        treeView = new TreeView(libraryMock);
    }

    @Test
    public void testInitGUI() {
        new Test() {
            {
                expectOne(libraryMock, "getDictionaries");
                libraryMock.getDictionaries();

                treeView.initGUI();

                verifyOneInteraction(libraryMock);
            }
        };
    }

    @Test
    public void testLoadFIXTree() {
        new Test() {
            {
                final DictionaryInfo dictionaryMock = context.mock(DictionaryInfo.class);
                final MessageInfo messageMock = context.mock(MessageInfo.class);
                final CompositeFixInfo compositeFixInfoMock = context.mock(CompositeFixInfo.class);
                final FixInfoTreeNode rootMock = context.mock(FixInfoTreeNode.class);

                expectMany(libraryMock).proxy().getDictionaries().get(anyInt());
                expectOne(libraryMock, "getDictionaries").andReturn(new ArrayList<DictionaryInfo>() {{
                    add(dictionaryMock);
                }});

                expectOne(dictionaryMock, "getMessages").andReturn(new ArrayList<MessageInfo>() {{
                    add(messageMock);
                }});

                expectOne(messageMock, "getName").andReturn("Message1");
                expectOne(messageMock, "getMessageType").andReturn("Type1");

                expectOne(rootMock, "add").withAnyArguments();

                treeView.loadFIXTree();

                verifyOneInteraction(libraryMock);
                verifyOneInteraction(dictionaryMock);
                verifyOneInteraction(messageMock);
                verifyOneInteraction(rootMock);
            }
        };
    }

    // Test methods for other public methods: loadItems, expandAll, collapseAll, search, clear, valueChanged, actionPerformed, keyPressed
}