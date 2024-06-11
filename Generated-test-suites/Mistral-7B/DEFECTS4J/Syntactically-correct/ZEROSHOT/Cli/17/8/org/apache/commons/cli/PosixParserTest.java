import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Given
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C");
        options.addOption("d", 4, "Option D");
        options.addOption("e", 5, "Option E");
        options.addOption("f", 6, "Option F");

        String[] args = {
                "-a",
                "-b",
                "-c",
                "--d=4",
                "-e=5",
                "--f=6",
                "-g",
                "--h"
        };

        PosixParser parser = new PosixParser();

        // When
        String[] tokens = parser.flatten(options, args, false);

        // Then
        List<String> expectedTokens = Arrays.asList(
                "-a",
                "--",
                "d",
                "4",
                "--",
                "e",
                "5",
                "--",
                "f",
                "6"
        );

        Assert.assertArrayEquals(expectedTokens.toArray(new String[expectedTokens.size()]), tokens);

        // Given
        String[] argsWithNonOption = {
                "-a",
                "--",
                "notAnOption"
        };

        // When
        String[] tokensWithNonOption = parser.flatten(options, argsWithNonOption, true);

        // Then
        List<String> expectedTokensWithNonOption = Arrays.asList(
                "-a",
                "--notAnOption"
        );

        Assert.assertArrayEquals(expectedTokensWithNonOption.toArray(new String[expectedTokensWithNonOption.size()]), tokensWithNonOption);

        // Given
        String[] argsWithLongOption = {
                "-a",
                "--option-d=longOptionValue"
        };

        // When
        String[] tokensWithLongOption = parser.flatten(options, argsWithLongOption, false);

        // Then
        List<String> expectedTokensWithLongOption = Arrays.asList(
                "-a",
                "--option-d",
                "longOptionValue"
        );

        Assert.assertArrayEquals(expectedTokensWithLongOption.toArray(new String[expectedTokensWithLongOption.size()]), tokensWithLongOption);

        // Given
        String[] argsWithBurstedOption = {
                "-abc",
                "--d=4"
        };

        // When
        String[] tokensWithBurstedOption = parser.flatten(options, argsWithBurstedOption, false);

        // Then
        List<String> expectedTokensWithBurstedOption = Arrays.asList(
                "-a",
                "--",
                "d",
                "4"
        );

        Assert.assertArrayEquals(expectedTokensWithBurstedOption.toArray(new String[expectedTokensWithBurstedOption.size()]), tokensWithBurstedOption);
    }
}