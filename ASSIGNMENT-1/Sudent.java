import java.util.Scanner;

class Student
{
    String name;
    int rollNo;
    int marks;

    Student(Scanner sc)
    {
        System.out.print("Enter Student Name: ");
        name = sc.next();

        System.out.print("Enter Roll Number: ");
        rollNo = sc.nextInt();

        System.out.print("Enter Marks: ");
        marks = sc.nextInt();
    }

    void display()
    {
        System.out.println("Name: " + name);
        System.out.println("Roll Number: " + rollNo);
        System.out.println("Marks: " + marks);
    }
}

public class Student {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of students: ");
        int n = sc.nextInt();

        Student[] s = new Student[n];

        for(int i = 0; i < n; i++){
            s[i] = new Student(sc);
        }

        System.out.println("\nStudent Details:");

        for(int i = 0; i < n; i++){
            s[i].display();
        }
    }
}