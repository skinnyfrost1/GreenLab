package com.greenlab.greenlab.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document

public class Course{
    private String courseId;
    private String courseName;
    private String courseDescription;
    private List<User> students;
    // private List<Lab> labs;

    public Course(){

    }

    public Course(String courseId, String courseName, String courseDescription, List<User> students){
        this.courseId=courseId;
        this.courseName=courseName;
        this.courseDescription=courseDescription;
        this.students = students;
    }


    /**
     * @param courseDescription the courseDescription to set
     */
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
    /**
     * @param courseId the courseId to set
     */
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    /**
     * @param courseName the courseName to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    /**
     * @param students the students to set
     */
    public void setStudents(List<User> students) {
        this.students = students;
    }
    /**
     * @return the courseDescription
     */
    public String getCourseDescription() {
        return courseDescription;
    }
    /**
     * @return the courseId
     */
    public String getCourseId() {
        return courseId;
    }
    /**
     * @return the courseName
     */
    public String getCourseName() {
        return courseName;
    }
    /**
     * @return the students
     */
    public List<User> getStudents() {
        return students;
    }
}