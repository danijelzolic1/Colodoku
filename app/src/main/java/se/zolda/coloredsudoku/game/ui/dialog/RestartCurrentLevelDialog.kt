package se.zolda.coloredsudoku.game.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import se.zolda.coloredsudoku.databinding.RestartLevelDialogBinding

class RestartCurrentLevelDialog(
    activity: Activity,
    private val listener: RestartCurrentLevelListener
): Dialog(activity) {
    private lateinit var binding: RestartLevelDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RestartLevelDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancel.setOnClickListener {
            this.dismiss()
        }

        binding.restart.setOnClickListener {
            listener.onRestart()
            this.dismiss()
        }
    }
}

interface RestartCurrentLevelListener{
    fun onRestart()
}