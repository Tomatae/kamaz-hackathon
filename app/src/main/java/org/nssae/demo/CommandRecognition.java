package org.nssae.demo;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRecognition {

    public String keyword = "камаз";
    private int iPoint;
    private boolean ended = true;
    public Context context;


    private long reacted = 0;
    public List<Boolean> slipperyFingers = Arrays.asList(false, false, false, false, false);

    public Map<Integer, Integer> gce = new HashMap<>();
    public void createGestures() {
        gce.put(0, 1); //00001
        gce.put(1, 2); //00010
        gce.put(2, 4); //00100
        gce.put(3, 8); //01000
        gce.put(4, 16); //10000
        gce.put(5, 30); //11110
        gce.put(6, 29); //11101
        gce.put(7, 27); //11011
        gce.put(8, 23); //10111
        gce.put(9, 15); //01111
        gce.put(10, 16); //10000
    }

    public Map<Integer, List<String>> vce = new HashMap<>();
    public void createSheet() {
        vce.put(0, Arrays.asList("сколько времени", "который час"));
        vce.put(1, Arrays.asList("закрой правое окно", "прикрой правое окно"));
        vce.put(2, Arrays.asList("открой правое окно", "приоткрой правое окно"));
        vce.put(3, Arrays.asList("закрой левое окно", "прикрой левое окно"));
        vce.put(4, Arrays.asList("открой левое окно", "приоткрой левое окно"));
        vce.put(5, Arrays.asList("дворники", "переключи дворники"));
        vce.put(6, Arrays.asList("включи дворники", "протри стекло"));
        vce.put(7, Arrays.asList("выключи дворники", "стекло чистое", "выключи дворники"));
        vce.put(8, Arrays.asList("музыку потише", "убавь громкост", "убавь музыку", "потише"));
        vce.put(9, Arrays.asList("музыку погромче", "прибавь громкост", "добавь громкост", "прибавь музыку", "добавь музыку", "погромче"));
        vce.put(10, Arrays.asList("заблокируй двери", "закрой замок", "заблокируй замок"));
    }

    public void createCommandRecognition() {
        createGestures();
        createSheet();
    }

    public String getDictionary(){
        ArrayList<String> ret = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry: vce.entrySet()){

            for (String v: entry.getValue()) {
                entry.getValue().set(entry.getValue().indexOf(v), String.format("\"камаз %s\"", v));
            }

            ret.addAll(entry.getValue());
        }
        return ret.toString();
    }

    public void recognizeCommand(String line) {
        try {
            JSONObject jsonObject = new JSONObject(line);
            if (jsonObject.has("partial"))
                lookThroughCommandExamples(jsonObject.get("partial").toString());
            if (jsonObject.has("text")) {
                ended = true;
                lookThroughCommandExamples(jsonObject.get("text").toString());
            }
        } catch (JSONException err) {
            System.out.println("Oooops, you've just caught an error");
        }
    }

    public void lookThroughCommandExamples(String line) {
        if (!updateIndexPointer(line)) return;
        for (Map.Entry<Integer, List<String>> entry : vce.entrySet()) {
            Integer k = entry.getKey();
            List<String> v = entry.getValue();
            for (String e : v) {
                checkSimilarities(k, e, line);
            }
        }
    }

    public boolean updateIndexPointer(String value) {
        if (ended) {
            iPoint = 0;
            ended = false;
        }
        if (value.equals("")) {
            iPoint = 0;
            return false;
        }
        int i = value.lastIndexOf(keyword) + keyword.length();
        if (i <= iPoint) return false;
        else iPoint = i;
        return true;
    }


    public void checkSimilarities(int key, String stored, String received) {
//        System.out.println(received);
        for (String w : stored.split(" ")) {

//            System.out.println(received);
//            System.out.println(stored);

            if (w.substring(1).equals("камаз")) w = w.substring(1);
            w = sanitizeString(w);
            stored = sanitizeString(stored);
            received = sanitizeString(received);

            if (!received.contains(w))
            {
//                System.out.println(received);
//                System.out.println(w);
                return;
            }
            System.out.println(w);
            System.out.println(received);
            System.out.println(received.contains(w));
        }
        System.out.println("Received a matching phrase");
        ActionNotifications.spreadAction(key, context);
    }

    @NonNull
    private String sanitizeString(String w) {
        if (w.endsWith("\"")){
            w = w.substring(0, w.length() - 1);
        }
        if (w.startsWith("\"")){
            w = w.substring(1);
        }
        return w;
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

    public void recognizeCommand() {
        if (System.currentTimeMillis() - reacted > 3000) {
            reacted = System.currentTimeMillis();
            int curr = convertCurrentDec();
            for (Map.Entry<Integer, Integer> entry : gce.entrySet()) {
                int k = entry.getKey();
                int v = entry.getValue();
                if (v == curr) {
                    System.out.println("Received a matching combination");

                    ActionNotifications.spreadAction(k, context);
                    return;
                }
            }

        }
    }

}
