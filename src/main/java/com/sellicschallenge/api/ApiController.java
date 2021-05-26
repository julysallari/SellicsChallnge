package com.sellicschallenge.api;

import com.sellicschallenge.computer.ScoreComputer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping(value = "/score")
    public ResponseEntity<ScoreResponse> getScore(@RequestParam(value = "q") String input){
        return ResponseEntity.ok(new ScoreResponse(input,ScoreComputer.getInstance().getScore(input)));
    }

    private class ScoreResponse {
        String keyword;
        int score;
        public ScoreResponse(String input, int score) {
            this.keyword = input;
            this.score = score;
        }

        public String getKeyword() {
            return keyword;
        }

        public int getScore() {
            return score;
        }
    }
}
