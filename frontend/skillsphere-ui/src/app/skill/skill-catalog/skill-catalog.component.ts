import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { KeycloakAuthService } from '../../services/keycloak-auth.service';
import { SkillCatalogService } from '../../services/skill-catalog.service';
import { SkillProfileService } from '../../skill-profile/skil-profile.service';

@Component({
  selector: 'app-skill-catalog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './skill-catalog.component.html',
  styleUrl: './skill-catalog.component.scss',
})
export class SkillCatalogComponent implements OnInit {
  private authService = inject(KeycloakAuthService);
  private skillService = inject(SkillCatalogService);
  private skillProfileService = inject(SkillProfileService);

  skills: any[] = [];

  showAddSkill = false;
  showEditSkill = false;

  editingSkillId: string | null = null;

  loading = false;
  saving = false;

  successMessage = '';
  errorMessage = '';

  newSkill = {
    skillName: '',
    description: '',
    category: 'TECHNICAL',
  };

  editSkill = {
    skillName: '',
    description: '',
    category: 'TECHNICAL',
  };

  ngOnInit(): void {
    this.loadSkills();

    if (this.isEmployee) {
      this.loadMyProfile();
    }
  }

  get isEmployee(): boolean {
    return this.authService.hasRole('ROLE_EMPLOYEE');
  }

  // =========================
  // ROLE CHECKS
  // =========================

  get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get isTrainingManager(): boolean {
    return this.authService.hasRole('ROLE_TRAINING_MANAGER');
  }

  get canManageSkills(): boolean {
    return this.isAdmin || this.isHr || this.isTrainingManager;
  }

  // =========================
  // STATISTICS
  // =========================

  get totalSkills(): number {
    return this.skills.length;
  }

  get technicalSkills(): number {
    return this.skills.filter((skill) => skill.category === 'TECHNICAL').length;
  }

  get softSkills(): number {
    return this.skills.filter((skill) => skill.category === 'SOFT_SKILL')
      .length;
  }

  get managementSkills(): number {
    return this.skills.filter((skill) => skill.category === 'MANAGEMENT')
      .length;
  }

  loadMyProfile(): void {
    this.loadingProfile = true;
    this.errorMessage = '';

    this.skillProfileService.getCurrentEmployee().subscribe({
      next: (employee: any) => {
        console.log('Current employee:', employee);

        this.employeeId = employee.employeeId;

        if (!this.employeeId) {
          this.loadingProfile = false;

          this.errorMessage = 'Unable to identify your employee profile.';

          return;
        }

        this.loadEmployeeSkills();
      },

      error: (error: any) => {
        console.error('Failed to load current employee:', error);

        this.loadingProfile = false;

        this.errorMessage = 'Unable to load your employee profile.';
      },
    });
  }

  loadEmployeeSkills(): void {
    if (!this.employeeId) {
      return;
    }

    this.loadingProfile = true;
    this.errorMessage = '';

    this.skillProfileService.getProfile(this.employeeId).subscribe({
      next: (profile: any) => {
        console.log('My skill profile:', profile);

        this.mySkills = this.normalizeEmployeeSkills(profile?.skills);

        console.log('Normalized My Skills:', this.mySkills);

        this.loadingProfile = false;
      },

      error: (error: any) => {
        console.error('Failed to load skill profile:', error);

        this.mySkills = [];

        this.loadingProfile = false;

        this.errorMessage = 'Unable to load your skills.';
      },
    });
  }

  closeProficiencyModal(): void {
    this.showProficiency = false;
    this.selectedSkill = null;
    this.selectedProficiency = 5;
    this.addingSkillId = null;
  }

  isSkillAdded(skillId: string): boolean {
    return this.mySkills.some(
      (employeeSkill) => String(employeeSkill?.skillId) === String(skillId),
    );
  }

  addToMySkills(skill: any): void {
    if (!this.employeeId) {
      this.errorMessage = 'Unable to identify your employee profile.';

      return;
    }

    if (!skill?.skillId) {
      this.errorMessage = 'Skill ID is missing.';

      return;
    }

    if (this.isSkillAdded(skill.skillId)) {
      this.errorMessage = 'This skill is already in your profile.';

      return;
    }

    this.selectedSkill = skill;
    this.selectedProficiency = 5;
    this.showProficiency = true;

    this.errorMessage = '';
  }

  private normalizeEmployeeSkills(skills: any[]): any[] {
    return (Array.isArray(skills) ? skills : []).map((employeeSkill: any) => {
      const skill = employeeSkill?.skill ?? employeeSkill;

      return {
        id: employeeSkill?.id,

        skillId: skill?.skillId ?? employeeSkill?.skillId,

        skillName:
          skill?.skillName ?? employeeSkill?.skillName ?? 'Unknown Skill',

        description:
          skill?.description ??
          employeeSkill?.description ??
          'No description available for this skill.',

        category: skill?.category ?? employeeSkill?.category ?? 'TECHNICAL',

        proficiency: employeeSkill?.proficiency ?? 0,
      };
    });
  }

confirmAddSkill(): void {
  if (!this.selectedSkill || !this.employeeId) {
    this.errorMessage = 'Unable to add skill.';
    return;
  }

  const skillName = this.selectedSkill.skillName;

  this.addingSkillId = this.selectedSkill.skillId;
  this.errorMessage = '';
  this.successMessage = '';

  this.skillProfileService
    .addSkill(
      this.employeeId,
      this.selectedSkill.skillId,
      this.selectedProficiency
    )
    .subscribe({
      next: () => {
        this.closeProficiencyModal();

        // IMPORTANT:
        // Fetch the profile again from backend/database
        this.skillProfileService
          .getProfile(this.employeeId)
          .subscribe({
            next: (profile: any) => {
              console.log('Reloaded profile:', profile);

              this.mySkills =
                this.normalizeEmployeeSkills(profile?.skills);

              console.log(
                'Updated My Skills:',
                this.mySkills
              );

              this.successMessage =
                `${skillName} added to your profile successfully.`;
            },

            error: (error: any) => {
              console.error(
                'Failed to reload profile:',
                error
              );

              this.errorMessage =
                'Skill was added, but the profile could not be refreshed.';
            }
          });
      },

      error: (error: any) => {
        console.error('Failed to add skill:', error);

        this.addingSkillId = null;

        this.errorMessage =
          error?.error?.message ??
          'Failed to add skill.';

        this.closeProficiencyModal();
      }
    });
}

  // =========================
  // LOAD SKILLS
  // =========================

  loadSkills(): void {
    this.loading = true;
    this.errorMessage = '';

    this.skillService.getSkills().subscribe({
      next: (data: any) => {
        this.skills = Array.isArray(data) ? data : [];

        this.loading = false;
      },

      error: (error: any) => {
        console.error('Failed to load skills:', error);

        this.loading = false;

        this.errorMessage = 'Unable to load skills.';
      },
    });
  }

  // =========================
  // ADD SKILL
  // =========================

  openAddSkill(): void {
    this.showAddSkill = true;
    this.showEditSkill = false;
    this.editingSkillId = null;

    this.successMessage = '';
    this.errorMessage = '';

    this.newSkill = {
      skillName: '',
      description: '',
      category: 'TECHNICAL',
    };
  }

  cancelAddSkill(): void {
    this.showAddSkill = false;

    this.newSkill = {
      skillName: '',
      description: '',
      category: 'TECHNICAL',
    };
  }

  addSkill(): void {
    if (!this.newSkill.skillName.trim()) {
      this.errorMessage = 'Skill name is required.';

      return;
    }

    if (!this.newSkill.category) {
      this.errorMessage = 'Skill category is required.';

      return;
    }

    this.saving = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.skillService
      .addSkill({
        skillName: this.newSkill.skillName.trim(),

        description: this.newSkill.description.trim(),

        category: this.newSkill.category,
      })
      .subscribe({
        next: (skill: any) => {
          this.skills.push(skill);

          this.saving = false;
          this.showAddSkill = false;

          this.successMessage = 'Skill added successfully.';

          this.newSkill = {
            skillName: '',
            description: '',
            category: 'TECHNICAL',
          };
        },

        error: (error: any) => {
          console.error('Failed to add skill:', error);

          this.saving = false;

          this.errorMessage = error.error?.message ?? 'Failed to add skill.';
        },
      });
  }

  // =========================
  // EDIT SKILL
  // =========================

  openEditSkill(skill: any): void {
    this.showEditSkill = true;
    this.showAddSkill = false;

    this.editingSkillId = skill.skillId;

    this.successMessage = '';
    this.errorMessage = '';

    this.editSkill = {
      skillName: skill.skillName ?? '',
      description: skill.description ?? '',
      category: skill.category ?? 'TECHNICAL',
    };

    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }

  cancelEditSkill(): void {
    this.showEditSkill = false;
    this.editingSkillId = null;

    this.editSkill = {
      skillName: '',
      description: '',
      category: 'TECHNICAL',
    };
  }

  updateSkill(): void {
    if (!this.editingSkillId) {
      return;
    }

    if (!this.editSkill.skillName.trim()) {
      this.errorMessage = 'Skill name is required.';

      return;
    }

    if (!this.editSkill.category) {
      this.errorMessage = 'Skill category is required.';

      return;
    }

    this.saving = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.skillService
      .updateSkill(this.editingSkillId, {
        skillName: this.editSkill.skillName.trim(),

        description: this.editSkill.description.trim(),

        category: this.editSkill.category,
      })
      .subscribe({
        next: (updatedSkill: any) => {
          const index = this.skills.findIndex(
            (skill) => skill.skillId === this.editingSkillId,
          );

          if (index !== -1) {
            this.skills[index] = updatedSkill;
          }

          this.saving = false;
          this.showEditSkill = false;
          this.editingSkillId = null;

          this.successMessage = 'Skill updated successfully.';

          this.editSkill = {
            skillName: '',
            description: '',
            category: 'TECHNICAL',
          };
        },

        error: (error: any) => {
          console.error('Failed to update skill:', error);

          this.saving = false;

          this.errorMessage = error.error?.message ?? 'Failed to update skill.';
        },
      });
  }

  // =========================
  // DELETE SKILL
  // =========================

  deleteSkill(skill: any): void {
    const confirmed = window.confirm(
      `Are you sure you want to delete "${skill.skillName}"?`,
    );

    if (!confirmed) {
      return;
    }

    this.successMessage = '';
    this.errorMessage = '';

    this.skillService.deleteSkill(skill.skillId).subscribe({
      next: () => {
        this.skills = this.skills.filter(
          (item) => item.skillId !== skill.skillId,
        );

        this.successMessage = 'Skill deleted successfully.';
      },

      error: (error: any) => {
        console.error('Failed to delete skill:', error);

        this.errorMessage = error.error?.message ?? 'Failed to delete skill.';
      },
    });
  }

  get availableSkills(): any[] {
  return this.skills.filter(
    skill => !this.isSkillAdded(skill.skillId)
  );
}

  employeeId = '';

  mySkills: any[] = [];

  loadingProfile = false;

  addingSkillId: string | null = null;

  selectedProficiency = 5;

  showProficiency = false;

  selectedSkill: any = null;
}
