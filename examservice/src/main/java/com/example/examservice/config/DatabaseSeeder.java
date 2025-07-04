package com.example.examservice.config;

import com.example.examservice.model.*;
import com.example.examservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final ExamRepository examRepo;
    private final QuestionRepository questionRepo;
    private final AnswerRepository answerRepo;
    private final StudentExamRepository studentExamRepo;
    private final StudentAnswerRepository studentAnswerRepo;

    public DatabaseSeeder(
        ExamRepository examRepo,
        QuestionRepository questionRepo,
        AnswerRepository answerRepo,
        StudentExamRepository studentExamRepo,
        StudentAnswerRepository studentAnswerRepo
    ) {
        this.examRepo = examRepo;
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.studentExamRepo = studentExamRepo;
        this.studentAnswerRepo = studentAnswerRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // لا تعيد الزرع إذا كانت البيانات موجودة أصلاً
        if (examRepo.count() > 0) {
            return;
        }

        // 1) أنشئ امتحاناً واحداً
        Exam exam = new Exam();
        exam.setCourseId(1L);
        exam.setTitle("امتحان تجريبي");
        exam.setDate(LocalDateTime.now().plusDays(1));
        exam = examRepo.save(exam);

        // 2) أنشئ بعض الأسئلة والإجابات لكل سؤال
        List<Question> questions = new ArrayList<>();
        List<Answer> allAnswers = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            Question q = new Question();
            q.setExam(exam);
            q.setText("ما هو نص السؤال رقم " + i + "?");
            q.setCorrectAnswer( (i % 4) + 1 );  // خيارات صحيحة موزّعة
            q = questionRepo.save(q);
            questions.add(q);

            // أربع إجابات افتراضية
            for (int opt = 1; opt <= 4; opt++) {
                Answer a = new Answer();
                a.setQuestion(q);
                a.setOptionIndex(opt);
                a.setBody("الإجابة " + opt + " للسؤال " + i);
                allAnswers.add(a);
            }
        }
        answerRepo.saveAll(allAnswers);

        // 3) أنشئ سجلاً للطالب (studentId = 2)
        StudentExam se = new StudentExam();
        se.setStudentId(2L);
        se.setExam(exam);
        se.setStartedAt(LocalDateTime.now());
        se.setCompletedAt(LocalDateTime.now().plusMinutes(30));
        // احسب درجة جزئية (مثلاً 2 من 3)
        se.setScore(2.0);
        se = studentExamRepo.save(se);

        // 4) أنشئ الإجابات التي أدخلها الطالب لهذا الامتحان
        List<StudentAnswer> studentAnswers = new ArrayList<>();
        for (Question q : questions) {
            StudentAnswer sa = new StudentAnswer();
            sa.setStudentExam(se);
            sa.setQuestion(q);
            // لنفترض اختار الطالب الخيار 1 لكل سؤال
            sa.setSelectedOption(1);
            sa.setCorrect(q.getCorrectAnswer() == 1);
            studentAnswers.add(sa);
        }
        studentAnswerRepo.saveAll(studentAnswers);

        System.out.println("✅ تم تهيئة قاعدة البيانات بالبيانات التجريبية.");
    }
}
