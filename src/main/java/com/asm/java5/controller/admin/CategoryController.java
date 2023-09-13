package com.asm.java5.controller.admin;

import com.asm.java5.domain.Category;
import com.asm.java5.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Controller
@RequestMapping("admin/categories")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;
//    Phải sử dụng findAllBykeywordLike để tối ưu khả năng search
    @ModelAttribute("categorys")
    public Page<Category> getAll(@RequestParam("page") Optional<Integer> page
            , @RequestParam("keyword") Optional<String> keyword, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), 3);
        Page<Category> categorys = categoryRepository.findAllByNameLike("%" + keyword.orElse("") + "%",pageable);
        model.addAttribute("keyword", keyword.orElse(""));
        return categorys;
    }
    @RequestMapping("")
    public String list(@ModelAttribute("category") Category category){
        return "admin/manager-category";
    }
    @GetMapping("delete")
    public String delete(@RequestParam("id") Integer id, Model model){
        categoryRepository.deleteById(id);
        model.addAttribute("message", "Xóa thành công!");
        return "forward:/admin/categories";
    }
    @GetMapping("edit")
    public String edit(Model model, @RequestParam("id") Integer id){
        Category category = categoryRepository.findById(id).get();
        model.addAttribute("category", category);
        return "admin/manager-category";
    }
    @PostMapping("update-or-create")
    public String save(Model model, @Valid @ModelAttribute("category") Category category,
                        BindingResult result){
        if(result.hasErrors()){
            return "admin/manager-category";
        }
            categoryRepository.save(category);
            model.addAttribute("message", "Lưu thành công!");
            return "forward:/admin/categories";

    }
    @PostMapping("search")
    public String search(Model model, @RequestParam("keyword") Optional<String> keyword,
                         @RequestParam("page") Optional<Integer> p){
        Pageable pageable = PageRequest.of(p.orElse(0), 3);
        Page<Category> categories  = categoryRepository.findAllByNameLike("%" + keyword.orElse("") + "%", pageable);
        model.addAttribute("keyword", keyword.orElse(""));
        model.addAttribute("category", new Category());
        model.addAttribute("categorys", categories);
        return "admin/manager-category";
    }

}
