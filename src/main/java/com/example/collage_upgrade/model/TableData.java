package com.example.collage_upgrade.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableData {
    private final StringProperty teacher;
    private final StringProperty subject;
    private final StringProperty semester1;
    private final StringProperty hoursPerWeek1;
    private final StringProperty hoursByPlan1;
    private final StringProperty hoursByPractice1; // Новое поле для часов по практике в 1 семестре
    private final StringProperty semester2;
    private final StringProperty hoursPerWeek2;
    private final StringProperty hoursByPlan2;
    private final StringProperty hoursByPractice2; // Новое поле для часов по практике во 2 семестре
    private final StringProperty total;

    public TableData(String teacher, String subject, String semester1, String hoursPerWeek1, String hoursByPlan1, String hoursByPractice1,
                     String semester2, String hoursPerWeek2, String hoursByPlan2, String hoursByPractice2, String total) {
        this.teacher = new SimpleStringProperty(teacher);
        this.subject = new SimpleStringProperty(subject);
        this.semester1 = new SimpleStringProperty(semester1);
        this.hoursPerWeek1 = new SimpleStringProperty(hoursPerWeek1);
        this.hoursByPlan1 = new SimpleStringProperty(hoursByPlan1);
        this.hoursByPractice1 = new SimpleStringProperty(hoursByPractice1);
        this.semester2 = new SimpleStringProperty(semester2);
        this.hoursPerWeek2 = new SimpleStringProperty(hoursPerWeek2);
        this.hoursByPlan2 = new SimpleStringProperty(hoursByPlan2);
        this.hoursByPractice2 = new SimpleStringProperty(hoursByPractice2);
        this.total = new SimpleStringProperty(total);
    }

    // Геттеры для всех полей
    public String getTeacher() { return teacher.get(); }
    public String getSubject() { return subject.get(); }
    public String getSemester1() { return semester1.get(); }
    public String getHoursPerWeek1() { return hoursPerWeek1.get(); }
    public String getHoursByPlan1() { return hoursByPlan1.get(); }
    public String getHoursByPractice1() { return hoursByPractice1.get(); }
    public String getSemester2() { return semester2.get(); }
    public String getHoursPerWeek2() { return hoursPerWeek2.get(); }
    public String getHoursByPlan2() { return hoursByPlan2.get(); }
    public String getHoursByPractice2() { return hoursByPractice2.get(); }
    public String getTotal() { return total.get(); }

    // Геттеры свойств для привязки к таблице
    public StringProperty teacherProperty() { return teacher; }
    public StringProperty subjectProperty() { return subject; }
    public StringProperty semester1Property() { return semester1; }
    public StringProperty hoursPerWeek1Property() { return hoursPerWeek1; }
    public StringProperty hoursByPlan1Property() { return hoursByPlan1; }
    public StringProperty hoursByPractice1Property() { return hoursByPractice1; }
    public StringProperty semester2Property() { return semester2; }
    public StringProperty hoursPerWeek2Property() { return hoursPerWeek2; }
    public StringProperty hoursByPlan2Property() { return hoursByPlan2; }
    public StringProperty hoursByPractice2Property() { return hoursByPractice2; }
    public StringProperty totalProperty() { return total; }

    // Сеттеры для всех полей
    public void setTeacher(String teacher) { this.teacher.set(teacher); }
    public void setSubject(String subject) { this.subject.set(subject); }
    public void setSemester1(String semester1) { this.semester1.set(semester1); }
    public void setHoursPerWeek1(String hoursPerWeek1) { this.hoursPerWeek1.set(hoursPerWeek1); }
    public void setHoursByPlan1(String hoursByPlan1) { this.hoursByPlan1.set(hoursByPlan1); }
    public void setHoursByPractice1(String hoursByPractice1) { this.hoursByPractice1.set(hoursByPractice1); }
    public void setSemester2(String semester2) { this.semester2.set(semester2); }
    public void setHoursPerWeek2(String hoursPerWeek2) { this.hoursPerWeek2.set(hoursPerWeek2); }
    public void setHoursByPlan2(String hoursByPlan2) { this.hoursByPlan2.set(hoursByPlan2); }
    public void setHoursByPractice2(String hoursByPractice2) { this.hoursByPractice2.set(hoursByPractice2); }
    public void setTotal(String total) { this.total.set(total); }
}
