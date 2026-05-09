/**
 * Enhanced Asynchronous Library Book Issue System
 * This demonstrates asynchronous processing without synchronization
 * Shows race conditions and the need for proper synchronization
 * 
 * Features:
 * - Asynchronous book processing without locks
 * - Demonstrates race conditions
 * - Thread priorities and yielding
 * - Comprehensive logging and error handling
 * - Comparison with synchronized version
 */
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class LibraryBookIssueAsyncEnhanced implements Runnable {
    
    // Shared resource - using AtomicInteger for basic thread safety
    private static AtomicInteger totalBooks = new AtomicInteger(5);
    
    // Track statistics using thread-safe collections
    private static final ConcurrentHashMap<String, Integer> studentBookCounts = new ConcurrentHashMap<>();
    private static final AtomicInteger successfulIssues = new AtomicInteger(0);
    private static final AtomicInteger failedIssues = new AtomicInteger(0);
    private static final AtomicInteger raceConditions = new AtomicInteger(0);
    
    // Instance variables
    private final String studentName;
    private final int booksRequested;
    private int booksIssued = 0;
    private long processingTime = 0;
    
    public LibraryBookIssueAsyncEnhanced(String studentName, int booksRequested) {
        this.studentName = studentName;
        this.booksRequested = booksRequested;
        studentBookCounts.put(studentName, 0);
    }
    
    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        long startTime = System.currentTimeMillis();
        
        System.out.println("=== " + studentName + " Started (Async) ===");
        System.out.println("Thread Name: " + currentThread.getName());
        System.out.println("Thread Priority: " + currentThread.getPriority());
        System.out.println("Thread ID: " + currentThread.getId());
        System.out.println("Books Requested: " + booksRequested);
        System.out.println("Available Books: " + totalBooks.get());
        System.out.println();
        
        // Try to issue requested books asynchronously
        for (int i = 1; i <= booksRequested; i++) {
            if (issueBookAsync(currentThread, i)) {
                booksIssued++;
                studentBookCounts.put(studentName, booksIssued);
            } else {
                break; // Stop if book issuance fails
            }
            
            // Random delay to simulate real-world processing
            try {
                int delay = ThreadLocalRandom.current().nextInt(50, 200);
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            
            // Yield control frequently in async mode
            Thread.yield();
        }
        
        long endTime = System.currentTimeMillis();
        processingTime = endTime - startTime;
        
        // Final status for this student
        System.out.println("=== " + studentName + " Completed (Async) ===");
        System.out.println("Books Issued: " + booksIssued + "/" + booksRequested);
        System.out.println("Processing Time: " + processingTime + "ms");
        System.out.println("Current Available Books: " + totalBooks.get());
        System.out.println();
    }
    
    /**
     * Issues a book asynchronously (without proper synchronization)
     * This method demonstrates race conditions
     */
    private boolean issueBookAsync(Thread thread, int bookNumber) {
        int currentBooks = totalBooks.get();
        
        System.out.println(studentName + " attempting to issue book #" + bookNumber + 
                          " (Available: " + currentBooks + ")");
        
        // Simulate processing time where race conditions can occur
        try {
            Thread.sleep(100 + ThreadLocalRandom.current().nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        
        // Check books again after processing delay
        currentBooks = totalBooks.get();
        
        if (currentBooks > 0) {
            // This is where race conditions can happen!
            // Multiple threads might pass this check and try to decrement
            int newCount = totalBooks.decrementAndGet();
            
            if (newCount >= 0) {
                successfulIssues.incrementAndGet();
                
                System.out.println(studentName + " successfully issued book #" + bookNumber);
                System.out.println("Remaining books: " + totalBooks.get());
                
                // Simulate book processing
                try {
                    Thread.sleep(200 + ThreadLocalRandom.current().nextInt(300));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                return true;
            } else {
                // Race condition detected! We went below zero
                raceConditions.incrementAndGet();
                totalBooks.incrementAndGet(); // Correct the count
                System.out.println("⚠️ RACE CONDITION: " + studentName + 
                                 " detected negative book count, correcting...");
                failedIssues.incrementAndGet();
                return false;
            }
        } else {
            System.out.println(studentName + " failed to issue book #" + bookNumber + 
                             " (No books available)");
            failedIssues.incrementAndGet();
            return false;
        }
    }
    
    /**
     * Demonstrates the problems with async approach
     */
    public static void demonstrateRaceConditions() {
        System.out.println("=== RACE CONDITION DEMONSTRATION ===");
        System.out.println("In async mode, multiple threads can:");
        System.out.println("1. Check book count simultaneously");
        System.out.println("2. All see books available");
        System.out.println("3. All try to issue books");
        System.out.println("4. Result: More books issued than available!");
        System.out.println();
    }
    
    /**
     * Get system statistics
     */
    public static void printAsyncStatistics() {
        System.out.println("=== ASYNC SYSTEM STATISTICS ===");
        System.out.println("Total Successful Issues: " + successfulIssues.get());
        System.out.println("Total Failed Issues: " + failedIssues.get());
        System.out.println("Race Conditions Detected: " + raceConditions.get());
        System.out.println("Final Book Count: " + totalBooks.get());
        System.out.println("Expected Book Count: " + (5 - successfulIssues.get()));
        System.out.println();
        
        // Show individual student results
        System.out.println("=== INDIVIDUAL STUDENT RESULTS ===");
        studentBookCounts.forEach((student, count) -> 
            System.out.println(student + ": " + count + " books"));
        System.out.println();
        
        // Check for data inconsistency
        int expectedCount = 5 - successfulIssues.get();
        int actualCount = totalBooks.get();
        if (expectedCount != actualCount) {
            System.out.println("⚠️ DATA INCONSISTENCY DETECTED!");
            System.out.println("Expected: " + expectedCount + " books");
            System.out.println("Actual: " + actualCount + " books");
            System.out.println("Difference: " + Math.abs(expectedCount - actualCount) + " books");
        } else {
            System.out.println("✓ Data consistency maintained (lucky run!)");
        }
        System.out.println();
    }
    
    /**
     * Restock books (async version)
     */
    public static void restockBooksAsync(int count) {
        int oldCount = totalBooks.get();
        totalBooks.addAndGet(count);
        
        System.out.println("=== ASYNC LIBRARY RESTOCK ===");
        System.out.println("Previous count: " + oldCount);
        System.out.println("Added: " + count + " books");
        System.out.println("New total: " + totalBooks.get());
        System.out.println("No notification sent (async mode)");
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println("ENHANCED ASYNCHRONOUS LIBRARY BOOK ISSUE SYSTEM");
        System.out.println("===============================================");
        System.out.println();
        
        demonstrateRaceConditions();
        
        // Create student threads with different priorities and book requests
        LibraryBookIssueAsyncEnhanced student1 = new LibraryBookIssueAsyncEnhanced("Alice", 2);
        LibraryBookIssueAsyncEnhanced student2 = new LibraryBookIssueAsyncEnhanced("Bob", 3);
        LibraryBookIssueAsyncEnhanced student3 = new LibraryBookIssueAsyncEnhanced("Charlie", 1);
        LibraryBookIssueAsyncEnhanced student4 = new LibraryBookIssueAsyncEnhanced("Diana", 2);
        LibraryBookIssueAsyncEnhanced student5 = new LibraryBookIssueAsyncEnhanced("Eve", 1);
        
        // Create threads
        Thread t1 = new Thread(student1, "Async-Alice");
        Thread t2 = new Thread(student2, "Async-Bob");
        Thread t3 = new Thread(student3, "Async-Charlie");
        Thread t4 = new Thread(student4, "Async-Diana");
        Thread t5 = new Thread(student5, "Async-Eve");
        
        // Set thread priorities to show scheduling effects
        t1.setPriority(Thread.MAX_PRIORITY);      // Alice gets highest priority
        t2.setPriority(Thread.NORM_PRIORITY);      // Bob gets normal priority
        t3.setPriority(Thread.MIN_PRIORITY);      // Charlie gets lowest priority
        t4.setPriority(Thread.NORM_PRIORITY);      // Diana gets normal priority
        t5.setPriority(Thread.NORM_PRIORITY);      // Eve gets normal priority
        
        System.out.println("Initial Book Count: " + totalBooks.get());
        System.out.println("Starting asynchronous book issuance process...\n");
        
        // Start all threads simultaneously
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        
        // Restock books asynchronously to show lack of coordination
        try {
            Thread.sleep(2000);
            restockBooksAsync(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Wait for all threads to complete
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("===============================================");
        System.out.println("ALL ASYNC BOOK ISSUANCE COMPLETED");
        System.out.println("===============================================");
        
        // Print final statistics
        printAsyncStatistics();
        
        // Comparison with sync version
        System.out.println("=== SYNC VS ASYNC COMPARISON ===");
        System.out.println("Sync Version: Safe, coordinated, slower");
        System.out.println("Async Version: Fast, but prone to race conditions");
        System.out.println("Recommendation: Use synchronization for shared resources!");
        System.out.println();
    }
}
