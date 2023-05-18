package com.smart.MySmartContactManager.Controller;

import com.smart.MySmartContactManager.Dao.UserRepository;
import com.smart.MySmartContactManager.Entity.User;
import com.smart.MySmartContactManager.Helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    @ResponseBody

    private String test() {

        User u = new User();

        u.setName("manik");
        u.setEmail("manik@gmail,com");
        userRepository.save(u);
        return "working";
    }


    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home-Smart Contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About-Smart Contact Manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register-Smart Contact Manager");

        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping("/do_register")
    public String registerUser(@Valid  @ModelAttribute("user") User user ,BindingResult result1, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
       try{

           if(!agreement){
               System.out.println("you have not checked terms and condition");
               throw new Exception("you have not checked terms and condition");

           }

           if(result1.hasErrors()){
               System.out.println("Error"+result1.toString());
               model.addAttribute("user",user);
               return "signup";

           }
           user.setEnabled(true);
           user.setRole("ROLE_USER");
           user.setImageUrl("default.png");
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           System.out.println("Agreement:"+agreement);
           System.out.println("User"+user);

           User result=userRepository.save(user);

           model.addAttribute("user",new User());
            session.setAttribute("message", new Message("successfully registered","alert-sucess"));
             return "signup";

       }catch (Exception e){
           e.printStackTrace();
           model.addAttribute("user",user);
           session.setAttribute("message",new Message("something went wrong"+e.getMessage(),"alert-danger"));
           return "signup";

       }




    }

  @GetMapping("/signin")
    public String customLogin(Model model){
        model.addAttribute("title","Login Page-Smart Contact Manager");
        return "login";
  }


}
