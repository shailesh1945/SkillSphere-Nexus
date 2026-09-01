import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CertificationService } from '../certification.service';

@Component({
  selector: 'app-certification-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './certification-list.component.html',
  styleUrl: './certification-list.component.scss'
})
export class CertificationListComponent implements OnInit {

  certifications: any[] = [];

  constructor(
    private certificationService: CertificationService
  ) {}

  ngOnInit(): void {

    const empId = '4d6e086b-742b-4505-ad44-e6206f7e1c7a';

    this.certificationService
      .getEmployeeCertifications(empId)
      .subscribe({
        next: (data: any) => {
          this.certifications = data;
        },
        error: (error) => {
          console.error(
            'Error loading certifications:',
            error
          );
        }
      });
  }

  trackByCertificationId(
    index: number,
    cert: any
  ): string {
    return cert.id;
  }

  getValidCount(): number {
  return this.certifications.filter(
    cert => cert.status === 'VALID'
  ).length;
}

getExpiredCount(): number {
  return this.certifications.filter(
    cert => cert.status === 'EXPIRED'
  ).length;
}
}