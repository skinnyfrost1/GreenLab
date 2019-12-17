package com.greenlab.greenlab.dto;

import com.greenlab.greenlab.model.User;

import java.util.List;

public class DeleteStudentResponseBody {
    private List<User> students;

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }
}
