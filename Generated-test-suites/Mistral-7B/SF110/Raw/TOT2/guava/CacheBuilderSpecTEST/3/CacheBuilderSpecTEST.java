
package com.google.common.cache;

import com.google.common.annotations.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
public final class CacheBuilderSpec {
    // ... (previous code)

    // ... (other methods)

    static {
        // ... (previous static initializers)
    }

    // ... (other static methods)

    abstract static class IntegerParser implements ValueParser {
        // ... (previous code)
    }

    abstract static class LongParser implements ValueParser {
        // ... (previous code)
    }

    static class InitialCapacityParser extends IntegerParser {
        // ... (previous code)
    }

    static class MaximumSizeParser extends LongParser {
        // ... (previous code)
    }

    static class MaximumWeightParser extends LongParser {
        // ... (previous code)
    }

    static class ConcurrencyLevelParser extends IntegerParser {
        // ... (previous code)
    }

    static class KeyStrengthParser implements ValueParser {
        // ... (previous code)
    }

    static class ValueStrengthParser implements ValueParser {
        // ... (previous code)
    }

    static class RecordStatsParser implements ValueParser {
        // ... (previous code)
    }

    abstract static class DurationParser implements ValueParser {
        // ... (previous code)
    }

    static class AccessDurationParser extends DurationParser {
        // ... (previous code)
    }

    static class WriteDurationParser extends DurationParser {
        // ... (previous code)
    }

    static class RefreshDurationParser extends DurationParser {
        // ... (previous code)
    }

    private interface ValueParser {
        void parse(final CacheBuilderSpec p0, final String p1, @Nullable final String p2);
    }
}
