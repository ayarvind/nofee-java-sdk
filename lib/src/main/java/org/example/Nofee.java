package org.example;

import java.lang.reflect.InvocationTargetException;

public class Nofee {
    private String authToken;
    private String projectID;
    public Nofee(String authToken){
        this.authToken = authToken;
    }
    public void useProject(String projectID){
        this.projectID = projectID;
    }
    public String getProjectID(){
        return this.projectID;
    }
    public String getAuthToken(){
        return this.authToken;
    }
    public Provider getProvider(String provider){
        Property property = new Property();
        String providerClass = property.get(provider);
        try {
            Class<?> clazz = Class.forName(providerClass);
            return (Provider) clazz.getConstructor(String.class).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    

}
