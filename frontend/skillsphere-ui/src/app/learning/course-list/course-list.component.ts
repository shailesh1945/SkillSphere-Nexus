import { Component, OnInit } from '@angular/core';
import { LearningService } from '../learning.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './course-list.component.html',
  styleUrl: './course-list.component.scss'
})
export class CourseListComponent implements OnInit {

  courses: any[] = [];

  totalCourses = 0
  enrollments = 0;
  completion = 0;
  completionRate = 0;

  constructor(
    private learningService: LearningService
  ) {}

  ngOnInit(): void {
    this.loadCourses();
  }


  loadCourses(): void {

    this.learningService.getCourses().subscribe({

      next: (data: any[]) => {

        this.courses = data;

        this.totalCourses = data.length;

        this.loadStatistics();

      },

      error: (error) => {

        console.error(
          'Error loading courses:',
          error
        );

      }

    });
  }


  loadStatistics(): void {

    let totalEnrollments = 0;
    let completedEnrollments = 0;

    let coursesLoaded = 0;

    if (this.courses.length === 0) {
      return;
    }


    this.courses.forEach((course: any) => {

      this.learningService
        .getCourseEnrollments(course.courseId)
        .subscribe({

          next: (data: any[]) => {

            totalEnrollments += data.length;


            completedEnrollments += data.filter(
              enrollment => enrollment.progress === 100
            ).length;


            coursesLoaded++;


            if (coursesLoaded === this.courses.length) {

              this.enrollments = totalEnrollments;

              this.completion = completedEnrollments;


              if (totalEnrollments > 0) {

                this.completionRate =
                  Math.round(
                    (completedEnrollments /
                      totalEnrollments) * 100
                  );

              }

            }

          },

          error: (error) => {

            console.error(
              'Error loading enrollments:',
              error
            );

          }

        });

    });

  }


  enroll(course: any): void {

    console.log(
      'Enroll clicked:',
      course
    );

  }

}