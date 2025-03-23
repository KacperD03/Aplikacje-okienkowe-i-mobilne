package com.test.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassContainer {
    private static ClassContainer instance = null;
    private final Map<String, ClassEmployee> groups = new HashMap<String, ClassEmployee>();

    private ClassContainer(){}

    public static ClassContainer getInstance(){
        if(instance == null) {
            instance = new ClassContainer();
        }
        return instance;
    }

    public void addClass(String name, double size) throws Exception{
        try {
            this.groups.put(name, new ClassEmployee(name, (int)size));
        } catch (Exception e){
            throw new Exception(e.getMessage());
//            System.out.println(e.getMessage());
        }
    }

    public ClassEmployee getGroup(String name){
        return groups.get(name);
    }

    public void removeClass(String name){
        this.groups.remove(name);
    }

    public List<ClassEmployee> findEmpty(){
        List<ClassEmployee> emptyGroups = new ArrayList<>();
        for(Map.Entry<String, ClassEmployee> entry : this.groups.entrySet()){
            if(entry.getValue().getCurrentSize() == 0) emptyGroups.add(entry.getValue());
        }
        return emptyGroups;
    }

//    public void summary(){
//        for(Map.Entry<String, ClassEmployee> entry : this.groups.entrySet()){
//            System.out.printf("%s, grupa zapelniona w %.2f%%\n", entry.getValue().getGroupName(),
//                    (float)entry.getValue().getCurrentSize() / entry.getValue().getMaxEmployees() * 100);
//        }
//    }

    public Map<String, ClassEmployee> getGroups() {
        return groups;
    }
}
