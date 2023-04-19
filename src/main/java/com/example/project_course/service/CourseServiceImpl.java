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

	// ��ҥ\��
	@Override
	public CourseResponse chooseCourse(CourseRequest req) {
		return pickCourse(req);
	}

	// �[�h��\��
	@Override
	public CourseResponse pickAndDropCourse(CourseRequest req) {
		if (req.getAddOrDrop().equals("�[��")) {
			return pickCourse(req);
		}
		if (req.getAddOrDrop().equals("�h��")) {
			return dropCourse(req);
		}
		return new CourseResponse("�Х��T��J�[��ΰh��");
	}

	// �s�W�ҵ{
	@Override
	public CourseResponse addNewCourse(CourseRequest req) {
		List<Course> list = new ArrayList<Course>();
		for (Course course : req.getCourseList()) {
			if (course.getCourseCode().equals("")) {
				return new CourseResponse("�ҵ{�N�X���o����");
			}
			if (course.getCourseName().equals("")) {
				return new CourseResponse("�ҵ{�W�٤��o����");
			}
			if (course.getWeek() == null) {
				return new CourseResponse("�P�����o����");
			}
			if (course.getStartTime() == null) {
				return new CourseResponse("�W�Үɶ����o����");
			}
			if (course.getEndTime() == null) {
				return new CourseResponse("�U�Үɶ����o����");
			}
			if (course.getCredit() == null) {
				return new CourseResponse("�Ǥ����o����");
			}
			if (course.getWeek() < 1 || course.getWeek() > 7) {
				return new CourseResponse("�P�����~�A�Х��T��J1~7");
			}
			if (course.getStartTime() > course.getEndTime()) {
				return new CourseResponse("�U�Үɶ����o����W�Үɶ�");
			}
			if (courseDao.findByCourseCode(course.getCourseCode()) != (null)) {
				return new CourseResponse("�ҵ{�N�X�w����");
			}
			if (course.getCredit() < 0 || course.getCredit() > 3) {
				return new CourseResponse("�Ǥ��ƿ��~�A�п�J1~3");
			}
			list.add(course);
		}
		courseDao.saveAll(list);
		return new CourseResponse("�ҵ{�s�W����");
	}

	// �R���ҵ{
	@Override
	public CourseResponse deleteCourse(CourseRequest req) {
		List<Course> removeCoureseList = new ArrayList<Course>();
		for (String courseCode : req.getCourseCodeList()) {
			if (!studentCourseDao.findByCourseName(courseCode).isEmpty()) {
				return new CourseResponse("�ӽҵ{�٦��ǥͭײ�");
			}
			removeCoureseList.add(courseDao.findByCourseCode(courseCode));
		}
		courseDao.deleteAll(removeCoureseList);
		return new CourseResponse("�����ҵ{���\");
	}

	@Override
	// �s�W�ǥ�
	public CourseResponse addNewStudent(CourseRequest req) {
		var student = req.getStudent();
		if (studentDao.existsById(student.getNumber())) {
			return new CourseResponse("�ӾǸ��w�s�b");
		}
		if (student.getNumber() == 0) {
			return new CourseResponse("�Ǹ����o��0");
		}
		if (student.getName().equals("")) {
			return new CourseResponse("�ǥͩm�W���o����");
		}
		if (student.getCredit() < 0 || student.getCredit() > 10) {
			return new CourseResponse("�Ǥ��ƻݤ���0~10");
		}
		studentDao.save(student);
		return new CourseResponse("�ǥ͸�ƫإߧ���");
	}

	@Override
	// �R���ǥ�
	public CourseResponse deleteStudent(CourseRequest req) {

		if (!checkNumber(req.getNumber())) {
			return new CourseResponse("�Ǹ����~");
		}
		if (!studentCourseDao.findByNumber(req.getNumber()).isEmpty()) {
			return new CourseResponse("���ǥͤ����ײ߽ҵ{�A�L�k�R��");
		}
		studentDao.deleteById(req.getNumber());
		return new CourseResponse("�w�R���Ӿǥ�");
	}

	// �d�߾ǥͿ�쪺�Ҧ��ҵ{
	@Override
	public SearchCourseResponse searchStudentCourse(SearchCourseRequest req) {
		if (!checkNumber(req.getNumber())) {
			return new SearchCourseResponse("�Ǹ����~");
		}
		var student = studentDao.findByNumber(req.getNumber());
		List<Course> courseList = new ArrayList<Course>();
		for (StudentCourse course : studentCourseDao.findByNumber(req.getNumber())) {
			courseList.add(courseDao.findByCourseCode(course.getCourseCode()));
		}
		return new SearchCourseResponse("�d�ߦ��\!", student, courseList);
	}

	// �νҵ{�N�X��ҵ{
	@Override
	public SearchCourseResponse searchCourseByCode(SearchCourseRequest req) {
		if (courseDao.findByCourseCode(req.getCourseCode()) == null) {
			return new SearchCourseResponse("�d�L���N�X!");
		}
		return new SearchCourseResponse("�d�ߦ��\!",
				courseDao.findByCourseCode(req.getCourseCode()));
	}

	// �νҵ{�W�٧�ҵ{
	@Override
	public SearchCourseResponse searchCourseByName(SearchCourseRequest req) {
		if (courseDao.findByCourseName(req.getCourseName()).isEmpty()) {
			return new SearchCourseResponse("�d�L���ҵ{�W��!");
		}
		return new SearchCourseResponse("�d�ߦ��\!",
				courseDao.findByCourseName(req.getCourseName()));
	}

	// �@��²�Ƶ{���Ϊ�method

	// �T�{�Ǹ����T
	public boolean checkNumber(int number) {
		return studentDao.existsByNumber(number);
	}

	// ���
	public CourseResponse pickCourse(CourseRequest req) {
		if (!checkNumber(req.getNumber())) {
			return new CourseResponse("�Ǹ����~");
		}
		int studentCredit = studentDao.findByNumber(req.getNumber()).getCredit();
		int chooseCredit = studentCredit;
		List<StudentCourse> student = new ArrayList<StudentCourse>();
		List<StudentCourse> overChoose = new ArrayList<StudentCourse>();
		List<StudentCourse> studentCourseList = studentCourseDao
				.findByNumber(req.getNumber());
		for (String course : req.getCourseCodeList()) {
			if (!courseDao.existsByCourseCode(course)) {
				return new CourseResponse("�ҵ{�N�X���~");
			}
			if (studentCourseDao.findByCourseName(course).size() == 3) {
				return new CourseResponse("�׽ҤH�Ƥw��", course);
				// �@��ҥu��T�H��
			}
			Course lesson = courseDao.findByCourseCode(course);
			chooseCredit += lesson.getCredit();
			if (chooseCredit > 10) {
				return new CourseResponse("��ת��Ǥ��w�W�L10");
			}
			for (StudentCourse studentCourse : studentCourseList) {
				var lessonWeek = courseDao
						.findByCourseCode(studentCourse.getCourseCode());
				if (lessonWeek.getWeek() == lesson.getWeek()) {
					if (lesson.getStartTime() >= lessonWeek.getStartTime()
							|| lesson.getStartTime() <= lessonWeek.getEndTime()) {
						return new CourseResponse("�İ�");
					}
					if (lesson.getEndTime() >= lessonWeek.getStartTime()
							|| lesson.getEndTime() <= lessonWeek.getEndTime()) {
						return new CourseResponse("�İ�");
					}
				}
			}
			StudentCourse studentCourse = new StudentCourse(lesson.getCourseCode(),
					req.getNumber(), lesson.getCourseName(), lesson.getCredit());
			if (studentCourseList.contains(studentCourse)
					|| (!studentCourseDao.findByNumberIsAndCourseNameIs(req.getNumber(),
							lesson.getCourseName()).isEmpty())) {
				overChoose.add(studentCourse);
				continue;
			}
			student.add(studentCourse);
		}
		studentCourseDao.saveAll(student);
		studentDao.setStudentCredit(req.getNumber(), chooseCredit);
		if (!overChoose.isEmpty()) {
			return new CourseResponse("��Ҧ��\�������ƿ��", student);
		}
		return new CourseResponse("��Ҧ��\", student);
	}

	// �h��ҵ{
	public CourseResponse dropCourse(CourseRequest req) {
		if (!checkNumber(req.getNumber())) {
			return new CourseResponse("�Ǹ����~!");
		}
		var dropCourseList = new ArrayList<StudentCourse>();
		for (String course : req.getCourseCodeList()) {
			if (!courseDao.existsByCourseCode(course)) {
				return new CourseResponse("�ҵ{�N�X���~!");
			}
			var lesson = courseDao.findByCourseCode(course);
			var studentCourse = studentCourseDao.findByNumberIsAndCourseCodeIs(
					req.getNumber(), lesson.getCourseCode());
			if (studentCourse == null) {
				return new CourseResponse("�A�S���׳o���!");
			}
			dropCourseList.add(studentCourse);
		}
		studentCourseDao.deleteAll(dropCourseList);
		return new CourseResponse("�w���\�h��", dropCourseList);
	}
}
