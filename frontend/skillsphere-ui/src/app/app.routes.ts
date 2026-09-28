import { Routes } from '@angular/router';

import { CourseListComponent } from './learning/course-list/course-list.component';
import { CourseDetailsComponent } from './learning/course-details/course-details.component';
import { EnrollmentComponent } from './learning/enrollment/enrollment.component';
import { LearningPathComponent } from './learning/learning-path/learning-path.component';

import { CertificationListComponent } from './certification/certification-list/certification-list.component';
import { ExpiringCertificationsComponent } from './certification/expiring-certifications/expiring-certifications.component';

import { DashboardComponent } from './dashboard/dashboard.component';

import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { CareerComponent } from './pages/career/career.component';
import { JobsComponent } from './pages/jobs/jobs.component';
import { AccessDeniedComponent } from './pages/access-denied/access-denied.component';

import { EmployeeProfileComponent } from './skill-profile/employee-profile/employee-profile.component';

import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { EmployeesComponent } from './employees/employees.component';

import { SkillCatalogComponent } from './skill/skill-catalog/skill-catalog.component';
import { AssessmentComponent } from './assessments/assessments.component';
import { MyAssessmentsComponent } from './assessments/my-assessments/my-assessments.component';

export const routes: Routes = [

  // =========================
  // DASHBOARD
  // =========================

  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },


  {
  path: 'skills',
  component: SkillCatalogComponent,
  canActivate: [authGuard]
},

  // =========================
  // EMPLOYEE / SKILL PROFILE
  // =========================

  {
  path: 'employees',
  component: EmployeesComponent,
  canActivate: [authGuard, roleGuard],
  data: {
    roles: ['ROLE_HR', 'ROLE_ADMIN']
  }
},

{
  path: 'employees/:empId',
  component: EmployeeProfileComponent,
  canActivate: [authGuard, roleGuard],
  data: {
    roles: ['ROLE_HR', 'ROLE_ADMIN']
  }
},

  // {
  //   path: 'employees',
  //   component: EmployeeProfileComponent,
  //   canActivate: [authGuard, roleGuard],
  //   data: {
  //     roles: [
  //       'ROLE_ADMIN',
  //       'ROLE_HR'
  //     ]
  //   }
  // },

  // =========================
  // LEARNING
  // =========================

  {
    path: 'learning/courses',
    component: CourseListComponent,
    canActivate: [authGuard]
  },

  {
    path: 'learning/courses/:courseId',
    component: CourseDetailsComponent,
    canActivate: [authGuard]
  },


  // =========================
// SKILL ASSESSMENTS
// =========================

{
  path: 'assessments',
  component: AssessmentComponent,
  canActivate: [authGuard, roleGuard],
  data: {
    roles: [
      'ROLE_HR',
      'ROLE_TRAINING_MANAGER',
      'ROLE_ADMIN'
    ]
  }
},

{
  path: 'my-assessments',
  component: MyAssessmentsComponent,
  canActivate: [authGuard, roleGuard],
  data: {
    roles: ['ROLE_EMPLOYEE']
  }
},

  {
    path: 'learning/enrollment',
    component: EnrollmentComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: [
        'ROLE_EMPLOYEE',
        'ROLE_ADMIN'
      ]
    }
  },

  {
    path: 'learning/path',
    component: LearningPathComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: [
        'ROLE_EMPLOYEE',
        'ROLE_TRAINING_MANAGER',
        'ROLE_ADMIN'
      ]
    }
  },

  // =========================
  // CERTIFICATIONS
  // =========================

  {
    path: 'certifications',
    component: CertificationListComponent,
    canActivate: [authGuard]
  },

  {
    path: 'certifications/expiring',
    component: ExpiringCertificationsComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: [
        'ROLE_HR',
        'ROLE_ADMIN'
      ]
    }
  },

  // =========================
  // CAREER
  // =========================

  {
    path: 'career',
    component: CareerComponent,
    canActivate: [authGuard]
  },

  // =========================
  // JOBS
  // =========================

  {
    path: 'jobs',
    component: JobsComponent,
    canActivate: [authGuard]
  },

  // =========================
  // ANALYTICS
  // =========================

  {
    path: 'analytics',
    component: AnalyticsComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: [
        'ROLE_ADMIN',
        'ROLE_HR',
        'ROLE_TRAINING_MANAGER'
      ]
    }
  },

  // =========================
  // ACCESS DENIED
  // =========================

  {
    path: 'access-denied',
    component: AccessDeniedComponent
  },

  // =========================
  // DEFAULT
  // =========================

  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },

  // =========================
  // FALLBACK
  // =========================

  {
    path: '**',
    redirectTo: 'dashboard'
  }
];