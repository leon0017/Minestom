package net.minestom.server.thread;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

@FunctionalInterface
@ApiStatus.Experimental
public interface ThreadProvider<T> {
    static <T> @NotNull ThreadProvider<T> counter() {
        return new ThreadProvider<>() {
            private final AtomicInteger counter = new AtomicInteger();

            @Override
            public int findThread(@NotNull T partition) {
                return counter.getAndIncrement();
            }
        };
    }

// forkstart
    static <T> @NotNull ThreadProvider<T> instanceBased() {
        return partition -> {
            Chunk chunk = (Chunk) partition;
            InstanceContainer instanceContainer = (InstanceContainer) chunk.getInstance();
            return instanceContainer.getInstanceBasedThreadId();
        };
    }
// forkend

    /**
     * Performs a server tick for all chunks based on their linked thread.
     *
     * @param partition the partition
     */
    int findThread(@NotNull T partition);

    /**
     * Defines how often chunks thread should be updated.
     *
     * @return the refresh type
     */
    default @NotNull RefreshType refreshType() {
        return RefreshType.NEVER;
    }

    /**
     * Defines how often chunks thread should be refreshed.
     */
    enum RefreshType {
        /**
         * Thread never change after being defined once.
         * <p>
         * Means that {@link #findThread(Object)} will only be called once for each partition.
         */
        NEVER,
        /**
         * Thread is updated as often as possible.
         * <p>
         * Means that {@link #findThread(Object)} may be called multiple time for each partition.
         */
        ALWAYS
    }
}
