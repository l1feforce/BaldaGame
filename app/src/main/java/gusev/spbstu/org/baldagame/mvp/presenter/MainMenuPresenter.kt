package gusev.spbstu.org.baldagame.mvp.presenter

import android.content.SharedPreferences
import android.support.v4.app.FragmentManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel
import gusev.spbstu.org.baldagame.mvp.views.MainMenuView
import gusev.spbstu.org.baldagame.ui.NewGameFragment

@InjectViewState
class MainMenuPresenter: MvpPresenter<MainMenuView>() {

    fun newGameIsClicked(supportFragmentManager: FragmentManager) {
        val newGameFragment = NewGameFragment()
        newGameFragment.show(supportFragmentManager, "newGame")
    }

    fun preparePrefs(sharedPrefs: SharedPreferences) {
        GameFieldModel.prefs = sharedPrefs
        GameFieldModel.dictionariesInit()
    }

    fun settingsIsClicked() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun exitIsClicked() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onlineGameIsClicked() {
        viewState.startAuthActivity()
    }

}