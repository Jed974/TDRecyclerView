package com.example.tdrecyclerview.authentication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tdrecyclerview.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.tdrecyclerview.MainActivity
import com.example.tdrecyclerview.network.Api
import kotlinx.coroutines.launch

class SignupFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        //return super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_signup, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var cancelButton = view.findViewById<Button>(R.id.cancel_signup_button)
        cancelButton.setOnClickListener(){
            findNavController().navigate(R.id.action_signupFragment_to_authenticationFragment)
        }

        var buttonSignup = view.findViewById<Button>(R.id.signup_button)
        var fieldFirstName = view.findViewById<EditText>(R.id.firstname_field_signup)
        var fieldLastName = view.findViewById<EditText>(R.id.lastname_field_signup)
        var fieldMail = view.findViewById<EditText>(R.id.email_field_signup)
        var fieldPwd = view.findViewById<EditText>(R.id.pwd_field_signup)
        var fieldPwdConfirm = view.findViewById<EditText>(R.id.pwd_confirm_field_signup)
        buttonSignup.setOnClickListener(){
            var signup = true
            if(fieldFirstName.text.toString().equals("")){
                signup = false;
                fieldFirstName.setBackgroundColor(Color.RED)
            }else{
                fieldFirstName.setBackgroundColor(Color.WHITE)
            }
            if(fieldLastName.text.toString().equals("")){
                signup = false;
                fieldLastName.setBackgroundColor(Color.RED)
            }else{
                fieldLastName.setBackgroundColor(Color.WHITE)
            }
            if(fieldMail.text.toString().equals("")){
                signup = false;
                fieldMail.setBackgroundColor(Color.RED)
            }else{
                fieldMail.setBackgroundColor(Color.WHITE)
            }
            if(fieldPwd.text.toString().equals("")){
                signup = false;
                fieldPwd.setBackgroundColor(Color.RED)
            }else{
                fieldPwd.setBackgroundColor(Color.WHITE)
            }
            if(!fieldPwdConfirm.text.toString().equals(fieldPwd.text.toString())){
                signup = false;
                fieldPwdConfirm.setBackgroundColor(Color.RED)
            }else{
                fieldPwdConfirm.setBackgroundColor(Color.WHITE)
            }
            if(signup){
                lifecycleScope.launch{
                    val token = Api.INSTANCE.userService.signup(SignupForm(
                            fieldFirstName.text.toString(),
                            fieldLastName.text.toString(),
                            fieldMail.text.toString(),
                            fieldPwd.text.toString()
                    )).body()?.token
                    if(token != null) {
                        PreferenceManager.getDefaultSharedPreferences(context).edit {
                            putString(SHARED_PREF_TOKEN_KEY, token)
                        }
                        val intent = Intent(activity, MainActivity::class.java)
                        (activity as AuthenticationActivity).changeActivity(intent)
                    }else{
                        Toast.makeText(context, "Can't sign up !", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}