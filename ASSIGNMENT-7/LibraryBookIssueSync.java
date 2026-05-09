class LibraryBookIssueSync implements Runnable {

    static int books = 5;

    public void run() {

        Thread t = Thread.currentThread();

        System.out.println("Thread Name: " + t.getName());
        System.out.println("Priority: " + t.getPriority());

        for (int i = 1; i <= 2; i++) {

            synchronized (LibraryBookIssueSync.class) {

                if (books == 0) {

                    try {
                        System.out.println(t.getName() + " waiting for books...");
                        LibraryBookIssueSync.class.wait();
                    } catch (Exception e) {}
                    
                }

                if (books > 0) {

                    System.out.println(t.getName() + " issuing book...");

                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {}

                    books--;

                    System.out.println(t.getName() + " issued book successfully");
                    System.out.println("Remaining books: " + books + "\n");

                    LibraryBookIssueSync.class.notifyAll();
                }
            }

            Thread.yield();
        }
    }

    public static void main(String args[]) {

        LibraryBookIssueSync obj = new LibraryBookIssueSync();

        Thread t1 = new Thread(obj);
        Thread t2 = new Thread(obj);
        Thread t3 = new Thread(obj);

        t1.setName("Student-1");
        t2.setName("Student-2");
        t3.setName("Student-3");

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.NORM_PRIORITY);
        t3.setPriority(Thread.MAX_PRIORITY);

        System.out.println("Library Book Issue Started (Synchronized)\n");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            
        } catch (Exception e) {}

        System.out.println("All book issues completed");
    }
}