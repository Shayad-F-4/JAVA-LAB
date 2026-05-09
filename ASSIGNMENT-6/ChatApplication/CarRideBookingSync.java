class CarRideBookingSync implements Runnable {

    static int cars = 5;

    public void run() {

        Thread t = Thread.currentThread();

        System.out.println("Thread Name: " + t.getName());
        System.out.println("Priority: " + t.getPriority());

        for (int i = 1; i <= 2; i++) {

            synchronized (CarRideBookingSync.class) {

                if (cars == 0) {

                    try {
                        System.out.println(t.getName() + " waiting for cars...");
                        CarRideBookingSync.class.wait();
                    } catch (Exception e) {}
                    
                }

                if (cars > 0) {

                    System.out.println(t.getName() + " booking car...");

                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {}

                    cars--;

                    System.out.println(t.getName() + " booked car successfully");
                    System.out.println("Remaining cars: " + cars + "\n");

                    CarRideBookingSync.class.notifyAll();
                }
            }

            Thread.yield();
        }
    }

    public static void main(String args[]) {

        CarRideBookingSync obj = new CarRideBookingSync();

        Thread t1 = new Thread(obj);
        Thread t2 = new Thread(obj);
        Thread t3 = new Thread(obj);

        t1.setName("User-1");
        t2.setName("User-2");
        t3.setName("User-3");

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.NORM_PRIORITY);
        t3.setPriority(Thread.MAX_PRIORITY);

        System.out.println("Car Ride Booking Started (Synchronized)\n");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (Exception e) {}

        System.out.println("All car bookings completed");
    }
}