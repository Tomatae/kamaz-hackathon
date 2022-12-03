package org.vosk.demo;

import android.content.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestureCommandRecognition {

    public List<Boolean> slipperyFingers = Arrays.asList(false, false, false, false, false);

    public Map<Integer, Integer> gestureCommandExamples = new HashMap<>();

    public Context context;

    public void createGestures() {
        gestureCommandExamples.put(0, 6); //00110
        gestureCommandExamples.put(1, 7); //00111
        gestureCommandExamples.put(2, 8); //01000
        gestureCommandExamples.put(3, 9); //01001
        gestureCommandExamples.put(4, 10); //01010
        gestureCommandExamples.put(5, 11); //01011
        gestureCommandExamples.put(6, 12); //01100
        gestureCommandExamples.put(7, 13); //01101
        gestureCommandExamples.put(8, 14); //01110
        gestureCommandExamples.put(9, 15); //01111
        gestureCommandExamples.put(10, 16); //10000
    }

    public int convertCurrentDec() {
        int res = 0;
        if (slipperyFingers.get(0)) res += 16;
        if (slipperyFingers.get(1)) res += 8;
        if (slipperyFingers.get(2)) res += 4;
        if (slipperyFingers.get(3)) res += 2;
        if (slipperyFingers.get(4)) res += 1;
        return res;
    }

    public int convertCurrentMask() {
        int res = 0;
        if (slipperyFingers.get(0)) res += 10000;
        if (slipperyFingers.get(1)) res += 1000;
        if (slipperyFingers.get(2)) res += 100;
        if (slipperyFingers.get(3)) res += 10;
        if (slipperyFingers.get(4)) res += 1;
        return res;
    }

    public int convertBooleansToDec(List<Boolean> bools) {
        int res = 0;
        if (bools.get(0)) res += 16;
        if (bools.get(1)) res += 8;
        if (bools.get(2)) res += 4;
        if (bools.get(3)) res += 2;
        if (bools.get(4)) res += 1;
        return res;
    }

    public void update() {
        int curr = convertCurrentDec();
        for (Map.Entry<Integer, Integer> entry : gestureCommandExamples.entrySet()) {
            int k = entry.getKey();
            int v = entry.getValue();
            if (v == curr) {
                System.out.println("Received a matching combination");
//                ActionNotifications.spreadAction(k, context);
                return;
            }
        }
    }
}
