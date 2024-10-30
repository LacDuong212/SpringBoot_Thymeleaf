package LD01.springboot_thymeleaf.Controller;

import LD01.springboot_thymeleaf.Entity.CategoryEntity;
import LD01.springboot_thymeleaf.Model.CategoryModel;
import LD01.springboot_thymeleaf.Service.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping("/add")
    public String add(ModelMap model) {
        CategoryModel cateModel = new CategoryModel();
        cateModel.setIsEdit(false);
        model.addAttribute("category", cateModel);
        return "admin/categories/addOrEdit";
    }

    @PostMapping("/saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryModel cateModel, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");
        }
        CategoryEntity entity = new CategoryEntity();

        BeanUtils.copyProperties(cateModel, entity);
        this.categoryService.save(entity);
        String message = "";
        if (cateModel.getIsEdit() == true) {
            message = "Category is Edited!!!";
        } else {
            message = "Category is saved!!!";
        }
        model.addAttribute("message", message);
        return new ModelAndView("forward:/admin/categories", model);
    }

    @RequestMapping("")
    public String list(ModelMap model) {
        int count = (int) categoryService.count();
        int currentPage = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("name"));
        Page<CategoryEntity> resultPage = null;
        resultPage = categoryService.findAll(pageable);
        int totalPages = resultPage.getTotalPages();
        if(totalPages > 0) {
            int start = Math.max(1, currentPage-2);
            int end = Math.min(currentPage + 2, totalPages);
            if(totalPages > count) {
                if(end == totalPages) start = end - count;
                else if (start == 1) end = start + count;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers",pageNumbers);
        }
        model.addAttribute("categoryPage",resultPage);
        return "admin/categories/list";
    }

    @GetMapping("edit/{categoryId}")
    public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        CategoryModel categoryModel = new CategoryModel();
        Optional<CategoryEntity> category = this.categoryService.findById(categoryId);
        CategoryEntity entity = category.get();
        BeanUtils.copyProperties(entity, categoryModel);
        categoryModel.setIsEdit(true);
        model.addAttribute("category", categoryModel);

        return new ModelAndView("admin/categories/addOrEdit", model);
    }

    @GetMapping("delete/{categoryId}")
    public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        model.addAttribute("message", "Category is deleted!!!");
        List<CategoryEntity> list = categoryService.findAll();
        model.addAttribute("categories", list);
        return new ModelAndView("redirect:/admin/categories");
    }

    @GetMapping("searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name="name",required = false) String name,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        int count = (int) categoryService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Pageable pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("name"));
        Page<CategoryEntity> resultPage = null;
        if(StringUtils.hasText(name)) {
            resultPage = categoryService.findByCategoryNameContaining(name,pageable);
            model.addAttribute("name",name);
        }else {
            resultPage = categoryService.findAll(pageable);
        }
        int totalPages = resultPage.getTotalPages();
        if(totalPages > 0) {
            int start = Math.max(1, currentPage-2);
            int end = Math.min(currentPage + 2, totalPages);
            if(totalPages > count) {
                if(end == totalPages) start = end - count;
                else if (start == 1) end = start + count;
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers",pageNumbers);
        }
        model.addAttribute("categoryPage",resultPage);
        return "admin/categories/list";
    }
}
