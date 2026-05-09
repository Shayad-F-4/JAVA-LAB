/**
 * Enhanced Synchronous Library Book Issue System
 * This demonstrates proper synchronization with wait/notify mechanism
 * Thread-safe book issuing with proper resource management
 * 
 * Features:
 * - Proper synchronization using synchronized blocks
 * - Wait/notify mechanism for resource availability
 * - Thread priorities and yielding
 * - Comprehensive logging and error handling
 */
public class LibraryBookIssueSyncEnhanced implements Runnable {
    
    // Shared resource - total books available
    private static int totalBooks = 5;
    private static final Object lock = new Object();
    
    // Instance variables for each thread
    private final String studentName;
    private final int booksRequested;
    private int booksIssued = 0;
    
    // Statistics tracking
    private static int successfulIssues = 0;
    private static int failedIssues = 0;
    private static long totalWaitTime = 0;
    
    public LibraryBookIssueSyncEnhanced(String studentName, int booksRequested) {
        this.studentName = studentName;
        this.booksRequested = booksRequested;
    }
    
    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        long startTime = System.currentTimeMillis();
        
        System.out.println("=== " + studentName + " Started ===");
        System.out.println("Thread Name: " + currentThread.getName());
        System.out.println("Thread Priority: " + currentThread.getPriority());
        System.out.println("Books Requested: " + booksRequested);
        System.out.println("Available Books: " + totalBooks);
        System.out.println();
        
        // Try to issue requested books
        for (int i = 1; i <= booksRequested; i++) {
            if (issueBook(currentThread, i)) {
                booksIssued++;
            } else {
                break; // Stop if book issuance fails
            }
            
            // Small delay between book issuances
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        totalWaitTime += totalTime;
        
        // Final status for this student
        System.out.println("=== " + studentName + " Completed ===");
        System.out.println("Books Issued: " + booksIssued + "/" + booksRequested);
        System.out.println("Total Time: " + totalTime + "ms");
        System.out.println();
        
        // Yield control to other threads
        Thread.yield();
    }
    
    /**
     * Issues a book with proper synchronization
     */
    private boolean issueBook(Thread thread, int bookNumber) {
        synchronized (lock) {
            // Wait if no books are available
            while (totalBooks == 0) {
                System.out.println(studentName + " waiting for books... (Book #" + bookNumber + ")");
                failedIssues++;
                
                try {
                    long waitStart = System.currentTimeMillis();
                    lock.wait(2000); // Wait with timeout
                    long waitEnd = System.currentTimeMillis();
                    
                    // Check if we timed out
                    if (waitEnd - waitStart >= 2000) {
                        System.out.println(studentName + " timeout waiting for books");
                        return false;
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(studentName + " interrupted while waiting");
                    return false;
                }
            }
            
            // Issue the book
            if (totalBooks > 0) {
                System.out.println(studentName + " issuing book #" + bookNumber + "...");
                
                // Simulate book processing time
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
                
                totalBooks--;
                booksIssued++;
                successfulIssues++;
                
                System.out.println(studentName + " successfully issued book #" + bookNumber);
                System.out.println("Remaining books: " + totalBooks);
                
                // Notify waiting threads that a book has been issued
                lock.notifyAll();
                
                return true;
            }
            
            return false;
        }
    }
    
    /**
     * Restocks books (demonstrates notifyAll usage)
     */
    public static void restockBooks(int count) {
        synchronized (lock) {
            totalBooks += count;
            System.out.println("=== LIBRARY RESTOCK ===");
            System.out.println("Added " + count + " books to inventory");
            System.out.println("Total books available: " + totalBooks);
            System.out.println("Notifying all waiting students...");
            lock.notifyAll();
            System.out.println();
        }
    }
    
    /**
     * Get system statistics
     */
    public static void printStatistics() {
        System.out.println("=== SYSTEM STATISTICS ===");
        System.out.println("Total Successful Issues: " + successfulIssues);
        System.out.println("Total Failed Issues: " + failedIssues);
        System.out.println("Remaining Books: " + totalBooks);
        System.out.println("Average Wait Time: " + (totalWaitTime / Math.max(1, successfulIssues + failedIssues)) + "ms");
        System.out.println();
    }
    
    public static void main(String[] args) {
        System.out.println("===============================================");
        System.out.println("ENHANCED SYNCHRONOUS LIBRARY BOOK ISSUE SYSTEM");
        System.out.println("===============================================");
        System.out.println();
        
        // Create student threads with different priorities and book requests
        LibraryBookIssueSyncEnhanced student1 = new LibraryBookIssueSyncEnhanced("Alice", 2);
        LibraryBookIssueSyncEnhanced student2 = new LibraryBookIssueSyncEnhanced("Bob", 3);
        LibraryBookIssueSyncEnhanced student3 = new LibraryBookIssueSyncEnhanced("Charlie", 1);
        LibraryBookIssueSyncEnhanced student4 = new LibraryBookIssueSyncEnhanced("Diana", 2);
        
        // Create threads
        Thread t1 = new Thread(student1, "Student-Alice");
        Thread t2 = new Thread(student2, "Student-Bob");
        Thread t3 = new Thread(student3, "Student-Charlie");
        Thread t4 = new Thread(student4, "Student-Diana");
        
        // Set thread priorities
        t1.setPriority(Thread.MAX_PRIORITY);      // Alice gets highest priority
        t2.setPriority(Thread.NORM_PRIORITY);      // Bob gets normal priority
        t3.setPriority(Thread.MIN_PRIORITY);      // Charlie gets lowest priority
        t4.setPriority(Thread.NORM_PRIORITY);      // Diana gets normal priority
        
        System.out.println("Initial Book Count: " + totalBooks);
        System.out.println("Starting book issuance process...\n");
        
        // Start all threads
        t1.start();
        t2.start();
        t3.start();
        
        // Start Diana after a delay to demonstrate wait/notify
        try {
            Thread.sleep(1000);
            t4.start();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Restock books after some time to demonstrate notify mechanism
        try {
            Thread.sleep(3000);
            restockBooks(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Wait for all threads to complete
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("===============================================");
        System.out.println("ALL BOOK ISSUANCE COMPLETED");
        System.out.println("===============================================");
        
        // Print final statistics
        printStatistics();
    }
}
