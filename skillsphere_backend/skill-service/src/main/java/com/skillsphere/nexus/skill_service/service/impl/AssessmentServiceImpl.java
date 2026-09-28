package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.AssessmentQuestionRequest;
import com.skillsphere.nexus.skill_service.dto.request.AssessmentRequest;
import com.skillsphere.nexus.skill_service.dto.request.AssessmentSubmissionRequest;
import com.skillsphere.nexus.skill_service.dto.request.QuestionAnswerRequest;
import com.skillsphere.nexus.skill_service.dto.response.AssessmentQuestionResponse;
import com.skillsphere.nexus.skill_service.dto.response.AssessmentResponse;
import com.skillsphere.nexus.skill_service.model.Assessment;
import com.skillsphere.nexus.skill_service.model.AssessmentQuestion;
import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.model.Skill;
import com.skillsphere.nexus.skill_service.repository.AssessmentRepository;
import com.skillsphere.nexus.skill_service.repository.EmployeeRepository;
import com.skillsphere.nexus.skill_service.repository.SkillRepository;
import com.skillsphere.nexus.skill_service.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;


    @Override
    public AssessmentResponse addAssessment(AssessmentRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() ->
                        new RuntimeException("Skill not found"));

        Assessment assessment = Assessment.builder()
                .assessmentName(request.getAssessmentName())
                .assessmentDate(request.getAssessmentDate())
                .score(null)
                .passed(null)
                .result(null)
                .verified(false)
                .employee(employee)
                .skill(skill)
                .build();

        if (request.getQuestions() != null) {

            for (AssessmentQuestionRequest questionRequest
                    : request.getQuestions()) {

                AssessmentQuestion question =
                        AssessmentQuestion.builder()
                                .assessment(assessment)
                                .questionText(
                                        questionRequest.getQuestionText())
                                .optionA(
                                        questionRequest.getOptionA())
                                .optionB(
                                        questionRequest.getOptionB())
                                .optionC(
                                        questionRequest.getOptionC())
                                .optionD(
                                        questionRequest.getOptionD())
                                .correctAnswer(
                                        questionRequest.getCorrectAnswer())
                                .marks(
                                        questionRequest.getMarks())
                                .build();

                assessment.getQuestions().add(question);
            }
        }

        return mapToResponse(
                assessmentRepository.save(assessment));
    }


    @Override
    public List<AssessmentResponse> getMyAssessments() {

        return assessmentRepository
                .findByEmployeeKeycloakUserId(
                        getCurrentKeycloakUserId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AssessmentQuestionResponse> getAssessmentQuestions(
            UUID assessmentId) {

        Assessment assessment =
                assessmentRepository.findById(assessmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Assessment not found"));

        String keycloakUserId =
                getCurrentKeycloakUserId();

        if (!assessment.getEmployee()
                .getKeycloakUserId()
                .equals(keycloakUserId)) {

            throw new RuntimeException(
                    "You are not assigned to this assessment");
        }

        return assessment.getQuestions()
                .stream()
                .map(question ->
                        AssessmentQuestionResponse.builder()
                                .questionId(
                                        question.getQuestionId())
                                .questionText(
                                        question.getQuestionText())
                                .optionA(
                                        question.getOptionA())
                                .optionB(
                                        question.getOptionB())
                                .optionC(
                                        question.getOptionC())
                                .optionD(
                                        question.getOptionD())
                                .build())
                .toList();
    }

    @Override
    public AssessmentResponse submitAssessment(
            UUID assessmentId,
            AssessmentSubmissionRequest request) {

        Assessment assessment =
                assessmentRepository.findById(assessmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Assessment not found"));

        String keycloakUserId =
                getCurrentKeycloakUserId();

        if (!assessment.getEmployee()
                .getKeycloakUserId()
                .equals(keycloakUserId)) {

            throw new RuntimeException(
                    "You are not assigned to this assessment");
        }

        if (assessment.getScore() != null) {
            throw new RuntimeException(
                    "Assessment has already been submitted");
        }

        long distinctAnsweredQuestions =
                request.getAnswers()
                        .stream()
                        .map(QuestionAnswerRequest::getQuestionId)
                        .distinct()
                        .count();

        if (distinctAnsweredQuestions
                != assessment.getQuestions().size()) {

            throw new RuntimeException(
                    "Please answer all questions");
        }

        int totalMarks = 0;
        int obtainedMarks = 0;

        for (AssessmentQuestion question
                : assessment.getQuestions()) {

            totalMarks += question.getMarks();

            QuestionAnswerRequest submittedAnswer =
                    request.getAnswers()
                            .stream()
                            .filter(answer ->
                                    answer.getQuestionId()
                                            .equals(
                                                    question.getQuestionId()))
                            .findFirst()
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Answer missing for question"));

            if (question.getCorrectAnswer()
                    .equalsIgnoreCase(
                            submittedAnswer.getAnswer())) {

                obtainedMarks += question.getMarks();
            }
        }

        double score =
                totalMarks == 0
                        ? 0.0
                        : ((double) obtainedMarks
                        / totalMarks) * 100.0;

        boolean passed = score >= 70.0;

        assessment.setScore(score);
        assessment.setPassed(passed);
        assessment.setResult(
                passed ? "PASS" : "FAIL");

        assessment.setVerified(false);

        return mapToResponse(
                assessmentRepository.save(assessment));
    }

    @Override
    public AssessmentResponse updateAssessment(
            UUID assessmentId,
            AssessmentRequest request) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() ->
                        new RuntimeException("Assessment not found"));

        Employee employee = employeeRepository.findById(
                request.getEmployeeId()
        ).orElseThrow(() ->
                new RuntimeException("Employee not found"));

        Skill skill = skillRepository.findById(
                request.getSkillId()
        ).orElseThrow(() ->
                new RuntimeException("Skill not found"));

        /*
         * Update basic assessment information
         */
        assessment.setAssessmentName(
                request.getAssessmentName());

        assessment.setAssessmentDate(
                request.getAssessmentDate());

        assessment.setEmployee(employee);

        assessment.setSkill(skill);

        /*
         * Replace existing questions
         *
         * Because Assessment has:
         *
         * cascade = CascadeType.ALL
         * orphanRemoval = true
         *
         * clearing the collection removes the old
         * questions from assessment_questions.
         */
        assessment.getQuestions().clear();

        if (request.getQuestions() != null) {

            for (AssessmentQuestionRequest questionRequest
                    : request.getQuestions()) {

                AssessmentQuestion question =
                        AssessmentQuestion.builder()
                                .assessment(assessment)
                                .questionText(
                                        questionRequest.getQuestionText())
                                .optionA(
                                        questionRequest.getOptionA())
                                .optionB(
                                        questionRequest.getOptionB())
                                .optionC(
                                        questionRequest.getOptionC())
                                .optionD(
                                        questionRequest.getOptionD())
                                .correctAnswer(
                                        questionRequest.getCorrectAnswer())
                                .marks(
                                        questionRequest.getMarks())
                                .build();

                assessment.getQuestions().add(question);
            }
        }

        /*
         * Updating an assessment invalidates any previous
         * employee result because the questions may have changed.
         *
         * The employee must take the updated assessment again.
         */
        assessment.setScore(null);
        assessment.setPassed(null);
        assessment.setResult(null);
        assessment.setVerified(false);

        Assessment updatedAssessment =
                assessmentRepository.save(assessment);

        return mapToResponse(updatedAssessment);
    }

    @Override
    public AssessmentResponse getAssessmentById(UUID assessmentId) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() ->
                        new RuntimeException("Assessment not found"));

        return mapToResponse(assessment);
    }

    @Override
    public List<AssessmentResponse> getAllAssessments() {

        return assessmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }



    @Override
    public void deleteAssessment(UUID assessmentId) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() ->
                        new RuntimeException("Assessment not found"));

        assessmentRepository.delete(assessment);
    }

    @Override
    public List<AssessmentResponse> getAssessmentsByEmployee(UUID employeeId) {

        return assessmentRepository.findByEmployeeEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AssessmentResponse> getAssessmentsBySkill(UUID skillId) {

        return assessmentRepository.findBySkillSkillId(skillId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Double calculateAverageScore(UUID employeeId) {

        List<Assessment> assessments =
                assessmentRepository
                        .findByEmployeeEmployeeId(employeeId);

        return assessments.stream()
                .map(Assessment::getScore)
                .filter(score -> score != null)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<AssessmentResponse> getPassedAssessments() {

        return assessmentRepository.findByResultIgnoreCase("PASS")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AssessmentResponse> getFailedAssessments() {

        return assessmentRepository.findByResultIgnoreCase("FAIL")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AssessmentResponse verifyAssessment(UUID assessmentId) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        assessment.setVerified(true);
        assessmentRepository.save(assessment);

        return mapToResponse(assessment);

    }


    private AssessmentResponse mapToResponse(
            Assessment assessment) {

        return AssessmentResponse.builder()
                .assessmentId(
                        assessment.getAssessmentId())

                .assessmentName(
                        assessment.getAssessmentName())

                .assessmentDate(
                        assessment.getAssessmentDate())

                .employeeId(
                        assessment.getEmployee()
                                .getEmployeeId())

                .skillId(
                        assessment.getSkill()
                                .getSkillId())

                .score(
                        assessment.getScore())

                .passed(
                        assessment.getPassed())

                .result(
                        assessment.getResult())

                .verified(
                        assessment.getVerified())

                .totalQuestions(
                        assessment.getQuestions() == null
                                ? 0
                                : assessment.getQuestions().size())

                .build();
    }

    private String getCurrentKeycloakUserId() {

        Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                authentication.getName() == null) {

            throw new RuntimeException(
                    "Authenticated employee not found");
        }

        return authentication.getName();
    }
}