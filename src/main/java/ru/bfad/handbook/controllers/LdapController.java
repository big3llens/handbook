package ru.bfad.handbook.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.bfad.handbook.dto.*;
import ru.bfad.handbook.models.Mailslist;
import ru.bfad.handbook.services.LdapPersonService;


import javax.naming.InvalidNameException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/ldap")
@RequiredArgsConstructor
@CrossOrigin
public class LdapController {

    @Autowired
    private LdapPersonService ldapPersonService;

//    private final RestTemplate restTemplate;

//    @Autowired
//    public LdapController(RestTemplateBuilder restTemplateBuilder){
//        this.restTemplate = restTemplateBuilder.build();
//    }

    @GetMapping("/persons")
    public List<PersonRequest> getAllPersonWithEmail (@RequestParam(name = "nameSpase") String nameSpase) throws InvalidNameException {
        return ldapPersonService.getAllPersonWithEmail(nameSpase);
    }

    @GetMapping("/mailslists")
    public ListsInit getAllMaillist () {
        return ldapPersonService.getAllMaillists();
    }

    @GetMapping("/mailslist")
    public EmailsRequest getMaillist (@RequestParam(name = "mailslistId")Integer id, @RequestParam(name = "mailslistName")String name) {
        return ldapPersonService.getMailslist(new MailslistDto(id, name));
    }


    @PostMapping("/maillist")
    public void addMaillist(@RequestBody Mailslist mailList){
        ldapPersonService.saveMaillist(mailList);
    }

    @PostMapping("/person")
    public void addPersonToMailsList(@RequestParam(name = "email") String email,
                                     @RequestParam(name = "selectedMailslistId") Integer selectedMailslistId, @RequestParam(name = "fullName") String fullName){
        System.out.println("000: "+ new PersonToMailslist(selectedMailslistId, email, fullName));
        ldapPersonService.addPersonToMailslist(new PersonToMailslist(selectedMailslistId, email, fullName));
    }

    @GetMapping("/employeesOffTheMailslist")
    public Collection<PersonRequest> getEmployeesOffTheMailslist (@RequestParam(name = "mailslistId") Integer mailslistId, @RequestParam(name = "nameSpace") String nameSpace) {
        System.out.println("111: Понял-принял");
        System.out.println("111: " + mailslistId + " ");
        return ldapPersonService.getEmployeesOffTheMailslist(nameSpace, mailslistId);
    }

    @PostMapping("/message")
//    public void sendMessage(@RequestBody MessageFrom message, @CurrentSecurityContext(expression = "authentication.principal")
//            Principal principal){
        public void sendMessage(@RequestBody MessageFrom message){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            String username = ((UserDetails)principal).getUsername();
//            System.out.println("if: " + username);
//        } else {
//            String username = principal.toString();
//            System.out.println("else: " + username);
//        }
//        System.out.println(principal.toString());
        if(!principal.toString().equals("anonymousUser")) ldapPersonService.sendMessage(message, principal.toString());
    }

//    @PostMapping("/message")
//    public void sendMessage(@RequestBody MessageFrom message){
////        String url = "http://ismaks.bfa-d.ru:31880/api/send_mail";
//        String url = "http://10.160.1.115/api/send_mail";
//
//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
//        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_XML));
//        messageConverters.add(converter);
//        restTemplate.setMessageConverters(messageConverters);
//
//        HttpHeaders headers = new HttpHeaders();
//        // set `content-type` header
////        headers.setContentType(MediaType.APPLICATION_JSON);
//        // set `accept` header
////        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
////        headers.set("Content-Type", "application/json; charset=utf-8");
//        headers.setContentType("");
//        HttpEntity<MessageTo> entity = new HttpEntity<>(ldapPersonService.createMessageTo(message), headers);
//
////        ResponseEntity<MessageTo> respEntity = this.restTemplate.postForObject(url, entity, MessageTo.class);
//        System.out.println("зашел");
//        restTemplate.postForObject(url, entity, MessageTo.class);
//
//    }

    @GetMapping("/test")
    public List<PersonRequest> test(){
        try {
            return ldapPersonService.getAllPersonWithEmail("ou=externalcompanies,dc=bfad,dc=ru");
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        return null;
    }

//    @PostMapping("/file")
//    public void uploadFile(@RequestParam(name = "file") MultipartFile file){
//        System.out.println(file.getName());
//
//    }

    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file){

        System.out.println(file.getName());

        ldapPersonService.saveFileFromMessage(file);

        return ResponseEntity.ok("Всё хорошо дошло, СПС!!");
    }

    @GetMapping("/testButton")
    public void testButton(@RequestParam(name = "email") String email){
        System.out.println(email);
    }
}
