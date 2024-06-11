// TableMetaTest.java

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.schemaspy.model.xml.TableColumnMeta;
import net.sourceforge.schemaspy.model.xml.TableMeta;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableMetaTest {

    @Test
    public void testTableMeta_getName() {
        // Test case from Expert 1
    }

    @Test
    public void testTableMeta_getComments() {
        // Test case from Expert 1
    }

    @Test
    public void testTableMeta_getColumns() {
        // Test case from Expert 1
    }

    @Test
    public void testTableMeta_getRemoteCatalog() {
        // Test case from Expert 1
    }

    @Test
    public void testTableMeta_getRemoteSchema() {
        // Test case from Expert 1
    }

    @Test
    public void testTableMeta_NullTableNode() {
        // Test for null tableNode when initializing TableMeta
    }

    @Test
    public void testTableMeta_EmptyOrNullColumnNodes() {
        // Test for empty or null columnNodes list when initializing TableMeta
    }

    @Test
    public void testTableMeta_NullAttributes() {
        // Test for null attributes
    }

    @Test
    public void testTableMeta_NullValues() {
        // Test for null values for name, comments, remoteCatalog, and remoteSchema
    }

    @Test
    public void testTableMeta_MalformedXMLStructure() {
        // Test for malformed or incorrect XML structure
    }

    private Node createTableNode() {
        // Implementation here
    }

    private Node createColumnNode() {
        // Implementation here
    }
}