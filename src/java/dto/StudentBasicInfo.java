/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;
import java.util.Map;

/**
 *
 * @author vankh
 */
public class StudentBasicInfo {

    private String name;
    private int id;
    private Map<String, List<StudentScoreView>> scores;

    public StudentBasicInfo() {
    }

    public StudentBasicInfo(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, List<StudentScoreView>> getScores() {
        return scores;
    }

    public void setScores(Map<String, List<StudentScoreView>> scores) {
        this.scores = scores;
    }
}