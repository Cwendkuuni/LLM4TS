 package jaw.entrada;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AbrirTest {
    static ProcessarEntidades form;

    @BeforeAll
    public static void setup() {
        form = new ProcessarEntidades();
    }
}

// Tests go here
