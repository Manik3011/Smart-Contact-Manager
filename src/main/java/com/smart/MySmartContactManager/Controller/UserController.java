package com.smart.MySmartContactManager.Controller;

import com.smart.MySmartContactManager.Dao.ContactRepository;
import com.smart.MySmartContactManager.Dao.UserRepository;
import com.smart.MySmartContactManager.Entity.Contact;
import com.smart.MySmartContactManager.Entity.User;
import com.smart.MySmartContactManager.Helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String Username=principal.getName();
        System.out.println("username ="+Username);

        User user= userRepository.getUserBuUsername(Username);

        System.out.println("User="+user);
        model.addAttribute("user",user);
    }

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal){


        return "Normal/dashboard";
    }


    @RequestMapping("/add-contact")
    public String addContact(Model model){
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new Contact());
        return "Normal/add_contact_form";
    }

    @PostMapping("/process-data")
    public String processData(@ModelAttribute Contact contact, @RequestParam("profileImage")MultipartFile file, Principal principal, HttpSession session){

 try {

     String name = principal.getName();
     User user = userRepository.getUserBuUsername(name);

     if(file.isEmpty()){

         System.out.println("image cannot be uploaded");
         contact.setImage("contact.png");

     }else{
         contact.setImage(file.getOriginalFilename());

         File savefile=new ClassPathResource("/static/img").getFile();

      Path path=   Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());

         Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
         System.out.println("image is uploaded");
     }

     contact.setUser(user);

     user.getContacts().add(contact);
     userRepository.save(user);

    // System.out.println("contacts :" + contact);

     session.setAttribute("message",new Message("your Contact is added","alert-success"));


 }catch (Exception e){
     System.out.println("error"+e.getMessage());
     e.printStackTrace();

     session.setAttribute("message",new Message("Something went wrong ","alert-danger"));


 }
        return "Normal/add_contact_form";
    }

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal){

        m.addAttribute("title","Show Contacts");

        String userName= principal.getName();

        User user=userRepository.getUserBuUsername(userName);

        Pageable pageable= PageRequest.of(page,3);

        Page<Contact> contacts=contactRepository.getAllContactsByUserId(user.getId(),pageable);
        m.addAttribute("contacts",contacts);
        m.addAttribute("currentPage",page);
        m.addAttribute("totalPages",contacts.getTotalPages());



      //  m.addAttribute("title","Show Contacts");

        return "Normal/ShowContacts";
    }

    @GetMapping("/{cid}/contact")
    public String contactDescription(Model m,@PathVariable("cid")int cid,Principal principal){


        System.out.println(cid);

       Optional<Contact>optionalContact = contactRepository.findById(cid);
       Contact contact=optionalContact.get();

      String userName= principal.getName();

      User user=userRepository.getUserBuUsername(userName);

      if (user.getId()==contact.getUser().getId())

       m.addAttribute("contact",contact);
        return "Normal/contactDetail" ;
    }

    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid")Integer cid,Model model,HttpSession session, Principal principal) {
        Optional<Contact>contactOptional=contactRepository.findById(cid);
        Contact contact=contactOptional.get();

        String userName=principal.getName();
        User user=userRepository.getUserBuUsername(userName);

        if(user.getId()==contact.getUser().getId()) {


            contact.setUser(null);
            this.contactRepository.delete(contact);
            session.setAttribute("message", new Message("Contact Deleted Successfully", "alert-success"));
        }
            return "redirect:/user/show-contacts/0";

    }

    @PostMapping("/update-contact/{cid}")
    public String updateContact(@PathVariable("cid")Integer cid, Model m){

        Optional<Contact>contactOptional=contactRepository.findById(cid);
        Contact contact=contactOptional.get();

        m.addAttribute("title","Update Contact");
        m.addAttribute("contact",contact);
        return "Normal/update_form";
    }

    @PostMapping("/process-update")
    public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,Model m ,HttpSession session,Principal principal){
      try {
          if(!file.isEmpty()){
              //image work
          }
          String userName=principal.getName();
          User user=this.userRepository.getUserBuUsername(userName);


          contact.setUser(user);
          contactRepository.deleteById(contact.getCid());



          //System.out.println(contact.getCid());



                    this.contactRepository.save(contact);

      }catch (Exception e){
          e.printStackTrace();
      }

        System.out.println("contact name"+contact.getName());
        System.out.println("contact Id "+contact.getCid());
        return "/user/ "+ contact.getCid()+"/contact/";
    }





}
