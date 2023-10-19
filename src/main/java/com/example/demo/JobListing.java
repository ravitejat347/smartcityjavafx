package com.example.demo;
/**
 * Author: Maaz
 */

import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class for managing job listings and applications.
 */
public class JobListing {

    static List<Job> jobs = new ArrayList<>();
    static Connection connection=DBConn.connectDB();

    /**
     * Retrieves a list of all available jobs from the database.
     *
     * @return A list of Job objects representing available jobs.
     * @throws RuntimeException If there is an SQL exception.
     */
    public static List<Job> getAllJobs(){
        String sql = "SELECT * FROM jobs";
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                jobs.add(new Job(resultSet.getString(2), resultSet.getInt(1),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5)));
            }
        return jobs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Allows a user to apply for a job.
     *
     * @param jobID The ID of the job to apply for.
     */
    public static void applyJob(int jobID) {
        // Get the user ID from User.getInstance().getUserID()
        int userID = User.getInstance().getUserID();

        // Check if the entry already exists
        String selectSql = "SELECT * FROM jobapplication WHERE job_id = ? AND user_id = ?";
        boolean alreadyExists = false;

        try (
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            selectStatement.setInt(1, jobID);
            selectStatement.setInt(2, userID);

            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                alreadyExists = true; // Entry already exists
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (alreadyExists) {
            // Display an alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Application Status");
            alert.setHeaderText(null);
            alert.setContentText("You have already applied for this job.");

            alert.showAndWait();
        } else {
            // Insert the new application
            String insertSql = "INSERT INTO jobapplication (job_id, user_id) VALUES (?, ?)";

            try (
                 PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

                insertStatement.setInt(1, jobID);
                insertStatement.setInt(2, userID);

                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Application Status");
                    alert.setHeaderText(null);
                    alert.setContentText("Congratulations! You have applied for the job");
                    alert.showAndWait();
                } else {
                    System.out.println("Failed to submit application.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds a new job listing to the database.
     *
     * @param title  The title of the job.
     * @param grade  The job grade.
     * @param agency The agency offering the job.
     * @param city   The city where the job is located.
     * @return The newly created Job object.
     * @throws SQLException If there is an SQL exception.
     */
    public static Job addJob(String title, String grade, String agency, String city ) throws SQLException {
        int jobID = 0;
        jobID = (int) (Math.random()*10000);

        //get a random jobID
        for(int i =0;i<jobs.size();i++){
            if(jobID == jobs.get(i).getJobId()){
                jobID = (int) (Math.random()*10000);
                i =0;
            }
        }

         //   public Job(String jobTitle, int jobId, String jobGrade, String jobAgency, String jobLocation){
        Job job = new Job(title,jobID,grade,agency,city);
        postJob(job);
        return job;
    }

    /**
     * Retrieves a list of all available jobs from the database.
     *
     * @return An ArrayList of Job objects representing available jobs.
     * @throws SQLException If there is an SQL exception.
     */
    public static ArrayList<Job> getJobs() throws SQLException {
        String sql = "SELECT * FROM jobs";
        ArrayList<Job> jobs = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            jobs.add(new Job(resultSet.getString(2), resultSet.getInt(1),
                    resultSet.getString(3), resultSet.getString(4),
                    resultSet.getString(5)));
        }
        return jobs;

    }

    /**
     * Removes a job listing from the database.
     *
     * @param job The Job object to be removed.
     * @throws SQLException If there is an SQL exception.
     */
    public static void removeJob(Job job) throws SQLException {
        int jobID = job.getJobId();
        String sql = "DELETE FROM jobs WHERE job_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, jobID);
        preparedStatement.executeUpdate();
    }

    /**
     * Retrieves a specific job listing from the database.
     *
     * @param job The Job object to retrieve.
     * @throws SQLException If there is an SQL exception.
     */
    public static void getJob(Job job) throws SQLException{
        int jobID = job.getJobId();
        String sql = String.format("SELECT * FROM jobs WHERE job_id = %s", jobID);
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
    }

    /**
     * Retrieves and displays all available jobs from the database.
     *
     * @param k The parameter for some unspecified purpose.
     * @throws SQLException If there is an SQL exception.
     */
    public static void getAllJobs(int k) throws SQLException {
        String sql = "SELECT * FROM jobs";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            for(int i =1;i<=5;i++){
                System.out.print (" " + rs.getString(i));
            }
            System.out.println();

        }
    }


    /**
     * Inserts a new job listing into the database.
     *
     * @param job The Job object to be inserted.
     * @throws SQLException If there is an SQL exception.
     */
    public static void postJob(Job job) throws SQLException {

        String sql = String.format("INSERT INTO jobs VALUES ('%s','%s', '%s', '%s', '%s')",
                job.getJobId(),job.getJobTitle(),job.getJobGrade(),job.getJobAgency(),job.getJobLocation());
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeUpdate();
        getJob(job);
    }

    /**
     * Replaces an old job listing with a new one in the database.
     *
     * @param oldJob The old Job object to be replaced.
     * @param newJob The new Job object to replace the old one.
     * @throws SQLException If there is an SQL exception.
     */
    public static void editJob(Job oldJob, Job newJob) throws SQLException {
        removeJob(oldJob);
        postJob(newJob);
    }
}
