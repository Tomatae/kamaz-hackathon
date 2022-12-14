package org.nssae.demo;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VoiceCommandRecognition {

    public Map<Integer, List<String>> commandExamples = new HashMap<>();
    public String keyword = "камаз";
    private int iPoint;
    private boolean ended = true;
    public Context context;

    public void createSheet() {
        commandExamples.put(0, Arrays.asList("сколько времени", "который час"));
        commandExamples.put(1, Arrays.asList("закрой правое окно", "прикрой правое окно"));
        commandExamples.put(2, Arrays.asList("открой правое окно", "приоткрой правое окно"));
        commandExamples.put(3, Arrays.asList("закрой левое окно", "прикрой левое окно"));
        commandExamples.put(4, Arrays.asList("открой левое окно", "приоткрой левое окно"));
        commandExamples.put(5, Arrays.asList("дворники", "переключи дворники"));
        commandExamples.put(6, Arrays.asList("включи дворники", "протри стекло"));
        commandExamples.put(7, Arrays.asList("выключи дворники", "стекло чистое", "выключи дворники"));
        commandExamples.put(8, Arrays.asList("музыку потише", "убавь громкост", "убавь музыку", "потише"));
        commandExamples.put(9, Arrays.asList("музыку погромче", "прибавь громкост", "добавь громкост", "прибавь музыку", "добавь музыку", "погромче"));
        commandExamples.put(10, Arrays.asList("заблокируй двери", "закрой замок", "заблокируй замок"));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDictionary(){
        ArrayList<String> ret = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry: commandExamples.entrySet()){

          for (String v:
                    entry.getValue()) {
               entry.getValue().set(entry.getValue().indexOf(v), String.format("\"камаз %s\"", v));
            }

            ret.addAll(entry.getValue());
        }
        return ret.toString();
    }

    public void checkSimilarities(int key, String stored, String received) {
        for (String w : stored.split(" ")) {
            if (received.length() <= iPoint) return;
            if (!received.substring(iPoint).contains(w)) return;
            iPoint = received.indexOf(w, iPoint);
        }
        System.out.println("Received a matching phrase");
        ActionNotifications.spreadAction(key, context);

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

    public void lookThroughCommandExamples(String line) {
        if (!updateIndexPointer(line)) return;
        for (Map.Entry<Integer, List<String>> entry : commandExamples.entrySet()) {
            Integer k = entry.getKey();
            List<String> v = entry.getValue();
            for (String e : v) {
                checkSimilarities(k, e, line);
            }
        }
    }

    public void addCommandExample(int commandId, String commandString) {
        List<String> commandStringList = Collections.singletonList(commandString);
        if (commandExamples.containsKey(commandId)) {
            commandStringList.addAll(Objects.requireNonNull(commandExamples.get(commandId)));
            commandExamples.remove(commandId);
        }
        commandExamples.put(commandId, commandStringList);
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
}
