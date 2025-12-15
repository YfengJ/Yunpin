package com.example.employment.service.impl;

import com.example.employment.entity.Company;
import com.example.employment.mapper.CompanyMapper;
import com.example.employment.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired private CompanyMapper companyMapper;
    @Override public List<Company> getAll() { return companyMapper.findAll(); }
    @Override public Company getById(Long id) { return companyMapper.findById(id); }
    @Override public int save(Company company) { return companyMapper.insert(company); }
    @Override public int update(Company company) { return companyMapper.update(company); }
    @Override public int deleteById(Long id) { return companyMapper.deleteById(id); }
}