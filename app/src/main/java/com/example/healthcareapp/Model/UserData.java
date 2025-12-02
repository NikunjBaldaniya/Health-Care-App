package com.example.healthcareapp.Model;

public class UserData {
    public String name, email, password, gender, age, bloodGroup, height, weight, activityLevel, avtar;

    public UserData() {}

    public UserData(String name, String email, String password, String gender, String age,
                    String bloodGroup, String height, String weight, String activityLevel) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.height = height;
        this.weight = weight;
        this.activityLevel = activityLevel;

        // Generate profile character
        if (name != null && !name.isEmpty()) {
            this.avtar = name.substring(0, 1).toUpperCase();
        } else {
            this.avtar = "?";
        }
    }
}


