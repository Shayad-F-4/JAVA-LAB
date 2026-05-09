import java.util.Scanner;

class ChatRoom{

    private String message;
    private boolean messageAvailable = false;
    private int messageCount = 0;

    public synchronized void sendMessage(String user, String msg){

        while (messageAvailable){

            try{
                wait(); // wait until previous message is read then send new message
            } catch(Exception e){

            }
        }

        message = msg;

        System.out.println(Thread.currentThread().getName() +
                " (Priority: " + Thread.currentThread().getPriority() + ")" +
                " sent: " + message);

        messageAvailable = true;
        messageCount++;

        notify();      // wake one thread
        notifyAll();   // wake all threads
    }

    public synchronized void readMessage(String user){

        while(!messageAvailable){
            try{
                wait();
            }catch(Exception e){
                
            }
        }

        System.out.println(Thread.currentThread().getName() +
                " received: " + message);

        messageAvailable = false;

        notifyAll();
    }
}

class User implements Runnable{

    String name;
    ChatRoom room;

    User(String name, ChatRoom room){
        this.name = name;
        this.room = room;
    }

    public void run(){
        for (int i = 1; i <= 2; i++){
            String msg = "Hello from " + name + " msg-" + i;

            room.sendMessage(name, msg);

            try{
                Thread.sleep(1000);
            } catch (Exception e) {}

            Thread.yield(); // give chance to other threads

            room.readMessage(name);
        }
    }
}

public class ChatApp{

    public static void main(String args[]) throws Exception{

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of users: ");
        int n = sc.nextInt();
        sc.nextLine();

        ChatRoom room = new ChatRoom();

        User users[] = new User[n];
        Thread threads[] = new Thread[n];

        for (int i = 0; i < n; i++) {

            System.out.print("Enter name of user " + (i + 1) + ": ");
            String name = sc.nextLine();

            users[i] = new User(name, room);

            threads[i] = new Thread(users[i]);

            // setName() => set thread name as user name
            threads[i].setName(name + "-Thread");

            // setPriority() => set thread priority (even index: high priority, odd index: low priority)
            if (i % 2 == 0){
                threads[i].setPriority(Thread.MAX_PRIORITY);//means highest priority at even idx
            }else{
                threads[i].setPriority(Thread.MIN_PRIORITY);//means lowest priority at odd idx
            }

            threads[i].start();// start each thread
        }
        
        // join()+isAlive()
        for (int i = 0; i < n; i++){

            threads[i].join();// wait for each thread join is use to wait for the thread to finish before proceeding to the next line of code 

            System.out.println(threads[i].getName()+ " finished. Alive? "+ threads[i].isAlive());
        }

        System.out.println("Main Thread: "+
                Thread.currentThread().getName());// getName() => get current thread name
    }
}