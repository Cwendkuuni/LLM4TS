
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.parser.DocumentBuilderFactory;

import net.sf.xisemele.impl.OperationsHelperImpl;

public class OperationsHelperImplTest {

    private Document createDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = factory.newDocumentBuilder().newDocument();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to create document.");
        }
    }

    @Test
    public void testChildren() {
        Expert1.testChildrenEmptyNode();
        Expert1.testChildrenSingleChildNode();
        Expert1.testChildrenMultipleChildren();
    }

    @Test
    public void testFind() {
        Expert2.testFindRootDocument();
        Expert2.testFindExactPath();
        Expert2.testFindNoMatchingPath();
        Expert2.testFindNullDocument();
    }

    @Test
    public void testNodeWithName() {
        Expert3.testNodeWithNameEmptyList();
        Expert3.testNodeWithNameSingleMatchingNode();
        Expert3.testNodeWithNameNoMatchingNode();
    }

    @Test
    public void testSublistWithName() {
        Expert3.testSublistWithNameEmptyList();
        Expert3.testSublistWithNameSingleMatchingNode();
        Expert3.testSublistWithNameMultipleMatchingNodes();
        Expert3.testSublistWithNameNoMatchingNodes();
    }
}
