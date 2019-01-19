package gusev.spbstu.org.baldagame.ui

import android.content.Context
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.mvp.presenter.MainMenuPresenter
import gusev.spbstu.org.baldagame.mvp.views.MainMenuView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainMenuActivity : MvpAppCompatActivity(), MainMenuView {

    @InjectPresenter
    lateinit var presenter:MainMenuPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.preparePrefs(getSharedPreferences("Dictionary", Context.MODE_PRIVATE))

        newGame.setOnClickListener {
            presenter.newGameIsClicked(supportFragmentManager)
        }
        onlineGame.setOnClickListener {
            presenter.onlineGameIsClicked()
        }
        settings.setOnClickListener {
            presenter.settingsIsClicked()
        }
        exit.setOnClickListener {
            presenter.exitIsClicked()
        }
    }

    override fun startAuthActivity() {
        startActivity<AuthActivity>()
    }

}
