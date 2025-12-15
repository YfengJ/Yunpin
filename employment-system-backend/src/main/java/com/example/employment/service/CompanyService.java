package com.example.employment.service;
import com.example.employment.entity.Company;
import java.util.List;

public interface CompanyService {
    List<Company> getAll();
    Company getById(Long id);
    int save(Company company);
    int update(Company company);
    int deleteById(Long id);
}