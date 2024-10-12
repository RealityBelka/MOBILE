package ru.gozerov.common.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ru.gozerov.biometrichack.R
import ru.gozerov.biometrichack.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                getColor(ru.gozerov.core.R.color.transparent),
                getColor(ru.gozerov.core.R.color.white)
            )
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        intent?.let {
            if (it.action == Intent.ACTION_VIEW) {
                navController.handleDeepLink(it)
            }
        }
    }


}