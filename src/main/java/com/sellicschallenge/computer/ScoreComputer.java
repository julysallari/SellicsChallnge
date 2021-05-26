package com.sellicschallenge.computer;

import com.sellicschallenge.api.ApiWebClient;

import java.util.*;


public class ScoreComputer {

    private static final String REQUEST_PATH = "/search/complete?search-alias=aps&mkt=1&q=";
    private static ScoreComputer scoreComputer;
    private static final ApiWebClient apiWebClient = new ApiWebClient();

    public static ScoreComputer getInstance() {
        if(scoreComputer == null) {
            scoreComputer = new ScoreComputer();
        }
        return scoreComputer;
    }

    public int getScore(String input) {
        Map<String, Integer> phrases = new HashMap<>();
        List<String> completions = apiWebClient.apiCall(REQUEST_PATH+input);
        if (completions.isEmpty()) {
            return 0;
        }
        int maxTimes = 9;
        int times = 0;
        int addedTiers = -1;
        int maxTier = 0;
        int newFounds = 0;
        int score = calculateScore(completions, input, 0 ,0, 0);
        while((times < maxTimes) && (times < input.length())) {
            completions = apiWebClient.apiCall("/search/complete?search-alias=aps&mkt=1&q="+input.substring(0, times+1));
            newFounds = checkTiers(phrases, completions, maxTier);
            if (newFounds > 0) {
                maxTier++;
                if (newFounds == completions.size()) {
                    addedTiers++;
                }
            }
            if(phraseFound(completions, input)) {
                return calculateScore(completions, input, maxTier, addedTiers, 100);
            }
            times++;
        }
        return score;
    }

    private int calculateScore(List<String> completions, String input, int maxTier, int addedTiers, int maxScore) {
        int score = maxScore - (maxTier+addedTiers)*10;
        int max = completions.size();
        for(int i=0; i<completions.size(); i++) {
            if (completions.get(i).equals(input)) {
                return score+max;
            }
            max--;
        }
        return score;
    }

    private boolean phraseFound(List<String> completions, String input) {
        for (String c : completions){
            if (c.equals(input)) {
                return true;
            }
        }
        return false;
    }

    private int checkTiers(Map<String, Integer> phrases, List<String> completions, int maxTier) {
        int newFounds = 0;
        for (String c : completions) {
            Integer tier = phrases.get(c);
            if(tier == null) {
                newFounds++;
                phrases.put(c, maxTier+1);
            }
        }

        Set<String> keys = new TreeSet<>(phrases.keySet());
        for (String p : keys) {
            if (!completions.contains(p)) {
                phrases.remove(p);
            }
        }
        return newFounds;
    }
}
