package console;

public class Outputs {
    public static void displayMenuBeforeLogin() {
        int iterator = 0;
        System.out.println();
        System.out.println("============ WORKBOARD ============");
        System.out.println(++iterator + ". Register");
        System.out.println(++iterator + ". Login");
        System.out.println(++iterator + ". Quit");
        System.out.println("===================================");
    }

    public static void displayMenuAfterLogin() {
        int iterator = 0;
        System.out.println("============ WORKBOARD ============");
        System.out.println(++iterator + ". Log out");
        System.out.println(++iterator + ". Account info");
        System.out.println(++iterator + ". Create workspace");
        System.out.println(++iterator + ". List workspaces");
        System.out.println(++iterator + ". Delete workspace");
        System.out.println(++iterator + ". Rename workspace");
        System.out.println(++iterator + ". Create task");
        System.out.println(++iterator + ". List tasks");
        System.out.println(++iterator + ". Create subtask");
        System.out.println(++iterator + ". List subtasks");
        System.out.println(++iterator + ". Quit");
        System.out.println("===================================");
    }
}
