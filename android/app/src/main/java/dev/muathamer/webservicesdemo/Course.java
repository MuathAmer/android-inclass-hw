package dev.muathamer.webservicesdemo;

class Course {

    private int id;
    private String name;
    private String teacher;
    private int studentsNo;

    public Course(int id, String name, String teacher, int studentsNo) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.studentsNo = studentsNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getStudentsNo() {
        return studentsNo;
    }

    public void setStudentsNo(int studentsNo) {
        this.studentsNo = studentsNo;
    }

    @Override
    public String toString() {
        return String.format("%d) \"%s\" given by teacher \'%s\', \n\t\t\t\tfor %d students.", id, name, teacher, studentsNo);
    }
}
