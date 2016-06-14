package net.hajar.mybus;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;


public class Validator {


    public static boolean isNameValid ( Context currentContext, CharSequence name )
    {
        if( name.toString().isEmpty() )
        {
            Toast.makeText(currentContext,"Name can't be Empty", Toast.LENGTH_LONG).show();
            return false;
        }

        if( !Character.isUpperCase( name.charAt( 0 ) ) )
        {
            Toast.makeText( currentContext, "Name Shoud start whith upper case letter ", Toast.LENGTH_LONG ).show();
            return false;
        }

        return true;
    }

    public static boolean isEmailValid( Context currentContext, CharSequence email )
    {
        if( email.toString().isEmpty() )
        {
            Toast.makeText( currentContext, "Email cannot be empty", Toast.LENGTH_LONG ).show();
            return false;
        }

        if( !Patterns.EMAIL_ADDRESS.matcher( email ).matches() )
        {
            Toast.makeText( currentContext, "Invalid Email format", Toast.LENGTH_LONG ).show();
            return false;
        }

        return true;
    }

    public static boolean isPasswordValid( Context currentContext, CharSequence password )
    {
        if( password.toString().isEmpty() )
        {
            Toast.makeText( currentContext, "Passward connot be empty", Toast.LENGTH_LONG ).show();
            return false;
        }

        return true;
    }


    public static boolean isphoneValid( Context currentContext, CharSequence phone )
    {

        if( !Patterns.PHONE.matcher( phone ).matches() )
        {
            Toast.makeText( currentContext, "Invalid phone format", Toast.LENGTH_LONG ).show();
            return false;
        }

        return true;
    }}
