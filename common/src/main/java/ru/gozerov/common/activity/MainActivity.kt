package ru.gozerov.common.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ru.gozerov.common.R.id.nav_host_fragment
import ru.gozerov.common.databinding.ActivityMainBinding
import ru.gozerov.core.R

class MainActivity : AppCompatActivity()/*, Navigator*/ {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                getColor(R.color.transparent),
                getColor(R.color.white)
            )
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        intent?.let {
            if (it.action == Intent.ACTION_VIEW) {
                navController.handleDeepLink(it)
            }
        }
    }

    /* override fun launch(screen: Screen) {
         val navController = findNavController(R.id.nav_host_fragment)
         when (screen) {
             Screen.VoiceRecord -> {
                 navController.navigate(R.id.action_biometricFragment_to_voiceFragment)
             }
         }

     }*/

}