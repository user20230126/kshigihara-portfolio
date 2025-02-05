package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report, UserDetail userdetail) {

        if (reportRepository.existsByEmployeeAndReportDate(userdetail.getEmployee(), report.getReportDate())) {
            return ErrorKinds.DATECHECK_ERROR;
        }

        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);
        report.setEmployee(userdetail.getEmployee());

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報削除
    @Transactional
    public ErrorKinds delete(Integer id) {

        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }
    
    //権限なし表示処理
    public List<Report> filterRole(UserDetail userdetail) {
        List<Report> allReports = reportRepository.findAll();
        List<Report> filterReports = new ArrayList<Report>();
        
        if(userdetail.getEmployee().getRole().getValue().equals("一般")) {
            for(Report rep : allReports) {
                if(rep.getEmployee().getName().equals(userdetail.getEmployee().getName())) {
                    filterReports.add(rep);
                }
            }
            return filterReports;
        }
        return allReports;
    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }
    
    //日報更新
    @Transactional
    public ErrorKinds update(UserDetail userdetail, Report report, Integer id) {
        
        List<Report> reportList = reportRepository.findByEmployee(userdetail.getEmployee());
        Report beforeReport = reportRepository.findById(id).get();
        
        if(reportList != null && beforeReport.getEmployee().getCode().equals(userdetail.getEmployee().getCode()) && !beforeReport.getReportDate().equals(report.getReportDate())) {
            for(Report rep:reportList) {
                if(rep.getReportDate().equals(report.getReportDate())) {
                    return ErrorKinds.DATECHECK_ERROR;
                }
            }
        }
        
        Report updateReport = findById(report.getId());
        report.setDeleteFlg(false);
        updateReport.setTitle(report.getTitle());
        updateReport.setReportDate(report.getReportDate());
        updateReport.setContent(report.getContent());
        LocalDateTime now = LocalDateTime.now();
        
        updateReport.setCreatedAt(reportRepository.findById(report.getId()).get().getCreatedAt());
        updateReport.setUpdatedAt(now);
        
        reportRepository.save(updateReport);
        return ErrorKinds.SUCCESS;
    }

}