import com.neetesh.problem1.services.ISelfDeleteMap;
import com.neetesh.problem1.services.impl.SelfDeleteMap;
import com.neetesh.problem1.services.impl.TimeoutManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SelfDeleteMapTest {

    private ISelfDeleteMap<String, Integer> map ;
    @BeforeEach
    void setUp() {
        map = new SelfDeleteMap<>(new TimeoutManager<>());
    }

    @Test
    void testPutAndGet() {
        map.put("A", 1, 1000);
        map.put("B", 2, 2000);

        assertEquals(1, map.get("A"));
        assertEquals(2, map.get("B"));
    }

    @Test
    void testGetAfterExpiration() throws InterruptedException {
        map.put("A", 1, 1000);
        map.put("B", 2, 2000);

        Thread.sleep(1500); // Wait for entry "A" to expire.

        assertNull(map.get("A")); // Entry "A" should have expired and been removed.
        assertEquals(2, map.get("B"));
    }

    @Test
    void testConcurrentAccess() throws InterruptedException {
        int threadCount = 100;
        int iterationCount = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                for (int j = 0; j < iterationCount; j++) {
                    map.put( "Value_" + finalI, finalI, 500);
                    map.get("Value_" + finalI);
                    map.remove("Value_" + finalI);
                }
                latch.countDown();
            });
        }

        latch.await();
        executorService.shutdown();

        // Assert that all entries are removed after their timeout.
        for (int i = 0; i < threadCount; i++) {
            assertNull(map.get("Value_" + i));
        }
    }
}
