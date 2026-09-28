import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';





import { AssessmentService } from '../services/assessment.service';
import { EmployeeService } from '../services/employee.service';
import { SkillCatalogService } from '../services/skill-catalog.service';
import { KeycloakAuthService } from '../services/keycloak-auth.service';

interface AssessmentQuestionForm {
  questionText: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
  correctAnswer: string;
  marks: number | null;
}

@Component({
  selector: 'app-assessments',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './assessments.component.html',
  styleUrl: './assessments.component.scss'
})
export class AssessmentComponent implements OnInit {

  private assessmentService = inject(AssessmentService);
  private employeeService = inject(EmployeeService);
  private skillService = inject(SkillCatalogService);
  private authService = inject(KeycloakAuthService);

  assessments: any[] = [];
  employees: any[] = [];
  skills: any[] = [];

  loading = true;
  saving = false;

  showForm = false;

  errorMessage = '';
  successMessage = '';

  newAssessment = {
  assessmentName: '',
  assessmentDate: '',
  employeeId: '',
  skillId: '',
  questions: [] as AssessmentQuestionForm[]
};

  ngOnInit(): void {
    this.loadData();
  }

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get isTrainingManager(): boolean {
    return this.authService.hasRole(
      'ROLE_TRAINING_MANAGER'
    );
  }

  get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get canManageAssessments(): boolean {
    return (
      this.isHr ||
      this.isTrainingManager ||
      this.isAdmin
    );
  }

  loadData(): void {

    this.loading = true;
    this.errorMessage = '';

    this.assessmentService
      .getAssessments()
      .subscribe({

        next: (assessments) => {

          this.assessments =
            Array.isArray(assessments)
              ? assessments
              : [];

          this.loadEmployees();
        },

        error: (error) => {

          console.error(
            'Failed to load assessments:',
            error
          );

          this.errorMessage =
            'Unable to load assessments.';

          this.loading = false;
        }
      });
  }

  loadEmployees(): void {

    this.employeeService
      .getEmployees()
      .subscribe({

        next: (employees) => {

          this.employees =
            Array.isArray(employees)
              ? employees
              : [];

          this.loadSkills();
        },

        error: (error) => {

          console.error(
            'Failed to load employees:',
            error
          );

          this.errorMessage =
            'Unable to load employees.';

          this.loading = false;
        }
      });
  }

  loadSkills(): void {

    this.skillService
      .getSkills()
      .subscribe({

        next: (skills) => {

          this.skills =
            Array.isArray(skills)
              ? skills
              : [];

          this.loading = false;
        },

        error: (error) => {

          console.error(
            'Failed to load skills:',
            error
          );

          this.errorMessage =
            'Unable to load skills.';

          this.loading = false;
        }
      });
  }

  openCreateForm(): void {

    this.resetForm();

    this.showForm = true;

    this.errorMessage = '';
    this.successMessage = '';
  }

  closeForm(): void {

    if (this.saving) {
      return;
    }

    this.showForm = false;
  }

  createAssessment(): void {

  if (
    !this.newAssessment.assessmentName.trim() ||
    !this.newAssessment.employeeId ||
    !this.newAssessment.skillId
  ) {

    this.errorMessage =
      'Please fill in the assessment name, employee and skill.';

    return;
  }

  if (this.newAssessment.questions.length === 0) {

    this.errorMessage =
      'Please add at least one question.';

    return;
  }

  for (
    const question of this.newAssessment.questions
  ) {

    if (
      !question.questionText.trim() ||
      !question.optionA.trim() ||
      !question.optionB.trim() ||
      !question.optionC.trim() ||
      !question.optionD.trim() ||
      !question.correctAnswer ||
      question.marks === null ||
      question.marks <= 0
    ) {

      this.errorMessage =
        'Please complete all question fields.';

      return;
    }
  }

  this.saving = true;
  this.errorMessage = '';
  this.successMessage = '';

  const payload = {

    assessmentName:
      this.newAssessment.assessmentName.trim(),

    assessmentDate:
      this.newAssessment.assessmentDate || null,

    employeeId:
      this.newAssessment.employeeId,

    skillId:
      this.newAssessment.skillId,

    questions:
      this.newAssessment.questions.map(question => ({

        questionText:
          question.questionText.trim(),

        optionA:
          question.optionA.trim(),

        optionB:
          question.optionB.trim(),

        optionC:
          question.optionC.trim(),

        optionD:
          question.optionD.trim(),

        correctAnswer:
          question.correctAnswer,

        marks:
          Number(question.marks)
      }))
  };

  console.log(
    'Creating assessment:',
    payload
  );

  this.assessmentService
    .createAssessment(payload)
    .subscribe({

      next: (response) => {

        console.log(
          'Assessment created:',
          response
        );

        this.saving = false;

        this.showForm = false;

        this.successMessage =
          'Skill assessment created successfully.';

        this.resetForm();

        this.loadData();
      },

      error: (error) => {

        console.error(
          'Assessment creation failed:',
          error
        );

        this.saving = false;

        this.errorMessage =
          error?.error?.message ||
          'Unable to create assessment.';
      }
    });
}

  verifyAssessment(
    assessmentId: string
  ): void {

    this.assessmentService
      .verifyAssessment(assessmentId)
      .subscribe({

        next: () => {

          this.successMessage =
            'Assessment verified successfully.';

          this.loadData();
        },

        error: (error) => {

          console.error(
            'Verification failed:',
            error
          );

          this.errorMessage =
            'Unable to verify assessment.';
        }
      });
  }

addQuestion(): void {

  this.newAssessment.questions.push({
    questionText: '',
    optionA: '',
    optionB: '',
    optionC: '',
    optionD: '',
    correctAnswer: '',
    marks: 10
  });
}

removeQuestion(index: number): void {

  if (this.newAssessment.questions.length <= 1) {
    return;
  }

  this.newAssessment.questions.splice(index, 1);
}

trackQuestion(
  index: number,
  question: AssessmentQuestionForm
): number {
  return index;
}

  deleteAssessment(
    assessmentId: string
  ): void {

    if (
      !confirm(
        'Are you sure you want to delete this assessment?'
      )
    ) {
      return;
    }

    this.assessmentService
      .deleteAssessment(assessmentId)
      .subscribe({

        next: () => {

          this.successMessage =
            'Assessment deleted successfully.';

          this.loadData();
        },

        error: (error) => {

          console.error(
            'Delete failed:',
            error
          );

          this.errorMessage =
            'Unable to delete assessment.';
        }
      });
  }

  getEmployeeName(
    employeeId: string
  ): string {

    const employee =
      this.employees.find(
        e => e.employeeId === employeeId
      );

    if (!employee) {
      return 'Unknown Employee';
    }

    return [
      employee.firstName,
      employee.lastName
    ]
      .filter(Boolean)
      .join(' ');
  }

  getSkillName(
    skillId: string
  ): string {

    const skill =
      this.skills.find(
        s => s.skillId === skillId
      );

    return skill?.skillName ||
      'Unknown Skill';
  }

  resetForm(): void {

  this.newAssessment = {

    assessmentName: '',

    assessmentDate: '',

    employeeId: '',

    skillId: '',

    questions: []
  };

  this.addQuestion();

  }
}