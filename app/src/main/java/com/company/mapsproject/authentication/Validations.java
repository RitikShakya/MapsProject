package com.company.mapsproject.authentication;

import java.util.regex.Pattern;

public class Validations {
    String username;



    public boolean validemail(String useremail){


        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)[com|in]$";
//     A-Z characters are allowed
// a-z characters are allowed
// 0-9 numbers are allowed
// Additionally email can contain dot(.), underscore(_), and dash(-).
// The remaining characters are not allowed.




        Pattern pat = Pattern.compile(emailRegex);

        return pat.matcher(useremail).matches();
    }
    public boolean validnum(String number){


        String phoneRegex= "(0|91)?[7-9][0-9]{9}";
//        The first digit should contain number between 7 to 9.
//        The rest 9 digit can contain any number between 0 to 9.
//        The mobile number can have 11 digits also by including 0 at the starting.
//                The mobile number can be of 12 digits also by including 91 at the starting

        Pattern pat = Pattern.compile(phoneRegex);
        if (number == null)
            return false;
        return pat.matcher(number).matches();


    }

    public boolean validusername(String user){
        this.username=user;
        if(user.length()<4||user.length()>25){
            return false;
        }
        else {
            return true;
        }

    }

    public boolean validpass(String pass){
        username="shakya";

        if(pass.contains(username)){
            return false;
        }else{

            String passRegex = "^[a-z](?=.*[0-9]{2,})(?=.*[A-Z]{2,})(?=.*[@#$%^&-+=()])[A-Za-z0-9@#$%^&-+=()]{8,15}$" ;


            //        (?=.*[0-9] {2 }) represents a digit must occur at least two.

            //        (?=.*[A-Z]{2 }) represents an upper case alphabet that must occur at two.
            //        (?=.*[@#$%^&-+=()] represents a special character that must occur at least once.
            //        (?=\\S+$) white spaces don’t allowed in the entire string.
            //.{8, 20} represents at least 8 characters and at most 15 characters





            Pattern pat = Pattern.compile(passRegex);
            if (pass == null )
                return false;
            return pat.matcher(pass).matches();
        }





    }

}
