package gusev.spbstu.org.baldagame.mvp.views

import com.arellomobile.mvp.MvpView

interface PauseMenuView: MvpView {
    fun startNewActivity()
    fun exit()
    fun restartTimer()
}