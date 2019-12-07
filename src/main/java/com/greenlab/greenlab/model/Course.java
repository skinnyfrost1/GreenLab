package com.greenlab.greenlab.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Document
public class Course {

    @Id
    private String _id;
    private String courseId;
    private String courseName;
    private String semester;
    private String courseDescription;
    private String creator;
    private String createDate;
    @DBRef
    private List<User> students;
    @DBRef
    private List<Lab> labs;

    public Course() {

    }

    public Course(String courseId, String courseName, String semester, String courseDescription, String createDate, String creator,
            List<User> students, List<Lab> labs) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.semester=semester;
        this.courseDescription = courseDescription;
        this.creator = creator;
        this.createDate = createDate;
        this.students = students;
        this.labs = labs;
    }

    public Course(String courseId, String courseName, String semester, String courseDescription, String createDate, String creator,
            List<User> students) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.semester=semester;
        this.courseDescription = courseDescription;
        this.creator = creator;
        this.createDate = createDate;
        this.students = students;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public String toString(){
        String str = "courseID="+getCourseId()+
                    "\ncourseName="+getCourseName()+
                    "\nsemester="+getSemester()+
                    "\ncourseDescription="+getCourseDescription()+
                    "\ncreateDate="+getCreateDate();

        return str;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<Lab> getLabs() {
        return labs;
    }

    public void setLabs(List<Lab> labs) {
        this.labs = labs;
    }
}