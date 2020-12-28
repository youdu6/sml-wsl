package com.wsl.controller;

import com.wsl.dao.*;
import com.wsl.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Controller
public class LoginController {

//    @DeleteMapping
//    @PutMapping
//    @GetMapping
    @Autowired
    UserDao userDao;
    @Autowired
    ProducerDao producerDao;
    @Autowired
    CartDao cartDao;
    @Autowired
    AdminDao adminDao;
    @Autowired
    NoticeDao noticeDao;
    //@RequestMapping(value = "/User/login",method = RequestMethod.POST)
    @PostMapping(value = "/user/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map, HttpSession session){
        Collection<User>  users=userDao.getAll();
        session.setAttribute("notices",noticeDao.getAll());
        for (User user:users){
            if (user.getName().equals(username)){
                if (user.getPassword()==Integer.parseInt(password)) {
                    session.setAttribute("loginUser", username);
                    session.setAttribute("isUser",true);
                    session.setAttribute("isAdmin",false);
                    return "redirect:/main.html";
                }else break;
            }
        }


        Collection<Producer>  producers=producerDao.getAll();
        for (Producer producer:producers){
            if (producer.getName().equals(username)){
                if (producer.getPassword()==Integer.parseInt(password)) {
                    session.setAttribute("loginUser", username);
                    session.setAttribute("isUser",false);
                    session.setAttribute("isAdmin",false);
                    return "redirect:/main.html";
                }else break;
            }
        }

        Collection<Admin> admins=adminDao.getAll();
        for (Admin admin:admins){
            if (admin.getName().equals(username)){
                if (admin.getPassword()==Integer.parseInt(password)) {
                    session.setAttribute("loginUser", username);
                    session.setAttribute("isUser",false);
                    session.setAttribute("isAdmin",true);
                    return "redirect:/main.html";
                }else break;
            }
        }

//        if(!StringUtils.isEmpty(username) && "123456".equals(password)){
//
//            session.setAttribute("loginUser",username);
//            return "redirect:/main.html";
////            return "dashboard2";
//        }else{

//        }
        map.put("msg","username/password wrong!");
        return  "login";
    }

    @GetMapping(value = "/user/register")
    public String register(){
        return "register";
    }

    @PostMapping(value = "/user/register")
    public String registerSolve(@RequestParam("username") String username,
                                @RequestParam("password") String password){
        Collection<User>  users=userDao.getAll();
        for (User user:users){
            if (user.getName().equals(username)){
                return "wrong";
            }
        }
        userDao.save(new User(null,username,Integer.parseInt(password)));
        cartDao.save(new Cart(null,new ArrayList<Game>(),username),username);
        return "redirect:/";
    }
}
