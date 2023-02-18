package org.nssae.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class ActionNotifications {

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static void spreadAction(int i, Context context) {
        switch (i) {
            case 0:
                System.out.println(0);
                showInfo("Сколько времени?", context);
                break;
            case 1:
                System.out.println(1);
                showInfo("Правое окно закрывается", context);
                break;
            case 2:
                System.out.println(2);
                showInfo("Правое окно открывается", context);
                break;
            case 3:
                System.out.println(3);
                showInfo("Левое окно закрывается", context);
                break;
            case 4:
                System.out.println(4);
                showInfo("Левое окно открывается", context);
                break;
            case 5:
                System.out.println(5);
                showInfo("Переключаем дворники", context);
                break;
            case 6:
                System.out.println(6);
                showInfo("Включаем дворники", context);
                break;
            case 7:
                System.out.println(7);
                showInfo("Выключаем дворники", context);
                break;
            case 8:
                System.out.println(8);
                showInfo("Музыка на минимум", context);
                break;
            case 9:
                System.out.println(9);
                showInfo("Музыка на максимум", context);
                break;
            case 10:
                System.out.println(10);
                showInfo("Двери закрыты", context);
                break;
            default:
                System.out.println("Неучтённый сценарий");
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private static void showInfo(String text, Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            this.getMainExecutor().execute(() -> Toast.makeText(this, "Showing data for Card ID = " + String.valueOf(id), Toast.LENGTH_LONG).show());
//        }
        Intent i = new Intent(Intent.CATEGORY_INFO, Uri.parse("localhost"));
        context.sendBroadcast(i);
        context.getMainExecutor().execute(() -> Toast.makeText(context, text, Toast.LENGTH_LONG).show());
    }
}
