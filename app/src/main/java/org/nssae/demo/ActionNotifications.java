package org.nssae.demo;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

public class ActionNotifications {

    public static void spreadAction(int i, Context context) {
        System.out.println(i);
        switch (i) {
            case 0:
                showInfo("Сколько времени?", context);
                break;
            case 1:
                showInfo("Правое окно закрывается", context);
                break;
            case 2:
                showInfo("Правое окно открывается", context);
                break;
            case 3:
                showInfo("Левое окно закрывается", context);
                break;
            case 4:
                showInfo("Левое окно открывается", context);
                break;
            case 5:
                showInfo("Переключаем дворники", context);
                break;
            case 6:
                showInfo("Включаем дворники", context);
                break;
            case 7:
                showInfo("Выключаем дворники", context);
                break;
            case 8:
                showInfo("Музыка на минимум", context);
                break;
            case 9:
                showInfo("Музыка на максимум", context);
                break;
            case 10:
                showInfo("Двери закрыты", context);
                break;
            default:
                System.out.println("Неучтённый сценарий");
                break;
        }
    }

    private static void showInfo(String text, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            context.getMainExecutor().execute(() -> Toast.makeText(context, text, Toast.LENGTH_LONG).show());
    }
}
