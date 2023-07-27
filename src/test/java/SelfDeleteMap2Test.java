
import com.neetesh.problem1.services.ISelfDeleteMap;
import com.neetesh.problem1.services.impl2.SelfDeleteMap2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelfDeleteMap2Test {

    private ISelfDeleteMap<Integer, String> selfDeleteMap;

    @BeforeEach
    void setUp() {
        selfDeleteMap = new SelfDeleteMap2<>();
    }

    @Test
    void putAndGetEntry() {
        selfDeleteMap.put(1, "Value1", 2000);
        assertEquals("Value1", selfDeleteMap.get(1));
    }

    @Test
    void getExpiredEntry() throws InterruptedException {
        selfDeleteMap.put(2, "Value2", 1000); // Expiry time set to 1 second
        Thread.sleep(2000); // Wait for the entry to expire
        assertNull(selfDeleteMap.get(2));
    }

    @Test
    void removeEntry() {
        selfDeleteMap.put(3, "Value3", 5000);
        selfDeleteMap.remove(3);
        assertNull(selfDeleteMap.get(3));
    }

    @Test
    void cleanupExpiredEntries() throws InterruptedException {
        selfDeleteMap.put(4, "Value4", 1000); // Expiry time set to 1 second
        selfDeleteMap.put(5, "Value5", 2000); // Expiry time set to 2 seconds
        Thread.sleep(3000); // Wait for both entries to expire
        assertNull(selfDeleteMap.get(4));
        assertNull(selfDeleteMap.get(5));
    }

    @Test
    void removeExpiredEntriesAfterCleanup() throws InterruptedException {
        selfDeleteMap.put(6, "Value6", 1000); // Expiry time set to 1 second
        selfDeleteMap.put(7, "Value7", 2000); // Expiry time set to 2 seconds
        Thread.sleep(3000); // Wait for both entries to expire
        selfDeleteMap.remove(6);
        selfDeleteMap.remove(7);
        assertNull(selfDeleteMap.get(6));
        assertNull(selfDeleteMap.get(7));
    }
}
