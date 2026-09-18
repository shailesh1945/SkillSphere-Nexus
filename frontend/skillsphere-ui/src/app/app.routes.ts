import { Routes } from '@angular/router';

import { CourseListComponent } from './learning/course-list/course-list.component';
import { CourseDetailsComponent } from './learning/course-details/course-details.component';
import { EnrollmentComponent } from './learning/enrollment/enrollment.component';
import { LearningPathComponent } from './learning/learning-path/learning-path.component';

import { CertificationListComponent } from './certification/certification-list/certification-list.component';
import { ExpiringCertificationsComponent } from './certification/expiring-certifications/expiring-certifications.component';

import { DashboardComponent }
  from './dashboard/dashboard.component';

import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [

  {
  path: 'dashboard',
  component: DashboardComponent,
  canActivate: [authGuard]
},
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },

  // =========================
  // LEARNING
  // =========================

  {
    path: 'learning/courses',
    component: CourseListComponent,
    canActivate: [authGuard],
  },

  {
    path: 'learning/courses/:courseId',
    component: CourseDetailsComponent,
    canActivate: [authGuard],
  },

  {
    path: 'learning/enrollment',
    component: EnrollmentComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ['EMPLOYEE', 'ADMIN'],
    },
  },

  {
    path: 'learning/path',
    component: LearningPathComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ['EMPLOYEE', 'TRAINING_MANAGER', 'ADMIN'],
    },
  },

  // =========================
  // CERTIFICATIONS
  // =========================

  {
    path: 'certifications',
    component: CertificationListComponent,
    canActivate: [authGuard],
  },

  {
    path: 'certifications/expiring',
    component: ExpiringCertificationsComponent,
    canActivate: [authGuard, roleGuard],
    data: {
      roles: ['HR', 'ADMIN'],
    },
  },

  // =========================
  // FALLBACK
  // =========================

  {
    path: '**',
    redirectTo: 'learning/courses',
  },
];
