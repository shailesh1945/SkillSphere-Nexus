import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AssessmentService } from '../../services/assessment.service';

interface Assessment {
  assessmentId: string;
  assessmentName: string;
  assessmentDate: string | null;
  employeeId: string;
  skillId: string;
  score: number | null;
  passed: boolean | null;
  result: string | null;
  verified: boolean;
  totalQuestions: number;
}

interface AssessmentQuestion {
  questionId: string;
  questionText: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
}

interface Answer {
  questionId: string;
  answer: string;
}

@Component({
  selector: 'app-my-assessments',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './my-assessments.component.html',
  styleUrl: './my-assessments.component.scss'
})
export class MyAssessmentsComponent implements OnInit {

  private assessmentService = inject(AssessmentService);

  assessments: Assessment[] = [];

  questions: AssessmentQuestion[] = [];

  answers: {
    [questionId: string]: string;
  } = {};

  selectedAssessment: Assessment | null = null;

  loading = true;
  loadingQuestions = false;
  submitting = false;

  showAssessment = false;

  errorMessage = '';
  successMessage = '';

  ngOnInit(): void {
    this.loadAssessments();
  }

  // =========================================================
  // LOAD MY ASSESSMENTS
  // =========================================================

  loadAssessments(): void {

    this.loading = true;
    this.errorMessage = '';

    this.assessmentService
      .getMyAssessments()
      .subscribe({

        next: (assessments) => {

          this.assessments =
            Array.isArray(assessments)
              ? assessments
              : [];

          this.loading = false;
        },

        error: (error) => {

          console.error(
            'Failed to load my assessments:',
            error
          );

          this.errorMessage =
            error?.error?.message ||
            'Unable to load your assessments.';

          this.loading = false;
        }
      });
  }

  // =========================================================
  // START ASSESSMENT
  // =========================================================

  startAssessment(
    assessment: Assessment
  ): void {

    if (
      assessment.score !== null &&
      assessment.score !== undefined
    ) {
      return;
    }

    this.selectedAssessment = assessment;

    this.questions = [];

    this.answers = {};

    this.errorMessage = '';

    this.successMessage = '';

    this.showAssessment = true;

    this.loadingQuestions = true;

    this.assessmentService
      .getAssessmentQuestions(
        assessment.assessmentId
      )
      .subscribe({

        next: (questions) => {

          this.questions =
            Array.isArray(questions)
              ? questions
              : [];

          this.loadingQuestions = false;

          if (this.questions.length === 0) {

            this.errorMessage =
              'This assessment does not contain any questions.';

          }
        },

        error: (error) => {

          console.error(
            'Failed to load assessment questions:',
            error
          );

          this.loadingQuestions = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to load assessment questions.';
        }
      });
  }

  // =========================================================
  // SELECT ANSWER
  // =========================================================

  selectAnswer(
    questionId: string,
    answer: string
  ): void {

    this.answers[questionId] = answer;
  }

  // =========================================================
  // CHECK WHETHER ALL QUESTIONS ARE ANSWERED
  // =========================================================

  areAllQuestionsAnswered(): boolean {

    if (this.questions.length === 0) {
      return false;
    }

    return this.questions.every(
      question =>
        !!this.answers[question.questionId]
    );
  }

  // =========================================================
  // SUBMIT ASSESSMENT
  // =========================================================

  submitAssessment(): void {

    if (!this.selectedAssessment) {
      return;
    }

    if (!this.areAllQuestionsAnswered()) {

      this.errorMessage =
        'Please answer all questions before submitting.';

      return;
    }

    const confirmed = confirm(
      'Are you sure you want to submit this assessment? You cannot retake it after submission.'
    );

    if (!confirmed) {
      return;
    }

    this.submitting = true;

    this.errorMessage = '';

    const submission: Answer[] =
      this.questions.map(question => ({
        questionId: question.questionId,
        answer: this.answers[question.questionId]
      }));

    this.assessmentService
      .submitAssessment(
        this.selectedAssessment.assessmentId,
        submission
      )
      .subscribe({

        next: (result) => {

          console.log(
            'Assessment submitted:',
            result
          );

          this.submitting = false;

          this.showAssessment = false;

          this.questions = [];

          this.answers = {};

          this.selectedAssessment = null;

          this.successMessage =
            `Assessment submitted successfully. Your score is ${result.score}%. Result: ${result.result}.`;

          this.loadAssessments();
        },

        error: (error) => {

          console.error(
            'Assessment submission failed:',
            error
          );

          this.submitting = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to submit assessment.';
        }
      });
  }

  // =========================================================
  // CLOSE ASSESSMENT
  // =========================================================

  closeAssessment(): void {

    if (this.submitting) {
      return;
    }

    this.showAssessment = false;

    this.selectedAssessment = null;

    this.questions = [];

    this.answers = {};

    this.errorMessage = '';
  }

  // =========================================================
  // STATUS
  // =========================================================

  getStatus(
    assessment: Assessment
  ): string {

    if (
      assessment.score !== null &&
      assessment.score !== undefined
    ) {
      return 'COMPLETED';
    }

    return 'ASSIGNED';
  }

  // =========================================================
  // RESULT CLASS
  // =========================================================

  getResultClass(
    assessment: Assessment
  ): string {

    if (assessment.result === 'PASS') {
      return 'pass';
    }

    if (assessment.result === 'FAIL') {
      return 'fail';
    }

    return 'pending';
  }
}