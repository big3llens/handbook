package ru.bfad.handbook.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import ru.bfad.handbook.dto.PersonRequest;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;
import java.util.List;

@Service
public class LdapPersonRepository {

    @Autowired
    private LdapTemplate ldapTemplate;

    private String filter = "(&(objectclass=person)(mail=*))";
//    private String filter = "(mail=*)";
//    private String filter = "(&(mail=*)(name=backuper))";
//    private String filter = "(name=backuper)";
//    private String filter = "(&(objectClass=person)(mail!null))";
//    private String filter = "(&(&(objectclass=person)(cn=*))!(mail=*)))";
//    private String filter = "(distinguishedName=cn=backuper,cn=users,dc=bfad,dc=ru)";
    private String base = "dc=bfad,dc=ru";
//    private String domenName = "ou=it,ou=bfa users,dc=bfad,dc=ru";
//    private String domenName = "cn=users,dc=bfad,dc=ru";
    private String domenName = "cn=users,dc=bfad,dc=ru";


//    public List<String> getAllPersonNames() {
//        ldapTemplate.setIgnorePartialResultException(true);
//        return ldapTemplate.search(
//                base, filter,
//                new AttributesMapper<String>() {
//                    public String mapFromAttributes(Attributes attrs)
//                            throws NamingException {
//                        return (String) attrs.get("mail").get();
//                    }
//                });
//    }

//    public List<Person> getAllPersonNames() {
//        ldapTemplate.setIgnorePartialResultException(true);
//        return ldapTemplate.search(
//                base, filter,
//                new PersonAttributesMapper());
//    }

    public List<PersonRequest> getAllPersonWithEmail(String nameSpase) throws InvalidNameException {
        ldapTemplate.setIgnorePartialResultException(true);
        LdapName ldapName = new LdapName(nameSpase);
        return ldapTemplate.search(
                ldapName, filter,
                new PersonAttributesMapper());
    }

//    public List<String> getAllPersonWithEmails() {
//        ldapTemplate.setIgnorePartialResultException(true);
//        LdapQuery query = query()
//                .attributes("cn", "sn")
//                .where("objectclass").is("person")
//                .and("instanceType").is("4");
//        return ldapTemplate.search(
//                query,
//                new AttributesMapper<String>() {
//                    public String mapFromAttributes(Attributes attrs)
//                            throws NamingException {
//                        return (String) attrs.get("mail").get();
//                    }
//                });
//    }
//
//    public List<PersonRequest> getAllPersons() {
//        ldapTemplate.setIgnorePartialResultException(true);
//        return ldapTemplate.search(query()
//                .where("objectclass").is("person"), new PersonAttributesMapper());
//    }

    private class PersonAttributesMapper implements AttributesMapper<PersonRequest> {
        public PersonRequest mapFromAttributes(Attributes attrs) throws NamingException {
            PersonRequest person = new PersonRequest();
            person.setFullName((String)attrs.get("name").get());
            person.setEmail((String)attrs.get("mail").get());
            return person;
        }
    }
}
