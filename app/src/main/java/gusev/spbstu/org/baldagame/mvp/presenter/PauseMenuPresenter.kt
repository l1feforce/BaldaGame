package gusev.spbstu.org.baldagame.mvp.presenter

import android.support.v4.app.FragmentManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import gusev.spbstu.org.baldagame.mvp.views.PauseMenuView
import gusev.spbstu.org.baldagame.ui.NewGameFragment

@InjectViewState
class PauseMenuPresenter : MvpPresenter<PauseMenuView>() {
    fun restartGameIsClicked() {
        print("lesha pidor")
    }

    fun newGameFragmentIsClicked(supportFragmentManager: FragmentManager?) {
        val newGameFragment = NewGameFragment()
        newGameFragment.show(supportFragmentManager, "newGame")
    }

    fun continueGameIsClicked() {
        viewState.restartTimer()
    }

    fun backToMenuIsClicked() {
        viewState.startNewActivity()
    }

    fun exitIsClicked() {
        viewState.exit()
    }

}