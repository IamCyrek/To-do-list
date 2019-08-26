package com.example.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(User.class)
public class User_ {

    public static volatile SingularAttribute<User, Long> id;

    public static volatile SingularAttribute<User, String> name;

    public static volatile SingularAttribute<User, String> email;

    public static volatile SingularAttribute<User, String> password;

    public static volatile SingularAttribute<User, Date> createdAt;

}
