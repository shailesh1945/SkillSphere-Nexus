import { Routes } from '@angular/router';

import { CourseList } from './learning/course-list/course-list';
import { CourseDetails } from './learning/course-details/course-details';
import { Enrollment } from './learning/enrollment/enrollment';
import { LearningPath } from './learning/learning-path/learning-path';

export const routes: Routes = [

  {
    path: '',
    redirectTo: 'learning/courses',
    pathMatch: 'full'
  },

  {
    path: 'learning/courses',
    component: CourseList
  },

  {
    path: 'learning/courses/:courseId',
    component: CourseDetails
  },

  {
    path: 'learning/enrollment',
    component: Enrollment
  },

  {
    path: 'learning/path',
    component: LearningPath
  },

  {
    path: '**',
    redirectTo: 'learning/courses'
  }

];