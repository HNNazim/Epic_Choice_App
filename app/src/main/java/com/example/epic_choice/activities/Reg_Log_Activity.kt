package com.example.epic_choice.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.epic_choice.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Reg_Log_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reg_log_activity)
    }

}
