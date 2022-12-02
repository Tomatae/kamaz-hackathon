package org.vosk.demo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoiceCommandRecognition {

    public Map<Integer, List<String>> commandExamples = new HashMap<>();
    public String keyword = "камаз";
    private int iPoint;
    private boolean ended = true;

    public void createSheet() {
        commandExamples.put(0, Arrays.asList("сколько времени", "который час"));
        commandExamples.put(1, Arrays.asList("закрой окно", "дует"));
        commandExamples.put(2, Arrays.asList("открой окно", "душно"));
        commandExamples.put(3, Arrays.asList("заблокируй дверь", "закройся", "закрой дверь", "закрой замок"));
    }

    public void checkSimilarities(int key, String stored, String received) {
        for (String w : stored.split(" ")) {
            if (received.length() <= iPoint) return;
            if (!received.substring(iPoint).contains(w)) return;
            iPoint = received.indexOf(w, iPoint);
        }
        System.out.println("Received a matching phrase");
        ActionNotifications.spreadAction(key);

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

    public void lookThroughCommandExamples(String line){
        if (!updateIndexPointer(line)) return;
        for (Map.Entry<Integer, List<String>> entry : commandExamples.entrySet()) {
            Integer k = entry.getKey();
            List<String> v = entry.getValue();
            for (String e : v) {
                checkSimilarities(k, e, line);
            }
        }
    }

    public void recognizeCommand(String line){
        try {
            JSONObject jsonObject = new JSONObject(line);
            if (jsonObject.has("partial")) lookThroughCommandExamples(jsonObject.get("partial").toString());
            if (jsonObject.has("text")) {
                ended = true;
                lookThroughCommandExamples(jsonObject.get("text").toString());
            }
        } catch (JSONException err) {
            System.out.println("Oooops, you've just caught an error");
        }
    }
}
