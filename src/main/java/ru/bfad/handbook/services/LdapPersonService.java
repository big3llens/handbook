package ru.bfad.handbook.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.bfad.handbook.dto.*;
import ru.bfad.handbook.models.*;
import ru.bfad.handbook.repositories.*;

import javax.naming.InvalidNameException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LdapPersonService {

    @Autowired
    LdapPersonRepository ldapPersonRepository;
    @Autowired
    MaillistRepositories maillistRepositories;
    @Autowired
    PersonRepositories personRepositories;
    @Autowired
    PersonMaillistRepositories personMaillistRepositories;
    @Autowired
    FromRepository fromRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public LdapPersonService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ListsInit getAllMaillists(){
        return new ListsInit(mapMaillistToDto(maillistRepositories.findAll()),
                fromRepository.findAll());
    }

    public List<PersonRequest> getAllPersonWithEmail(String nameSpace) throws InvalidNameException {
        return ldapPersonRepository.getAllPersonWithEmail(nameSpace);
    }

    public void saveMaillist(Mailslist mailList){
        maillistRepositories.save(mailList);
    }

    public void addPersonToMailslist(PersonToMailslist emailToMailslist){
        if(!personRepositories.existsByEmail(emailToMailslist.getEmail())) personRepositories.save(new Person(emailToMailslist.getEmail(), emailToMailslist.getFullName()));
        Integer idPerson = personRepositories.findPersonByEmail(emailToMailslist.getEmail()).orElseThrow(() -> new RuntimeException("emailа {" + emailToMailslist.getEmail() +"} в базе нет")).getId();
        personMaillistRepositories.save(new PersonMailslist(emailToMailslist.getId(), idPerson));

    }

    public Collection<PersonRequest> getEmployeesOffTheMailslist(String nameSpace, Integer mailslistId){
        try {
            Collection<PersonRequest> personsFromTheCompany = ldapPersonRepository.getAllPersonWithEmail(nameSpace);
            Collection<PersonRequest> personsInTheList = mapPersonToPersonRequest(maillistRepositories.findById(mailslistId).orElseThrow().getPersons());

            System.out.println("222: **********************************************************************************");
            System.out.println("222: " + personsFromTheCompany);
            System.out.println("222: ID листа: " + mailslistId);
            System.out.println("222: " + personsInTheList);
            System.out.println("222: **********************************************************************************");

            Collection<PersonRequest> p = createRemainingPersonList(personsFromTheCompany, personsInTheList);
            System.out.println("333: " +  p);
            return p;
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public List<PersonRequest> getMailslist (MailslistDto mailslist){
//        return mapPersonToPersonRequest(maillistRepositories.findById(mailslist.getId()).orElseThrow(() -> new RuntimeException("Листа с названием: {" + mailslist.getName() + "} в базе нет")).getPersons());
//    }

    public EmailsRequest getMailslist (MailslistDto mailslist){
        List<PersonRequest> persons = mapPersonToPersonRequest(maillistRepositories.findById(mailslist.getId()).orElseThrow(() -> new RuntimeException("Листа с названием: {" + mailslist.getName() + "} в базе нет")).getPersons());
        return new EmailsRequest(persons, getEmailsFromPersonsList(persons));
    }


    public MessageTo createMessageTo (MessageFrom message){
        return new MessageTo(message.getFrom(), message.getSubject(), message.getText(),
                converPersonsToEmails(maillistRepositories.findById(message.getMailslistId()).orElseThrow().getPersons()), message.getDeliveryMethod());
    }

    public void sendMessage (MessageFrom message, String principal){
        String url = "http://ismaks.bfa-d.ru:31880/api/send_mail";

//        System.out.println("Principal: " + principal.getName());
        MessageTo messageTo = createMessageTo(message);

//        System.out.println("list: " + maillistRepositories.findById(message.getMailslistId()).orElseThrow(() -> new RuntimeException("лист: " + message.getMailslistId() + "не найден")).toString());
//
//        System.out.println("collections: " + maillistRepositories.findById(message.getMailslistId()).orElseThrow(() -> new RuntimeException("лист: " + message.getMailslistId() + "не найден")).getPersons().toString());

//        String to = getEmailsFromPersonsList(mapPersonToPersonRequest(maillistRepositories.findById(message.getMailslistId()).orElseThrow(() -> new RuntimeException("лист: " + message.getMailslistId() + "не найден")).getPersons()));
//
//        System.out.println("to: " + to);
        Message message2 = new Message(fromRepository.findFromByEmail(messageTo.getFrom()),
                messageTo.getSubject(), messageTo.getText(), userRepository.findUserByUsername(principal).orElseThrow(),
                "sidorovapp@bfa-d.ru, ShishkinaOE@bfa-d.ru, sidorenkoia@bfa-d.ru, EliseevaYV@bfa-d.ru, sarukhanyanmg@bfa-d.ru");

        System.out.println("***************ФОРМИРУЕМ ПИСЬМО ДЛЯ СОХРАНЕНИЯ***************");
        if (maillistRepositories.findById(message.getMailslistId()).orElseThrow(() -> new RuntimeException("лист: " + message.getMailslistId() + "не найден")).toString() != null){
            Message message1 = new Message(fromRepository.findFromByEmail(messageTo.getFrom()),
                    messageTo.getSubject(), messageTo.getText(), userRepository.findUserByUsername(principal).orElseThrow(),
                    getEmailsFromPersonsList(mapPersonToPersonRequest(maillistRepositories.findById(message.getMailslistId()).orElseThrow(() -> new RuntimeException("лист: " + message.getMailslistId() + "не найден")).getPersons())));
            System.out.println("ПИСЬМО ДЛЯ СОХРАНЕНИЯ В БАЗУ: " + message1.toString());
            messageRepository.save(message2);
        }
        else System.out.println("ОТПРАВЛЯЮТ В ПУСТОЙ ЛИСТ!!");


        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        MappingJackson2HttpMessageConverter converter2 = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
        messageConverters.add(converter);
        messageConverters.add(converter2);
        restTemplate.setMessageConverters(messageConverters);

        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/json; charset=utf-8");

        HttpEntity<MessageTo> entity = new HttpEntity<>(messageTo, headers);

        System.out.println("зашел");
        System.out.println(message);
        System.out.println(messageTo);
//        System.out.println(restTemplate.postForObject(url, entity, SagalovResponse.class));
    }

    public void saveFileFromMessage(MultipartFile file){
        System.out.println("");
        try {
            byte[] uploadingFileContent = file.getBytes();
            File uploadingFile = new File("C:\\Data\\uploadFiles");
            String uploadFilePath = generatedFileName() + file.getOriginalFilename();
            Path path = Paths.get(uploadFilePath);
            Files.write(path, uploadingFileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<MailslistDto> mapMaillistToDto (List<Mailslist> maillists){
        List<MailslistDto> maillistDtos = new ArrayList<>();
        for (Mailslist m: maillists) {
            maillistDtos.add(new MailslistDto(m.getId(), m.getName()));
        }
        return maillistDtos;
    }

    private Collection<String> converPersonsToEmails (Collection<Person> persons){
        Collection<String> emails = new ArrayList<>();
        for (Person p: persons) {
            emails.add(p.getEmail());
        }
        return emails;
    }

    private List<PersonRequest> mapPersonToPersonRequest (Collection<Person> personslist){
        List<PersonRequest> personsRequestList = new ArrayList<>();
        for (Person m: personslist) {
            personsRequestList.add(new PersonRequest(m.getName(), m.getEmail()));
        }
        return personsRequestList;
    }

    private Collection<PersonRequest> createRemainingPersonList (Collection<PersonRequest> allPersons, Collection<PersonRequest> personsInTheMailslist){
        Collection<PersonRequest> remainingPersons = new ArrayList<>(allPersons);
        for (PersonRequest ap: allPersons) {
            for (PersonRequest personInList: personsInTheMailslist) {
                if(ap.getEmail().equals(personInList.getEmail())) {
                    remainingPersons.remove(ap);
                    break;
                }
            }
        }
        return remainingPersons;
    }
    private String generatedFileName (){
        StringBuilder sb =  new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

//        sb.append("C:\\Data\\uploadFiles\\").append(calendar.getTime());
        sb.append("C:\\Data\\uploadFiles\\");
        return sb.toString().replaceAll("\\s", "");
    }

    private String getEmailsFromPersonsList(List<PersonRequest> persons){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < persons.size(); i++) {
            sb.append(persons.get(i).getEmail());
            if(i < persons.size() - 1) sb.append(", ");
        }
        System.out.println("РЕЗУЛЬТАТ ПРИВЕДЕНИЯ АДРЕСОВ К СТРОКЕ: " + sb.toString());
        return sb.toString();
    }

    private String fromToString(From from){
        return from.getEmail();
    }
}
