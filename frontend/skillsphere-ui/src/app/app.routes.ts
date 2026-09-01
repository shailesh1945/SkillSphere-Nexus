import { Routes } from '@angular/router';

import { CourseListComponent } from './learning/course-list/course-list.component';
import { CourseDetailsComponent } from './learning/course-details/course-details.component';
import { EnrollmentComponent } from './learning/enrollment/enrollment.component';
import { LearningPathComponent } from './learning/learning-path/learning-path.component';
import { CertificationListComponent } from './certification/certification-list/certification-list.component';
import { ExpiringCertificationsComponent } from './certification/expiring-certifications/expiring-certifications.component';

export const routes: Routes = [

  {
    path: '',
    redirectTo: 'learning/courses',
    pathMatch: 'full'
  },

  {
    path: 'learning/courses',
    component: CourseListComponent
  },

  {
    path: 'learning/courses/:courseId',
    component: CourseDetailsComponent   
  },

  {
    path: 'learning/enrollment',
    component: EnrollmentComponent
  },

  {
    path: 'learning/path',
    component: LearningPathComponent
  },

  {
  path: 'certifications',
  component: CertificationListComponent
},

{
  path: 'certifications/expiring',
  component: ExpiringCertificationsComponent
},

  {
    path: '**',
    redirectTo: 'learning/courses'
  }

];