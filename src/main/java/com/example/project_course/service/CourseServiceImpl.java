package com.example.project_course.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project_course.entity.Course;
import com.example.project_course.entity.StudentCourse;
import com.example.project_course.repository.CourseDao;
import com.example.project_course.repository.StudentCourseDao;
import com.example.project_course.repository.StudentDao;
import com.example.project_course.vo.CourseRequest;
import com.example.project_course.vo.CourseResponse;
import com.example.project_course.vo.SearchCourseRequest;
import com.example.project_course.vo.SearchCourseResponse;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	StudentDao studentDao;
	@Autowired
	CourseDao courseDao;
	@Autowired
	StudentCourseDao studentCourseDao;

	// 選課功能
	@Override
	public CourseResponse chooseCourse(CourseRequest req) {
		return pickCourse(req);
	}

	// 加退選功能
	@Override
	public CourseResponse pickAndDropCourse(CourseRequest req) {
		if (req.getAddOrDrop().equals("加選")) {
			return pickCourse(req);
		}
		if (req.getAddOrDrop().equals("退選")) {
			return dropCourse(req);
		}
		return new CourseResponse("請正確輸入加選或退選");
	}

	// 新增課程
	@Override
	public CourseResponse addNewCourse(CourseRequest req) {
		List<Course> list = new ArrayList<Course>();
		for (Course course : req.getCourseList()) {
			if (course.getCourseCode().equals("")) {
				return new CourseResponse("課程代碼不得為空");
			}
			if (course.getCourseName().equals("")) {
				return new CourseResponse("課程名稱不得為空");
			}
			if (course.getWeek() == null) {
				return new CourseResponse("星期不得為空");
			}
			if (course.getStartTime() == null) {
				return new CourseResponse("上課時間不得為空");
			}
			if (course.getEndTime() == null) {
				return new CourseResponse("下課時間不得為空");
			}
			if (course.getCredit() == null) {
				return new CourseResponse("學分不得為空");
			}
			if (course.getWeek() < 1 || course.getWeek() > 7) {
				return new CourseResponse("星期錯誤，請正確輸入1~7");
			}
			if (course.getStartTime() > course.getEndTime()) {
				return new CourseResponse("下課時間不得早於上課時間");
			}
			if (courseDao.findByCourseCode(course.getCourseCode()) != (null)) {
				return new CourseResponse("課程代碼已重複");
			}
			if (course.getCredit() < 0 || course.getCredit() > 3) {
				return new CourseResponse("學分數錯誤，請輸入1~3");
			}
			list.add(course);
		}
		courseDao.saveAll(list);
		return new CourseResponse("課程新增完成");
	}

	// 刪除課程
	@Override
	public CourseResponse deleteCourse(CourseRequest req) {
		List<Course> removeCoureseList = new ArrayList<Course>();
		for (String courseCode : req.getCourseCodeList()) {
			if (!studentCourseDao.findByCourseName(courseCode).isEmpty()) {
				return new CourseResponse("該課程還有學生修習");
			}
			removeCoureseList.add(courseDao.findByCourseCode(courseCode));
		}
		courseDao.deleteAll(removeCoureseList);
		return new CourseResponse("移除課程成功");
	}

	@Override
	// 新增學生
	public CourseResponse addNewStudent(CourseRequest req) {
		var student = req.getStudent();
		if (studentDao.existsById(student.getNumber())) {
			return new CourseResponse("該學號已存在");
		}
		if (student.getNumber() == 0) {
			return new CourseResponse("學號不得為0");
		}
		if (student.getName().equals("")) {
			return new CourseResponse("學生姓名不得為空");
		}
		if (student.getCredit() < 0 || student.getCredit() > 10) {
			return new CourseResponse("學分數需介於0~10");
		}
		studentDao.save(student);
		return new CourseResponse("學生資料建立完成");
	}

	@Override
	// 刪除學生
	public CourseResponse deleteStudent(CourseRequest req) {

		if (!checkNumber(req.getNumber())) {
			return new CourseResponse("學號錯誤");
		}
		if (!studentCourseDao.findByNumber(req.getNumber()).isEmpty()) {
			return new CourseResponse("此學生仍有修習課程，無法刪除");
		}
		studentDao.deleteById(req.getNumber());
		return new CourseResponse("已刪除該學生");
	}

	// 查詢學生選到的所有課程
	@Override
	public SearchCourseResponse searchStudentCourse(SearchCourseRequest req) {
		if (!checkNumber(req.getNumber())) {
			return new SearchCourseResponse("學號錯誤");
		}
		var student = studentDao.findByNumber(req.getNumber());
		List<Course> courseList = new ArrayList<Course>();
		for (StudentCourse course : studentCourseDao.findByNumber(req.getNumber())) {
			courseList.add(courseDao.findByCourseCode(course.getCourseCode()));
		}
		return new SearchCourseResponse("查詢成功!", student, courseList);
	}

	// 用課程代碼找課程
	@Override
	public SearchCourseResponse searchCourseByCode(SearchCourseRequest req) {
		if (courseDao.findByCourseCode(req.getCourseCode()) == null) {
			return new SearchCourseResponse("查無此代碼!");
		}
		return new SearchCourseResponse("查詢成功!",
				courseDao.findByCourseCode(req.getCourseCode()));
	}

	// 用課程名稱找課程
	@Override
	public SearchCourseResponse searchCourseByName(SearchCourseRequest req) {
		if (courseDao.findByCourseName(req.getCourseName()).isEmpty()) {
			return new SearchCourseResponse("查無此課程名稱!");
		}
		return new SearchCourseResponse("查詢成功!",
				courseDao.findByCourseName(req.getCourseName()));
	}

	// 一些簡化程式用的method

	// 確認學號正確
	public boolean checkNumber(int number) {
		return studentDao.existsByNumber(number);
	}

	// 選課
	public CourseResponse pickCourse(CourseRequest req) {
		if (!checkNumber(req.getNumber())) {
			return new CourseResponse("學號錯誤");
		}
		int studentCredit = studentDao.findByNumber(req.getNumber()).getCredit();
		int chooseCredit = studentCredit;
		List<StudentCourse> student = new ArrayList<StudentCourse>();
		List<StudentCourse> sameCourse = new ArrayList<StudentCourse>();
		List<StudentCourse> studentCourseList = studentCourseDao
				.findByNumber(req.getNumber());
		for (String course : req.getCourseCodeList()) {
			if (!courseDao.existsByCourseCode(course)) {
				return new CourseResponse("課程代碼錯誤");
			}
			if (studentCourseDao.findByCourseName(course).size() == 3) {
				return new CourseResponse("修課人數已滿", course);
				// 一堂課只能三人修
			}
			Course lesson = courseDao.findByCourseCode(course);
			chooseCredit += lesson.getCredit();
			if (chooseCredit > 10) {
				return new CourseResponse("選修的學分已超過10");
			}
			for (StudentCourse studentCourse : studentCourseList) {
				var lessonWeek = courseDao
						.findByCourseCode(studentCourse.getCourseCode());
				if (lessonWeek.getWeek() == lesson.getWeek()) {
					if (lesson.getStartTime() >= lessonWeek.getStartTime()
							|| lesson.getStartTime() <= lessonWeek.getEndTime()) {
						return new CourseResponse("衝堂");
					}
					if (lesson.getEndTime() >= lessonWeek.getStartTime()
							|| lesson.getEndTime() <= lessonWeek.getEndTime()) {
						return new CourseResponse("衝堂");
					}
				}
			}
			StudentCourse studentCourse = new StudentCourse(lesson.getCourseCode(),
					req.getNumber(), lesson.getCourseName(), lesson.getCredit());
			if (studentCourseList.contains(studentCourse)
					|| (!studentCourseDao.findByNumberIsAndCourseNameIs(req.getNumber(),
							lesson.getCourseName()).isEmpty())) {
				sameCourse.add(studentCourse);
				continue;
			}
			student.add(studentCourse);
		}
		studentCourseDao.saveAll(student);
		studentDao.setStudentCredit(req.getNumber(), chooseCredit);
		if (!sameCourse.isEmpty()) {
			return new CourseResponse("選課成功但有重複選課", student);
		}
		return new CourseResponse("選課成功", student);
	}

	// 退選課程
	public CourseResponse dropCourse(CourseRequest req) {
		if (!checkNumber(req.getNumber())) {
			return new CourseResponse("學號錯誤!");
		}
		var dropCourseList = new ArrayList<StudentCourse>();
		for (String course : req.getCourseCodeList()) {
			if (!courseDao.existsByCourseCode(course)) {
				return new CourseResponse("課程代碼錯誤!");
			}
			var lesson = courseDao.findByCourseCode(course);
			var studentCourse = studentCourseDao.findByNumberIsAndCourseCodeIs(
					req.getNumber(), lesson.getCourseCode());
			if (studentCourse == null) {
				return new CourseResponse("你沒有修這堂課!");
			}
			dropCourseList.add(studentCourse);
		}
		studentCourseDao.deleteAll(dropCourseList);
		return new CourseResponse("已成功退選", dropCourseList);
	}
}
