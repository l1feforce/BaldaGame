package gusev.spbstu.org.baldagame.ui


import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.mvp.presenter.PauseMenuPresenter
import gusev.spbstu.org.baldagame.mvp.views.PauseMenuView
import kotlinx.android.synthetic.main.fragment_pause_menu.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.support.v4.startActivity




class PauseMenuFragment : MvpAppCompatDialogFragment(), PauseMenuView {

    @InjectPresenter
    lateinit var presenter: PauseMenuPresenter
    lateinit var customView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return customView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        customView = context!!.layoutInflater.inflate(R.layout.fragment_pause_menu, null)
        val dialog = AlertDialog.Builder(activity)
                .setView(customView)
        return dialog.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        val back = ColorDrawable(Color.WHITE)
        val inset = InsetDrawable(back, 60)
        dialog.window.setBackgroundDrawable(inset)
        //actions with fragment
        continueGame.setOnClickListener { presenter.continueGameIsClicked() }
        newGameFragment.setOnClickListener { presenter.newGameFragmentIsClicked(activity?.supportFragmentManager) }
        backToMenu.setOnClickListener { presenter.backToMenuIsClicked() }
        exitFragment.setOnClickListener { presenter.exitIsClicked() }
    }

    override fun restartTimer() {
        dialog.dismiss()
        val activity = (activity as GameFieldActivity)
        activity.resumeAfterPause()
        activity.makeFullScreen()

    }

    override fun exit() {
        dialog.dismiss()
        activity?.moveTaskToBack(true)
        activity?.finish()
    }

    override fun startNewActivity() {
        startActivity<MainMenuActivity>()
    }

}
