package org.nr.jobms.job.mapper;

import org.nr.jobms.job.Job;
import org.nr.jobms.job.dto.JobDTO;
import org.nr.jobms.job.external.Company;
import org.nr.jobms.job.external.Review;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDto(Job job, Company company, List<Review> reviews) {
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(job.getId());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMinSalary(job.getMinSalary());
        jobDTO.setCompany(company);
        jobDTO.setReviews(reviews);
        return jobDTO;
    }
}
