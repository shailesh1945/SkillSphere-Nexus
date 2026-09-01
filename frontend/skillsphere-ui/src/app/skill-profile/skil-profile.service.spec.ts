import { TestBed } from '@angular/core/testing';

import { SkilProfileService } from './skil-profile.service';

describe('SkilProfileService', () => {
  let service: SkilProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SkilProfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
