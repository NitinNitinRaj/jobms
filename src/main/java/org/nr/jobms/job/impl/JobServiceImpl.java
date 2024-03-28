package org.nr.jobms.job.impl;


import org.nr.jobms.job.Job;
import org.nr.jobms.job.JobRepository;
import org.nr.jobms.job.JobService;
import org.nr.jobms.job.dto.JobDTO;
import org.nr.jobms.job.external.Company;
import org.nr.jobms.job.external.Review;
import org.nr.jobms.job.mapper.JobMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

    public JobServiceImpl(JobRepository jobRepository, RestTemplate restTemplate) {
        this.jobRepository = jobRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private JobDTO convertToDto(Job job) {

        Company company = restTemplate.getForObject("http://COMPANY-SERVICE:8081/companies/" + job.getCompanyId(), Company.class);

        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange("http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Review>>() {
                }
        );
        List<Review> reviews = reviewResponse.getBody();
        return JobMapper.mapToJobWithCompanyDto(job, company, reviews);
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return convertToDto(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setLocation(updatedJob.getLocation());
            job.setCompanyId(updatedJob.getCompanyId());
            jobRepository.save(job);
            return true;
        }
        return false;
    }
}