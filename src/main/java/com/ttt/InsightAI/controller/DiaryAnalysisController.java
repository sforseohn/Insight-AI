package com.ttt.InsightAI.controller;

import com.ttt.InsightAI.domain.Analysis;
import com.ttt.InsightAI.domain.Diary;
import com.ttt.InsightAI.domain.DiaryAnalysisRequest;
import com.ttt.InsightAI.domain.User;
import com.ttt.InsightAI.repository.AnalysisRepository;
import com.ttt.InsightAI.repository.DiaryRepository;
import com.ttt.InsightAI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/diary-analysis")
public class DiaryAnalysisController {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final AnalysisRepository analysisRepository;

    @Autowired
    public DiaryAnalysisController(UserRepository userRepository, DiaryRepository diaryRepository, AnalysisRepository analysisRepository) {
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
        this.analysisRepository = analysisRepository;
    }

    @PostMapping
    public Analysis analyzeDiary(@RequestBody DiaryAnalysisRequest request) {
        if (request.getUserId() == null || request.getTitle() == null || request.getContent() == null || request.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is invalid");
        }

        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Diary diary = new Diary();
        diary.setUser(user);
        diary.setTitle(request.getTitle());
        diary.setContent(request.getContent());
        diary.setDate(request.getDate());

        Analysis analysis = new Analysis();
        analysis.setUser(user);
        analysis.setDiary(diary);
        // Here, call the method that analyzes the diary and fill the fields of analysis...

        diary.setAnalysis(analysis); // Set the analysis for the diary

        diaryRepository.save(diary); // This will also save the analysis thanks to CascadeType.ALL

        return analysis;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Diary>> getAllDiariesAndAnalyses(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(diaryRepository.findByUser(user));
    }
}
