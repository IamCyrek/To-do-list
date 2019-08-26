package com.example.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Task.class)
public class Task_ {

    public static volatile SingularAttribute<Task, Long> id;

    public static volatile SingularAttribute<Task, String> content;

    public static volatile SingularAttribute<Task, Integer> priority;

    public static volatile SingularAttribute<Task, Date> creationTime;

    public static volatile SingularAttribute<Task, Boolean> isRemoved;

    public static volatile SingularAttribute<Task, User> user;

}
