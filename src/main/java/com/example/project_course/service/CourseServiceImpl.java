package com.example.project_course.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.project_course.entity.Course;
import com.example.project_course.entity.Student;
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
			if (!StringUtils.hasText(course.getCourseCode())) {
				return new CourseResponse("課程代碼不得為空");
			}
//			!StringUtils.hasText(course.getCourseName())
			if (!StringUtils.hasText(course.getCourseName())) {
				return new CourseResponse("課程名稱不得為空");
			}
			if (course.getWeek() == null) {
//				可以防止空格(因空格也是null)
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
			if (course.getCredit() <= 0 || course.getCredit() > 3) {
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
			if (!studentCourseDao.findByCourseCode(courseCode).isEmpty()) {
				return new CourseResponse("該課程還有學生修習");
			}
			if (!courseDao.existsByCourseCode(courseCode)) {
				return new CourseResponse("課程代碼錯誤");
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
		List<StudentCourse> studentAndCourseList = studentCourseDao
				.findByNumber(req.getNumber());
//		List<Course> courseList = courseDao.xxxxxxx;
		// 讓for不要一直進去資料庫
		if (!checkNumber(req.getNumber())) {
			return new SearchCourseResponse("學號錯誤");
		}
		Student student = studentDao.findByNumber(req.getNumber());
		List<Course> courseList = new ArrayList<Course>();
		for (StudentCourse course : studentAndCourseList) {
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

	//
	// 修改課程
	public CourseResponse updateCourse(CourseRequest req) {
		List<Course> list = new ArrayList<Course>();
		for (Course course : req.getCourseList()) {
			if (!StringUtils.hasText(course.getCourseCode())) {
				return new CourseResponse("課程代碼不得為空");
			}
			if (!courseDao.existsByCourseCode(course.getCourseCode())) {
				return new CourseResponse("課程代碼錯誤");
			}
			if (!StringUtils.hasText(course.getCourseName())) {
				return new CourseResponse("課程名稱不得為空");
			}
			if (course.getWeek() == null) {
//				可以防止空格(因空格也是null)
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
			if (course.getCredit() <= 0 || course.getCredit() > 3) {
				return new CourseResponse("學分數錯誤，請輸入1~3");
			}
			list.add(course);
		}
		courseDao.saveAll(list);
		return new CourseResponse("課程修改完成");

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
		int chooseCredit = studentDao.findByNumber(req.getNumber()).getCredit();
//		有點冗長(已修正)
		List<StudentCourse> student = new ArrayList<StudentCourse>();
		List<StudentCourse> sameCourse = new ArrayList<StudentCourse>();
		List<StudentCourse> studentCourseList = studentCourseDao
				.findByNumber(req.getNumber());
		for (String courseCode : req.getCourseCodeList()) {
//			建議命名courseCode(已修正)
			if (!courseDao.existsByCourseCode(courseCode)) {
				return new CourseResponse("課程代碼錯誤");
			}
			if (studentCourseDao.findByCourseCode(courseCode).size() == 3) {
				return new CourseResponse("修課人數已滿", courseCode);
				// 一堂課只能三人修
//				有問題待修正  (已修正)
			}
			Course chooseCourse = courseDao.findByCourseCode(courseCode);
//			欲選的課
			chooseCredit += chooseCourse.getCredit();
			if (chooseCredit > 10) {
				return new CourseResponse("選修的學分已超過10");
			}
			for (StudentCourse studentCourse : studentCourseList) {
				Course alreadyChooseCourse = courseDao
						.findByCourseCode(studentCourse.getCourseCode());
				// 確認選的課是不是選過
				if (alreadyChooseCourse.equals(chooseCourse)) {
					sameCourse.add(studentCourse);
					continue;
//					如果選過的話放入相同選課清單，進下一次遍歷
				}
				if (student.size() > 0) {
					if (alreadyChooseCourse.getWeek() == chooseCourse.getWeek()) {
						if (conflict(chooseCourse, alreadyChooseCourse)) {
							return new CourseResponse("有衝堂!但有選到課", student);
						}
					}
				} else if (alreadyChooseCourse.getWeek() == chooseCourse.getWeek()) {
					if (conflict(chooseCourse, alreadyChooseCourse)) {
						return new CourseResponse("有衝堂!但有選到課", student);
					}
				}
			}
			StudentCourse studentCourse = new StudentCourse(chooseCourse.getCourseCode(),
					req.getNumber(), chooseCourse.getCourseName(),
					chooseCourse.getCredit());
			student.add(studentCourse);
			studentCourseDao.save(studentCourse);
		}
		studentDao.setStudentCredit(req.getNumber(), chooseCredit);
		return new CourseResponse("選課成功", student);

	}

	// 判斷衝堂
	public boolean conflict(Course chooseCourse, Course alreadyChooseCourse) {
		return (chooseCourse.getStartTime() >= alreadyChooseCourse.getStartTime()
				&& chooseCourse.getStartTime() <= alreadyChooseCourse.getEndTime())
				|| (chooseCourse.getEndTime() >= alreadyChooseCourse.getStartTime()
						&& chooseCourse.getEndTime() <= alreadyChooseCourse.getEndTime());
	}

	// 退選課程
	public CourseResponse dropCourse(CourseRequest req) {
		int credit = studentDao.findByNumber(req.getNumber()).getCredit();
		int deleteCredit = 0;
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
			deleteCredit += lesson.getCredit();
			dropCourseList.add(studentCourse);

		}
		credit -= deleteCredit;
		studentCourseDao.deleteAll(dropCourseList);
		studentDao.setStudentCredit(req.getNumber(), credit);
		return new CourseResponse("已成功退選", dropCourseList);
	}
}
