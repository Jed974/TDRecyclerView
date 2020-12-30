package com.example.tdrecyclerview.authentication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.tdrecyclerview.MainActivity
import com.example.tdrecyclerview.R
import com.example.tdrecyclerview.network.Api
import com.example.tdrecyclerview.network.UserService
import com.example.tdrecyclerview.userinfo.UserInfoActivity
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        //return super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var buttonSignin = view.findViewById<Button>(R.id.signin_button)
        var fieldMail = view.findViewById<EditText>(R.id.email_field)
        var fieldPassword = view.findViewById<EditText>(R.id.pwd_field)
        buttonSignin.setOnClickListener(){
            if(fieldMail.text.toString().equals("")){
                fieldMail.highlightColor = Color.RED
            }else if(fieldPassword.text.toString().equals("")){
                fieldPassword.highlightColor = Color.RED
            }else{
                lifecycleScope.launch {
                    val token = Api.INSTANCE.userService.login(LoginForm(fieldMail.text.toString(), fieldPassword.text.toString())).body()?.token
                    getDefaultSharedPreferences(context).edit{
                        putString(SHARED_PREF_TOKEN_KEY, token)
                    }
                    val intent = Intent(activity, MainActivity::class.java)
                    (activity as AuthenticationActivity).changeActivity(intent)
                }
            }
        }
    }

}