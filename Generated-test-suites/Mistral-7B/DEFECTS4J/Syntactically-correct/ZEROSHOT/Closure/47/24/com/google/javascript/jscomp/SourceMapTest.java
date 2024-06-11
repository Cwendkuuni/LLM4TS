import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.OutputStreamWriter;
import org.junit.Test;

public class SourceMapTest {

  @Test
  public void testSourceMap() throws IOException {
    // Test creation of a SourceMap object using different formats
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance().getInstance();
    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance().getInstance();
    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance().getInstance();

    // Test that the correct SourceMapGenerator is used for each format
    assertEquals(SourceMapGeneratorV1.class, sourceMapV1.generator.getClass());
    assertEquals(SourceMapGeneratorV2.class, sourceMapV2.generator.getClass());

    // Test adding mappings to the SourceMap
    Node node = Node.newJsonObject();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMapDefault.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMapV2.addMapping(node, outputStartPosition, outputEndPosition);

    // Test that mappings are added correctly for different detail levels
    SourceMap sourceMapAll = SourceMap.Format.V1.getInstance().getInstance();
    SourceMap sourceMapSymbols = SourceMap.Format.DEFAULT.getInstance().getInstance();

    Node nodeAll = Node.newIdentifier("someFunction");
    FilePosition outputStartPositionAll = new FilePosition(3, 3);
    FilePosition outputEndPositionAll = new FilePosition(4, 4);

    sourceMapAll.addMapping(nodeAll, outputStartPositionAll, outputEndPositionAll);
    sourceMapAll.setPrefixMappings(getLocationMappings());

    Node nodeSymbols = Node.newNumber(42);
    FilePosition outputStartPositionSymbols = new FilePosition(5, 5);
    FilePosition outputEndPositionSymbols = new FilePosition(6, 6);

    sourceMapSymbols.addMapping(nodeSymbols, outputStartPositionSymbols, outputEndPositionSymbols);
    sourceMapSymbols.setPrefixMappings(getLocationMappings());

    // Test that only mappings for nodes with the correct detail level are added
    assertEquals(1, sourceMapAll.getMappings().size());
    assertEquals(0, sourceMapSymbols.getMappings().size());

    // Test appending the SourceMap to a output stream
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(out);

    sourceMapV1.appendTo(writer, "sourceMapName.js.map");
    sourceMapDefault.appendTo(writer, "sourceMapName.js.map");
    sourceMapV2.appendTo(writer, "sourceMapName.js.map");

    String sourceMapV1String = out.toString();
    String sourceMapDefaultString = out.toString();
    String sourceMapV2String = out.toString();

    // Test that the SourceMap strings are equal for the same format and detail level
    assertEquals(sourceMapV1String, sourceMapDefaultString);
    assertNotEquals(sourceMapV1String, sourceMapV2String);

    // Test resetting the SourceMap
    sourceMapV1.reset();
    sourceMapDefault.reset();
    sourceMapV2.reset();

    // Test setting the starting position, wrapper prefix, and validation
    sourceMapV1.setStartingPosition(1, 1);
    sourceMapDefault.setStartingPosition(1, 1);
    sourceMapV2.setStartingPosition(1, 1);

    sourceMapV1.setWrapperPrefix("wprefix_");
    sourceMapDefault.setWrapperPrefix("wprefix_");
    sourceMapV2.setWrapperPrefix("wprefix_");

    sourceMapV1.validate(true);
    sourceMapDefault.validate(true);
    sourceMapV2.validate(true);

    sourceMapV1.validate(false);
    sourceMapDefault.validate(false);
    sourceMapV2.validate(false);
  }

  private List<LocationMapping> getLocationMappings() {
    return Lists.newArrayList(
        new LocationMapping("oldProjectPath/", "newProjectPath/"),
        new LocationMapping("src/", ""));
  }
}