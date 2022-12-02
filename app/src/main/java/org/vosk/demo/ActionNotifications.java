package org.vosk.demo;

public class ActionNotifications {

    public static void spreadAction(int i) {
        switch (i) {
            case 0:
                System.out.println(0);
                break;
            case 1:
                System.out.println(1);
                break;
            case 2:
                System.out.println(2);
                break;
            default:
                System.out.println("Неучтённый сценарий");
                break;
        }
    }
}
