import java.util.Scanner;

public class View {
    public static void Init(){
        boolean exit = true;
        Scanner sc = new Scanner(System.in);
        final String DOM = "1", SAX = "2", StAX = "3", EXIT = "0";
        while (exit){
            System.out.println("""
                Hello, User! Choose, what parser you want to use:
                
                1. DOM parser
                2. SAX parser
                3. StAX parser
                0. Exit
                """);
            String choice = sc.nextLine();
            switch (choice){
                case DOM -> DOMparser.Init();
                case SAX -> SAXparser.Init();
                case StAX -> StAXparser.Init();
                case EXIT -> exit = false;
                default -> System.out.println("Repeat input!");
            }
        }

    }
}
