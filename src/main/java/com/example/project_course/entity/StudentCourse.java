package com.example.project_course.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.project_course.entity.StudentCourse.StudentCourseId;

@Entity
@IdClass(StudentCourseId.class)
@Table(name = "student_course")
public class StudentCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "course_code")
	private String courseCode;
	@Id
	@Column(name = "student_number")
	private Integer number;
	@Column(name = "course_name")
	private String courseName;
	@Column(name = "student_credit")
	private Integer credit;

	public StudentCourse(String courseCode, Integer number, String courseName,
			Integer credit) {
		super();
		this.courseCode = courseCode;
		this.number = number;
		this.courseName = courseName;
		this.credit = credit;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public StudentCourse() {
		super();
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public static class StudentCourseId implements Serializable {
		private static final long serialVersionUID = 1L;
		private String courseCode;
		private Integer number;

		public String getCourseCode() {
			return courseCode;
		}

		public void setCourseCode(String courseCode) {
			this.courseCode = courseCode;
		}

		public Integer getNumber() {
			return number;
		}

		public void setNumber(Integer number) {
			this.number = number;
		}

	}
}
