
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CertificationService } from '../certification.service';

@Component({
  selector: 'app-expiring-certifications',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './expiring-certifications.component.html',
  styleUrl: './expiring-certifications.component.scss'
})
export class ExpiringCertificationsComponent implements OnInit {

  certifications: any[] = [];

  constructor(
    private certificationService: CertificationService
  ) {}

  ngOnInit(): void {

    this.certificationService
      .getExpiring()
      .subscribe({
        next: (data: any) => {
          this.certifications = data;
        },
        error: (error) => {
          console.error(
            'Error loading expiring certifications:',
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
}