package com.spring.springbootapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.spring.springbootapplication.entity.Users;
import com.spring.springbootapplication.mapper.UsersMapper;


@Controller
public class SkillCntroller {

    // データベースにアクセスするためのユーザーマッパー
    @Autowired
    private UsersMapper usersMapper;

    /**
     * 
     * @param id
     * @param mav
     * @return
     */

    @GetMapping("/users/skill/{id}")
    public ModelAndView editSkill(@PathVariable Long id, ModelAndView mav) {
        Users user = usersMapper.findById(id);
        mav.setViewName("SkillEdit");
        mav.addObject("user", user);
        return mav;
    }
    
}
