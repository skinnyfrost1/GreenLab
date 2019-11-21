package com.greenlab.greenlab.model;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class StuCourse {
    @Id
    private String _id; // course object id + email

    private String courseObjectId; //5deeksdjhvkdhvks
    private String studentEmail;
    private String courseId; // CHE 133

    public StuCourse(String courseObjectId, String studentEmail, String courseId) {
        this.courseObjectId = courseObjectId;
        this.studentEmail = studentEmail;
        this.courseId = courseId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCourseObjectId() {
        return courseObjectId;
    }

    public void setCourseObjectId(String courseObjectId) {
        this.courseObjectId = courseObjectId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudent() {
        return studentEmail;
    }

    public void setStudent(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "StuCourse{" +
                "courseObjectId='" + courseObjectId + '\'' +
                ", _id='" + _id + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }
}
