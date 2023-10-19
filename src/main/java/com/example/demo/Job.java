package com.example.demo;
/**
 * Author: Maaz
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class Job {
    private final StringProperty jobTitle;
    private int jobId;
    private StringProperty jobGrade;
    private StringProperty jobAgency;
    private StringProperty jobLocation;
    private final Button applyButton;

    public Job(String jobTitle, int jobId, String jobGrade, String jobAgency, String jobLocation){
        this.jobTitle = new SimpleStringProperty(jobTitle);
        this.jobId = jobId;
        this.jobGrade = new SimpleStringProperty(jobGrade);
        this.jobAgency = new SimpleStringProperty(jobAgency);
        this.jobLocation = new SimpleStringProperty(jobLocation);
        this.applyButton = new Button("Apply");
    }

    public Job(int jobId){
        this.jobId = jobId;
        this.jobTitle = new SimpleStringProperty("");
        this.jobGrade = new SimpleStringProperty("");
        this.jobAgency = new SimpleStringProperty("");
        this.jobLocation = new SimpleStringProperty("");
        this.applyButton = new Button("Apply");

    }

    public String getJobTitle() {
        return jobTitle.get();
    }
    public StringProperty titleProperty() {
        return jobTitle;
    }

    public int getJobId() {
        return jobId;
    }

    public String getJobGrade() {
        return jobGrade.get();
    }
    public StringProperty JobGradeProperty() {
        return jobGrade;
    }
    public String getJobAgency() {
        return jobAgency.get();
    }
    public StringProperty JobAgencyProperty() {
        return jobAgency;
    }

    public StringProperty JobLocationProperty() {
        return jobLocation;
    }
    public Button ApplyButtonProperty() {
        return applyButton;
    }

    public String getJobLocation(){
        return jobLocation.get();
    }

}
