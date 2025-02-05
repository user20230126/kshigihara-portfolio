package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetail userdetail, Model model) {

        model.addAttribute("listSize", reportService.filterRole(userdetail).size());
        model.addAttribute("reportList", reportService.filterRole(userdetail));

        return "reports/list";
    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {

        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@AuthenticationPrincipal UserDetail userdetail, @ModelAttribute Report report, Model model) {
        model.addAttribute("employeeName", userdetail.getEmployee().getName());

        return "reports/new";
    }

    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, @AuthenticationPrincipal UserDetail userdetail,
            Employee employee, Model model) {

        // 入力チェック
        if (res.hasErrors()) {
            return create(userdetail, report, model);
        }

        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
        try {
            ErrorKinds result = reportService.save(report, userdetail);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return create(userdetail, report, model);
            }

        } catch (DataIntegrityViolationException e) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
            return create(userdetail, report, model);
        }

        return "redirect:/reports";
    }

    // 日報削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return detail(id, model);
        }

        return "redirect:/reports";
    }
    
    //日報更新画面
    @GetMapping(value = "/{id}/update")
    public String getReport(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetail userdetail, Model model, @ModelAttribute Report report) {
    
        model.addAttribute("employeeName", userdetail.getEmployee().getName());
        
        if (id == null) {
            model.addAttribute("report",report);
            return "reports/update";
            
        } else {
            model.addAttribute("report", reportService.findById(id));
            return "reports/update";
        }
    }
    
    // 日報更新処理
    @PostMapping(value = "/{id}/update")
    public String update(@PathVariable("id") Integer id, @Validated Report report,BindingResult res,
            @AuthenticationPrincipal UserDetail userdetail,  Model model) {
        if (res.hasErrors()) {
            // エラーあり
            return getReport(null, userdetail, model, report);
        }

        try {
            ErrorKinds result = reportService.update(userdetail, report, id);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return getReport(null, userdetail, model, report);
            }

        } catch (DataIntegrityViolationException e) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
            return getReport(null, userdetail, model, report);
        }

        return "redirect:/reports";
    }

}